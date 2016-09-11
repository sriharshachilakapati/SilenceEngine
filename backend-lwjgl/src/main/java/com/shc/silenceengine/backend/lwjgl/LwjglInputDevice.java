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

package com.shc.silenceengine.backend.lwjgl;

import com.shc.silenceengine.backend.lwjgl.glfw.GLFW3;
import com.shc.silenceengine.backend.lwjgl.glfw.Window;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.events.ControllerConnectionEvent;
import com.shc.silenceengine.input.Controller;
import com.shc.silenceengine.input.InputDevice;
import com.shc.silenceengine.input.Mouse;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static com.shc.silenceengine.input.Keyboard.*;
import static com.shc.silenceengine.input.Mouse.*;
import static org.lwjgl.glfw.GLFW.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class LwjglInputDevice extends InputDevice
{
    private final Map<Integer, Integer> keyMap   = new HashMap<>();
    private final Map<Integer, Integer> mouseMap = new HashMap<>();

    public LwjglInputDevice()
    {
        Window window = ((LwjglDisplayDevice) SilenceEngine.display).window;

        window.setKeyCallback((window1, key, scanCode, action, mods) ->
                postKeyEvent(translateKeyCode(key), action != GLFW_RELEASE));

        window.setMouseButtonCallback((window1, button, action, mods) ->
                postMouseEvent(translateMouseCode(button), action != GLFW_RELEASE));

        window.setCursorPositionCallback((window1, xPos, yPos) ->
        {
            Mouse.dx = (int) (xPos - Mouse.x);
            Mouse.dy = (int) (yPos - Mouse.y);

            Mouse.x = (int) xPos;
            Mouse.y = (int) yPos;
        });

        window.setScrollCallback((window1, xOffset, yOffset) ->
        {
            int dsx = (int) xOffset;
            int dsy = (int) yOffset;

            // To normalize between 0 and 1
            Mouse.deltaScrollX = dsx > 0 ? 1 : dsx == 0 ? 0 : -1;
            Mouse.deltaScrollY = dsy > 0 ? 1 : dsy == 0 ? 0 : -1;
        });

        window.setCharacterModsCallback((window1, codePoint, mods) ->
                postTextEvent(Character.toChars(codePoint)));

        GLFW3.setJoystickCallback((joystick, connected) ->
        {
            ControllerConnectionEvent event = new ControllerConnectionEvent();
            event.controllerConnected = connected;
            event.isControllerIdeal = false;
            event.controllerName = glfwGetJoystickName(joystick);

            event.axisMapping = new Controller.Mapping();
            event.buttonMapping = new Controller.Mapping();

            event.numButtons = glfwGetJoystickButtons(joystick).capacity();
            event.numAxes = glfwGetJoystickAxes(joystick).capacity();

            postControllerConnectionEvent(joystick, event);
        });

        createKeyMap();
        createMouseMap();
    }

    static void pollControllers()
    {
        for (int i = GLFW_JOYSTICK_1; i <= GLFW_JOYSTICK_LAST; i++)
        {
            if (glfwJoystickPresent(i))
            {
                ByteBuffer buttons = glfwGetJoystickButtons(i);

                while (buttons.hasRemaining())
                    SilenceEngine.input.postControllerButtonEvent(i, buttons.position(), buttons.get() == 1);

                FloatBuffer axes = glfwGetJoystickAxes(i);

                while (axes.hasRemaining())
                    SilenceEngine.input.postControllerAxisEvent(i, axes.position(), axes.get());
            }
        }
    }

    private void createMouseMap()
    {
        mouseMap.put(GLFW_MOUSE_BUTTON_1, BUTTON_1);
        mouseMap.put(GLFW_MOUSE_BUTTON_2, BUTTON_2);
        mouseMap.put(GLFW_MOUSE_BUTTON_3, BUTTON_3);
        mouseMap.put(GLFW_MOUSE_BUTTON_4, BUTTON_4);
        mouseMap.put(GLFW_MOUSE_BUTTON_5, BUTTON_5);
        mouseMap.put(GLFW_MOUSE_BUTTON_6, BUTTON_6);
        mouseMap.put(GLFW_MOUSE_BUTTON_7, BUTTON_7);
        mouseMap.put(GLFW_MOUSE_BUTTON_8, BUTTON_8);
    }

    private void createKeyMap()
    {
        // Escape key
        keyMap.put(GLFW_KEY_ESCAPE, KEY_ESCAPE);

        // Function keys
        keyMap.put(GLFW_KEY_F1, KEY_F1);
        keyMap.put(GLFW_KEY_F2, KEY_F2);
        keyMap.put(GLFW_KEY_F3, KEY_F3);
        keyMap.put(GLFW_KEY_F4, KEY_F4);
        keyMap.put(GLFW_KEY_F5, KEY_F5);
        keyMap.put(GLFW_KEY_F6, KEY_F6);
        keyMap.put(GLFW_KEY_F7, KEY_F7);
        keyMap.put(GLFW_KEY_F8, KEY_F8);
        keyMap.put(GLFW_KEY_F9, KEY_F9);
        keyMap.put(GLFW_KEY_F10, KEY_F10);
        keyMap.put(GLFW_KEY_F11, KEY_F11);
        keyMap.put(GLFW_KEY_F12, KEY_F12);

        // Number keys
        keyMap.put(GLFW_KEY_0, KEY_0);
        keyMap.put(GLFW_KEY_1, KEY_1);
        keyMap.put(GLFW_KEY_2, KEY_2);
        keyMap.put(GLFW_KEY_3, KEY_3);
        keyMap.put(GLFW_KEY_4, KEY_4);
        keyMap.put(GLFW_KEY_5, KEY_5);
        keyMap.put(GLFW_KEY_6, KEY_6);
        keyMap.put(GLFW_KEY_7, KEY_7);
        keyMap.put(GLFW_KEY_8, KEY_8);
        keyMap.put(GLFW_KEY_9, KEY_9);

        // Symbol keys
        keyMap.put(GLFW_KEY_GRAVE_ACCENT, KEY_BACKTICK);
        keyMap.put(GLFW_KEY_MINUS, KEY_UNDERSCORE);
        keyMap.put(GLFW_KEY_EQUAL, KEY_EQUALS);
        keyMap.put(GLFW_KEY_LEFT_BRACKET, KEY_LEFT_BRACE);
        keyMap.put(GLFW_KEY_RIGHT_BRACKET, KEY_RIGHT_BRACE);
        keyMap.put(GLFW_KEY_BACKSLASH, KEY_BACKWARD_SLASH);
        keyMap.put(GLFW_KEY_SEMICOLON, KEY_SEMICOLON);
        keyMap.put(GLFW_KEY_APOSTROPHE, KEY_DOUBLE_QUOTE);
        keyMap.put(GLFW_KEY_COMMA, KEY_COMMA);
        keyMap.put(GLFW_KEY_PERIOD, KEY_PERIOD);
        keyMap.put(GLFW_KEY_SLASH, KEY_FORWARD_SLASH);

        // Lock keys
        keyMap.put(GLFW_KEY_NUM_LOCK, KEY_NUM_LOCK);
        keyMap.put(GLFW_KEY_CAPS_LOCK, KEY_CAPS_LOCK);
        keyMap.put(GLFW_KEY_SCROLL_LOCK, KEY_SCROLL_LOCK);

        // Special keys
        keyMap.put(GLFW_KEY_TAB, KEY_TAB);
        keyMap.put(GLFW_KEY_BACKSPACE, KEY_BACKSPACE);
        keyMap.put(GLFW_KEY_ENTER, KEY_ENTER);
        keyMap.put(GLFW_KEY_LEFT_SHIFT, KEY_LEFT_SHIFT);
        keyMap.put(GLFW_KEY_RIGHT_SHIFT, KEY_RIGHT_SHIFT);
        keyMap.put(GLFW_KEY_LEFT_CONTROL, KEY_LEFT_CTRL);
        keyMap.put(GLFW_KEY_RIGHT_CONTROL, KEY_RIGHT_CTRL);
        keyMap.put(GLFW_KEY_LEFT_ALT, KEY_LEFT_ALT);
        keyMap.put(GLFW_KEY_RIGHT_ALT, KEY_RIGHT_ALT);
        keyMap.put(GLFW_KEY_LEFT_SUPER, KEY_LEFT_SUPER);
        keyMap.put(GLFW_KEY_RIGHT_SUPER, KEY_RIGHT_SUPER);
        keyMap.put(GLFW_KEY_INSERT, KEY_INSERT);
        keyMap.put(GLFW_KEY_DELETE, KEY_DELETE);
        keyMap.put(GLFW_KEY_HOME, KEY_HOME);
        keyMap.put(GLFW_KEY_PAGE_DOWN, KEY_PAGEDOWN);
        keyMap.put(GLFW_KEY_PAGE_UP, KEY_PAGEUP);
        keyMap.put(GLFW_KEY_PRINT_SCREEN, KEY_PRINT_SCREEN);
        keyMap.put(GLFW_KEY_PAUSE, KEY_PAUSE_BREAK);

        // Keypad keys
        keyMap.put(GLFW_KEY_KP_0, KEY_KP_0);
        keyMap.put(GLFW_KEY_KP_1, KEY_KP_1);
        keyMap.put(GLFW_KEY_KP_2, KEY_KP_2);
        keyMap.put(GLFW_KEY_KP_3, KEY_KP_3);
        keyMap.put(GLFW_KEY_KP_4, KEY_KP_4);
        keyMap.put(GLFW_KEY_KP_5, KEY_KP_5);
        keyMap.put(GLFW_KEY_KP_6, KEY_KP_6);
        keyMap.put(GLFW_KEY_KP_7, KEY_KP_7);
        keyMap.put(GLFW_KEY_KP_8, KEY_KP_8);
        keyMap.put(GLFW_KEY_KP_9, KEY_KP_9);
        keyMap.put(GLFW_KEY_KP_DIVIDE, KEY_KP_SLASH);
        keyMap.put(GLFW_KEY_KP_MULTIPLY, KEY_KP_ASTERISK);
        keyMap.put(GLFW_KEY_KP_SUBTRACT, KEY_KP_MINUS);
        keyMap.put(GLFW_KEY_KP_ADD, KEY_KP_PLUS);
        keyMap.put(GLFW_KEY_KP_ENTER, KEY_KP_ENTER);
        keyMap.put(GLFW_KEY_KP_DECIMAL, KEY_KP_PERIOD);

        // Arrow keys
        keyMap.put(GLFW_KEY_UP, KEY_UP);
        keyMap.put(GLFW_KEY_DOWN, KEY_DOWN);
        keyMap.put(GLFW_KEY_LEFT, KEY_LEFT);
        keyMap.put(GLFW_KEY_RIGHT, KEY_RIGHT);

        // Alphabet keys
        keyMap.put(GLFW_KEY_A, KEY_A);
        keyMap.put(GLFW_KEY_B, KEY_B);
        keyMap.put(GLFW_KEY_C, KEY_C);
        keyMap.put(GLFW_KEY_D, KEY_D);
        keyMap.put(GLFW_KEY_E, KEY_E);
        keyMap.put(GLFW_KEY_F, KEY_F);
        keyMap.put(GLFW_KEY_G, KEY_G);
        keyMap.put(GLFW_KEY_H, KEY_H);
        keyMap.put(GLFW_KEY_I, KEY_I);
        keyMap.put(GLFW_KEY_J, KEY_J);
        keyMap.put(GLFW_KEY_K, KEY_K);
        keyMap.put(GLFW_KEY_L, KEY_L);
        keyMap.put(GLFW_KEY_M, KEY_M);
        keyMap.put(GLFW_KEY_N, KEY_N);
        keyMap.put(GLFW_KEY_O, KEY_O);
        keyMap.put(GLFW_KEY_P, KEY_P);
        keyMap.put(GLFW_KEY_Q, KEY_Q);
        keyMap.put(GLFW_KEY_R, KEY_R);
        keyMap.put(GLFW_KEY_S, KEY_S);
        keyMap.put(GLFW_KEY_T, KEY_T);
        keyMap.put(GLFW_KEY_U, KEY_U);
        keyMap.put(GLFW_KEY_V, KEY_V);
        keyMap.put(GLFW_KEY_W, KEY_W);
        keyMap.put(GLFW_KEY_X, KEY_X);
        keyMap.put(GLFW_KEY_Y, KEY_Y);
        keyMap.put(GLFW_KEY_Z, KEY_Z);
        keyMap.put(GLFW_KEY_SPACE, KEY_SPACE);
    }

    private int translateKeyCode(int nativeCode)
    {
        Integer code = keyMap.get(nativeCode);

        return (code == null) ? 0 : code;
    }

    private int translateMouseCode(int nativeCode)
    {
        Integer code = mouseMap.get(nativeCode);

        return (code == null) ? 0 : code;
    }
}
