package com.shc.silenceengine.utils;

import com.shc.silenceengine.math.Matrix4;
import com.shc.silenceengine.math.Vector3;

/**
 * @author Sri Harsha Chilakapati
 */
public class TransformUtils
{
    public static Matrix4 createTranslation(Vector3 translation)
    {
        Matrix4 result = new Matrix4().initIdentity();

        result.set(0, 3, translation.getX())
              .set(1, 3, translation.getY())
              .set(2, 3, translation.getZ());

        return result;
    }

    public static Matrix4 createScaling(Vector3 scale)
    {
        Matrix4 result = new Matrix4().initIdentity();

        result.set(0, 0, scale.getX())
              .set(1, 1, scale.getY())
              .set(2, 2, scale.getZ());

        return result;
    }

    public static Matrix4 createRotation(Vector3 axis, float angle)
    {
        Matrix4 result = new Matrix4().initIdentity();

        float c = (float) Math.cos(angle);
        float s = (float) Math.sin(angle);

        Vector3 v = axis.copy().normalize();

        result.set(0, 0, v.getX() * v.getX() * (1-c) + c)
              .set(0, 1, v.getX() * v.getY() * (1-c) - v.getZ() * s)
              .set(0, 2, v.getX() * v.getZ() * (1-c) + v.getY() * s);

        result.set(1, 0, v.getY() * v.getX() * (1-c) + v.getZ() * s)
              .set(1, 1, v.getY() * v.getY() * (1-c) + c)
              .set(1, 2, v.getY() * v.getZ() * (1-c) - v.getX() * s);

        result.set(2, 0, v.getX() * v.getZ() * (1-c) - v.getY() * s)
              .set(2, 1, v.getY() * v.getZ() * (1-c) + v.getX() * s)
              .set(2, 2, v.getZ() * v.getZ() * (1-c) + c);

        return result;
    }

    public static Matrix4 createOrtho2d(float left, float right, float bottom, float top, float zNear, float zFar)
    {
        Matrix4 result = new Matrix4().initIdentity();

        float tx = - (right + left) / (right - left);
        float ty = - (top + bottom) / (top - bottom);
        float tz = - (zFar + zNear) / (zFar - zNear);

        result.set(0, 0, 2/(right - left))
              .set(1, 1, 2/(top - bottom))
              .set(2, 2, 2/(zNear - zFar));

        result.set(0, 3, tx).set(1, 3, ty).set(2, 3, tz);

        return result;
    }

    public static Matrix4 createPerspective(float fovy, float aspect, float zNear, float zFar)
    {
        Matrix4 result = new Matrix4().initZero();

        float f = (float) (1f/Math.tan(fovy/2));

        result.set(0, 0, f/aspect)
              .set(1, 1, f)
              .set(2, 2, (zFar+zNear)/(zFar-zNear))
              .set(2, 3, 2*zFar*zNear/(zNear-zFar))
              .set(3, 2, -1);

        return result;
    }
}
