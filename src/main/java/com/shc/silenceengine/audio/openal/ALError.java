package com.shc.silenceengine.audio.openal;

import com.shc.silenceengine.core.Game;

import static org.lwjgl.openal.AL10.*;

/**
 * Class used to check for OpenAL errors (non contextual) and rethrow them
 * as ALException, which is a runtime exception.
 *
 * @author Sri Harsha Chilakapati
 */
public final class ALError
{
    // Prevent instantiation
    private ALError()
    {
    }

    /**
     * Checks for OpenAL errors. If any error is found, the error is
     * thrown as a ALException to help you find the error where it occurs.
     * This method only checks for errors in the development mode.
     */
    public static void check()
    {
        check(false);
    }

    /**
     * Checks for OpenAL errors. If any error is found, the error is
     * thrown as a ALException to help you find the error where it occurs.
     * This method only checks for errors in the development mode unless
     * forced.
     *
     * @param force If true, the errors are also checked if the game is
     *              not in the development mode.
     */
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
