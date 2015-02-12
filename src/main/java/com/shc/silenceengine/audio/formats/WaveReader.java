package com.shc.silenceengine.audio.formats;

import com.shc.silenceengine.audio.ISoundReader;
import com.shc.silenceengine.core.SilenceException;

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

import static org.lwjgl.openal.AL10.*;

/**
 * <p>
 * A sound reader based on the JavaSound API. That means, this class is able to
 * read sound samples from any format that JavaSound's <code>AudioInputStream</code>
 * class is able to decode. Currently, it can read from .wav, .wave, .au, .aif,
 * .aiff, .mid, .midi formats.
 * </p>
 *
 * <p>
 * Though you are allowed to create instances of this reader on your own, it is
 * recommended that you use the {@link com.shc.silenceengine.audio.ISoundReader}
 * interface to construct readers, as it is more flexible that way. And also, you
 * do not need to call the <code>register()</code> method in this class, as it
 * will be called automatically for you. Even if you do call it explicitly, there
 * is no harm.
 * </p>
 *
 * <p>
 * If you are really in using this class though, feel free to use it. All it takes
 * is an <code>InputStream</code> to construct the reader, and you can use it in
 * anyway that makes sense for you.
 * </p>
 *
 * @author Sri Harsha Chilakapati
 */
public class WaveReader implements ISoundReader
{
    private ByteBuffer data;

    private int sampleRate;
    private int format;

    /**
     * Registers this class to handle specific extensions.
     */
    public static void register()
    {
        // Register extensions that we handle
        ISoundReader.register("wav",  WaveReader.class);
        ISoundReader.register("wave", WaveReader.class);
        ISoundReader.register("au",   WaveReader.class);
        ISoundReader.register("aif",  WaveReader.class);
        ISoundReader.register("aiff", WaveReader.class);
        ISoundReader.register("mid",  WaveReader.class);
        ISoundReader.register("midi", WaveReader.class);
    }

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

            data = ISoundReader.convertAudioBytes(samples, fmt.getSampleSizeInBits() == 16);

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
