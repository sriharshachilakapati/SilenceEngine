package com.shc.silenceengine.math;

/**
 * @author Sri Harsha Chilakapati
 */
public class Matrix4
{
    private float[][] m;

    public Matrix4()
    {
        m = new float[4][4];
        initIdentity();
    }

    public Matrix4(Matrix4 m)
    {
        for (int i=0; i<4; i++)
        {
            for (int j=0; j<4; j++)
            {
                this.m[i][j] = m.get(i, j);
            }
        }
    }

    public Matrix4 initIdentity()
    {
        for (int i=0; i<4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                if (i == j)
                    m[i][j] = 1;
                else
                    m[i][j] = 0;
            }
        }

        return this;
    }

    public Matrix4 initZero()
    {
        for (int i=0; i<4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                m[i][j] = 0;
            }
        }

        return this;
    }

    public Matrix4 add(Matrix4 m)
    {
        for (int i=0; i<4; i++)
        {
            for (int j=0; j<4; j++)
            {
                this.m[i][j] += m.get(i, j);
            }
        }

        return this;
    }

    public Matrix4 subtract(Matrix4 m)
    {
        for (int i=0; i<4; i++)
        {
            for (int j=0; j<4; j++)
            {
                this.m[i][j] -= m.get(i, j);
            }
        }

        return this;
    }

    public Matrix4 multiply(Matrix4 m)
    {
        float[][] temp = new float[4][4];

        for (int i=0; i<4; i++)
        {
            for (int j=0; j<4; j++)
            {
                for (int k=0; k<4; k++)
                {
                    temp[i][j] += this.m[i][k] * m.get(k, i);
                }
            }
        }

        this.m = temp;

        return this;
    }

    public Vector4 multiply(Vector4 v)
    {
        return new Vector4().setX(m[0][0] * v.getX())
                            .setY(m[0][1] * v.getY())
                            .setZ(m[0][2] * v.getZ())
                            .setW(m[0][3] * v.getW());
    }

    public Matrix4 copy()
    {
        return new Matrix4(this);
    }

    public float get(int x, int y)
    {
        return m[x][y];
    }

    public Matrix4 set(int x, int j, float val)
    {
        m[x][j] = val;

        return this;
    }
}
