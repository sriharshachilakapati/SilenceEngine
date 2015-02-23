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
 * @author Sri Harsha Chilakapati
 */
public class Rectangle extends Polygon
{
    private float width, height;

    private Vector2 v1;
    private Vector2 v2;
    private Vector2 v3;
    private Vector2 v4;

    private Vector2 min;
    private Vector2 max;

    public Rectangle()
    {
        this(0, 0, 0, 0);
    }

    public Rectangle(float x, float y, float width, float height)
    {
        this.width = width;
        this.height = height;

        v1 = new Vector2();
        v2 = new Vector2();
        v3 = new Vector2();
        v4 = new Vector2();

        min = new Vector2();
        max = new Vector2();

        setPosition(new Vector2(x, y));

        updateVertices();
    }

    private void updateVertices()
    {
        clearVertices();

        addVertex(v1.set(0, 0));
        addVertex(v2.set(width, 0));
        addVertex(v3.set(width, height));
        addVertex(v4.set(0, height));
    }

    public Rectangle(float width, float height)
    {
        this(0, 0, width, height);
    }

    public Rectangle(Vector2 min, Vector2 max)
    {
        this(min.x, min.y, max.x - min.x, max.y - min.y);
    }

    public boolean intersects(Polygon p)
    {
        if (p instanceof Rectangle && p.getRotation() == 0 && getRotation() == 0)
        {
            Rectangle r = (Rectangle) p;

            float x = getPosition().getX();
            float y = getPosition().getY();

            float rx = r.getX();
            float ry = r.getY();

            return (x < rx + r.width) && (rx < x + width) && (y < ry + r.height) && (ry < y + height);
        }
        else
            return super.intersects(p);
    }

    public float getX()
    {
        return getPosition().getX();
    }

    public void setX(float x)
    {
        Vector2 position = getPosition();
        position.setX(x);
        setPosition(position);
    }

    public float getY()
    {
        return getPosition().getY();
    }

    public void setY(float y)
    {
        Vector2 position = getPosition();
        position.setY(y);
        setPosition(position);
    }

    public Rectangle copy()
    {
        return new Rectangle(getX(), getY(), width, height);
    }

    @Override
    public int hashCode()
    {
        int result = (getX() != +0.0f ? Float.floatToIntBits(getX()) : 0);
        result = 31 * result + (getY() != +0.0f ? Float.floatToIntBits(getY()) : 0);
        result = 31 * result + (width != +0.0f ? Float.floatToIntBits(width) : 0);
        result = 31 * result + (height != +0.0f ? Float.floatToIntBits(height) : 0);
        return result;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rectangle rectangle = (Rectangle) o;

        return Float.compare(rectangle.height, height) == 0 &&
               Float.compare(rectangle.width, width) == 0 &&
               Float.compare(rectangle.getX(), getX()) == 0 &&
               Float.compare(rectangle.getY(), getY()) == 0;
    }

    @Override
    public String toString()
    {
        return "Rectangle{" +
               "x=" + getX() +
               ", y=" + getY() +
               ", width=" + width +
               ", height=" + height +
               '}';
    }

    public float getWidth()
    {
        return width;
    }

    public void setWidth(float width)
    {
        this.width = width;
        updateVertices();
    }

    public float getHeight()
    {
        return height;
    }

    public void setHeight(float height)
    {
        this.height = height;
        updateVertices();
    }

    public void set(float x, float y, float width, float height)
    {
        setPosition(x, y);

        this.width = width;
        this.height = height;

        float rotation = getRotation();
        updateVertices();

        setRotation(rotation);
    }

    public Vector2 getMin()
    {
        return min.set(getPosition()).addSelf(v1);
    }

    public Vector2 getMax()
    {
        return max.set(min).addSelf(v3);
    }
}
