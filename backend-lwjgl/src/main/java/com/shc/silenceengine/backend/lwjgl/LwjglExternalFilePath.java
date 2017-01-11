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

import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.utils.functional.BiCallback;
import com.shc.silenceengine.utils.functional.Promise;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
class LwjglExternalFilePath extends LwjglFilePath
{
    LwjglExternalFilePath(String path)
    {
        super(path, Type.EXTERNAL);
    }

    @Override
    public Promise<Boolean> exists()
    {
        return new Promise<>((resolve, reject) -> resolve.invoke(Files.exists(Paths.get(getPath()))));
    }

    @Override
    public Promise<Boolean> isDirectory()
    {
        return new Promise<>((resolve, reject) -> resolve.invoke(Files.isDirectory(Paths.get(getPath()))));
    }

    @Override
    public Promise<Void> moveTo(FilePath path)
    {
        return new Promise<>((resolve, reject) ->
        {
            try
            {
                Files.move(Paths.get(this.path), Paths.get(path.getPath()));
                resolve.invoke(null);
            }
            catch (IOException e)
            {
                reject.invoke(e);
            }
        });
    }

    @Override
    public Promise<Void> mkdirs()
    {
        return new Promise<>((resolve, reject) ->
        {
            BiCallback<Boolean, Boolean> function = (isFile, exists) ->
            {
                try
                {
                    if (isFile && !exists)
                        getParent().mkdirs();
                    else
                        Files.createDirectories(Paths.get(path));

                    resolve.invoke(null);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    reject.invoke(e);
                }
            };

            isFile().then(isFile -> exists().then(exists -> function.invoke(isFile, exists), reject), reject);
        });
    }

    @Override
    public Promise<Void> createFile()
    {
        return new Promise<>((resolve, reject) ->
        {
            try
            {
                Files.createFile(Paths.get(path));
                resolve.invoke(null);
            }
            catch (IOException e)
            {
                reject.invoke(e);
            }
        });
    }

    @Override
    public Promise<Boolean> delete()
    {
        return new Promise<>((resolve, reject) ->
                exists().then(exists ->
                        isDirectory().then(isDirectory ->
                        {
                            try
                            {
                                if (!exists)
                                    throw new SilenceException("Cannot delete non existing file.");

                                if (isDirectory)
                                {
                                    listFiles().then(files ->
                                    {
                                        // Delete all the children first
                                        files.forEach(FilePath::delete);
                                    }, reject);
                                }

                                Files.deleteIfExists(Paths.get(path));
                                resolve.invoke(null);
                            }
                            catch (IOException e)
                            {
                                reject.invoke(e);
                            }
                        }, reject), reject));
    }

    @Override
    public void deleteOnExit()
    {
        new File(getPath()).deleteOnExit();
    }

    @Override
    public Promise<Long> sizeInBytes()
    {
        return new Promise<>((resolve, reject) ->
                exists().then(exists ->
                {
                    if (exists)
                        try
                        {
                            resolve.invoke(Files.size(Paths.get(path)));
                        }
                        catch (IOException e)
                        {
                            reject.invoke(e);
                        }
                    else
                        resolve.invoke(-1L);
                }, reject));
    }

    @Override
    public Promise<List<FilePath>> listFiles()
    {
        return new Promise<>((resolve, reject) ->
                isDirectory().then(isDirectory ->
                        exists().then(exists ->
                        {
                            if (!isDirectory)
                                throw new SilenceException("Cannot list files in a path which is not a directory.");

                            if (!exists)
                                throw new SilenceException("Cannot list files in a non existing directory.");

                            List<FilePath> list = new ArrayList<>();

                            File file = new File(path);

                            File[] children = file.listFiles();

                            if (children != null)
                                for (File child : children)
                                    list.add(new LwjglExternalFilePath(path + SEPARATOR + child.getPath().replace(file.getPath(), "")));

                            resolve.invoke(Collections.unmodifiableList(list));
                        }, reject), reject));
    }

    @Override
    public InputStream getInputStream() throws IOException
    {
        return Files.newInputStream(Paths.get(getPath()));
    }

    @Override
    public OutputStream getOutputStream(boolean append) throws IOException
    {
        return Files.newOutputStream(Paths.get(getPath()));
    }
}
