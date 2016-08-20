/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2016 Sri Harsha Chilakapati
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

import com.shc.silenceengine.audio.openal.ALBuffer;
import com.shc.silenceengine.core.IResource;
import com.shc.silenceengine.core.SilenceEngine;

/**
 * <p>A sound is a collection of samples that needs to be played. It simply is a wrapper over an OpenAL buffer.</p>
 *
 * @author Sri Harsha Chilakapati
 */
public class Sound implements IResource
{
    /**
     * The OpenAL Buffer that is backing the audio of this sound.
     */
    public ALBuffer buffer;

    /**
     * Construct a new Sound object with a OpenAL Buffer.
     *
     * @param buffer The OpenAL audio buffer containing the audio samples.
     */
    public Sound(ALBuffer buffer)
    {
        this.buffer = buffer;
    }

    /**
     * Plays this sound in a static environment.
     */
    public void play()
    {
        SilenceEngine.audio.scene.playStatic(this);
    }

    /**
     * Plays this sound in a static environment, optionally allowing you to loop the sound.
     *
     * @param loop Whether to loop the sound.
     */
    public void play(boolean loop)
    {
        SilenceEngine.audio.scene.playStatic(this, loop);
    }

    /**
     * Stops this sound from playing. Note that in case this sound is playing through a source, then this method doesn't
     * stop this sound.
     */
    public void stop()
    {
        SilenceEngine.audio.scene.stopStatic(this);
    }

    @Override
    public void dispose()
    {
    }
}
