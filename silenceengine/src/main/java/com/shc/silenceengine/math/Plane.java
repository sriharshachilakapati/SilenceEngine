/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2016 Sri Harsha Chilakapati
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
 *
 */

package com.shc.silenceengine.math;

import com.shc.silenceengine.utils.ReusableStack;

/**
 * @author Sri Harsha Chilakapati
 */
public class Plane
{
    public static final ReusableStack<Plane> REUSABLE_STACK = new ReusableStack<>(Plane::new);

    public Vector3 normal;
    public float   d;

    public Plane()
    {
        this(Vector3.ZERO, 0);
    }

    public Plane(Vector3 normal, float d)
    {
        this.normal = new Vector3(normal);
        this.d = d;
    }

    public Plane(float a, float b, float c, float d)
    {
        this.normal = new Vector3(a, b, c);
        this.d = d;

        float length = normal.length();
        normal.scale(1 / length);
        this.d /= length;
    }

    public Plane(Plane plane)
    {
        this(plane.normal, plane.d);
    }

    public static Vector3 intersection(Plane p1, Plane p2, Plane p3, Vector3 dest)
    {
        if (dest == null)
            dest = new Vector3();

        float c23x, c23y, c23z;
        float c31x, c31y, c31z;
        float c12x, c12y, c12z;

        c23x = p2.normal.y * p3.normal.z - p2.normal.z * p3.normal.y;
        c23y = p2.normal.z * p3.normal.x - p2.normal.x * p3.normal.z;
        c23z = p2.normal.x * p3.normal.y - p2.normal.y * p3.normal.x;

        c31x = p3.normal.y * p1.normal.z - p3.normal.z * p1.normal.y;
        c31y = p3.normal.z * p1.normal.x - p3.normal.x * p1.normal.z;
        c31z = p3.normal.x * p1.normal.y - p3.normal.y * p1.normal.x;

        c12x = p1.normal.y * p2.normal.z - p1.normal.z * p2.normal.y;
        c12y = p1.normal.z * p2.normal.x - p1.normal.x * p2.normal.z;
        c12z = p1.normal.x * p2.normal.y - p1.normal.y * p2.normal.x;

        float dot = p1.normal.dot(c23x, c23y, c23z);
        dest.x = (-c23x * p1.d - c31x * p2.d - c12x * p3.d) / dot;
        dest.y = (-c23y * p1.d - c31y * p2.d - c12y * p3.d) / dot;
        dest.z = (-c23z * p1.d - c31z * p2.d - c12z * p3.d) / dot;

        return dest;
    }

    public Side testPoint(Vector3 point)
    {
        return testPoint(point.x, point.y, point.z);
    }

    public Side testPoint(float x, float y, float z)
    {
        float test = normal.dot(x, y, z) + d;

        if (test == 0)
            return Side.ON_PLANE;

        if (test > 0)
            return Side.FRONT;

        return Side.BACK;
    }

    public Plane set(Vector3 normal, float d)
    {
        this.normal.set(normal);
        this.d = d;

        return this;
    }

    public Plane set(float a, float b, float c, float d)
    {
        this.normal.set(a, b, c);
        this.d = d;

        float length = normal.length();
        normal.scale(1 / length);
        this.d /= length;

        return this;
    }

    public Plane set(Plane plane)
    {
        this.normal.set(plane.normal);
        this.d = plane.d;

        return this;
    }

    @Override
    public int hashCode()
    {
        int result = normal.hashCode();
        result = 31 * result + (d != +0.0f ? Float.floatToIntBits(d) : 0);
        return result;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Plane plane = (Plane) o;

        return Float.compare(plane.d, d) == 0 &&
               normal.x == plane.normal.x &&
               normal.y == plane.normal.y &&
               normal.z == plane.normal.z;
    }

    @Override
    public String toString()
    {
        return "Plane{" +
               "normal=" + normal +
               ", d=" + d +
               '}';
    }

    public enum Side
    {
        FRONT, BACK, ON_PLANE
    }
}
