package com.shc.silenceengine.backend.lwjgl;

import com.shc.silenceengine.backend.lwjgl.glfw.Monitor;
import com.shc.silenceengine.backend.lwjgl.glfw.VideoMode;
import com.shc.silenceengine.backend.lwjgl.glfw.Window;
import com.shc.silenceengine.core.IDisplayDevice;
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
    private Window   window;
    private Platform platform;

    public LwjglDisplayDevice()
    {
        Window.setHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        Window.setHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
        Window.setHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        Window.setHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);

        window = new Window(800, 600, "SilenceEngine Window");
        window.show();
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
            if (isLinux) platform = is64Bit ? Platform.LINUX_64 : Platform.LINUX_32;
            if (isMac) platform = Platform.MACOSX;
        }

        return platform;
    }

    @Override
    public void setSize(int width, int height)
    {
        window.setSize(width, height);
    }

    @Override
    public void setFullscreen(boolean fullscreen)
    {
        if (fullscreen)
            window.setMonitor(Monitor.getPrimaryMonitor());
        else
            window.setMonitor(null);
    }

    @Override
    public void centerOnScreen()
    {
        VideoMode mode = Monitor.getPrimaryMonitor().getVideoMode();

        Vector2 windowPosition = window.getPosition();

        windowPosition.x = (mode.getWidth() - windowPosition.x) / 2;
        windowPosition.y = (mode.getHeight() - windowPosition.y) / 2;

        window.setPosition(windowPosition);
    }

    @Override
    public void setPosition(int x, int y)
    {
        window.setPosition(x, y);
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
        // TODO: Implement this feature
    }
}
