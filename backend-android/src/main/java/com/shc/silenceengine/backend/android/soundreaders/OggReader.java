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
 */

package com.shc.silenceengine.backend.android.soundreaders;

import com.shc.androidopenal.STB_Vorbis;
import com.shc.androidopenal.STB_VorbisInfo;
import com.shc.silenceengine.audio.openal.ALFormat;
import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.io.DirectBuffer;
import com.shc.silenceengine.io.PrimitiveSize;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static com.shc.androidopenal.STBVorbis.*;

/**
 * <p> A sound reader based on the STB vorbis libraries. That means, this class is able to read sound samples from a OGG
 * audio file in Vorbis encoding that the STB Vorbis class is able to decode. Currently, it can read from .ogg and .oga
 * file formats. </p>
 *
 * @author Sri Harsha Chilakapati
 */
public class OggReader
{
    private ByteBuffer data;

    private int sampleRate;

    private ALFormat format;

    /**
     * Constructs an OGG reader to read from an DirectBuffer.
     *
     * @param directBuffer Direct buffer to read the data from
     */
    public OggReader(DirectBuffer directBuffer)
    {
        decodeToPCM(directBuffer);
    }

    /**
     * This method reads in the OGG data, and convert it into PCM samples ready to be fed into the mouth of OpenAL
     * ALBuffer class.
     */
    private void decodeToPCM(DirectBuffer input)
    {
        // Create a memory representation to read from memory
        ByteBuffer memoryBuffer = (ByteBuffer) input.nativeBuffer();

        // Open the vorbis file to get stb_vorbis*
        IntBuffer error = ByteBuffer.allocateDirect(PrimitiveSize.INT).order(ByteOrder.nativeOrder()).asIntBuffer();
        STB_Vorbis handle = stb_vorbis_open_memory(memoryBuffer, input.sizeBytes(), error, null);

        if (handle == null || STB_Vorbis.getPointer(handle) == 0)
            throw new SilenceException("Error " + error.get(0) + ": decoding the OGG data");

        // Get the information about the OGG header
        STB_VorbisInfo info = stb_vorbis_get_info(handle);

        int channels = info.getChannels();
        sampleRate = (int) info.getSample_rate();

        format = channels == 1 ? ALFormat.MONO_16 : ALFormat.STEREO_16;

        // Read all the samples once for all
        long numSamples = stb_vorbis_stream_length_in_samples(handle);
        ByteBuffer pcm = ByteBuffer.allocateDirect((int) (numSamples * PrimitiveSize.FLOAT)).order(ByteOrder.nativeOrder());
        pcm.clear();
        ShortBuffer shortBuffer = pcm.asShortBuffer();
        stb_vorbis_get_samples_short_interleaved(handle, channels, shortBuffer, shortBuffer.remaining());

        // Free the info
        info.free();

        // Convert the audio bytes and store the data buffer
        data = pcm;

        // Close the stb_vorbis* handle
        stb_vorbis_close(handle);
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
