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

import com.shc.silenceengine.core.glfw.GLFW3;
import com.shc.silenceengine.core.glfw.Window;
import com.shc.silenceengine.utils.NativesLoader;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

/**
 * @author Sri Harsha Chilakapati
 */
public class GLFWMultiThreadTest
{
    public static void main(String[] args) throws Exception
    {
        NativesLoader.loadLWJGL();

        if (GLFW3.init())
        {
            Window.setHint(GLFW.GLFW_RESIZABLE, false);
            Window.setHint(GLFW.GLFW_SAMPLES, 4);

            Window window = new Window(640, 480, "Main Window");
            window.show();

            Window threadWindow = new Window(640, 480, "Threaded Window");
            threadWindow.show();

            GLFW3.setSwapInterval(1);

            Thread thread = new Thread(() -> myThread(threadWindow));
            thread.start();

            window.makeCurrent();

            while (!window.shouldClose())
            {
                GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

                GL11.glBegin(GL11.GL_QUADS);
                {
                    GL11.glColor4f(0, 1, 0, 1);
                    GL11.glVertex3f(-0.8f, 0.8f, 0);

                    GL11.glColor4f(0, 0, 1, 1);
                    GL11.glVertex3f(0.8f, 0.8f, 0);

                    GL11.glColor4f(1, 0, 0, 1);
                    GL11.glVertex3f(0.8f, -0.8f, 0);

                    GL11.glColor4f(1, 1, 1, 1);
                    GL11.glVertex3f(-0.8f, -0.8f, 0);
                }
                GL11.glEnd();

                window.swapBuffers();
                GLFW3.pollEvents();
            }

            window.destroy();
            threadWindow.destroy();

            thread.interrupt();
            thread.join();
        }

        GLFW3.terminate();
    }

    private static void myThread(Window threadWindow)
    {
        threadWindow.makeCurrent();

        while (!threadWindow.shouldClose())
        {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

            GL11.glBegin(GL11.GL_TRIANGLES);
            {
                GL11.glColor4f(1, 0, 0, 1);
                GL11.glVertex3f(0, 0.8f, 0);

                GL11.glColor4f(0, 1, 0, 1);
                GL11.glVertex3f(0.8f, -0.8f, 0);

                GL11.glColor4f(0, 0, 1, 1);
                GL11.glVertex3f(-0.8f, -0.8f, 0);
            }
            GL11.glEnd();

            threadWindow.swapBuffers();
        }

        // Cannot destroy the window from thread. So simply hide it.
        threadWindow.hide();
    }
}
