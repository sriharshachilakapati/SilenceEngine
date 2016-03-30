package com.shc.silenceengine.logging;

/**
 * @author Sri Harsha Chilakapati
 */
public abstract class Logger
{
    public final String name;

    public Logger(String name)
    {
        this.name = name;
    }

    public abstract void info(Object... messages);

    public abstract void warn(Object... messages);

    public abstract void error(Object... messages);

    public void log(Level level, Object... messages)
    {
        switch (level)
        {
            case INFO:
                info(messages);
                break;

            case WARNING:
                warn(messages);
                break;

            case ERROR:
                error(messages);
                break;
        }
    }

    public enum Level
    {
        INFO,
        WARNING,
        ERROR
    }
}
