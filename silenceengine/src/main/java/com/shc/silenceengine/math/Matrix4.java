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

import com.shc.silenceengine.io.DirectFloatBuffer;
import com.shc.silenceengine.utils.ReusableStack;

import java.util.Arrays;

/**
 * @author Sri Harsha Chilakapati
 */
public class Matrix4
{
    public static final ReusableStack<Matrix4> REUSABLE_STACK = new ReusableStack<>(Matrix4::new);

    public final float[][] m;

    public Matrix4(Vector4 c0, Vector4 c1, Vector4 c2, Vector4 c3)
    {
        this();

        m[0][0] = c0.x;
        m[1][0] = c1.x;
        m[2][0] = c2.x;
        m[3][0] = c3.x;
        m[0][1] = c0.y;
        m[1][1] = c1.y;
        m[2][1] = c2.y;
        m[3][1] = c3.y;
        m[0][2] = c0.z;
        m[1][2] = c1.z;
        m[2][2] = c2.z;
        m[3][2] = c3.z;
        m[0][3] = c0.w;
        m[1][3] = c1.w;
        m[2][3] = c2.w;
        m[3][3] = c3.w;
    }

    public Matrix4(Matrix3 m)
    {
        this();
        set(m);
    }

    public Matrix4()
    {
        m = new float[4][4];
        initIdentity();
    }

    public Matrix4(Matrix4 m)
    {
        this();
        set(m);
    }

    public Matrix4(float diagonal)
    {
        this();
        set(diagonal);
    }

    public Matrix4 set(Matrix3 m)
    {
        this.m[0][0] = m.m[0][0];
        this.m[1][0] = m.m[1][0];
        this.m[2][0] = m.m[2][0];
        this.m[0][1] = m.m[0][1];
        this.m[1][1] = m.m[1][1];
        this.m[2][1] = m.m[2][1];
        this.m[0][2] = m.m[0][2];
        this.m[1][2] = m.m[1][2];
        this.m[2][2] = m.m[2][2];

        return this;
    }

    public Matrix4 initIdentity()
    {
        return set(1);
    }

    public Matrix4 set(Matrix4 m)
    {
        this.m[0][0] = m.m[0][0];
        this.m[1][0] = m.m[1][0];
        this.m[2][0] = m.m[2][0];
        this.m[3][0] = m.m[3][0];
        this.m[0][1] = m.m[0][1];
        this.m[1][1] = m.m[1][1];
        this.m[2][1] = m.m[2][1];
        this.m[3][1] = m.m[3][1];
        this.m[0][2] = m.m[0][2];
        this.m[1][2] = m.m[1][2];
        this.m[2][2] = m.m[2][2];
        this.m[3][2] = m.m[3][2];
        this.m[0][3] = m.m[0][3];
        this.m[1][3] = m.m[1][3];
        this.m[2][3] = m.m[2][3];
        this.m[3][3] = m.m[3][3];

        return this;
    }

    public float get(int x, int y)
    {
        return m[x][y];
    }

    public Matrix4 set(float diagonal)
    {
        m[0][0] = diagonal;
        m[1][0] = 0;
        m[2][0] = 0;
        m[3][0] = 0;
        m[0][1] = 0;
        m[1][1] = diagonal;
        m[2][1] = 0;
        m[3][1] = 0;
        m[0][2] = 0;
        m[1][2] = 0;
        m[2][2] = diagonal;
        m[3][2] = 0;
        m[0][3] = 0;
        m[1][3] = 0;
        m[2][3] = 0;
        m[3][3] = diagonal;

        return this;
    }

    public Matrix4 add(Matrix4 m)
    {
        this.m[0][0] += m.m[0][0];
        this.m[1][0] += m.m[1][0];
        this.m[2][0] += m.m[2][0];
        this.m[3][0] += m.m[3][0];
        this.m[0][1] += m.m[0][1];
        this.m[1][1] += m.m[1][1];
        this.m[2][1] += m.m[2][1];
        this.m[3][1] += m.m[3][1];
        this.m[0][2] += m.m[0][2];
        this.m[1][2] += m.m[1][2];
        this.m[2][2] += m.m[2][2];
        this.m[3][2] += m.m[3][2];
        this.m[0][3] += m.m[0][3];
        this.m[1][3] += m.m[1][3];
        this.m[2][3] += m.m[2][3];
        this.m[3][3] += m.m[3][3];

        return this;
    }

    public Matrix4 subtract(Matrix4 m)
    {
        this.m[0][0] -= m.m[0][0];
        this.m[1][0] -= m.m[1][0];
        this.m[2][0] -= m.m[2][0];
        this.m[3][0] -= m.m[3][0];
        this.m[0][1] -= m.m[0][1];
        this.m[1][1] -= m.m[1][1];
        this.m[2][1] -= m.m[2][1];
        this.m[3][1] -= m.m[3][1];
        this.m[0][2] -= m.m[0][2];
        this.m[1][2] -= m.m[1][2];
        this.m[2][2] -= m.m[2][2];
        this.m[3][2] -= m.m[3][2];
        this.m[0][3] -= m.m[0][3];
        this.m[1][3] -= m.m[1][3];
        this.m[2][3] -= m.m[2][3];
        this.m[3][3] -= m.m[3][3];

        return this;
    }

    public Matrix4 multiply(Matrix4 m)
    {
        float m00, m01, m02, m03;
        float m10, m11, m12, m13;
        float m20, m21, m22, m23;
        float m30, m31, m32, m33;

        m00 = this.m[0][0] * m.m[0][0] + this.m[1][0] * m.m[0][1] + this.m[2][0] * m.m[0][2] + this.m[3][0] * m.m[0][3];
        m10 = this.m[0][0] * m.m[1][0] + this.m[1][0] * m.m[1][1] + this.m[2][0] * m.m[1][2] + this.m[3][0] * m.m[1][3];
        m20 = this.m[0][0] * m.m[2][0] + this.m[1][0] * m.m[2][1] + this.m[2][0] * m.m[2][2] + this.m[3][0] * m.m[2][3];
        m30 = this.m[0][0] * m.m[3][0] + this.m[1][0] * m.m[3][1] + this.m[2][0] * m.m[3][2] + this.m[3][0] * m.m[3][3];

        m01 = this.m[0][1] * m.m[0][0] + this.m[1][1] * m.m[0][1] + this.m[2][1] * m.m[0][2] + this.m[3][1] * m.m[0][3];
        m11 = this.m[0][1] * m.m[1][0] + this.m[1][1] * m.m[1][1] + this.m[2][1] * m.m[1][2] + this.m[3][1] * m.m[1][3];
        m21 = this.m[0][1] * m.m[2][0] + this.m[1][1] * m.m[2][1] + this.m[2][1] * m.m[2][2] + this.m[3][1] * m.m[2][3];
        m31 = this.m[0][1] * m.m[3][0] + this.m[1][1] * m.m[3][1] + this.m[2][1] * m.m[3][2] + this.m[3][1] * m.m[3][3];

        m02 = this.m[0][2] * m.m[0][0] + this.m[1][2] * m.m[0][1] + this.m[2][2] * m.m[0][2] + this.m[3][2] * m.m[0][3];
        m12 = this.m[0][2] * m.m[1][0] + this.m[1][2] * m.m[1][1] + this.m[2][2] * m.m[1][2] + this.m[3][2] * m.m[1][3];
        m22 = this.m[0][2] * m.m[2][0] + this.m[1][2] * m.m[2][1] + this.m[2][2] * m.m[2][2] + this.m[3][2] * m.m[2][3];
        m32 = this.m[0][2] * m.m[3][0] + this.m[1][2] * m.m[3][1] + this.m[2][2] * m.m[3][2] + this.m[3][2] * m.m[3][3];

        m03 = this.m[0][3] * m.m[0][0] + this.m[1][3] * m.m[0][1] + this.m[2][3] * m.m[0][2] + this.m[3][3] * m.m[0][3];
        m13 = this.m[0][3] * m.m[1][0] + this.m[1][3] * m.m[1][1] + this.m[2][3] * m.m[1][2] + this.m[3][3] * m.m[1][3];
        m23 = this.m[0][3] * m.m[2][0] + this.m[1][3] * m.m[2][1] + this.m[2][3] * m.m[2][2] + this.m[3][3] * m.m[2][3];
        m33 = this.m[0][3] * m.m[3][0] + this.m[1][3] * m.m[3][1] + this.m[2][3] * m.m[3][2] + this.m[3][3] * m.m[3][3];

        this.m[0][0] = m00;
        this.m[1][0] = m10;
        this.m[2][0] = m20;
        this.m[3][0] = m30;
        this.m[0][1] = m01;
        this.m[1][1] = m11;
        this.m[2][1] = m21;
        this.m[3][1] = m31;
        this.m[0][2] = m02;
        this.m[1][2] = m12;
        this.m[2][2] = m22;
        this.m[3][2] = m32;
        this.m[0][3] = m03;
        this.m[1][3] = m13;
        this.m[2][3] = m23;
        this.m[3][3] = m33;

        return this;
    }

    public Matrix4 initZero()
    {
        return set(0);
    }

    public Matrix4 set(int x, int j, float val)
    {
        m[x][j] = val;

        return this;
    }

    /**
     * Multiplies this matrix with a row matrix defined by the vector {@code v} from the right side, and stores the
     * result in {@code dest}.
     *
     * @param v    The vector to multiply this matrix with.
     * @param dest The vector to store the result.
     *
     * @return The destination vector, aka the result.
     */
    public Vector3 multiply(Vector3 v, Vector3 dest)
    {
        float X = v.x;
        float Y = v.y;
        float Z = v.z;
        float W = 1;

        float A = m[0][0], B = m[0][1], C = m[0][2], D = m[0][3];
        float E = m[1][0], F = m[1][1], G = m[1][2], H = m[1][3];
        float I = m[2][0], J = m[2][1], K = m[2][2], L = m[2][3];

        // /        \  /   \     /                       \
        // | a b c d | | x |     | a.x + b.y + c.z + d.w |
        // | e f g h | | y |     | e.x + f.y + g.z + h.w |
        // | i j k l | | z |  =  | i.x + j.y + k.z + l.w |
        // | m n o p | | w |     | m.x + n.y + o.z + p.w |  // IGNORE FOR Vector3
        // \        /  \   /     \                      /

        return dest.set(A * X + B * Y + C * Z + D * W,
                E * X + F * Y + G * Z + H * W,
                I * X + J * Y + K * Z + L * W);
    }

    /**
     * Multiplies this matrix with a row matrix defined by the vector {@code v} from the right side, and stores the
     * result in {@code dest}.
     *
     * @param v    The vector to multiply this matrix with.
     * @param dest The vector to store the result.
     *
     * @return The destination vector, aka the result.
     */
    public Vector4 multiply(Vector4 v, Vector4 dest)
    {
        float X = v.x;
        float Y = v.y;
        float Z = v.z;
        float W = 1;

        float A = m[0][0], B = m[0][1], C = m[0][2], D = m[0][3];
        float E = m[1][0], F = m[1][1], G = m[1][2], H = m[1][3];
        float I = m[2][0], J = m[2][1], K = m[2][2], L = m[2][3];
        float M = m[3][0], N = m[3][1], O = m[3][2], P = m[3][3];

        // /        \  /   \     /                       \
        // | a b c d | | x |     | a.x + b.y + c.z + d.w |
        // | e f g h | | y |     | e.x + f.y + g.z + h.w |
        // | i j k l | | z |  =  | i.x + j.y + k.z + l.w |
        // | m n o p | | w |     | m.x + n.y + o.z + p.w |
        // \        /  \   /     \                      /

        return dest.set(A * X + B * Y + C * Z + D * W,
                E * X + F * Y + G * Z + H * W,
                I * X + J * Y + K * Z + L * W,
                M * X + N * Y + O * Z + P * W);
    }

    public Matrix4 transpose()
    {
        float m00 = this.m[0][0], m01 = this.m[1][0], m02 = this.m[2][0], m03 = this.m[3][0];
        float m10 = this.m[0][1], m11 = this.m[1][1], m12 = this.m[2][1], m13 = this.m[3][1];
        float m20 = this.m[0][2], m21 = this.m[1][2], m22 = this.m[2][2], m23 = this.m[3][2];
        float m30 = this.m[0][3], m31 = this.m[1][3], m32 = this.m[2][3], m33 = this.m[3][3];

        this.m[0][0] = m00;
        this.m[1][0] = m10;
        this.m[2][0] = m20;
        this.m[3][0] = m30;
        this.m[0][1] = m01;
        this.m[1][1] = m11;
        this.m[2][1] = m21;
        this.m[3][1] = m31;
        this.m[0][2] = m02;
        this.m[1][2] = m12;
        this.m[2][2] = m22;
        this.m[3][2] = m32;
        this.m[0][3] = m03;
        this.m[1][3] = m13;
        this.m[2][3] = m23;
        this.m[3][3] = m33;

        return this;
    }

    public Matrix4 invert()
    {
        float s = determinant();

        if (s == 0)
            return this;

        s = 1f / s;

        float m00, m01, m02, m03;
        float m10, m11, m12, m13;
        float m20, m21, m22, m23;
        float m30, m31, m32, m33;

        m00 = (m[1][1] * (m[2][2] * m[3][3] - m[2][3] * m[3][2]) + m[1][2] * (m[2][3] * m[3][1] - m[2][1] * m[3][3]) + m[1][3] * (m[2][1] * m[3][2] - m[2][2] * m[3][1])) * s;
        m01 = (m[2][1] * (m[0][2] * m[3][3] - m[0][3] * m[3][2]) + m[2][2] * (m[0][3] * m[3][1] - m[0][1] * m[3][3]) + m[2][3] * (m[0][1] * m[3][2] - m[0][2] * m[3][1])) * s;
        m02 = (m[3][1] * (m[0][2] * m[1][3] - m[0][3] * m[1][2]) + m[3][2] * (m[0][3] * m[1][1] - m[0][1] * m[1][3]) + m[3][3] * (m[0][1] * m[1][2] - m[0][2] * m[1][1])) * s;
        m03 = (m[0][1] * (m[1][3] * m[2][2] - m[1][2] * m[2][3]) + m[0][2] * (m[1][1] * m[2][3] - m[1][3] * m[2][1]) + m[0][3] * (m[1][2] * m[2][1] - m[1][1] * m[2][2])) * s;
        m10 = (m[1][2] * (m[2][0] * m[3][3] - m[2][3] * m[3][0]) + m[1][3] * (m[2][2] * m[3][0] - m[2][0] * m[3][2]) + m[1][0] * (m[2][3] * m[3][2] - m[2][2] * m[3][3])) * s;
        m11 = (m[2][2] * (m[0][0] * m[3][3] - m[0][3] * m[3][0]) + m[2][3] * (m[0][2] * m[3][0] - m[0][0] * m[3][2]) + m[2][0] * (m[0][3] * m[3][2] - m[0][2] * m[3][3])) * s;
        m12 = (m[3][2] * (m[0][0] * m[1][3] - m[0][3] * m[1][0]) + m[3][3] * (m[0][2] * m[1][0] - m[0][0] * m[1][2]) + m[3][0] * (m[0][3] * m[1][2] - m[0][2] * m[1][3])) * s;
        m13 = (m[0][2] * (m[1][3] * m[2][0] - m[1][0] * m[2][3]) + m[0][3] * (m[1][0] * m[2][2] - m[1][2] * m[2][0]) + m[0][0] * (m[1][2] * m[2][3] - m[1][3] * m[2][2])) * s;
        m20 = (m[1][3] * (m[2][0] * m[3][1] - m[2][1] * m[3][0]) + m[1][0] * (m[2][1] * m[3][3] - m[2][3] * m[3][1]) + m[1][1] * (m[2][3] * m[3][0] - m[2][0] * m[3][3])) * s;
        m21 = (m[2][3] * (m[0][0] * m[3][1] - m[0][1] * m[3][0]) + m[2][0] * (m[0][1] * m[3][3] - m[0][3] * m[3][1]) + m[2][1] * (m[0][3] * m[3][0] - m[0][0] * m[3][3])) * s;
        m22 = (m[3][3] * (m[0][0] * m[1][1] - m[0][1] * m[1][0]) + m[3][0] * (m[0][1] * m[1][3] - m[0][3] * m[1][1]) + m[3][1] * (m[0][3] * m[1][0] - m[0][0] * m[1][3])) * s;
        m23 = (m[0][3] * (m[1][1] * m[2][0] - m[1][0] * m[2][1]) + m[0][0] * (m[1][3] * m[2][1] - m[1][1] * m[2][3]) + m[0][1] * (m[1][0] * m[2][3] - m[1][3] * m[2][0])) * s;
        m30 = (m[1][0] * (m[2][2] * m[3][1] - m[2][1] * m[3][2]) + m[1][1] * (m[2][0] * m[3][2] - m[2][2] * m[3][0]) + m[1][2] * (m[2][1] * m[3][0] - m[2][0] * m[3][1])) * s;
        m31 = (m[2][0] * (m[0][2] * m[3][1] - m[0][1] * m[3][2]) + m[2][1] * (m[0][0] * m[3][2] - m[0][2] * m[3][0]) + m[2][2] * (m[0][1] * m[3][0] - m[0][0] * m[3][1])) * s;
        m32 = (m[3][0] * (m[0][2] * m[1][1] - m[0][1] * m[1][2]) + m[3][1] * (m[0][0] * m[1][2] - m[0][2] * m[1][0]) + m[3][2] * (m[0][1] * m[1][0] - m[0][0] * m[1][1])) * s;
        m33 = (m[0][0] * (m[1][1] * m[2][2] - m[1][2] * m[2][1]) + m[0][1] * (m[1][2] * m[2][0] - m[1][0] * m[2][2]) + m[0][2] * (m[1][0] * m[2][1] - m[1][1] * m[2][0])) * s;

        this.m[0][0] = m00;
        this.m[1][0] = m10;
        this.m[2][0] = m20;
        this.m[3][0] = m30;
        this.m[0][1] = m01;
        this.m[1][1] = m11;
        this.m[2][1] = m21;
        this.m[3][1] = m31;
        this.m[0][2] = m02;
        this.m[1][2] = m12;
        this.m[2][2] = m22;
        this.m[3][2] = m32;
        this.m[0][3] = m03;
        this.m[1][3] = m13;
        this.m[2][3] = m23;
        this.m[3][3] = m33;

        return this;
    }

    public Matrix4 copy()
    {
        return new Matrix4(this);
    }

    public float determinant()
    {
        return (m[0][0] * m[1][1] - m[0][1] * m[1][0]) * (m[2][2] * m[3][3] - m[2][3] * m[3][2]) -
               (m[0][0] * m[1][2] - m[0][2] * m[1][0]) * (m[2][1] * m[3][3] - m[2][3] * m[3][1]) +
               (m[0][0] * m[1][3] - m[0][3] * m[1][0]) * (m[2][1] * m[3][2] - m[2][2] * m[3][1]) +
               (m[0][1] * m[1][2] - m[0][2] * m[1][1]) * (m[2][0] * m[3][3] - m[2][3] * m[3][0]) -
               (m[0][1] * m[1][3] - m[0][3] * m[1][1]) * (m[2][0] * m[3][2] - m[2][2] * m[3][0]) +
               (m[0][2] * m[1][3] - m[0][3] * m[1][2]) * (m[2][0] * m[3][1] - m[2][1] * m[3][0]);
    }

    public DirectFloatBuffer storeInto(DirectFloatBuffer buffer)
    {
        buffer.write(0, this.m[0][0]);
        buffer.write(1, this.m[0][1]);
        buffer.write(2, this.m[0][2]);
        buffer.write(3, this.m[0][3]);

        buffer.write(4, this.m[1][0]);
        buffer.write(5, this.m[1][1]);
        buffer.write(6, this.m[1][2]);
        buffer.write(7, this.m[1][3]);

        buffer.write(8, this.m[2][0]);
        buffer.write(9, this.m[2][1]);
        buffer.write(10, this.m[2][2]);
        buffer.write(11, this.m[2][3]);

        buffer.write(12, this.m[3][0]);
        buffer.write(13, this.m[3][1]);
        buffer.write(14, this.m[3][2]);
        buffer.write(15, this.m[3][3]);

        return buffer;
    }

    @Override
    public int hashCode()
    {
        return Arrays.deepHashCode(m);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Matrix4 matrix4 = (Matrix4) o;

        return Arrays.deepEquals(m, matrix4.m);
    }

    @Override
    public String toString()
    {
        StringBuilder s = new StringBuilder();

        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
                s.append(m[i][j]).append(' ');

            s.append('\n');
        }

        return s.toString();
    }
}
