package com.shc.silenceengine.audio.openal;

import com.shc.silenceengine.core.Game;

import static org.lwjgl.openal.ALC10.*;

/**
 * @author Sri Harsha Chilakapati
 */
public final class ALCError
{
    private ALCError()
    {
    }

    public static void check()
    {
        check(ALContext.getInstance().getDevice().getPointer(), false);
    }

    public static void check(boolean force)
    {
        check(ALContext.getInstance().getDevice().getPointer(), force);
    }

    public static void check(long device)
    {
        check(device, false);
    }

    public static void check(long device, boolean force)
    {
        if (!force && !Game.development)
            return;

        switch (alcGetError(device))
        {
            case ALC_NO_ERROR:
                break;

            case ALC_INVALID_DEVICE:
                throw new ALCException("ALC_INVALID_DEVICE: Invalid or no device selected");

            case ALC_INVALID_CONTEXT:
                throw new ALCException("ALC_INVALID_CONTEXT: Invalid or no context selected");

            case ALC_INVALID_ENUM:
                throw new ALCException("ALC_INVALID_ENUM: Invalid enum value");

            case ALC_INVALID_VALUE:
                throw new ALCException("ALC_INVALID_VALUE: Invalid parameter value");

            case ALC_OUT_OF_MEMORY:
                throw new ALCException("ALC_OUT_OF_MEMORY: OpenAL ran out of memory");

            default:
                throw new ALCException("Unknown OpenAL Context Error");
        }
    }
}
