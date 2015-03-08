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

import com.shc.silenceengine.core.Game;
import org.lwjgl.openal.ALDevice;

import static org.lwjgl.openal.ALC10.*;

/**
 * This class is used to check OpenAL Context errors and rethrow them as ALCException. Useful for debugging purposes.
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
     * Checks for OpenAL context errors with the default OpenAL device. This method only checks for errors in
     * development mode.
     */
    public static void check()
    {
        check(ALContext.getInstance().getDevice().getPointer(), false);
    }

    /**
     * Checks for the OpenAL context errors with the specified OpenAL device. This method only checks for errors in the
     * development mode unless forced.
     *
     * @param device The memory location aka the pointer of the device. You can use ALDevice.getPointer() to obtain
     *               this.
     * @param force  If true, the checks are performed even when the game is not in the development mode.
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
                throw new ALCException.InvalidDevice();
            case ALC_INVALID_CONTEXT:
                throw new ALCException.InvalidContext();
            case ALC_INVALID_ENUM:
                throw new ALCException.InvalidEnum();
            case ALC_INVALID_VALUE:
                throw new ALCException.InvalidValue();
            case ALC_OUT_OF_MEMORY:
                throw new ALCException.OutOfMemory();

            default:
                throw new ALCException("Unknown OpenAL Context Error");
        }
    }

    /**
     * Checks for OpenAL context errors with the default OpenAL device. This method only checks for errors in
     * development mode unless forced.
     *
     * @param force If true, the checks are performed even when the game is not in the development mode.
     */
    public static void check(boolean force)
    {
        check(ALContext.getInstance().getDevice().getPointer(), force);
    }

    /**
     * Checks for the OpenAL context errors with the specified OpenAL device. This method only checks for errors in the
     * development mode.
     *
     * * @param device The OpenAL Device that should be checked for context errors.
     */
    public static void check(ALDevice device)
    {
        check(device.getPointer());
    }

    /**
     * Checks for the OpenAL context errors with the specified OpenAL device. This method only checks for errors in the
     * development mode.
     *
     * @param device The memory location aka the pointer of the device. You can use ALDevice.getPointer() to obtain this
     *               parameter.
     */
    public static void check(long device)
    {
        check(device, false);
    }

    /**
     * Checks for the OpenAL context errors with the specified OpenAL device. This method only checks for errors in the
     * development mode.
     *
     * @param device The OpenAL Device that should be checked for context errors.
     * @param force  If true, the checks are performed even when the game is not in the development mode.
     */
    public static void check(ALDevice device, boolean force)
    {
        check(device.getPointer(), force);
    }

    /**
     * Checks for the OpenAL context error and return the error on the top of the OpenAL error stack, using the default
     * device.
     *
     * @return The value of the error as an enum.
     */
    public static Value get()
    {
        return get(ALContext.getInstance().getDevice().getPointer());
    }

    /**
     * Checks for the OpenAL context error and return the error on the top of the OpenAL error stack.
     *
     * @param device The memory location aka the pointer of the device. You can use ALDevice.getPointer() to obtain
     *               this.
     *
     * @return The value of the error as an enum.
     */
    public static Value get(long device)
    {
        switch (alcGetError(device))
        {
            case ALC_INVALID_DEVICE:
                return Value.INVALID_DEVICE;
            case ALC_INVALID_CONTEXT:
                return Value.INVALID_CONTEXT;
            case ALC_INVALID_ENUM:
                return Value.INVALID_ENUM;
            case ALC_INVALID_VALUE:
                return Value.INVALID_VALUE;
            case ALC_OUT_OF_MEMORY:
                return Value.OUT_OF_MEMORY;
        }

        return Value.NO_ERROR;
    }

    /**
     * Checks for the OpenAL context error and return the error on the top of the OpenAL error stack, using the default
     * device.
     *
     * @param device The OpenAL Device that should be checked for context errors.
     *
     * @return The value of the error as an enum.
     */
    public static Value get(ALDevice device)
    {
        return get(device.getPointer());
    }

    /**
     * The value enumeration of the OpenAL Context Error
     */
    public static enum Value
    {
        NO_ERROR, INVALID_DEVICE, INVALID_CONTEXT, INVALID_ENUM, INVALID_VALUE, OUT_OF_MEMORY
    }
}
