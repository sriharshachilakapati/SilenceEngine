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

package com.shc.silenceengine.backend.gwt;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.dom.client.KeyCodes;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.events.ControllerConnectionEvent;
import com.shc.silenceengine.input.Controller;
import com.shc.silenceengine.input.InputDevice;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.input.Mouse;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sri Harsha Chilakapati
 */
public class GwtInputDevice extends InputDevice
{
    private final Map<Integer, Integer> keysMap             = new HashMap<>();
    private final Map<Integer, Integer> mouseMap            = new HashMap<>();
    private final Map<Integer, Integer> keyShortCircuitMap  = new HashMap<>();

    private boolean gamepadsExist = false;

    public GwtInputDevice()
    {
        createKeyMapping();
        createMouseMapping();

        Canvas canvas = ((GwtDisplayDevice) SilenceEngine.display).canvas;

        canvas.addKeyPressHandler(event ->
        {
            postTextEvent((char) event.getUnicodeCharCode());
            event.preventDefault();
        });

        canvas.addKeyDownHandler(event ->
        {
            int keyCode = getKeyCode(event.getNativeKeyCode());

            postKeyEvent(keyCode, true);

            switch (keyCode)
            {
                case Keyboard.KEY_UP:
                case Keyboard.KEY_DOWN:
                case Keyboard.KEY_RIGHT:
                case Keyboard.KEY_LEFT:
                    event.preventDefault();
            }
        });

        canvas.addKeyUpHandler(event ->
        {
            int keyCode = getKeyCode(event.getNativeKeyCode());

            postKeyEvent(keyCode, false);

            switch (keyCode)
            {
                case Keyboard.KEY_UP:
                case Keyboard.KEY_DOWN:
                case Keyboard.KEY_RIGHT:
                case Keyboard.KEY_LEFT:
                    event.preventDefault();
            }
        });

        canvas.addMouseDownHandler(event ->
        {
            postMouseEvent(getMouseCode(event.getNativeButton()), true);
            canvas.setFocus(true);
            event.preventDefault();
        });

        canvas.addMouseUpHandler(event ->
        {
            postMouseEvent(getMouseCode(event.getNativeButton()), false);
            event.preventDefault();
        });

        canvas.addMouseMoveHandler(event ->
        {
            int x = event.getX();
            int y = event.getY();

            Mouse.dx = x - Mouse.x;
            Mouse.dy = y - Mouse.y;

            Mouse.x = x;
            Mouse.y = y;

            event.preventDefault();
        });

        canvas.addMouseWheelHandler(event ->
        {
            int dsy = event.getDeltaY();

            // To normalize between 0 and 1
            Mouse.deltaScrollY = dsy > 0 ? 1 : dsy == 0 ? 0 : -1;
            Mouse.deltaScrollX = 0;

            event.preventDefault();
        });

        canvas.addTouchStartHandler(event ->
        {
            postTouchEvents(event.getTargetTouches(), true);
            event.preventDefault();
        });

        canvas.addTouchMoveHandler(event ->
        {
            postTouchEvents(event.getTargetTouches(), true);
            event.preventDefault();
        });

        canvas.addTouchEndHandler(event ->
        {
            postTouchEvents(event.getTargetTouches(), false);
            event.preventDefault();
        });

        canvas.addTouchCancelHandler(event ->
        {
            postTouchEvents(event.getTargetTouches(), false);
            event.preventDefault();
        });

        preventContextMenu(canvas.getCanvasElement());

        registerControllerConnectionEvents(this, Controller.MAX_CONTROLLERS);
    }

    public static void pollControllers()
    {
        GwtInputDevice inputDevice = (GwtInputDevice) SilenceEngine.input;

        if (inputDevice.gamepadsExist)
            pollControllers(inputDevice);
    }

    private static native void pollControllers(GwtInputDevice gid) /*-{
        var gamepads = navigator.getGamepads();

        for (var i = 0; i < gamepads.length; i++)
        {
            var gamepad = gamepads[i];

            if (gamepad && gamepad.connected)
            {
                var id = gamepad.index;
                var j, amount;

                for (j = 0; j < gamepad.buttons.length; j++)
                {
                    var button = j;
                    var state = gamepad.buttons[j].pressed;
                    amount = gamepad.buttons[j].value;

                    if (button != -1)
                        gid.@com.shc.silenceengine.backend.gwt.GwtInputDevice::postControllerButtonEvent(*)(id, button, state, amount)
                }

                for (j = 0; j < gamepad.axes.length; j++)
                {
                    var axe = j;
                    amount = gamepad.axes[j];

                    if (axe != -1)
                        gid.@com.shc.silenceengine.backend.gwt.GwtInputDevice::postControllerAxisEvent(*)(id, axe, amount);
                }
            }
        }
    }-*/;

    private void createControllerEvent(int id, boolean connected, boolean ideal,
                                       String name, int numButtons, int numAxes)
    {
        ControllerConnectionEvent event = new ControllerConnectionEvent();

        event.buttonMapping = new Controller.Mapping();
        event.axisMapping = new Controller.Mapping();
        event.isControllerIdeal = ideal;
        event.controllerConnected = connected;
        event.controllerName = name;
        event.numButtons = numButtons;
        event.numAxes = numAxes;

        if (ideal)
        {
            // Controller mappings came from the HTML5 GamePad API
            // https://www.w3.org/TR/gamepad/#remapping

            event.buttonMapping.map(0, Controller.BUTTON_A);
            event.buttonMapping.map(1, Controller.BUTTON_B);
            event.buttonMapping.map(2, Controller.BUTTON_X);
            event.buttonMapping.map(3, Controller.BUTTON_Y);
            event.buttonMapping.map(4, Controller.BUTTON_L1);
            event.buttonMapping.map(5, Controller.BUTTON_R1);
            event.buttonMapping.map(6, Controller.BUTTON_L2);
            event.buttonMapping.map(7, Controller.BUTTON_R2);
            event.buttonMapping.map(8, Controller.BUTTON_SELECT);
            event.buttonMapping.map(9, Controller.BUTTON_START);
            event.buttonMapping.map(10, Controller.BUTTON_LEFT_STICK);
            event.buttonMapping.map(11, Controller.BUTTON_RIGHT_STICK);
            event.buttonMapping.map(12, Controller.BUTTON_DPAD_UP);
            event.buttonMapping.map(13, Controller.BUTTON_DPAD_DOWN);
            event.buttonMapping.map(14, Controller.BUTTON_DPAD_LEFT);
            event.buttonMapping.map(15, Controller.BUTTON_DPAD_RIGHT);

            event.axisMapping.map(0, Controller.AXIS_LEFT_X);
            event.axisMapping.map(1, Controller.AXIS_LEFT_Y);
            event.axisMapping.map(2, Controller.AXIS_RIGHT_X);
            event.axisMapping.map(3, Controller.AXIS_RIGHT_Y);
        }

        postControllerConnectionEvent(id, event);
    }

    private native void registerControllerConnectionEvents(GwtInputDevice gid, int maxGamePads) /*-{
        $wnd.addEventListener('gamepadconnected', function gamePadConnectEventHandler(event)
        {
            gid.@com.shc.silenceengine.backend.gwt.GwtInputDevice::gamepadsExist = true;

            var gamepad = event.gamepad;

            var id = gamepad.index;
            var name = gamepad.id;

            if (id >= maxGamePads)
                return;

            var ideal = gamepad.mapping == 'standard';

            gid.@com.shc.silenceengine.backend.gwt.GwtInputDevice::createControllerEvent(*)(id, true, ideal, name,
                gamepad.buttons.length, gamepad.axes.length);
        });

        $wnd.addEventListener('gamepaddisconnected', function gamePadDisconnectEventHandler(event)
        {
            var gamepad = event.gamepad;

            var id = gamepad.index;
            var name = 'Null Gamepad';

            if (id >= maxGamePads)
                return;

            gid.@com.shc.silenceengine.backend.gwt.GwtInputDevice::createControllerEvent(*)(id, false, false, name,
                0, 0);
        });
    }-*/;

    private native void preventContextMenu(CanvasElement canvas) /*-{
        canvas.oncontextmenu = function (){ return false; };
    }-*/;

    private void postTouchEvents(JsArray<Touch> touches, boolean isDown)
    {
        int i, j;

        // Browsers report only the available touches in a list. So update all the touches in the list accurately.
        // We ignore the touches past finger 9 (only ten fingers are read).
        for (i = 0, j = com.shc.silenceengine.input.Touch.FINGER_0;
             i < touches.length() && j <= com.shc.silenceengine.input.Touch.FINGER_9; i++, j++)
        {
            Touch touch = touches.get(i);
            postTouchEvent(j, isDown, touch.getClientX(), touch.getClientY());
        }

        // For all the remain fingers, set the finger down state to false. Otherwise they are reported as down
        // continuously. This will fix that issue.
        while (j <= com.shc.silenceengine.input.Touch.FINGER_9)
            postTouchEvent(j++, false, 0, 0);
    }

    @Override
    public void postKeyEvent(int keyCode, boolean isDown)
    {
        super.postKeyEvent(keyCode, isDown);

        Integer shortCircuitCode = keyShortCircuitMap.get(keyCode);

        if (shortCircuitCode != null)
            super.postKeyEvent(shortCircuitCode, isDown);
    }

    private int getKeyCode(int nativeKeyCode)
    {
        Integer code = keysMap.get(nativeKeyCode);
        return code == null ? 0 : code;
    }

    private int getMouseCode(int nativeButtonCode)
    {
        Integer code = mouseMap.get(nativeButtonCode);
        return code == null ? 0 : code;
    }

    private void createKeyMapping()
    {
        // Escape key
        keysMap.put(KeyCodes.KEY_ESCAPE, Keyboard.KEY_ESCAPE);

        // Function keys
        keysMap.put(KeyCodes.KEY_F1, Keyboard.KEY_F1);
        keysMap.put(KeyCodes.KEY_F2, Keyboard.KEY_F2);
        keysMap.put(KeyCodes.KEY_F3, Keyboard.KEY_F3);
        keysMap.put(KeyCodes.KEY_F4, Keyboard.KEY_F4);
        keysMap.put(KeyCodes.KEY_F5, Keyboard.KEY_F5);
        keysMap.put(KeyCodes.KEY_F6, Keyboard.KEY_F6);
        keysMap.put(KeyCodes.KEY_F7, Keyboard.KEY_F7);
        keysMap.put(KeyCodes.KEY_F8, Keyboard.KEY_F8);
        keysMap.put(KeyCodes.KEY_F9, Keyboard.KEY_F9);
        keysMap.put(KeyCodes.KEY_F10, Keyboard.KEY_F10);
        keysMap.put(KeyCodes.KEY_F11, Keyboard.KEY_F11);
        keysMap.put(KeyCodes.KEY_F12, Keyboard.KEY_F12);

        // Number keys
        keysMap.put(KeyCodes.KEY_ONE, Keyboard.KEY_1);
        keysMap.put(KeyCodes.KEY_TWO, Keyboard.KEY_2);
        keysMap.put(KeyCodes.KEY_THREE, Keyboard.KEY_3);
        keysMap.put(KeyCodes.KEY_FOUR, Keyboard.KEY_4);
        keysMap.put(KeyCodes.KEY_FIVE, Keyboard.KEY_5);
        keysMap.put(KeyCodes.KEY_SIX, Keyboard.KEY_6);
        keysMap.put(KeyCodes.KEY_SEVEN, Keyboard.KEY_7);
        keysMap.put(KeyCodes.KEY_EIGHT, Keyboard.KEY_8);
        keysMap.put(KeyCodes.KEY_NINE, Keyboard.KEY_9);
        keysMap.put(KeyCodes.KEY_ZERO, Keyboard.KEY_0);

        // Symbol keys (codes missing from KeyCodes class are found out by checking output in keydown handler).
        keysMap.put(172, Keyboard.KEY_TILDE);
        keysMap.put(189, Keyboard.KEY_MINUS);
        keysMap.put(187, Keyboard.KEY_PLUS);
        keysMap.put(219, Keyboard.KEY_LEFT_BRACKET);
        keysMap.put(221, Keyboard.KEY_RIGHT_BRACKET);
        keysMap.put(220, Keyboard.KEY_PIPE);
        keysMap.put(186, Keyboard.KEY_COLON);
        keysMap.put(222, Keyboard.KEY_SINGLE_QUOTE);
        keysMap.put(188, Keyboard.KEY_LESS_THAN);
        keysMap.put(190, Keyboard.KEY_GREATER_THAN);
        keysMap.put(191, Keyboard.KEY_QUESTION);

        // Lock keys
        keysMap.put(KeyCodes.KEY_CAPS_LOCK, Keyboard.KEY_CAPS_LOCK);
        keysMap.put(KeyCodes.KEY_NUMLOCK, Keyboard.KEY_NUM_LOCK);
        keysMap.put(KeyCodes.KEY_SCROLL_LOCK, Keyboard.KEY_SCROLL_LOCK);

        // Special keys
        keysMap.put(KeyCodes.KEY_TAB, Keyboard.KEY_TAB);
        keysMap.put(KeyCodes.KEY_BACKSPACE, Keyboard.KEY_BACKSPACE);
        keysMap.put(KeyCodes.KEY_ENTER, Keyboard.KEY_ENTER);
        keysMap.put(KeyCodes.KEY_SHIFT, Keyboard.KEY_LEFT_SHIFT);
        keysMap.put(KeyCodes.KEY_CTRL, Keyboard.KEY_LEFT_CTRL);
        keysMap.put(KeyCodes.KEY_ALT, Keyboard.KEY_LEFT_ALT);
        keysMap.put(91, Keyboard.KEY_LEFT_SUPER);
        keysMap.put(92, Keyboard.KEY_RIGHT_SUPER);
        keysMap.put(KeyCodes.KEY_INSERT, Keyboard.KEY_INSERT);
        keysMap.put(KeyCodes.KEY_DELETE, Keyboard.KEY_DELETE);
        keysMap.put(KeyCodes.KEY_HOME, Keyboard.KEY_HOME);
        keysMap.put(KeyCodes.KEY_END, Keyboard.KEY_END);
        keysMap.put(KeyCodes.KEY_PAGEUP, Keyboard.KEY_PAGEUP);
        keysMap.put(KeyCodes.KEY_PAGEDOWN, Keyboard.KEY_PAGEDOWN);
        keysMap.put(KeyCodes.KEY_PAUSE, Keyboard.KEY_PAUSE_BREAK);
        keysMap.put(KeyCodes.KEY_PRINT_SCREEN, Keyboard.KEY_PRINT_SCREEN);

        // Keypad keys
        keysMap.put(KeyCodes.KEY_NUM_ZERO, Keyboard.KEY_KP_0);
        keysMap.put(KeyCodes.KEY_NUM_ONE, Keyboard.KEY_KP_1);
        keysMap.put(KeyCodes.KEY_NUM_TWO, Keyboard.KEY_KP_2);
        keysMap.put(KeyCodes.KEY_NUM_THREE, Keyboard.KEY_KP_3);
        keysMap.put(KeyCodes.KEY_NUM_FOUR, Keyboard.KEY_KP_4);
        keysMap.put(KeyCodes.KEY_NUM_FIVE, Keyboard.KEY_KP_5);
        keysMap.put(KeyCodes.KEY_NUM_SIX, Keyboard.KEY_KP_6);
        keysMap.put(KeyCodes.KEY_NUM_SEVEN, Keyboard.KEY_KP_7);
        keysMap.put(KeyCodes.KEY_NUM_EIGHT, Keyboard.KEY_KP_8);
        keysMap.put(KeyCodes.KEY_NUM_NINE, Keyboard.KEY_KP_9);
        keysMap.put(KeyCodes.KEY_NUM_DIVISION, Keyboard.KEY_KP_SLASH);
        keysMap.put(KeyCodes.KEY_NUM_MULTIPLY, Keyboard.KEY_KP_ASTERISK);
        keysMap.put(KeyCodes.KEY_NUM_MINUS, Keyboard.KEY_KP_MINUS);
        keysMap.put(KeyCodes.KEY_NUM_PLUS, Keyboard.KEY_KP_PLUS);
        keysMap.put(KeyCodes.KEY_NUM_PERIOD, Keyboard.KEY_KP_PERIOD);

        // Arrow keys
        keysMap.put(KeyCodes.KEY_UP, Keyboard.KEY_UP);
        keysMap.put(KeyCodes.KEY_DOWN, Keyboard.KEY_DOWN);
        keysMap.put(KeyCodes.KEY_LEFT, Keyboard.KEY_LEFT);
        keysMap.put(KeyCodes.KEY_RIGHT, Keyboard.KEY_RIGHT);

        // Alphabet keys
        keysMap.put(KeyCodes.KEY_A, Keyboard.KEY_A);
        keysMap.put(KeyCodes.KEY_B, Keyboard.KEY_B);
        keysMap.put(KeyCodes.KEY_C, Keyboard.KEY_C);
        keysMap.put(KeyCodes.KEY_D, Keyboard.KEY_D);
        keysMap.put(KeyCodes.KEY_E, Keyboard.KEY_E);
        keysMap.put(KeyCodes.KEY_F, Keyboard.KEY_F);
        keysMap.put(KeyCodes.KEY_G, Keyboard.KEY_G);
        keysMap.put(KeyCodes.KEY_H, Keyboard.KEY_H);
        keysMap.put(KeyCodes.KEY_I, Keyboard.KEY_I);
        keysMap.put(KeyCodes.KEY_J, Keyboard.KEY_J);
        keysMap.put(KeyCodes.KEY_K, Keyboard.KEY_K);
        keysMap.put(KeyCodes.KEY_L, Keyboard.KEY_L);
        keysMap.put(KeyCodes.KEY_M, Keyboard.KEY_M);
        keysMap.put(KeyCodes.KEY_N, Keyboard.KEY_N);
        keysMap.put(KeyCodes.KEY_O, Keyboard.KEY_O);
        keysMap.put(KeyCodes.KEY_P, Keyboard.KEY_P);
        keysMap.put(KeyCodes.KEY_Q, Keyboard.KEY_Q);
        keysMap.put(KeyCodes.KEY_R, Keyboard.KEY_R);
        keysMap.put(KeyCodes.KEY_S, Keyboard.KEY_S);
        keysMap.put(KeyCodes.KEY_T, Keyboard.KEY_T);
        keysMap.put(KeyCodes.KEY_U, Keyboard.KEY_U);
        keysMap.put(KeyCodes.KEY_V, Keyboard.KEY_V);
        keysMap.put(KeyCodes.KEY_W, Keyboard.KEY_W);
        keysMap.put(KeyCodes.KEY_X, Keyboard.KEY_X);
        keysMap.put(KeyCodes.KEY_Y, Keyboard.KEY_Y);
        keysMap.put(KeyCodes.KEY_Z, Keyboard.KEY_Z);
        keysMap.put(KeyCodes.KEY_SPACE, Keyboard.KEY_SPACE);

        // Short circuit some keys
        keyShortCircuitMap.put(Keyboard.KEY_ENTER, Keyboard.KEY_KP_ENTER);
        keyShortCircuitMap.put(Keyboard.KEY_LEFT_SHIFT, Keyboard.KEY_RIGHT_SHIFT);
        keyShortCircuitMap.put(Keyboard.KEY_LEFT_CTRL, Keyboard.KEY_RIGHT_CTRL);
        keyShortCircuitMap.put(Keyboard.KEY_LEFT_ALT, Keyboard.KEY_RIGHT_ALT);
    }

    private void createMouseMapping()
    {
        mouseMap.put(1, Mouse.BUTTON_LEFT);
        mouseMap.put(2, Mouse.BUTTON_RIGHT);
        mouseMap.put(4, Mouse.BUTTON_MIDDLE);
    }
}
