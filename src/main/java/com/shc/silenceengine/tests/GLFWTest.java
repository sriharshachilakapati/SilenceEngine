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

package com.shc.silenceengine.tests;

import com.shc.silenceengine.core.glfw.Monitor;
import com.shc.silenceengine.core.glfw.VideoMode;
import com.shc.silenceengine.core.glfw.Window;
import com.shc.silenceengine.utils.NativesLoader;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
public class GLFWTest
{
    public static void main(String[] args)
    {
        NativesLoader.loadLWJGL();
        GLFW.glfwInit();

        List<Monitor> monitors = Monitor.getMonitors();

        for (Monitor monitor : monitors)
        {
            System.out.println(monitor);

            List<VideoMode> modes = monitor.getVideoModes();
            modes.forEach(System.out::println);
        }

        Window.setHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        Window.setHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
        Window.setHint(GLFW.GLFW_RESIZABLE, true);

        Window window = new Window(800, 600, "Test");
        window.makeCurrent();

        System.out.println(GL11.glGetString(GL11.GL_VERSION));

        while (!window.shouldClose())
        {
            window.swapBuffers();

            GLFW.glfwPollEvents();
        }

        System.out.println("Window Position: " + window.getPosition() + "  |  Window Size: " + window.getSize());

        window.destroy();
        GLFW.glfwTerminate();
    }
}
