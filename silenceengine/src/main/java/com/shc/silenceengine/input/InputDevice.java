package com.shc.silenceengine.input;

import com.shc.silenceengine.core.SilenceEngine;

import static com.shc.silenceengine.input.Mouse.*;
import static com.shc.silenceengine.input.Touch.*;

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

        SilenceEngine.eventManager.addUpdateHandler(this::update);
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

    private void update(float deltaTime)
    {
        Keyboard.update();
        Mouse.update();
        Touch.update();

        if (simulateTouch)
        {
            int finger = FINGER_0;

            for (int button = BUTTON_FIRST; button <= BUTTON_LAST; button++)
            {
                // Safeguard against max fingers
                if (finger == NUM_FINGERS)
                    break;

                if (isButtonDown(button))
                    // Mouse button down, post an event and move to next finger
                    postTouchEvent(finger++, true, Mouse.x, Mouse.y);
                else
                    // Post event making finger up, and keep the current finger test
                    // for next button.
                    postTouchEvent(finger, false, Mouse.x, Mouse.y);
            }
        }
    }

    public void setSimulateTouch(boolean simulation)
    {
        this.simulateTouch = simulation;
    }
}
