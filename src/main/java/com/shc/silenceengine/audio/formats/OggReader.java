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

package com.shc.silenceengine.audio.formats;

import com.shc.silenceengine.audio.ISoundReader;
import com.shc.silenceengine.audio.openal.ALFormat;
import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.utils.FileUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBVorbisInfo;

import java.io.InputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.stb.STBVorbis.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * <p> A sound reader based on the STB vorbis libraries. That means, this class is able to read sound samples from a OGG
 * audio file in Vorbis encoding that the STB Vorbis class is able to decode. Currently, it can read from .ogg and .oga
 * file formats. </p>
 *
 * <p> Though you are allowed to create instances of this reader on your own, it is recommended that you use the {@link
 * com.shc.silenceengine.audio.ISoundReader} interface to construct readers, as it is more flexible that way. And also,
 * you do not need to call the <code>register()</code> method in this class, as it will be called automatically for you.
 * Even if you do call it explicitly, there is no harm. </p>
 *
 * <p> If you are really in using this class though, feel free to use it. All it takes is an <code>InputStream</code> to
 * construct the reader, and you can use it in anyway that makes sense for you. </p>
 *
 * @author Sri Harsha Chilakapati
 */
public class OggReader implements ISoundReader
{
    private ByteBuffer data;

    private int sampleRate;

    private ALFormat format;

    /**
     * Constructs an OGG reader to read from an InputStream.
     *
     * @param is The InputStream to loadLWJGL the sound samples from.
     */
    public OggReader(InputStream is)
    {
        decodeToPCM(is);
    }

    /**
     * Registers the extensions that this class is able to handle.
     */
    public static void register()
    {
        ISoundReader.register("ogg", OggReader.class);
        ISoundReader.register("oga", OggReader.class);
    }

    /**
     * This method reads in the OGG data, and convert it into PCM samples ready to be fed into the mouth of OpenAL
     * ALBuffer class.
     */
    private void decodeToPCM(InputStream input)
    {
        // Create a memory representation to read from memory
        ByteBuffer memoryBuffer = FileUtils.readToByteBuffer(input);

        // Open the vorbis file to get stb_vorbis*
        IntBuffer error = BufferUtils.createIntBuffer(1);
        long handle = stb_vorbis_open_memory(memoryBuffer, error, null);

        if (handle == NULL)
            throw new SilenceException("Error " + error.get(0) + ": decoding the OGG data");

        // Get the information about the OGG header
        STBVorbisInfo info = new STBVorbisInfo();
        stb_vorbis_get_info(handle, info.buffer());

        int channels = info.getChannels();
        sampleRate = info.getSampleRate();

        format = channels == 1 ? ALFormat.MONO_16 : ALFormat.STEREO_16;

        // Read all the samples once for all
        int numSamples = stb_vorbis_stream_length_in_samples(handle);
        ByteBuffer pcm = BufferUtils.createByteBuffer(numSamples * Short.BYTES);
        stb_vorbis_get_samples_short_interleaved(handle, channels, pcm, numSamples);

        // Convert the audio bytes and store the data buffer
        data = ISoundReader.convertAudioBytes(pcm, channels == 2);

        // Close the stb_vorbis* handle
        stb_vorbis_close(handle);
    }

    @Override
    public Buffer getData()
    {
        return data;
    }

    @Override
    public int getSampleRate()
    {
        return sampleRate;
    }

    @Override
    public ALFormat getFormat()
    {
        return format;
    }
}
