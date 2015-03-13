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
import com.shc.silenceengine.utils.MathUtils;

import static org.lwjgl.glfw.GLFW.*;

/**
 * The Display class takes care of creating a display and managing it. As a user of SilenceEngine, I don't expect you to
 * create the Display, the display creation will be handled automatically by the Game class.
 *
 * @author Sri Harsha Chilakapati
 */
public final class Display
{
    // The display window
    private static Window displayWindow = null;

    // Width and height of the display
    private static int width  = 800;
    private static int height = 600;

    // Width and height of windowed display, used to
    // restore the properties to the newly created one
    private static int oldWidth  = 800;
    private static int oldHeight = 600;

    // Position of the windowed display, restored when
    // fullscreen is switched off
    private static int oldPosX = 0;
    private static int oldPosY = 0;

    // The position of the display in screen coordinates
    private static int posX = 0;
    private static int posY = 0;

    // The title of the Display
    private static String title = "SilenceEngine";

    // Private flags to maintain the Display
    private static boolean resized    = false;
    private static boolean fullScreen = false;
    private static boolean resizable  = true;
    private static boolean vSync      = true;
    private static boolean dirty      = false;

    /**
     * Private constructor. Prevent instantiation
     */
    private Display()
    {
    }

    /**
     * Creates the default display, in windowed mode, initially hidden.
     */
    public static void create()
    {
        // Set error callback
        GLFW3.setErrorCallback((error, description) ->
        {
            throw new SilenceException("" + error + ": " + description);
        });

        // Create the window
        displayWindow = createWindow(width, height, title, null, null, false, resizable);
        centerOnScreen();
    }

    /**
     * A private method to handle the creation of GLFW windows. Takes care of creating the window with windowing hints,
     * a size, a title, fullscreen or not, parent window to share the context, and whether initially visible or not.
     *
     * @param width     The width of the window
     * @param height    The height of the window
     * @param title     The title of the window
     * @param monitor   The monitor to create the window on
     * @param parent    The parent window, if the context needs to be shared
     * @param visible   Is the window visible upon creation?
     * @param resizable Is the window resizable?
     *
     * @return A window handle. (GLFWWindow* as in C++, but this is Java, so a long)
     */
    private static Window createWindow(int width, int height, String title, Monitor monitor, Window parent, boolean visible, boolean resizable)
    {
        // Dispose existing Batcher because VAOs cannot be shared across context
        if (Game.getBatcher() != null)
            Game.getBatcher().dispose();

        // Window Hints for OpenGL context
        Window.setHint(GLFW_SAMPLES, 4);

        if (System.getProperty("os.name").toLowerCase().contains("mac"))
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

        Window.setHint(GLFW_VISIBLE, visible);
        Window.setHint(GLFW_RESIZABLE, resizable);

        // Size fix
        width = MathUtils.clamp(width, 2, Integer.MAX_VALUE);
        height = MathUtils.clamp(height, 2, Integer.MAX_VALUE);

        if (Game.development)
        {
            System.setProperty("org.lwjgl.util.Debug", "true");
            Window.setHint(GLFW_OPENGL_DEBUG_CONTEXT, true);
        }

        // Create the window
        Window window = new Window(width, height, title, monitor, parent);

        // Take care of OpenGL context
        window.makeCurrent();
        glfwSwapInterval(vSync ? 1 : 0);

        Game.setBatcher(new Batcher());

        window.setSizeCallback((win, w, h) ->
        {
            Display.width = w;
            Display.height = h;

            resized = true;
        });

        window.setKeyCallback(Keyboard::glfwKeyCallback);

        window.setPositionCallback((win, xPos, yPos) ->
        {
            Display.posX = xPos;
            Display.posY = yPos;
        });

        window.setCursorPositionCallback(Mouse::glfwCursorCallback);
        window.setScrollCallback(Mouse::glfwScrollCallback);
        window.setMouseButtonCallback(Mouse::glfwMouseButtonCallback);

        Display.dirty = true;

        // Reset the window hints for next window creation
        Window.setDefaultHints();

        return window;
    }

    public void makeCurrent()
    {
        displayWindow.makeCurrent();
    }

    /**
     * Centers the display on screen.
     */
    public static void centerOnScreen()
    {
        if (fullScreen || displayWindow == null)
            return;

        VideoMode videoMode = Monitor.getPrimaryMonitor().getVideoMode();
        displayWindow.setPosition((videoMode.getWidth() - width) / 2, (videoMode.getHeight() - height) / 2);
    }

    /**
     * @return true if the user has clicked the close button, or pressed platform specific close shortcut keys like
     * Cmd-Q(Mac) or Alt-F4 (linux and windows)
     */
    public static boolean isCloseRequested()
    {
        return displayWindow.shouldClose();
    }

    /**
     * Hides the display from being shown to the user.
     */
    public static void hide()
    {
        displayWindow.hide();
    }

    /**
     * Destroy the display, will be unusable until create is called. Please don't call this method in between frames,
     * unless you are keenly awaiting for a number of runtime exceptions to be thrown.
     */
    public static void destroy()
    {
        displayWindow.destroy();
    }

    /**
     * @return the window of the current display. Please don't store this window, and always use this method to get one,
     * because a stored window can get invalid at any time.
     */
    public static Window getWindow()
    {
        return displayWindow;
    }

    /**
     * @return the height of the display
     */
    public static int getHeight()
    {
        return height;
    }

    /**
     * Sets the height of the display, measured in screen coordinates.
     *
     * @param height The height of the window
     */
    public static void setHeight(int height)
    {
        Display.height = height;
        displayWindow.setSize(width, height);
    }

    /**
     * @return The width of the display, in screen coordinates.
     */
    public static int getWidth()
    {
        return width;
    }

    /**
     * Sets the width of the display, in screen coordinates.
     *
     * @param width The width of the window.
     */
    public static void setWidth(int width)
    {
        Display.width = width;
        displayWindow.setSize(width, height);
    }

    /**
     * Sets the size of the Display window.
     *
     * @param width  The width of the Display window
     * @param height The height of the Display window
     */
    public static void setSize(int width, int height)
    {
        Display.width = width;
        Display.height = height;

        displayWindow.setSize(width, height);
    }

    /**
     * @return The size of the current window as a Vector2
     */
    public static Vector2 getSize()
    {
        return new Vector2(width, height);
    }

    /**
     * @return The aspect ratio of the Display
     */
    public static float getAspectRatio()
    {
        return ((float) width) / ((float) height);
    }

    /**
     * @return whether the Display is fullscreen or not
     */
    public static boolean isFullScreen()
    {
        return fullScreen;
    }

    /**
     * Sets the state of fullscreen of the Display.
     *
     * @param fullScreen If true, window will be made fullscreen
     */
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
        }
        else
        {
            // Restore window size
            width = oldWidth;
            height = oldHeight;

            // Restore window position
            posX = oldPosX;
            posY = oldPosY;
        }

        // Create new window
        Window fsDisplayWindow = createWindow(width, height, title, fullScreen ? Monitor.getPrimaryMonitor() : null, displayWindow, false, resizable);
        displayWindow.destroy();
        displayWindow = fsDisplayWindow;

        setPosition(posX, posY);
        setSize(width, height);

        hide();
        show();

        // Make an update
        update();
    }

    /**
     * Makes the display visible to the users.
     */
    public static void show()
    {
        displayWindow.show();
    }

    /**
     * Updates the display. Swaps the buffers and polls new events.
     */
    public static void update()
    {
        displayWindow.swapBuffers();
        GLFW3.pollEvents();

        // Force binding
        Program.CURRENT = null;
        Texture.CURRENT = null;

        Program.DEFAULT.use();
        Texture.EMPTY.bind();
    }

    /**
     * Sets the window position on the screen
     *
     * @param x The x-coordinate of the window (in screen coordinates)
     * @param y The y-coordinate of the window (in screen coordinates)
     */
    public static void setPosition(int x, int y)
    {
        if (fullScreen || displayWindow == null)
            return;

        posX = x;
        posY = y;

        displayWindow.setPosition(x, y);
    }

    /**
     * @return Whether the window is resizable or not.
     */
    public static boolean isResizable()
    {
        return resizable;
    }

    /**
     * Changes the Resizable property of the Display window.
     *
     * @param resizable The value of the Resizable window property.
     */
    public static void setResizable(boolean resizable)
    {
        if (Display.resizable == resizable)
            return;

        Display.resizable = resizable;

        Window newDisplay = createWindow(width, height, title, fullScreen ? Monitor.getPrimaryMonitor() : null, displayWindow, fullScreen, resizable);
        displayWindow.destroy();
        displayWindow = newDisplay;

        setPosition(posX, posY);
        show();

        update();
    }

    public static boolean getVSync()
    {
        return Display.vSync;
    }

    public static void setVSync(boolean vSync)
    {
        Display.vSync = vSync;
        glfwSwapInterval(vSync ? 1 : 0);
        update();
    }

    /**
     * @return The title of the Display
     */
    public static String getTitle()
    {
        return title;
    }

    /**
     * Sets the title of the Display
     *
     * @param title The title of the window
     */
    public static void setTitle(String title)
    {
        Display.title = title;

        if (displayWindow != null)
            displayWindow.setTitle(title);
    }

    /**
     * @return True if the display has been resized
     */
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
        return dirty && (Display.dirty = false);
    }

    /**
     * Hides the cursor over the Display
     */
    public static void hideCursor()
    {
        displayWindow.setInputMode(GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
    }

    /**
     * Shows the cursor over the Display
     */
    public static void showCursor()
    {
        displayWindow.setInputMode(GLFW_CURSOR, GLFW_CURSOR_NORMAL);
    }

    public static void grabCursor()
    {
        displayWindow.setInputMode(GLFW_CURSOR, GLFW_CURSOR_DISABLED);
    }
}
