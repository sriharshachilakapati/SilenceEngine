package com.shc.silenceengine;

import com.shc.silenceengine.audio.AudioEngine;
import com.shc.silenceengine.collision.CollisionEngine;
import com.shc.silenceengine.core.NativesLoader;
import com.shc.silenceengine.graphics.GraphicsEngine;
import com.shc.silenceengine.input.InputEngine;

/**
 * @author Sri Harsha Chilakapati
 */
public final class SilenceEngine implements IEngine
{
    public static GraphicsEngine graphics;
    public static AudioEngine audio;
    public static CollisionEngine collision;
    public static InputEngine input;
    private static SilenceEngine instance;

    private SilenceEngine()
    {
    }

    public static IEngine getInstance()
    {
        if (instance == null)
            instance = new SilenceEngine();

        return instance;
    }

    public static String getVersion()
    {
        return "0.0.3a";
    }

    @Override
    public void init()
    {
        if (getPlatform() == Platform.MACOSX)
        {
            // We need to start AWT in Headless mode, Needed for AWT to work on OS X
            System.setProperty("java.awt.headless", "true");
        }

        // Load LWJGL natives
        NativesLoader.loadLWJGL();

        // Create the other engines
        graphics = new GraphicsEngine();
        audio = new AudioEngine();
        collision = new CollisionEngine();
        input = new InputEngine();

        // Initialize other engines
        graphics.init();
        audio.init();
        collision.init();
        input.init();
    }

    public static Platform getPlatform()
    {
        final String OS = System.getProperty("os.name").toLowerCase();
        final String ARCH = System.getProperty("os.arch").toLowerCase();

        boolean isWindows = OS.contains("windows");
        boolean isLinux = OS.contains("linux");
        boolean isMac = OS.contains("mac");
        boolean is64Bit = ARCH.equals("amd64") || ARCH.equals("x86_64");

        if (isWindows) return is64Bit ? Platform.WINDOWS_64 : Platform.WINDOWS_32;
        if (isLinux) return is64Bit ? Platform.LINUX_64 : Platform.LINUX_32;
        if (isMac) return Platform.MACOSX;

        return Platform.UNKNOWN;
    }

    @Override
    public void beginFrame()
    {
        graphics.beginFrame();
        audio.beginFrame();
        collision.beginFrame();
        input.beginFrame();
    }

    @Override
    public void endFrame()
    {
        graphics.endFrame();
        audio.endFrame();
        collision.endFrame();
        input.endFrame();
    }

    @Override
    public void dispose()
    {
        audio.dispose();
        collision.dispose();
        input.dispose();
        graphics.dispose();
    }

    public static enum Platform
    {
        WINDOWS_32, WINDOWS_64, MACOSX, LINUX_32, LINUX_64, UNKNOWN
    }
}
