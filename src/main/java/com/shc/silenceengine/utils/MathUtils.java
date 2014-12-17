package com.shc.silenceengine.utils;

/**
 * @author Sri Harsha Chilakapati
 */
public final class MathUtils
{
    private MathUtils()
    {
    }

    public static int clamp(int val, int min, int max)
    {
        val = Math.min(val, max);
        val = Math.max(val, min);

        return val;
    }

    public static float clamp(float val, float min, float max)
    {
        val = Math.min(val, max);
        val = Math.max(val, min);

        return val;
    }
}
