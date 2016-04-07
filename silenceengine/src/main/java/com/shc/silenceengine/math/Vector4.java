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

import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.utils.ReusableStack;

/**
 * @author Sri Harsha Chilakapati
 */
public class Vector4
{
    public static final Vector4 ZERO = new Vector4(0, 0, 0, 0);

    public static final ReusableStack<Vector4> REUSABLE_STACK = new ReusableStack<>(Vector4::new);

    public float x, y, z, w;

    public Vector4()
    {
        this(0, 0, 0, 0);
    }

    public Vector4(float x, float y, float z, float w)
    {
        set(x, y, z, w);
    }

    public Vector4(float v)
    {
        this(v, v, v, v);
    }

    public Vector4(Vector2 v, float z, float w)
    {
        this(v.x, v.y, z, w);
    }

    public Vector4(float x, Vector2 v, float w)
    {
        this(x, v.x, v.y, w);
    }

    public Vector4(float x, float y, Vector2 v)
    {
        this(x, y, v.x, v.y);
    }

    public Vector4(Vector3 v, float w)
    {
        this(v.x, v.y, v.z, w);
    }

    public Vector4(float x, Vector3 v)
    {
        this(x, v.x, v.y, v.z);
    }

    public Vector4(Vector4 v)
    {
        this(v.x, v.y, v.z, v.w);
    }

    public Vector4 set(float x, float y, float z, float w)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;

        return this;
    }

    public Vector4 add(float x, float y, float z, float w)
    {
        return set(this.x + x, this.y + y, this.z + z, this.w + w);
    }

    public Vector4 copy()
    {
        return new Vector4(this);
    }

    public Vector4 add(Vector3 v, float w)
    {
        return add(v.x, v.y, v.z, w);
    }

    public Vector4 add(float x, Vector3 v)
    {
        return add(x, v.x, v.y, v.z);
    }

    public Vector4 add(Vector2 v, float z, float w)
    {
        return add(v.x, v.y, z, w);
    }

    public Vector4 add(Vector2 v1, Vector2 v2)
    {
        return add(v1.x, v1.y, v2.x, v2.y);
    }

    public Vector4 add(float x, float y, Vector2 v)
    {
        return add(x, y, v.x, v.y);
    }

    public Vector4 subtract(Vector4 v)
    {
        return add(-v.x, -v.y, -v.z, -v.w);
    }

    public Vector4 subtract(Vector3 v, float w)
    {
        return subtract(v.x, v.y, v.z, w);
    }

    public Vector4 subtract(float x, float y, float z, float w)
    {
        return add(-x, -y, -z, -w);
    }

    public Vector4 subtract(float x, Vector3 v)
    {
        return subtract(x, v.x, v.y, v.z);
    }

    public Vector4 subtract(Vector2 v, float z, float w)
    {
        return subtract(v.x, v.y, z, w);
    }

    public Vector4 subtract(Vector2 v1, Vector2 v2)
    {
        return subtract(v1.x, v1.y, v2.x, v2.y);
    }

    public Vector4 subtract(float x, float y, Vector2 v)
    {
        return subtract(x, y, v.x, v.y);
    }

    public float dot(Vector4 v)
    {
        return x * v.x + y * v.y + z * v.z + w * v.w;
    }

    public Vector4 normalize()
    {
        float l = length();

        if (l == 0 || l == 1)
            return this;

        return set(x / l, y / l, z / l, w / l);
    }

    public Vector4 normalize3()
    {
        float l = (float) Math.sqrt(x * x + y * y + z * z);

        if (l == 0 || l == 1)
            return this;

        return set(x / l, y / l, z / l, w / l);
    }

    public float length()
    {
        return (float) Math.sqrt(lengthSquared());
    }

    public float lengthSquared()
    {
        return x * x + y * y + z * z + w * w;
    }

    public Vector4 negate()
    {
        return set(-x, -y, -z, -w);
    }

    public Vector4 multiply(Vector4 v)
    {
        return scale(v.x, v.y, v.z, v.w);
    }

    public Vector4 scale(float sx, float sy, float sz, float sw)
    {
        return set(x * sx, y * sy, z * sz, w * sw);
    }

    public Vector4 lerp(Vector4 target, float alpha)
    {
        Vector4 temp = Vector4.REUSABLE_STACK.pop();
        scale(1f - alpha).add(temp.set(target).scale(alpha));
        Vector4.REUSABLE_STACK.push(temp);

        return this;
    }

    public Vector4 add(Vector4 v)
    {
        return add(v.x, v.y, v.z, v.w);
    }

    public Vector4 scale(float s)
    {
        return scale(s, s, s, s);
    }

    public Vector4 set(Vector4 v)
    {
        return set(v.x, v.y, v.z, v.w);
    }

    public Vector4 set(float v)
    {
        return set(v, v, v, v);
    }

    public Vector4 set(Vector2 v, float z, float w)
    {
        return set(v.x, v.y, z, w);
    }

    public Vector4 set(float x, Vector2 v, float w)
    {
        return set(x, v.x, v.y, w);
    }

    public Vector4 set(float x, float y, Vector2 v)
    {
        return set(x, y, v.x, v.y);
    }

    public Vector4 set(Vector3 v, float w)
    {
        return set(v.x, v.y, v.z, w);
    }

    public Vector4 set(float x, Vector3 v)
    {
        return set(x, v.x, v.y, v.z);
    }

    @Override
    public String toString()
    {
        return "[" + x + ", " + y + ", " + z + ", " + w + "]";
    }

    public Vector4 set(Color color)
    {
        return set(color.r, color.g, color.b, color.a);
    }
}
