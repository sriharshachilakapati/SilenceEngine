package com.shc.silenceengine.core.events;

/**
 *@author Sri Harsha Chilakapati
 */
@FunctionalInterface
public interface IRenderEventHandler
{
    void render(float delta);
}
