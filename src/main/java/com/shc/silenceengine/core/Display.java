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

import com.shc.silenceengine.core.glfw.GLFW3;
import com.shc.silenceengine.core.glfw.Monitor;
import com.shc.silenceengine.core.glfw.VideoMode;
import com.shc.silenceengine.core.glfw.Window;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.opengl.Program;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.input.Mouse;
import com.shc.silenceengine.math.Vector2;

import static org.lwjgl.glfw.GLFW.*;

/**
 * The Display class takes care of creating a display and managing it. As a user of SilenceEngine, I don't expect you to
 * create the Display, the display creation will be handled automatically by the Game class.
 *
 * @author Sri Harsha Chilakapati
 */
public final class Display
{
    private static Window displayWindow;

    private static boolean resizable  = true;
    private static boolean resized    = true;
    private static boolean fullScreen = false;
    private static boolean dirty      = true;
    private static boolean vSync      = true;

    private static int width  = 800;
    private static int height = 600;

    private static int oldWidth;
    private static int oldHeight;
    private static int oldPosX;
    private static int oldPosY;

    private static int posX = 0;
    private static int posY = 0;

    private static Monitor   monitor   = null;
    private static VideoMode videoMode = null;

    private static void setHints()
    {
        Window.setHint(GLFW_SAMPLES, 4);

        if (SilenceEngine.getPlatform() == SilenceEngine.Platform.MACOSX)
        {
            Window.setHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
            Window.setHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        }
        else
        {
            Window.setHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
            Window.setHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        }

        Window.setHint(GLFW_OPENGL_FORWARD_COMPAT, true);
        Window.setHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        Window.setHint(GLFW_VISIBLE, false);
        Window.setHint(GLFW_RESIZABLE, resizable);

        GLFW3.setSwapInterval(vSync ? 1 : 0);
    }

    private static void setCallbacks(Window window)
    {
        window.setPositionCallback(Display::glfwPositionCallback);
        window.setSizeCallback(Display::glfwSizeCallback);
        window.setKeyCallback(Keyboard::glfwKeyCallback);

        window.setMouseButtonCallback(Mouse::glfwMouseButtonCallback);
        window.setScrollCallback(Mouse::glfwScrollCallback);
        window.setCursorPositionCallback(Mouse::glfwCursorCallback);
    }

    private static void clearHints()
    {
        Window.setDefaultHints();
    }

    private static Window createWindow(int width, int height, String title, Monitor monitor, Window share)
    {
        if (Game.getBatcher() != null)
            Game.getBatcher().dispose();

        setHints();
        Window window = new Window(width, height, title, monitor, share);
        window.makeCurrent();
        setCallbacks(window);
        clearHints();

        window.setPosition(posX, posY);

        dirty = true;

        Game.setBatcher(new Batcher());

        return window;
    }

    public static void create()
    {
        displayWindow = createWindow(width, height, "SilenceEngine Window", monitor, null);
        displayWindow.makeCurrent();
        displayWindow.show();
    }

    public static void centerOnScreen()
    {
        if (monitor != null)
            return;

        VideoMode videoMode = Monitor.getPrimaryMonitor().getVideoMode();
        setPosition((videoMode.getWidth() - width) / 2, (videoMode.getHeight() - height) / 2);
    }

    public static void makeCurrent()
    {
        if (displayWindow != null)
            displayWindow.makeCurrent();
    }

    public static boolean isCloseRequested()
    {
        return displayWindow != null && displayWindow.shouldClose();
    }

    public static void show()
    {
        if (displayWindow == null)
            return;

        displayWindow.show();
    }

    public static void hide()
    {
        if (displayWindow == null)
            return;

        displayWindow.hide();
    }

    public static void hideCursor()
    {
        if (displayWindow == null)
            return;

        displayWindow.setInputMode(GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
    }

    public static void grabCursor()
    {
        if (displayWindow == null)
            return;

        displayWindow.setInputMode(GLFW_CURSOR, GLFW_CURSOR_DISABLED);
    }

    public static void showCursor()
    {
        if (displayWindow == null)
            return;

        displayWindow.setInputMode(GLFW_CURSOR, GLFW_CURSOR_NORMAL);
    }

    public static void update()
    {
        if (displayWindow == null)
            return;

        displayWindow.swapBuffers();
        GLFW3.pollEvents();

        // Force binding
        Program.CURRENT = null;
        Texture.CURRENT = null;

        Program.DEFAULT.use();
        Texture.EMPTY.bind();
    }

    public static void destroy()
    {
        if (displayWindow == null)
            return;

        displayWindow.destroy();
    }

    public static boolean wasResized()
    {
        if (resized)
        {
            resized = false;
            return true;
        }

        return false;
    }

    public static boolean wasDirty()
    {
        if (dirty)
        {
            dirty = false;
            return true;
        }

        return false;
    }

    public static Window getWindow()
    {
        return displayWindow;
    }

    public static int getWidth()
    {
        return width;
    }

    public static int getHeight()
    {
        return height;
    }

    public static void setWidth(int width)
    {
        if (displayWindow == null)
            return;

        Display.width = width;
        displayWindow.setSize(width, getHeight());
    }

    public static void setHeight(int height)
    {
        if (displayWindow == null)
            return;

        Display.height = height;
        displayWindow.setSize(getWidth(), height);
    }

    public static Vector2 getSize()
    {
        if (displayWindow == null)
            return Vector2.ZERO;

        Vector2 size = displayWindow.getSize();

        Display.width = (int) size.x;
        Display.height = (int) size.y;

        return size;
    }

    public static void setSize(int width, int height)
    {
        if (displayWindow == null)
            return;

        Display.width = width;
        Display.height = height;
        displayWindow.setSize(width, height);
    }

    public static void setSize(Vector2 size)
    {
        setSize((int) size.x, (int) size.y);
    }

    public static Vector2 getPosition()
    {
        if (displayWindow == null)
            return Vector2.ZERO;

        Vector2 position = displayWindow.getPosition();

        Display.posX = (int) position.x;
        Display.posY = (int) position.y;

        return position;
    }

    public static void setPosition(int x, int y)
    {
        if (displayWindow == null)
            return;

        Display.posX = x;
        Display.posY = y;

        displayWindow.setPosition(x, y);
    }

    public static void setPosition(Vector2 p)
    {
        setPosition((int) p.x, (int) p.y);
    }

    public static boolean isResizable()
    {
        return resizable;
    }

    public static void setResizable(boolean resizable)
    {
        if (Display.resizable == resizable)
            return;

        Display.resizable = resizable;

        Window resizableWindow = createWindow(width, height, getTitle(), monitor, displayWindow);

        displayWindow.destroy();
        displayWindow = resizableWindow;
        displayWindow.makeCurrent();

        dirty = true;

        hide();
        show();

        // Update the Display
        update();
    }

    public static boolean getVSync()
    {
        return vSync;
    }

    public static void setVSync(boolean vSync)
    {
        Display.vSync = vSync;
        GLFW3.setSwapInterval(vSync ? 1 : 0);
    }

    public static String getTitle()
    {
        return displayWindow.getTitle();
    }

    public static void setTitle(String title)
    {
        if (displayWindow == null)
            return;

        displayWindow.setTitle(title);
    }

    public static float getAspectRatio()
    {
        if (displayWindow == null)
            return 0;

        return (float) getWidth() / (float) getHeight();
    }

    public static boolean isFullScreen()
    {
        return fullScreen;
    }

    public static void setFullScreen(boolean fullScreen)
    {
        if (Display.fullScreen == fullScreen)
        return;

        Display.fullScreen = fullScreen;

        if (fullScreen)
        {
            VideoMode videoMode = Monitor.getPrimaryMonitor().getVideoMode();

            // Save window size
            oldWidth = width;
            oldHeight = height;

            // Save window position
            oldPosX = posX;
            oldPosY = posY;

            width = videoMode.getWidth();
            height = videoMode.getHeight();

            monitor = Monitor.getPrimaryMonitor();
        }
        else
        {
            // Restore window size
            width = oldWidth;
            height = oldHeight;

            // Restore window position
            posX = oldPosX;
            posY = oldPosY;

            monitor = null;
        }

        // Create new window
        Window fsDisplayWindow = createWindow(width, height, displayWindow.getTitle(), monitor, displayWindow);
        displayWindow.destroy();
        displayWindow = fsDisplayWindow;
        displayWindow.makeCurrent();

        setPosition(posX, posY);
        setSize(width, height);

        hide();
        show();

        // Make an update
        update();

        dirty = true;
    }

    public static VideoMode getVideoMode()
    {
        return videoMode;
    }

    public static void setVideoMode(VideoMode videoMode)
    {
        if (Display.videoMode == videoMode)
            return;

        if (videoMode == null)
        {
            setFullScreen(false);
            return;
        }

        Display.videoMode = videoMode;

        setFullScreen(true);
        setSize(videoMode.getWidth(), videoMode.getHeight());

        update();
        dirty = true;
    }

    public static Monitor getMonitor()
    {
        return monitor;
    }

    public static void setMonitor(Monitor monitor)
    {
        if (Display.monitor == monitor)
            return;

        Display.monitor = monitor;
        Display.fullScreen = true;

        videoMode = Monitor.getPrimaryMonitor().getVideoMode();

        // Save window size
        oldWidth = width;
        oldHeight = height;

        // Save window position
        oldPosX = posX;
        oldPosY = posY;

        width = videoMode.getWidth();
        height = videoMode.getHeight();

        // Create new window
        Window fsDisplayWindow = createWindow(width, height, displayWindow.getTitle(), monitor, displayWindow);
        displayWindow.destroy();
        displayWindow = fsDisplayWindow;
        displayWindow.makeCurrent();

        setPosition(posX, posY);
        setSize(width, height);

        hide();
        show();

        // Make an update
        update();

        dirty = true;
    }

    /*
     * This part consists of different public GLFW callbacks, that make the Display
     * fully functional. If you are overriding the callbacks, please invoke these.
     */

    public static void glfwPositionCallback(Window window, int x, int y)
    {
        Display.posX = x;
        Display.posY = y;
    }

    public static void glfwSizeCallback(Window window, int w, int h)
    {
        Display.width = w;
        Display.height = h;

        resized = true;
    }
}
