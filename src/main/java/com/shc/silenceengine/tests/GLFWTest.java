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

import com.shc.silenceengine.core.glfw.Cursor;
import com.shc.silenceengine.core.glfw.GLFW3;
import com.shc.silenceengine.core.glfw.Window;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.utils.NativesLoader;

/**
 * This is the getting started example on the GLFW website ported to use the GLFW wrapper API.
 *
 * @author Sri Harsha Chilakapati
 */
public class GLFWTest
{
    public static void main(String[] args)
    {
        // Start with loading the natives
        NativesLoader.loadLWJGL();

        // Initialize GLFW library
        if (!GLFW3.init())
            return;

        // Create a windowed mode window and its OpenGL context
        Window window = new Window(640, 480, "Hello World");

        // Make the window's context current
        window.makeCurrent();

        // Change cursor
        Texture cursorImage = Texture.fromResource("resources/cursor.png");
        Cursor cursor = new Cursor(cursorImage);

        window.setCursor(cursor);

        // Loop until the user closes the window
        while (!window.shouldClose())
        {
            Keyboard.startEventFrame();

            // Render here
            if (Keyboard.isClicked(Keyboard.KEY_ESCAPE))
                break;

            // Swap front and back buffers
            window.swapBuffers();

            // Poll for and process events
            GLFW3.pollEvents();
            Keyboard.clearEventFrame();
        }

        window.destroy();
        cursor.destroy();

        GLFW3.terminate();
    }
}
