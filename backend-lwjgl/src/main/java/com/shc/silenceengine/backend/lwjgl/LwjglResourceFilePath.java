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

package com.shc.silenceengine.backend.lwjgl;

import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.io.FilePath;

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
public class LwjglResourceFilePath extends LwjglFilePath
{
    public LwjglResourceFilePath(String path)
    {
        super(path, Type.RESOURCE);
    }

    public ResourceType getResourceType()
    {
        URL url = LwjglResourceFilePath.class.getClassLoader().getResource(getPath());

        if (url != null && url.toString().contains("jar"))
            return ResourceType.JAR;

        return ResourceType.FILE;
    }

    @Override
    public boolean exists()
    {
        return LwjglResourceFilePath.class.getClassLoader().getResource(path) != null;
    }

    @Override
    public boolean isDirectory()
    {
        if (!exists())
            return false;

        try
        {
            if (getResourceType() == ResourceType.FILE)
                return Files.isDirectory(Paths.get(getIDEPath()));

            List<JarEntry> entries = getJarEntries();

            // Size greater than 1 for directories, since they contain files
            return entries.size() > 1 || entries.get(0).isDirectory();
        }
        catch (IOException e)
        {
            SilenceEngine.log.getRootLogger().error(e);
        }

        return false;
    }

    @Override
    public boolean isFile()
    {
        return !isDirectory();
    }

    @Override
    public InputStream getInputStream() throws IOException
    {
        return LwjglResourceFilePath.class.getClassLoader().getResourceAsStream(getPath());
    }

    @Override
    public OutputStream getOutputStream() throws IOException
    {
        throw new IOException("Cannot open an OutputStream for in-jar resources.");
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
        throw new SilenceException("Cannot delete an in-jar resource upon exit.");
    }

    @Override
    public long sizeInBytes()
    {
        if (!exists())
            return -1;

        try
        {
            if (getResourceType() == ResourceType.FILE)
                return Files.size(Paths.get(getIDEPath()));

            List<JarEntry> entries = getJarEntries();

            long size = 0;

            for (JarEntry entry : entries)
                size += entry.getSize();

            return size;
        }
        catch (IOException e)
        {
            SilenceEngine.log.getRootLogger().error(e);
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

        if (getResourceType() == ResourceType.FILE)
        {
            File file = new File(getIDEPath());

            File[] children = file.listFiles();

            if (children != null)
                for (File child : children)
                    filePaths.add(new LwjglResourceFilePath(path + SEPARATOR + child.getPath().replace(file.getPath(), "")));
        }
        else
        {
            List<JarEntry> entries = getJarEntries();
            entries.forEach(e -> filePaths.add(FilePath.getResourceFile(e.getName())));
        }

        return Collections.unmodifiableList(filePaths);
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
    public enum ResourceType
    {
        JAR,  // Resource contained within a JAR file in classpath
        FILE  // Resource is a file on the disk (must be run from the IDE)
    }
}
