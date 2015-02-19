package com.shc.silenceengine;

/**
 * @author Sri Harsha Chilakapati
 */
public interface IEngine
{
    public void init();

    public void beginFrame();

    public void endFrame();

    public void dispose();
}
