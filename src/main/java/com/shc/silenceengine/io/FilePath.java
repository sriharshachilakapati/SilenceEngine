/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2015 Sri Harsha Chilakapati
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

package com.shc.silenceengine.io;

import com.shc.silenceengine.core.SilenceException;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Sri Harsha Chilakapati
 */
public class FilePath
{
    public static final char SEPARATOR = File.separatorChar;

    private String path;
    private Type type;

    private boolean directory;
    private boolean exists;

    private FilePath(String path, Type type)
    {
        this.path = path.replaceAll("\\\\", "/").replaceAll("/+", "/").trim();
        this.type = type;

        switch (type)
        {
            case EXTERNAL:
                directory = Files.isDirectory(Paths.get(path));
                exists = Files.exists(Paths.get(path));
                break;

            case RESOURCE:
                directory = this.path.endsWith("/");
                exists = FilePath.class.getResource("/" + path) != null;
                break;
        }
    }

    public String getPath()
    {
        return path;
    }

    public Type getType()
    {
        return type;
    }

    public boolean exists()
    {
        return exists;
    }

    public boolean isDirectory()
    {
        return directory;
    }

    public InputStream getInputStream()
    {
        if (isDirectory())
            throw new SilenceException("Cannot read from a directory.");

        if (!exists())
            throw new SilenceException("Cannot read from a non existing file.");

        InputStream inputStream = null;

        try
        {
            switch (type)
            {
                case EXTERNAL: inputStream = Files.newInputStream(Paths.get(path)); break;
                case RESOURCE: inputStream = FilePath.class.getClassLoader().getResourceAsStream(path);
            }
        }
        catch (Exception e)
        {
            SilenceException.reThrow(e);
        }

        return inputStream;
    }

    public OutputStream getOutputStream()
    {
        if (type == Type.RESOURCE)
            throw new SilenceException("Cannot write to a resource file.");

        if (isDirectory())
            throw new SilenceException("Cannot write to a directory.");

        OutputStream outputStream = null;

        try
        {
            outputStream = Files.newOutputStream(Paths.get(path));
        }
        catch (Exception e)
        {
            SilenceException.reThrow(e);
        }

        return outputStream;
    }

    public FilePath getParent()
    {
        String[] parts = path.split("/");

        String path = parts[0];
        for (int i = 1; i < parts.length - 1; i++)
            path += "/" + parts[i] + "/";

        return new FilePath(path + "/", type);
    }

    public FilePath getChild(String path)
    {
        if (!isDirectory())
            throw new SilenceException("Cannot get a child for a file.");

        if (!exists())
            throw new SilenceException("Cannot get a child for a non existing directory.");

        return new FilePath(this.path + SEPARATOR + path, type);
    }

    public boolean delete()
    {
        if (getType() == Type.RESOURCE)
            throw new SilenceException("Cannot delete resource files.");

        try
        {
            return Files.deleteIfExists(Paths.get(path));
        }
        catch (Exception e)
        {
            SilenceException.reThrow(e);
        }

        return false;
    }

    public static FilePath getExternalFile(String path)
    {
        return new FilePath(path, Type.EXTERNAL);
    }

    public static FilePath getResourceFile(String path)
    {
        return new FilePath(path, Type.RESOURCE);
    }

    public enum Type
    {
        EXTERNAL, RESOURCE
    }

    @Override
    public String toString()
    {
        return "FilePath{" +
               "path='" + path + '\'' +
               ", type=" + type +
               ", isDirectory=" + directory +
               ", exists=" + exists +
               '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FilePath filePath = (FilePath) o;

        return isDirectory() == filePath.isDirectory() &&
               exists() == filePath.exists() &&
               getPath().equals(filePath.getPath()) &&
               getType() == filePath.getType();
    }

    @Override
    public int hashCode()
    {
        int result = getPath().hashCode();
        result = 31 * result + getType().hashCode();
        result = 31 * result + (isDirectory() ? 1 : 0);
        result = 31 * result + (exists() ? 1 : 0);
        return result;
    }
}
