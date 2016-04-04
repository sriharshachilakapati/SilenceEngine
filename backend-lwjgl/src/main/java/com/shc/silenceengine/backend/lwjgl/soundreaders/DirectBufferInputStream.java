package com.shc.silenceengine.backend.lwjgl.soundreaders;

import com.shc.silenceengine.io.DirectBuffer;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Sri Harsha Chilakapati
 */
public class DirectBufferInputStream extends InputStream
{
    private DirectBuffer directBuffer;

    private int index;

    public DirectBufferInputStream(DirectBuffer directBuffer)
    {
        this.directBuffer = directBuffer;
    }

    @Override
    public int read() throws IOException
    {
        if (index >= directBuffer.sizeBytes())
            return -1;

        return directBuffer.readByte(index++);
    }
}
