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
 * $$ \begin{bmatrix} m00 &amp; m10 &amp; m20  \\ m01 &amp; m11 &amp; m21  \\ m02 &amp; m12 &amp; m22 \end{bmatrix} $$
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
     * @param diagonal The value to set Identity positions to
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
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                this.m[i][j] = m.m[i][j];
            }
        }

        return this;
    }

    /**
     * Sets the Matrix to an Identity Matrix.
     *
     * $$ \begin{bmatrix} 1 &amp; 0 &amp; 0  \\ 0 &amp; 1 &amp; 0  \\ 0 &amp; 0 &amp; 1 \end{bmatrix} $$
     *
     * @return This {@code Matrix3}
     */
    public Matrix3 initIdentity()
    {
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                if (i == j)
                    m[i][j] = 1;
                else
                    m[i][j] = 0;
            }
        }

        return this;
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
     * $$ \begin{bmatrix} d &amp; 0 &amp; 0  \\ 0 &amp; d &amp; 0  \\ 0 &amp; 0 &amp; d \end{bmatrix} $$
     *
     * @param diagonal The value to set Identity positions to (d in the matrix)
     *
     * @return This {@code Matrix3}
     */
    public Matrix3 set(float diagonal)
    {
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                m[i][j] = (i == j) ? diagonal : 0;
            }
        }

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
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                this.m[i][j] += m.m[i][j];
            }
        }

        return this;
    }

    public Matrix3 subtract(Matrix3 m)
    {
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                this.m[i][j] -= m.m[i][j];
            }
        }

        return this;
    }

    public Matrix3 multiply(Matrix3 m)
    {
        // Use a temporary matrix from the matrix stack instead of
        // creating a temporary float array every frame.
        Matrix3 temp = Matrix3.REUSABLE_STACK.pop().initZero();

        for (int r = 0; r < 3; r++)
        {
            for (int c = 0; c < 3; c++)
            {
                for (int k = 0; k < 3; k++)
                    temp.m[c][r] += this.m[k][r] * m.m[c][k];
            }
        }

        this.set(temp);
        Matrix3.REUSABLE_STACK.push(temp);

        return this;
    }

    /**
     * Set all positions in this {@code Matrix3} to 0.
     *
     * $$ \begin{bmatrix} 0 &amp; 0 &amp; 0  \\ 0 &amp; 0 &amp; 0  \\ 0 &amp; 0 &amp; 0 \end{bmatrix} $$
     *
     * @return This {@code Matrix3}
     */
    public Matrix3 initZero()
    {
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                m[i][j] = 0;
            }
        }

        return this;
    }

    public Matrix3 set(int x, int j, float val)
    {
        m[x][j] = val;

        return this;
    }

    public Matrix3 transpose()
    {
        Matrix3 temp = Matrix3.REUSABLE_STACK.pop();

        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                temp.set(i, j, m[j][i]);
            }
        }

        this.set(temp);
        Matrix3.REUSABLE_STACK.push(temp);

        return this;
    }

    public Vector3 multiply(Vector3 v, Vector3 dest)
    {
        return dest.set(m[0][0] * v.x, m[0][1] * v.y, m[0][2] * v.z);
    }

    public Matrix3 invert()
    {
        float s = determinant();

        if (s == 0)
            return this;

        s = 1f / s;

        Matrix3 dest = Matrix3.REUSABLE_STACK.pop();

        dest.m[0][0] = +((m[1][1] * m[2][2]) - (m[2][1] * m[1][2])) * s;
        dest.m[0][1] = -((m[0][1] * m[2][2]) - (m[2][1] * m[0][2])) * s;
        dest.m[0][2] = +((m[0][1] * m[1][2]) - (m[1][1] * m[0][2])) * s;
        dest.m[1][0] = -((m[1][0] * m[2][2]) - (m[2][0] * m[1][2])) * s;
        dest.m[1][1] = +((m[0][0] * m[2][2]) - (m[2][0] * m[0][2])) * s;
        dest.m[1][2] = -((m[0][0] * m[1][2]) - (m[1][0] * m[0][2])) * s;
        dest.m[2][0] = +((m[1][0] * m[2][1]) - (m[2][0] * m[1][1])) * s;
        dest.m[2][1] = -((m[0][0] * m[2][1]) - (m[2][0] * m[0][1])) * s;
        dest.m[2][2] = +((m[0][0] * m[1][1]) - (m[1][0] * m[0][1])) * s;

        set(dest);

        Matrix3.REUSABLE_STACK.push(dest);
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
        int index = 0;

        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
                buffer.write(index++, get(i, j));
        }

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
