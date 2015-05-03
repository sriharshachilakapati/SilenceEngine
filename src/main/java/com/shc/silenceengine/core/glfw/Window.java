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
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.input.Mouse;
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
 * <p> This class is an object oriented wrapper for GLFWwindow structure, which encapsulates both the window and the
 * context. For example, this creates a 640 by 480 windowed mode window.</p>
 *
 * <pre>
 *     Window window = new Window(640, 480, "My Title");
 * </pre>
 *
 * <p> There are several other overloads of the constructors allowing you to specify the size, title, monitor, a parent
 * window and the video mode. The window object is provided along with the input events, so the event handlers can tell
 * which window recieved the input.</p>
 *
 * <h2>Full screen windows</h2>
 *
 * <p> To create a full screen window, you need to specify which monitor the window should use. In most cases, the
 * user's primary monitor is a good choice. For more information about retrieving monitors, see
 * {@link Monitor#getMonitors()}.</p>
 *
 * <pre>
 *     Window window = new Window(640, 480, Monitor.getPrimaryMonitor());
 * </pre>
 *
 * <p> Full screen windows cover the entire display area of a monitor, have no border or decorations. You can also pass
 * a {@link VideoMode} to the constructor along with the monitor to change the resolution of the monitor.</p>
 *
 * <h2>Window hints</h2>
 *
 * <p> There are a number of hints that can be set before the creation of a window and context. Some affect the window
 * itself, others affect the framebuffer or context. These hints are set to their default values each time the library
 * is initialized with {@link GLFW3#init()}, can be set individually with {@link Window#setHint(int, boolean)} or
 * {@link Window#setHint(int, int)}, and reset all at once to their defaults with {@link Window#setDefaultHints()}.</p>
 *
 * <pre>
 *     Window.setHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
 *     Window.setHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
 *     Window.setHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, true);
 *     Window.setHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
 *
 *     Window coreProfileWindow = new Window(640, 480);
 * </pre>
 *
 * <p> Note that hints need to be set before the creation of the window and context you wish to have the specified
 * attributes.</p>
 *
 * <h2>Context object sharing</h2>
 *
 * <p> When creating a window and its OpenGL context, you can specify another window whose context the new one should
 * share its objects (textures, vertex and element buffers, etc.) with.</p>
 *
 * <pre>
 *     Window secondWindow = new Window(640, 480, "Second Window", firstWindow);
 * </pre>
 *
 * <p> Object sharing is implemented by the operating system and graphics driver. On platforms where it is possible to
 * choose which types of objects are shared, GLFW requests that all types are shared. A common exception to this is VAO
 * objects, they have to be recreated for every context.</p>
 *
 * @see Monitor
 * @see VideoMode
 * @see Cursor
 *
 * @author Sri Harsha Chilakapati
 */
public class Window
{
    // A HashMap to store the Window objects based on the handle of the window.
    // This is used to send the Window object to the input events because, say
    // long pointers are ugly in Java.
    private static Map<Long, Window> registeredWindows = new HashMap<>();

    private long handle;

    private Vector2 position;
    private Vector2 size;

    private String  title;
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

    /**
     * Creates a window which is 800 by 600 in dimensions with the title "SilenceEngine Window".
     */
    public Window()
    {
        this(800, 600);
    }

    /**
     * Creates a windowed mode window with a specified width and height, in screen coordinates. The title will be the
     * default title which is "SilenceEngine Window".
     *
     * @param width  The desired width, in screen coordinates, of the window. This must be greater than zero.
     * @param height The desired height, in screen coordinates, of the window. This must be greater than zero.
     */
    public Window(int width, int height)
    {
        this(width, height, (Monitor) null, null);
    }


    /**
     * Crates a window and its associated OpenGL context, by taking into account the resolution of the screen and the
     * monitor that the window should be made full screen on, and another window if you want to share the context of the
     * other window.
     *
     * @param width   The desired width, in screen coordinates, of the window. This must be greater than zero.
     * @param height  The desired height, in screen coordinates, of the window. This must be greater than zero.
     * @param monitor The monitor to use for full screen mode, or <code>null</code> to use windowed mode.
     * @param share   The window whose context to share resources with, or <code>null</code> to not share resources.
     */
    public Window(int width, int height, Monitor monitor, Window share)
    {
        this(width, height, "SilenceEngine Window", monitor, share);
    }

    /**
     * <p> Creates a window and its associated OpenGL context. Most of the options controlling how the window and its
     * context should be created are specified with window hints.</p>
     *
     * <p> Successful creation does not change which context is current. Before you can use the newly created context,
     * you need to make it current.</p>
     *
     * <p> The created window, framebuffer and context may differ from what you requested, as not all parameters and
     * hints are hard constraints. This includes the size of the window, especially for full screen windows. You could
     * query them manually after the window is created.</p>
     *
     * <p> To create a full screen window, you need to specify the monitor the window will cover. If no monitor is
     * specified, windowed mode will be used. Unless you have a way for the user to choose a specific monitor, it is
     * recommended that you pick the primary monitor.</p>
     *
     * <p> For full screen windows, the specified size becomes the resolution of the window's desired video mode. As
     * long as a full screen window has input focus, the supported video mode most closely matching the desired video
     * mode is set for the specified monitor.</p>
     *
     * <p> By default, newly created windows use the placement recommended by the window system. To create the window at
     * a specific position, make it initially invisible using the <code>GLFW_VISIBLE</code> window hint, set its
     * position and then show it.</p>
     *
     * <p> If a full screen window has input focus, the screensaver is prohibited from starting.</p>
     *
     * <p> The swap interval is not set during window creation and the initial value may vary depending on driver
     * settings and defaults.</p>
     *
     * @param width   The desired width, in screen coordinates, of the window. This must be greater than zero.
     * @param height  The desired height, in screen coordinates, of the window. This must be greater than zero.
     * @param title   The desired title string of the Window. This can also contain UTF-8 characters.
     * @param monitor The monitor to use for full screen mode, or <code>null</code> to use windowed mode.
     * @param share   The window whose context to share resources with, or <code>null</code> to not share resources.
     */
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

    /**
     * This constructor creates a fullscreen mode window that fills the monitor. The resolution will be set to the default
     * resolution of the monitor supplied. If the monitor is supplied as <code>null</code>, then a windowed mode window
     * is created instead which is 800 by 600 in size.
     *
     * @param monitor The monitor to use for full screen mode, or <code>null</code> to use windowed mode.
     */
    public Window(Monitor monitor)
    {
        this(monitor != null ? monitor.getVideoMode().getWidth() : 800,
                monitor != null ? monitor.getVideoMode().getHeight() : 600, monitor);
    }

    /**
     * Creates a full screen window over a specified monitor. The width and height are the resolution of the monitor.
     *
     * @param width   The desired width, in screen coordinates, of the window. This must be greater than zero.
     * @param height  The desired height, in screen coordinates, of the window. This must be greater than zero.
     * @param monitor The monitor to use for full screen mode, or <code>null</code> to use windowed mode.
     */
    public Window(int width, int height, Monitor monitor)
    {
        this(width, height, "SilenceEngine Window", monitor);
    }

    public Window(int width, int height, String title, Monitor monitor)
    {
        this(width, height, title, monitor, null);
    }

    public Window(Window share)
    {
        this(800, 600, share);
    }

    public Window(int width, int height, Window share)
    {
        this(width, height, "SilenceEngine window", share);
    }

    public Window(int width, int height, String title, Window share)
    {
        this(width, height, title, null, share);
    }

    public Window(String title)
    {
        this(800, 600, title);
    }

    public Window(int width, int height, String title)
    {
        this(width, height, title, (Monitor) null);
    }

    public Window(VideoMode videoMode, Monitor monitor)
    {
        this(videoMode, "SilenceEngine Window", monitor);
    }

    public Window(VideoMode videoMode, String title, Monitor monitor)
    {
        this(videoMode, title, monitor, null);
    }

    public Window(VideoMode videoMode, String title, Monitor monitor, Window share)
    {
        this(videoMode.getWidth(), videoMode.getHeight(), title, monitor, share);
    }

    public static void setHint(int hint, boolean value)
    {
        setHint(hint, value ? 1 : 0);
    }

    public static void setHint(int hint, int value)
    {
        glfwWindowHint(hint, value);
    }

    public static void setDefaultHints()
    {
        glfwDefaultWindowHints();
    }

    public static Window getCurrentContext()
    {
        return registeredWindows.get(glfwGetCurrentContext());
    }

    public long getHandle()
    {
        return handle;
    }

    private void initNativeCallbacks()
    {
        // Initialize overriden callbacks
        initCustomCallbacks();

        // Create the callback functions that re-post the events
        glfwCharCallback = GLFWCharCallback((window, codePoint) ->
                characterCallback.invoke(registeredWindows.get(window), codePoint));

        glfwCharModsCallback = GLFWCharModsCallback((window, codePoint, mods) ->
                characterModsCallback.invoke(registeredWindows.get(window), codePoint, mods));

        glfwCursorEnterCallback = GLFWCursorEnterCallback((window, entered) ->
                cursorEnterCallback.invoke(registeredWindows.get(window), entered != 0));

        glfwCursorPosCallback = GLFWCursorPosCallback((win, xPos, yPos) ->
        {
            Window window = registeredWindows.get(win);

            cursorPositionCallback.invoke(window, xPos, yPos);
            Mouse.glfwCursorCallback(window, xPos, yPos);
        });

        glfwDropCallback = GLFWDropCallback((window, count, names) ->
                dropCallback.invoke(registeredWindows.get(window), Callbacks.dropCallbackNamesString(count, names)));

        glfwFramebufferSizeCallback = GLFWFramebufferSizeCallback((window, width, height) ->
                framebufferSizeCallback.invoke(registeredWindows.get(window), width, height));

        glfwKeyCallback = GLFWKeyCallback((win, key, scanCode, action, mods) ->
        {
            Window window = registeredWindows.get(win);
            keyCallback.invoke(window, key, scanCode, action, mods);
            Keyboard.glfwKeyCallback(window, key, scanCode, action, mods);
        });

        glfwMouseButtonCallback = GLFWMouseButtonCallback((win, button, action, mods) ->
        {
            Window window = registeredWindows.get(win);
            mouseButtonCallback.invoke(window, button, action, mods);
            Mouse.glfwMouseButtonCallback(window, button, action, mods);
        });

        glfwScrollCallback = GLFWScrollCallback((win, xOffset, yOffset) ->
        {
            Window window = registeredWindows.get(win);
            scrollCallback.invoke(window, xOffset, yOffset);
            Mouse.glfwScrollCallback(window, xOffset, yOffset);
        });

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

    public void setCursorPos(Vector2 pos)
    {
        setCursorPos(pos.x, pos.y);
    }

    public void setCursorPos(double xPos, double yPos)
    {
        glfwSetCursorPos(handle, xPos, yPos);
    }

    public void setInputMode(int mode, int value)
    {
        glfwSetInputMode(handle, mode, value);
    }

    public void setCursor(Cursor cursor)
    {
        glfwSetCursor(handle, cursor == null ? NULL : cursor.getHandle());
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

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;

        glfwSetWindowTitle(handle, title);
    }

    public int getAttribute(int attribute)
    {
        return glfwGetWindowAttrib(handle, attribute);
    }

    public ICharacterCallback getCharacterCallback()
    {
        return characterCallback;
    }

    public void setCharacterCallback(ICharacterCallback callback)
    {
        if (callback == null)
            callback = (window, codePoint) -> {};

        this.characterCallback = callback;
    }

    public ICharacterModsCallback getCharacterModsCallback()
    {
        return characterModsCallback;
    }

    public void setCharacterModsCallback(ICharacterModsCallback callback)
    {
        if (callback == null)
            callback = (window, cp, mods) -> {};

        this.characterModsCallback = callback;
    }

    public ICursorEnterCallback getCursorEnterCallback()
    {
        return cursorEnterCallback;
    }

    public void setCursorEnterCallback(ICursorEnterCallback callback)
    {
        if (callback == null)
            callback = (window, entered) -> {};

        this.cursorEnterCallback = callback;
    }

    public ICursorPositionCallback getCursorPositionCallback()
    {
        return cursorPositionCallback;
    }

    public void setCursorPositionCallback(ICursorPositionCallback callback)
    {
        if (callback == null)
            callback = (window, x, y) -> {};

        this.cursorPositionCallback = callback;
    }

    public IDropCallback getDropCallback()
    {
        return dropCallback;
    }

    public void setDropCallback(IDropCallback callback)
    {
        if (callback == null)
            callback = (window, paths) -> {};

        this.dropCallback = callback;
    }

    public IFramebufferSizeCallback getFramebufferSizeCallback()
    {
        return framebufferSizeCallback;
    }

    public void setFramebufferSizeCallback(IFramebufferSizeCallback callback)
    {
        if (callback == null)
            callback = (window, w, h) -> {};

        this.framebufferSizeCallback = callback;
    }

    public IKeyCallback getKeyCallback()
    {
        return keyCallback;
    }

    public void setKeyCallback(IKeyCallback callback)
    {
        if (callback == null)
            callback = (win, key, sc, act, mods) -> {};

        this.keyCallback = callback;
    }

    public IMouseButtonCallback getMouseButtonCallback()
    {
        return mouseButtonCallback;
    }

    public void setMouseButtonCallback(IMouseButtonCallback callback)
    {
        if (callback == null)
            callback = (win, b, act, mods) -> {};

        this.mouseButtonCallback = callback;
    }

    public IScrollCallback getScrollCallback()
    {
        return scrollCallback;
    }

    public void setScrollCallback(IScrollCallback callback)
    {
        if (callback == null)
            callback = (win, x, y) -> {};

        this.scrollCallback = callback;
    }

    public IWindowCloseCallback getCloseCallback()
    {
        return windowCloseCallback;
    }

    public void setCloseCallback(IWindowCloseCallback callback)
    {
        if (callback == null)
            callback = (win) -> {};

        this.windowCloseCallback = callback;
    }

    public IWindowFocusCallback getFocusCallback()
    {
        return windowFocusCallback;
    }

    public void setFocusCallback(IWindowFocusCallback callback)
    {
        if (callback == null)
            callback = (win, focused) -> {};

        this.windowFocusCallback = callback;
    }

    public IWindowIconifyCallback getIconifyCallback()
    {
        return windowIconifyCallback;
    }

    public void setIconifyCallback(IWindowIconifyCallback callback)
    {
        if (callback == null)
            callback = (win, ic) -> {};

        this.windowIconifyCallback = callback;
    }

    public IWindowPositionCallback getPositionCallback()
    {
        return windowPositionCallback;
    }

    public void setPositionCallback(IWindowPositionCallback callback)
    {
        if (callback == null)
            callback = (win, x, y) -> {};

        this.windowPositionCallback = callback;
    }

    public IWindowRefreshCallback getRefreshCallback()
    {
        return windowRefreshCallback;
    }

    public void setRefreshCallback(IWindowRefreshCallback callback)
    {
        if (callback == null)
            callback = (win) -> {};

        this.windowRefreshCallback = callback;
    }

    public IWindowSizeCallback getSizeCallback()
    {
        return windowSizeCallback;
    }

    public void setSizeCallback(IWindowSizeCallback callback)
    {
        if (callback == null)
            callback = (win, w, h) -> {};

        this.windowSizeCallback = callback;
    }
}
