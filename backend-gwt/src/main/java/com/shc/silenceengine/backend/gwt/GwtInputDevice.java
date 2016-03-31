package com.shc.silenceengine.backend.gwt;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.event.dom.client.KeyCodes;
import com.shc.silenceengine.core.SilenceEngine;
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
    private final Map<Integer, Integer> keysMap  = new HashMap<>();
    private final Map<Integer, Integer> mouseMap = new HashMap<>();

    private final Map<Integer, Integer> keyShortCircuitMap = new HashMap<>();

    public GwtInputDevice()
    {
        createKeyMapping();
        createMouseMapping();

        Canvas canvas = ((GwtDisplayDevice) SilenceEngine.display).canvas;

        canvas.addKeyDownHandler(event -> postKeyEvent(getKeyCode(event.getNativeKeyCode()), true));
        canvas.addKeyUpHandler(event -> postKeyEvent(getKeyCode(event.getNativeKeyCode()), false));

        canvas.addMouseDownHandler(event -> postMouseEvent(getMouseCode(event.getNativeButton()), true));
        canvas.addMouseUpHandler(event -> postMouseEvent(getMouseCode(event.getNativeButton()), false));

        canvas.addMouseMoveHandler(event -> {
            int x = event.getClientX();
            int y = event.getClientY();

            Mouse.dx = x - Mouse.x;
            Mouse.dy = y - Mouse.y;

            Mouse.x = x;
            Mouse.y = y;
        });

        canvas.addMouseWheelHandler(event -> {
            int dsy = event.getDeltaY();

            // To normalize between 0 and 1
            Mouse.deltaScrollY = dsy > 0 ? 1 : dsy == 0 ? 0 : -1;
            Mouse.deltaScrollX = 0;
        });
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
        mouseMap.put(3, Mouse.BUTTON_MIDDLE);
    }
}
