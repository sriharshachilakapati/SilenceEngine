package com.shc.silenceengine.audio.openal;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.*;

/**
 * This class is an object oriented wrapper to the OpenAL buffers.
 *
 * @author Sri Harsha Chilakapati
 */
public class ALBuffer
{
    private int     id;
    private boolean disposed;

    /**
     * Constructs a new OpenAL buffer.
     *
     * @throws ALException.OutOfMemory If there is no enough memory
     */
    public ALBuffer()
    {
        id = alGenBuffers();
        ALError.check();
    }

    /**
     * Uploads the data present in a NIO Buffer of a specific format and frequency
     * into this OpenAL Buffer.
     *
     * @param data      The NIO Buffer containing the data samples
     * @param format    The OpenAL format of the data samples in the buffer
     * @param frequency The frequency of the sound samples (in Hz)
     *
     * @throws ALException.InvalidValue If the data does not match the format specified.
     * @throws ALException.OutOfMemory  If there is no available memory to store the data.
     */
    public void uploadData(Buffer data, int format, int frequency)
    {
        if (disposed)
            throw new ALException("Unable to upload data to disposed OpenAL buffer");

        if (data instanceof ByteBuffer)
            alBufferData(id, format, (ByteBuffer) data, frequency);

        else if (data instanceof IntBuffer)
            alBufferData(id, format, (IntBuffer) data, frequency);

        else if (data instanceof ShortBuffer)
            alBufferData(id, format, (ShortBuffer) data, frequency);

        else
            throw new ALException("The data should only be one of ByteBuffer, IntBuffer or ShortBuffer");

        ALError.check();
    }

    /**
     * Disposes this OpenAL Buffer. A disposed OpenAL buffer is no longer
     * usable, to use again, you need to create another one.
     *
     * @throws ALException if the buffer is already disposed.
     */
    public void dispose()
    {
        if (disposed)
            throw new ALException("Unable to dispose an already disposed OpenAL buffer");

        alDeleteBuffers(id);
        ALError.check();
    }

    /**
     * @return Returns the OpenAL Buffer Object ID
     */
    public int getID()
    {
        return id;
    }

    /**
     * @return True if disposed, else false
     */
    public boolean isDisposed()
    {
        return disposed;
    }
}
