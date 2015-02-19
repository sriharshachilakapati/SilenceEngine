package com.shc.silenceengine;

import com.shc.silenceengine.audio.AudioEngine;
import com.shc.silenceengine.collision.CollisionEngine;
import com.shc.silenceengine.core.NativesLoader;

/**
 * @author Sri Harsha Chilakapati
 */
public final class SilenceEngine implements IEngine
{
    private SilenceEngine()
    {
    }

    @Override
    public void init()
    {
        // We need to start AWT in Headless mode, Needed for AWT to work on OS X
        System.setProperty("java.awt.headless", "true");

        // Load LWJGL natives
        NativesLoader.loadLWJGL();

        // Create the other engines
        audio = new AudioEngine();
        collision = new CollisionEngine();

        // Initialize other engines
        audio.init();
        collision.init();
    }

    @Override
    public void dispose()
    {
        audio.dispose();
        collision.dispose();
    }

    public static enum Platform
    {
        WINDOWS_32, WINDOWS_64, MACOSX, LINUX_32, LINUX_64, UNKNOWN
    }

    public static Platform getPlatform()
    {
        final String OS   = System.getProperty("os.name").toLowerCase();
        final String ARCH = System.getProperty("os.arch").toLowerCase();

        boolean isWindows = OS.contains("windows");
        boolean isLinux   = OS.contains("linux");
        boolean isMac     = OS.contains("mac");
        boolean is64Bit   = ARCH.equals("amd64") || ARCH.equals("x86_64");

        if (isWindows) return is64Bit ? Platform.WINDOWS_64 : Platform.WINDOWS_32;
        if (isLinux)   return is64Bit ? Platform.LINUX_64   : Platform.LINUX_32;
        if (isMac)     return Platform.MACOSX;

        return Platform.UNKNOWN;
    }

    public static String getVersion()
    {
        return "0.0.3a";
    }

    public static AudioEngine audio;
    public static CollisionEngine collision;

    private static SilenceEngine instance;

    public static void start()
    {
        if (instance != null)
            return;

        instance = new SilenceEngine();
        instance.init();
    }

    public static void cleanUp()
    {
        if (instance == null)
            return;

        instance.dispose();
    }
}
