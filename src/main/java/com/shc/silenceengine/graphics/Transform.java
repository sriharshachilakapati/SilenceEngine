package com.shc.silenceengine.graphics;

import com.shc.silenceengine.math.Matrix4;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.Vector3;
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

    public Transform apply(Transform transform)
    {
        tMatrix.multiply(transform.getMatrix());
        return this;
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
