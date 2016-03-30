package com.shc.silenceengine.logging;

/**
 * @author Sri Harsha Chilakapati
 */
public interface ILogDevice
{
    Logger getLogger(String name);

    Logger getRootLogger();
}
