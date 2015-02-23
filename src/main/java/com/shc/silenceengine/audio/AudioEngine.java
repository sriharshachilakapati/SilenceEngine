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
import com.shc.silenceengine.audio.openal.ALContext;
import com.shc.silenceengine.core.IEngine;
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
