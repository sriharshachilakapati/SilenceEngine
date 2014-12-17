package com.shc.silenceengine.geom3d;

import com.shc.silenceengine.math.Vector3;

/**
 * @author Sri Harsha Chilakapati
 */
public class Sphere extends Mesh
{
    private float radius;

    public Sphere(Vector3 position, float radius)
    {
        setPosition(position);

        this.radius = radius;
        updateVertices();
    }

    private void updateVertices()
    {
        clearVertices();

        for (int i = 0; i <= 40; i++)
        {

        }

//        for(int i = 0; i <= 40; i++)
//        {
//            double lat0 = M_PI * (-0.5 + (double) (i - 1) / 40);
//            double z0  = sin(lat0);
//            double zr0 =  cos(lat0);
//
//            double lat1 = M_PI * (-0.5 + (double) i / 40);
//            double z1 = sin(lat1);
//            double zr1 = cos(lat1);
//
//            for(int j = 0; j <= 40; j++)
//            {
//                double lng = 2 * M_PI * (double) (j - 1) / 40;
//                double x = cos(lng);
//                double y = sin(lng);
//
//                ballVerts.push_back(x * zr0); //X
//                ballVerts.push_back(y * zr0); //Y
//                ballVerts.push_back(z0);      //Z
//
//                ballVerts.push_back(0.0f);
//                ballVerts.push_back(1.0f);
//                ballVerts.push_back(0.0f);
//                ballVerts.push_back(1.0f); //R,G,B,A
//
//                ballVerts.push_back(x * zr1); //X
//                ballVerts.push_back(y * zr1); //Y
//                ballVerts.push_back(z1);      //Z
//
//                ballVerts.push_back(0.0f);
//                ballVerts.push_back(1.0f);
//                ballVerts.push_back(0.0f);
//                ballVerts.push_back(1.0f); //R,G,B,A
//            }
//        }
    }
}
