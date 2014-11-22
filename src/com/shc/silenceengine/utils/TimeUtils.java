package com.shc.silenceengine.utils;

/**
 * A standard Time Utility class
 */
public class TimeUtils
{
    public static long currentNanos()
    {
        return System.nanoTime();
    }

    public static long currentMicros()
    {
        return currentNanos()/1000;
    }

    public static long currentMillis()
    {
        return currentNanos()/1000000;
    }

    public static long currentSeconds()
    {
        return currentNanos()/1000000000;
    }
}
