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

package com.shc.silenceengine.backend.android;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.io.FilePath;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
public class AndroidResourceFilePath extends AndroidFilePath
{
    private AssetManager assetManager;

    public AndroidResourceFilePath(String path)
    {
        super(path, Type.RESOURCE);
        assetManager = ((AndroidDisplayDevice) SilenceEngine.display).activity.getAssets();
    }

    @Override
    public boolean exists()
    {
        try
        {
            AssetFileDescriptor desc = assetManager.openFd(path);
            desc.close();
        }
        catch (Exception e)
        {
            return true;
        }

        return false;
    }

    @Override
    public boolean isDirectory()
    {
        if (!exists())
            return false;

        try
        {
            return assetManager.list(getPath()).length != 0;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean isFile()
    {
        return !isDirectory();
    }

    @Override
    public void moveTo(FilePath path) throws IOException
    {
        throw new IOException("Cannot move an in-jar resource.");
    }

    @Override
    public void mkdirs() throws IOException
    {
        throw new IOException("Cannot create directory inside a jar");
    }

    @Override
    public void createFile() throws IOException
    {
        throw new IOException("Cannot create a file inside jar");
    }

    @Override
    public boolean delete() throws IOException
    {
        throw new IOException("Cannot delete an in-jar resource");
    }

    @Override
    public void deleteOnExit()
    {
        throw new SilenceException("Cannot delete a resource upon exit.");
    }

    @Override
    public long sizeInBytes()
    {
        if (!exists())
            return -1;

        try
        {
            AssetFileDescriptor fd = assetManager.openFd(getPath());

            long size = fd.getLength();
            fd.close();

            return size;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return -1;
    }

    @Override
    public List<FilePath> listFiles() throws IOException
    {
        if (!isDirectory())
            throw new SilenceException("Cannot list files in a path which is not a directory.");

        if (!exists())
            throw new SilenceException("Cannot list files in a non existing directory.");

        List<FilePath> filePaths = new ArrayList<>();

        for (String s : assetManager.list(getPath()))
            filePaths.add(new AndroidResourceFilePath(getPath() + SEPARATOR + s));

        return Collections.unmodifiableList(filePaths);
    }

    @Override
    public InputStream getInputStream() throws IOException
    {
        return assetManager.open(getPath());
    }

    @Override
    public OutputStream getOutputStream() throws IOException
    {
        throw new IOException("Cannot open an OutputStream for resources.");
    }
}
