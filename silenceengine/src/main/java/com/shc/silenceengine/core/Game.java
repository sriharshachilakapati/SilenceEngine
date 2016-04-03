package com.shc.silenceengine.core;

/**
 * @author Sri Harsha Chilakapati
 */
public abstract class Game
{
    public static boolean DEVELOPMENT = true;

    public Game()
    {
        SilenceEngine.eventManager.addUpdateHandler(this::update);
        SilenceEngine.eventManager.addRenderHandler(this::render);
        SilenceEngine.eventManager.addResizeHandler(this::resized);
        SilenceEngine.eventManager.addDisposeHandler(this::dispose);
    }

    public void init()
    {
    }

    public void update(float deltaTime)
    {
    }

    public void render(float delta)
    {
    }

    public void dispose()
    {
    }

    public void resized()
    {
    }
}
