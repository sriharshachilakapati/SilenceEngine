package com.shc.silenceengine.backend.lwjgl;

import com.shc.silenceengine.logging.Logger;

/**
 * @author Sri Harsha Chilakapati
 */
public class LwjglLogger extends Logger
{
    public LwjglLogger(String name)
    {
        super(name);
    }

    @Override
    public void info(Object... messages)
    {
        for (Object message : messages)
            System.out.println("[" + name + "] INFO: " + message);
    }

    @Override
    public void warn(Object... messages)
    {
        for (Object message : messages)
            System.err.println("[" + name + "] WARN: " + message);
    }

    @Override
    public void error(Object... messages)
    {
        for (Object message : messages)
            System.err.println("[" + name + "] ERROR: " + message);
    }
}
