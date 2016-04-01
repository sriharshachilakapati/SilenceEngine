package com.shc.silenceengine.input;

/**
 * @author Sri Harsha Chilakapati
 */
public final class Keyboard
{
    /**
     * The ESCAPE key.
     */
    public static final int KEY_ESCAPE = 1;

    /**
     * The Function Keys.
     */
    public static final int
            KEY_F1  = 2,
            KEY_F2  = 3,
            KEY_F3  = 4,
            KEY_F4  = 5,
            KEY_F5  = 6,
            KEY_F6  = 7,
            KEY_F7  = 8,
            KEY_F8  = 9,
            KEY_F9  = 10,
            KEY_F10 = 11,
            KEY_F11 = 12,
            KEY_F12 = 13;

    /**
     * Number keys.
     */
    public static final int
            KEY_1 = 14,
            KEY_2 = 15,
            KEY_3 = 16,
            KEY_4 = 17,
            KEY_5 = 18,
            KEY_6 = 19,
            KEY_7 = 20,
            KEY_8 = 21,
            KEY_9 = 22,
            KEY_0 = 23;

    /**
     * The Symbol keys.
     */
    public static final int
            KEY_BACKTICK          = 24,
            KEY_TILDE             = KEY_BACKTICK,
            KEY_EXCLAMATION       = KEY_1,
            KEY_AT                = KEY_2,
            KEY_HASH              = KEY_3,
            KEY_CURRENCY          = KEY_4,
            KEY_PERCENT           = KEY_5,
            KEY_CAP               = KEY_6,
            KEY_AMPERSAND         = KEY_7,
            KEY_ASTERISK          = KEY_8,
            KEY_LEFT_PARANTHESIS  = KEY_9,
            KEY_RIGHT_PARANTHESIS = KEY_0,
            KEY_UNDERSCORE        = 25,
            KEY_MINUS             = KEY_UNDERSCORE,
            KEY_PLUS              = 26,
            KEY_EQUALS            = KEY_PLUS,
            KEY_LEFT_BRACKET      = 27,
            KEY_LEFT_BRACE        = KEY_LEFT_BRACKET,
            KEY_RIGHT_BRACKET     = 28,
            KEY_RIGHT_BRACE       = KEY_RIGHT_BRACKET,
            KEY_PIPE              = 29,
            KEY_BACKWARD_SLASH    = KEY_PIPE,
            KEY_COLON             = 30,
            KEY_SEMICOLON         = KEY_COLON,
            KEY_SINGLE_QUOTE      = 31,
            KEY_DOUBLE_QUOTE      = KEY_SINGLE_QUOTE,
            KEY_LESS_THAN         = 32,
            KEY_COMMA             = KEY_LESS_THAN,
            KEY_FULLSTOP          = 33,
            KEY_PERIOD            = KEY_FULLSTOP,
            KEY_GREATER_THAN      = KEY_FULLSTOP,
            KEY_QUESTION          = 34,
            KEY_FORWARD_SLASH     = KEY_QUESTION;

    /**
     * Lock keys.
     */
    public static final int
            KEY_CAPS_LOCK   = 35,
            KEY_SCROLL_LOCK = 36,
            KEY_NUM_LOCK    = 37;

    /**
     * Special keys.
     */
    public static final int
            KEY_TAB          = 38,
            KEY_BACKSPACE    = 39,
            KEY_ENTER        = 40,
            KEY_LEFT_SHIFT   = 41,
            KEY_RIGHT_SHIFT  = 42,
            KEY_LEFT_CTRL    = 43,
            KEY_RIGHT_CTRL   = 44,
            KEY_LEFT_ALT     = 45,
            KEY_RIGHT_ALT    = 46,
            KEY_LEFT_SUPER   = 47,
            KEY_RIGHT_SUPER  = 48,
            KEY_INSERT       = 49,
            KEY_DELETE       = 50,
            KEY_HOME         = 51,
            KEY_END          = 52,
            KEY_PAGEUP       = 53,
            KEY_PAGEDOWN     = 54,
            KEY_PRINT_SCREEN = 55,
            KEY_PAUSE_BREAK  = 56;

    /**
     * Keypad keys.
     */
    public static final int
            KEY_KP_0        = 57,
            KEY_KP_1        = 58,
            KEY_KP_2        = 59,
            KEY_KP_3        = 60,
            KEY_KP_4        = 61,
            KEY_KP_5        = 62,
            KEY_KP_6        = 63,
            KEY_KP_7        = 64,
            KEY_KP_8        = 65,
            KEY_KP_9        = 66,
            KEY_KP_SLASH    = 67,
            KEY_KP_ASTERISK = 68,
            KEY_KP_MINUS    = 69,
            KEY_KP_PLUS     = 70,
            KEY_KP_ENTER    = 71,
            KEY_KP_PERIOD   = 72;

    /**
     * Arrow keys.
     */
    public static final int
            KEY_UP    = 73,
            KEY_DOWN  = 74,
            KEY_LEFT  = 75,
            KEY_RIGHT = 76;

    /**
     * Alphabet keys.
     */
    public static final int
            KEY_A = 77,
            KEY_B = 78,
            KEY_C = 79,
            KEY_D = 80,
            KEY_E = 81,
            KEY_F = 82,
            KEY_G = 83,
            KEY_H = 84,
            KEY_I = 85,
            KEY_J = 86,
            KEY_K = 87,
            KEY_L = 88,
            KEY_M = 89,
            KEY_N = 90,
            KEY_O = 91,
            KEY_P = 92,
            KEY_Q = 93,
            KEY_R = 94,
            KEY_S = 95,
            KEY_T = 96,
            KEY_U = 97,
            KEY_V = 98,
            KEY_W = 99,
            KEY_X = 100,
            KEY_Y = 101,
            KEY_Z = 102;

    /**
     * Total number of keys.
     */
    public static final int NUM_KEYS  = 103;
    public static final int KEY_FIRST = 1;
    public static final int KEY_LAST  = NUM_KEYS - 1;

    private static ButtonState[] keyStates = new ButtonState[NUM_KEYS];

    static boolean[] eventKeyStates = new boolean[NUM_KEYS];

    /**
     * Prevent instantiation.
     */
    private Keyboard()
    {
    }

    static void init()
    {
        for (int i = KEY_FIRST; i <= KEY_LAST; i++)
            keyStates[i] = ButtonState.RELEASED;
    }

    static void update()
    {
        for (int i = KEY_FIRST; i <= KEY_LAST; i++)
        {
            if (keyStates[i] == ButtonState.PRESSED)
                // If the current key state is pressed, change it to waiting for release
                keyStates[i] = ButtonState.WAITING_FOR_RELEASE;

            if (keyStates[i] == ButtonState.RELEASED)
                // Set the key state to pressed or released depending on whether it's down or not
                keyStates[i] = eventKeyStates[i] ? ButtonState.PRESSED : ButtonState.RELEASED;
            else
                // Set the key state to released if the event state is false. Otherwise keep it intact.
                keyStates[i] = eventKeyStates[i] ? keyStates[i] : ButtonState.RELEASED;
        }
    }

    public static boolean isKeyDown(int key)
    {
        return keyStates[key] != ButtonState.RELEASED;
    }

    public static boolean isKeyUp(int key)
    {
        return keyStates[key] == ButtonState.RELEASED;
    }

    public static boolean isKeyTapped(int key)
    {
        return keyStates[key] == ButtonState.PRESSED;
    }

    public static ButtonState getKeyState(int key)
    {
        return keyStates[key];
    }
}
