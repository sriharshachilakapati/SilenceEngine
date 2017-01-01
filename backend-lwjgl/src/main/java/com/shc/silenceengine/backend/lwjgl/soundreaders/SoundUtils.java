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

package com.shc.silenceengine.backend.lwjgl.soundreaders;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

final class SoundUtils
{
    /**
     * Method borrowed from LWJGL 3 demos, this converts stereo and mono data samples to the internal format of OpenAL.
     *
     * @param samples        The Byte array of audio samples
     * @param twoByteSamples Whether the samples are shorts or bytes
     *
     * @return The ByteBuffer containing fixed samples.
     */
    static ByteBuffer convertAudioBytes(byte[] samples, boolean twoByteSamples)
    {
        ByteBuffer src = ByteBuffer.wrap(samples);
        src.order(ByteOrder.LITTLE_ENDIAN);

        return convertAudioBytes(src, twoByteSamples);
    }

    /**
     * Method borrowed from LWJGL 3 demos, this converts stereo and mono data samples to the internal format of OpenAL.
     *
     * @param samples        The ByteBuffer of audio samples
     * @param twoByteSamples Whether the samples are shorts or bytes
     *
     * @return The ByteBuffer containing fixed samples.
     */
    private static ByteBuffer convertAudioBytes(ByteBuffer samples, boolean twoByteSamples)
    {
        ByteBuffer dest = ByteBuffer.allocateDirect(samples.capacity());
        dest.order(ByteOrder.nativeOrder());

        if (twoByteSamples)
        {
            ShortBuffer dest_short = dest.asShortBuffer();
            ShortBuffer src_short = samples.asShortBuffer();

            while (src_short.hasRemaining())
                dest_short.put(src_short.get());
        }
        else
        {
            while (samples.hasRemaining())
                dest.put(samples.get());
        }
        dest.rewind();

        return dest;
    }
}
