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

import java.util.HashMap;
import java.util.Map;

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
    public static final int MAX_CONTROLLERS = CONTROLLER_15 + 1;

    // Ideal Joystick (https://github.com/sriharshachilakapati/SilenceEngine/wiki/Controller-Input/)
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

    public static final int AXE_LEFT_X  = 0;
    public static final int AXE_LEFT_Y  = 1;
    public static final int AXE_RIGHT_X = 2;
    public static final int AXE_RIGHT_Y = 3;

    // Generic joystick buttons and axes
    public static final int BUTTON_0  = 0;
    public static final int BUTTON_1  = 1;
    public static final int BUTTON_2  = 2;
    public static final int BUTTON_3  = 3;
    public static final int BUTTON_4  = 4;
    public static final int BUTTON_5  = 5;
    public static final int BUTTON_6  = 6;
    public static final int BUTTON_7  = 7;
    public static final int BUTTON_8  = 8;
    public static final int BUTTON_9  = 9;
    public static final int BUTTON_10 = 10;
    public static final int BUTTON_11 = 11;
    public static final int BUTTON_12 = 12;
    public static final int BUTTON_13 = 13;
    public static final int BUTTON_14 = 14;
    public static final int BUTTON_15 = 15;
    public static final int BUTTON_16 = 16;
    public static final int BUTTON_17 = 17;
    public static final int BUTTON_18 = 18;
    public static final int BUTTON_19 = 19;
    public static final int BUTTON_20 = 20;
    public static final int BUTTON_21 = 21;
    public static final int BUTTON_22 = 22;
    public static final int BUTTON_23 = 23;
    public static final int BUTTON_24 = 24;
    public static final int BUTTON_25 = 25;
    public static final int BUTTON_26 = 26;
    public static final int BUTTON_27 = 27;
    public static final int BUTTON_28 = 28;
    public static final int BUTTON_29 = 29;
    public static final int BUTTON_30 = 30;

    public static final int AXIS_0  = 0;
    public static final int AXIS_1  = 1;
    public static final int AXIS_2  = 2;
    public static final int AXIS_3  = 3;
    public static final int AXIS_4  = 4;
    public static final int AXIS_5  = 5;
    public static final int AXIS_6  = 6;
    public static final int AXIS_7  = 7;
    public static final int AXIS_8  = 8;
    public static final int AXIS_9  = 9;
    public static final int AXIS_10 = 10;

    public static final int MAX_BUTTONS = BUTTON_30 + 1;
    public static final int MAX_AXES    = AXIS_10 + 1;

    public static final State[] states = new State[MAX_CONTROLLERS];

    // Prevent construction
    private Controller()
    {
    }

    public static int getNumConnected()
    {
        int count = 0;

        for (int i = 0; i < MAX_CONTROLLERS; i++)
            if (states[i].connected)
                count++;

        return count;
    }

    static void init()
    {
        for (int i = 0; i < MAX_CONTROLLERS; i++)
            states[i] = new State();
    }

    static void update()
    {
        for (int i = 0; i < MAX_CONTROLLERS; i++)
            states[i].update();
    }

    public static class Button
    {
        public InputState state;

        boolean eventState;

        private Button()
        {
            reset();
        }

        private void update()
        {
            if (state == InputState.PRESSED)
                state = InputState.WAITING_FOR_RELEASE;

            if (state == InputState.RELEASED)
                state = eventState ? InputState.PRESSED : InputState.RELEASED;
            else
                state = eventState ? state : InputState.RELEASED;
        }

        private void reset()
        {
            state = InputState.RELEASED;
            eventState = false;
        }
    }

    public static class Axe
    {
        public InputState state;
        public double     amount;

        private Axe()
        {
            reset();
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

        private void reset()
        {
            state = InputState.RELEASED;
            amount = 0;
        }
    }

    public static class State
    {
        public final Button[] buttons = new Button[MAX_BUTTONS];
        public final Axe[]    axes    = new Axe[MAX_AXES];

        public boolean connected;
        public boolean ideal;

        public String name;

        public Mapping buttonMapping;
        public Mapping axeMapping;

        public int numButtons;
        public int numAxes;

        private State()
        {
            for (int i = 0; i < MAX_BUTTONS; i++)
                buttons[i] = new Button();

            for (int i = 0; i < MAX_AXES; i++)
                axes[i] = new Axe();
        }

        private void update()
        {
            for (int i = 0; i < MAX_BUTTONS; i++)
                buttons[i].update();

            for (int i = 0; i < MAX_AXES; i++)
                axes[i].update();
        }

        void reset()
        {
            for (int i = 0; i < MAX_BUTTONS; i++)
                buttons[i].reset();

            for (int i = 0; i < MAX_AXES; i++)
                axes[i].reset();
        }

        public Button getButton(int button)
        {
            return buttons[buttonMapping.getRawCode(button)];
        }

        public Axe getAxe(int axe)
        {
            return axes[axeMapping.getRawCode(axe)];
        }

        public boolean isButtonDown(int button)
        {
            return getButton(button).state != InputState.RELEASED;
        }

        public boolean isButtonTapped(int button)
        {
            return getButton(button).state == InputState.PRESSED;
        }

        public boolean isAxeDown(int axe)
        {
            return getAxe(axe).state != InputState.RELEASED;
        }

        public boolean isAxeTapped(int axe)
        {
            return getAxe(axe).state == InputState.PRESSED;
        }
    }

    public static class Mapping
    {
        private Map<Integer, Integer> keyVal = new HashMap<>();
        private Map<Integer, Integer> valKey = new HashMap<>();

        public void clear()
        {
            keyVal.clear();
            valKey.clear();
        }

        public void map(int rawCode, int idealCode)
        {
            keyVal.put(rawCode, idealCode);
            valKey.put(idealCode, rawCode);
        }

        public int getIdealCode(int rawCode)
        {
            Integer val = keyVal.get(rawCode);
            return val == null ? rawCode : val;
        }

        public int getRawCode(int idealCode)
        {
            Integer key = valKey.get(idealCode);
            return key == null ? idealCode : key;
        }
    }
}
