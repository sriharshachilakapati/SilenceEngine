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

import com.google.gwt.storage.client.Storage;
import com.shc.easyjson.JSON;
import com.shc.easyjson.JSONObject;
import com.shc.easyjson.ParseException;
import com.shc.silenceengine.io.DirectBuffer;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.io.FileReader;
import com.shc.silenceengine.io.FileWriter;
import com.shc.silenceengine.io.IODevice;
import com.shc.silenceengine.io.ImageReader;

/**
 * @author Sri Harsha Chilakapati
 */
public class GwtIODevice implements IODevice
{
    private FileReader  fileReader  = new GwtFileReader();
    private ImageReader imageReader = new GwtImageReader();

    @Override
    public DirectBuffer create(int sizeInBytes)
    {
        return new GwtDirectBuffer(sizeInBytes);
    }

    @Override
    public void free(DirectBuffer directBuffer)
    {
        // Will be taken care by GC
    }

    @Override
    public FilePath createResourceFilePath(String path)
    {
        return new GwtFilePath(path, FilePath.Type.RESOURCE);
    }

    @Override
    public FilePath createExternalFilePath(String path)
    {
        return new GwtFilePath(path, FilePath.Type.EXTERNAL);
    }

    @Override
    public FileReader getFileReader()
    {
        return fileReader;
    }

    @Override
    public ImageReader getImageReader()
    {
        return imageReader;
    }

    @Override
    public FileWriter getFileWriter()
    {
        return null;
    }

    @Override
    public JSONObject getPreferences(String name)
    {
        Storage localStorage = Storage.getLocalStorageIfSupported();

        if (localStorage == null)
            return new JSONObject();

        try
        {
            String json = localStorage.getItem(name);
            return JSON.parse(json == null ? "{}" : json);
        }
        catch (ParseException e)
        {
            return new JSONObject();
        }
    }

    @Override
    public void savePreferences(String name, JSONObject preferences)
    {
        Storage localStorage = Storage.getLocalStorageIfSupported();

        if (localStorage == null)
            return;

        localStorage.setItem(name, JSON.write(preferences));
    }
}
