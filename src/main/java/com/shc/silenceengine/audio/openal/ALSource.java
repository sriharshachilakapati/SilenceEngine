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
    /**
     * The State of this source.
     */
    public static enum State
    {
        PLAYING, STOPPED, PAUSED, LOOPING
    }

    private int id;
    private boolean disposed;

    /**
     * Constructs a new OpenAL source.
     */
    public ALSource()
    {
        id = alGenSources();
    }

    /**
     * Attaches an ALBuffer to this source. The buffer contains the audio
     * samples that this source should play.
     *
     * @param buffer The ALBuffer containing the sound samples to be played.
     */
    public void attachBuffer(ALBuffer buffer)
    {
        setParameter(AL_BUFFER, buffer.getID());
    }

    /**
     * Gets a property from this ALSource.
     *
     * @param parameter The parameter of the property to be returned.
     *
     * @return The value of the property with key parameter.
     *
     * @throws ALException AL_INVALID_ENUM if the value is invalid for the parameter.
     * @throws ALException AL_INVALID_NAME if this source is already disposed.
     * @throws ALException AL_INVALID_OPERATION if there is no context.
     * @throws ALException AL_INVALID_VALUE if the value is not an integer.
     */
    public int getParameter(int parameter)
    {
        int result = alGetSourcei(id, parameter);
        ALError.check();

        return result;
    }

    /**
     * Sets the value of a property in this source object.
     *
     * @param parameter The name of the parameter to be set
     * @param value     The value of the parameter to be set
     *
     * @throws ALException AL_INVALID_ENUM if the value is invalid for parameter.
     * @throws ALException AL_INVALID_OPERATION if the context is invalid
     */
    public void setParameter(int parameter, boolean value)
    {
        setParameter(parameter, value ? AL_TRUE : AL_FALSE);
    }

    public void setParameter(int parameter, int value)
    {
        if (disposed)
            throw new ALException("ALSource is already disposed");

        alSourcei(id, parameter, value);
        ALError.check();
    }

    public void setParameter(int parameter, float value)
    {
        if (disposed)
            throw new ALException("ALSource is already disposed");

        alSourcef(id, parameter, value);
        ALError.check();
    }

    public void setParameter(int parameter, float value1, float value2, float value3)
    {
        if (disposed)
            throw new ALException("ALSource is already disposed");

        alSource3f(id, parameter, value1, value2, value3);
        ALError.check();
    }

    public void setParameter(int parameter, Vector3 value)
    {
        setParameter(parameter, value.x, value.y, value.z);
    }

    public void play()
    {
        if (disposed)
            throw new ALException("Cannot play a disposed ALSource");

        alSourcePlay(id);
        ALError.check();
    }

    public void pause()
    {
        if (disposed)
            throw new ALException("Cannot pause a disposed ALSource");

        alSourcePause(id);
        ALError.check();
    }

    public void stop()
    {
        if (disposed)
            throw new ALException("Cannot stop a disposed ALSource");

        alSourceStop(id);
        ALError.check();
    }

    public void rewind()
    {
        if (disposed)
            throw new ALException("Cannot rewind a disposed ALSource");

        alSourceRewind(id);
        ALError.check();
    }

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

            case AL_PAUSED : return State.PAUSED;
        }

        return State.STOPPED;
    }

    public void dispose()
    {
        if (disposed)
            throw new ALException("Cannot Dispose an already disposed OpenAL Source");

        alDeleteSources(id);
    }

    public int getId()
    {
        return id;
    }

    public boolean isDisposed()
    {
        return disposed;
    }
}
