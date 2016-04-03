package com.shc.silenceengine.backend.lwjgl;

import com.shc.silenceengine.backend.lwjgl.glfw.GLFW3;
import com.shc.silenceengine.backend.lwjgl.glfw.Monitor;
import com.shc.silenceengine.backend.lwjgl.glfw.VideoMode;
import com.shc.silenceengine.backend.lwjgl.glfw.Window;
import com.shc.silenceengine.core.IDisplayDevice;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.math.Vector2;
import org.lwjgl.glfw.GLFW;

import static com.shc.silenceengine.core.SilenceEngine.*;

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

    public LwjglDisplayDevice()
    {
        GLFW3.init();
        GLFW3.setSwapInterval(1);

        Window.setHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        Window.setHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
        Window.setHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        Window.setHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);

        windowWidth = 800;
        windowHeight = 600;

        window = new Window(windowWidth, windowHeight, "SilenceEngine Window");

        centerOnScreen();
        window.show();
        window.makeCurrent();

        window.setPositionCallback((window1, xPos, yPos) -> {
            if (!fullscreen)
            {
                windowPositionX = xPos;
                windowPositionY = yPos;
            }
        });

        window.setSizeCallback((window1, width, height) -> {
            if (!fullscreen)
            {
                windowWidth = width;
                windowHeight = height;
            }

            SilenceEngine.eventManager.raiseResizeEvent();
        });

        SilenceEngine.eventManager.raiseResizeEvent();

        SilenceEngine.eventManager.addDisposeHandler(this::cleanUp);
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

    private void cleanUp()
    {
        window.destroy();
        GLFW3.terminate();
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
    public boolean isFullscreen()
    {
        return fullscreen;
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
        return (int) window.getPosition().x;
    }

    @Override
    public int getHeight()
    {
        return (int) window.getPosition().y;
    }

    @Override
    public void setTitle(String title)
    {
        window.setTitle(title);
    }

    @Override
    public String getTitle()
    {
        return window.getTitle();
    }

    @Override
    public void setIcon(FilePath filePath)
    {
        window.setIcon(filePath);
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
}
