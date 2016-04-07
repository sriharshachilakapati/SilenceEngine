/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2016 Sri Harsha Chilakapati
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
 *
 */

package com.shc.silenceengine.backend.lwjgl.soundreaders;

import com.shc.silenceengine.audio.openal.ALFormat;
import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.io.DirectBuffer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * <p> A sound reader based on the JavaSound API. That means, this class is able to read sound samples from any format
 * that JavaSound's <code>AudioInputStream</code> class is able to decode. Currently, it can read from .wav, .wave, .au,
 * .aif, .aiff, .mid, .midi formats. </p>
 *
 * @author Sri Harsha Chilakapati
 */
public class WaveReader
{
    private ByteBuffer data;

    private int      sampleRate;
    private ALFormat format;

    public WaveReader(DirectBuffer directBuffer)
    {
        this(new DirectBufferInputStream(directBuffer));
    }

    /**
     * Constructs a WaveReader that reads the sound samples from an InputStream.
     *
     * @param is The InputStream of the WaveForm Audio resource.
     */
    private WaveReader(InputStream is)
    {
        // Simply use JavaSound reader here
        try
        {
            BufferedInputStream bis = new BufferedInputStream(is);
            AudioInputStream ais = AudioSystem.getAudioInputStream(bis);
            AudioFormat fmt = ais.getFormat();

            sampleRate = (int) fmt.getSampleRate();
            int format = fmt.getChannels();

            if (format == 1)
            {
                if (fmt.getSampleSizeInBits() == 8)
                    this.format = ALFormat.MONO_8;
                else if (fmt.getSampleSizeInBits() == 16)
                    this.format = ALFormat.MONO_16;
            }
            else if (format == 2)
            {
                if (fmt.getSampleSizeInBits() == 8)
                    this.format = ALFormat.STEREO_8;
                else if (fmt.getSampleSizeInBits() == 16)
                    this.format = ALFormat.STEREO_16;
            }
            else
                throw new SilenceException("Incorrect WAV file format");

            // Length of the WAV file
            int length = ais.available();

            if (length <= 0)
                length = ais.getFormat().getChannels() * (int) ais.getFrameLength() * ais.getFormat().getSampleSizeInBits() / 8;

            byte[] samples = new byte[length];
            DataInputStream dis = new DataInputStream(ais);
            dis.readFully(samples);

            data = SoundUtils.convertAudioBytes(samples, fmt.getSampleSizeInBits() == 16);

            // Close the input streams
            ais.close();
            dis.close();
            is.close();
        }
        catch (UnsupportedAudioFileException | IOException e)
        {
            SilenceException.reThrow(e);
        }
    }

    public ByteBuffer getData()
    {
        return data;
    }

    public int getSampleRate()
    {
        return sampleRate;
    }

    public ALFormat getFormat()
    {
        return format;
    }
}
