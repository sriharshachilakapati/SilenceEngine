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
