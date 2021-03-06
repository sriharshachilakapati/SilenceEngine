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

import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.SystemClock;
import com.shc.silenceengine.core.IDisplayDevice;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.utils.functional.Promise;
import com.shc.silenceengine.utils.functional.SimpleCallback;
import com.shc.silenceengine.utils.functional.UniCallback;

/**
 * @author Sri Harsha Chilakapati
 */
class AndroidDisplayDevice implements IDisplayDevice
{
    private AndroidLauncher activity;

    private double startTime;

    AndroidDisplayDevice()
    {
        this.startTime = SystemClock.elapsedRealtimeNanos();
        this.activity = AndroidLauncher.instance;

        setSize(800, 600);
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
    public Promise<Void> setIcon(FilePath filePath)
    {
        return new Promise<>((resolve, reject) -> resolve.invoke(null));
    }

    @Override
    public void setIcon(FilePath filePath, SimpleCallback success, UniCallback<Throwable> error)
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

    @Override
    public void setGrabMouse(boolean grabMouse)
    {
    }

    @Override
    public String prompt(String message, String defaultValue)
    {
        return BlockingDialogs.prompt(message, defaultValue, activity);
    }

    @Override
    public String prompt(String message)
    {
        return prompt(message, "");
    }

    @Override
    public boolean confirm(String message)
    {
        return BlockingDialogs.confirm(message, activity);
    }

    @Override
    public void alert(String message)
    {
        BlockingDialogs.alert(message, activity);
    }

    @Override
    public float getAspectRatio()
    {
        return (float) getWidth() / (float) getHeight();
    }
}
