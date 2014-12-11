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

        result.set(3, 0, translation.getX())
              .set(3, 1, translation.getY())
              .set(3, 2, translation.getZ());

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
              .set(1, 0, v.getX() * v.getY() * (1-c) - v.getZ() * s)
              .set(2, 0, v.getX() * v.getZ() * (1-c) + v.getY() * s);

        result.set(0, 1, v.getY() * v.getX() * (1-c) + v.getZ() * s)
              .set(1, 1, v.getY() * v.getY() * (1-c) + c)
              .set(2, 1, v.getY() * v.getZ() * (1-c) - v.getX() * s);

        result.set(0, 2, v.getX() * v.getZ() * (1-c) - v.getY() * s)
              .set(1, 2, v.getY() * v.getZ() * (1-c) + v.getX() * s)
              .set(2, 2, v.getZ() * v.getZ() * (1-c) + c);

        return result;
    }

    public static Matrix4 createOrtho2d(float left, float right, float bottom, float top, float zNear, float zFar)
    {
        Matrix4 result = new Matrix4();

        result.set(0, 0, 2 / (right - left))
              .set(1, 1, 2 / (top - bottom))
              .set(2, 2, -2 / (zFar - zNear))
              .set(3, 0, -(right + left) / (right - left))
              .set(3, 1, -(top + bottom) / (top - bottom))
              .set(3, 2, -(zFar + zNear) / (zFar - zNear))
              .set(3, 3, 1);

        return result;
    }

    public static Matrix4 createPerspective(float fovy, float aspect, float zNear, float zFar)
    {
        Matrix4 result = new Matrix4().initZero();

        float yScale = 1f / (float) Math.tan(Math.toRadians(fovy / 2f));
        float xScale = yScale / aspect;
        float frustumLength = zFar - zNear;

        result.set(0, 0, xScale)
              .set(1, 1, yScale)
              .set(2, 2, -((zFar + zNear) / frustumLength))
              .set(2, 3, -1)
              .set(3, 2, -((2 * zFar * zNear) / frustumLength))
              .set(3, 3, 0);

        return result;
    }

    public static Matrix4 createLookAt(Vector3 eye, Vector3 center, Vector3 up)
    {
        Matrix4 result = new Matrix4().initIdentity();

        Vector3 f = center.copy().subtract(eye).normalize();
        Vector3 s = f.copy().cross(up).normalize();
        Vector3 u = s.copy().cross(f);

        result.set(0, 0, s.getX())
              .set(1, 0, s.getY())
              .set(2, 0, s.getZ());

        result.set(0, 1, u.getX())
              .set(1, 1, u.getY())
              .set(2, 1, u.getZ());

        result.set(0, 2, -f.getX())
              .set(1, 2, -f.getY())
              .set(2, 2, -f.getZ());

        result.set(3, 0, -s.dot(eye))
              .set(3, 1, -u.dot(eye))
              .set(3, 2, f.dot(eye));

        return result;
    }
}
