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
 *
 */

package com.shc.silenceengine.backend.lwjgl;

import com.shc.silenceengine.io.DirectBuffer;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.io.FileReader;
import com.shc.silenceengine.io.IODevice;
import com.shc.silenceengine.io.ImageReader;

/**
 * @author Sri Harsha Chilakapati
 */
public class LwjglIODevice implements IODevice
{
    private FileReader  fileReader  = new LwjglFileReader();
    private ImageReader imageReader = new LwjglImageReader();

    @Override
    public DirectBuffer create(int sizeInBytes)
    {
        return new LwjglDirectBuffer(sizeInBytes);
    }

    @Override
    public void free(DirectBuffer directBuffer)
    {
        ((LwjglDirectBuffer) directBuffer).free();
    }

    @Override
    public FilePath createResourceFilePath(String path)
    {
        return new LwjglResourceFilePath(path);
    }

    @Override
    public FilePath createExternalFilePath(String path)
    {
        return new LwjglExternalFilePath(path);
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
}
