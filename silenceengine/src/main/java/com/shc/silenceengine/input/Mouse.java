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

    private static ButtonState[] buttonStates = new ButtonState[NUM_BUTTONS];

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
            buttonStates[i] = ButtonState.RELEASED;
    }

    static void update()
    {
        for (int i = BUTTON_FIRST; i <= BUTTON_LAST; i++)
        {
            if (buttonStates[i] == ButtonState.PRESSED)
                // If the current button state is pressed, change it to waiting for release
                buttonStates[i] = ButtonState.WAITING_FOR_RELEASE;

            if (buttonStates[i] == ButtonState.RELEASED)
                // Set the button state to pressed or released depending on whether it's down or not
                buttonStates[i] = eventButtonStates[i] ? ButtonState.PRESSED : ButtonState.RELEASED;
            else
                // Set the button state to released if the event state is false. Otherwise keep it intact.
                buttonStates[i] = eventButtonStates[i] ? buttonStates[i] : ButtonState.RELEASED;
        }
    }

    public static boolean isButtonDown(int button)
    {
        return buttonStates[button] != ButtonState.RELEASED;
    }

    public static boolean isButtonUp(int button)
    {
        return buttonStates[button] == ButtonState.RELEASED;
    }

    public static boolean isButtonTapped(int button)
    {
        return buttonStates[button] == ButtonState.PRESSED;
    }

    public static ButtonState getButtonState(int button)
    {
        return buttonStates[button];
    }
}
