package com.shc.silenceengine.graphics;

import com.shc.silenceengine.math.Matrix4;

/**
 * @author Sri Harsha Chilakapati
 */
public interface ICamera
{
    public Matrix4 getProjection();
    public Matrix4 getView();
}
