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

import static com.shc.silenceengine.input.Touch.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class AndroidInputDevice extends InputDevice
{
    private GLSurfaceView surfaceView;

    public AndroidInputDevice()
    {
        surfaceView = ((AndroidDisplayDevice) SilenceEngine.display).surfaceView;
        surfaceView.setOnKeyListener(this::onKey);
        surfaceView.setOnTouchListener(this::onTouch);
    }

    private boolean onKey(View v, int keyCode, KeyEvent event)
    {
        // TODO: Create a key mapping and use it

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
