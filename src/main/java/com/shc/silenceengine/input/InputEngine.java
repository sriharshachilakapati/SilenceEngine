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

package com.shc.silenceengine.input;

import com.shc.silenceengine.core.Display;
import com.shc.silenceengine.core.IEngine;
import com.shc.silenceengine.core.glfw.Window;
import com.shc.silenceengine.core.glfw.callbacks.ICursorPositionCallback;
import com.shc.silenceengine.core.glfw.callbacks.IKeyCallback;
import com.shc.silenceengine.core.glfw.callbacks.IMouseButtonCallback;
import com.shc.silenceengine.core.glfw.callbacks.IScrollCallback;

/**
 * @author Sri Harsha Chilakapati
 */
public class InputEngine implements IEngine
{
    @Override
    public void init()
    {
        Controller.create();
    }

    @Override
    public void beginFrame()
    {
        // Poll the controllers
        Controller.poll();

        // Start the event frames
        Keyboard.startEventFrame();
        Mouse.startEventFrame();
        Controller.startEventFrame();
    }

    @Override
    public void endFrame()
    {
        // Clear the event frames
        Keyboard.clearEventFrame();
        Mouse.clearEventFrame();
        Controller.clearEventFrame();
    }

    @Override
    public void dispose()
    {
    }

    private IKeyCallback            keyCallback            = Keyboard::glfwKeyCallback;
    private IMouseButtonCallback    mouseButtonCallback    = Mouse::glfwMouseButtonCallback;
    private ICursorPositionCallback cursorPositionCallback = Mouse::glfwCursorCallback;
    private IScrollCallback         scrollCallback         = Mouse::glfwScrollCallback;

    public void postKeyEvent(Window window, int key, int scanCode, int action, int mods)
    {
        keyCallback.invoke(window, key, scanCode, action, mods);
    }

    public void postKeyEvent(int key, int scanCode, int action, int mods)
    {
        postKeyEvent(Display.getWindow(), key, scanCode, action, mods);
    }

    public void postKeyEvent(int key, int action, int mods)
    {
        postKeyEvent(key, -1, action, mods);
    }

    public void postKeyEvent(int key, int action)
    {
        postKeyEvent(key, action, 0);
    }

    public void postMouseButtonEvent(Window window, int button, int action, int mods)
    {
        mouseButtonCallback.invoke(window, button, action, mods);
    }

    public void postMouseButtonEvent(int button, int action, int mods)
    {
        postMouseButtonEvent(Display.getWindow(), button, action, mods);
    }

    public void postMouseButtonEvent(int button, int action)
    {
        postMouseButtonEvent(button, action, 0);
    }

    public void postMouseScrollEvent(Window window, double dx, double dy)
    {
        scrollCallback.invoke(window, dx, dy);
    }

    public void postMouseScrollEvent(double dx, double dy)
    {
        postMouseScrollEvent(Display.getWindow(), dx, dy);
    }

    public void postMouseCursorPositionEvent(Window window, double x, double y)
    {
        cursorPositionCallback.invoke(window, x, y);
    }

    public void postMouseCursorPositionEvent(double x, double y)
    {
        postMouseCursorPositionEvent(Display.getWindow(), x, y);
    }
}
