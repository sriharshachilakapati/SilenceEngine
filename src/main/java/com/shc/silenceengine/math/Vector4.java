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
public class Vector4
{
    public static final Vector4 ZERO = new Vector4(0, 0, 0, 0);

    public static final ReusableStack<Vector4> REUSABLE_STACK = new ReusableStack<>(Vector4.class);

    public float x, y, z, w;

    public Vector4()
    {
        this(0, 0, 0, 0);
    }

    public Vector4(float x, float y, float z, float w)
    {
        set(x, y, z, w);
    }

    public Vector4 set(float x, float y, float z, float w)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;

        return this;
    }

    public Vector4(float v)
    {
        this(v, v, v, v);
    }

    public Vector4(Vector2 v, float z, float w)
    {
        this(v.getX(), v.getY(), z, w);
    }

    public Vector4(float x, Vector2 v, float w)
    {
        this(x, v.getX(), v.getY(), w);
    }

    public Vector4(float x, float y, Vector2 v)
    {
        this(x, y, v.getX(), v.getY());
    }

    public Vector4(Vector3 v, float w)
    {
        this(v.getX(), v.getY(), v.getZ(), w);
    }

    public Vector4(float x, Vector3 v)
    {
        this(x, v.getX(), v.getY(), v.getZ());
    }

    public Vector4(Vector4 v)
    {
        this(v.x, v.y, v.z, v.w);
    }

    public Vector4 add(Vector4 v)
    {
        return add(v.x, v.y, v.z, v.w);
    }

    public Vector4 add(float x, float y, float z, float w)
    {
        return copy().addSelf(x, y, z, w);
    }

    public Vector4 addSelf(float x, float y, float z, float w)
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

    public Vector4 addSelf(Vector3 v, float w)
    {
        return addSelf(v.x, v.y, v.z, w);
    }

    public Vector4 add(float x, Vector3 v)
    {
        return add(x, v.x, v.y, v.z);
    }

    public Vector4 addSelf(float x, Vector3 v)
    {
        return addSelf(x, v.x, v.y, v.z);
    }

    public Vector4 add(Vector2 v, float z, float w)
    {
        return add(v.x, v.y, z, w);
    }

    public Vector4 addSelf(Vector2 v, float z, float w)
    {
        return addSelf(v.x, v.y, z, w);
    }

    public Vector4 add(Vector2 v1, Vector2 v2)
    {
        return add(v1.x, v1.y, v2.x, v2.y);
    }

    public Vector4 addSelf(Vector2 v1, Vector2 v2)
    {
        return addSelf(v1.x, v1.y, v2.x, v2.y);
    }

    public Vector4 add(float x, float y, Vector2 v)
    {
        return add(x, y, v.x, v.y);
    }

    public Vector4 addSelf(float x, float y, Vector2 v)
    {
        return addSelf(x, y, v.x, v.y);
    }

    public Vector4 subtract(Vector4 v)
    {
        return add(-v.x, -v.y, -v.z, -v.w);
    }

    public Vector4 subtractSelf(Vector4 v)
    {
        return addSelf(-v.x, -v.y, -v.z, -v.w);
    }

    public Vector4 subtract(Vector3 v, float w)
    {
        return subtract(v.x, v.y, v.z, w);
    }

    public Vector4 subtract(float x, float y, float z, float w)
    {
        return add(-x, -y, -z, -w);
    }

    public Vector4 subtractSelf(Vector3 v, float w)
    {
        return subtractSelf(v.x, v.y, v.z, w);
    }

    public Vector4 subtractSelf(float x, float y, float z, float w)
    {
        return addSelf(-x, -y, -z, -w);
    }

    public Vector4 subtract(float x, Vector3 v)
    {
        return subtract(x, v.x, v.y, v.z);
    }

    public Vector4 subtractSelf(float x, Vector3 v)
    {
        return subtractSelf(x, v.x, v.y, v.z);
    }

    public Vector4 subtract(Vector2 v, float z, float w)
    {
        return subtract(v.x, v.y, z, w);
    }

    public Vector4 subtractSelf(Vector2 v, float z, float w)
    {
        return subtractSelf(v.x, v.y, z, w);
    }

    public Vector4 subtract(Vector2 v1, Vector2 v2)
    {
        return subtract(v1.x, v1.y, v2.x, v2.y);
    }

    public Vector4 subtractSelf(Vector2 v1, Vector2 v2)
    {
        return subtractSelf(v1.x, v1.y, v2.x, v2.y);
    }

    public Vector4 subtract(float x, float y, Vector2 v)
    {
        return subtract(x, y, v.x, v.y);
    }

    public Vector4 subtractSelf(float x, float y, Vector2 v)
    {
        return subtractSelf(x, y, v.x, v.y);
    }

    public Vector4 scale(float s)
    {
        return scale(s, s, s, s);
    }

    public Vector4 scale(float sx, float sy, float sz, float sw)
    {
        return new Vector4(x * sx, y * sy, z * sz, w * sw);
    }

    public float dot(Vector4 v)
    {
        return x * v.x + y * v.y + z * v.z + w * v.w;
    }

    public Vector4 normalize()
    {
        return copy().normalizeSelf();
    }

    public Vector4 normalizeSelf()
    {
        float l = length();

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
        return new Vector4(-x, -y, -z, -w);
    }

    public Vector4 negateSelf()
    {
        return set(-x, -y, -z, -w);
    }

    public Vector4 multiply(Vector4 v)
    {
        return scale(v.x, v.y, v.z, v.w);
    }

    public Vector4 multiplySelf(Vector4 v)
    {
        return scaleSelf(v.x, v.y, v.z, v.w);
    }

    public Vector4 scaleSelf(float sx, float sy, float sz, float sw)
    {
        return set(x * sx, y * sy, z * sz, w * sw);
    }

    public Vector4 lerp(Vector4 target, float alpha)
    {
        return copy().lerpSelf(target, alpha);
    }

    public Vector4 lerpSelf(Vector4 target, float alpha)
    {
        Vector4 temp = Vector4.REUSABLE_STACK.pop();
        scaleSelf(1f - alpha).addSelf(temp.set(target).scaleSelf(alpha));
        Vector4.REUSABLE_STACK.push(temp);

        return this;
    }

    public Vector4 addSelf(Vector4 v)
    {
        return addSelf(v.x, v.y, v.z, v.w);
    }

    public Vector4 scaleSelf(float s)
    {
        return scaleSelf(s, s, s, s);
    }

    public Vector4 set(Vector4 v)
    {
        return set(v.x, v.y, v.z, v.w);
    }

    public float getX()
    {
        return x;
    }

    public Vector4 setX(float x)
    {
        this.x = x;
        return this;
    }

    public float getY()
    {
        return y;
    }

    public Vector4 setY(float y)
    {
        this.y = y;
        return this;
    }

    public float getZ()
    {
        return z;
    }

    public Vector4 setZ(float z)
    {
        this.z = z;
        return this;
    }

    public float getW()
    {
        return w;
    }

    public Vector4 setW(float w)
    {
        this.w = w;
        return this;
    }

    /* Swizzling */
    public float getR()
    {
        return x;
    }

    public Vector4 setR(float r)
    {
        x = r;
        return this;
    }

    public float getG()
    {
        return y;
    }

    public Vector4 setG(float g)
    {
        y = g;
        return this;
    }

    public float getB()
    {
        return z;
    }

    public Vector4 setB(float b)
    {
        z = b;
        return this;
    }

    public float getA()
    {
        return w;
    }

    public Vector4 setA(float a)
    {
        w = a;
        return this;
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

    public Vector2 getXX()
    {
        return new Vector2(x, x);
    }

    public Vector2 getXY()
    {
        return new Vector2(x, y);
    }

    public Vector2 getXZ()
    {
        return new Vector2(x, z);
    }

    public Vector2 getXW()
    {
        return new Vector2(x, w);
    }

    public Vector2 getYX()
    {
        return new Vector2(y, x);
    }

    public Vector2 getYY()
    {
        return new Vector2(y, y);
    }

    public Vector2 getYZ()
    {
        return new Vector2(y, z);
    }

    public Vector2 getYW()
    {
        return new Vector2(y, w);
    }

    public Vector2 getZX()
    {
        return new Vector2(z, x);
    }

    public Vector2 getZY()
    {
        return new Vector2(z, y);
    }

    public Vector2 getZZ()
    {
        return new Vector2(z, z);
    }

    public Vector2 getZW()
    {
        return new Vector2(z, w);
    }

    public Vector2 getWX()
    {
        return new Vector2(w, x);
    }

    public Vector2 getWY()
    {
        return new Vector2(w, y);
    }

    public Vector2 getWZ()
    {
        return new Vector2(w, z);
    }

    public Vector2 getWW()
    {
        return new Vector2(w, w);
    }

    public Vector3 getXXX()
    {
        return new Vector3(x, x, x);
    }

    public Vector3 getXXY()
    {
        return new Vector3(x, x, y);
    }

    public Vector3 getXXZ()
    {
        return new Vector3(x, x, z);
    }

    public Vector3 getXXW()
    {
        return new Vector3(x, x, w);
    }

    public Vector3 getXYX()
    {
        return new Vector3(x, y, x);
    }

    public Vector3 getXYY()
    {
        return new Vector3(x, y, y);
    }

    public Vector3 getXYZ()
    {
        return new Vector3(x, y, z);
    }

    public Vector3 getXYW()
    {
        return new Vector3(x, y, w);
    }

    public Vector3 getXZX()
    {
        return new Vector3(x, z, x);
    }

    public Vector3 getXZY()
    {
        return new Vector3(x, z, y);
    }

    public Vector3 getXZZ()
    {
        return new Vector3(x, z, z);
    }

    public Vector3 getXZW()
    {
        return new Vector3(x, z, w);
    }

    public Vector3 getXWX()
    {
        return new Vector3(x, w, x);
    }

    public Vector3 getXWY()
    {
        return new Vector3(x, w, y);
    }

    public Vector3 getXWZ()
    {
        return new Vector3(x, w, z);
    }

    public Vector3 getXWW()
    {
        return new Vector3(x, w, w);
    }

    public Vector3 getYXX()
    {
        return new Vector3(y, x, x);
    }

    public Vector3 getYXY()
    {
        return new Vector3(y, x, y);
    }

    public Vector3 getYXZ()
    {
        return new Vector3(y, x, z);
    }

    public Vector3 getYXW()
    {
        return new Vector3(y, x, w);
    }

    public Vector3 getYYX()
    {
        return new Vector3(y, y, x);
    }

    public Vector3 getYYY()
    {
        return new Vector3(y, y, y);
    }

    public Vector3 getYYZ()
    {
        return new Vector3(y, y, z);
    }

    public Vector3 getYYW()
    {
        return new Vector3(y, y, w);
    }

    public Vector3 getYZX()
    {
        return new Vector3(y, z, x);
    }

    public Vector3 getYZY()
    {
        return new Vector3(y, z, y);
    }

    public Vector3 getYZZ()
    {
        return new Vector3(y, z, z);
    }

    public Vector3 getYZW()
    {
        return new Vector3(y, z, w);
    }

    public Vector3 getYWX()
    {
        return new Vector3(y, w, x);
    }

    public Vector3 getYWY()
    {
        return new Vector3(y, w, y);
    }

    public Vector3 getYWZ()
    {
        return new Vector3(y, w, z);
    }

    public Vector3 getYWW()
    {
        return new Vector3(y, w, w);
    }

    public Vector3 getZXX()
    {
        return new Vector3(z, x, x);
    }

    public Vector3 getZXY()
    {
        return new Vector3(z, x, y);
    }

    public Vector3 getZXZ()
    {
        return new Vector3(z, x, z);
    }

    public Vector3 getZXW()
    {
        return new Vector3(z, x, w);
    }

    public Vector3 getZYX()
    {
        return new Vector3(z, y, x);
    }

    public Vector3 getZYY()
    {
        return new Vector3(z, y, y);
    }

    public Vector3 getZYZ()
    {
        return new Vector3(z, y, z);
    }

    public Vector3 getZYW()
    {
        return new Vector3(z, y, w);
    }

    public Vector3 getZZX()
    {
        return new Vector3(z, z, x);
    }

    public Vector3 getZZY()
    {
        return new Vector3(z, z, y);
    }

    public Vector3 getZZZ()
    {
        return new Vector3(z, z, z);
    }

    public Vector3 getZZW()
    {
        return new Vector3(z, z, w);
    }

    public Vector3 getZWX()
    {
        return new Vector3(z, w, x);
    }

    public Vector3 getZWY()
    {
        return new Vector3(z, w, y);
    }

    public Vector3 getZWZ()
    {
        return new Vector3(z, w, z);
    }

    public Vector3 getZWW()
    {
        return new Vector3(z, w, w);
    }

    public Vector3 getWXX()
    {
        return new Vector3(w, x, x);
    }

    public Vector3 getWXY()
    {
        return new Vector3(w, x, y);
    }

    public Vector3 getWXZ()
    {
        return new Vector3(w, x, z);
    }

    public Vector3 getWXW()
    {
        return new Vector3(w, x, w);
    }

    public Vector3 getWYX()
    {
        return new Vector3(w, y, x);
    }

    public Vector3 getWYY()
    {
        return new Vector3(w, y, y);
    }

    public Vector3 getWYZ()
    {
        return new Vector3(w, y, z);
    }

    public Vector3 getWYW()
    {
        return new Vector3(w, y, w);
    }

    public Vector3 getWZX()
    {
        return new Vector3(w, z, x);
    }

    public Vector3 getWZY()
    {
        return new Vector3(w, z, y);
    }

    public Vector3 getWZZ()
    {
        return new Vector3(w, z, z);
    }

    public Vector3 getWZW()
    {
        return new Vector3(w, z, w);
    }

    public Vector3 getWWX()
    {
        return new Vector3(w, w, x);
    }

    public Vector3 getWWY()
    {
        return new Vector3(w, w, y);
    }

    public Vector3 getWWZ()
    {
        return new Vector3(w, w, z);
    }

    public Vector3 getWWW()
    {
        return new Vector3(w, w, w);
    }

    public Vector2 getRR()
    {
        return new Vector2(x, x);
    }

    public Vector2 getRG()
    {
        return new Vector2(x, y);
    }

    public Vector2 getRB()
    {
        return new Vector2(x, z);
    }

    public Vector2 getRA()
    {
        return new Vector2(x, w);
    }

    public Vector2 getGR()
    {
        return new Vector2(y, x);
    }

    public Vector2 getGG()
    {
        return new Vector2(y, y);
    }

    public Vector2 getGB()
    {
        return new Vector2(y, z);
    }

    public Vector2 getGA()
    {
        return new Vector2(y, w);
    }

    public Vector2 getBR()
    {
        return new Vector2(z, x);
    }

    public Vector2 getBG()
    {
        return new Vector2(z, y);
    }

    public Vector2 getBB()
    {
        return new Vector2(z, z);
    }

    public Vector2 getBA()
    {
        return new Vector2(z, w);
    }

    public Vector2 getAR()
    {
        return new Vector2(w, x);
    }

    public Vector2 getAG()
    {
        return new Vector2(w, y);
    }

    public Vector2 getAB()
    {
        return new Vector2(w, z);
    }

    public Vector2 getAA()
    {
        return new Vector2(w, w);
    }

    public Vector3 getRRR()
    {
        return new Vector3(x, x, x);
    }

    public Vector3 getRRG()
    {
        return new Vector3(x, x, y);
    }

    public Vector3 getRRB()
    {
        return new Vector3(x, x, z);
    }

    public Vector3 getRRA()
    {
        return new Vector3(x, x, w);
    }

    public Vector3 getRGR()
    {
        return new Vector3(x, y, x);
    }

    public Vector3 getRGG()
    {
        return new Vector3(x, y, y);
    }

    public Vector3 getRGB()
    {
        return new Vector3(x, y, z);
    }

    public Vector3 getRGA()
    {
        return new Vector3(x, y, w);
    }

    public Vector3 getRBR()
    {
        return new Vector3(x, z, x);
    }

    public Vector3 getRBG()
    {
        return new Vector3(x, z, y);
    }

    public Vector3 getRBB()
    {
        return new Vector3(x, z, z);
    }

    public Vector3 getRBA()
    {
        return new Vector3(x, z, w);
    }

    public Vector3 getRAR()
    {
        return new Vector3(x, w, x);
    }

    public Vector3 getRAG()
    {
        return new Vector3(x, w, y);
    }

    public Vector3 getRAB()
    {
        return new Vector3(x, w, z);
    }

    public Vector3 getRAA()
    {
        return new Vector3(x, w, w);
    }

    public Vector3 getGRR()
    {
        return new Vector3(y, x, x);
    }

    public Vector3 getGRG()
    {
        return new Vector3(y, x, y);
    }

    public Vector3 getGRB()
    {
        return new Vector3(y, x, z);
    }

    public Vector3 getGRA()
    {
        return new Vector3(y, x, w);
    }

    public Vector3 getGGR()
    {
        return new Vector3(y, y, x);
    }

    public Vector3 getGGG()
    {
        return new Vector3(y, y, y);
    }

    public Vector3 getGGB()
    {
        return new Vector3(y, y, z);
    }

    public Vector3 getGGA()
    {
        return new Vector3(y, y, w);
    }

    public Vector3 getGBR()
    {
        return new Vector3(y, z, x);
    }

    public Vector3 getGBG()
    {
        return new Vector3(y, z, y);
    }

    public Vector3 getGBB()
    {
        return new Vector3(y, z, z);
    }

    public Vector3 getGBA()
    {
        return new Vector3(y, z, w);
    }

    public Vector3 getGAR()
    {
        return new Vector3(y, w, x);
    }

    public Vector3 getGAG()
    {
        return new Vector3(y, w, y);
    }

    public Vector3 getGAB()
    {
        return new Vector3(y, w, z);
    }

    public Vector3 getGAA()
    {
        return new Vector3(y, w, w);
    }

    public Vector3 getBRR()
    {
        return new Vector3(z, x, x);
    }

    public Vector3 getBRG()
    {
        return new Vector3(z, x, y);
    }

    public Vector3 getBRB()
    {
        return new Vector3(z, x, z);
    }

    public Vector3 getBRA()
    {
        return new Vector3(z, x, w);
    }

    public Vector3 getBGR()
    {
        return new Vector3(z, y, x);
    }

    public Vector3 getBGG()
    {
        return new Vector3(z, y, y);
    }

    public Vector3 getBGB()
    {
        return new Vector3(z, y, z);
    }

    public Vector3 getBGA()
    {
        return new Vector3(z, y, w);
    }

    public Vector3 getBBR()
    {
        return new Vector3(z, z, x);
    }

    public Vector3 getBBG()
    {
        return new Vector3(z, z, y);
    }

    public Vector3 getBBB()
    {
        return new Vector3(z, z, z);
    }

    public Vector3 getBBA()
    {
        return new Vector3(z, z, w);
    }

    public Vector3 getBAR()
    {
        return new Vector3(z, w, x);
    }

    public Vector3 getBAG()
    {
        return new Vector3(z, w, y);
    }

    public Vector3 getBAB()
    {
        return new Vector3(z, w, z);
    }

    public Vector3 getBAA()
    {
        return new Vector3(z, w, w);
    }

    public Vector3 getARR()
    {
        return new Vector3(w, x, x);
    }

    public Vector3 getARG()
    {
        return new Vector3(w, x, y);
    }

    public Vector3 getARB()
    {
        return new Vector3(w, x, z);
    }

    public Vector3 getARA()
    {
        return new Vector3(w, x, w);
    }

    public Vector3 getAGR()
    {
        return new Vector3(w, y, x);
    }

    public Vector3 getAGG()
    {
        return new Vector3(w, y, y);
    }

    public Vector3 getAGB()
    {
        return new Vector3(w, y, z);
    }

    public Vector3 getAGA()
    {
        return new Vector3(w, y, w);
    }

    public Vector3 getABR()
    {
        return new Vector3(w, z, x);
    }

    public Vector3 getABG()
    {
        return new Vector3(w, z, y);
    }

    public Vector3 getABB()
    {
        return new Vector3(w, z, z);
    }

    public Vector3 getABA()
    {
        return new Vector3(w, z, w);
    }

    public Vector3 getAAR()
    {
        return new Vector3(w, w, x);
    }

    public Vector3 getAAG()
    {
        return new Vector3(w, w, y);
    }

    public Vector3 getAAB()
    {
        return new Vector3(w, w, z);
    }

    public Vector3 getAAA()
    {
        return new Vector3(w, w, w);
    }

    @Override
    public String toString()
    {
        return "[" + x + ", " + y + ", " + z + ", " + w + "]";
    }
}
