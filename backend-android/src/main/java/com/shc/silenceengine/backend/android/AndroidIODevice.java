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

package com.shc.silenceengine.backend.android;

import com.shc.silenceengine.io.DirectBuffer;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.io.FileReader;
import com.shc.silenceengine.io.FileWriter;
import com.shc.silenceengine.io.IODevice;
import com.shc.silenceengine.io.ImageReader;

/**
 * @author Sri Harsha Chilakapati
 */
public class AndroidIODevice implements IODevice
{
    private FileReader  fileReader  = new AndroidFileReader();
    private FileWriter  fileWriter  = new AndroidFileWriter();
    private ImageReader imageReader = new AndroidImageReader();

    @Override
    public DirectBuffer create(int sizeInBytes)
    {
        return new AndroidDirectBuffer(sizeInBytes);
    }

    @Override
    public void free(DirectBuffer directBuffer)
    {
        ((AndroidDirectBuffer) directBuffer).free();
    }

    @Override
    public FilePath createResourceFilePath(String path)
    {
        return new AndroidResourceFilePath(path);
    }

    @Override
    public FilePath createExternalFilePath(String path)
    {
        return new AndroidExternalFilePath(path);
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
        return fileWriter;
    }
}
