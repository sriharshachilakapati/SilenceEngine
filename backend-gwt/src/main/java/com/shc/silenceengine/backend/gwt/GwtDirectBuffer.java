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
    private ArrayBuffer buffer;
    private DataView    view;

    public GwtDirectBuffer(int sizeInBytes)
    {
        super(sizeInBytes);

        buffer = ArrayBufferNative.create(sizeInBytes);
        view = DataViewNative.create(buffer);
    }

    @Override
    public DirectBuffer writeInt(int byteIndex, int value)
    {
        view.setInt32(byteIndex, value);
        return this;
    }

    @Override
    public DirectBuffer writeFloat(int byteIndex, float value)
    {
        view.setFloat32(byteIndex, value);
        return this;
    }

    @Override
    public DirectBuffer writeLong(int byteIndex, long value)
    {
        view.setUint32(byteIndex, value);
        return this;
    }

    @Override
    public DirectBuffer writeDouble(int byteIndex, double value)
    {
        view.setFloat64(byteIndex, value);
        return this;
    }

    @Override
    public DirectBuffer writeShort(int byteIndex, short value)
    {
        view.setInt16(byteIndex, value);
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
        return view.getInt32(byteIndex);
    }

    @Override
    public float readFloat(int byteIndex)
    {
        return view.getFloat32(byteIndex);
    }

    @Override
    public long readLong(int byteIndex)
    {
        return view.getUint32(byteIndex);
    }

    @Override
    public double readDouble(int byteIndex)
    {
        return view.getFloat64(byteIndex);
    }

    @Override
    public short readShort(int byteIndex)
    {
        return view.getInt16(byteIndex);
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
