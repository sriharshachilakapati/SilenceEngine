package com.shc.silenceengine.math;

/**
 * @author Sri Harsha Chilakapati
 */
public class Vector4
{
    public float x, y, z, w;

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
        return new Vector4(this.x + x, this.y + y, this.z + z, this.w + w);
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
        return new Vector4(x * sx, y * sy, z * sz, w * sw);
    }

    public float dot(Vector4 v)
    {
        return x*v.x + y*v.y + z*v.z + w*v.w;
    }

    public float lengthSquared()
    {
        return x*x + y*y + z*z + w*w;
    }

    public float length()
    {
        return (float) Math.sqrt(lengthSquared());
    }

    public Vector4 normalize()
    {
        float l = length();

        return new Vector4(x/l, y/l, z/l, w/l);
    }

    public Vector4 negate()
    {
        return new Vector4(-x, -y, -z, -w);
    }

    public Vector4 multiply(Vector4 v)
    {
        return scale(v.x, v.y, v.z, v.w);
    }

    public Vector4 copy()
    {
        return new Vector4(this);
    }

    public float getX()
    {
        return x;
    }

    public Vector4 setX(float x)
    {
        this.x = x;
        return this;
    }

    public float getY()
    {
        return y;
    }

    public Vector4 setY(float y)
    {
        this.y = y;
        return this;
    }

    public float getZ()
    {
        return z;
    }

    public Vector4 setZ(float z)
    {
        this.z = z;
        return this;
    }

    public float getW()
    {
        return w;
    }

    public Vector4 setW(float w)
    {
        this.w = w;
        return this;
    }

    /* Swizzling */
    public float getR()
    {
        return x;
    }

    public Vector4 setR(float r)
    {
        x = r;
        return this;
    }

    public float getG()
    {
        return y;
    }

    public Vector4 setG(float g)
    {
        y = g;
        return this;
    }

    public float getB()
    {
        return z;
    }

    public Vector4 setB(float b)
    {
        z = b;
        return this;
    }

    public float getA()
    {
        return w;
    }

    public Vector4 setA(float a)
    {
        w = a;
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

    // TODO: Implement more swizzling

    @Override
    public String toString()
    {
        return "[" + x + ", " + y + ", " + z + ", " + w + "]";
    }
}
