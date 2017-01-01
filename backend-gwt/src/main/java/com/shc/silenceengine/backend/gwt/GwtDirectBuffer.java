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

import com.google.gwt.typedarrays.client.ArrayBufferNative;
import com.google.gwt.typedarrays.client.DataViewNative;
import com.google.gwt.typedarrays.shared.ArrayBuffer;
import com.google.gwt.typedarrays.shared.DataView;
import com.shc.silenceengine.io.DirectBuffer;

/**
 * @author Sri Harsha Chilakapati
 */
public class GwtDirectBuffer extends DirectBuffer
{
    private static boolean littleEndian = false;
    private static boolean endiannness  = false;

    private ArrayBuffer buffer;
    private DataView    view;

    public GwtDirectBuffer(ArrayBuffer data)
    {
        super(data.byteLength());

        if (!endiannness)
        {
            littleEndian = getEndianness();
            endiannness = true;
        }

        this.buffer = data;
        view = DataViewNative.create(buffer);
    }

    public GwtDirectBuffer(int sizeInBytes)
    {
        super(sizeInBytes);

        if (!endiannness)
        {
            littleEndian = getEndianness();
            endiannness = true;
        }

        buffer = ArrayBufferNative.create(sizeInBytes);
        view = DataViewNative.create(buffer);
    }

    private native boolean getEndianness() /*-{
        var buffer = new ArrayBuffer(2);
        new DataView(buffer).setInt16(0, 256, true);

        // Int16Array uses the platform's endianness.
        return new Int16Array(buffer)[0] === 256;
    }-*/;

    @Override
    public DirectBuffer writeInt(int byteIndex, int value)
    {
        view.setInt32(byteIndex, value, littleEndian);
        return this;
    }

    @Override
    public DirectBuffer writeFloat(int byteIndex, float value)
    {
        view.setFloat32(byteIndex, value, littleEndian);
        return this;
    }

    @Override
    public DirectBuffer writeLong(int byteIndex, long value)
    {
        view.setUint32(byteIndex, value, littleEndian);
        return this;
    }

    @Override
    public DirectBuffer writeDouble(int byteIndex, double value)
    {
        view.setFloat64(byteIndex, value, littleEndian);
        return this;
    }

    @Override
    public DirectBuffer writeShort(int byteIndex, short value)
    {
        view.setInt16(byteIndex, value, littleEndian);
        return this;
    }

    @Override
    public DirectBuffer writeByte(int byteIndex, byte value)
    {
        view.setInt8(byteIndex, value);
        return this;
    }

    @Override
    public int readInt(int byteIndex)
    {
        return view.getInt32(byteIndex, littleEndian);
    }

    @Override
    public float readFloat(int byteIndex)
    {
        return view.getFloat32(byteIndex, littleEndian);
    }

    @Override
    public long readLong(int byteIndex)
    {
        return view.getUint32(byteIndex, littleEndian);
    }

    @Override
    public double readDouble(int byteIndex)
    {
        return view.getFloat64(byteIndex, littleEndian);
    }

    @Override
    public short readShort(int byteIndex)
    {
        return view.getInt16(byteIndex, littleEndian);
    }

    @Override
    public byte readByte(int byteIndex)
    {
        return view.getInt8(byteIndex);
    }

    @Override
    public Object nativeBuffer()
    {
        return view;
    }

    @Override
    public DirectBuffer clear()
    {
        for (int i = 0; i < sizeInBytes; i++)
            writeByte(i, (byte) 0);

        return this;
    }
}
