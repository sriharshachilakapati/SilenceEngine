package com.shc.silenceengine.audio.formats;

import com.shc.silenceengine.audio.ISoundReader;
import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.utils.*;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.*;

/**
 * A WaveReader to read the sound samples from Waveform Audio. Supports all
 * the file formats that are supported by JavaSound API. Currently the files
 * that are read using this, are .wav, .wave, .au, .aif, .aiff, .mid, .midi.
 *
 * @author Sri Harsha Chilakapati
 */
public class WaveReader implements ISoundReader
{
    private ByteBuffer data;

    private int sampleRate;
    private int format;

    /**
     * Constructs a WaveReader that reads the sound samples from an InputStream.
     *
     * @param is The InputStream of the WaveForm Audio resource.
     */
    public WaveReader(InputStream is)
    {
        // Simply use JavaSound reader here
        try
        {
            BufferedInputStream bis = new BufferedInputStream(is);
            AudioInputStream ais = AudioSystem.getAudioInputStream(bis);
            AudioFormat fmt = ais.getFormat();

            sampleRate = (int) fmt.getSampleRate();
            format = fmt.getChannels();

            if (format == 1)
            {
                if (fmt.getSampleSizeInBits() == 8)
                    format = AL_FORMAT_MONO8;
                else if (fmt.getSampleSizeInBits() == 16)
                    format = AL_FORMAT_MONO16;
            }
            else if (format == 2)
            {
                if (fmt.getSampleSizeInBits() == 8)
                    format = AL_FORMAT_STEREO8;
                else if (fmt.getSampleSizeInBits() == 16)
                    format = AL_FORMAT_STEREO16;
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

            data = convertAudioBytes(samples, fmt.getSampleSizeInBits() == 16);

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

    /**
     * Method borrowed from LWJGL3 demos, this converts stereo and mono data samples.
     *
     * @param samples The Byte array of audio samples
     * @param stereo  Whether to convert to stereo audio
     *
     * @return The ByteBuffer containing fixed samples.
     */
    private ByteBuffer convertAudioBytes(byte[] samples, boolean stereo)
    {
        ByteBuffer dest = ByteBuffer.allocateDirect(samples.length);
        dest.order(ByteOrder.nativeOrder());

        ByteBuffer src = ByteBuffer.wrap(samples);
        src.order(ByteOrder.LITTLE_ENDIAN);

        if (stereo)
        {
            ShortBuffer dest_short = dest.asShortBuffer();
            ShortBuffer src_short = src.asShortBuffer();

            while (src_short.hasRemaining())
                dest_short.put(src_short.get());
        }
        else
        {
            while (src.hasRemaining())
                dest.put(src.get());
        }

        dest.rewind();
        return dest;
    }

    /**
     * Constructs a WaveReader that reads samples from a wave file.
     * @param filename The filename of the wav file resource.
     */
    public WaveReader(String filename)
    {
        this(FileUtils.getResource(filename));
    }

    public Buffer getData()
    {
        return data;
    }

    public int getSampleRate()
    {
        return sampleRate;
    }

    public int getFormat()
    {
        return format;
    }
}
