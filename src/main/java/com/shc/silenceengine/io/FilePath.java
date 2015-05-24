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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URISyntaxException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * <p> A FilePath is handle to a file that can be both an external file besides the JAR, or has an absolute path from
 * the root of the filesystem or is a resource packed in some JAR in the classpath. This class uses some tricks to find
 * the other properties of the resources like is that a directory or a file, it's size and also whether it exists. To
 * construct a FilePath, use either {@link #getExternalFile(String)} or {@link #getResourceFile(String)} methods.</p>
 *
 * <pre>
 *     FilePath resource = FilePath.getResourceFile("resources/test.png");
 *     FilePath external = FilePath.getExternalFile("C:/Windows/explorer.exe");
 * </pre>
 *
 * <p> Once you create a FilePath, you can derive new files if that is a directory, or you can get the parent path of a
 * path. You can also get a list of files in a directory denoted by a FilePath instance. To read from a file, or to
 * write to a file, you can get an {@link InputStream} or an {@link OutputStream} by using {@link #getInputStream()} and
 * {@link #getOutputStream()} methods.</p>
 *
 * <p> For external files, this class uses the {@link Files} class of Java NIO package. For resources, there are two
 * ways. If the resource is accessed from an IDE, it uses a {@link File} instance with the URI returned by the class
 * loader's {@link ClassLoader#getResource(String)} method. If the resource is accessed from a running JAR, then an
 * instance of {@link JarFile} is created using reflection, and it's entries are used to access the attributes.</p>
 *
 * @author Sri Harsha Chilakapati
 */
public class FilePath
{
    /**
     * The UNIX style path separator ({@code /}) character. All the path strings are converted to this character when
     * creating an instance with the constructor.
     */
    public static final char SEPARATOR = '/';

    // The path of the resource, and the type
    private String path;
    private Type   type;

    /**
     * Constructs an instance of FilePath by taking a path string, and a type.
     *
     * @param path The path string of the path
     * @param type The type of the file, one of {@link Type#EXTERNAL} or {@link Type#RESOURCE}.
     *
     * @throws IOException When an error occurred reading the attributes of the file path.
     */
    private FilePath(String path, Type type) throws IOException
    {
        this.path = path.replaceAll("\\\\", "/").replaceAll("/+", "/").trim();
        this.type = type;
    }

    /**
     * This method calculates the size of the resource file or the directory. If this path is a directory, then the size
     * is the sum of all the files in this directory. Otherwise the size is calculated either by retrieving the JarEntry
     * of the resource, or by delegating it to the Files class with a path, in case of running from the IDE.
     *
     * @return The size of the FilePath, in bytes.
     *
     * @throws IOException If an error occurred while accessing the file path.
     */
    private long calculateResourceSize() throws IOException
    {
        long size = 0;

        if (isDirectory())
        {
            // If this path is a directory, then the size is the sum of all the files in it.
            List<FilePath> files = listFiles();

            for (FilePath path : files)
                size += path.sizeInBytes();
        }
        else
        {
            // Otherwise, try to find the location of the executable JAR
            File jarFile = new File(FilePath.class.getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20", " "));

            // If this is a JAR file, we are running through an executable JAR. Construct a JarFile instance
            // and use that instance to read the entries from the JAR.
            if (jarFile.isFile())
            {
                JarFile jar = new JarFile(jarFile);

                // Collect all the entries whose name equals with the path
                List<JarEntry> entries = jar.stream().filter(e -> e.getName().equals(path))
                        .collect(Collectors.toList());

                size = entries.get(0).getSize();

                jar.close();
            }
            else
            {
                try
                {
                    // This is a resource, but the file is requested when running from an IDE. We can safely delegate
                    // the work to the Files class now.
                    size = Files.size(Paths.get(FilePath.class.getClassLoader().getResource(path).toURI()));
                }
                catch (URISyntaxException e)
                {
                    SilenceException.reThrow(e);
                }
            }
        }

        return size;
    }

    /**
     * This method finds whether this path exists in the executable JAR file.
     *
     * @return True if the file exists, else false.
     *
     * @throws IOException If an error occurs when accessing the JAR file.
     */
    private boolean existsInJar() throws IOException
    {
        boolean exists = false;

        File jarFile = new File(FilePath.class.getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20", " "));

        if (jarFile.isFile())
        {
            JarFile jar = new JarFile(jarFile);
            exists = jar.stream().filter(e -> e.getName().startsWith(path)).count() > 0;
            jar.close();
        }

        return exists;
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
        if (getType() == Type.EXTERNAL)
            return Files.exists(Paths.get(path));
        else
            try
            {
                return existsInJar() || FilePath.class.getClassLoader().getResource(path) != null;
            }
            catch (IOException e)
            {
                SilenceException.reThrow(e);
            }

        return false;
    }

    public boolean isFile()
    {
        return !isDirectory();
    }

    public boolean isDirectory()
    {
        if (!exists())
            return false;

        if (getType() == Type.EXTERNAL)
            return Files.isDirectory(Paths.get(path));
        else
        {
            boolean isDirectory = false;

            try
            {
                File file = new File(FilePath.class.getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20", " "));

                if (file.isFile())
                {
                    JarFile jarFile = new JarFile(file);

                    isDirectory = jarFile.stream().filter(e -> e.getName().startsWith(path)).count() > 1;

                    jarFile.close();
                }
                else
                {
                    try
                    {
                        isDirectory = Files.isDirectory(Paths.get(FilePath.class.getClassLoader().getResource(path).toURI()));
                    }
                    catch (FileSystemNotFoundException fsnfe)
                    {
                        // We swallow the exception here, as this is a bug with the implementation of ZipFileSystem
                        // See: http://stackoverflow.com/
                        isDirectory = path.endsWith("/");
                    }
                    catch (Exception e)
                    {
                        SilenceException.reThrow(e);
                    }
                }
            }
            catch (Exception e)
            {
                SilenceException.reThrow(e);
            }

            return isDirectory;
        }
    }

    public InputStream getInputStream() throws IOException
    {
        if (isDirectory())
            throw new SilenceException("Cannot read from a directory.");

        if (!exists())
            throw new SilenceException("Cannot read from a non existing file.");

        InputStream inputStream = null;

        switch (type)
        {
            case EXTERNAL:
                inputStream = Files.newInputStream(Paths.get(path));
                break;

            case RESOURCE:
                inputStream = FilePath.class.getClassLoader().getResourceAsStream(path);
        }

        return inputStream;
    }

    public OutputStream getOutputStream() throws IOException
    {
        if (type == Type.RESOURCE)
            throw new SilenceException("Cannot write to a resource file.");

        if (isDirectory())
            throw new SilenceException("Cannot write to a directory.");

        return Files.newOutputStream(Paths.get(path));
    }

    public Reader getReader() throws IOException
    {
        return new InputStreamReader(getInputStream());
    }

    public Writer getWriter() throws IOException
    {
        return new OutputStreamWriter(getOutputStream());
    }

    public void copyTo(FilePath path) throws IOException
    {
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

    public void moveTo(FilePath path) throws IOException
    {
        if (getType() == Type.RESOURCE || path.getType() == Type.EXTERNAL)
            throw new SilenceException("Cannot move resource files!");

        Files.move(Paths.get(this.path), Paths.get(path.getPath()));
    }

    public void mkdirs() throws IOException
    {
        if (getType() == Type.RESOURCE)
            throw new SilenceException("Cannot create resource directories!");

        if (!isDirectory())
            throw new SilenceException("Cannot create directories for a path that denotes a file!");

        Files.createDirectories(Paths.get(path));
    }

    public void createFile() throws IOException
    {
        if (getType() == Type.RESOURCE)
            throw new SilenceException("Cannot create resource files!");

        if (isDirectory())
            throw new SilenceException("Cannot convert a directory to a file");

        Files.createFile(Paths.get(path));
    }

    public FilePath getParent() throws IOException
    {
        String[] parts = path.split("/");

        String path = parts[0];
        for (int i = 1; i < parts.length - 1; i++)
            path += "/" + parts[i] + "/";

        return new FilePath(path + "/", type);
    }

    public FilePath getChild(String path) throws IOException
    {
        if (!isDirectory())
            throw new SilenceException("Cannot get a child for a file.");

        if (!exists())
            throw new SilenceException("Cannot get a child for a non existing directory.");

        return new FilePath(this.path + SEPARATOR + path, type);
    }

    public boolean delete() throws IOException
    {
        if (getType() == Type.RESOURCE)
            throw new SilenceException("Cannot delete resource files.");

        return Files.deleteIfExists(Paths.get(path));
    }

    public void deleteOnExit() throws IOException
    {
        if (getType() == Type.RESOURCE)
            throw new SilenceException("Cannot delete resource files.");

        new File(path).deleteOnExit();
    }

    public String getExtension()
    {
        String[] parts = getPath().split("\\.(?=[^\\.]+$)");
        return parts.length > 1 ? parts[1] : "";
    }

    public long sizeInBytes()
    {
        if (!exists())
            return -1;

        try
        {
            if (getType() == Type.EXTERNAL)
                return Files.size(Paths.get(path));
            else
                return calculateResourceSize();
        }
        catch (IOException e)
        {
            SilenceException.reThrow(e);
        }

        return -1;
    }

    public List<FilePath> listFiles() throws IOException
    {
        if (!isDirectory())
            throw new SilenceException("Cannot list files in a path which is not a directory.");

        if (!exists())
            throw new SilenceException("Cannot list files in a non existing directory.");

        List<FilePath> list = new ArrayList<>();

        if (getType() == Type.EXTERNAL)
        {
            File file = new File(path);

            for (File child : file.listFiles())
                list.add(new FilePath(path + SEPARATOR + child.getPath().replace(file.getPath(), ""), getType()));
        }
        else
        {
            File file = new File(FilePath.class.getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20", " "));

            if (file.isFile())
            {
                JarFile jarFile = new JarFile(file);
                List<JarEntry> entries = jarFile.stream().filter(e -> e.getName().startsWith(path))
                        .collect(Collectors.toList());

                for (JarEntry entry : entries)
                {
                    list.add(new FilePath(entry.getName(), getType()));
                }

                jarFile.close();
            }
            else
            {
                try
                {
                    List<Path> paths = Files.list(Paths.get(FilePath.class.getClassLoader().getResource(path).toURI()))
                            .collect(Collectors.toList());

                    for (Path path : paths)
                    {
                        list.add(new FilePath(this.path + SEPARATOR + path.toFile().getName(), getType()));
                    }
                }
                catch (URISyntaxException e)
                {
                    SilenceException.reThrow(e);
                }
            }
        }

        return Collections.unmodifiableList(list);
    }

    public static FilePath getExternalFile(String path) throws IOException
    {
        return new FilePath(path, Type.EXTERNAL);
    }

    public static FilePath getResourceFile(String path) throws IOException
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
               ", extension='" + getExtension() + "'" +
               ", type=" + type +
               ", isDirectory=" + isDirectory() +
               ", exists=" + exists() +
               ", size=" + sizeInBytes() +
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
               sizeInBytes() == filePath.sizeInBytes() &&
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
        result = 31 * result + (int) (sizeInBytes() ^ (sizeInBytes() >>> 32));
        return result;
    }
}
