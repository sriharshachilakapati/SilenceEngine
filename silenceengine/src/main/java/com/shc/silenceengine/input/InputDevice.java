package com.shc.silenceengine.input;

/**
 * @author Sri Harsha Chilakapati
 */
public abstract class InputDevice
{
    public InputDevice()
    {
        Keyboard.init();
        Mouse.init();
    }

    public void postKeyEvent(int key, boolean isDown)
    {
        Keyboard.eventKeyStates[key] = isDown;
    }

    public void postMouseEvent(int button, boolean isDown)
    {
        Mouse.eventButtonStates[button] = isDown;
    }

    public void update()
    {
        Keyboard.update();
        Mouse.update();
    }
}
