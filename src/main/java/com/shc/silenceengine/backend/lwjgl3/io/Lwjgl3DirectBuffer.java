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

import com.shc.silenceengine.io.DirectBuffer;
import org.lwjgl.system.jemalloc.JEmalloc;

import java.nio.ByteBuffer;

/**
 * @author Sri Harsha Chilakapati
 */
public class Lwjgl3DirectBuffer extends DirectBuffer
{
    private ByteBuffer nativeBuffer;

    public Lwjgl3DirectBuffer(int sizeInBytes)
    {
        super(sizeInBytes);
        nativeBuffer = JEmalloc.je_malloc(sizeInBytes);
    }

    @Override
    public DirectBuffer writeInt(int byteIndex, int value)
    {
        nativeBuffer.putInt(byteIndex, value);
        return this;
    }

    @Override
    public DirectBuffer writeFloat(int byteIndex, float value)
    {
        nativeBuffer.putFloat(byteIndex, value);
        return this;
    }

    @Override
    public DirectBuffer writeLong(int byteIndex, long value)
    {
        nativeBuffer.putLong(byteIndex, value);
        return this;
    }

    @Override
    public DirectBuffer writeDouble(int byteIndex, double value)
    {
        nativeBuffer.putDouble(byteIndex, value);
        return this;
    }

    @Override
    public DirectBuffer writeShort(int byteIndex, short value)
    {
        nativeBuffer.putShort(byteIndex, value);
        return this;
    }

    @Override
    public DirectBuffer writeByte(int byteIndex, byte value)
    {
        nativeBuffer.put(byteIndex, value);
        return this;
    }

    @Override
    public int readInt(int byteIndex)
    {
        return nativeBuffer.getInt(byteIndex);
    }

    @Override
    public float readFloat(int byteIndex)
    {
        return nativeBuffer.getFloat(byteIndex);
    }

    @Override
    public long readLong(int byteIndex)
    {
        return nativeBuffer.getLong(byteIndex);
    }

    @Override
    public double readDouble(int byteIndex)
    {
        return nativeBuffer.getDouble(byteIndex);
    }

    @Override
    public short readShort(int byteIndex)
    {
        return nativeBuffer.getShort(byteIndex);
    }

    @Override
    public byte readByte(int byteIndex)
    {
        return nativeBuffer.get(byteIndex);
    }

    @Override
    public Object nativeBuffer()
    {
        return nativeBuffer;
    }

    @Override
    public DirectBuffer clear()
    {
        nativeBuffer.clear();
        return this;
    }

    public void free()
    {
        JEmalloc.je_free(nativeBuffer);
    }
}
