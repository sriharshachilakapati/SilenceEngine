package com.shc.silenceengine.utils;

import static org.lwjgl.glfw.GLFW.*;

/**
 * A standard Time Utility class
 */
public class TimeUtils
{
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
}
