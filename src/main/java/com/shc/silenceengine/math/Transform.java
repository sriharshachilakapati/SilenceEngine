package com.shc.silenceengine.math;

import com.shc.silenceengine.utils.TransformUtils;

/**
 * @author Sri Harsha Chilakapati
 */
public class Transform
{
    // The transformation matrix
    private Matrix4 tMatrix;

    public Transform()
    {
        tMatrix = new Matrix4();
    }

    public Transform translate(Vector2 v)
    {
        return translate(new Vector3(v, 0));
    }

    public Transform translate(Vector3 v)
    {
        tMatrix.multiply(TransformUtils.createTranslation(v));
        return this;
    }

    public Transform rotate(Vector3 axis, float angle)
    {
        tMatrix.multiply(TransformUtils.createRotation(axis, angle));
        return this;
    }

    public Transform scale(Vector2 scale)
    {
        return scale(new Vector3(scale, 0));
    }

    public Transform scale(Vector3 scale)
    {
        tMatrix.multiply(TransformUtils.createScaling(scale));
        return this;
    }

    public Transform apply(Matrix4 matrix)
    {
        tMatrix.multiply(matrix);
        return this;
    }

    public Transform apply(Transform transform)
    {
        return apply(transform.getMatrix());
    }

    public Transform apply(Quaternion q)
    {
        return apply(TransformUtils.createRotation(q));
    }

    public Transform copy()
    {
        return new Transform().apply(tMatrix);
    }

    public Transform reset()
    {
        tMatrix.initIdentity();
        return this;
    }

    public Matrix4 getMatrix()
    {
        return tMatrix;
    }
}
