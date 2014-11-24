package com.shc.silenceengine;

import org.lwjgl.opengl.GLContext;
import org.lwjgl.system.glfw.ErrorCallback;
import org.lwjgl.system.glfw.GLFWvidmode;
import org.lwjgl.system.glfw.WindowCallback;
import org.lwjgl.system.glfw.WindowCallbackAdapter;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * The Display class takes care of creating a display and managing it.
 *
 * @author Sri Harsha Chilakapati
 */
public class Display
{
    // The display handle
    private static long displayHandle   = NULL;

    // Width and height of the display
    private static int    width         = 640;
    private static int    height        = 480;

    // Width and height of windowed display, used to
    // restore the properties to the newly created one
    private static int    oldWidth      = 640;
    private static int    oldHeight     = 480;

    // Position of the windowed display, restored when
    // fullscreen is switched off
    private static int    oldPosX       = 0;
    private static int    oldPosY       = 0;

    // The position of the display in screen coordinates
    private static int    posX          = 0;
    private static int    posY          = 0;

    // The title of the Display
    private static String title         = "SilenceEngine";

    // Private flags to maintain the Display
    private static boolean resized    = false;
    private static boolean fullScreen = false;

    /**
     * A private method to handle the creation of GLFW windows. Takes care of creating
     * the window with windowing hints, a size, a title, fullscreen or not, parent window
     * to share the context, and whether initially visible or not.
     *
     * @param width    The width of the window
     * @param height   The height of the window
     * @param title    The title of the window
     * @param monitor  The monitor to create the window on
     * @param parent   The parent window, if the context needs to be shared
     * @param visible  Is the window visible upon creation?
     *
     * @return A window handle. (GLFWWindow* as in C++, but this is Java, so a long)
     */
    private static long createWindow(int width, int height, String title, long monitor, long parent, boolean visible)
    {
        // Window Hints for OpenGL context
        glfwWindowHint(GLFW_SAMPLES, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_VISIBLE, visible ? GL_TRUE : GL_FALSE);

        // Create the window
        long window = glfwCreateWindow(width, height, title, monitor, parent);

        if (window == NULL)
            throw new SilenceException("Error creating Display. Your system is unsupported.");

        // Take care of OpenGL context
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);

        GLContext.createFromCurrent();

        // Window callbacks
        WindowCallback.set(window, new WindowCallbackAdapter()
        {
            @Override
            public void windowSize(long window, int width, int height)
            {
                Display.width  = width;
                Display.height = height;

                resized = true;
            }

            @Override
            public void key(long window, int key, int scanCode, int action, int mods)
            {
                // TODO:
            }

            @Override
            public void windowPos(long window, int xPos, int yPos)
            {
                Display.posX = xPos;
                Display.posY = yPos;
            }
        });

        return window;
    }

    /**
     * Creates the default display, in windowed mode, initially hidden.
     */
    public static void create()
    {
        glfwSetErrorCallback(ErrorCallback.Util.getDefault());

        // Initialize GLFW
        if (glfwInit() != GL_TRUE)
            throw new SilenceException("Error creating Display. Your system is unsupported.");

        // Create the window
        displayHandle = createWindow(width, height, title, NULL, NULL, false);
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
     *         platform specific close shortcut keys like Cmd-Q(Mac) or
     *         Alt-F4 (linux & windows)
     */
    public static boolean isCloseRequested()
    {
        return glfwWindowShouldClose(displayHandle) == GL_TRUE;
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
    }

    /**
     * @return the handle of the current display. Please don't store
     *         this handle, and always use this method to get one,
     *         because a stored handle can get invalid at any time.
     */
    public static long getDisplayHandle() { return displayHandle; }

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
     * Centers the display on screen.
     */
    public static void centerOnScreen()
    {
        if (fullScreen || displayHandle == NULL)
            return;

        ByteBuffer vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(displayHandle, (GLFWvidmode.width(vidMode) - width)/2,
                                        (GLFWvidmode.height(vidMode) - height)/2);
    }

    public static void setPosition(int x, int y)
    {
        if (fullScreen || displayHandle == NULL)
            return;

        posX = x;
        posY = y;

        glfwSetWindowPos(displayHandle, x, y);
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
        long fsDisplayHandle = createWindow(width, height, title, fullScreen ? glfwGetPrimaryMonitor() : NULL, displayHandle, true);
        glfwDestroyWindow(displayHandle);
        displayHandle = fsDisplayHandle;

        setPosition(posX, posY);
        show();
    }

    public static String getTitle()
    {
        return title;
    }

    public static void setTitle(String title)
    {
        Display.title = title;

        if (displayHandle != NULL)
            glfwSetWindowTitle(displayHandle, title);
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

    public static void hideCursor()
    {
        glfwSetInputMode(displayHandle, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
    }

    public static void showCursor()
    {
        glfwSetInputMode(displayHandle, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
    }
}
