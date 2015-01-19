package com.shc.silenceengine.core;

import com.shc.silenceengine.graphics.Batcher;

/**
 * @author Sri Harsha Chilakapati
 */
public abstract class GameState
{
    public void onEnter()
    {
    }

    public void update(float delta)
    {
    }

    public void render(float delta, Batcher batcher)
    {
    }

    public void resize()
    {
    }

    public void onLeave()
    {
    }
}
