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
import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.core.glfw.Window;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class Mouse
{
    // Constants
    public static final int MOUSE_BUTTON_1 = GLFW_MOUSE_BUTTON_1;
    public static final int MOUSE_BUTTON_2 = GLFW_MOUSE_BUTTON_2;
    public static final int MOUSE_BUTTON_3 = GLFW_MOUSE_BUTTON_3;
    public static final int MOUSE_BUTTON_4 = GLFW_MOUSE_BUTTON_4;
    public static final int MOUSE_BUTTON_5 = GLFW_MOUSE_BUTTON_5;
    public static final int MOUSE_BUTTON_6 = GLFW_MOUSE_BUTTON_6;
    public static final int MOUSE_BUTTON_7 = GLFW_MOUSE_BUTTON_7;
    public static final int MOUSE_BUTTON_8 = GLFW_MOUSE_BUTTON_8;

    public static final int MOUSE_BUTTON_LEFT   = GLFW_MOUSE_BUTTON_LEFT;
    public static final int MOUSE_BUTTON_RIGHT  = GLFW_MOUSE_BUTTON_RIGHT;
    public static final int MOUSE_BUTTON_MIDDLE = GLFW_MOUSE_BUTTON_MIDDLE;

    public static final int MOUSE_BUTTON_LAST = GLFW_MOUSE_BUTTON_LAST;

    private static List<Integer> events          = new ArrayList<>();
    private static List<Integer> eventsThisFrame = new ArrayList<>();
    private static List<Integer> eventsLastFrame = new ArrayList<>();
    private static float x, y, dx, dy;
    private static float scrollX, scrollY;

    public static void startEventFrame()
    {
        eventsThisFrame.clear();
        eventsThisFrame.addAll(events);
    }

    public static void clearEventFrame()
    {
        eventsLastFrame.clear();
        eventsLastFrame.addAll(eventsThisFrame);
    }

    public static boolean isReleased(int button)
    {
        return !isPressed(button);
    }

    public static boolean isPressed(int button)
    {
        return Display.getWindow().getMouseButton(button) == GLFW_PRESS || isClicked(button);
    }

    public static boolean isClicked(int button)
    {
        return eventsThisFrame.contains(button) && !eventsLastFrame.contains(button);
    }

    public static float getX()
    {
        return x;
    }

    public static float getY()
    {
        return y;
    }

    public static float getDX()
    {
        float dx = Mouse.dx;
        Mouse.dx = 0;
        return dx;
    }

    public static float getDY()
    {
        float dy = Mouse.dy;
        Mouse.dy = 0;
        return dy;
    }

    public static float getScrollX()
    {
        float scrollX = Mouse.scrollX;
        Mouse.scrollX = 0;
        return scrollX;
    }

    public static float getScrollY()
    {
        float scrollY = Mouse.scrollY;
        Mouse.scrollY = 0;
        return scrollY;
    }

    public static void glfwMouseButtonCallback(Window window, int button, int action, int mods)
    {
        Mouse.setButton(button, action != GLFW_RELEASE);
    }

    public static void setButton(int button, boolean pressed)
    {
        if (pressed && !events.contains(button))
            events.add(button);

        if (!pressed && events.contains(button))
            events.remove((Integer) button);
    }

    public static void glfwCursorCallback(Window window, double x, double y)
    {
        Mouse.dx = (float) x - Mouse.x;
        Mouse.dy = (float) y - Mouse.y;

        Mouse.x = (float) x;
        Mouse.y = (float) y;
    }

    public static void glfwScrollCallback(Window window, double scrollX, double scrollY)
    {
        Mouse.scrollX = (float) scrollX;
        Mouse.scrollY = (float) scrollY;
    }

    public static String getButtonName(int button)
    {
        for (Field field : Mouse.class.getDeclaredFields())
            try
            {
                if (Modifier.isStatic(field.getModifiers()) && field.getInt(null) == button)
                    return field.getName();
            }
            catch (Exception e)
            {
                SilenceException.reThrow(e);
            }

        return "Unknown mouse button code";
    }
}
