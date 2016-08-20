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

package com.shc.silenceengine.io;

import com.shc.silenceengine.core.SilenceEngine;

/**
 * @author Sri Harsha Chilakapati
 */
public abstract class DirectBuffer
{
    protected int sizeInBytes;

    public DirectBuffer(int sizeInBytes)
    {
        this.sizeInBytes = sizeInBytes;
    }

    public static DirectBuffer wrap(byte... data)
    {
        DirectBuffer buffer = create(data.length);

        for (int i = 0; i < data.length; i++)
            buffer.writeByte(i, data[i]);

        return buffer;
    }

    public static DirectBuffer wrap(short... data)
    {
        DirectBuffer buffer = create(data.length * PrimitiveSize.SHORT);

        for (int i = 0; i < data.length; i++)
            buffer.writeShort(i * PrimitiveSize.SHORT, data[i]);

        return buffer;
    }

    public static DirectBuffer wrap(int... data)
    {
        DirectBuffer buffer = create(data.length * PrimitiveSize.INT);

        for (int i = 0; i < data.length; i++)
            buffer.writeInt(i * PrimitiveSize.INT, data[i]);

        return buffer;
    }

    public static DirectBuffer wrap(float... data)
    {
        DirectBuffer buffer = create(data.length * PrimitiveSize.FLOAT);

        for (int i = 0; i < data.length; i++)
            buffer.writeFloat(i * PrimitiveSize.FLOAT, data[i]);

        return buffer;
    }

    public static DirectBuffer wrap(long... data)
    {
        DirectBuffer buffer = create(data.length * PrimitiveSize.LONG);

        for (int i = 0; i < data.length; i++)
            buffer.writeLong(i * PrimitiveSize.LONG, data[i]);

        return buffer;
    }

    public static DirectBuffer wrap(double... data)
    {
        DirectBuffer buffer = create(data.length * PrimitiveSize.DOUBLE);

        for (int i = 0; i < data.length; i++)
            buffer.writeDouble(i * PrimitiveSize.DOUBLE, data[i]);

        return buffer;
    }

    public static DirectBuffer create(int sizeInBytes)
    {
        return SilenceEngine.io.create(sizeInBytes);
    }

    public static void free(DirectBuffer buffer)
    {
        SilenceEngine.io.free(buffer);
    }

    public abstract DirectBuffer writeInt(int byteIndex, int value);

    public abstract DirectBuffer writeFloat(int byteIndex, float value);

    public abstract DirectBuffer writeLong(int byteIndex, long value);

    public abstract DirectBuffer writeDouble(int byteIndex, double value);

    public abstract DirectBuffer writeShort(int byteIndex, short value);

    public abstract DirectBuffer writeByte(int byteIndex, byte value);

    public abstract int readInt(int byteIndex);

    public abstract float readFloat(int byteIndex);

    public abstract long readLong(int byteIndex);

    public abstract double readDouble(int byteIndex);

    public abstract short readShort(int byteIndex);

    public abstract byte readByte(int byteIndex);

    public abstract Object nativeBuffer();

    public abstract DirectBuffer clear();

    public int sizeBytes()
    {
        return sizeInBytes;
    }
}
