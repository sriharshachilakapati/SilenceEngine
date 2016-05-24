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

package com.shc.silenceengine.backend.android;

import android.opengl.GLSurfaceView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.input.InputDevice;

import java.util.HashMap;
import java.util.Map;

import static android.view.KeyEvent.*;
import static com.shc.silenceengine.input.Keyboard.*;
import static com.shc.silenceengine.input.Touch.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class AndroidInputDevice extends InputDevice
{
    private static Map<Integer, Integer> keyMap;

    private GLSurfaceView surfaceView;

    public AndroidInputDevice()
    {
        surfaceView = ((AndroidDisplayDevice) SilenceEngine.display).surfaceView;
        surfaceView.setOnKeyListener(this::onKey);
        surfaceView.setOnTouchListener(this::onTouch);

        prepareKeyMap();
    }

    private void prepareKeyMap()
    {
        keyMap = new HashMap<>();

        // Escape key
        keyMap.put(KEYCODE_ESCAPE, KEY_ESCAPE);

        // Function keys
        keyMap.put(KEYCODE_F1, KEY_F1);
        keyMap.put(KEYCODE_F2, KEY_F2);
        keyMap.put(KEYCODE_F3, KEY_F3);
        keyMap.put(KEYCODE_F4, KEY_F4);
        keyMap.put(KEYCODE_F5, KEY_F5);
        keyMap.put(KEYCODE_F6, KEY_F6);
        keyMap.put(KEYCODE_F7, KEY_F7);
        keyMap.put(KEYCODE_F8, KEY_F8);
        keyMap.put(KEYCODE_F9, KEY_F9);
        keyMap.put(KEYCODE_F10, KEY_F10);
        keyMap.put(KEYCODE_F11, KEY_F11);
        keyMap.put(KEYCODE_F12, KEY_F12);

        // Number keys
        keyMap.put(KEYCODE_0, KEY_0);
        keyMap.put(KEYCODE_1, KEY_1);
        keyMap.put(KEYCODE_2, KEY_2);
        keyMap.put(KEYCODE_3, KEY_3);
        keyMap.put(KEYCODE_4, KEY_4);
        keyMap.put(KEYCODE_5, KEY_5);
        keyMap.put(KEYCODE_6, KEY_6);
        keyMap.put(KEYCODE_7, KEY_7);
        keyMap.put(KEYCODE_8, KEY_8);
        keyMap.put(KEYCODE_9, KEY_9);

        // Symbol keys
        keyMap.put(KEYCODE_MINUS, KEY_UNDERSCORE);
        keyMap.put(KEYCODE_EQUALS, KEY_EQUALS);
        keyMap.put(KEYCODE_LEFT_BRACKET, KEY_LEFT_BRACE);
        keyMap.put(KEYCODE_RIGHT_BRACKET, KEY_RIGHT_BRACE);
        keyMap.put(KEYCODE_BACKSLASH, KEY_BACKWARD_SLASH);
        keyMap.put(KEYCODE_SEMICOLON, KEY_SEMICOLON);
        keyMap.put(KEYCODE_APOSTROPHE, KEY_DOUBLE_QUOTE);
        keyMap.put(KEYCODE_COMMA, KEY_COMMA);
        keyMap.put(KEYCODE_PERIOD, KEY_PERIOD);
        keyMap.put(KEYCODE_SLASH, KEY_FORWARD_SLASH);

        // Lock keys
        keyMap.put(KEYCODE_NUM_LOCK, KEY_NUM_LOCK);
        keyMap.put(KEYCODE_CAPS_LOCK, KEY_CAPS_LOCK);
        keyMap.put(KEYCODE_SCROLL_LOCK, KEY_SCROLL_LOCK);

        // Special keys
        keyMap.put(KEYCODE_TAB, KEY_TAB);
        keyMap.put(KEYCODE_BACK, KEY_BACKSPACE);
        keyMap.put(KEYCODE_ENTER, KEY_ENTER);
        keyMap.put(KEYCODE_SHIFT_LEFT, KEY_LEFT_SHIFT);
        keyMap.put(KEYCODE_SHIFT_RIGHT, KEY_RIGHT_SHIFT);
        keyMap.put(KEYCODE_CTRL_LEFT, KEY_LEFT_CTRL);
        keyMap.put(KEYCODE_CTRL_RIGHT, KEY_RIGHT_CTRL);
        keyMap.put(KEYCODE_ALT_LEFT, KEY_LEFT_ALT);
        keyMap.put(KEYCODE_ALT_RIGHT, KEY_RIGHT_ALT);
        keyMap.put(KEYCODE_INSERT, KEY_INSERT);
        keyMap.put(KEYCODE_DEL, KEY_DELETE);
        keyMap.put(KEYCODE_HOME, KEY_HOME);
        keyMap.put(KEYCODE_PAGE_DOWN, KEY_PAGEDOWN);
        keyMap.put(KEYCODE_PAGE_UP, KEY_PAGEUP);

        // Keypad keys
        keyMap.put(KEYCODE_NUMPAD_0, KEY_KP_0);
        keyMap.put(KEYCODE_NUMPAD_1, KEY_KP_1);
        keyMap.put(KEYCODE_NUMPAD_2, KEY_KP_2);
        keyMap.put(KEYCODE_NUMPAD_3, KEY_KP_3);
        keyMap.put(KEYCODE_NUMPAD_4, KEY_KP_4);
        keyMap.put(KEYCODE_NUMPAD_5, KEY_KP_5);
        keyMap.put(KEYCODE_NUMPAD_6, KEY_KP_6);
        keyMap.put(KEYCODE_NUMPAD_7, KEY_KP_7);
        keyMap.put(KEYCODE_NUMPAD_8, KEY_KP_8);
        keyMap.put(KEYCODE_NUMPAD_9, KEY_KP_9);
        keyMap.put(KEYCODE_NUMPAD_DIVIDE, KEY_KP_SLASH);
        keyMap.put(KEYCODE_NUMPAD_MULTIPLY, KEY_KP_ASTERISK);
        keyMap.put(KEYCODE_NUMPAD_SUBTRACT, KEY_KP_MINUS);
        keyMap.put(KEYCODE_NUMPAD_ADD, KEY_KP_PLUS);
        keyMap.put(KEYCODE_NUMPAD_ENTER, KEY_KP_ENTER);
        keyMap.put(KEYCODE_NUMPAD_DOT, KEY_KP_PERIOD);

        // Arrow keys
        keyMap.put(KEYCODE_DPAD_UP, KEY_UP);
        keyMap.put(KEYCODE_DPAD_DOWN, KEY_DOWN);
        keyMap.put(KEYCODE_DPAD_LEFT, KEY_LEFT);
        keyMap.put(KEYCODE_DPAD_RIGHT, KEY_RIGHT);

        // Alphabet keys
        keyMap.put(KEYCODE_A, KEY_A);
        keyMap.put(KEYCODE_B, KEY_B);
        keyMap.put(KEYCODE_C, KEY_C);
        keyMap.put(KEYCODE_D, KEY_D);
        keyMap.put(KEYCODE_E, KEY_E);
        keyMap.put(KEYCODE_F, KEY_F);
        keyMap.put(KEYCODE_G, KEY_G);
        keyMap.put(KEYCODE_H, KEY_H);
        keyMap.put(KEYCODE_I, KEY_I);
        keyMap.put(KEYCODE_J, KEY_J);
        keyMap.put(KEYCODE_K, KEY_K);
        keyMap.put(KEYCODE_L, KEY_L);
        keyMap.put(KEYCODE_M, KEY_M);
        keyMap.put(KEYCODE_N, KEY_N);
        keyMap.put(KEYCODE_O, KEY_O);
        keyMap.put(KEYCODE_P, KEY_P);
        keyMap.put(KEYCODE_Q, KEY_Q);
        keyMap.put(KEYCODE_R, KEY_R);
        keyMap.put(KEYCODE_S, KEY_S);
        keyMap.put(KEYCODE_T, KEY_T);
        keyMap.put(KEYCODE_U, KEY_U);
        keyMap.put(KEYCODE_V, KEY_V);
        keyMap.put(KEYCODE_W, KEY_W);
        keyMap.put(KEYCODE_X, KEY_X);
        keyMap.put(KEYCODE_Y, KEY_Y);
        keyMap.put(KEYCODE_Z, KEY_Z);
        keyMap.put(KEYCODE_SPACE, KEY_SPACE);
    }

    private int translateKeyCode(int nativeCode)
    {
        Integer code = keyMap.get(nativeCode);
        return (code == null) ? 0 : code;
    }

    private boolean onKey(View v, int keyCode, KeyEvent event)
    {
        surfaceView.queueEvent(() -> postKeyEvent(translateKeyCode(keyCode), event.getAction() == ACTION_DOWN));
        return true;
    }

    private boolean onTouch(View v, MotionEvent e)
    {
        final int action = e.getActionMasked();

        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
                surfaceView.queueEvent(() -> postTouchEvent(FINGER_0, true, e.getX(), e.getY()));
                break;

            case MotionEvent.ACTION_UP:
                surfaceView.queueEvent(() -> postTouchEvent(FINGER_0, false, e.getX(), e.getY()));
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
            {
                final int index = e.getActionIndex();
                final int finger = index + 1;

                if (finger < FINGER_1 || finger > FINGER_9)
                    break;

                final boolean isDown = action == MotionEvent.ACTION_POINTER_DOWN;
                surfaceView.queueEvent(() -> postTouchEvent(finger, isDown, e.getX(), e.getY()));
            }
            break;

            case MotionEvent.ACTION_MOVE:
                for (int i = 0; i < e.getPointerCount(); i++)
                {
                    final int finger = i + 1;

                    if (finger < FINGER_1 || finger > FINGER_9)
                        break;

                    surfaceView.queueEvent(() ->
                            postTouchEvent(finger, true, e.getX(finger - 1), e.getY(finger - 1)));
                }
                for (int i = e.getPointerCount(); i < FINGER_9; i++)
                {
                    final int finger = i + 1;
                    surfaceView.queueEvent(() -> postTouchEvent(finger, false, 0, 0));
                }
                break;
        }

        return true;
    }
}
