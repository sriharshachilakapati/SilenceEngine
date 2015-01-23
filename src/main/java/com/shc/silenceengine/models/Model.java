package com.shc.silenceengine.models;

import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.scene.SceneNode;

/**
 * @author Sri Harsha Chilakapati
 */
public abstract class Model extends SceneNode
{
    public abstract void update(float delta);

    public abstract void render(float delta, Batcher batcher);

    public static Model load(String filename)
    {
        return null;
    }
}
