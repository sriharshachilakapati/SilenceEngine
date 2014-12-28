package com.shc.silenceengine.audio.openal;

import com.shc.silenceengine.math.Vector3;

import static org.lwjgl.openal.AL10.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class ALSource
{
    private int id;
    private boolean disposed;

    public ALSource()
    {
        id = alGenSources();
    }

    public void attachBuffer(ALBuffer buffer)
    {
        setParameter(AL_BUFFER, buffer.getID());
    }

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
