/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 Sri Harsha Chilakapati
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

/**
 * @author Sri Harsha Chilakapati
 */
public final class Mouse
{
    /**
     * Mouse button codes.
     */
    public static final int
            BUTTON_1      = 1,
            BUTTON_2      = 2,
            BUTTON_3      = 3,
            BUTTON_4      = 4,
            BUTTON_5      = 5,
            BUTTON_6      = 6,
            BUTTON_7      = 7,
            BUTTON_8      = 8,
            BUTTON_LEFT   = BUTTON_1,
            BUTTON_RIGHT  = BUTTON_2,
            BUTTON_MIDDLE = BUTTON_3;

    public static final int BUTTON_FIRST = 1;
    public static final int BUTTON_LAST  = 8;
    public static final int NUM_BUTTONS  = BUTTON_LAST + 1;

    private static InputState[] buttonStates = new InputState[NUM_BUTTONS];

    /**
     * The x-coordinate of Mouse cursor
     */
    public static int x;

    /**
     * The y-coordinate of Mouse cursor
     */
    public static int y;

    /**
     * The delta movement horizontally
     */
    public static int dx;

    /**
     * The delta movement vertically
     */
    public static int dy;

    /**
     * The delta scroll value horizontally
     */
    public static float deltaScrollX;

    /**
     * The delta scroll value vertically
     */
    public static float deltaScrollY;

    static boolean[] eventButtonStates = new boolean[NUM_BUTTONS];

    /**
     * Prevent instantiation.
     */
    private Mouse()
    {
    }

    static void init()
    {
        for (int i = BUTTON_FIRST; i <= BUTTON_LAST; i++)
            buttonStates[i] = InputState.RELEASED;
    }

    static void update()
    {
        for (int i = BUTTON_FIRST; i <= BUTTON_LAST; i++)
        {
            if (buttonStates[i] == InputState.PRESSED)
                // If the current button state is pressed, change it to waiting for release
                buttonStates[i] = InputState.WAITING_FOR_RELEASE;

            if (buttonStates[i] == InputState.RELEASED)
                // Set the button state to pressed or released depending on whether it's down or not
                buttonStates[i] = eventButtonStates[i] ? InputState.PRESSED : InputState.RELEASED;
            else
                // Set the button state to released if the event state is false. Otherwise keep it intact.
                buttonStates[i] = eventButtonStates[i] ? buttonStates[i] : InputState.RELEASED;
        }
    }

    public static boolean isButtonDown(int button)
    {
        return buttonStates[button] != InputState.RELEASED;
    }

    public static boolean isButtonUp(int button)
    {
        return buttonStates[button] == InputState.RELEASED;
    }

    public static boolean isButtonTapped(int button)
    {
        return buttonStates[button] == InputState.PRESSED;
    }

    public static InputState getButtonState(int button)
    {
        return buttonStates[button];
    }
}
