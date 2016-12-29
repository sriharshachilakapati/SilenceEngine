/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2016 Sri Harsha Chilakapati
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
import com.shc.silenceengine.backend.lwjgl.glfw.Monitor;
import com.shc.silenceengine.backend.lwjgl.glfw.VideoMode;
import com.shc.silenceengine.backend.lwjgl.glfw.Window;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.core.IDisplayDevice;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.utils.functional.SimpleCallback;
import com.shc.silenceengine.utils.functional.UniCallback;
import org.lwjgl.glfw.GLFW;

import static com.shc.silenceengine.core.SilenceEngine.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.util.tinyfd.TinyFileDialogs.*;

/**
 * An implementation of the Display using GLFW as the windowing backend.
 *
 * @author Sri Harsha Chilakapati
 */
public class LwjglDisplayDevice implements IDisplayDevice
{
    public  Window   window;
    private Platform platform;

    private int windowWidth, windowHeight;
    private int windowPositionX, windowPositionY;

    private boolean fullscreen;
    private boolean focus = true;

    public LwjglDisplayDevice()
    {
        GLFW3.init();

        Window.setHint(GLFW.GLFW_OPENGL_DEBUG_CONTEXT, Game.DEVELOPMENT);
        Window.setHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        Window.setHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
        Window.setHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        Window.setHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);

        windowWidth = 800;
        windowHeight = 600;

        window = new Window(windowWidth, windowHeight);

        centerOnScreen();
        window.show();
        window.makeCurrent();

        window.setPositionCallback((window1, xPos, yPos) ->
        {
            if (!fullscreen)
            {
                windowPositionX = xPos;
                windowPositionY = yPos;
            }
        });

        window.setSizeCallback((window1, width, height) ->
        {
            if (!fullscreen)
            {
                windowWidth = width;
                windowHeight = height;
            }

            SilenceEngine.eventManager.raiseResizeEvent();
        });

        window.setFocusCallback((window1, focus1) ->
        {
            focus = focus1;

            if (focus)
                SilenceEngine.gameLoop.onFocusGain();
            else
                SilenceEngine.gameLoop.onFocusLost();
        });

        SilenceEngine.eventManager.addDisposeHandler(this::cleanUp);

        GLFW3.setSwapInterval(1);
    }

    @Override
    public Platform getPlatform()
    {
        if (platform == null)
        {
            final String OS = System.getProperty("os.name").toLowerCase();
            final String ARCH = System.getProperty("os.arch").toLowerCase();

            boolean isWindows = OS.contains("windows");
            boolean isLinux = OS.contains("linux");
            boolean isMac = OS.contains("mac");
            boolean is64Bit = ARCH.equals("amd64") || ARCH.equals("x86_64");

            platform = Platform.UNKNOWN;

            if (isWindows) platform = is64Bit ? Platform.WINDOWS_64 : Platform.WINDOWS_32;
            if (isLinux) platform = is64Bit ? Platform.LINUX_64 : Platform.UNKNOWN;
            if (isMac) platform = Platform.MACOSX;
        }

        return platform;
    }

    @Override
    public void setSize(int width, int height)
    {
        window.setSize(width, height);

        if (!fullscreen)
        {
            windowWidth = width;
            windowHeight = height;
        }

        SilenceEngine.eventManager.raiseResizeEvent();
    }

    @Override
    public boolean isFullscreen()
    {
        return fullscreen;
    }

    @Override
    public void setFullscreen(boolean fullscreen)
    {
        if (fullscreen)
        {
            this.fullscreen = true;
            window.setMonitor(Monitor.getPrimaryMonitor());
        }
        else
        {
            window.setMonitor(null);
            window.setSize(windowWidth, windowHeight);
            window.setPosition(windowPositionX, windowPositionY);

            this.fullscreen = false;
        }

        SilenceEngine.eventManager.raiseResizeEvent();
    }

    @Override
    public void centerOnScreen()
    {
        VideoMode mode = Monitor.getPrimaryMonitor().getVideoMode();

        Vector2 windowPosition = window.getSize();

        windowPosition.x = (mode.getWidth() - windowPosition.x) / 2;
        windowPosition.y = (mode.getHeight() - windowPosition.y) / 2;

        window.setPosition(windowPosition);

        windowPositionX = (int) windowPosition.x;
        windowPositionY = (int) windowPosition.y;
    }

    @Override
    public void setPosition(int x, int y)
    {
        window.setPosition(x, y);

        windowPositionX = x;
        windowPositionY = y;
    }

    @Override
    public int getWidth()
    {
        return (int) window.getSize().x;
    }

    @Override
    public int getHeight()
    {
        return (int) window.getSize().y;
    }

    @Override
    public String getTitle()
    {
        return window.getTitle();
    }

    @Override
    public void setTitle(String title)
    {
        window.setTitle(title);
    }

    @Override
    public void setIcon(FilePath filePath, SimpleCallback callback, UniCallback<Throwable> error)
    {
        window.setIcon(filePath, callback, error);
    }

    @Override
    public void close()
    {
        window.setShouldClose(true);
    }

    @Override
    public double nanoTime()
    {
        return System.nanoTime();
    }

    @Override
    public void setVSync(boolean vSync)
    {
        GLFW3.setSwapInterval(vSync ? 1 : 0);
    }

    @Override
    public boolean hasFocus()
    {
        return focus;
    }

    @Override
    public void setGrabMouse(boolean grabMouse)
    {
        if (grabMouse)
            window.setInputMode(GLFW_CURSOR, GLFW_CURSOR_NORMAL);
        else
            window.setInputMode(GLFW_CURSOR, GLFW_CURSOR_DISABLED);
    }

    @Override
    public String prompt(String message, String defaultValue)
    {
        // This is because of a bug in TinyFileDialogs, the build crashes if there
        // are \t or \n characters in the message shown to the user.
        if (platform == Platform.WINDOWS_32 || platform == Platform.WINDOWS_64)
            message = message.replaceAll("[\\t\\n]", "");

        return tinyfd_inputBox(getTitle(), message, defaultValue);
    }

    @Override
    public boolean confirm(String message)
    {
        return tinyfd_messageBox(getTitle(), message, "yesno", "question", true);
    }

    @Override
    public void alert(String message)
    {
        tinyfd_messageBox(getTitle(), message, "ok", "info", true);
    }

    private void cleanUp()
    {
        window.destroy();
        GLFW3.terminate();
    }
}
