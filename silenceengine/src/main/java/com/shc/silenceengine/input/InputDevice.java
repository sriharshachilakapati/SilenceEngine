package com.shc.silenceengine.input;

/**
 * @author Sri Harsha Chilakapati
 */
public abstract class InputDevice
{
    private boolean simulateTouch;

    public InputDevice()
    {
        simulateTouch = false;

        Keyboard.init();
        Mouse.init();
        Touch.init();
    }

    public void postKeyEvent(int key, boolean isDown)
    {
        Keyboard.eventKeyStates[key] = isDown;
    }

    public void postMouseEvent(int button, boolean isDown)
    {
        Mouse.eventButtonStates[button] = isDown;
    }

    public void postTouchEvent(int finger, boolean isDown, int xPos, int yPos)
    {
        Touch.eventStates[finger] = isDown;
        Touch.eventPositions[finger].set(xPos, yPos);
    }

    public void update()
    {
        Keyboard.update();
        Mouse.update();
        Touch.update();

        if (simulateTouch)
        {
            postTouchEvent(Touch.FINGER_0, Mouse.isButtonDown(Mouse.BUTTON_LEFT), Mouse.x, Mouse.y);
            postTouchEvent(Touch.FINGER_1, Mouse.isButtonDown(Mouse.BUTTON_RIGHT), Mouse.x, Mouse.y);
            postTouchEvent(Touch.FINGER_2, Mouse.isButtonDown(Mouse.BUTTON_MIDDLE), Mouse.x, Mouse.y);
        }
    }

    public void setSimulateTouch(boolean simulation)
    {
        this.simulateTouch = simulation;
    }
}
