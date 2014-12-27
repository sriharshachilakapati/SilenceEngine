package com.shc.silenceengine.utils;

import static org.lwjgl.glfw.GLFW.*;

/**
 * A standard Time Utility class
 */
public final class TimeUtils
{
    public static enum Unit
    {
        NANOS, MICROS, MILLIS, SECONDS
    }

    private TimeUtils()
    {
    }

    public static double currentNanos()
    {
        return currentMicros() * 1000.0;
    }

    public static double currentMicros()
    {
        return currentMillis() * 1000.0;
    }

    public static double currentMillis()
    {
        return currentSeconds() * 1000.0;
    }

    public static double currentSeconds()
    {
        return glfwGetTime();
    }

    public static double currentTime(Unit unit)
    {
        switch (unit)
        {
            case NANOS: return currentNanos();
            case MICROS: return currentMicros();
            case MILLIS: return currentMillis();
        }

        return currentSeconds();
    }

    public static double convert(double time, Unit source, Unit target)
    {
        if (source == target)
            return time;

        double factor = 1;

        if (source == Unit.SECONDS)
        {
            if (target == Unit.MILLIS)
                factor = 1000.0;
            else if (target == Unit.MICROS)
                factor = 1000000.0;
            else
                factor = 1000000000.0;
        }
        else if (source == Unit.MILLIS)
        {
            if (target == Unit.SECONDS)
                factor = 1/1000.0;
            else if (target == Unit.MICROS)
                factor = 1000.0;
            else
                factor = 1000000.0;
        }
        else if (source == Unit.MICROS)
        {
            if (target == Unit.SECONDS)
                factor = 1/1000000.0;
            else if (target == Unit.MILLIS)
                factor = 1/1000.0;
            else
                factor = 1000.0;
        }
        else
        {
            if (target == Unit.SECONDS)
                factor = 1/1000000000.0;
            else if (target == Unit.MILLIS)
                factor = 1/1000000.0;
            else if (target == Unit.MICROS)
                factor = 1/1000.0;
        }

        return time * factor;
    }
}
