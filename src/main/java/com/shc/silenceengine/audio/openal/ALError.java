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
    public static enum Value
    {
        NO_ERROR, INVALID_NAME, INVALID_ENUM, INVALID_VALUE, INVALID_OPERATION, OUT_OF_MEMORY
    }

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

            case AL_INVALID_NAME:      throw new ALException.InvalidName();
            case AL_INVALID_ENUM:      throw new ALException.InvalidEnum();
            case AL_INVALID_VALUE:     throw new ALException.InvalidValue();
            case AL_INVALID_OPERATION: throw new ALException.InvalidOperation();
            case AL_OUT_OF_MEMORY:     throw new ALException.OutOfMemory();

            default:
                throw new ALException("Unknown OpenAL Error");
        }
    }

    public static Value get()
    {
        switch (alGetError())
        {
            case AL_INVALID_NAME:      return Value.INVALID_NAME;
            case AL_INVALID_ENUM:      return Value.INVALID_ENUM;
            case AL_INVALID_VALUE:     return Value.INVALID_VALUE;
            case AL_INVALID_OPERATION: return Value.INVALID_OPERATION;
            case AL_OUT_OF_MEMORY:     return Value.OUT_OF_MEMORY;
        }

        return Value.NO_ERROR;
    }
}
