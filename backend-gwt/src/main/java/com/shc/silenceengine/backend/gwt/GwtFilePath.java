package com.shc.silenceengine.backend.gwt;

import com.google.gwt.xhr.client.XMLHttpRequest;
import com.shc.silenceengine.io.FilePath;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
public class GwtFilePath extends FilePath
{
    private static final List<FilePath> EMPTY_LIST = Collections.unmodifiableList(new ArrayList<>());

    private boolean exists;
    private int     size;

    /**
     * Constructs an instance of FilePath by taking a path string, and a type.
     *
     * @param path The path string of the path
     * @param type The type of the file, one of {@link Type#EXTERNAL} or {@link Type#RESOURCE}.
     */
    protected GwtFilePath(String path, Type type)
    {
        super(path, type);

        XMLHttpRequest request = XMLHttpRequest.create();

        request.open("HEAD", getPath());

        request.setOnReadyStateChange(xhr -> {
            if (request.getReadyState() == XMLHttpRequest.DONE)
                try
                {
                    size = Integer.parseInt(request.getResponseHeader("Content-Length"));
                }
                catch (Exception e)
                {
                    size = 0;
                }
        });

        request.send();

        exists = request.getStatus() != 404;
    }

    @Override
    public boolean exists()
    {
        return exists;
    }

    @Override
    public boolean isDirectory()
    {
        return !isFile();
    }

    @Override
    public boolean isFile()
    {
        return exists; // FIXME: Assuming it is a file if it exists
    }

    @Override
    public void copyTo(FilePath path) throws IOException
    {
        throw new IOException("Cannot copy files in HTML5 platform.");
    }

    @Override
    public void moveTo(FilePath path) throws IOException
    {
        throw new IOException("Cannot move files in HTML5 platform.");
    }

    @Override
    public void mkdirs() throws IOException
    {
        throw new IOException("Cannot create directories in HTML5 platform.");
    }

    @Override
    public void createFile() throws IOException
    {
        throw new IOException("Cannot create files in HTML5 platform.");
    }

    @Override
    public boolean delete() throws IOException
    {
        throw new IOException("Cannot delete files in HTML5 platform.");
    }

    @Override
    public void deleteOnExit()
    {
        throw new UnsupportedOperationException("Deleting is not supported in HTML5 platform.");
    }

    @Override
    public long sizeInBytes()
    {
        return size;
    }

    @Override
    public List<FilePath> listFiles() throws IOException
    {
        return EMPTY_LIST;
    }
}
