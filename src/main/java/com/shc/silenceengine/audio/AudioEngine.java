package com.shc.silenceengine.audio;

import com.shc.silenceengine.IEngine;
import com.shc.silenceengine.audio.formats.OggReader;
import com.shc.silenceengine.audio.formats.WaveReader;
import com.shc.silenceengine.audio.openal.ALContext;

import java.io.InputStream;

/**
 * @author Sri Harsha Chilakapati
 */
public final class AudioEngine implements IEngine
{
    public void init()
    {
        // Initialize OpenAL context
        ALContext.getInstance().init();

        // Register default readers
        WaveReader.register();
        OggReader.register();
    }

    public Sound getSound(String name)
    {
        return new Sound(name);
    }

    public Sound getSound(InputStream is, String extension)
    {
        return new Sound(is, extension);
    }

    public void dispose()
    {
        ALContext.getInstance().dispose();
    }
}
