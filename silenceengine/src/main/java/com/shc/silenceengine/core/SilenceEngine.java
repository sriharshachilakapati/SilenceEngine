package com.shc.silenceengine.core;

import com.shc.silenceengine.graphics.IGraphicsDevice;
import com.shc.silenceengine.io.IODevice;

/**
 * The SilenceEngine class is the core of the entire engine, and contains all the devices for a platform that we are
 * running upon. It also explains which platform are we running.
 *
 * @author Sri Harsha Chilakapati
 */
public final class SilenceEngine
{
    private SilenceEngine()
    {
    }

    /**
     * The {@link IDisplayDevice} that handles the display, that is the window thing.
     */
    public static IDisplayDevice display;

    /**
     * The {@link IGraphicsDevice} that handles the graphics.
     */
    public static IGraphicsDevice graphics;

    /**
     * The {@link IODevice} that handles IO operations.
     */
    public static IODevice io;

    public enum Platform
    {
        WINDOWS_32,
        WINDOWS_64,
        LINUX_64,
        MACOSX,
        HTML5,
        UNKNOWN
    }
}
