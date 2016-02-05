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

package com.shc.silenceengine.backend.lwjgl3.io;

import com.shc.silenceengine.io.IODevice;
import com.shc.silenceengine.io.DirectBuffer;
import com.shc.silenceengine.io.FilePath;

/**
 * @author Sri Harsha Chilakapati
 */
public class Lwjgl3IODevice extends IODevice
{
    @Override
    public DirectBuffer create(int sizeInBytes)
    {
        return new Lwjgl3DirectBuffer(sizeInBytes);
    }

    @Override
    public void free(DirectBuffer directBuffer)
    {
        ((Lwjgl3DirectBuffer) directBuffer).free();
    }

    @Override
    public FilePath createResourceFilePath(String path)
    {
        return new Lwjgl3ResourceFilePath(path);
    }

    @Override
    public FilePath createExternalFilePath(String path)
    {
        return new Lwjgl3ExternalFilePath(path);
    }
}
