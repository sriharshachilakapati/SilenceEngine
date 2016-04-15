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
        // XMLHttpRequest cannot be used with directories, so if it exists
        // then it should be a directory
        return exists;
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
