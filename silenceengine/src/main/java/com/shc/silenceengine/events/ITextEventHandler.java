package com.shc.silenceengine.events;

/**
 * @author Sri Harsha Chilakapati
 */
@FunctionalInterface
public interface ITextEventHandler
{
    void invoke(char... chars);
}
