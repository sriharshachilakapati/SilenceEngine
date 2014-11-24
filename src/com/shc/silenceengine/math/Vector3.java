package com.shc.silenceengine.math;

/**
 * @author Sri Harsha Chilakapati
 */
public class Vector3
{
    private float x, y, z;

    public Vector3()
    {
        this(0, 0, 0);
    }

    public Vector3(Vector2 v, float z)
    {
        this(v.getX(), v.getY(), z);
    }

    public Vector3(float x, Vector2 v)
    {
        this(x, v.getX(), v.getY());
    }

    public Vector3(Vector3 v)
    {
        this(v.getX(), v.getY(), v.getZ());
    }

    public Vector3(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3 add(float x, float y, float z)
    {
        this.x += x;
        this.y += y;
        this.z += z;

        return this;
    }

    public Vector3 add(Vector3 v)
    {
        return add(v.x, v.y, v.z);
    }

    public Vector3 subtract(float x, float y, float z)
    {
        return add(-x, -y, -z);
    }

    public Vector3 subtract(Vector3 v)
    {
        return add(-v.x, -v.y, -v.z);
    }

    public Vector3 scale(float sx, float sy, float sz)
    {
        x *= sx;
        y *= sy;
        z *= sz;

        return this;
    }

    public Vector3 scale(float s)
    {
        return scale(s, s, s);
    }

    public Vector3 cross(Vector3 v)
    {
        return cross(v.x, v.y, v.z);
    }

    public Vector3 cross(float vx, float vy, float vz)
    {
        float x = this.x * vz - this.z * vy;
        float y = this.z * vx - this.x * vz;
        float z = this.x * vy - this.y * vx;

        this.x = x;
        this.y = y;
        this.z = z;

        return this;
    }

    public Vector3 normalize()
    {
        float l = length();

        x /= l;
        y /= l;
        z /= l;

        return this;
    }

    public float dot(Vector3 v)
    {
        return x*v.x + y*v.y + z*v.z;
    }

    public float lengthSquared() { return x*x + y*y + z*z; }

    public float length() { return (float) Math.sqrt(lengthSquared()); }

    public Vector3 copy()
    {
        return new Vector3(this);
    }

    public float getX()
    {
        return x;
    }

    public Vector3 setX(float x)
    {
        this.x = x;
        return this;
    }

    public float getY()
    {
        return y;
    }

    public Vector3 setY(float y)
    {
        this.y = y;
        return this;
    }

    public float getZ()
    {
        return z;
    }

    public Vector3 setZ(float z)
    {
        this.z = z;
        return this;
    }

    /* Swizzling */
    public float getR()
    {
        return x;
    }

    public Vector3 setR(float r)
    {
        x = r;
        return this;
    }

    public float getG()
    {
        return y;
    }

    public Vector3 setG(float g)
    {
        y = g;
        return this;
    }

    public float getB()
    {
        return z;
    }

    public Vector3 setB(float b)
    {
        z = b;
        return this;
    }

    public Vector2 getXY()
    {
        return new Vector2(x, y);
    }

    public Vector2 getYZ()
    {
        return new Vector2(y, z);
    }

    public Vector2 getXZ()
    {
        return new Vector2(x, z);
    }
}
