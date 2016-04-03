package com.shc.silenceengine.core.events;

/**
 * @author Sri Harsha Chilakapati
 */
@FunctionalInterface
public interface IUpdateEventHandler
{
    void update(float deltaTime);
}
