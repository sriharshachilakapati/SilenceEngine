package com.shc.silenceengine.audio;

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
    public static enum State
    {
        PLAYING, STOPPED, PAUSED, LOOPING
    }

    private ALSource source;
    private ALBuffer buffer;

    private State state;

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

            default:
                throw new SilenceException("Unknown Audio format. " + format);
        }

        buffer.uploadData(reader.getData(), reader.getFormat(), reader.getSampleRate());

        source = new ALSource();
        source.attachBuffer(buffer);

        state = State.STOPPED;
        looping = false;
    }

    public Sound(String filename)
    {
        this(FileUtils.getResource(filename), FileUtils.getExtension(filename));
    }

    public void play()
    {
        if (state == State.PLAYING || state == State.LOOPING)
            return;

        if (looping)
            state = State.LOOPING;
        else
            state = State.PLAYING;

        source.play();
    }

    public void pause()
    {
        if (state == State.PAUSED)
            return;

        state = State.PAUSED;
        source.pause();
    }

    public void stop()
    {
        if (state == State.STOPPED)
            return;

        state = State.STOPPED;
        source.stop();
    }

    public void setLooping(boolean looping)
    {
        this.looping = looping;
        source.setParameter(AL_LOOPING, looping);
    }

    public State getState()
    {
        return state;
    }

    public void dispose()
    {
        source.dispose();
        buffer.dispose();
    }
}
