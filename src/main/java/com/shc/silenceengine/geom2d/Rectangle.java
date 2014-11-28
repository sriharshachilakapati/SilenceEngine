package com.shc.silenceengine.geom2d;

/**
 * @author Sri Harsha Chilakapati
 */
public class Rectangle
{
    private float x, y, width, height;

    public Rectangle()
    {
        this(0, 0, 0, 0);
    }

    public Rectangle(float x, float y, float width, float height)
    {
        this.x = x;
        this.y = y;

        this.width  = width;
        this.height = height;
    }

    public boolean intersects(Rectangle r)
    {
        return (x < r.x + r.width) && (r.x < x + width) && (y < r.y + r.height) && (r.y < y + height);
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

    public float getWidth()
    {
        return width;
    }

    public void setWidth(float width)
    {
        this.width = width;
    }

    public float getHeight()
    {
        return height;
    }

    public void setHeight(float height)
    {
        this.height = height;
    }

    @Override
    public String toString()
    {
        return "Rectangle{" +
               "x=" + x +
               ", y=" + y +
               ", width=" + width +
               ", height=" + height +
               '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rectangle rectangle = (Rectangle) o;

        if (Float.compare(rectangle.height, height) != 0) return false;
        if (Float.compare(rectangle.width, width) != 0) return false;
        if (Float.compare(rectangle.x, x) != 0) return false;
        if (Float.compare(rectangle.y, y) != 0) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
        result = 31 * result + (width != +0.0f ? Float.floatToIntBits(width) : 0);
        result = 31 * result + (height != +0.0f ? Float.floatToIntBits(height) : 0);
        return result;
    }
}
