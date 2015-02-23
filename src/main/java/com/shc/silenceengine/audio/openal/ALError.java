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

import static org.lwjgl.openal.AL10.*;

/**
 * Class used to check for OpenAL errors (non contextual) and rethrow them as ALException, which is a runtime
 * exception.
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
     * Checks for OpenAL errors. If any error is found, the error is thrown as a ALException to help you find the error
     * where it occurs. This method only checks for errors in the development mode.
     */
    public static void check()
    {
        check(false);
    }

    /**
     * Checks for OpenAL errors. If any error is found, the error is thrown as a ALException to help you find the error
     * where it occurs. This method only checks for errors in the development mode unless forced.
     *
     * @param force If true, the errors are also checked if the game is not in the development mode.
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
                throw new ALException.InvalidName();
            case AL_INVALID_ENUM:
                throw new ALException.InvalidEnum();
            case AL_INVALID_VALUE:
                throw new ALException.InvalidValue();
            case AL_INVALID_OPERATION:
                throw new ALException.InvalidOperation();
            case AL_OUT_OF_MEMORY:
                throw new ALException.OutOfMemory();

            default:
                throw new ALException("Unknown OpenAL Error");
        }
    }

    /**
     * Checks for the OpenAL error and return the error on the top of the OpenAL error stack, using the default device.
     *
     * @return The value of the error as an enum.
     */
    public static Value get()
    {
        switch (alGetError())
        {
            case AL_INVALID_NAME:
                return Value.INVALID_NAME;
            case AL_INVALID_ENUM:
                return Value.INVALID_ENUM;
            case AL_INVALID_VALUE:
                return Value.INVALID_VALUE;
            case AL_INVALID_OPERATION:
                return Value.INVALID_OPERATION;
            case AL_OUT_OF_MEMORY:
                return Value.OUT_OF_MEMORY;
        }

        return Value.NO_ERROR;
    }

    /**
     * The value enumeration of OpenAL error
     */
    public static enum Value
    {
        NO_ERROR, INVALID_NAME, INVALID_ENUM, INVALID_VALUE, INVALID_OPERATION, OUT_OF_MEMORY
    }
}
