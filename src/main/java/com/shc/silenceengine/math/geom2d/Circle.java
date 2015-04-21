/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2015 Sri Harsha Chilakapati
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.shc.silenceengine.math.geom2d;

import com.shc.silenceengine.math.Vector2;

/**
 * Represents a Circle Polygon.
 *
 * @author Sri Harsha Chilakapati
 */
public class Circle extends Ellipse
{
    /**
     * Constructs a point-circle, with center as origin and radius as 1
     */
    public Circle()
    {
        this(1);
    }

    /**
     * Constructs a circle with a center position and a radius.
     *
     * @param x      The center x-coordinate
     * @param y      The center y-coordinate
     * @param radius The radius of the circle
     */
    public Circle(float x, float y, float radius)
    {
        super(x, y, radius, radius);
    }

    public Circle(float radius)
    {
        this(0, 0, radius);
    }

    /**
     * Checks if a point exists inside this circle.
     *
     * @param p The point to check
     *
     * @return True if inside, else False.
     */
    @Override
    public boolean contains(Vector2 p)
    {
        return (((getX() - p.getX()) * (getX() - p.getX())) + ((getY() - p.getY()) * (getY() - p.getY()))) < getRadius() * getRadius();
    }

    @Override
    public int hashCode()
    {
        int result = (getRadius() != +0.0f ? Float.floatToIntBits(getRadius()) : 0);
        result = 31 * result + (getX() != +0.0f ? Float.floatToIntBits(getX()) : 0);
        result = 31 * result + (getY() != +0.0f ? Float.floatToIntBits(getY()) : 0);
        return result;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Circle circle = (Circle) o;

        return getRadius() == circle.getRadius() && getX() == circle.getX() && getY() == circle.getY();
    }

    @Override
    public String toString()
    {
        return "Circle{" +
               "x=" + getX() +
               ", y=" + getY() +
               ", r=" + getRadius() +
               '}';
    }

    public float getX()
    {
        return getCenter().getX();
    }

    public void setX(float x)
    {
        Vector2 center = getCenter();
        center.setX(x);
        setCenter(center);
    }

    public float getY()
    {
        return getCenter().getY();
    }

    public void setY(float y)
    {
        Vector2 center = getCenter();
        center.setY(y);
        setCenter(center);
    }

    public float getRadius()
    {
        return getRadiusX();
    }

    public void setRadius(float radius)
    {
        setRadiusX(radius);
        setRadiusY(radius);
    }
}
