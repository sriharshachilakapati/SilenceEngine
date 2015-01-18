package com.shc.silenceengine.audio.openal;

import com.shc.silenceengine.core.Game;

import static org.lwjgl.openal.ALC10.*;

/**
 * This class is used to check OpenAL Context errors and rethrow them
 * as ALCException. Useful for debugging purposes.
 *
 * @author Sri Harsha Chilakapati
 */
public final class ALCError
{
    // Prevent instantiation
    private ALCError()
    {
    }

    /**
     * Checks for OpenAL context errors with the default OpenAL device. This
     * method only checks for errors in development mode.
     */
    public static void check()
    {
        check(ALContext.getInstance().getDevice().getPointer(), false);
    }

    /**
     * Checks for OpenAL context errors with the default OpenAL device. This
     * method only checks for errors in development mode unless forced.
     *
     * @param force If true, the checks are performed even when the game is not
     *              in the development mode.
     */
    public static void check(boolean force)
    {
        check(ALContext.getInstance().getDevice().getPointer(), force);
    }

    /**
     * Checks for the OpenAL context errors with the specified OpenAL device. This
     * method only checks for errors in the development mode.
     *
     * @param device The memory location aka the pointer of the device. You can use
     *               ALDevice.getPointer() to obtain this.
     */
    public static void check(long device)
    {
        check(device, false);
    }

    /**
     * Checks for the OpenAL context errors with the specified OpenAL device. This
     * method only checks for errors in the development mode unless forced.
     *
     * @param device The memory location aka the pointer of the device. You can use
     *               ALDevice.getPointer() to obtain this.
     * @param force  If true, the checks are performed even when the game is not in
     *               the development mode.
     */
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
