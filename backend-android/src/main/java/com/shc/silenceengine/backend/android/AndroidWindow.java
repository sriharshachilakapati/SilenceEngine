/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 Sri Harsha Chilakapati
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
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.utils.TaskManager;
import com.shc.silenceengine.utils.functional.SimpleCallback;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author Sri Harsha Chilakapati
 */
public class AndroidWindow implements GLSurfaceView.Renderer
{
    public SimpleCallback loopFrame;
    public SimpleCallback resized;

    private SimpleCallback startCallback;

    public AndroidWindow(SimpleCallback startCallback)
    {
        this.startCallback = startCallback;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config)
    {
        loopFrame = () ->
        {
            // Assume 100 seconds so that tasks are force flushed
            TaskManager.forceUpdateTasks(100);
            TaskManager.forceRenderTasks(100);
        };

        resized = () ->
        {
        };

        SilenceEngine.eventManager.clearAllHandlers();
        SilenceEngine.gameLoop.onFocusLost();
        startCallback.invoke();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height)
    {
        resized.invoke();
    }

    @Override
    public void onDrawFrame(GL10 gl)
    {
        loopFrame.invoke();
    }
}
