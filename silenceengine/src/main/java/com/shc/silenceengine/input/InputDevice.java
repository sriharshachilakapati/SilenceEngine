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
