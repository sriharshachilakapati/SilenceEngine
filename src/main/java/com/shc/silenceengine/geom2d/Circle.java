package com.shc.silenceengine.geom2d;

/**
 * @author Sri Harsha Chilakapati
 */
public class Circle
{
    private float radius;
    private float x;
    private float y;

    public Circle()
    {
        this(0, 0, 1); // Point-circle
    }

    public Circle(float x, float y, float radius)
    {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public boolean intersects(Circle c)
    {
        return (x*c.x + y*c.y) <= (radius + c.radius) * (radius + c.radius);
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

    public float getRadius()
    {
        return radius;
    }

    public void setRadius(float radius)
    {
        this.radius = radius;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Circle circle = (Circle) o;

        return radius == circle.radius && x == circle.x && y == circle.y;
    }

    @Override
    public int hashCode()
    {
        int result = (radius != +0.0f ? Float.floatToIntBits(radius) : 0);
        result = 31 * result + (x != +0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
        return result;
    }

    @Override
    public String toString()
    {
        return "Circle{" +
               "x=" + x +
               ", y=" + y +
               ", r=" + radius +
               '}';
    }
}
