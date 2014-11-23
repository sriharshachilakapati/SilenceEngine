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
    }

    public void initIdentity()
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
    }

    
}
