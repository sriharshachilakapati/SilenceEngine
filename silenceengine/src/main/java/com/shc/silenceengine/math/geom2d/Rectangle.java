/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 Sri Harsha Chilakapati
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
public class Rectangle
{
    public float x, y, width, height;

    public Rectangle()
    {
        this(0, 0, 0, 0);
    }

    public Rectangle(float x, float y, float width, float height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Rectangle(float width, float height)
    {
        this(0, 0, width, height);
    }

    public Rectangle(Vector2 min, Vector2 max)
    {
        this(min.x, min.y, max.x - min.x, max.y - min.y);
    }

    public Polygon createPolygon()
    {
        return createPolygon(null);
    }

    public Polygon createPolygon(Polygon polygon)
    {
        if (polygon == null)
            polygon = new Polygon();

        polygon.clearVertices();
        polygon.setRotation(0);
        polygon.setScale(1, 1);
        polygon.setPosition(x + width / 2, y + height / 2);

        polygon.addVertex(-width / 2, -height / 2);
        polygon.addVertex(+width / 2, -height / 2);
        polygon.addVertex(+width / 2, +height / 2);
        polygon.addVertex(-width / 2, +height / 2);

        return polygon;
    }

    public boolean intersects(Rectangle r)
    {
        return (x < r.x + r.width) &&
               (r.x < x + width) &&
               (y < r.y + r.height) &&
               (r.y < y + height);
    }

    public Rectangle copy()
    {
        return new Rectangle(x, y, width, height);
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

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rectangle rectangle = (Rectangle) o;

        return Float.compare(rectangle.height, height) == 0 &&
               Float.compare(rectangle.width, width) == 0 &&
               Float.compare(rectangle.x, x) == 0 &&
               Float.compare(rectangle.y, y) == 0;
    }

    @Override
    public String toString()
    {
        return "Rectangle{" +
               "width=" + width +
               ", height=" + height +
               '}';
    }

    public float getIntersectionWidth(Rectangle aabb)
    {
        float tx1 = this.x;
        float rx1 = aabb.x;

        float tx2 = tx1 + this.width;
        float rx2 = rx1 + aabb.width;

        return tx2 > rx2 ? rx2 - tx1 : tx2 - rx1;
    }

    public float getIntersectionHeight(Rectangle aabb)
    {
        float ty1 = this.y;
        float ry1 = aabb.y;

        float ty2 = ty1 + this.height;
        float ry2 = ry1 + aabb.height;

        return ty2 > ry2 ? ry2 - ty1 : ty2 - ry1;
    }

    public void set(float x, float y, float width, float height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void setPosition(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public void setCenter(float x, float y)
    {
        this.x = x + width / 2;
        this.y = y + height / 2;
    }

    public void set(Rectangle rect)
    {
        set(rect.x, rect.y, rect.width, rect.height);
    }
}
