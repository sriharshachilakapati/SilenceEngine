package com.shc.silenceengine.graphics.cameras;

import com.shc.silenceengine.math.Matrix4;

/**
 * @author Sri Harsha Chilakapati
 */
public abstract class BaseCamera
{
    public static BaseCamera CURRENT = new NullCamera();

    public void apply()
    {
        CURRENT = this;
    }

    public abstract Matrix4 getProjection();

    public abstract Matrix4 getView();
}
