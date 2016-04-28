/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2016 Sri Harsha Chilakapati
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.shc.silenceengine.core;

import com.shc.silenceengine.audio.AudioDevice.AudioFormat;
import com.shc.silenceengine.audio.Sound;
import com.shc.silenceengine.audio.openal.ALBuffer;
import com.shc.silenceengine.graphics.Image;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.utils.IDGenerator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Sri Harsha Chilakapati
 */
public class ResourceLoader
{
    private static Map<Class<? extends IResource>, ILoadHelper> loadHelpers = new HashMap<>();

    private Map<FilePath, Class<? extends IResource>> toBeLoaded;

    private Map<Long, FilePath>      idMap;
    private Map<FilePath, IResource> loaded;

    private int done  = 0;
    private int count = 0;

    public ResourceLoader()
    {
        toBeLoaded = new HashMap<>();
        idMap = new HashMap<>();
        loaded = new HashMap<>();
    }

    public static void setHelper(Class<? extends IResource> clazz, ILoadHelper loadHelper)
    {
        loadHelpers.put(clazz, loadHelper);
    }

    private static void imageLoadHelper(FilePath path, ISubmitter<Image> submitter)
    {
        SilenceEngine.io.getImageReader().readImage(path, image -> submitter.submit(image, path));
    }

    private static void textureLoadHelper(FilePath path, ISubmitter<Texture> submitter)
    {
        SilenceEngine.io.getImageReader().readImage(path, image -> {
            submitter.submit(Texture.fromImage(image), path);
            image.dispose();
        });
    }

    private static void alBufferLoadHelper(FilePath path, ISubmitter<ALBuffer> submitter)
    {
        AudioFormat format = AudioFormat.OGG;

        switch (path.getExtension())
        {
            case "ogg":
            case "ogv":
            case "oga":
                format = AudioFormat.OGG;
                break;

            case "wav":
            case "pcm":
            case "wave":
            case "riff":
                format = AudioFormat.WAV;
                break;

            case "mp3":
            case "mp4":
            case "mpg":
                format = AudioFormat.MP3;
                break;

            case "webm":
                format = AudioFormat.WEBM;
        }

        final AudioFormat finalFormat = format;
        SilenceEngine.io.getFileReader().readBinaryFile(path, data ->
                SilenceEngine.audio.readToALBuffer(finalFormat, data, buffer -> submitter.submit(buffer, path)));
    }

    private static void soundLoadHelper(FilePath path, ISubmitter<Sound> submitter)
    {
        alBufferLoadHelper(path, (resource, filePath) -> submitter.submit(new Sound(resource), path));
    }

    public long define(Class<? extends IResource> klass, FilePath path)
    {
        toBeLoaded.put(path, klass);
        count++;

        long id = IDGenerator.generate();
        idMap.put(id, path);

        return id;
    }

    @SuppressWarnings("unchecked")
    public <T extends IResource> T get(long id)
    {
        return (T) loaded.get(idMap.get(id));
    }

    public void start()
    {
        Iterator<FilePath> iterator = toBeLoaded.keySet().iterator();

        while (iterator.hasNext())
        {
            FilePath path = iterator.next();

            Class<? extends IResource> type = toBeLoaded.get(path);

            ILoadHelper<?> loadHelper = loadHelpers.get(type);
            loadHelper.load(path, (resource, path1) -> {
                done++;
                loaded.put(path1, resource);
            });

            iterator.remove();
        }
    }

    public float getPercentage()
    {
        return (float) done / (float) count * 100f;
    }

    public boolean isDone()
    {
        return getPercentage() == 100;
    }

    public void disposeAll()
    {
        for (IResource resource : loaded.values())
            resource.dispose();
    }

    @FunctionalInterface
    public interface ISubmitter<T extends IResource>
    {
        void submit(T resource, FilePath path);
    }

    @FunctionalInterface
    public interface ILoadHelper<T extends IResource>
    {
        void load(FilePath path, ISubmitter<T> submitter);
    }

    static
    {
        setHelper(Image.class, ResourceLoader::imageLoadHelper);
        setHelper(Texture.class, ResourceLoader::textureLoadHelper);
        setHelper(ALBuffer.class, ResourceLoader::alBufferLoadHelper);
        setHelper(Sound.class, ResourceLoader::soundLoadHelper);
    }
}
