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
        surfaceView.setOnGenericMotionListener(this::onTouch);
    }

    private boolean onKey(View v, int keyCode, KeyEvent event)
    {
        // TODO: Create a key mapping and use it

        return true;
    }

    private boolean onTouch(View v, MotionEvent e)
    {
        int index = e.getActionIndex() + 1; // Indices start with one on silenceengine

        float x = e.getX();
        float y = e.getY();

        boolean isDown = e.getAction() == MotionEvent.ACTION_DOWN ||
                         e.getAction() == MotionEvent.ACTION_POINTER_DOWN ||
                         e.getAction() == MotionEvent.ACTION_MOVE;

        // TODO: Fix this for multiple touches
        surfaceView.queueEvent(() -> postTouchEvent(index, isDown, x, y));

        return false;
    }
}
