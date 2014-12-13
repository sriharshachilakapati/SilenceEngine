package com.shc.silenceengine.utils;

import com.shc.silenceengine.math.Matrix3;
import com.shc.silenceengine.math.Matrix4;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.math.Vector4;

/**
 * @author Sri Harsha Chilakapati
 */
public final class MathUtils
{
    private MathUtils()
    {
    }

    public static Vector2 add(Vector2 left, Vector2 right)
    {
        return new Vector2(left).add(right);
    }

    public static Vector3 add(Vector3 left, Vector3 right)
    {
        return new Vector3(left).add(right);
    }

    public static Vector4 add(Vector4 left, Vector4 right)
    {
        return new Vector4(left).add(right);
    }

    public static Vector2 subtract(Vector2 left, Vector2 right)
    {
        return new Vector2(left).subtract(right);
    }

    public static Vector3 subtract(Vector3 left, Vector3 right)
    {
        return new Vector3(left).subtract(right);
    }

    public static Vector4 subtract(Vector4 left, Vector4 right)
    {
        return new Vector4(left).subtract(right);
    }

    public static float dot(Vector2 left, Vector2 right)
    {
        return left.dot(right);
    }

    public static float dot(Vector3 left, Vector3 right)
    {
        return left.dot(right);
    }

    public static float dot(Vector4 left, Vector4 right)
    {
        return left.dot(right);
    }

    public static Vector3 cross(Vector3 left, Vector3 right)
    {
        return new Vector3(left).cross(right);
    }

    public static Vector2 scale(Vector2 v, float s)
    {
        return new Vector2(v).scale(s);
    }

    public static Vector3 scale(Vector3 v, float s)
    {
        return new Vector3(v).scale(s);
    }

    public static Vector4 scale(Vector4 v, float s)
    {
        return new Vector4(v).scale(s);
    }

    public static Vector2 scale(Vector2 v, float sx, float sy)
    {
        return new Vector2(v).scale(sx, sy);
    }

    public static Vector3 scale(Vector3 v, float sx, float sy, float sz)
    {
        return new Vector3(v).scale(sx, sy, sz);
    }

    public static Vector4 scale(Vector4 v, float sx, float sy, float sz, float sw)
    {
        return new Vector4(v).scale(sx, sy, sz, sw);
    }

    public static Matrix3 add(Matrix3 left, Matrix3 right)
    {
        return new Matrix3(left).add(right);
    }

    public static Matrix4 add(Matrix4 left, Matrix4 right)
    {
        return new Matrix4(left).add(right);
    }

    public static Matrix3 subtract(Matrix3 left, Matrix3 right)
    {
        return new Matrix3(left).subtract(right);
    }

    public static Matrix4 subtract(Matrix4 left, Matrix4 right)
    {
        return new Matrix4(left).subtract(right);
    }

    public static Matrix3 multiply(Matrix3 left, Matrix3 right)
    {
        return new Matrix3(left).multiply(right);
    }

    public static Matrix4 multiply(Matrix4 left, Matrix4 right)
    {
        return new Matrix4(left).multiply(right);
    }
}
