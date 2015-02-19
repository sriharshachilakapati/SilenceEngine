package com.shc.silenceengine.core;

import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.opengl.GL3Context;
import com.shc.silenceengine.graphics.opengl.Program;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.input.Mouse;
import com.shc.silenceengine.math.Vector2;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GLContext;

import java.nio.ByteBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * The Display class takes care of creating a display and managing it.
 * As a user of SilenceEngine, I don't expect you to create the Display,
 * the display creation will be handled automatically by the Game class.
 *
 * @author Sri Harsha Chilakapati
 */
public final class Display
{
    // The display handle
    private static long displayHandle = NULL;

    // Width and height of the display
    private static int width = 640;
    private static int height = 480;

    // Width and height of windowed display, used to
    // restore the properties to the newly created one
    private static int oldWidth = 640;
    private static int oldHeight = 480;

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
    private static boolean resized = false;
    private static boolean fullScreen = false;
    private static boolean resizable = true;
    private static boolean vSync = true;

    // Clear color
    private static Color clearColor = Color.BLACK;

    // Callbacks from GLFW
    private static GLFWWindowSizeCallback winSizeCallback;
    private static GLFWKeyCallback winKeyCallback;
    private static GLFWWindowPosCallback winPosCallback;
    private static GLFWCursorPosCallback winCurPosCallback;
    private static GLFWMouseButtonCallback winMouseButtonCallback;
    private static GLFWScrollCallback winScrollCallback;
    private static GLFWErrorCallback errorCallback;

    /**
     * Private constructor. Prevent instantiation
     */
    private Display()
    {
    }

    /**
     * A private method to handle the creation of GLFW windows. Takes care of creating
     * the window with windowing hints, a size, a title, fullscreen or not, parent window
     * to share the context, and whether initially visible or not.
     *
     * @param width     The width of the window
     * @param height    The height of the window
     * @param title     The title of the window
     * @param monitor   The monitor to create the window on
     * @param parent    The parent window, if the context needs to be shared
     * @param visible   Is the window visible upon creation?
     * @param resizable Is the window resizable?
     * @return A window handle. (GLFWWindow* as in C++, but this is Java, so a long)
     */
    private static long createWindow(int width, int height, String title, long monitor, long parent, boolean visible, boolean resizable)
    {
        // Dispose existing Batcher because VAOs cannot be shared across context
        if (Game.getBatcher() != null)
            Game.getBatcher().dispose();

        // Window Hints for OpenGL context
        glfwWindowHint(GLFW_SAMPLES, 4);

        if (System.getProperty("os.name").toLowerCase().contains("mac"))
        {
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        }
        else
        {
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        }
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_VISIBLE, visible ? GL_TRUE : GL_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, resizable ? GL_TRUE : GL_FALSE);

        if (Game.development)
        {
            System.setProperty("org.lwjgl.util.Debug", "true");
            glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GL_TRUE);
        }

        // Create the window
        long window = glfwCreateWindow(width, height, title, monitor, parent);

        if (window == NULL)
            throw new SilenceException("Error creating Display. Your system is unsupported.");

        // Take care of OpenGL context
        glfwMakeContextCurrent(window);
        glfwSwapInterval(vSync ? 1 : 0);

        GLContext.createFromCurrent();

        Game.setBatcher(new Batcher());

        // Window callbacks
        releaseCallbacks();

        glfwSetWindowSizeCallback(window, winSizeCallback = GLFWWindowSizeCallback((win, w, h) ->
        {
            Display.width = w;
            Display.height = h;

            resized = true;
        }));

        glfwSetKeyCallback(window, winKeyCallback = GLFWKeyCallback(Keyboard::glfwKeyCallback));

        glfwSetWindowPosCallback(window, winPosCallback = GLFWWindowPosCallback((win, xPos, yPos) ->
        {
            Display.posX = xPos;
            Display.posY = yPos;
        }));

        glfwSetCursorPosCallback(window, winCurPosCallback = GLFWCursorPosCallback(Mouse::glfwCursorCallback));
        glfwSetScrollCallback(window, winScrollCallback = GLFWScrollCallback(Mouse::glfwScrollCallback));
        glfwSetMouseButtonCallback(window, winMouseButtonCallback = GLFWMouseButtonCallback(Mouse::glfwMouseButtonCallback));

        return window;
    }

    /**
     * Used to release GLFW callbacks. This is necessary to prevent
     * segmentation fault errors in native code.
     */
    private static void releaseCallbacks()
    {
        if (winSizeCallback != null)
            winSizeCallback.release();

        if (winKeyCallback != null)
            winKeyCallback.release();

        if (winPosCallback != null)
            winPosCallback.release();

        if (winCurPosCallback != null)
            winCurPosCallback.release();

        if (winScrollCallback != null)
            winScrollCallback.release();

        if (winMouseButtonCallback != null)
            winMouseButtonCallback.release();
    }

    /**
     * Creates the default display, in windowed mode, initially hidden.
     */
    public static void create()
    {
        // Set error callback
        glfwSetErrorCallback(errorCallback = GLFWErrorCallback((error, description) ->
        {
            throw new SilenceException("" + error + ": " + Callbacks.errorCallbackDescriptionString(description));
        }));

        // Initialize GLFW
        if (glfwInit() != GL_TRUE)
            throw new SilenceException("Error creating Display. Your system is unsupported.");

        // Create the window
        displayHandle = createWindow(width, height, title, NULL, NULL, false, resizable);
        centerOnScreen();
    }

    /**
     * Makes the display visible to the users.
     */
    public static void show()
    {
        glfwShowWindow(displayHandle);
    }

    /**
     * @return true if the user has clicked the close button, or pressed
     * platform specific close shortcut keys like Cmd-Q(Mac) or
     * Alt-F4 (linux &amp; windows)
     */
    public static boolean isCloseRequested()
    {
        return glfwWindowShouldClose(displayHandle) == GL_TRUE;
    }

    /**
     * Sets the viewport of the display.
     *
     * @param x      The x-coordinate of the top-left point
     * @param y      The y-coordinate of the top-left point
     * @param width  The width of the window
     * @param height The height of the window
     */
    public static void setViewport(int x, int y, int width, int height)
    {
        GL3Context.viewport(x, y, width, height);
    }

    /**
     * Hides the display from being shown to the user.
     */
    public static void hide()
    {
        glfwHideWindow(displayHandle);
    }

    /**
     * Destroy the display, will be unusable until create is called.
     * Please don't call this method in between frames, unless you
     * are keenly awaiting for a number of runtime exceptions to be
     * thrown.
     */
    public static void destroy()
    {
        releaseCallbacks();
        errorCallback.release();

        glfwDestroyWindow(displayHandle);

        glfwTerminate();
    }

    /**
     * Updates the display. Swaps the buffers and polls new events.
     */
    public static void update()
    {
        glfwSwapBuffers(displayHandle);
        glfwPollEvents();

        GL3Context.clearColor(clearColor);

        // Force binding
        Program.CURRENT = null;
        Texture.CURRENT = null;

        Program.DEFAULT.use();
        Texture.EMPTY.bind();
    }

    /**
     * @return the handle of the current display. Please don't store
     * this handle, and always use this method to get one,
     * because a stored handle can get invalid at any time.
     */
    public static long getDisplayHandle()
    {
        return displayHandle;
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
        glfwSetWindowSize(displayHandle, width, height);
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
        glfwSetWindowSize(displayHandle, width, height);
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

        glfwSetWindowSize(displayHandle, width, height);
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
     * Centers the display on screen.
     */
    public static void centerOnScreen()
    {
        if (fullScreen || displayHandle == NULL)
            return;

        ByteBuffer vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(displayHandle, (GLFWvidmode.width(vidMode) - width) / 2,
                (GLFWvidmode.height(vidMode) - height) / 2);
    }

    /**
     * Sets the window position on the screen
     *
     * @param x The x-coordinate of the window (in screen coordinates)
     * @param y The y-coordinate of the window (in screen coordinates)
     */
    public static void setPosition(int x, int y)
    {
        if (fullScreen || displayHandle == NULL)
            return;

        posX = x;
        posY = y;

        glfwSetWindowPos(displayHandle, x, y);
    }

    /**
     * Sets the clear color of the Display, i.e., the background color
     *
     * @param c The background color to clear the window
     */
    public static void setClearColor(Color c)
    {
        clearColor = c;
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
            ByteBuffer vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Save window size
            oldWidth = width;
            oldHeight = height;

            // Save window position
            oldPosX = posX;
            oldPosY = posY;

            width = GLFWvidmode.width(vidMode);
            height = GLFWvidmode.height(vidMode);
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
        long fsDisplayHandle = createWindow(width, height, title, fullScreen ? glfwGetPrimaryMonitor() : NULL, displayHandle, true, resizable);
        glfwDestroyWindow(displayHandle);
        displayHandle = fsDisplayHandle;

        setPosition(posX, posY);
        show();

        // Make an update
        update();
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

        long newDisplay = createWindow(width, height, title, fullScreen ? glfwGetPrimaryMonitor() : NULL, displayHandle, fullScreen, resizable);
        glfwDestroyWindow(displayHandle);
        displayHandle = newDisplay;

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

        if (displayHandle != NULL)
            glfwSetWindowTitle(displayHandle, title);
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

    /**
     * Hides the cursor over the Display
     */
    public static void hideCursor()
    {
        glfwSetInputMode(displayHandle, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
    }

    /**
     * Shows the cursor over the Display
     */
    public static void showCursor()
    {
        glfwSetInputMode(displayHandle, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
    }
}
