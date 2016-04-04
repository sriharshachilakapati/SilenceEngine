package com.shc.silenceengine.audio;

import com.shc.silenceengine.math.Vector3;

/**
 * @author Sri Harsha Chilakapati
 */
public class AudioSource
{
    public final Vector3 position  = new Vector3();
    public final Vector3 velocity  = new Vector3();
    public final Vector3 direction = new Vector3();

    boolean updated = true;

    public void update()
    {
        // The updating to ALSource will happen in AudioScene
        updated = true;
    }
}
