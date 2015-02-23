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
        float[][] temp = new float[3][3];

        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                for (int k = 0; k < 3; k++)
                {
                    temp[i][j] += this.m[i][k] * m.get(k, j);
                }
            }
        }

        this.m = temp;

        return this;
    }

    public Matrix3 transpose()
    {
        return new Matrix3(this).transposeSelf();
    }

    public Matrix3 transposeSelf()
    {
        float[][] temp = new float[3][3];

        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                temp[i][j] = m[j][i];
            }
        }

        m = temp;

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

    public Matrix3 set(int x, int j, float val)
    {
        m[x][j] = val;

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
