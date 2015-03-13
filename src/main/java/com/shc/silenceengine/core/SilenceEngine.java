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

package com.shc.silenceengine.core;

import com.shc.silenceengine.audio.AudioEngine;
import com.shc.silenceengine.collision.CollisionEngine;
import com.shc.silenceengine.core.glfw.GLFW3;
import com.shc.silenceengine.graphics.GraphicsEngine;
import com.shc.silenceengine.input.InputEngine;
import com.shc.silenceengine.utils.Logger;
import com.shc.silenceengine.utils.NativesLoader;
import org.lwjgl.Sys;

/**
 * @author Sri Harsha Chilakapati
 */
public final class SilenceEngine implements IEngine
{
    static
    {
        // Every exception occurs after SilenceException, even
        // the uncaught exceptions are thrown as runtime exceptions
        Thread.setDefaultUncaughtExceptionHandler((t, e) ->
        {
            try
            {
                // No need to rethrow SilenceException
                if (e instanceof SilenceException)
                    throw e;

                SilenceException.reThrow(e);
            }
            catch (Throwable ex)
            {
                ex.printStackTrace();
                System.exit(-1);
            }
        });
    }
    private static SilenceEngine   instance;
    public static  GraphicsEngine  graphics;
    public static  AudioEngine     audio;
    public static  CollisionEngine collision;
    public static  InputEngine     input;

    private SilenceEngine()
    {
    }

    public static IEngine getInstance()
    {
        if (instance == null)
            instance = new SilenceEngine();

        return instance;
    }

    @Override
    public void init()
    {
        Logger.log("Initializing SilenceEngine. Platform identified as " + getPlatform());

        if (getPlatform() == Platform.MACOSX)
        {
            Logger.log("Running AWT fix on Mac OS X, needed for LWJGL to run");

            // We need to start AWT in Headless mode, Needed for AWT to work on OS X
            System.setProperty("java.awt.headless", "true");
        }

        Logger.log("Initializing LWJGL library. Extracting natives");

        // Load LWJGL natives
        NativesLoader.loadLWJGL();

        Logger.log("LWJGL version " + Sys.getVersion() + " is initialised");

        // Initialize GLFW
        if (!GLFW3.init())
            throw new SilenceException("Error initializing GLFW. Your system is unsupported.");

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

        Logger.log("SilenceEngine version " + getVersion() + " was initialized successfully");
    }

    public static String getVersion()
    {
        return "0.0.3a";
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

        Logger.log("Terminating GLFW library");
        GLFW3.terminate();

        Logger.log("SilenceEngine version " + getVersion() + " was successfully terminated");
    }

    public static enum Platform
    {
        WINDOWS_32, WINDOWS_64, MACOSX, LINUX_32, LINUX_64, UNKNOWN
    }
}
