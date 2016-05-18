package com.shc.silenceengine.backend.android;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.os.SystemClock;
import android.view.Window;
import android.view.WindowManager;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.io.FilePath;

/**
 * @author Sri Harsha Chilakapati
 */
public class AndroidDisplayDevice implements com.shc.silenceengine.core.IDisplayDevice
{
    public GLSurfaceView surfaceView;
    public Activity      activity;

    private double startTime;

    public AndroidDisplayDevice(Activity activity)
    {
        this.startTime = SystemClock.elapsedRealtimeNanos();
        this.activity = activity;

        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        surfaceView = new GLSurfaceView(activity);
        surfaceView.setPreserveEGLContextOnPause(true);
        surfaceView.setEGLContextClientVersion(3);
        surfaceView.setRenderer(new AndroidWindow());
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
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        else
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
