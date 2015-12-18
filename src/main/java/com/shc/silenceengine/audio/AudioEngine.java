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

package com.shc.silenceengine.audio;

import com.shc.silenceengine.audio.formats.OggReader;
import com.shc.silenceengine.audio.formats.WaveReader;
import com.shc.silenceengine.backend.lwjgl3.openal.ALContext;
import com.shc.silenceengine.core.IEngine;
import com.shc.silenceengine.utils.Logger;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALDevice;

import java.io.InputStream;

/**
 * The central hub for all the audio related operations in SilenceEngine. You have to initialize this engine if you want
 * to have audio when you are doing your own game loop. It is invoked automatically if you inherit your game from the
 * <code>Game</code> class.
 *
 * @author Sri Harsha Chilakapati
 */
public final class AudioEngine implements IEngine
{
    /**
     * Initializes the AudioEngine. Opens an OpenAL Device and creates an OpenAL 1.1 context. This method also registers
     * the <code>WaveReader</code> and <code>OggReader</code> to the <code>ISoundReader</code> automatically.
     */
    public void init()
    {
        Logger.info("Initializing Audio Engine with OpenAL 1.1");

        // Initialize OpenAL context
        ALContext.getInstance().init();

        // Register default readers
        WaveReader.register();
        OggReader.register();

        Logger.info("Audio Engine initialized successfully, with device "
                    + ALC10.alcGetString(getALDevice().address(), ALC10.ALC_DEVICE_SPECIFIER));
    }

    @Override
    public void beginFrame()
    {
    }

    @Override
    public void endFrame()
    {
    }

    @Override
    public void dispose()
    {
        Logger.info("Disposing the Audio Engine, and any audio resources");
        ALContext.getInstance().dispose();
        Logger.info("Audio Engine has been successfully disposed");
    }

    /**
     * Loads a sound from a resource name.
     *
     * @param name The resource name of the sound file to load.
     *
     * @return The loaded Sound object.
     */
    public Sound getSound(String name)
    {
        return new Sound(name);
    }

    /**
     * Loads a sound from an InputStream
     *
     * @param is        The InputStream to read the sound data
     * @param extension The extension of the file to determine the format of the data
     *
     * @return The loaded Sound object
     */
    public Sound getSound(InputStream is, String extension)
    {
        return new Sound(is, extension);
    }

    /**
     * @return The ALContext instance used to manipulate the OpenAL context
     */
    public ALContext getALContext()
    {
        return ALContext.getInstance();
    }

    /**
     * @return The OpenAL device which is playing the audio
     */
    public ALDevice getALDevice()
    {
        return getALContext().getDevice();
    }
}
