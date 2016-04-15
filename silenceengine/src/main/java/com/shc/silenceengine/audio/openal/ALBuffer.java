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

package com.shc.silenceengine.audio.openal;

import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.io.DirectBuffer;

/**
 * <p> This class is an object oriented wrapper to the OpenAL buffers. A buffer encapsulates OpenAL state related to
 * storing sample data. The internal format of the audio samples allowed in a buffer is PCM and is expected to be in
 * native order. </p>
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
        id = SilenceEngine.audio.alGenBuffers();
        ALError.check();
    }

    public ALBuffer(int alBufferID)
    {
        id = alBufferID;
    }

    /**
     * Uploads the data present in a DirectBuffer of a specific format and frequency into this OpenAL Buffer.
     *
     * @param data      The DirectBuffer containing the data samples
     * @param format    The OpenAL format of the data samples in the buffer
     * @param frequency The frequency of the sound samples (in Hz)
     *
     * @throws ALException.InvalidValue If the data does not match the format specified.
     * @throws ALException.OutOfMemory  If there is no available memory to store the data.
     */
    public void uploadData(DirectBuffer data, ALFormat format, int frequency)
    {
        if (isDisposed())
            throw new ALException("Unable to upload data to disposed OpenAL buffer");

        SilenceEngine.audio.alBufferData(id, format.getAlFormat(), data, frequency);

        ALError.check();
    }

    /**
     * Disposes this OpenAL Buffer. A disposed OpenAL buffer is no longer usable, to use again, you need to create
     * another one.
     *
     * @throws ALException if the buffer is already disposed.
     */
    public void dispose()
    {
        if (isDisposed())
            throw new ALException("Unable to dispose an already disposed OpenAL buffer");

        SilenceEngine.audio.alDeleteBuffers(id);
        ALError.check();

        disposed = true;
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
