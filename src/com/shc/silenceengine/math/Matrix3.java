package com.shc.silenceengine.math;

/**
 * @author Sri Harsha Chilakapati
 */
public class Matrix3
{
    private float[][] m;

    public Matrix3()
    {
        m = new float[3][3];
        initIdentity();
    }

    public Matrix3(Matrix3 m)
    {
        for (int i=0; i<3; i++)
        {
            for (int j=0; j<3; j++)
            {
                this.m[i][j] = m.get(i, j);
            }
        }
    }

    public Matrix3 initIdentity()
    {
        for (int i=0; i<3; i++)
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

    public Matrix3 initZero()
    {
        for (int i=0; i<3; i++)
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
        for (int i=0; i<3; i++)
        {
            for (int j=0; j<3; j++)
            {
                this.m[i][j] += m.get(i, j);
            }
        }

        return this;
    }

    public Matrix3 subtract(Matrix3 m)
    {
        for (int i=0; i<3; i++)
        {
            for (int j=0; j<3; j++)
            {
                this.m[i][j] -= m.get(i, j);
            }
        }

        return this;
    }

    public Matrix3 multiply(Matrix3 m)
    {
        float[][] temp = new float[3][3];

        for (int i=0; i<3; i++)
        {
            for (int j=0; j<3; j++)
            {
                for (int k=0; k<3; k++)
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
        float[][] temp = new float[3][3];

        for (int i=0; i<3; i++)
        {
            for (int j=0; j<3; j++)
            {
                temp[i][j] = m[j][i];
            }
        }

        m = temp;

        return this;
    }

    public Vector3 multiply(Vector3 v)
    {
        return new Vector3().setX(m[0][0] * v.getX())
                            .setY(m[0][1] * v.getY())
                            .setZ(m[0][2] * v.getZ());
    }

    public Matrix3 copy()
    {
        return new Matrix3(this);
    }

    public float get(int x, int y)
    {
        return m[x][y];
    }

    public Matrix3 set(int x, int j, float val)
    {
        m[x][j] = val;

        return this;
    }
}
