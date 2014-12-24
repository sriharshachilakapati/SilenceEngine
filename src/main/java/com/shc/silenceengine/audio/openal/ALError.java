package com.shc.silenceengine.audio.openal;

import com.shc.silenceengine.core.Game;

import static org.lwjgl.openal.AL10.*;

/**
 * @author Sri Harsha Chilakapati
 */
public final class ALError
{
    private ALError()
    {
    }

    public static void check()
    {
        check(false);
    }

    public static void check(boolean force)
    {
        if (!force && !Game.development)
            return;

        switch (alGetError())
        {
            case AL_NO_ERROR:
                break;

            case AL_INVALID_NAME:
                throw new ALException("AL_INVALID_NAME: Invalid name parameter passed");

            case AL_INVALID_ENUM:
                throw new ALException("AL_INVALID_ENUM: Invalid enum value");

            case AL_INVALID_VALUE:
                throw new ALException("AL_INVALID_VALUE: Invalid parameter value");

            case AL_INVALID_OPERATION:
                throw new ALException("AL_INVALID_OPERATION: Illegal call");

            case AL_OUT_OF_MEMORY:
                throw new ALException("AL_OUT_OF_MEMORY: OpenAL ran out of memory");

            default:
                throw new ALException("Unknown OpenAL Error");
        }
    }
}
