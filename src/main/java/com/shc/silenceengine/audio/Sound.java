package com.shc.silenceengine.audio;

import com.shc.silenceengine.audio.formats.OggReader;
import com.shc.silenceengine.audio.formats.WaveReader;
import com.shc.silenceengine.audio.openal.ALBuffer;
import com.shc.silenceengine.audio.openal.ALSource;
import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.utils.*;

import java.io.InputStream;

import static org.lwjgl.openal.AL10.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class Sound
{
    private ALSource source;
    private ALBuffer buffer;

    private boolean looping;

    public Sound(InputStream is, String format)
    {
        buffer = new ALBuffer();

        ISoundReader reader;

        switch (format.toLowerCase())
        {
            // WaveReader supported formats
            case "au":
            case "aif":
            case "mid":
            case "midi":
            case "wav":
                reader = new WaveReader(is);
                break;

            // OggReader supported formats
            case "ogg":
            case "oga":
                reader = new OggReader(is);
                break;

            default:
                throw new SilenceException("Unknown Audio format. " + format);
        }

        buffer.uploadData(reader.getData(), reader.getFormat(), reader.getSampleRate());

        source = new ALSource();
        source.attachBuffer(buffer);

        looping = false;
    }

    public Sound(String filename)
    {
        this(FileUtils.getResource(filename), FileUtils.getExtension(filename));
    }

    public void play()
    {
        ALSource.State state = source.getState();

        if (state == ALSource.State.PLAYING || state == ALSource.State.LOOPING)
            return;

        source.play();
    }

    public void pause()
    {
        ALSource.State state = source.getState();

        if (state == ALSource.State.PAUSED)
            return;

        source.pause();
    }

    public void stop()
    {
        if (getState() == ALSource.State.STOPPED)
            return;

        source.stop();
    }

    public void setLooping(boolean looping)
    {
        this.looping = looping;
        source.setParameter(AL_LOOPING, looping);
    }

    public void dispose()
    {
        source.dispose();
        buffer.dispose();
    }

    public ALSource.State getState()
    {
        return source.getState();
    }

    public ALSource getSource()
    {
        return source;
    }

    public ALBuffer getBuffer()
    {
        return buffer;
    }

    public boolean isLooping()
    {
        return looping;
    }
}
