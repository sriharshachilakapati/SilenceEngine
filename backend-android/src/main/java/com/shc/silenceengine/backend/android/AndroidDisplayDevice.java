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

import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.os.SystemClock;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.io.FilePath;

/**
 * @author Sri Harsha Chilakapati
 */
public class AndroidDisplayDevice implements com.shc.silenceengine.core.IDisplayDevice
{
    public GLSurfaceView   surfaceView;
    public AndroidLauncher activity;

    private double startTime;

    public AndroidDisplayDevice()
    {
        this.startTime = SystemClock.elapsedRealtimeNanos();
        this.activity = AndroidLauncher.instance;
        this.surfaceView = activity.surfaceView;
    }

    @Override
    public SilenceEngine.Platform getPlatform()
    {
        return SilenceEngine.Platform.ANDROID;
    }

    @Override
    public void setSize(int width, int height)
    {
        if (width > height)
            activity.runOnUiThread(() ->
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE));
        else
            activity.runOnUiThread(() ->
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT));
    }

    @Override
    public boolean isFullscreen()
    {
        return true;
    }

    @Override
    public void setFullscreen(boolean fullscreen)
    {
    }

    @Override
    public void centerOnScreen()
    {
    }

    @Override
    public void setPosition(int x, int y)
    {
    }

    @Override
    public int getWidth()
    {
        Point point = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(point);
        return point.x;
    }

    @Override
    public int getHeight()
    {
        Point point = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(point);
        return point.y;
    }

    @Override
    public String getTitle()
    {
        return "";
    }

    @Override
    public void setTitle(String title)
    {
    }

    @Override
    public void setIcon(FilePath filePath)
    {
    }

    @Override
    public void close()
    {
    }

    @Override
    public double nanoTime()
    {
        return SystemClock.elapsedRealtimeNanos() - startTime;
    }

    @Override
    public void setVSync(boolean vSync)
    {
    }

    @Override
    public boolean hasFocus()
    {
        return true;
    }
}
