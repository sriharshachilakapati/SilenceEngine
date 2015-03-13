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

import com.shc.silenceengine.core.glfw.callbacks.*;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.Vector4;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GLContext;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class Window
{
    private static Map<Long, Window> registeredWindows = new HashMap<>();

    private long handle;

    private Vector2 position;
    private Vector2 size;

    private String title;
    private Monitor monitor;

    /* Native GLFW Callbacks */
    private GLFWCharCallback            glfwCharCallback;
    private GLFWCharModsCallback        glfwCharModsCallback;
    private GLFWCursorEnterCallback     glfwCursorEnterCallback;
    private GLFWCursorPosCallback       glfwCursorPosCallback;
    private GLFWDropCallback            glfwDropCallback;
    private GLFWFramebufferSizeCallback glfwFramebufferSizeCallback;
    private GLFWKeyCallback             glfwKeyCallback;
    private GLFWMouseButtonCallback     glfwMouseButtonCallback;
    private GLFWScrollCallback          glfwScrollCallback;
    private GLFWWindowCloseCallback     glfwWindowCloseCallback;
    private GLFWWindowFocusCallback     glfwWindowFocusCallback;
    private GLFWWindowIconifyCallback   glfwWindowIconifyCallback;
    private GLFWWindowPosCallback       glfwWindowPosCallback;
    private GLFWWindowRefreshCallback   glfwWindowRefreshCallback;
    private GLFWWindowSizeCallback      glfwWindowSizeCallback;

    /* Overridden custom Callbacks for better interfacing with C++ code */
    private ICharacterCallback       characterCallback;
    private ICharacterModsCallback   characterModsCallback;
    private ICursorEnterCallback     cursorEnterCallback;
    private ICursorPositionCallback  cursorPositionCallback;
    private IDropCallback            dropCallback;
    private IFramebufferSizeCallback framebufferSizeCallback;
    private IKeyCallback             keyCallback;
    private IMouseButtonCallback     mouseButtonCallback;
    private IScrollCallback          scrollCallback;
    private IWindowCloseCallback     windowCloseCallback;
    private IWindowFocusCallback     windowFocusCallback;
    private IWindowIconifyCallback   windowIconifyCallback;
    private IWindowPositionCallback  windowPositionCallback;
    private IWindowRefreshCallback   windowRefreshCallback;
    private IWindowSizeCallback      windowSizeCallback;

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
        this.title = title;
        this.monitor = monitor;

        handle = glfwCreateWindow(width, height, title, monitor == null ? NULL : monitor.getHandle(), share == null ? NULL : share.getHandle());

        if (handle == NULL)
            throw new RuntimeException("Cannot create window");

        registeredWindows.put(handle, this);

        // Initialize the native callbacks
        initNativeCallbacks();
    }

    private void initNativeCallbacks()
    {
        // Initialize overriden callbacks
        initCustomCallbacks();

        // Create the callback functions that re-post the events
        glfwCharCallback = GLFWCharCallback((window, codePoint) ->
            characterCallback.invoke(registeredWindows.get(window), codePoint)
        );

        glfwCharModsCallback = GLFWCharModsCallback((window, codePoint, mods) ->
            characterModsCallback.invoke(registeredWindows.get(window), codePoint, mods)
        );

        glfwCursorEnterCallback = GLFWCursorEnterCallback((window, entered) ->
            cursorEnterCallback.invoke(registeredWindows.get(window), entered != 0)
        );

        glfwCursorPosCallback = GLFWCursorPosCallback((window, xPos, yPos) ->
            cursorPositionCallback.invoke(registeredWindows.get(window), xPos, yPos)
        );

        glfwDropCallback = GLFWDropCallback((window, count, names) ->
            dropCallback.invoke(registeredWindows.get(window), Callbacks.dropCallbackNamesString(count, names))
        );

        glfwFramebufferSizeCallback = GLFWFramebufferSizeCallback((window, width, height) ->
            framebufferSizeCallback.invoke(registeredWindows.get(window), width, height)
        );

        glfwKeyCallback = GLFWKeyCallback((window, key, scanCode, action, mods) ->
            keyCallback.invoke(registeredWindows.get(window), key, scanCode, action, mods)
        );

        glfwMouseButtonCallback = GLFWMouseButtonCallback((window, button, action, mods) ->
            mouseButtonCallback.invoke(registeredWindows.get(window), button, action, mods)
        );

        glfwScrollCallback = GLFWScrollCallback((window, xOffset, yOffset) ->
            scrollCallback.invoke(registeredWindows.get(window), xOffset, yOffset)
        );

        glfwWindowCloseCallback = GLFWWindowCloseCallback((window) ->
            windowCloseCallback.invoke(registeredWindows.get(window))
        );

        glfwWindowFocusCallback = GLFWWindowFocusCallback((window, focus) ->
            windowFocusCallback.invoke(registeredWindows.get(window), focus != 0)
        );

        glfwWindowIconifyCallback = GLFWWindowIconifyCallback((window, iconify) ->
            windowIconifyCallback.invoke(registeredWindows.get(window), iconify != 0)
        );

        glfwWindowPosCallback = GLFWWindowPosCallback((window, xPos, yPos) ->
            windowPositionCallback.invoke(registeredWindows.get(window), xPos, yPos)
        );

        glfwWindowRefreshCallback = GLFWWindowRefreshCallback((window) ->
            windowRefreshCallback.invoke(registeredWindows.get(window))
        );

        glfwWindowSizeCallback = GLFWWindowSizeCallback((window, width, height) ->
            windowSizeCallback.invoke(registeredWindows.get(window), width, height)
        );

        // Register native callbacks
        glfwSetCharCallback(handle, glfwCharCallback);
        glfwSetCharModsCallback(handle, glfwCharModsCallback);
        glfwSetCursorEnterCallback(handle, glfwCursorEnterCallback);
        glfwSetCursorPosCallback(handle, glfwCursorPosCallback);
        glfwSetDropCallback(handle, glfwDropCallback);
        glfwSetFramebufferSizeCallback(handle, glfwFramebufferSizeCallback);
        glfwSetKeyCallback(handle, glfwKeyCallback);
        glfwSetMouseButtonCallback(handle, glfwMouseButtonCallback);
        glfwSetScrollCallback(handle, glfwScrollCallback);
        glfwSetWindowCloseCallback(handle, glfwWindowCloseCallback);
        glfwSetWindowFocusCallback(handle, glfwWindowFocusCallback);
        glfwSetWindowIconifyCallback(handle, glfwWindowIconifyCallback);
        glfwSetWindowPosCallback(handle, glfwWindowPosCallback);
        glfwSetWindowRefreshCallback(handle, glfwWindowRefreshCallback);
        glfwSetWindowSizeCallback(handle, glfwWindowSizeCallback);
    }

    private void initCustomCallbacks()
    {
        // The default callbacks does nothing
        setCharacterCallback(null);
        setCharacterModsCallback(null);
        setCursorEnterCallback(null);
        setCursorPositionCallback(null);
        setDropCallback(null);
        setFramebufferSizeCallback(null);
        setKeyCallback(null);
        setMouseButtonCallback(null);
        setScrollCallback(null);
        setCloseCallback(null);
        setFocusCallback(null);
        setIconifyCallback(null);
        setPositionCallback(null);
        setRefreshCallback(null);
        setSizeCallback(null);
    }

    private void releaseNativeCallbacks()
    {
        glfwCharCallback.release();
        glfwCharModsCallback.release();
        glfwCursorEnterCallback.release();
        glfwCursorPosCallback.release();
        glfwDropCallback.release();
        glfwFramebufferSizeCallback.release();
        glfwKeyCallback.release();
        glfwMouseButtonCallback.release();
        glfwScrollCallback.release();
        glfwWindowCloseCallback.release();
        glfwWindowFocusCallback.release();
        glfwWindowIconifyCallback.release();
        glfwWindowPosCallback.release();
        glfwWindowRefreshCallback.release();
        glfwWindowSizeCallback.release();
    }

    public int getKey(int key)
    {
        return glfwGetKey(handle, key);
    }

    public int getMouseButton(int button)
    {
        return glfwGetMouseButton(handle, button);
    }

    public Vector2 getCursorPos()
    {
        DoubleBuffer xPos = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer yPos = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(handle, xPos, yPos);

        return new Vector2((float) xPos.get(), (float) yPos.get());
    }

    public void setCursorPos(double xPos, double yPos)
    {
        glfwSetCursorPos(handle, xPos, yPos);
    }

    public void setCursorPos(Vector2 pos)
    {
        setCursorPos(pos.x, pos.y);
    }

    public void setInputMode(int mode, int value)
    {
        glfwSetInputMode(handle, mode, value);
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

    public void iconify()
    {
        glfwIconifyWindow(handle);
    }

    public void restore()
    {
        glfwRestoreWindow(handle);
    }

    public void hide()
    {
        glfwHideWindow(handle);
    }

    public void show()
    {
        glfwShowWindow(handle);
    }

    public void destroy()
    {
        releaseNativeCallbacks();
        glfwDestroyWindow(handle);
    }

    public Vector2 getPosition()
    {
        IntBuffer xPos = BufferUtils.createIntBuffer(1);
        IntBuffer yPos = BufferUtils.createIntBuffer(1);
        glfwGetWindowPos(handle, xPos, yPos);

        return position.set(xPos.get(), yPos.get());
    }

    public void setPosition(Vector2 position)
    {
        this.position.set(position);
        glfwSetWindowPos(handle, (int) position.x, (int) position.y);
    }

    public void setPosition(float x, float y)
    {
        setPosition(position.set(x, y));
    }

    public Vector2 getSize()
    {
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        glfwGetWindowSize(handle, width, height);

        return size.set(width.get(), height.get());
    }

    public void setSize(Vector2 size)
    {
        this.size.set(size);
        glfwSetWindowSize(handle, (int) size.x, (int) size.y);
    }

    public void setSize(float width, float height)
    {
        setSize(size.set(width, height));
    }

    public Vector4 getFrameSize()
    {
        IntBuffer left = BufferUtils.createIntBuffer(1);
        IntBuffer top = BufferUtils.createIntBuffer(1);
        IntBuffer right = BufferUtils.createIntBuffer(1);
        IntBuffer bottom = BufferUtils.createIntBuffer(1);

        glfwGetWindowFrameSize(handle, left, top, right, bottom);

        return new Vector4(left.get(), top.get(), right.get(), bottom.get());
    }

    public Monitor getMonitor()
    {
        return monitor;
    }

    public void setTitle(String title)
    {
        this.title = title;

        glfwSetWindowTitle(handle, title);
    }

    public String getTitle()
    {
        return title;
    }

    public int getAttribute(int attribute)
    {
        return glfwGetWindowAttrib(handle, attribute);
    }

    public void setCharacterCallback(ICharacterCallback callback)
    {
        if (callback == null)
            callback = (window, codePoint) -> {};

        this.characterCallback = callback;
    }

    public void setCharacterModsCallback(ICharacterModsCallback callback)
    {
        if (callback == null)
            callback = (window, cp, mods) -> {};

        this.characterModsCallback = callback;
    }

    public void setCursorEnterCallback(ICursorEnterCallback callback)
    {
        if (callback == null)
            callback = (window, entered) -> {};

        this.cursorEnterCallback = callback;
    }

    public void setCursorPositionCallback(ICursorPositionCallback callback)
    {
        if (callback == null)
            callback = (window, x, y) -> {};

        this.cursorPositionCallback = callback;
    }

    public void setDropCallback(IDropCallback callback)
    {
        if (callback == null)
            callback = (window, paths) -> {};

        this.dropCallback = callback;
    }

    public void setFramebufferSizeCallback(IFramebufferSizeCallback callback)
    {
        if (callback == null)
            callback = (window, w, h) -> {};

        this.framebufferSizeCallback = callback;
    }

    public void setKeyCallback(IKeyCallback callback)
    {
        if (callback == null)
            callback = (win, key, sc, act, mods) -> {};

        this.keyCallback = callback;
    }

    public void setMouseButtonCallback(IMouseButtonCallback callback)
    {
        if (callback == null)
            callback = (win, b, act, mods) -> {};

        this.mouseButtonCallback = callback;
    }

    public void setScrollCallback(IScrollCallback callback)
    {
        if (callback == null)
            callback = (win, x, y) -> {};

        this.scrollCallback = callback;
    }

    public void setCloseCallback(IWindowCloseCallback callback)
    {
        if (callback == null)
            callback = (win) -> {};

        this.windowCloseCallback = callback;
    }

    public void setFocusCallback(IWindowFocusCallback callback)
    {
        if (callback == null)
            callback = (win, focused) -> {};

        this.windowFocusCallback = callback;
    }

    public void setIconifyCallback(IWindowIconifyCallback callback)
    {
        if (callback == null)
            callback = (win, ic) -> {};

        this.windowIconifyCallback = callback;
    }

    public void setPositionCallback(IWindowPositionCallback callback)
    {
        if (callback == null)
            callback = (win, x, y) -> {};

        this.windowPositionCallback = callback;
    }

    public void setRefreshCallback(IWindowRefreshCallback callback)
    {
        if (callback == null)
            callback = (win) -> {};

        this.windowRefreshCallback = callback;
    }

    public void setSizeCallback(IWindowSizeCallback callback)
    {
        if (callback == null)
            callback = (win, w, h) -> {};

        this.windowSizeCallback = callback;
    }

    public long getHandle()
    {
        return handle;
    }

    public static void setHint(int hint, int value)
    {
        glfwWindowHint(hint, value);
    }

    public static void setHint(int hint, boolean value)
    {
        setHint(hint, value ? 1 : 0);
    }

    public static void setDefaultHints()
    {
        glfwDefaultWindowHints();
    }

    public static Window getCurrentContext()
    {
        return registeredWindows.get(glfwGetCurrentContext());
    }
}
