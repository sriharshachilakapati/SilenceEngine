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

import com.shc.silenceengine.utils.ReusableStack;

/**
 * @author Sri Harsha Chilakapati
 */
public class Quaternion
{
    public static final ReusableStack<Quaternion> REUSABLE_STACK = new ReusableStack<>(Quaternion.class);

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

    public Quaternion set(float x, float y, float z, float w)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;

        return this;
    }

    public Quaternion(Vector3 axis, float angle)
    {
        set(axis, angle);
    }

    public Quaternion set(Vector3 axis, float angle)
    {
        angle = (float) Math.toRadians(angle) * 0.5f;
        axis = axis.normalize();

        float sinAngle = (float) Math.sin(angle);
        float cosAngle = (float) Math.cos(angle);

        x = axis.x * sinAngle;
        y = axis.y * sinAngle;
        z = axis.z * sinAngle;
        w = cosAngle;

        return this;
    }

    public Quaternion(float pitch, float yaw, float roll)
    {
        set(pitch, yaw, roll);
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
        return new Quaternion(this.x + x, this.y + y, this.z + z, this.w + w);
    }

    public Quaternion addSelf(Quaternion q)
    {
        return addSelf(q.x, q.y, q.z, q.w);
    }

    public Quaternion addSelf(float x, float y, float z, float w)
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

    public Quaternion subtractSelf(Quaternion q)
    {
        return subtractSelf(q.x, q.y, q.z, q.w);
    }

    public Quaternion subtractSelf(float x, float y, float z, float w)
    {
        return addSelf(-x, -y, -z, -w);
    }

    public Quaternion normalize()
    {
        float length = length();

        if (length == 0 || length == 1)
            return copy();

        return new Quaternion(x / length, y / length, z / length, w / length);
    }

    public Quaternion copy()
    {
        return new Quaternion(x, y, z, w);
    }

    public float length()
    {
        return (float) Math.sqrt(lengthSquared());
    }

    public float lengthSquared()
    {
        return x * x + y * y + z * z + w * w;
    }

    public Quaternion conjugate()
    {
        return new Quaternion(-x, -y, -z, w);
    }

    public Quaternion multiply(Quaternion q)
    {
        float nx = w * q.x + x * q.w + y * q.z - z * q.y;
        float ny = w * q.y + y * q.w + z * q.x - x * q.z;
        float nz = w * q.z + z * q.w + x * q.y - y * q.x;
        float nw = w * q.w - x * q.x - y * q.y - z * q.z;

        return new Quaternion(nx, ny, nz, nw).normalizeSelf();
    }

    public Quaternion normalizeSelf()
    {
        float length = length();

        if (length == 0 || length == 1)
            return this;

        return set(x / length, y / length, z / length, w / length);
    }

    public Quaternion multiplySelf(Quaternion q)
    {
        float nx = w * q.x + x * q.w + y * q.z - z * q.y;
        float ny = w * q.y + y * q.w + z * q.x - x * q.z;
        float nz = w * q.z + z * q.w + x * q.y - y * q.x;
        float nw = w * q.w - x * q.x - y * q.y - z * q.z;

        return set(nx, ny, nz, nw).normalizeSelf();
    }

    public Vector3 multiply(Vector3 v)
    {
        return multiply(v, new Vector3());
    }

    public Vector3 multiply(Vector3 v, Vector3 dest)
    {
        Vector3 temp = Vector3.REUSABLE_STACK.pop();

        Quaternion temp1 = Quaternion.REUSABLE_STACK.pop();
        Quaternion temp2 = Quaternion.REUSABLE_STACK.pop();
        Quaternion temp3 = Quaternion.REUSABLE_STACK.pop();

        float length = v.length();
        v = temp.set(v).normalizeSelf();

        Quaternion q1 = temp1.set(this).conjugateSelf().normalizeSelf();
        Quaternion qv = temp2.set(v.x, v.y, v.z, 1);
        Quaternion q = this;

        Quaternion res = temp3.set(q).normalizeSelf().multiplySelf(qv.multiplySelf(q1));

        dest.x = res.x;
        dest.y = res.y;
        dest.z = res.z;

        Vector3.REUSABLE_STACK.push(temp);

        Quaternion.REUSABLE_STACK.push(temp1);
        Quaternion.REUSABLE_STACK.push(temp2);
        Quaternion.REUSABLE_STACK.push(temp3);

        return dest.normalizeSelf().scaleSelf(length);
    }

    public Quaternion invert()
    {
        return copy().invertSelf();
    }

    public Quaternion invertSelf()
    {
        float norm = (x * x + y * y + z * z + w * w);

        x = +x / norm;
        y = -y / norm;
        z = -z / norm;
        w = -w / norm;

        return this;
    }

    public Quaternion conjugateSelf()
    {
        return set(-x, -y, -z, w);
    }

    public Quaternion lerp(Quaternion target, float alpha)
    {
        return copy().lerpSelf(target, alpha);
    }

    public Quaternion lerpSelf(Quaternion target, float alpha)
    {
        Vector4 temp1 = Vector4.REUSABLE_STACK.pop();
        Vector4 temp2 = Vector4.REUSABLE_STACK.pop();

        Vector4 start = temp1.set(x, y, z, w);
        Vector4 end = temp2.set(target.x, target.y, target.z, target.w);
        Vector4 lerp = start.lerpSelf(end, alpha).normalizeSelf();

        set(lerp.x, lerp.y, lerp.z, lerp.w);

        Vector4.REUSABLE_STACK.push(temp1);
        Vector4.REUSABLE_STACK.push(temp2);

        return this;
    }

    public Quaternion slerp(Quaternion target, float alpha)
    {
        return copy().slerpSelf(target, alpha);
    }

    public Quaternion slerpSelf(Quaternion target, float alpha)
    {
        final float dot = dot(target);
        float scale1, scale2;

        if ((1 - dot) > 0.1)
            return lerpSelf(target, alpha);

        scale1 = 1f - alpha;
        scale2 = alpha;

        if (dot < 0.f)
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

    public float getZ()
    {
        return z;
    }

    public void setZ(float z)
    {
        this.z = z;
    }

    public float getW()
    {
        return w;
    }

    public void setW(float w)
    {
        this.w = w;
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
