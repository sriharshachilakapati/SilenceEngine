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

/**
 * @author Sri Harsha Chilakapati
 */
public final class Controller
{
    public static final int CONTROLLER_0    = 0;
    public static final int CONTROLLER_1    = 1;
    public static final int CONTROLLER_2    = 2;
    public static final int CONTROLLER_3    = 3;
    public static final int CONTROLLER_4    = 4;
    public static final int CONTROLLER_5    = 5;
    public static final int CONTROLLER_6    = 6;
    public static final int CONTROLLER_7    = 7;
    public static final int CONTROLLER_8    = 8;
    public static final int CONTROLLER_9    = 9;
    public static final int CONTROLLER_10   = 10;
    public static final int CONTROLLER_11   = 11;
    public static final int CONTROLLER_12   = 12;
    public static final int CONTROLLER_13   = 13;
    public static final int CONTROLLER_14   = 14;
    public static final int CONTROLLER_15   = 15;
    public static final int NUM_CONTROLLERS = CONTROLLER_15 + 1;

    public static final int BUTTON_A           = 0;
    public static final int BUTTON_B           = 1;
    public static final int BUTTON_X           = 2;
    public static final int BUTTON_Y           = 3;
    public static final int BUTTON_L1          = 4;
    public static final int BUTTON_L2          = 5;
    public static final int BUTTON_R1          = 6;
    public static final int BUTTON_R2          = 7;
    public static final int BUTTON_SELECT      = 8;
    public static final int BUTTON_START       = 9;
    public static final int BUTTON_DPAD_UP     = 10;
    public static final int BUTTON_DPAD_RIGHT  = 11;
    public static final int BUTTON_DPAD_DOWN   = 12;
    public static final int BUTTON_DPAD_LEFT   = 13;
    public static final int BUTTON_LEFT_STICK  = 14;
    public static final int BUTTON_RIGHT_STICK = 15;
    public static final int NUM_BUTTONS        = BUTTON_RIGHT_STICK + 1;

    public static final int AXE_LEFT_X  = 0;
    public static final int AXE_LEFT_Y  = 1;
    public static final int AXE_RIGHT_X = 2;
    public static final int AXE_RIGHT_Y = 3;
    public static final int NUM_AXES    = AXE_RIGHT_Y + 1;

    public static class Button
    {
        public InputState state = InputState.RELEASED;

        boolean newState;

        private Button()
        {
        }

        private void update()
        {
            if (state == InputState.PRESSED)
                state = InputState.WAITING_FOR_RELEASE;

            if (state == InputState.RELEASED)
                state = newState ? InputState.PRESSED : InputState.RELEASED;
            else
                state = newState ? state : InputState.RELEASED;
        }
    }

    public static class Axe
    {
        public InputState state = InputState.RELEASED;

        public double amount;

        private Axe()
        {
        }

        private void update()
        {
            final boolean newState = amount != 0;

            if (state == InputState.PRESSED)
                state = InputState.WAITING_FOR_RELEASE;

            if (state == InputState.RELEASED)
                state = newState ? InputState.PRESSED : InputState.RELEASED;
            else
                state = newState ? state : InputState.RELEASED;
        }
    }

    public static class State
    {
        public final Button[] buttons = new Button[NUM_BUTTONS];
        public final Axe[]    axes    = new Axe[NUM_AXES];

        public boolean connected;
        public String  name;

        private State()
        {
            for (int i = 0; i < NUM_BUTTONS; i++)
                buttons[i] = new Button();

            for (int i = 0; i < NUM_AXES; i++)
                axes[i] = new Axe();
        }

        private void update()
        {
            for (int i = 0; i < NUM_BUTTONS; i++)
                buttons[i].update();

            for (int i = 0; i < NUM_AXES; i++)
                axes[i].update();
        }
    }

    public static final State[] states = new State[NUM_CONTROLLERS];

    static void init()
    {
        for (int i = 0; i < NUM_CONTROLLERS; i++)
            states[i] = new State();
    }

    static void update()
    {
        for (int i = 0; i < NUM_CONTROLLERS; i++)
            states[i].update();
    }
}
