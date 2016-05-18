package com.shc.silenceengine.backend.android;

import android.opengl.GLSurfaceView;
import com.shc.silenceengine.core.SilenceEngine;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author Sri Harsha Chilakapati
 */
public class AndroidWindow implements GLSurfaceView.Renderer
{
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config)
    {
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height)
    {
        SilenceEngine.eventManager.raiseResizeEvent();
    }

    @Override
    public void onDrawFrame(GL10 gl)
    {
        SilenceEngine.gameLoop.performLoopFrame();
    }
}
