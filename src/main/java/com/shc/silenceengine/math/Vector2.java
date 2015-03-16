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

package com.shc.silenceengine.math;

import com.shc.silenceengine.utils.MathUtils;
import com.shc.silenceengine.utils.ReusableStack;

/**
 * @author Sri Harsha Chilakapati
 */
public class Vector2
{
    public static final Vector2 ZERO   = new Vector2(0, 0);
    public static final Vector2 AXIS_X = new Vector2(1, 0);
    public static final Vector2 AXIS_Y = new Vector2(0, 1);

    public static final ReusableStack<Vector2> REUSABLE_STACK = new ReusableStack<>(Vector2.class);

    public float x, y;

    public Vector2()
    {
        this(0, 0);
    }

    public Vector2(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public Vector2(float v)
    {
        this(v, v);
    }

    public Vector2(Vector2 v)
    {
        this(v.x, v.y);
    }

    public Vector2(Vector3 v)
    {
        this(v.x, v.y);
    }

    public Vector2(Vector4 v)
    {
        this(v.x, v.y);
    }

    public Vector2 add(Vector2 v)
    {
        return add(v.x, v.y);
    }

    public Vector2 add(float x, float y)
    {
        return new Vector2(this.x + x, this.y + y);
    }

    public Vector2 addSelf(Vector2 v)
    {
        return addSelf(v.x, v.y);
    }

    public Vector2 addSelf(float x, float y)
    {
        return set(this.x + x, this.y + y);
    }

    public Vector2 set(float x, float y)
    {
        this.x = x;
        this.y = y;

        return this;
    }

    public Vector2 subtract(float x, float y)
    {
        return add(-x, -y);
    }

    public Vector2 normalize()
    {
        return copy().normalizeSelf();
    }

    public Vector2 copy()
    {
        return new Vector2(this);
    }

    public Vector2 normalizeSelf()
    {
        float l = length();

        if (l == 0 || l == 1)
            return this;

        return set(x / l, y / l);
    }

    public float length()
    {
        return (float) Math.sqrt(lengthSquared());
    }

    public float lengthSquared()
    {
        return x * x + y * y;
    }

    public Vector2 rotate(float angle)
    {
        return copy().rotateSelf(angle);
    }

    public Vector2 rotateSelf(float angle)
    {
        float cos = MathUtils.cos(angle);
        float sin = MathUtils.sin(angle);

        return set(x * cos - y * sin, x * sin + y * cos);
    }

    public Vector2 negate()
    {
        return new Vector2(-x, -y);
    }

    public Vector2 negateSelf()
    {
        return set(-x, -y);
    }

    public float angle()
    {
        return MathUtils.atan2(y, x);
    }

    public float angle(Vector2 v)
    {
        return MathUtils.acos(this.dot(v) / (length() * v.length()));
    }

    public float dot(Vector2 v)
    {
        return dot(v.x, v.y);
    }

    public float dot(float x, float y)
    {
        return this.x * x + this.y * y;
    }

    public float distance(Vector2 v)
    {
        return MathUtils.sqrt(distanceSquared(v));
    }

    public float distanceSquared(Vector2 v)
    {
        return (v.x - x) * (v.x - x) + (v.y - y) * (v.y - y);
    }

    public Vector2 lerp(Vector2 target, float alpha)
    {
        return copy().lerpSelf(target, alpha);
    }

    public Vector2 lerpSelf(Vector2 target, float alpha)
    {
        final float oneMinusAlpha = 1f - alpha;

        float x = (this.x * oneMinusAlpha) + (target.x * alpha);
        float y = (this.y * oneMinusAlpha) + (target.y * alpha);

        return set(x, y);
    }

    public Vector2 perpendicular()
    {
        return new Vector2(y, -x);
    }

    public Vector2 perpendicularSelf()
    {
        return set(y, x);
    }

    public Vector2 projectSelf(Vector2 v)
    {
        return scaleSelf(dot(v) / v.lengthSquared());
    }

    public Vector2 scaleSelf(float s)
    {
        return scaleSelf(s, s);
    }

    public Vector2 scaleSelf(float sx, float sy)
    {
        return set(x * sx, y * sy);
    }

    public Vector2 reflect(Vector2 axis)
    {
        return project(axis).scale(2).subtract(this);
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
        return new Vector2(x * sx, y * sy);
    }

    public Vector2 project(Vector2 v)
    {
        return scale(dot(v) / v.lengthSquared());
    }

    public Vector2 reflectSelf(Vector2 axis)
    {
        return set(project(axis).scaleSelf(2).subtractSelf(this));
    }

    public Vector2 subtractSelf(Vector2 v)
    {
        return subtractSelf(v.x, v.y);
    }

    public Vector2 subtractSelf(float x, float y)
    {
        return addSelf(-x, -y);
    }

    public Vector2 set(Vector2 v)
    {
        return set(v.x, v.y);
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

    public Vector2 set(float v)
    {
        return set(v, v);
    }

    @Override
    public int hashCode()
    {
        int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
        return result;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector2 vector2 = (Vector2) o;

        return Float.compare(vector2.x, x) == 0 && Float.compare(vector2.y, y) == 0;
    }

    @Override
    public String toString()
    {
        return "[" + x + ", " + y + "]";
    }
}
