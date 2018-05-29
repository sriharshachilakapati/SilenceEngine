/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 Sri Harsha Chilakapati
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

package com.shc.silenceengine.backend.lwjgl;

import com.shc.silenceengine.backend.lwjgl.glfw.GLFW3;
import com.shc.silenceengine.backend.lwjgl.glfw.Window;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.utils.TaskManager;
import com.shc.silenceengine.utils.functional.SimpleCallback;
import org.lwjgl.system.Configuration;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * The LwjglRuntime initializes the LWJGL library, and starts the native event loop.
 *
 * @author Sri Harsha Chilakapati
 */
public final class LwjglRuntime
{
    private static SilenceEngine.Platform platform;

    private LwjglRuntime()
    {
    }

    /**
     * Useful utility to restart the JVM automatically with -XstartOnFirstThread argument on MacOSX as required by GLFW.
     * Method originally written by <b>Kappa</b> on the Java-Gaming forums. This code was from shared code snippet which
     * was copied from http://www.java-gaming.org/topics/starting-jvm-on-mac-with-xstartonfirstthread-programmatically/37697/view.html
     */
    private static void checkForXstartOnFirstThread()
    {
        // get current jvm process pid
        String pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
        // get environment variable on whether XstartOnFirstThread is enabled
        String env = System.getenv("JAVA_STARTED_ON_FIRST_THREAD_" + pid);

        // if environment variable is "1" then XstartOnFirstThread is enabled
        if (env != null && env.equals("1"))
            return;

        SilenceEngine.log.getRootLogger().warn("Restarting the JVM to start with -XstartOnFirstThread flag");

        // restart jvm with -XstartOnFirstThread
        String separator = System.getProperty("file.separator");
        String classpath = System.getProperty("java.class.path");
        String mainClass = System.getenv("JAVA_MAIN_CLASS_" + pid);
        String jvmPath = System.getProperty("java.home") + separator + "bin" + separator + "java";

        if (mainClass == null)
        {
            StackTraceElement[] stack = Thread.currentThread().getStackTrace();
            StackTraceElement main = stack[stack.length - 1];
            mainClass = main.getClassName();
        }

        List<String> inputArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();

        List<String> jvmArgs = new ArrayList<>();

        jvmArgs.add(jvmPath);
        jvmArgs.add("-XstartOnFirstThread");
        jvmArgs.addAll(inputArguments);
        jvmArgs.add("-cp");
        jvmArgs.add(classpath);
        jvmArgs.add(mainClass);

        try
        {
            ProcessBuilder processBuilder = new ProcessBuilder(jvmArgs);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            String line;

            while ((line = br.readLine()) != null)
                System.out.println(line);

            process.waitFor();
            System.exit(process.exitValue());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        System.exit(-1);
    }

    public static void start(Game game)
    {
        SilenceEngine.log = new LwjglLogDevice();

        // Check for -XstartOnFirstThread on Mac OS X
        if (getPlatform() == SilenceEngine.Platform.MACOSX)
            checkForXstartOnFirstThread();

        Configuration.DEBUG.set(Game.DEVELOPMENT);

        SilenceEngine.io = new LwjglIODevice();
        SilenceEngine.display = new LwjglDisplayDevice();
        SilenceEngine.input = new LwjglInputDevice();
        SilenceEngine.graphics = new LwjglGraphicsDevice();
        SilenceEngine.audio = new LwjglAudioDevice();

        // Set AWT fix on Mac OS X
        if (getPlatform() == SilenceEngine.Platform.MACOSX)
            System.setProperty("java.awt.headless", "true");

        Window window = ((LwjglDisplayDevice) SilenceEngine.display).window;

        final SimpleCallback[] performLoopFrame = {
                () ->
                {
                    // Assume 100 seconds so that tasks are force flushed
                    TaskManager.forceUpdateTasks(100);
                    TaskManager.forceRenderTasks(100);
                }
        };

        final boolean[] gameDone = { false };

        // Stop events from happening until game is initialized
        SilenceEngine.eventManager.waitUntil(() -> gameDone[0]);

        // Initiate setting the icon
        SilenceEngine.display.setIcon(FilePath.getResourceFile("engine_resources/icon.png"), () ->
        {
            // Initialize SilenceEngine
            SilenceEngine.init(() ->
            {
                // Call the game's init method
                game.init();
                gameDone[0] = true;

                // Replace the callback so that we now perform the game loop instead of flushing
                // tasks in TaskManager.
                performLoopFrame[0] = SilenceEngine.gameLoop::performLoopFrame;

                // Raise a resize event now
                SilenceEngine.eventManager.raiseResizeEvent();
            });
        });

        // The native event loop
        while (!window.shouldClose())
        {
            GLFW3.pollEvents();
            LwjglInputDevice.pollControllers();

            performLoopFrame[0].invoke();
            window.swapBuffers();
        }

        // Raise the dispose event finally
        SilenceEngine.eventManager.raiseDisposeEvent();
    }

    static SilenceEngine.Platform getPlatform()
    {
        if (platform == null)
        {
            final String OS = System.getProperty("os.name").toLowerCase();
            final String ARCH = System.getProperty("os.arch").toLowerCase();

            boolean isWindows = OS.contains("windows");
            boolean isLinux = OS.contains("linux");
            boolean isMac = OS.contains("mac");
            boolean is64Bit = ARCH.equals("amd64") || ARCH.equals("x86_64");

            platform = SilenceEngine.Platform.UNKNOWN;

            if (isWindows) platform = is64Bit ? SilenceEngine.Platform.WINDOWS_64 : SilenceEngine.Platform.WINDOWS_32;
            if (isLinux) platform = is64Bit ? SilenceEngine.Platform.LINUX_64 : SilenceEngine.Platform.UNKNOWN;
            if (isMac) platform = SilenceEngine.Platform.MACOSX;
        }

        return platform;
    }
}
