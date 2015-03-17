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
public class Matrix3
{
    public static final ReusableStack<Matrix3> REUSABLE_STACK = new ReusableStack<>(Matrix3.class);

    private float[][] m;

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

    public Matrix3 set(Matrix3 m)
    {
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                this.m[i][j] = m.get(i, j);
            }
        }

        return this;
    }

    public float get(int x, int y)
    {
        return m[x][y];
    }

    public Matrix3(float diagonal)
    {
        this();
        set(diagonal);
    }

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

    public Matrix3 add(Matrix3 m)
    {
        return new Matrix3(this).addSelf(m);
    }

    public Matrix3 addSelf(Matrix3 m)
    {
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                this.m[i][j] += m.get(i, j);
            }
        }

        return this;
    }

    public Matrix3 subtract(Matrix3 m)
    {
        return new Matrix3(this).subtractSelf(m);
    }

    public Matrix3 subtractSelf(Matrix3 m)
    {
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                this.m[i][j] -= m.get(i, j);
            }
        }

        return this;
    }

    public Matrix3 multiply(Matrix3 m)
    {
        return new Matrix3(this).multiplySelf(m);
    }

    public Matrix3 multiplySelf(Matrix3 m)
    {
        // Use a temporary matrix from the matrix stack instead of
        // creating a temporary float array every frame.
        Matrix3 temp = Matrix3.REUSABLE_STACK.pop().initZero();

        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                for (int k = 0; k < 3; k++)
                {
                    temp.set(i, j, temp.get(i, j) + (this.m[i][k] * m.get(k, j)));
                }
            }
        }

        this.set(temp);
        Matrix3.REUSABLE_STACK.push(temp);

        return this;
    }

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
        return new Matrix3(this).transposeSelf();
    }

    public Matrix3 transposeSelf()
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

    public Vector3 multiply(Vector3 v)
    {
        return multiply(v, new Vector3());
    }

    public Vector3 multiply(Vector3 v, Vector3 dest)
    {
        return dest.set(m[0][0] * v.getX(), m[0][1] * v.getY(), m[0][2] * v.getZ());
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

    public Matrix3 invert()
    {
        return copy().invertSelf();
    }

    public Matrix3 invertSelf()
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

    @Override
    public String toString()
    {
        StringBuilder s = new StringBuilder();

        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                s.append(m[i][j]).append(' ');
            }
            s.append('\n');
        }

        return s.toString();
    }
}
