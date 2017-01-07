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

import android.os.Environment;
import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.utils.functional.BiCallback;
import com.shc.silenceengine.utils.functional.Promise;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
public class AndroidExternalFilePath extends AndroidFilePath
{
    private File file;

    public AndroidExternalFilePath(String path)
    {
        super(path, Type.EXTERNAL);
        file = new File(Environment.getExternalStorageDirectory(), getPath());
    }

    @Override
    public Promise<Boolean> exists()
    {
        return new Promise<>((resolve, reject) -> resolve.invoke(file.exists()));
    }

    @Override
    public Promise<Boolean> isDirectory()
    {
        return new Promise<>((resolve, reject) -> resolve.invoke(file.isDirectory()));
    }

    @Override
    public Promise<Boolean> isFile()
    {
        return new Promise<>((resolve, reject) -> resolve.invoke(!file.isDirectory()));
    }

    @Override
    public Promise<Void> moveTo(FilePath path)
    {
        return new Promise<>((resolve, reject) ->
        {
            InputStream in = null;
            try
            {
                in = getInputStream();
                OutputStream out = ((AndroidFilePath) path).getOutputStream(false);

                byte[] buffer = new byte[1024];
                int read;
                while ((read = in.read(buffer)) != -1)
                {
                    out.write(buffer, 0, read);
                }
                in.close();

                out.flush();
                out.close();

                delete().then(deleted -> resolve.invoke(null), reject);
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
        BiCallback<Boolean, Boolean> function = (isFile, exists) ->
        {
            if (isFile && !exists)
                getParent().mkdirs();
            else
                file.mkdirs();
        };

        return new Promise<>((resolve, reject) ->
                isFile().then(isFile ->
                        exists().then(exists ->
                        {
                            function.invoke(isFile, exists);
                            resolve.invoke(null);
                        }, reject), reject));
    }

    @Override
    public Promise<Void> createFile()
    {
        return new Promise<>((resolve, reject) ->
        {
            isDirectory().then(isDirectory ->
            {
                if (isDirectory)
                    throw new SilenceException("Cannot convert a directory to a file");

                try
                {
                    file.createNewFile();
                    resolve.invoke(null);
                }
                catch (IOException e)
                {
                    reject.invoke(e);
                }
            });
        });
    }

    @Override
    public String getAbsolutePath()
    {
        return file.getAbsolutePath();
    }

    @Override
    public Promise<Boolean> delete()
    {
        return new Promise<>((resolve, reject) ->
                exists().then(exists ->
                        isDirectory().then(isDirectory ->
                        {
                            if (!exists)
                                throw new SilenceException("Cannot delete non existing file.");

                            if (isDirectory)
                            {
                                listFiles().then(files ->
                                {
                                    // Delete all the children first
                                    for (FilePath filePath : files)
                                        filePath.delete();
                                });
                            }

                            resolve.invoke(file.delete());
                        }, reject), reject));
    }

    @Override
    public void deleteOnExit()
    {
        file.deleteOnExit();
    }

    @Override
    public Promise<Long> sizeInBytes()
    {
        return new Promise<>((resolve, reject) -> resolve.invoke(file.exists() ? file.length() : -1L));
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

                            File[] children = file.listFiles();

                            if (children != null)
                                for (File child : children)
                                    list.add(new AndroidExternalFilePath(path + SEPARATOR + child.getPath().replace(file.getPath(), "")));

                            resolve.invoke(Collections.unmodifiableList(list));
                        }, reject), reject));
    }

    @Override
    public InputStream getInputStream() throws IOException
    {
        return new FileInputStream(file);
    }

    @Override
    public OutputStream getOutputStream(boolean append) throws IOException
    {
        return new FileOutputStream(file, append);
    }
}
