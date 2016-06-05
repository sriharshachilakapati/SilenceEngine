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
import com.shc.silenceengine.events.IKeyEventHandler;
import com.shc.silenceengine.events.IMouseEventHandler;
import com.shc.silenceengine.events.ITouchEventHandler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static com.shc.silenceengine.input.Mouse.*;
import static com.shc.silenceengine.input.Touch.*;

/**
 * @author Sri Harsha Chilakapati
 */
public abstract class InputDevice
{
    private boolean simulateTouch;

    private List<IKeyEventHandler>   keyEventHandlers;
    private List<IMouseEventHandler> mouseEventHandlers;
    private List<ITouchEventHandler> touchEventHandlers;

    private Queue<Object> eventHandlerAddQueue;
    private Queue<Object> eventHandlerRemQueue;

    public InputDevice()
    {
        simulateTouch = false;

        keyEventHandlers = new ArrayList<>();
        mouseEventHandlers = new ArrayList<>();
        touchEventHandlers = new ArrayList<>();

        eventHandlerAddQueue = new LinkedList<>();
        eventHandlerRemQueue = new LinkedList<>();

        Keyboard.init();
        Mouse.init();
        Touch.init();

        SilenceEngine.eventManager.addUpdateHandler(this::update);
    }

    public void addKeyEventHandler(IKeyEventHandler handler)
    {
        eventHandlerAddQueue.add(handler);
    }

    public void addMouseEventHandler(IMouseEventHandler handler)
    {
        eventHandlerAddQueue.add(handler);
    }

    public void addTouchEventHandler(ITouchEventHandler handler)
    {
        eventHandlerAddQueue.add(handler);
    }

    public void removeKeyEventHandler(IKeyEventHandler handler)
    {
        eventHandlerRemQueue.add(handler);
    }

    public void removeMouseEventHandler(IMouseEventHandler handler)
    {
        eventHandlerRemQueue.add(handler);
    }

    public void removeTouchEventHandler(ITouchEventHandler handler)
    {
        eventHandlerRemQueue.add(handler);
    }

    public void postKeyEvent(int key, boolean isDown)
    {
        Keyboard.eventKeyStates[key] = isDown;
        processEventHandlerQueues();

        for (IKeyEventHandler keyEventHandler : keyEventHandlers)
            keyEventHandler.onKeyEvent(key, isDown);
    }

    public void postMouseEvent(int button, boolean isDown)
    {
        Mouse.eventButtonStates[button] = isDown;
        processEventHandlerQueues();

        for (IMouseEventHandler mouseEventHandler : mouseEventHandlers)
            mouseEventHandler.onMouseEvent(button, isDown, Mouse.x, Mouse.y);
    }

    public void postTouchEvent(int finger, boolean isDown, float xPos, float yPos)
    {
        Touch.eventStates[finger] = isDown;
        Touch.eventPositions[finger].set(xPos, yPos);

        processEventHandlerQueues();

        for (ITouchEventHandler touchEventHandler : touchEventHandlers)
            touchEventHandler.onTouchEvent(finger, isDown, xPos, yPos);
    }

    private void processEventHandlerQueues()
    {
        Object handler;

        while ((handler = eventHandlerAddQueue.poll()) != null)
        {
            if (handler instanceof IKeyEventHandler)
                keyEventHandlers.add((IKeyEventHandler) handler);

            else if (handler instanceof IMouseEventHandler)
                mouseEventHandlers.add((IMouseEventHandler) handler);

            else if (handler instanceof ITouchEventHandler)
                touchEventHandlers.add((ITouchEventHandler) handler);
        }

        while ((handler = eventHandlerRemQueue.poll()) != null)
        {
            if (handler instanceof IKeyEventHandler)
                keyEventHandlers.remove(handler);

            else if (handler instanceof IMouseEventHandler)
                mouseEventHandlers.remove(handler);

            else if (handler instanceof ITouchEventHandler)
                touchEventHandlers.remove(handler);
        }
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
