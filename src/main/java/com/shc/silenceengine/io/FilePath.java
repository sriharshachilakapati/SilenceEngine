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
import java.nio.file.Files;
import java.util.List;
import java.util.jar.JarFile;

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
public abstract class FilePath
{
    /**
     * The UNIX style path separator ({@code /}) character. All the path strings are converted to this character when
     * creating an instance with the constructor.
     */
    public static final char SEPARATOR = '/';

    // The path of the resource, and the type
    protected String path;
    protected Type   type;

    /**
     * Constructs an instance of FilePath by taking a path string, and a type.
     *
     * @param path The path string of the path
     * @param type The type of the file, one of {@link Type#EXTERNAL} or {@link Type#RESOURCE}.
     */
    protected FilePath(String path, Type type)
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
        return BackendIO.get().createExternalFilePath(path);
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
        return BackendIO.get().createResourceFilePath(path);
    }

    /**
     * Checks if the file resolved to from this FilePath instance actually exists, or whether it is a hypothetical one.
     *
     * @return True if the file exists, or else False.
     */
    public abstract boolean exists();

    /**
     * Checks if this FilePath is a directory, or a file.
     *
     * @return True if a directory, else false.
     */
    public abstract boolean isDirectory();

    /**
     * Checks if this FilePath is a file, or a directory.
     *
     * @return True if a file, else false.
     */
    public abstract boolean isFile();

    /**
     * Gets an {@link InputStream} that can be used to read data from this file path.
     *
     * @return An input stream that allows you to read from this FilePath.
     *
     * @throws IOException If an I/O error occurs while creating an input stream.
     */
    public abstract InputStream getInputStream() throws IOException;

    /**
     * Gets an {@link OutputStream} that can be used to write data to this file path. Note that data can only be written
     * to external file paths, resources will generate a SilenceException.
     *
     * @return An output stream that allows you to write into this FilePath.
     *
     * @throws IOException      If an I/O error occurs.
     * @throws SilenceException If the path is a directory, or if the path is a resource.
     */
    public abstract OutputStream getOutputStream() throws IOException;

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
    public abstract void copyTo(FilePath path) throws IOException;

    /**
     * Moves this path to another path, overwriting the destination if something exists there already.
     *
     * @param path The destination path to move the contents of this path into.
     *
     * @throws IOException      If an I/O error occurs.
     * @throws SilenceException If either the source or the destinations are resources.
     */
    public abstract void moveTo(FilePath path) throws IOException;

    public abstract void mkdirs() throws IOException;

    public abstract void createFile() throws IOException;

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

        return type == Type.RESOURCE ? BackendIO.get().createResourceFilePath(path + SEPARATOR)
                                     : BackendIO.get().createExternalFilePath(path + SEPARATOR);
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

        return type == Type.RESOURCE ? BackendIO.get().createResourceFilePath(this.path + SEPARATOR + path)
                                     : BackendIO.get().createExternalFilePath(this.path + SEPARATOR + path);
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
     * Deletes the file resolved by this FilePath instance.
     *
     * @return True if the attempt is successful or false otherwise.
     *
     * @throws IOException      If an I/O error occurs.
     * @throws SilenceException If this file is a resource, or if this doesn't exist.
     */
    public abstract boolean delete() throws IOException;

    /**
     * Marks this FilePath to be deleted on JVM exit.
     *
     * @throws SilenceException If this path is a resource.
     */
    public abstract void deleteOnExit();

    /**
     * Returns the extension of the file represented by this FilePath.
     *
     * @return The extension of the file without the leading dot.
     */
    public String getExtension()
    {
        if (isDirectory())
            return "";

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
    public abstract long sizeInBytes();

    /**
     * Returns a list of FilePaths for the children of this directory.
     *
     * @return An un-modifiable list of FilePaths for all the children of this directory.
     *
     * @throws IOException      If an I/O error occurs.
     * @throws SilenceException If this is not a directory, of if this doesn't exist.
     */
    public abstract List<FilePath> listFiles() throws IOException;

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
               "path='" + getPath() + '\'' +
               ", name='" + getName() + "'" +
               ", extension='" + getExtension() + "'" +
               ", type=" + type +
               ", isDirectory=" + isDirectory() +
               ", exists=" + exists() +
               ", size=" + sizeInBytes() +
               '}';
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

    public enum Type
    {
        RESOURCE,
        EXTERNAL
    }
}
