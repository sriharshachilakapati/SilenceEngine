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

    public Matrix4(Matrix3 m)
    {
        this();

        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                this.m[i][j] = m.get(i, j);
            }
        }
    }

    public Matrix4(Matrix4 m)
    {
        this();

        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                this.m[i][j] = m.get(i, j);
            }
        }
    }

    public Matrix4 initIdentity()
    {
        for (int i = 0; i < 4; i++)
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
        for (int i = 0; i < 4; i++)
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
        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                this.m[i][j] += m.get(i, j);
            }
        }

        return this;
    }

    public Matrix4 subtract(Matrix4 m)
    {
        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                this.m[i][j] -= m.get(i, j);
            }
        }

        return this;
    }

    public Matrix4 multiply(Matrix4 m)
    {
        float[][] temp = new float[4][4];

        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                for (int k = 0; k < 4; k++)
                {
                    temp[i][j] += this.m[i][k] * m.get(k, j);
                }
            }
        }

        this.m = temp;

        return this;
    }

    public Vector3 multiply(Vector3 v)
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

        return new Vector3(A * X + B * Y + C * Z + D * W,
                E * X + F * Y + G * Z + H * W,
                I * X + J * Y + K * Z + L * W);
    }

    public Vector4 multiply(Vector4 v)
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

        return new Vector4(A * X + B * Y + C * Z + D * W,
                E * X + F * Y + G * Z + H * W,
                I * X + J * Y + K * Z + L * W,
                M * X + N * Y + O * Z + P * W);
    }

    public Matrix4 transpose()
    {
        float[][] temp = new float[4][4];

        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                temp[i][j] = m[j][i];
            }
        }

        m = temp;

        return this;
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

    @Override
    public String toString()
    {
        StringBuilder s = new StringBuilder();

        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                s.append(m[i][j]).append(' ');
            }
            s.append('\n');
        }

        return s.toString();
    }
}
