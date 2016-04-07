/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2016 Sri Harsha Chilakapati
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
 *
 */

package com.shc.silenceengine.backend.lwjgl.glfw;

import com.shc.silenceengine.backend.lwjgl.glfw.callbacks.IErrorCallback;
import com.shc.silenceengine.math.Vector3;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * Provides Java-ified versions of the GLFW methods. Use the methods of this class if you want to use the other classes
 * of this GLFW package. This is because the callbacks you have set will have to be translated to the callbacks that
 * LWJGL provides which can be sent to the native GLFW library. This abstraction is necessary to allow the more OOP use
 * of the GLFW library.
 *
 * @author Sri Harsha Chilakapati
 */
public final class GLFW3
{
    // The error callbacks
    private static GLFWErrorCallback glfwErrorCallback;
    private static IErrorCallback    errorCallback;

    private static boolean initialized = false;

    /**
     * Prevent instantiation for a static utility class.
     */
    private GLFW3()
    {
    }

    /**
     * Initializes the GLFW3 library. If you are not using the built-in Game class of the SilenceEngine, you should call
     * this method as soon as you can after loading the LWJGL3 natives.
     *
     * @return True if initialised successful, or else False.
     */
    public static boolean init()
    {
        if (isInitialized())
            return true;

        int state = glfwInit();

        // Return immediately in case of error
        if (state == 0)
            return initialized = false;

        // Error callback that does nothing
        setErrorCallback(null);
        glfwErrorCallback = GLFWErrorCallback.createString(errorCallback::invoke);

        // Register the callback
        glfwSetErrorCallback(glfwErrorCallback);

        return initialized = true;
    }

    /**
     * Sets an error callback which will be invoked with an error code and a human readable description of the error
     * when an error occurred in the GLFW library.
     *
     * @param errorCallback The error callback to be invoked when an error occurs in GLFW.
     */
    public static void setErrorCallback(IErrorCallback errorCallback)
    {
        if (errorCallback == null)
            errorCallback = (error, description) -> {
            };

        GLFW3.errorCallback = errorCallback;
    }

    /**
     * <p> This function processes only those events that are already in the event queue and then returns immediately.
     * Processing events will cause the window and input callbacks associated with those events to be called.</p>
     *
     * <p> On some platforms, a window move, resize or menu operation will cause event processing to block. This is due
     * to how event processing is designed on those platforms. You can use the window refresh callback to redraw the
     * contents of your window when necessary during such operations.</p>
     *
     * <p>On some platforms, certain events are sent directly to the application without going through the event queue,
     * causing callbacks to be called outside of a call to one of the event processing functions. Event processing is
     * not required for joystick input to work.</p>
     */
    public static void pollEvents()
    {
        glfwPollEvents();
    }

    /**
     * <p> This function puts the calling thread to sleep until at least one event is available in the event queue. Once
     * one or more events are available, it behaves exactly like glfwPollEvents, i.e. the events in the queue are
     * processed and the function then returns immediately. Processing events will cause the window and input callbacks
     * associated with those events to be called.</p>
     *
     * <p>Since not all events are associated with callbacks, this function may return without a callback having been
     * called even if you are monitoring all callbacks.</p>
     *
     * <p>On some platforms, a window move, resize or menu operation will cause event processing to block. This is due
     * to how event processing is designed on those platforms. You can use the window refresh callback to redraw the
     * contents of your window when necessary during such operations. On some platforms, certain callbacks may be called
     * outside of a call to one of the event processing functions.</p>
     *
     * <p>If no windows exist, this function returns immediately. For synchronization of threads in applications that do
     * not create windows, use your threading library of choice. Event processing is not required for joystick input to
     * work.</p>
     */
    public static void waitEvents()
    {
        glfwWaitEvents();
    }

    /**
     * <p> This function posts an empty event from the current thread to the event queue, causing glfwWaitEvents to
     * return.</p>
     *
     * <p> If no windows exist, this function returns immediately. For synchronization of threads in applications that
     * do not create windows, use your threading library of choice.</p>
     */
    public static void postEmptyEvent()
    {
        glfwPostEmptyEvent();
    }

    /**
     * <p> This function sets the swap interval for the current context, i.e. the number of screen updates to wait from
     * the time {@link GLFW#glfwSwapBuffers(long)} was called before swapping the buffers and returning. This is
     * sometimes called vertical synchronization, vertical retrace synchronization or just vsync.</p>
     *
     * <p> Contexts that support either of the <code>WGL_EXT_swap_control_tear</code> and
     * <code>GLX_EXT_swap_control_tear</code> extensions also accept negative swap intervals, which allow the driver to
     * swap even if a frame arrives a little bit late. You can check for the presence of these extensions using {@link
     * GLFW#glfwExtensionSupported(CharSequence)}. For more information about swap tearing, see the extension
     * specifications.</p>
     *
     * <p> A context must be current on the calling thread. Calling this function without a current context will cause a
     * <code>GLFW_NO_CURRENT_CONTEXT</code> error.</p>
     *
     * @param i The number of screen updates to wait before swapping the buffers.
     */
    public static void setSwapInterval(int i)
    {
        glfwSwapInterval(i);
    }

    /**
     * <p> This function retrieves the major, minor and revision numbers of the GLFW library. It is intended for when
     * you are using GLFW as a shared library and want to ensure that you are using the minimum required version. It is
     * included here so you may want to write the GLFW version to a log file.</p>
     *
     * <p> The version is returned as a {@link Vector3} object whose components x, y, z represent the major, minor and
     * revision numbers of the version respectively</p>
     *
     * @return The version of the GLFW3 library linked with LWJGL3 as a Vector3 object.
     */
    public static Vector3 getVersion()
    {
        IntBuffer version = BufferUtils.createIntBuffer(3);
        nglfwGetVersion(memAddress(version), memAddress(version) + Integer.BYTES, memAddress(version) + 2 * Integer.BYTES);
        return new Vector3(version.get(0), version.get(1), version.get(2));
    }

    /**
     * <p> This function returns the compile-time generated version string of the GLFW library binary. It describes the
     * version, platform, compiler and any platform-specific compile-time options.</p>
     *
     * <p><b>Do not use the version string</b> to parse the GLFW library version. The getVersion() function already
     * provides the version of the running library binary.</p>
     *
     * @return The String representation of the compile-time generated GLFW version string.
     */
    public static String getVersionString()
    {
        return glfwGetVersionString();
    }

    /**
     * <p> This function destroys all remaining windows and cursors, restores any modified gamma ramps and frees any
     * other allocated resources. Once this function is called, you must again call {@link GLFW3#init()} successfully
     * before you will be able to use most GLFW functions.</p>
     *
     * <p> If GLFW has been successfully initialized, this function should be called before the application exits. If
     * initialization fails, there is no need to call this function, as it is called by {@link GLFW3#init()} before it
     * returns failure.</p>
     */
    public static void terminate()
    {
        if (!isInitialized())
            return;

        if (glfwErrorCallback != null)
            glfwErrorCallback.free();

        glfwTerminate();
        initialized = false;
    }

    /**
     * Returns whether GLFW3 has been initialized or not.
     *
     * @return True if GLFW3 is initialized with {@link #init()}. False otherwise.
     */
    public static boolean isInitialized()
    {
        return initialized;
    }
}
