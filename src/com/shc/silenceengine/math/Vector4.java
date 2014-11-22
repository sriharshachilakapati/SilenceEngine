package com.shc.silenceengine.math;

/**
 * @author Sri Harsha Chilakapati
 */
public class Vector4
{
    private float x, y, z, w;

    public Vector4()
    {
        this(0, 0, 0, 0);
    }

    public Vector4(Vector2 v, float z, float w)
    {
        this(v.getX(), v.getY(), z, w);
    }

    public Vector4(float x, Vector2 v, float w)
    {
        this(x, v.getX(), v.getY(), w);
    }

    public Vector4(float x, float y, Vector2 v)
    {
        this(x, y, v.getX(), v.getY());
    }

    public Vector4(Vector3 v, float w)
    {
        this(v.getX(), v.getY(), v.getZ(), w);
    }

    public Vector4(float x, Vector3 v)
    {
        this(x, v.getX(), v.getY(), v.getZ());
    }

    public Vector4(Vector4 v)
    {
        this(v.x, v.y, v.z, v.w);
    }

    public Vector4(float x, float y, float z, float w)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vector4 add(float x, float y, float z, float w)
    {
        this.x += x;
        this.y += y;
        this.z += z;
        this.w += w;

        return this;
    }

    public Vector4 add(Vector4 v)
    {
        return add(v.x, v.y, v.z, v.w);
    }

    public Vector4 subtract(float x, float y, float z, float w)
    {
        return add(-x, -y, -z, -w);
    }

    public Vector4 subtract(Vector4 v)
    {
        return add(-v.x, -v.y, -v.z, -v.w);
    }

    public Vector4 scale(float s)
    {
        return scale(s, s, s, s);
    }

    public Vector4 scale(float sx, float sy, float sz, float sw)
    {
        x *= sx;
        y *= sy;
        z *= sz;
        w *= sw;

        return this;
    }


}
