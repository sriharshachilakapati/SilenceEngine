/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 Sri Harsha Chilakapati
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

package com.shc.silenceengine.backend.android.soundreaders;

import com.shc.silenceengine.audio.openal.ALException;
import com.shc.silenceengine.audio.openal.ALFormat;
import com.shc.silenceengine.backend.android.DirectBufferInputStream;
import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.io.DirectBuffer;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * A Loader utility for (.wav) files.
 *
 * @author Sri Harsha Chilakapati
 */
public class WavReader
{
    public ByteBuffer data;
    public ALFormat   alFormat;
    public int        sampleRate;

    public WavReader(DirectBuffer memory)
    {
        try
        {
            loadFromStreamImpl(new DirectBufferInputStream(memory));
        }
        catch (IOException e)
        {
            SilenceException.reThrow(e);
        }
    }

    private static long readUnsignedIntLittleEndian(DataInputStream is) throws IOException
    {
        byte[] buf = new byte[4];
        is.readFully(buf);
        return (buf[0] & 0xFF | ((buf[1] & 0xFF) << 8) | ((buf[2] & 0xFF) << 16) | ((buf[3] & 0xFF) << 24));
    }

    private static short readUnsignedShortLittleEndian(DataInputStream is) throws IOException
    {
        byte[] buf = new byte[2];
        is.readFully(buf);
        return (short) (buf[0] & 0xFF | ((buf[1] & 0xFF) << 8));
    }

    private void loadFromStreamImpl(InputStream aIn) throws IOException
    {
        /*
         * references:
         * http://www.sonicspot.com/guide/wavefiles.html
         * https://ccrma.stanford.edu/courses/422/projects/WaveFormat/
         * http://stackoverflow.com/questions/1111539/is-the-endianness-of-format-params-guaranteed-in-riff-wav-files
         * http://sharkysoft.com/archive/lava/docs/javadocs/lava/riff/wave/doc-files/riffwave-content.htm
         */

        final DataInputStream din;
        if (aIn instanceof DataInputStream)
        {
            din = (DataInputStream) aIn;
        }
        else
        {
            din = new DataInputStream(aIn);
        }
        try
        {
            if (din.readInt() != 0x52494646)
            {
                throw new ALException("Invalid WAV header");
            }
            // length of the RIFF, unused
            readUnsignedIntLittleEndian(din);
            if (din.readInt() != 0x57415645)
            {// "WAVE"
                throw new ALException("Invalid WAV header");
            }
            boolean foundFmt = false;
            boolean foundData = false;

            short sChannels = 0, sSampleSizeInBits = 0;
            long sampleRate = 0;
            long chunkLength;

            while (!foundData)
            {
                int chunkId = din.readInt();
                chunkLength = readUnsignedIntLittleEndian(din);
                switch (chunkId)
                {
                    case 0x666D7420: // "fmt "
                        foundFmt = true;
                        // compression code, unused
                        readUnsignedShortLittleEndian(din);
                        sChannels = readUnsignedShortLittleEndian(din);
                        sampleRate = readUnsignedIntLittleEndian(din);
                        // bytes per second, unused
                        readUnsignedIntLittleEndian(din);
                        // block alignment, unused
                        readUnsignedShortLittleEndian(din);
                        sSampleSizeInBits = readUnsignedShortLittleEndian(din);
                        din.skip(chunkLength - 16);
                        break;
                    case 0x66616374: // "fact"
                        din.skip(chunkLength);
                        break;
                    case 0x64617461: // "data"
                        if (!foundFmt)
                            throw new ALException(
                                    "WAV fmt chunks must be before data chunks");
                        foundData = true;
                        break;
                    default:
                        // unrecognized chunk, skips it
                        din.skip(chunkLength);
                }
            }

            final int channels = (int) sChannels;
            final int sampleSizeInBits = sSampleSizeInBits;

            readSamples(aIn, channels, sampleSizeInBits, (int) sampleRate, ByteOrder.LITTLE_ENDIAN);
        }
        finally
        {
            din.close();
        }
    }

    private void readSamples(InputStream aIn, final int numChannels, final int bits, final int sampleRate, final ByteOrder byteOrder)
            throws IOException
    {
        if (!(aIn instanceof BufferedInputStream))
            aIn = new BufferedInputStream(aIn);

        alFormat = ALFormat.MONO_8;

        if ((bits == 8) && (numChannels == 1))
            alFormat = ALFormat.MONO_8;

        else if ((bits == 16) && (numChannels == 1))
            alFormat = ALFormat.MONO_16;

        else if ((bits == 8) && (numChannels == 2))
            alFormat = ALFormat.STEREO_8;

        else if ((bits == 16) && (numChannels == 2))
            alFormat = ALFormat.STEREO_16;

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[16384];

        while ((nRead = aIn.read(data, 0, data.length)) != -1)
            buffer.write(data, 0, nRead);

        buffer.flush();

        byte[] array = buffer.toByteArray();


        final ByteBuffer bBuffer = ByteBuffer.allocateDirect(array.length).order(ByteOrder.nativeOrder()).put(array);
        bBuffer.rewind();

        // Must byte swap in case endianess mismatch
        if (bits == 16 && ByteOrder.nativeOrder() != byteOrder)
        {
            final int len = bBuffer.remaining();

            for (int i = 0; i < len; i += 2)
            {
                final byte a = bBuffer.get(i);
                final byte b = bBuffer.get(i + 1);
                bBuffer.put(i, b);
                bBuffer.put(i + 1, a);
            }
        }

        this.data = bBuffer;
        this.sampleRate = sampleRate;

        aIn.close();
    }

}