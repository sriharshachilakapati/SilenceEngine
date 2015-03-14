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

import com.shc.silenceengine.core.glfw.callbacks.IErrorCallback;
import com.shc.silenceengine.math.Vector3;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFWErrorCallback;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;

/**
 * @author Sri Harsha Chilakapati
 */
public final class GLFW3
{
    private static GLFWErrorCallback glfwErrorCallback;
    private static IErrorCallback    errorCallback;

    public static boolean init()
    {
        int state = glfwInit();

        // Return immediately in case of error
        if (state == 0)
            return false;

        // Error callback that does nothing
        setErrorCallback(null);
        glfwErrorCallback = GLFWErrorCallback((error, description) ->
            errorCallback.invoke(error, Callbacks.errorCallbackDescriptionString(description))
        );

        // Register the callback
        glfwSetErrorCallback(glfwErrorCallback);

        return true;
    }

    public static void pollEvents()
    {
        glfwPollEvents();
    }

    public static void waitEvents()
    {
        glfwWaitEvents();
    }

    public static void postEmptyEvent()
    {
        glfwPostEmptyEvent();
    }

    public static void setSwapInterval(int i)
    {
        glfwSwapInterval(i);
    }

    public static Vector3 getVersion()
    {
        IntBuffer major = BufferUtils.createIntBuffer(1);
        IntBuffer minor = BufferUtils.createIntBuffer(1);
        IntBuffer rev   = BufferUtils.createIntBuffer(1);

        glfwGetVersion(major, minor, rev);

        return new Vector3(major.get(), minor.get(), rev.get());
    }

    public static String getVersionString()
    {
        return glfwGetVersionString();
    }

    public static void terminate()
    {
        if (glfwErrorCallback != null)
            glfwErrorCallback.release();

        glfwTerminate();
    }

    public static void setErrorCallback(IErrorCallback errorCallback)
    {
        if (errorCallback == null)
            errorCallback = (error, description) -> {};

        GLFW3.errorCallback = errorCallback;
    }
}
