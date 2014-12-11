package com.shc.silenceengine.graphics;

import com.shc.silenceengine.math.Matrix4;

/**
 * @author Sri Harsha Chilakapati
 */
public class BaseCamera
{
    public static Matrix4 projection = new Matrix4();
    public static Matrix4 view = new Matrix4();

    public void apply()
    {
    }
}
