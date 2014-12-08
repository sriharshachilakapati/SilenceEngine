package com.shc.silenceengine.graphics;

import com.shc.silenceengine.core.Display;
import com.shc.silenceengine.math.Matrix4;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.utils.TransformUtils;

/**
 * @author Sri Harsha Chilakapati
 */
public class PerspCam implements ICamera
{
    private Matrix4 mProj;
    private Matrix4 mView;

    public PerspCam()
    {
        this(70, Display.getAspectRatio(), 0.01f, 100);
    }

    public PerspCam(float fovy, float aspect, float zNear, float zFar)
    {
        mProj = TransformUtils.createPerspective(fovy, aspect, zNear, zFar);
        mView = new Matrix4().initIdentity();

        translate(0, 0, -1);
    }

    public PerspCam translate(Vector3 v)
    {
        mView.multiply(TransformUtils.createTranslation(v));
        return this;
    }

    public PerspCam translate(float x, float y, float z)
    {
        return translate(new Vector3(x, y, z));
    }

    public PerspCam rotate(Vector3 axis, float angle)
    {
        mView.multiply(TransformUtils.createRotation(axis, angle));
        return this;
    }

    public PerspCam setPositionAndLookAt(Vector3 position, Vector3 location)
    {
        mView = TransformUtils.createLookAt(position, location, Vector3.AXIS_Y);
        return this;
    }

    public PerspCam initProjection(float fovy, float aspect, float zNear, float zFar)
    {
        mProj = TransformUtils.createPerspective(fovy, aspect, zNear, zFar);
        return this;
    }

    public Matrix4 getProjection()
    {
        return mProj;
    }

    public Matrix4 getView()
    {
        return mView;
    }
}