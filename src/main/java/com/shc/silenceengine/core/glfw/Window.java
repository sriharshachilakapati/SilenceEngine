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

package com.shc.silenceengine.core.glfw;

import com.shc.silenceengine.math.Vector2;
import org.lwjgl.opengl.GLContext;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class Window
{
    private long handle;

    private Vector2 position;
    private Vector2 size;

    public Window()
    {
        this(800, 600);
    }

    public Window(Monitor monitor)
    {
        this(800, 600, monitor);
    }

    public Window(Window share)
    {
        this(800, 600, share);
    }

    public Window(String title)
    {
        this(800, 600, title);
    }

    public Window(int width, int height)
    {
        this(width, height, (Monitor) null, null);
    }

    public Window(int width, int height, String title)
    {
        this(width, height, title, (Monitor) null);
    }

    public Window(int width, int height, Monitor monitor)
    {
        this(width, height, "SilenceEngine Window", monitor);
    }

    public Window(int width, int height, Window share)
    {
        this(width, height, "SilenceEngine window", share);
    }

    public Window(int width, int height, Monitor monitor, Window share)
    {
        this(width, height, "SilenceEngine Window", monitor, share);
    }

    public Window(int width, int height, String title, Monitor monitor)
    {
        this(width, height, title, monitor, null);
    }

    public Window(int width, int height, String title, Window share)
    {
        this(width, height, title, null, share);
    }

    public Window(VideoMode videoMode, String title, Monitor monitor, Window share)
    {
        this(videoMode.getWidth(), videoMode.getHeight(), title, monitor, share);
    }

    public Window(VideoMode videoMode, String title, Monitor monitor)
    {
        this(videoMode, title, monitor, null);
    }

    public Window(VideoMode videoMode, Monitor monitor)
    {
        this(videoMode, "SilenceEngine Window", monitor);
    }

    public Window(int width, int height, String title, Monitor monitor, Window share)
    {
        size = new Vector2(width, height);
        position = new Vector2();

        handle = glfwCreateWindow(width, height, title, monitor == null ? NULL : monitor.getHandle(), share == null ? NULL : share.handle);
    }

    public void makeCurrent()
    {
        glfwMakeContextCurrent(handle);
        GLContext.createFromCurrent();
    }

    public void swapBuffers()
    {
        glfwSwapBuffers(handle);
    }

    public boolean shouldClose()
    {
        return glfwWindowShouldClose(handle) == 1;
    }

    public void setShouldClose(boolean value)
    {
        glfwSetWindowShouldClose(handle, value ? 1 : 0);
    }

    public void destroy()
    {
        glfwDestroyWindow(handle);
    }

    public Vector2 getPosition()
    {

        return position;
    }

    public void setPosition(Vector2 position)
    {
        this.position.set(position);
    }

    public Vector2 getSize()
    {
        return size;
    }

    public void setSize(Vector2 size)
    {
        this.size.set(size);
    }

    public long getHandle()
    {
        return handle;
    }

    public static void setHint(int hint, int value)
    {
        glfwWindowHint(hint, value);
    }
}
