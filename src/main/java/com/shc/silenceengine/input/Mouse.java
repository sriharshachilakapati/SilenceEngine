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

import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.core.glfw.Window;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

/**
 * A class for handling polled Mouse input.
 *
 * @author Sri Harsha Chilakapati
 * @author Josh "ShadowLordAlpha"
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

    /**
     * Remove all events from the current event frame and add all events from the event buffer into the current event
     * frame
     */
    public static void startEventFrame()
    {
        eventsThisFrame.clear();
        eventsThisFrame.addAll(events);
    }

    /**
     * Remove all events from the last frame and move all events from the current event frame into the last frame list.
     */
    public static void clearEventFrame()
    {
        eventsLastFrame.clear();
        eventsLastFrame.addAll(eventsThisFrame);
    }

    /**
     * Gets if the button has not been pressed this event frame
     *
     * @param button The button
     *
     * @return true if the button has not been pressed this event frame
     */
    public static boolean isReleased(int button)
    {
        return !isPressed(button);
    }

    public static boolean isReleased(int button, int... mods)
    {
        return !isPressed(button, mods);
    }

    /**
     * Gets if the button has been pressed this event frame
     *
     * @param button The button
     *
     * @return true if the button has been pressed this event frame
     */
    public static boolean isPressed(int button)
    {
        return eventsThisFrame.contains(button);
    }

    public static boolean isPressed(int button, int... mods)
    {
        for (int mod : mods)
            if (!Keyboard.isPressed(mod))
                return false;

        return isPressed(button);
    }

    /**
     * Gets if the button has been pressed in this event frame and not the frame before
     *
     * @param button The button
     *
     * @return true if the button has been pressed in this frame and not the frame before
     */
    public static boolean isClicked(int button)
    {
        return eventsThisFrame.contains(button) && !eventsLastFrame.contains(button);
    }

    public static boolean isClicked(int button, int... mods)
    {
        for (int mod : mods)
            if (!Keyboard.isPressed(mod))
                return false;

        return isClicked(button);
    }

    /**
     * Gets the Mouses x position
     *
     * @return The mouses x position
     */
    public static float getX()
    {
        return x;
    }

    /**
     * Gets the Mouses y position
     *
     * @return The mouses y position
     */
    public static float getY()
    {
        return y;
    }

    /**
     * Gets the Mouses change in x position. This will reset after the value has been fetched
     *
     * @return The mouses change in x position
     */
    public static float getDX()
    {
        float dx = Mouse.dx;
        Mouse.dx = 0;
        return dx;
    }

    /**
     * Gets the Mouses change in y position. This will reset after the value has been fetched
     *
     * @return The mouses change in y position
     */
    public static float getDY()
    {
        float dy = Mouse.dy;
        Mouse.dy = 0;
        return dy;
    }

    /**
     * Gets the Mouses scroll distance in x direction. This will reset after the value has been fetched
     *
     * @return The Mouses scroll distance in x direction
     */
    public static float getScrollX()
    {
        float scrollX = Mouse.scrollX;
        Mouse.scrollX = 0;
        return scrollX;
    }

    /**
     * Gets the Mouses scroll distance in y direction. This will reset after the value has been fetched
     *
     * @return The Mouses scroll distance in y direction
     */
    public static float getScrollY()
    {
        float scrollY = Mouse.scrollY;
        Mouse.scrollY = 0;
        return scrollY;
    }

    /**
     * Method used to pass GLFW Mouse Button events into the buffer
     *
     * @param window The window that recieved the event.
     * @param button The button that was in the event.
     * @param action The action that was performed with the button.
     * @param mods   The modifiers that were pressed.
     */
    public static void glfwMouseButtonCallback(Window window, int button, int action, int mods)
    {
        Mouse.setButton(button, action != GLFW_RELEASE);

        for (int mod : Keyboard.MODIFIERS)
            if ((mods & mod) == mod)
                Keyboard.setKey(mod, true);
    }

    /**
     * Add or remove a button from the event buffer
     *
     * @param button  The button
     * @param pressed add the button to the buffer if true remove it if false
     */
    public static void setButton(int button, boolean pressed)
    {
        if (pressed && !events.contains(button))
            events.add(button);

        if (!pressed && events.contains(button))
            events.remove((Integer) button);
    }

    /**
     * Method used to pass GLFW cursor move events into the buffer
     *
     * @param window The window that received the event.
     * @param x      The x-coordinate of the mouse cursor.
     * @param y      THe y-coordinate of the mouse cursor.
     */
    public static void glfwCursorCallback(Window window, double x, double y)
    {
        Mouse.dx = (float) x - Mouse.x;
        Mouse.dy = (float) y - Mouse.y;

        Mouse.x = (float) x;
        Mouse.y = (float) y;
    }

    /**
     * Method used to pass GLFW scroll events into the buffer
     *
     * @param window  The window that received the event
     * @param scrollX The amount scrolled on the x-axis
     * @param scrollY The amount scrolled on the y-axis
     */
    public static void glfwScrollCallback(Window window, double scrollX, double scrollY)
    {
        Mouse.scrollX = (float) scrollX;
        Mouse.scrollY = (float) scrollY;
    }

    /**
     * Gets a String with the buttons's human readable name in it
     *
     * @param button The button
     *
     * @return A String with the buttons's human readable name in it
     */
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
