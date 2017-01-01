/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 Sri Harsha Chilakapati
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

import com.google.gwt.core.client.GWT;
import com.google.gwt.xhr.client.XMLHttpRequest;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.utils.functional.Promise;

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

    private static String resourcesRoot;

    /**
     * Constructs an instance of FilePath by taking a path string, and a type.
     *
     * @param path The path string of the path
     * @param type The type of the file, one of {@link Type#EXTERNAL} or {@link Type#RESOURCE}.
     */
    protected GwtFilePath(String path, Type type)
    {
        super(path, type);
    }

    @Override
    public Promise<Boolean> exists()
    {
        return new Promise<>((resolve, reject) ->
        {
            XMLHttpRequest request = XMLHttpRequest.create();

            request.open("HEAD", getAbsolutePath());

            request.setOnReadyStateChange(xhr ->
            {
                if (request.getStatus() == 404)
                    resolve.invoke(false);

                else if (request.getReadyState() == XMLHttpRequest.DONE && request.getStatus() == 200)
                    resolve.invoke(true);
            });

            request.send();
        });
    }

    @Override
    public Promise<Boolean> isDirectory()
    {
        return exists();
    }

    @Override
    public Promise<Boolean> isFile()
    {
        return exists();
    }

    @Override
    public Promise<Void> copyTo(FilePath path)
    {
        return new Promise<>((resolve, reject) -> reject.invoke(new IOException("Cannot copy files in HTML5 platform.")));
    }

    @Override
    public Promise<Void> moveTo(FilePath path)
    {
        return new Promise<>((resolve, reject) -> reject.invoke(new IOException("Cannot move files in HTML5 platform.")));
    }

    @Override
    public Promise<Void> mkdirs()
    {
        return new Promise<>((resolve, reject) -> reject.invoke(new IOException("Cannot create directories in HTML5 platform.")));
    }

    @Override
    public Promise<Void> createFile()
    {
        return new Promise<>((resolve, reject) -> reject.invoke(new IOException("Cannot create files in HTML5 platform.")));
    }

    @Override
    public String getAbsolutePath()
    {
        if (type == Type.RESOURCE)
            return resourcesRoot + super.getAbsolutePath();

        return super.getAbsolutePath();
    }

    @Override
    public Promise<Boolean> delete()
    {
        return new Promise<>((resolve, reject) -> reject.invoke(new IOException("Cannot delete files in HTML5 platform.")));
    }

    @Override
    public void deleteOnExit()
    {
        throw new UnsupportedOperationException("Deleting is not supported in HTML5 platform.");
    }

    @Override
    public Promise<Long> sizeInBytes()
    {
        return new Promise<>((resolve, reject) ->
        {
            XMLHttpRequest request = XMLHttpRequest.create();

            request.open("HEAD", getAbsolutePath());

            request.setOnReadyStateChange(xhr ->
            {
                if (request.getStatus() == 404)
                    resolve.invoke(-1L);

                else if (request.getReadyState() == XMLHttpRequest.DONE && request.getStatus() == 200)
                    try
                    {
                        resolve.invoke(Long.parseLong(request.getResponseHeader("Content-Length")));
                    }
                    catch (Exception e)
                    {
                        resolve.invoke(0L);
                    }
            });

            request.send();
        });
    }

    @Override
    public Promise<List<FilePath>> listFiles()
    {
        return new Promise<>((resolve, reject) -> resolve.invoke(EMPTY_LIST));
    }

    static
    {
        resourcesRoot = GWT.getModuleBaseURL();

        // Get rid of the trailing slash
        if (resourcesRoot.endsWith("/"))
            resourcesRoot = resourcesRoot.substring(0, resourcesRoot.lastIndexOf('/'));

        // Get rid of the module directory
        resourcesRoot = resourcesRoot.substring(0, resourcesRoot.lastIndexOf('/'));

        // Add the trailing slash if not present
        if (!resourcesRoot.endsWith("/"))
            resourcesRoot += "/";
    }
}
