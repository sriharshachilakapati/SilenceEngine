package com.shc.silenceengine.audio.openal;

import com.shc.silenceengine.core.SilenceException;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class ALBuffer
{
    private int     id;
    private boolean disposed;

    public ALBuffer()
    {
        id = alGenBuffers();
        ALError.check();
    }

    public void uploadData(Buffer data, int format, int frequency)
    {
        if (disposed)
            throw new SilenceException("Unable to upload data to disposed OpenAL buffer");

        if (data instanceof ByteBuffer)
            alBufferData(id, format, (ByteBuffer)data, frequency);

        else if (data instanceof IntBuffer)
            alBufferData(id, format, (IntBuffer)data, frequency);

        else if (data instanceof ShortBuffer)
            alBufferData(id, format, (ShortBuffer) data, frequency);

        else
            throw new SilenceException("The data should only be one of ByteBuffer, IntBuffer or ShortBuffer");

        ALError.check();
    }

    public void dispose()
    {
        if (disposed)
            throw new SilenceException("Unable to dispose an already disposed OpenAL buffer");

        alDeleteBuffers(id);
        ALError.check();
    }

    public int getID()
    {
        return id;
    }

    public boolean isDisposed()
    {
        return disposed;
    }
}
