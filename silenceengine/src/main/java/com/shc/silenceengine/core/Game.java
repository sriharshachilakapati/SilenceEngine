package com.shc.silenceengine.core;

import com.shc.silenceengine.core.gameloops.VariableTimeSteppedLoop;

/**
 * @author Sri Harsha Chilakapati
 */
public abstract class Game
{
    public static boolean DEVELOPMENT = true;
    public static Game    INSTANCE;

    public Game()
    {
        INSTANCE = this;
        SilenceEngine.gameLoop = new VariableTimeSteppedLoop();
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
