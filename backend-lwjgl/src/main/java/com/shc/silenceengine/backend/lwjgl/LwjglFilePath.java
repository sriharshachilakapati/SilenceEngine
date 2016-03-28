package com.shc.silenceengine.backend.lwjgl;

import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.io.FilePath;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Sri Harsha Chilakapati
 */
public abstract class LwjglFilePath extends FilePath
{
    /**
     * Constructs an instance of FilePath by taking a path string, and a type.
     *
     * @param path The path string of the path
     * @param type The type of the file, one of {@link Type#EXTERNAL} or {@link Type#RESOURCE}.
     */
    protected LwjglFilePath(String path, Type type)
    {
        super(path, type);
    }

    public abstract InputStream getInputStream() throws IOException;

    public abstract OutputStream getOutputStream() throws IOException;

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

        try (InputStream inputStream = getInputStream(); OutputStream outputStream = ((LwjglFilePath) path).getOutputStream())
        {
            while ((length = inputStream.read(buffer)) > 0)
            {
                outputStream.write(buffer, 0, length);
            }
        }
    }
}
