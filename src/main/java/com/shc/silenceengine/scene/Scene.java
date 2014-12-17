package com.shc.silenceengine.scene;

import com.shc.silenceengine.graphics.Batcher;

/**
 * @author Sri Harsha Chilakapati
 */
public final class Scene extends SceneNode
{
    public Scene()
    {
    }

    public void preUpdate(float delta)
    {
    }

    public void preRender(float delta, Batcher batcher)
    {
    }

    public void update(float delta)
    {
        updateChildren(delta);
    }

    public void render(float delta, Batcher batcher)
    {
        renderChildren(delta, batcher);
    }
}
