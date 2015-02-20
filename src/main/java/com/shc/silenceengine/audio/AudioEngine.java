package com.shc.silenceengine.audio;

import com.shc.silenceengine.core.IEngine;
import com.shc.silenceengine.audio.formats.OggReader;
import com.shc.silenceengine.audio.formats.WaveReader;
import com.shc.silenceengine.audio.openal.ALContext;
import com.shc.silenceengine.utils.Logger;
import org.lwjgl.openal.ALC10;

import java.io.InputStream;

/**
 * @author Sri Harsha Chilakapati
 */
public final class AudioEngine implements IEngine
{
    public void init()
    {
        Logger.log("Initializing Audio Engine with OpenAL 1.1");
        // Initialize OpenAL context
        ALContext.getInstance().init();

        // Register default readers
        WaveReader.register();
        OggReader.register();

        Logger.log("Audio Engine initialized successfully, with device "
                   + ALC10.alcGetString(ALContext.getInstance().getDevice().getPointer(), ALC10.ALC_DEVICE_SPECIFIER));
    }

    @Override
    public void beginFrame()
    {
    }

    @Override
    public void endFrame()
    {
    }

    public void dispose()
    {
        Logger.log("Disposing the Audio Engine, and any audio resources");
        ALContext.getInstance().dispose();
        Logger.log("Audio Engine has been successfully disposed");
    }

    public Sound getSound(String name)
    {
        return new Sound(name);
    }

    public Sound getSound(InputStream is, String extension)
    {
        return new Sound(is, extension);
    }
}
