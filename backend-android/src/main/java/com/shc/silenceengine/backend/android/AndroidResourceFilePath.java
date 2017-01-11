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

package com.shc.silenceengine.backend.android;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.utils.functional.Promise;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
class AndroidResourceFilePath extends AndroidFilePath
{
    private AssetManager assetManager;

    AndroidResourceFilePath(String path)
    {
        super(path, Type.RESOURCE);
        assetManager = AndroidLauncher.instance.getAssets();
    }

    @Override
    public Promise<Boolean> exists()
    {
        return new Promise<>((resolve, reject) ->
        {
            try
            {
                AssetFileDescriptor desc = assetManager.openFd(path);
                desc.close();
                resolve.invoke(false);
            }
            catch (Exception e)
            {
                resolve.invoke(true);
            }
        });
    }

    @Override
    public Promise<Boolean> isDirectory()
    {
        return new Promise<>((resolve, reject) ->
                exists().then(exists ->
                {
                    if (!exists)
                        resolve.invoke(false);
                    else
                        try
                        {
                            resolve.invoke(assetManager.list(getPath()).length != 0);
                        }
                        catch (IOException e)
                        {
                            resolve.invoke(false);
                        }
                }));
    }

    @Override
    public Promise<Boolean> isFile()
    {
        return new Promise<>((resolve, reject) -> isDirectory().then(isDirectory -> resolve.invoke(!isDirectory)));
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
        throw new SilenceException("Cannot delete a resource upon exit.");
    }

    @Override
    public Promise<Long> sizeInBytes()
    {
        return new Promise<>((resolve, reject) ->
                exists().then(exists ->
                {
                    if (!exists)
                        resolve.invoke(-1L);
                    else
                        try
                        {
                            AssetFileDescriptor fd = assetManager.openFd(getPath());

                            long size = fd.getLength();
                            fd.close();

                            resolve.invoke(size);
                        }
                        catch (IOException e)
                        {
                            resolve.invoke(-1L);
                        }
                }));
    }

    @Override
    public Promise<List<FilePath>> listFiles()
    {
        return new Promise<>((resolve, reject) ->
                isDirectory().then(isDirectory ->
                        exists().then(exists ->
                        {
                            try
                            {
                                if (!isDirectory)
                                    throw new SilenceException("Cannot list files in a path which is not a directory.");

                                if (!exists)
                                    throw new SilenceException("Cannot list files in a non existing directory.");

                                List<FilePath> filePaths = new ArrayList<>();

                                for (String s : assetManager.list(getPath()))
                                    filePaths.add(new AndroidResourceFilePath(getPath() + SEPARATOR + s));

                                resolve.invoke(Collections.unmodifiableList(filePaths));
                            }
                            catch (IOException e)
                            {
                                reject.invoke(e);
                            }

                        }, reject), reject));
    }

    @Override
    public InputStream getInputStream() throws IOException
    {
        return assetManager.open(getPath());
    }

    @Override
    public OutputStream getOutputStream(boolean append) throws IOException
    {
        throw new IOException("Cannot open an OutputStream for resources.");
    }
}
