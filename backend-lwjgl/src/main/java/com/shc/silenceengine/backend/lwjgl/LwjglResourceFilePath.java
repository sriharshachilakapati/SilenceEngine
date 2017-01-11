/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 Sri Harsha Chilakapati
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

package com.shc.silenceengine.backend.lwjgl;

import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.utils.functional.Promise;
import com.shc.silenceengine.utils.functional.SimpleCallback;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * @author Sri Harsha Chilakapati
 */
class LwjglResourceFilePath extends LwjglFilePath
{
    LwjglResourceFilePath(String path)
    {
        super(path, Type.RESOURCE);
    }

    private ResourceType getResourceType()
    {
        URL url = LwjglResourceFilePath.class.getClassLoader().getResource(getPath());

        if (url != null && url.toString().contains("jar"))
            return ResourceType.JAR;

        return ResourceType.FILE;
    }

    @Override
    public Promise<Boolean> exists()
    {
        return new Promise<>((resolve, reject) ->
                resolve.invoke(LwjglResourceFilePath.class.getClassLoader().getResource(path) != null));
    }

    @Override
    public Promise<Boolean> isDirectory()
    {
        return new Promise<>((resolve, reject) ->
                exists().then(exists ->
                {
                    if (!exists) resolve.invoke(false);

                    try
                    {
                        if (getResourceType() == ResourceType.FILE)
                            resolve.invoke(Files.isDirectory(Paths.get(getIDEPath())));
                        else
                        {
                            List<JarEntry> entries = getJarEntries();

                            // Size greater than 1 for directories, since they contain files
                            resolve.invoke(entries.size() > 1 || entries.get(0).isDirectory());
                        }
                    }
                    catch (IOException e)
                    {
                        SilenceEngine.log.getRootLogger().error(e);
                        reject.invoke(e);
                    }
                }, reject));
    }

    @Override
    public Promise<Void> moveTo(FilePath path)
    {
        return new Promise<>((resolve, reject) -> reject.invoke(new IOException("Cannot move an in-jar resource.")));
    }

    @Override
    public Promise<Void> mkdirs()
    {
        return new Promise<>((resolve, reject) -> reject.invoke(new IOException("Cannot create directory inside a jar")));
    }

    @Override
    public Promise<Void> createFile()
    {
        return new Promise<>((resolve, reject) -> reject.invoke(new IOException("Cannot create a file inside jar")));
    }

    @Override
    public Promise<Boolean> delete()
    {
        return new Promise<>((resolve, reject) -> reject.invoke(new IOException("Cannot delete an in-jar resource")));
    }

    @Override
    public void deleteOnExit()
    {
        throw new SilenceException("Cannot delete an in-jar resource upon exit.");
    }

    @Override
    public Promise<Long> sizeInBytes()
    {
        return new Promise<>((resolve, reject) ->
                exists().then(exists ->
                {
                    if (!exists)
                        resolve.invoke(-1L);

                    try
                    {
                        if (getResourceType() == ResourceType.FILE)
                            resolve.invoke(Files.size(Paths.get(getIDEPath())));

                        List<JarEntry> entries = getJarEntries();

                        long size = 0;

                        for (JarEntry entry : entries)
                            size += entry.getSize();

                        resolve.invoke(size);
                    }
                    catch (IOException e)
                    {
                        SilenceEngine.log.getRootLogger().error(e);
                        reject.invoke(e);
                    }

                    resolve.invoke(-1L);
                }, reject));
    }

    @Override
    public Promise<List<FilePath>> listFiles()
    {
        return new Promise<>((resolve, reject) ->
        {
            boolean[] values = new boolean[2];

            SimpleCallback function = () ->
            {
                if (!values[0])
                    throw new SilenceException("Cannot list files in a path which is not a directory.");

                if (!values[1])
                    throw new SilenceException("Cannot list files in a non existing directory.");

                List<FilePath> filePaths = new ArrayList<>();

                try
                {
                    if (getResourceType() == ResourceType.FILE)
                    {
                        File file = null;
                        file = new File(getIDEPath());

                        File[] children = file.listFiles();

                        if (children != null)
                        {
                            for (File child : children)
                                filePaths.add(new LwjglResourceFilePath(path + SEPARATOR + child.getPath().replace(file.getPath(), "")));
                        }
                    }
                    else
                    {
                        List<JarEntry> entries = null;
                        entries = getJarEntries();

                        entries.forEach(e -> filePaths.add(FilePath.getResourceFile(e.getName())));
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    reject.invoke(e);
                }

                resolve.invoke(Collections.unmodifiableList(filePaths));
            };

            isDirectory().then(value0 ->
            {
                values[0] = value0;

                exists().then(value1 ->
                {
                    values[1] = value1;
                    function.invoke();

                }, reject);
            }, reject);
        });
    }

    @Override
    public InputStream getInputStream() throws IOException
    {
        return LwjglResourceFilePath.class.getClassLoader().getResourceAsStream(getPath());
    }

    @Override
    public OutputStream getOutputStream(boolean append) throws IOException
    {
        throw new IOException("Cannot open an OutputStream for in-jar resources.");
    }

    private String getIDEPath() throws IOException
    {
        URL url = LwjglResourceFilePath.class.getClassLoader().getResource(getPath());

        if (url == null)
            throw new IOException("Error, path not found.");

        String urlString = url.toString();

        SilenceEngine.Platform platform = SilenceEngine.display.getPlatform();

        if (platform == SilenceEngine.Platform.WINDOWS_32 || platform == SilenceEngine.Platform.WINDOWS_64)
            return URLDecoder.decode(urlString.substring(urlString.indexOf('/') + 1), "UTF-8");
        else
            return "/" + URLDecoder.decode(urlString.substring(urlString.indexOf('/') + 1), "UTF-8");
    }

    private List<JarEntry> getJarEntries() throws IOException
    {
        // Get the JAR file of the resource
        URL url = LwjglResourceFilePath.class.getClassLoader().getResource(getPath());

        if (url == null)
            throw new SilenceException("Error, resource doesn't exist.");

        String jarUrl = url.toString();
        String jarPath = URLDecoder.decode(jarUrl.substring(jarUrl.indexOf('/'), jarUrl.indexOf('!')), "UTF-8");

        // Now get the JarEntry for this path
        JarFile jarFile = new JarFile(new File(jarPath));

        List<JarEntry> entries = jarFile.stream().filter(e -> e.getName().startsWith(path))
                .collect(Collectors.toList());

        jarFile.close();

        if (entries != null)
            return entries;

        throw new IOException("Cannot find the JAR entry for " + getPath() + " in JAR file " + jarPath);
    }

    /**
     * The ResourceType specifies whether the resource file is inside a JAR or on the disk (run from IDE).
     */
    private enum ResourceType
    {
        JAR,  // Resource contained within a JAR file in classpath
        FILE  // Resource is a file on the disk (must be run from the IDE)
    }
}
