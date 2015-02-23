/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2015 Sri Harsha Chilakapati
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.shc.silenceengine.utils;

import static org.lwjgl.glfw.GLFW.*;

/**
 * A standard Time Utility class
 *
 * @author Sri Harsha Chilakapati
 */
public final class TimeUtils
{
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
            case NANOS:
                return currentNanos();
            case MICROS:
                return currentMicros();
            case MILLIS:
                return currentMillis();
        }

        return currentSeconds();
    }

    public static double currentTime()
    {
        return currentTime(getDefaultTimeUnit());
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
                factor = 1 / 1000.0;
            else if (target == Unit.MICROS)
                factor = 1000.0;
            else
                factor = 1000000.0;
        }
        else if (source == Unit.MICROS)
        {
            if (target == Unit.SECONDS)
                factor = 1 / 1000000.0;
            else if (target == Unit.MILLIS)
                factor = 1 / 1000.0;
            else
                factor = 1000.0;
        }
        else
        {
            if (target == Unit.SECONDS)
                factor = 1 / 1000000000.0;
            else if (target == Unit.MILLIS)
                factor = 1 / 1000000.0;
            else if (target == Unit.MICROS)
                factor = 1 / 1000.0;
        }

        return time * factor;
    }

    public static Unit getDefaultTimeUnit()
    {
        return Unit.SECONDS;
    }

    public static enum Unit
    {
        NANOS, MICROS, MILLIS, SECONDS
    }
}
