package com.shc.silenceengine.graphics.cameras;

import com.shc.silenceengine.math.Matrix4;

/**
 * A NullCamera, whose view and projection matrices are always
 * identities. Allows you to use the default transformations of
 * OpenGL context.
 *
 * @author Sri Harsha Chilakapati
 */
public class NullCamera extends BaseCamera
{
    private Matrix4 mProj;
    private Matrix4 mView;

    public NullCamera()
    {
        mProj = new Matrix4();
        mView = new Matrix4();
    }

    @Override
    public Matrix4 getProjection()
    {
        return mProj;
    }

    @Override
    public Matrix4 getView()
    {
        return mView;
    }
}
