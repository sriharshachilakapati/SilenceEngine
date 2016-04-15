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

import com.shc.silenceengine.audio.openal.ALSource;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.utils.ReusableStack;

import java.util.HashMap;
import java.util.Map;

import static com.shc.silenceengine.audio.AudioDevice.Constants.*;

/**
 * @author Sri Harsha Chilakapati
 */
public final class AudioScene
{
    private ReusableStack<ALSource> sourcesPool;

    private Map<ALSource, AudioSource> playingSources;

    /**
     * Prevent instantiation by users. Should only be used via {@code SilenceEngine.audio.scene}
     */
    AudioScene()
    {
        sourcesPool = new ReusableStack<>(ALSource::new);
        playingSources = new HashMap<>();

        SilenceEngine.eventManager.addDisposeHandler(this::cleanUp);
        SilenceEngine.eventManager.addUpdateHandler(this::updateSources);
    }

    public void stopAllSources()
    {
        for (ALSource source : playingSources.keySet())
        {
            source.stop();
            sourcesPool.push(source);
        }

        playingSources.clear();
    }

    private void cleanUp()
    {
        // Cleanup all the sources in the object pool
        for (ALSource source : sourcesPool.getAsList())
            source.dispose();
    }

    private void updateSources(float deltaTime)
    {
        for (ALSource source : playingSources.keySet())
        {
            AudioSource audioSource = playingSources.get(source);

            if (audioSource.updated)
            {
                source.setParameter(AL_POSITION, audioSource.position);
                source.setParameter(AL_VELOCITY, audioSource.velocity);
                source.setParameter(AL_DIRECTION, audioSource.direction);

                audioSource.updated = false;
            }

            if (source.getState() != ALSource.State.PLAYING)
            {
                playingSources.remove(source);
                sourcesPool.push(source);
            }
        }
    }
}
