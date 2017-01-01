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

package com.shc.silenceengine.math;

import com.shc.silenceengine.utils.MathUtils;
import com.shc.silenceengine.utils.ReusableStack;

/**
 * @author Sri Harsha Chilakapati
 */
public class Quaternion
{
    public static final ReusableStack<Quaternion> REUSABLE_STACK = new ReusableStack<>(Quaternion::new);

    public float x;
    public float y;
    public float z;
    public float w;

    public Quaternion()
    {
        this(0, 0, 0, 1);
    }

    public Quaternion(float x, float y, float z, float w)
    {
        set(x, y, z, w);
    }

    public Quaternion(Vector3 axis, float angle)
    {
        set(axis, angle);
    }

    public Quaternion(float pitch, float yaw, float roll)
    {
        set(pitch, yaw, roll);
    }

    @Override
    public int hashCode()
    {
        int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
        result = 31 * result + (z != +0.0f ? Float.floatToIntBits(z) : 0);
        result = 31 * result + (w != +0.0f ? Float.floatToIntBits(w) : 0);
        return result;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Quaternion that = (Quaternion) o;

        return Float.compare(that.x, x) == 0 &&
               Float.compare(that.y, y) == 0 &&
               Float.compare(that.z, z) == 0 &&
               Float.compare(that.w, w) == 0;
    }

    public Quaternion set(float x, float y, float z, float w)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;

        return this;
    }

    public Quaternion set(Vector3 axis, float angle)
    {
        angle = angle / 2;
        axis = axis.normalize();

        float sinAngle = MathUtils.sin(angle);
        float cosAngle = MathUtils.cos(angle);

        x = axis.x * sinAngle;
        y = axis.y * sinAngle;
        z = axis.z * sinAngle;
        w = cosAngle;

        return this;
    }

    public Quaternion set(float pitch, float yaw, float roll)
    {
        pitch = (float) Math.toRadians(pitch) * 0.5f;
        yaw = (float) Math.toRadians(yaw) * 0.5f;
        roll = (float) Math.toRadians(roll) * 0.5f;

        float sinP = (float) Math.sin(pitch);
        float sinY = (float) Math.sin(yaw);
        float sinR = (float) Math.sin(roll);
        float cosP = (float) Math.cos(pitch);
        float cosY = (float) Math.cos(yaw);
        float cosR = (float) Math.cos(roll);

        x = sinP * cosY * cosR - cosP * sinY * sinR;
        y = cosP * sinY * cosR + sinP * cosY * sinR;
        z = cosP * cosY * sinR - sinP * sinY * cosR;
        w = cosP * cosY * cosR + sinP * sinY * sinR;

        return this;
    }

    public Quaternion add(Quaternion q)
    {
        return add(q.x, q.y, q.z, q.w);
    }

    public Quaternion add(float x, float y, float z, float w)
    {
        return set(this.x + x, this.y + y, this.z + z, this.w + w);
    }

    public Quaternion subtract(Quaternion q)
    {
        return subtract(q.x, q.y, q.z, q.w);
    }

    public Quaternion subtract(float x, float y, float z, float w)
    {
        return add(-x, -y, -z, -w);
    }

    public float length()
    {
        return (float) Math.sqrt(lengthSquared());
    }

    public Quaternion copy()
    {
        return new Quaternion(x, y, z, w);
    }

    public float lengthSquared()
    {
        return x * x + y * y + z * z + w * w;
    }

    public Quaternion normalize()
    {
        float length = length();

        if (length == 0 || length == 1)
            return this;

        return set(x / length, y / length, z / length, w / length);
    }

    public Quaternion multiply(Quaternion q)
    {
        float nx = w * q.x + x * q.w + y * q.z - z * q.y;
        float ny = w * q.y + y * q.w + z * q.x - x * q.z;
        float nz = w * q.z + z * q.w + x * q.y - y * q.x;
        float nw = w * q.w - x * q.x - y * q.y - z * q.z;

        return set(nx, ny, nz, nw).normalize();
    }

    public Vector3 multiplyInverse(Vector3 v, Vector3 dest)
    {
        invert().multiply(v, dest);
        invert();

        return dest;
    }

    public Vector3 multiply(Vector3 v, Vector3 dest)
    {
        Vector3 temp = Vector3.REUSABLE_STACK.pop();

        Quaternion temp1 = Quaternion.REUSABLE_STACK.pop();
        Quaternion temp2 = Quaternion.REUSABLE_STACK.pop();
        Quaternion temp3 = Quaternion.REUSABLE_STACK.pop();

        float length = v.length();
        v = temp.set(v).normalize();

        Quaternion q1 = temp1.set(this).conjugate().normalize();
        Quaternion qv = temp2.set(v.x, v.y, v.z, 0);
        Quaternion q = this;

        Quaternion res = temp3.set(q).normalize().multiply(qv.multiply(q1).normalize());

        dest.x = res.x;
        dest.y = res.y;
        dest.z = res.z;

        Vector3.REUSABLE_STACK.push(temp);

        Quaternion.REUSABLE_STACK.push(temp1);
        Quaternion.REUSABLE_STACK.push(temp2);
        Quaternion.REUSABLE_STACK.push(temp3);

        return dest.normalize().scale(length);
    }

    public Quaternion invert()
    {
        float norm = lengthSquared();

        if (norm == 0)
            return conjugate();

        x = -x / norm;
        y = -y / norm;
        z = -z / norm;
        w = +w / norm;

        return this;
    }

    public Quaternion conjugate()
    {
        return set(-x, -y, -z, w);
    }

    public Quaternion lerp(Quaternion target, float alpha)
    {
        Vector4 temp1 = Vector4.REUSABLE_STACK.pop();
        Vector4 temp2 = Vector4.REUSABLE_STACK.pop();

        Vector4 start = temp1.set(x, y, z, w);
        Vector4 end = temp2.set(target.x, target.y, target.z, target.w);
        Vector4 lerp = start.lerp(end, alpha).normalize();

        set(lerp.x, lerp.y, lerp.z, lerp.w);

        Vector4.REUSABLE_STACK.push(temp1);
        Vector4.REUSABLE_STACK.push(temp2);

        return this;
    }

    public Quaternion slerp(Quaternion target, float alpha)
    {
        final float dot = dot(target);
        float scale1, scale2;

        if ((1 - dot) > 0.1)
        {
            Quaternion temp = REUSABLE_STACK.pop();

            if (dot < 0.0f)
                temp.set(-target.x, -target.y, -target.z, -target.w);
            else
                temp.set(target);

            lerp(temp, alpha);
            REUSABLE_STACK.push(temp);

            return this;
        }

        scale1 = 1f - alpha;
        scale2 = alpha;

        if (dot < 0.0f)
            scale2 = -scale2;

        x = (scale1 * x) + (scale2 * target.x);
        y = (scale1 * y) + (scale2 * target.y);
        z = (scale1 * z) + (scale2 * target.z);
        w = (scale1 * w) + (scale2 * target.w);

        return this;
    }

    public float dot(Quaternion q)
    {
        return x * q.x + y * q.y + z * q.z + w * q.w;
    }

    public Quaternion set()
    {
        return set(0, 0, 0, 1);
    }

    public Quaternion set(Quaternion q)
    {
        return set(q.x, q.y, q.z, q.w);
    }
}
