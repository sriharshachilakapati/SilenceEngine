package com.shc.silenceengine.backend.lwjgl;

import com.shc.silenceengine.logging.ILogDevice;
import com.shc.silenceengine.logging.Logger;

/**
 * @author Sri Harsha Chilakapati
 */
public class LwjglLogDevice implements ILogDevice
{
    private Logger rootLogger = new LwjglLogger("SilenceEngine");

    @Override
    public Logger getLogger(String name)
    {
        return new LwjglLogger(name);
    }

    @Override
    public Logger getRootLogger()
    {
        return rootLogger;
    }
}
