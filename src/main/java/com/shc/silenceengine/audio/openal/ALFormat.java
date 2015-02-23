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

import com.shc.silenceengine.core.SilenceException;

import static org.lwjgl.openal.AL10.*;

/**
 * Represents the internal format of an OpenAL Buffer. This format is not the file format or the encoding, but it is
 * whether the audio is mono or stereo and whether the audio is 8-bit or 16-bit audio.
 *
 * @author Sri Harsha Chilakapati
 */
public enum ALFormat
{
    /**
     * 8-Bit Mono Audio Format
     */
    MONO_8(AL_FORMAT_MONO8),

    /**
     * 16-Bit Mono Audio Format
     */
    MONO_16(AL_FORMAT_MONO16),

    /**
     * 8-Bit Stereo Audio Format
     */
    STEREO_8(AL_FORMAT_STEREO8),

    /**
     * 16-Bit Stereo Audio Format
     */
    STEREO_16(AL_FORMAT_STEREO16);

    // The integer constant of the OpenAL format
    private int alFormat;

    private ALFormat(int alFormat)
    {
        this.alFormat = alFormat;
    }

    /**
     * Turns the OpenAL Format integer constant into a ALFormat enum.
     *
     * @param value The integer constant
     *
     * @return The equivalent ALFormat enum
     */
    public static ALFormat getEnum(int value)
    {
        switch (value)
        {
            case AL_FORMAT_MONO8:
                return MONO_8;
            case AL_FORMAT_MONO16:
                return MONO_16;
            case AL_FORMAT_STEREO8:
                return STEREO_8;
            case AL_FORMAT_STEREO16:
                return STEREO_16;
        }

        throw new SilenceException("Unknown format value: " + value);
    }

    /**
     * Returns the OpenAL format constant integer
     *
     * @return The integer constant value of this OpenAL Format
     */
    public int getAlFormat()
    {
        return alFormat;
    }
}
