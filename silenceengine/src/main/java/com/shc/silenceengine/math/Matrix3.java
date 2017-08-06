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
 * A 3x3 Matrix.
 *
 * @author Sri Harsha Chilakapati
 */
public class Matrix3
{
    public static final ReusableStack<Matrix3> REUSABLE_STACK = new ReusableStack<>(Matrix3::new);

    public final float[][] m;

    public Matrix3(Matrix3 m)
    {
        this();
        set(m);
    }

    public Matrix3()
    {
        m = new float[3][3];
        initIdentity();
    }

    /**
     * Create a {@code Matrix3} using {@link #set(float)}.
     *
     * @param diagonal The value to set identity positions to
     *
     * @see #set(float)
     */
    public Matrix3(float diagonal)
    {
        this();
        set(diagonal);
    }

    /**
     * Set this {@code Matrix3} to match {@code m}.
     *
     * @param m The {@code Matrix3} to copy.
     *
     * @return This {@code Matrix3}
     */
    public Matrix3 set(Matrix3 m)
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

    /**
     * Sets the Matrix to an Identity Matrix.
     *
     * @return This {@code Matrix3}
     */
    public Matrix3 initIdentity()
    {
        return set(1);
    }

    /**
     * Get the value in this {@code Matrix3} at position {@code (x, y)}.
     *
     * @param x The x position of this Matrix3 to obtain.
     * @param y The y position of this Matrix3 to obtain.
     *
     * @return The value at {@code (x, y)}
     */
    public float get(int x, int y)
    {
        return m[x][y];
    }

    /**
     * Sets all Identity positions to {@code diagonal}.
     *
     * @param diagonal The value to set identity positions to.
     *
     * @return This {@code Matrix3}
     */
    public Matrix3 set(float diagonal)
    {
        m[0][0] = diagonal;
        m[1][0] = 0;
        m[2][0] = 0;
        m[0][1] = 0;
        m[1][1] = diagonal;
        m[2][1] = 0;
        m[0][2] = 0;
        m[1][2] = 0;
        m[2][2] = diagonal;

        return this;
    }

    /**
     * Adds {@code m} to this {@code Matrix3} and returns itself.
     *
     * @param m The {@code Matrix3} to add on to this {@code Matrix3}
     *
     * @return This {@code Matrix3}
     */
    public Matrix3 add(Matrix3 m)
    {
        this.m[0][0] += m.m[0][0];
        this.m[1][0] += m.m[1][0];
        this.m[2][0] += m.m[2][0];
        this.m[0][1] += m.m[0][1];
        this.m[1][1] += m.m[1][1];
        this.m[2][1] += m.m[2][1];
        this.m[0][2] += m.m[0][2];
        this.m[1][2] += m.m[1][2];
        this.m[2][2] += m.m[2][2];

        return this;
    }

    public Matrix3 subtract(Matrix3 m)
    {
        this.m[0][0] -= m.m[0][0];
        this.m[1][0] -= m.m[1][0];
        this.m[2][0] -= m.m[2][0];
        this.m[0][1] -= m.m[0][1];
        this.m[1][1] -= m.m[1][1];
        this.m[2][1] -= m.m[2][1];
        this.m[0][2] -= m.m[0][2];
        this.m[1][2] -= m.m[1][2];
        this.m[2][2] -= m.m[2][2];

        return this;
    }

    public Matrix3 multiply(Matrix3 m)
    {
        float m00, m01, m02;
        float m10, m11, m12;
        float m20, m21, m22;

        m00 = this.m[0][0] * m.m[0][0] + this.m[1][0] * m.m[0][1] + this.m[2][0] * m.m[0][2];
        m10 = this.m[0][0] * m.m[1][0] + this.m[1][0] * m.m[1][1] + this.m[2][0] * m.m[1][2];
        m20 = this.m[0][0] * m.m[2][0] + this.m[1][0] * m.m[2][1] + this.m[2][0] * m.m[2][2];

        m01 = this.m[0][1] * m.m[0][0] + this.m[1][1] * m.m[0][1] + this.m[2][1] * m.m[0][2];
        m11 = this.m[0][1] * m.m[1][0] + this.m[1][1] * m.m[1][1] + this.m[2][1] * m.m[1][2];
        m21 = this.m[0][1] * m.m[2][0] + this.m[1][1] * m.m[2][1] + this.m[2][1] * m.m[2][2];

        m02 = this.m[0][2] * m.m[0][0] + this.m[1][2] * m.m[0][1] + this.m[2][2] * m.m[0][2];
        m12 = this.m[0][2] * m.m[1][0] + this.m[1][2] * m.m[1][1] + this.m[2][2] * m.m[1][2];
        m22 = this.m[0][2] * m.m[2][0] + this.m[1][2] * m.m[2][1] + this.m[2][2] * m.m[2][2];

        this.m[0][0] = m00;
        this.m[1][0] = m10;
        this.m[2][0] = m20;
        this.m[0][1] = m01;
        this.m[1][1] = m11;
        this.m[2][1] = m21;
        this.m[0][2] = m02;
        this.m[1][2] = m12;
        this.m[2][2] = m22;

        return this;
    }

    /**
     * Set all positions in this {@code Matrix3} to 0.
     *
     * @return This {@code Matrix3}
     */
    public Matrix3 initZero()
    {
        return set(0);
    }

    public Matrix3 set(int x, int j, float val)
    {
        m[x][j] = val;

        return this;
    }

    public Matrix3 transpose()
    {
        float m00 = this.m[0][0], m01 = this.m[1][0], m02 = this.m[2][0];
        float m10 = this.m[0][1], m11 = this.m[1][1], m12 = this.m[2][1];
        float m20 = this.m[0][2], m21 = this.m[1][2], m22 = this.m[2][2];

        this.m[0][0] = m00;
        this.m[1][0] = m10;
        this.m[2][0] = m20;
        this.m[0][1] = m01;
        this.m[1][1] = m11;
        this.m[2][1] = m21;
        this.m[0][2] = m02;
        this.m[1][2] = m12;
        this.m[2][2] = m22;

        return this;
    }

    public Vector3 multiply(Vector3 v, Vector3 dest)
    {
        return dest.set(m[0][0] * v.x + m[0][1] * v.y + m[0][2] * v.z,
                m[1][0] * v.x + m[1][1] * v.y + m[1][2] * v.z,
                m[2][0] * v.x + m[2][1] * v.y + m[2][2] * v.z);
    }

    public Matrix3 invert()
    {
        float s = determinant();

        if (s == 0)
            return this;

        s = 1f / s;

        float m00, m01, m02;
        float m10, m11, m12;
        float m20, m21, m22;

        m00 = +((m[1][1] * m[2][2]) - (m[2][1] * m[1][2])) * s;
        m01 = -((m[0][1] * m[2][2]) - (m[2][1] * m[0][2])) * s;
        m02 = +((m[0][1] * m[1][2]) - (m[1][1] * m[0][2])) * s;
        m10 = -((m[1][0] * m[2][2]) - (m[2][0] * m[1][2])) * s;
        m11 = +((m[0][0] * m[2][2]) - (m[2][0] * m[0][2])) * s;
        m12 = -((m[0][0] * m[1][2]) - (m[1][0] * m[0][2])) * s;
        m20 = +((m[1][0] * m[2][1]) - (m[2][0] * m[1][1])) * s;
        m21 = -((m[0][0] * m[2][1]) - (m[2][0] * m[0][1])) * s;
        m22 = +((m[0][0] * m[1][1]) - (m[1][0] * m[0][1])) * s;

        this.m[0][0] = m00;
        this.m[1][0] = m10;
        this.m[2][0] = m20;
        this.m[0][1] = m01;
        this.m[1][1] = m11;
        this.m[2][1] = m21;
        this.m[0][2] = m02;
        this.m[1][2] = m12;
        this.m[2][2] = m22;

        return this;
    }

    public Matrix3 copy()
    {
        return new Matrix3(this);
    }

    public float determinant()
    {
        return ((m[0][0] * m[1][1] * m[2][2]) +
                (m[1][0] * m[2][1] * m[0][2]) +
                (m[2][0] * m[0][1] * m[1][2])) -
               ((m[2][0] * m[1][1] * m[0][2]) +
                (m[0][0] * m[2][1] * m[1][2]) +
                (m[1][0] * m[0][1] * m[2][2]));
    }

    public DirectFloatBuffer storeInto(DirectFloatBuffer buffer)
    {
        buffer.write(0, this.m[0][0]);
        buffer.write(1, this.m[0][1]);
        buffer.write(2, this.m[0][2]);

        buffer.write(3, this.m[1][0]);
        buffer.write(4, this.m[1][1]);
        buffer.write(5, this.m[1][2]);

        buffer.write(6, this.m[2][0]);
        buffer.write(7, this.m[2][1]);
        buffer.write(8, this.m[2][2]);

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

        Matrix3 matrix3 = (Matrix3) o;

        return Arrays.deepEquals(m, matrix3.m);
    }

    @Override
    public String toString()
    {
        StringBuilder s = new StringBuilder();

        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
                s.append(m[i][j]).append(' ');

            s.append('\n');
        }

        return s.toString();
    }
}
