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
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
     */
    private FilePath(String path, Type type)
    {
        this.path = path.replaceAll("\\\\", "" + SEPARATOR).replaceAll("/+", "" + SEPARATOR).trim();
        this.type = type;

        if (type == Type.RESOURCE && this.path.startsWith("/"))
            this.path = this.path.replaceFirst("/", "");
    }

    /**
     * This method creates a {@link FilePath} instance that handles a path to an external file or directory. The path
     * can be either an absolute path, or a relative path. In case of the relative path, the files are expected to be in
     * project root folder when running through an IDE, and besides the JAR file when running from an executable JAR.
     *
     * @param path The path string that specifies the location of the file or directory.
     *
     * @return The FilePath instance that can be used to handle an external file.
     */
    public static FilePath getExternalFile(String path)
    {
        return new FilePath(path, Type.EXTERNAL);
    }

    /**
     * This method creates a {@link FilePath} instance that handles a path to an internal file or directory, which is
     * present in the classpath. This path is always absolute, and starts with the root of classpath, which will be the
     * source directory in case of running from the IDE, or from the root of the JAR file when running from executable
     * JAR file.
     *
     * @param path The path string that specifies the location of the file or directory.
     *
     * @return The FilePath instance that can be used to handle a resource file, which will be packed into the JAR.
     */
    public static FilePath getResourceFile(String path)
    {
        return new FilePath(path, Type.RESOURCE);
    }

    /**
     * Gets the path string of this FilePath instance. This path will be the same as the one that you used to create
     * this object, except that it will be changed to use the separator defined by {@link #SEPARATOR} which works in all
     * the platforms.
     *
     * @return The path string of this FilePath instance.
     */
    public String getPath()
    {
        if (path.trim().endsWith("" + SEPARATOR))
            return path.trim().substring(0, path.lastIndexOf(SEPARATOR));

        return path;
    }

    /**
     * Gets the absolute path string of this FilePath instance. The root of the path will be the root of the filesystem
     * if this path is an external file, or the root of the classpath if this is a JAR file resource.
     *
     * @return The absolute path string of this FilePath instance.
     */
    public String getAbsolutePath()
    {
        if (type == Type.EXTERNAL)
            // For external files, use the File object to get the absolute path
            return new File(path).getAbsolutePath().replaceAll("\\\\", "" + SEPARATOR)
                    .replaceAll("/+", "" + SEPARATOR).trim();
        else
            // For resource files, the path is always absolute, just return it
            return path;
    }

    /**
     * Gets the type of file this FilePath instance resolves to.
     *
     * @return One of {@link Type#EXTERNAL} or {@link Type#RESOURCE}.
     */
    public Type getType()
    {
        return type;
    }

    /**
     * Checks if the file resolved to from this FilePath instance actually exists, or whether it is a hypothetical one.
     *
     * @return True if the file exists, or else False.
     */
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

        // Get the code source as a file
        File jarFile = new File(FilePath.class.getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20", " "));

        if (jarFile.isFile())
        {
            // If this is a file, we are sure that we are running from a JAR file.
            JarFile jar = new JarFile(jarFile);
            exists = jar.stream().filter(e -> e.getName().startsWith(path)).count() > 0;
            jar.close();
        }

        return exists;
    }

    /**
     * Checks if this FilePath is a file, or a directory.
     *
     * @return True if a file, else false.
     */
    public boolean isFile()
    {
        return exists() && !isDirectory();
    }

    /**
     * Checks if this FilePath is a directory, or a file.
     *
     * @return True if a directory, else false.
     */
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
                // Try to get the code source as a file
                File file = new File(FilePath.class.getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20", " "));

                if (file.isFile())
                {
                    // If the source is a JAR file, then this is a directory if it has more entries that starts with
                    // the path of this resource.
                    JarFile jarFile = new JarFile(file);

                    isDirectory = jarFile.stream().filter(e -> e.getName().startsWith(path)).count() > 1;

                    jarFile.close();
                }
                else
                {
                    // Now that we know that our code base is a directory, we should be running from the IDE
                    URL url = FilePath.class.getClassLoader().getResource(path);

                    if (url == null)
                    {
                        // A guess, if the url is not found, search the code source for this path (Shouldn't be run)
                        isDirectory = new File(file, path).isDirectory();
                    }
                    else
                    {
                        final Map<String, String> env = new HashMap<>();
                        final String[] array = url.toURI().toString().split("!");
                        Path path;

                        if (array[0].startsWith("jar") || array[0].startsWith("zip"))
                        {
                            // The requested file is from a JAR file in the classpath, so create a
                            // new FileSystem to resolve it.
                            final FileSystem fs = FileSystems.newFileSystem(URI.create(array[0]), env);
                            path = fs.getPath(array[1]);
                            isDirectory = Files.isDirectory(path);
                            fs.close();
                        }
                        else
                        {
                            // The requested file is from the directory of the project
                            path = Paths.get(url.toURI());
                            isDirectory = Files.isDirectory(path);
                        }
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

    /**
     * Gets an {@link InputStream} that can be used to read data from this file path.
     *
     * @return An input stream that allows you to read from this FilePath.
     *
     * @throws IOException If an I/O error occurs while creating an input stream.
     */
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

    /**
     * Gets an {@link OutputStream} that can be used to write data to this file path. Note that data can only be written
     * to external file paths, resources will generate a SilenceException.
     *
     * @return An output stream that allows you to write into this FilePath.
     *
     * @throws IOException      If an I/O error occurs.
     * @throws SilenceException If the path is a directory, or if the path is a resource.
     */
    public OutputStream getOutputStream() throws IOException
    {
        if (type == Type.RESOURCE)
            throw new SilenceException("Cannot write to a resource file.");

        if (isDirectory())
            throw new SilenceException("Cannot write to a directory.");

        return Files.newOutputStream(Paths.get(path));
    }

    /**
     * Gets a {@link Reader} that can be used to read the data from this file path.
     *
     * @return An InputStreamReader instance that reads from the input stream of this FilePath.
     *
     * @throws IOException      If an I/O error occurs.
     * @throws SilenceException If the path is a directory, or if the path does not exist.
     */
    public Reader getReader() throws IOException
    {
        return new InputStreamReader(getInputStream());
    }

    /**
     * Gets a {@link Writer} that can be used to write data into this file path.
     *
     * @return An OutputStreamWriter instance that writes data into the output stream of this FilePath.
     *
     * @throws IOException      If an I/O error occurs.
     * @throws SilenceException If the path is a directory, or if the path does not exist.
     */
    public Writer getWriter() throws IOException
    {
        return new OutputStreamWriter(getOutputStream());
    }

    /**
     * Copies the contents of this FilePath into another FilePath replacing the destination contents.
     *
     * @param path The destination FilePath where to copy the contents of this FilePath.
     *
     * @throws IOException      If an I/O error occurs.
     * @throws SilenceException If either this or the destination are directories, or if this path doesn't exist.
     */
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

            inputStream.close();
            outputStream.close();
        }
    }

    /**
     * Moves this path to another path, overwriting the destination if something exists there already.
     *
     * @param path The destination path to move the contents of this path into.
     *
     * @throws IOException      If an I/O error occurs.
     * @throws SilenceException If either the source or the destinations are resources.
     */
    public void moveTo(FilePath path) throws IOException
    {
        if (getType() == Type.RESOURCE || path.getType() == Type.RESOURCE)
            throw new SilenceException("Cannot move resource files!");

        Files.move(Paths.get(this.path), Paths.get(path.getPath()));

        // Change this path
        this.path = path.path;
    }

    /**
     * Creates the directories represented by this path if any does not exist.
     *
     * @throws IOException      If an I/O error occurs.
     * @throws SilenceException If this path is a resource.
     */
    public void mkdirs() throws IOException
    {
        if (getType() == Type.RESOURCE)
            throw new SilenceException("Cannot create resource directories!");

        if (isFile() && !exists())
            getParent().mkdirs();
        else
            Files.createDirectories(Paths.get(path));
    }

    /**
     * Creates an empty file at the path resolved by this FilePath instance.
     *
     * @throws IOException      If an I/O error occurs.
     * @throws SilenceException If this path is a resource, or is a directory.
     */
    public void createFile() throws IOException
    {
        if (getType() == Type.RESOURCE)
            throw new SilenceException("Cannot create resource files!");

        if (isDirectory())
            throw new SilenceException("Cannot convert a directory to a file");

        Files.createFile(Paths.get(path));
    }

    /**
     * Gets a new FilePath that represents the parent directory of this FilePath.
     *
     * @return The parent directory of this FilePath.
     */
    public FilePath getParent()
    {
        String[] parts = path.split("" + SEPARATOR);

        String path = parts[0];
        for (int i = 1; i < parts.length - 1; i++)
            path += SEPARATOR + parts[i] + SEPARATOR;

        return new FilePath(path + SEPARATOR, type);
    }

    /**
     * Gets a new FilePath that represents a file that is a child of this path.
     *
     * @param path The path of the child, relative to this path.
     *
     * @return The FilePath instance of the child.
     *
     * @throws SilenceException If this path is not a directory, or if it does not exist.
     */
    public FilePath getChild(String path)
    {
        if (!isDirectory())
            throw new SilenceException("Cannot get a child for a file.");

        if (!exists())
            throw new SilenceException("Cannot get a child for a non existing directory.");

        return new FilePath(this.path + SEPARATOR + path, type);
    }

    /**
     * Deletes the file resolved by this FilePath instance.
     *
     * @return True if the attempt is successful or false otherwise.
     *
     * @throws IOException      If an I/O error occurs.
     * @throws SilenceException If this file is a resource, or if this doesn't exist.
     */
    public boolean delete() throws IOException
    {
        if (getType() == Type.RESOURCE)
            throw new SilenceException("Cannot delete resource files.");

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

    /**
     * Marks this FilePath to be deleted on JVM exit.
     *
     * @throws SilenceException If this path is a resource.
     */
    public void deleteOnExit()
    {
        if (getType() == Type.RESOURCE)
            throw new SilenceException("Cannot delete resource files.");

        new File(path).deleteOnExit();
    }

    /**
     * Returns the extension of the file represented by this FilePath.
     *
     * @return The extension of the file without the leading dot.
     */
    public String getExtension()
    {
        String[] parts = getPath().split("\\.(?=[^\\.]+$)");
        return parts.length > 1 ? parts[1] : "";
    }

    /**
     * Returns the file name of the file represented by this FilePath.
     *
     * @return The filename of this FilePath, along with the extension.
     */
    public String getName()
    {
        String path = this.path;

        if (path.endsWith("" + SEPARATOR))
            path = path.substring(0, path.length() - 1);

        return path.substring(path.lastIndexOf(SEPARATOR) + 1);
    }

    /**
     * Returns the file name of this file represented by this FilePath without it's extension.
     *
     * @return The filename of this FilePath, without the extension.
     */
    public String getNameWithoutExtension()
    {
        return getName().replaceAll("\\." + getExtension(), "");
    }

    /**
     * Returns the file size of this path in number of bytes. In case of a directory, the size will be the sum of the
     * sizes of all it's children. If this file path does not exist, a value of {@code -1} is returned.
     *
     * @return The size of the file in bytes.
     */
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
                    URL url = FilePath.class.getClassLoader().getResource(path);

                    if (url != null)
                        size = Files.size(Paths.get(url.toURI()));
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
     * Returns a list of FilePaths for the children of this directory.
     *
     * @return An un-modifiable list of FilePaths for all the children of this directory.
     *
     * @throws IOException      If an I/O error occurs.
     * @throws SilenceException If this is not a directory, of if this doesn't exist.
     */
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

            File[] children = file.listFiles();

            if (children != null)
                for (File child : children)
                    list.add(new FilePath(path + SEPARATOR + child.getPath().replace(file.getPath(), ""), getType()));
        }
        else
        {
            File file = new File(FilePath.class.getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20", " "));

            if (file.isFile())
            {
                JarFile jarFile = new JarFile(file);

                jarFile.stream().filter(e -> e.getName().startsWith(path)).forEach(p ->
                        list.add(new FilePath(p.getName(), getType())));

                jarFile.close();
            }
            else
            {
                try
                {
                    URL url = FilePath.class.getClassLoader().getResource(path);

                    if (url != null)
                    {
                        Files.list(Paths.get(url.toURI())).forEach(p ->
                                list.add(new FilePath(path + SEPARATOR + p.toFile().getName(), getType())));
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

    @Override
    public int hashCode()
    {
        int result = getPath().hashCode();
        result = 31 * result + getType().hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FilePath filePath = (FilePath) o;

        return getPath().equals(filePath.getPath()) && getType() == filePath.getType();
    }

    @Override
    public String toString()
    {
        return "FilePath{" +
               "path='" + path + '\'' +
               ", name='" + getName() + "'" +
               ", extension='" + getExtension() + "'" +
               ", type=" + type +
               ", isDirectory=" + isDirectory() +
               ", exists=" + exists() +
               ", size=" + sizeInBytes() +
               '}';
    }

    /**
     * Describes the type of the FilePath. A path can only be either {@link #EXTERNAL} or {@link #RESOURCE}.
     */
    public enum Type
    {
        EXTERNAL, RESOURCE
    }
}
