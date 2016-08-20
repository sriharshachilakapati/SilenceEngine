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
import java.util.Iterator;
import java.util.Map;

import static com.shc.silenceengine.audio.AudioDevice.Constants.*;

/**
 * <p>An AudioScene allows you to play positional sounds, in 3D. The scene updates itself whenever the game updates,
 * and also updates the position, direction and velocity of the updated sources. It acts like a master of all sounds.
 * </p>
 *
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

    /**
     * Stops all the sources and makes sure that no sound is playing from the game. This method does not stop the static
     * sounds of course because static sounds do not have a source attached.
     */
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
        Iterator<PlayingSource> iterator = playingSources.keySet().iterator();

        while (iterator.hasNext())
        {
            PlayingSource playingSource = iterator.next();
            ALSource source = playingSource.alSource;
            AudioSource audioSource = playingSources.get(playingSource);

            if (source.getState() != ALSource.State.PLAYING)
            {
                source.attachBuffer(null);

                iterator.remove();
                sourcesPool.push(source);
                playingSourcesPool.push(playingSource);

                continue;
            }

            if (audioSource.updated)
            {
                source.pause();

                source.setParameter(AL_POSITION, audioSource.position);
                source.setParameter(AL_VELOCITY, audioSource.velocity);
                source.setParameter(AL_DIRECTION, audioSource.direction);

                audioSource.updated = false;
                source.play();
            }
        }
    }

    /**
     * Plays a sound as a static audio, that is, it has no position and special effects.
     *
     * @param sound The sound to be played.
     */
    public void playStatic(Sound sound)
    {
        play(sound, defaultAudioSource, false);
    }

    /**
     * Plays a static sound, optionally allowing you to loop the sound.
     *
     * @param sound The sound to be played.
     * @param loop  Whether to loop the sound.
     */
    public void playStatic(Sound sound, boolean loop)
    {
        play(sound, defaultAudioSource, loop);
    }

    /**
     * Plays a sound through a specified AudioSource. The AudioSource instance specifies the spatial properties of the
     * sound to be played.
     *
     * @param sound  The Sound object to be played.
     * @param source The AudioSource object which describes the spatial properties.
     */
    public void play(Sound sound, AudioSource source)
    {
        play(sound, source, false);
    }

    /**
     * Plays a sound through a specified AudioSource, optionally allowing you to loop the sound. The AudioSource
     * instance specifies the spatial properties of the sound to be played.
     *
     * @param sound  The Sound object to be played.
     * @param source The AudioSource object which describes the spatial properties.
     * @param loop   Whether to play the sound in loop.
     */
    public void play(Sound sound, AudioSource source, boolean loop)
    {
        ALSource alSource = sourcesPool.pop();

        source.update();

        alSource.attachBuffer(sound.buffer);
        alSource.setParameter(AL_POSITION, source.position);
        alSource.setParameter(AL_VELOCITY, source.velocity);
        alSource.setParameter(AL_DIRECTION, source.direction);
        alSource.setParameter(AL_LOOPING, loop);

        source.updated = false;
        alSource.play();

        PlayingSource playingSource = playingSourcesPool.pop();
        playingSource.sound = sound;
        playingSource.alSource = alSource;

        playingSources.put(playingSource, source);
    }

    /**
     * Stops a sound which is playing from all the sources. Other sounds for the sources will not be effected.
     *
     * @param sound The sound to stop playing.
     */
    public void stopFromAllSources(Sound sound)
    {
        for (PlayingSource source : playingSources.keySet())
        {
            if (source.sound.buffer.getID() == sound.buffer.getID())
                source.alSource.stop();
        }
    }

    /**
     * Stops all the sounds that are playing from a specific source.
     *
     * @param source The source from which the sounds should be stopped.
     */
    public void stopAllFromSource(AudioSource source)
    {
        for (PlayingSource playingSource : playingSources.keySet())
        {
            if (playingSources.get(playingSource) == source)
                playingSource.alSource.stop();
        }
    }

    /**
     * Stops the sound which is being played statically without any spatial properties.
     *
     * @param sound The sound to be stopped.
     */
    public void stopStatic(Sound sound)
    {
        stop(sound, defaultAudioSource);
    }

    /**
     * Stops a sound from playing from a specific source.
     *
     * @param sound  The sound to be stopped.
     * @param source The source which is playing that sound.
     */
    public void stop(Sound sound, AudioSource source)
    {
        for (PlayingSource playingSource : playingSources.keySet())
        {
            if (playingSource.sound.buffer.getID() == sound.buffer.getID())
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
