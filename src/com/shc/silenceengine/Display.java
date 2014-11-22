package com.shc.silenceengine;

import com.shc.silenceengine.graphics.Shader;
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

    private static int  width           = 640;
    private static int  height          = 480;
    private static int  oldWidth        = 640;
    private static int  oldHeight       = 480;

    private static boolean resized = false;

    private static boolean fullScreen = false;

    public static void create()
    {
        glfwSetErrorCallback(ErrorCallback.Util.getDefault());

        // Initialize GLFW
        if (glfwInit() != GL_TRUE)
            throw new SilenceException("Error creating Display. Your system is unsupported.");

        // Window Hints for OpenGL context
        glfwWindowHint(GLFW_SAMPLES, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE);

        // Create the window
        displayHandle = glfwCreateWindow(width, height, "SilenceEngine", NULL, NULL);

        if (displayHandle == NULL)
            throw new SilenceException("Error creating Display. Your system is unsupported.");

        centerOnScreen();

        glfwMakeContextCurrent(displayHandle);
        glfwSwapInterval(1);

        // Make the context current
        GLContext.createFromCurrent();

        // OpenGL version
        System.out.println(glGetString(GL_VERSION));

        // Window callbacks
        WindowCallback.set(displayHandle, new WindowCallbackAdapter()
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
        long fsDisplayHandle = glfwCreateWindow(width, height, "SilenceEngine", fullScreen ? glfwGetPrimaryMonitor() : NULL, displayHandle);
        glfwDestroyWindow(displayHandle);
        displayHandle = fsDisplayHandle;

        glfwMakeContextCurrent(displayHandle);
        glfwSwapInterval(1);

        GLContext.createFromCurrent();
        resized = true;

        WindowCallback.set(displayHandle, new WindowCallbackAdapter()
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

        Shader.DEFAULT.use();
        show();
        centerOnScreen();
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
