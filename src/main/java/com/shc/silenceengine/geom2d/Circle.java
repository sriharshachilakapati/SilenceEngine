package com.shc.silenceengine.geom2d;

import com.shc.silenceengine.math.Vector2;

/**
 * @author Sri Harsha Chilakapati
 */
public class Circle extends Polygon
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

        setPosition(new Vector2(x, y));
        updateVertices();
    }

    private void updateVertices()
    {
        float numSegments = 10 * radius;

        float theta = (float) (2 * Math.PI / numSegments);
        float c = (float) Math.cos(theta);
        float s = (float) Math.sin(theta);
        float t;

        float x = radius;
        float y = 0;

        clearVertices();
        for (int i = 0; i < numSegments; i++)
        {
            addVertex(new Vector2(x, y));

            t = x;
            x = c * x - s * y;
            y = s * t + c * y;
        }
    }

    public boolean intersects(Polygon p)
    {
        if (p instanceof Circle)
        {
            Circle c = (Circle) p;
            return (((x - c.x) * (x - c.x)) + ((y - c.y) * (y - c.y))) < (radius + c.radius) * (radius + c.radius);
        }
        else
            return super.intersects(p);
    }

    public boolean contains(Point p)
    {
        return (((x - p.getX()) * (x - p.getX())) + ((y - p.getY()) * (y - p.getY()))) < radius * radius;
    }

    public float getX()
    {
        return x;
    }

    public void setX(float x)
    {
        this.x = x;
        setPosition(new Vector2(x, y));
    }

    public float getY()
    {
        return y;
    }

    public void setY(float y)
    {
        this.y = y;
        setPosition(new Vector2(x, y));
    }

    public float getRadius()
    {
        return radius;
    }

    public void setRadius(float radius)
    {
        this.radius = radius;
        updateVertices();
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
