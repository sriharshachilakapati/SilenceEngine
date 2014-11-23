package com.shc.silenceengine;

import com.shc.silenceengine.graphics.Shader;
import org.lwjgl.opencl.CLContextCallback;
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
    private static long displayHandle   = NULL;

    private static int    width         = 640;
    private static int    height        = 480;
    private static int    oldWidth      = 640;
    private static int    oldHeight     = 480;
    private static String title         = "SilenceEngine";

    private static boolean resized = false;

    private static boolean fullScreen = false;

    private static long createWindow(int width, int height, String title, long monitor, long parent, boolean visible)
    {
        // Window Hints for OpenGL context
        glfwWindowHint(GLFW_SAMPLES, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        if (!visible)
            glfwWindowHint(GLFW_VISIBLE, GL_FALSE);

        long window = glfwCreateWindow(width, height, title, monitor, parent);

        if (window == NULL)
            throw new SilenceException("Error creating Display. Your system is unsupported.");

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);

        // Make the context current
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
        });

        return window;
    }

    public static void create()
    {
        glfwSetErrorCallback(ErrorCallback.Util.getDefault());

        // Initialize GLFW
        if (glfwInit() != GL_TRUE)
            throw new SilenceException("Error creating Display. Your system is unsupported.");

        // Create the window
        displayHandle = createWindow(width, height, title, NULL, NULL, false);
        centerOnScreen();

        // OpenGL version
        System.out.println(glGetString(GL_VERSION));
    }

    public static void show()
    {
        glfwShowWindow(displayHandle);
    }

    public static boolean isCloseRequested()
    {
        return glfwWindowShouldClose(displayHandle) == GL_TRUE;
    }

    public static void hide()
    {
        glfwHideWindow(displayHandle);
    }

    public static void destroy()
    {
        glfwDestroyWindow(displayHandle);
        glfwTerminate();
    }

    public static void update()
    {
        glfwSwapBuffers(displayHandle);
        glfwPollEvents();
    }

    public static long getDisplayHandle() { return displayHandle; }

    public static int getHeight()
    {
        return height;
    }

    public static void setHeight(int height)
    {
        Display.height = height;
        glfwSetWindowSize(displayHandle, width, height);
    }

    public static int getWidth()
    {
        return width;
    }

    public static void setWidth(int width)
    {
        Display.width = width;
        glfwSetWindowSize(displayHandle, width, height);
    }

    public static void centerOnScreen()
    {
        if (fullScreen)
            return;

        ByteBuffer vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(displayHandle, (GLFWvidmode.width(vidMode) - width)/2,
                                        (GLFWvidmode.height(vidMode) - height)/2);
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

            oldWidth = width;
            oldHeight = height;

            width = GLFWvidmode.width(vidMode);
            height = GLFWvidmode.height(vidMode);
        }
        else
        {
            width = oldWidth;
            height = oldHeight;
        }

        // Create new window
        long fsDisplayHandle = createWindow(width, height, title, fullScreen ? glfwGetPrimaryMonitor() : NULL, displayHandle, true);
        glfwDestroyWindow(displayHandle);
        displayHandle = fsDisplayHandle;

        Shader.DEFAULT.use();
        show();
        centerOnScreen();
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
}
