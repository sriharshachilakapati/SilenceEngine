package com.shc.silenceengine.math;

/**
 * @author Sri Harsha Chilakapati
 */
public class Vector2
{
    private float x, y;

    public Vector2()
    {
        this(0, 0);
    }

    public Vector2(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public Vector2(Vector2 v)
    {
        x = v.x;
        y = v.y;
    }

    public float lengthSquared()
    {
        return x*x + y*y;
    }

    public float length()
    {
        return (float)Math.sqrt(lengthSquared());
    }

    public Vector2 copy()
    {
        return new Vector2(this);
    }

    public Vector2 add(float x, float y)
    {
        this.x += x;
        this.y += y;

        return this;
    }

    public Vector2 add(Vector2 v)
    {
        return add(v.x, v.y);
    }

    public Vector2 subtract(float x, float y)
    {
        return add(-x, -y);
    }

    public Vector2 subtract(Vector2 v)
    {
        return add(-v.x, -v.y);
    }

    public Vector2 scale(float s)
    {
        return scale(s, s);
    }

    public Vector2 scale(float sx, float sy)
    {
        x *= sx;
        y *= sy;

        return this;
    }

    public float dot(Vector2 v)
    {
        return dot(v.x, v.y);
    }

    public float dot(float x, float y)
    {
        return this.x*x + this.y*y;
    }

    public Vector2 normalize()
    {
        float l = length();

        x /= l;
        y /= l;

        return this;
    }

    public float getX()
    {
        return x;
    }

    public void setX(float x)
    {
        this.x = x;
    }

    public float getY()
    {
        return y;
    }

    public void setY(float y)
    {
        this.y = y;
    }
}
