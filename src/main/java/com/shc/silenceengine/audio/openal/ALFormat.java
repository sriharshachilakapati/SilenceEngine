package com.shc.silenceengine.audio.openal;

import com.shc.silenceengine.core.SilenceException;

import static org.lwjgl.openal.AL10.*;

/**
 * Represents the internal format of an OpenAL Buffer. This format is not
 * the file format or the encoding, but it is whether the audio is mono or
 * stereo and whether the audio is 8-bit or 16-bit audio.
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
     * Returns the OpenAL format constant integer
     *
     * @return The integer constant value of this OpenAL Format
     */
    public int getAlFormat()
    {
        return alFormat;
    }

    /**
     * Turns the OpenAL Format integer constant into a ALFormat enum.
     *
     * @param value The integer constant
     * @return The equivalent ALFormat enum
     */
    public static ALFormat getEnum(int value)
    {
        switch (value)
        {
            case AL_FORMAT_MONO8:    return MONO_8;
            case AL_FORMAT_MONO16:   return MONO_16;
            case AL_FORMAT_STEREO8:  return STEREO_8;
            case AL_FORMAT_STEREO16: return STEREO_16;
        }

        throw new SilenceException("Unknown format value: " + value);
    }
}
