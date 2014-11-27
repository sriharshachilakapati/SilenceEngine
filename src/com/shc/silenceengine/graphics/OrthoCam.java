package com.shc.silenceengine.graphics;

import com.shc.silenceengine.core.Display;
import com.shc.silenceengine.math.Matrix4;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.utils.TransformUtils;

/**
 * @author Sri Harsha Chilakapati
 */
public class OrthoCam implements ICamera
{
    private Matrix4 mProj;
    private Matrix4 mView;

    private float width;
    private float height;

    public OrthoCam(float left, float right, float bottom, float top)
    {
        width  = right - left;
        height = bottom - top;
        mProj  = TransformUtils.createOrtho2d(left, right, bottom, top, 0, 100);
        mView  = new Matrix4().initIdentity();
    }

    public OrthoCam(float width, float height)
    {
        this(0, width, height, 0);
    }

    public OrthoCam()
    {
        this(Display.getWidth(), Display.getHeight());
    }

    public OrthoCam translate(float x, float y)
    {
        mView.multiply(TransformUtils.createTranslation(new Vector3(x, y, 0)));
        return this;
    }

    public OrthoCam translate(Vector2 v)
    {
        mView.multiply(TransformUtils.createTranslation(new Vector3(v, 0)));
        return this;
    }

    public OrthoCam center(float x, float y)
    {
        return lookAt(new Vector2(x, y));
    }

    public OrthoCam lookAt(Vector2 v)
    {
        float x = (width/2) - v.getX();
        float y = (height/2) - v.getY();

        return translate(x, y);
    }

    public OrthoCam rotate(Vector3 axis, float angle)
    {
        mView.multiply(TransformUtils.createRotation(axis, angle));
        return this;
    }

    public OrthoCam initProjection(float width, float height)
    {
        return initProjection(0, width, height, 0);
    }

    public OrthoCam initProjection(float left, float right, float bottom, float top)
    {
        width  = right - left;
        height = bottom - top;
        mProj  = TransformUtils.createOrtho2d(left, right, bottom, top, 0, 100);
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
