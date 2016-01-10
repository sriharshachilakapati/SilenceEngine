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

package com.shc.silenceengine.backend.lwjgl3.io;

import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.utils.Logger;

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
public class Lwjgl3ExternalFilePath extends FilePath
{
    public Lwjgl3ExternalFilePath(String path)
    {
        super(path, Type.EXTERNAL);
    }

    @Override
    public boolean exists()
    {
        return Files.exists(Paths.get(getPath()));
    }

    @Override
    public boolean isDirectory()
    {
        return Files.isDirectory(Paths.get(getPath()));
    }

    @Override
    public boolean isFile()
    {
        return !isDirectory();
    }

    @Override
    public InputStream getInputStream() throws IOException
    {
        return Files.newInputStream(Paths.get(getPath()));
    }

    @Override
    public OutputStream getOutputStream() throws IOException
    {
        return Files.newOutputStream(Paths.get(getPath()));
    }

    @Override
    public void copyTo(FilePath path) throws IOException
    {
        boolean thisIsDirectory = this.isDirectory();
        boolean pathIsDirectory = path.isDirectory();

        if (thisIsDirectory && !pathIsDirectory)
            throw new SilenceException("Cannot copy a directory into a file.");

        if (!thisIsDirectory && pathIsDirectory)
            throw new SilenceException("Cannot copy a file into a directory.");

        if (!exists())
            throw new SilenceException("Cannot copy a non existing file.");

        byte[] buffer = new byte[1024];
        int length;

        try (InputStream inputStream = getInputStream(); OutputStream outputStream = path.getOutputStream())
        {
            while ((length = inputStream.read(buffer)) > 0)
            {
                outputStream.write(buffer, 0, length);
            }
        }
    }

    @Override
    public void moveTo(FilePath path) throws IOException
    {
        Files.move(Paths.get(this.path), Paths.get(path.getPath()));
    }

    @Override
    public void mkdirs() throws IOException
    {
        if (isFile() && !exists())
            getParent().mkdirs();
        else
            Files.createDirectories(Paths.get(path));
    }

    @Override
    public void createFile() throws IOException
    {
        if (isDirectory())
            throw new SilenceException("Cannot convert a directory to a file");

        Files.createFile(Paths.get(path));
    }

    @Override
    public boolean delete() throws IOException
    {
        if (!exists())
            throw new SilenceException("Cannot delete non existing file.");

        if (isDirectory())
        {
            // Delete all the children first
            for (FilePath filePath : listFiles())
                filePath.delete();
        }

        return Files.deleteIfExists(Paths.get(path));
    }

    @Override
    public void deleteOnExit()
    {
        new File(getPath()).deleteOnExit();
    }

    @Override
    public long sizeInBytes()
    {
        try
        {
            if (exists())
                return Files.size(Paths.get(path));
        }
        catch (IOException e)
        {
            Logger.trace(e);
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

        List<FilePath> list = new ArrayList<>();

        File file = new File(path);

        File[] children = file.listFiles();

        if (children != null)
            for (File child : children)
                list.add(new Lwjgl3ExternalFilePath(path + SEPARATOR + child.getPath().replace(file.getPath(), "")));

        return Collections.unmodifiableList(list);
    }
}
