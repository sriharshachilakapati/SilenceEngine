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

import com.jcraft.jogg.Packet;
import com.jcraft.jogg.Page;
import com.jcraft.jogg.StreamState;
import com.jcraft.jogg.SyncState;
import com.jcraft.jorbis.Block;
import com.jcraft.jorbis.Comment;
import com.jcraft.jorbis.DspState;
import com.jcraft.jorbis.Info;
import com.shc.silenceengine.audio.ISoundReader;
import com.shc.silenceengine.audio.openal.ALFormat;
import com.shc.silenceengine.core.SilenceException;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;

/**
 * <p> A sound reader based on the j-ogg and JOrbis libraries. That means, this class is able to read sound samples from
 * a OGG audio file in Vorbis encoding that the j-ogg and JOrbis libraries are able to decode. Currently, it can read
 * from .ogg and .oga file formats. </p>
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

    private int      sampleRate;
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
     * Borrowed from DecodeExample.java file of JOrbis examples, but heavily modified. This method reads in the OGG
     * data, and convert it into PCM samples ready to be fed into the mouth of OpenAL ALBuffer class.
     */
    private void decodeToPCM(InputStream input)
    {
        // The PCM Output stream, will contain decoded PCM samples
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        int convSize = 4096 * 2;
        byte[] convBuffer = new byte[convSize];

        // JOGG variables to extract Packets of Vorbis samples
        SyncState oy = new SyncState();
        StreamState os = new StreamState();
        Page og = new Page();
        Packet op = new Packet();

        // JOrbis variables to decode Vorbis samples into PCM
        Info vi = new Info();
        Comment vc = new Comment();
        DspState vd = new DspState();
        Block vb = new Block(vd);

        // The data-buffer we use to read from InputStream
        byte[] buffer;
        int count = 0;

        // Initialize the SyncState
        oy.init();

        while (true)
        {
            // End Of Stream flag
            int eos = 0;

            // Submit a 4k block to JOrbis Ogg layer
            int index = oy.buffer(4096);
            buffer = oy.data;

            try
            {
                count = input.read(buffer, index, 4096);
            }
            catch (Exception e)
            {
                // Rethrow exception as RuntimeException
                SilenceException.reThrow(e);
            }
            oy.wrote(count);

            // Get the first page.
            if (oy.pageout(og) != 1)
            {
                // Have we simply run out of data?  If so, we're done.
                if (count < 4096)
                    break;

                throw new SilenceException("Input does not appear to be an Ogg bitstream.");
            }

            // Get the serial number and set up the rest of decode.
            // serial number first; use it to set up a logical stream
            os.init(og.serialno());

            // Extract the initial header from the first page and verify that the
            // Ogg bitstream is in fact Vorbis data

            // I handle the initial header first instead of just having the code
            // read all three Vorbis headers at once because reading the initial
            // header is an easy way to identify a Vorbis bitstream and it's
            // useful to see that functionality separated out.

            vi.init();
            vc.init();

            if (os.pagein(og) < 0)
                throw new SilenceException("Error reading first page of Ogg bitstream data.");

            if (os.packetout(op) != 1)
                throw new SilenceException("Error reading initial header packet.");

            if (vi.synthesis_headerin(vc, op) < 0)
                throw new SilenceException("This Ogg bitstream does not contain Vorbis audio data.");

            // Get OpenAL format and SampleRate from info
            int format = vi.channels;

            if (format == 1)
                this.format = ALFormat.MONO_16;
            else if (format == 2)
                this.format = ALFormat.STEREO_16;
            else
                throw new SilenceException("Incorrect OGG file format");

            sampleRate = vi.rate;

            // At this point, we're sure we're Vorbis.  We've set up the logical
            // (Ogg) bitstream decoder.  Get the comment and code-book headers and
            // set up the Vorbis decoder

            // The next two packets in order are the comment and code-book headers.
            // They're likely large and may span multiple pages.  Thus we read
            // and submit data until we get our two packets, watching that no
            // pages are missing.  If a page is missing, error out; losing a
            // header page is the only place where missing data is fatal.

            int i = 0;
            while (i < 2)
            {
                while (i < 2)
                {
                    int result = oy.pageout(og);
                    if (result == 0)
                        break;

                    // Don't complain about missing or corrupt data yet.  We'll
                    // catch it at the packet output phase

                    if (result == 1)
                    {
                        os.pagein(og);

                        while (i < 2)
                        {
                            result = os.packetout(op);

                            if (result == 0)
                                break;

                            if (result == -1)
                                throw new SilenceException("Corrupt secondary header");

                            // Synthesis the header
                            vi.synthesis_headerin(vc, op);
                            i++;
                        }
                    }
                }

                // No harm in not checking before adding more
                index = oy.buffer(4096);
                buffer = oy.data;

                try
                {
                    count = input.read(buffer, index, 4096);
                }
                catch (Exception e)
                {
                    SilenceException.reThrow(e);
                }

                if (count == 0 && i < 2)
                    throw new SilenceException("End of file before finding all Vorbis headers!");

                oy.wrote(count);
            }

            convSize = 4096 / vi.channels;

            // OK, got and parsed all three headers. Initialize the Vorbis decoder
            vd.synthesis_init(vi);
            vb.init(vd);

            float[][][] _pcm = new float[1][][];
            int[] _index = new int[vi.channels];

            // The rest is just a straight decode loop until end of stream
            while (eos == 0)
            {
                while (eos == 0)
                {

                    int result = oy.pageout(og);
                    if (result == 0)
                        break; // need more data

                    if (result == -1)
                        throw new SilenceException("Corrupt or missing data in bitstream");

                    else
                    {
                        os.pagein(og);
                        while (true)
                        {
                            result = os.packetout(op);

                            if (result == 0)
                                break;

                            if (result != -1)
                            {
                                // We have a packet.  Decode it
                                int samples;

                                if (vb.synthesis(op) == 0)
                                {
                                    vd.synthesis_blockin(vb);
                                }

                                // PCM is a multichannel float vector.  In stereo, for
                                // example, pcm[0] is left, and pcm[1] is right.  samples is
                                // the size of each channel.  Convert the float values
                                // (-1 <= range <= 1) to whatever PCM format and write it out

                                while ((samples = vd.synthesis_pcmout(_pcm, _index)) > 0)
                                {
                                    float[][] pcm = _pcm[0];
                                    int bout = (samples < convSize ? samples : convSize);

                                    // Convert floats to 16 bit signed integers (host order) interleaved
                                    for (i = 0; i < vi.channels; i++)
                                    {
                                        int ptr = i * 2;
                                        int mono = _index[i];
                                        for (int j = 0; j < bout; j++)
                                        {
                                            int val = (int) (pcm[i][mono + j] * 32767.);

                                            // Might as well guard against clipping
                                            if (val > 32767)
                                                val = 32767;

                                            if (val < -32768)
                                                val = -32768;

                                            if (val < 0)
                                                val = val | 0x8000;

                                            convBuffer[ptr] = (byte) (val);
                                            convBuffer[ptr + 1] = (byte) (val >>> 8);

                                            ptr += 2 * (vi.channels);
                                        }
                                    }

                                    output.write(convBuffer, 0, 2 * vi.channels * bout);

                                    // Tell JVorbis how many samples we actually consumed
                                    vd.synthesis_read(bout);
                                }
                            }
                        }
                        if (og.eos() != 0)
                            eos = 1;
                    }
                }
                if (eos == 0)
                {
                    index = oy.buffer(4096);
                    buffer = oy.data;

                    try
                    {
                        count = input.read(buffer, index, 4096);
                    }
                    catch (Exception e)
                    {
                        SilenceException.reThrow(e);
                    }
                    oy.wrote(count);

                    if (count == 0)
                        eos = 1;
                }
            }

            // Cleanup for this part of the Stream
            os.clear();

            vb.clear();
            vd.clear();
            vi.clear();
        }

        // OK, clean up the framer
        oy.clear();

        // Convert the decoded samples into mono / stereo bytes
        byte[] samples = output.toByteArray();
        data = ISoundReader.convertAudioBytes(samples, vi.channels == 2);
    }

    /**
     * Registers the extensions that this class is able to handle.
     */
    public static void register()
    {
        ISoundReader.register("ogg", OggReader.class);
        ISoundReader.register("oga", OggReader.class);
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
