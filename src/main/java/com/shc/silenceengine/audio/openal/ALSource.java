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

package com.shc.silenceengine.audio.openal;

import com.shc.silenceengine.math.Vector3;

import static org.lwjgl.openal.AL10.*;

/**
 * Represents an OpenAL Sound source.
 *
 * @author Sri Harsha Chilakapati
 */
public class ALSource
{
    private int     id;
    private boolean disposed;

    /**
     * Constructs a new OpenAL source.
     *
     * @throws ALException.OutOfMemory If there is no memory available.
     */
    public ALSource()
    {
        id = alGenSources();
    }

    /**
     * Attaches an ALBuffer to this source. The buffer contains the audio samples that this source should play.
     *
     * @param buffer The ALBuffer containing the sound samples to be played.
     *
     * @throws ALException.InvalidValue If the buffer is already disposed.
     */
    public void attachBuffer(ALBuffer buffer)
    {
        setParameter(AL_BUFFER, buffer.getID());
    }

    /**
     * Sets the value of a property in this source object.
     *
     * @param parameter The name of the parameter to be set
     * @param value     The value of the parameter to be set
     *
     * @throws ALException.InvalidEnum If the value is invalid for parameter.
     * @throws ALException             If this source is already disposed.
     */
    public void setParameter(int parameter, int value)
    {
        if (disposed)
            throw new ALException("ALSource is already disposed");

        alSourcei(id, parameter, value);
        ALError.check();
    }

    /**
     * Sets the value of a property in this source object.
     *
     * @param parameter The name of the parameter to be set
     * @param value     The value of the parameter to be set
     *
     * @throws ALException.InvalidEnum If the value is invalid for parameter.
     * @throws ALException             If this source is already disposed.
     */
    public void setParameter(int parameter, boolean value)
    {
        setParameter(parameter, value ? AL_TRUE : AL_FALSE);
    }

    /**
     * Sets the value of a property in this source object.
     *
     * @param parameter The name of the parameter to be set
     * @param value     The value of the parameter to be set
     *
     * @throws ALException.InvalidEnum If the value is invalid for parameter.
     * @throws ALException             If this source is already disposed.
     */
    public void setParameter(int parameter, float value)
    {
        if (disposed)
            throw new ALException("ALSource is already disposed");

        alSourcef(id, parameter, value);
        ALError.check();
    }

    /**
     * Sets the value of a property in this source object.
     *
     * @param parameter The name of the parameter to be set
     * @param value     The value of the parameter to be set
     *
     * @throws ALException.InvalidEnum If the value is invalid for parameter.
     * @throws ALException             If this source is already disposed.
     */
    public void setParameter(int parameter, Vector3 value)
    {
        setParameter(parameter, value.x, value.y, value.z);
    }

    /**
     * Sets the value of a property in this source object.
     *
     * @param parameter The name of the parameter to be set
     * @param value1    The first value of the parameter to be set
     * @param value2    The second value of the parameter to be set
     * @param value3    The third value of the parameter to be set
     *
     * @throws ALException.InvalidEnum If the values are invalid for parameter.
     * @throws ALException             If this source is already disposed.
     */
    public void setParameter(int parameter, float value1, float value2, float value3)
    {
        if (disposed)
            throw new ALException("ALSource is already disposed");

        alSource3f(id, parameter, value1, value2, value3);
        ALError.check();
    }

    /**
     * Starts playing from this source.
     *
     * @throws ALException If the source is already disposed.
     */
    public void play()
    {
        if (disposed)
            throw new ALException("Cannot play a disposed ALSource");

        alSourcePlay(id);
        ALError.check();
    }

    /**
     * Pauses the playback from this source.
     *
     * @throws ALException If the source is already disposed.
     */
    public void pause()
    {
        if (disposed)
            throw new ALException("Cannot pause a disposed ALSource");

        alSourcePause(id);
        ALError.check();
    }

    /**
     * Stops the playback from this source.
     *
     * @throws ALException If the source is already disposed.
     */
    public void stop()
    {
        if (disposed)
            throw new ALException("Cannot stop a disposed ALSource");

        alSourceStop(id);
        ALError.check();
    }

    /**
     * Rewinds this source, so that it will be seek to the starting.
     *
     * @throws ALException If the source is already disposed.
     */
    public void rewind()
    {
        if (disposed)
            throw new ALException("Cannot rewind a disposed ALSource");

        alSourceRewind(id);
        ALError.check();
    }

    /**
     * Queries the current playback state of this ALSource.
     *
     * @return The playback state of this ALSource.
     *
     * @throws ALException.InvalidName If the source is already disposed.
     */
    public State getState()
    {
        int state = getParameter(AL_SOURCE_STATE);

        switch (state)
        {
            case AL_PLAYING:
                if (getParameter(AL_LOOPING) == AL_TRUE)
                    return State.LOOPING;
                else
                    return State.PLAYING;

            case AL_PAUSED:
                return State.PAUSED;
        }

        return State.STOPPED;
    }

    /**
     * Gets a property from this ALSource.
     *
     * @param parameter The parameter of the property to be returned.
     *
     * @return The value of the property with key parameter.
     *
     * @throws ALException.InvalidEnum      If the value is invalid for the parameter.
     * @throws ALException.InvalidName      If this source is already disposed.
     * @throws ALException.InvalidOperation If there is no context.
     * @throws ALException.InvalidValue     If the value is not an integer.
     */
    public int getParameter(int parameter)
    {
        int result = alGetSourcei(id, parameter);
        ALError.check();

        return result;
    }

    /**
     * Disposes this source, releasing all of it's resources.
     *
     * @throws ALException If the source is already disposed.
     */
    public void dispose()
    {
        if (disposed)
            throw new ALException("Cannot Dispose an already disposed OpenAL Source");

        alDeleteSources(id);
        ALError.check();
    }

    /**
     * @return The OpenAL ID for this ALSource instance.
     */
    public int getId()
    {
        return id;
    }

    /**
     * @return True if this source is disposed, Else false.
     */
    public boolean isDisposed()
    {
        return disposed;
    }

    /**
     * The State of this source.
     */
    public static enum State
    {
        PLAYING, STOPPED, PAUSED, LOOPING
    }
}
