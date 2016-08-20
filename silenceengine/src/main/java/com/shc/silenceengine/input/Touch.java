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
 */

package com.shc.silenceengine.input;

import com.shc.silenceengine.math.Vector2;

/**
 * @author Sri Harsha Chilakapati
 */
public final class Touch
{
    public static final int FINGER_0 = 1;
    public static final int FINGER_1 = 2;
    public static final int FINGER_2 = 3;
    public static final int FINGER_3 = 4;
    public static final int FINGER_4 = 5;
    public static final int FINGER_5 = 6;
    public static final int FINGER_6 = 7;
    public static final int FINGER_7 = 8;
    public static final int FINGER_8 = 9;
    public static final int FINGER_9 = 10;

    public static final int NUM_FINGERS = 11;

    private static boolean isAnyFingerDown;

    private static InputState[] fingerStates    = new InputState[NUM_FINGERS];
    private static Vector2[]    fingerPositions = new Vector2[NUM_FINGERS];

    static boolean[] eventStates    = new boolean[NUM_FINGERS];
    static Vector2[] eventPositions = new Vector2[NUM_FINGERS];

    /**
     * Prevent instantiation.
     */
    private Touch()
    {
    }

    static void init()
    {
        for (int i = FINGER_0; i <= FINGER_9; i++)
        {
            fingerStates[i] = InputState.RELEASED;

            fingerPositions[i] = new Vector2();
            eventPositions[i] = new Vector2();
        }
    }

    static void update()
    {
        isAnyFingerDown = false;

        for (int i = FINGER_0; i <= FINGER_9; i++)
        {
            if (fingerStates[i] == InputState.PRESSED)
                // If the current finger state is pressed, change it to waiting for release
                fingerStates[i] = InputState.WAITING_FOR_RELEASE;

            if (fingerStates[i] == InputState.RELEASED)
                // Set the finger state to pressed or released depending on whether it's down or not
                fingerStates[i] = eventStates[i] ? InputState.PRESSED : InputState.RELEASED;
            else
                // Set the finger state to released if the event state is false. Otherwise keep it intact.
                fingerStates[i] = eventStates[i] ? fingerStates[i] : InputState.RELEASED;

            if (fingerStates[i] != InputState.RELEASED)
                isAnyFingerDown = true;

            fingerPositions[i].set(eventPositions[i]);
        }
    }

    public static boolean isFingerDown(int finger)
    {
        return fingerStates[finger] != InputState.RELEASED;
    }

    public static boolean isFingerUp(int finger)
    {
        return fingerStates[finger] == InputState.RELEASED;
    }

    public static boolean isFingerTapped(int finger)
    {
        return fingerStates[finger] == InputState.PRESSED;
    }

    public static InputState getFingerState(int finger)
    {
        return fingerStates[finger];
    }

    public static boolean isAnyFingerDown()
    {
        return isAnyFingerDown;
    }

    public static boolean isNoFingerDown()
    {
        return !isAnyFingerDown;
    }

    public static Vector2 getFingerPosition(int finger)
    {
        return fingerPositions[finger];
    }
}
