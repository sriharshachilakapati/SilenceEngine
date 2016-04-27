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
    private ReusableStack<ALSource>      sourcesPool;
    private ReusableStack<PlayingSource> playingSourcesPool;

    private Map<PlayingSource, AudioSource> playingSources;

    private AudioSource defaultAudioSource;

    /**
     * Prevent instantiation by users. Should only be used via {@code SilenceEngine.audio.scene}
     */
    AudioScene()
    {
        sourcesPool = new ReusableStack<>(ALSource::new);
        playingSourcesPool = new ReusableStack<>(PlayingSource::new);

        playingSources = new HashMap<>();
        defaultAudioSource = new AudioSource();

        SilenceEngine.eventManager.addDisposeHandler(this::cleanUp);
        SilenceEngine.eventManager.addUpdateHandler(this::updateSources);
    }

    public void stopAllSources()
    {
        for (PlayingSource source : playingSources.keySet())
        {
            source.alSource.stop();
            sourcesPool.push(source.alSource);
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
        for (PlayingSource playingSource : playingSources.keySet())
        {
            ALSource source = playingSource.alSource;
            AudioSource audioSource = playingSources.get(playingSource);

            if (audioSource.updated)
            {
                source.setParameter(AL_POSITION, audioSource.position);
                source.setParameter(AL_VELOCITY, audioSource.velocity);
                source.setParameter(AL_DIRECTION, audioSource.direction);

                audioSource.updated = false;
            }

            if (source.getState() != ALSource.State.PLAYING)
            {
                playingSources.remove(playingSource);
                sourcesPool.push(source);
                playingSourcesPool.push(playingSource);
            }
        }
    }

    public void play(Sound sound)
    {
        play(sound, defaultAudioSource, false);
    }

    public void play(Sound sound, boolean loop)
    {
        play(sound, defaultAudioSource, loop);
    }

    public void play(Sound sound, AudioSource source)
    {
        play(sound, source, false);
    }

    public void play(Sound sound, AudioSource source, boolean loop)
    {
        ALSource alSource = sourcesPool.pop();

        source.update();

        alSource.attachBuffer(sound.buffer);
        alSource.setParameter(AL_POSITION, source.position);
        alSource.setParameter(AL_VELOCITY, source.velocity);
        alSource.setParameter(AL_DIRECTION, source.direction);
        alSource.setParameter(AL_LOOPING, loop);

        alSource.play();

        PlayingSource playingSource = playingSourcesPool.pop();
        playingSource.sound = sound;
        playingSource.alSource = alSource;

        playingSources.put(playingSource, source);
    }

    public void stop(Sound sound)
    {
        for (PlayingSource source : playingSources.keySet())
        {
            if (source.sound.buffer.getID() == sound.buffer.getID())
                stop(playingSources.get(source));
        }
    }

    public void stop(AudioSource source)
    {
        for (PlayingSource playingSource : playingSources.keySet())
        {
            if (playingSources.get(playingSource) == source)
                playingSource.alSource.stop();
        }
    }

    private static class PlayingSource
    {
        private ALSource alSource;
        private Sound    sound;
    }
}
