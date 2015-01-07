package com.shc.silenceengine.math;

import com.shc.silenceengine.utils.MathUtils;

/**
 * @author Sri Harsha Chilakapati
 */
public class Vector3
{
    public static final Vector3 ZERO   = new Vector3(0, 0, 0);
    public static final Vector3 AXIS_X = new Vector3(1, 0, 0);
    public static final Vector3 AXIS_Y = new Vector3(0, 1, 0);
    public static final Vector3 AXIS_Z = new Vector3(0, 0, 1);

    public float x, y, z;

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
    
    public Vector3(Vector4 v)
    {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    public Vector3 add(float x, float y, float z)
    {
        return new Vector3(this.x + x, this.y + y, this.z + z);
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
        return new Vector3(x * sx, y * sy, z * sz);
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

        return new Vector3(x, y, z);
    }

    public Vector3 normalize()
    {
        float l = length();
        
        if (l == 0 || l == 1)
            return copy();

        return new Vector3(x/l, y/l, z/l);
    }

    public Vector3 negate()
    {
        return new Vector3(-x, -y, -z);
    }

    public float dot(Vector3 v)
    {
        return x*v.x + y*v.y + z*v.z;
    }

    public float lengthSquared() { return x*x + y*y + z*z; }

    public float length() { return (float) Math.sqrt(lengthSquared()); }
    
    public float distanceSquared(float x, float y, float z)
    {
        final float x2 = (x - this.x) * (x - this.x);
        final float y2 = (y - this.y) * (y - this.y);
        final float z2 = (z - this.z) * (z - this.z);
        
        return x2 + y2 + z2;
    }
    
    public float distanceSquared(Vector3 v)
    {
        return distanceSquared(v.x, v.y, v.z);
    }
    
    public float distanceSquared(Vector2 v)
    {
        return distanceSquared(v.x, v.y, 0);
    }
    
    public float distance(float x, float y, float z)
    {
        return MathUtils.sqrt(distanceSquared(x, y, z));
    }
    
    public float distance(Vector3 v)
    {
        return MathUtils.sqrt(distanceSquared(v));
    }
    
    public float distance(Vector2 v)
    {
        return MathUtils.sqrt(distanceSquared(v));
    }
    
    public Vector3 rotate(Vector3 axis, float angle)
    {
        return new Quaternion(axis, angle).multiply(this);
    }
    
    public Vector3 lerp(Vector3 target, float alpha)
    {
        return scale(1f - alpha).add(target.scale(alpha));
    }

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

    public Vector3 set(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
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

    @Override
    public String toString()
    {
        return "[" + x + ", " + y + ", " + z + "]";
    }
}
