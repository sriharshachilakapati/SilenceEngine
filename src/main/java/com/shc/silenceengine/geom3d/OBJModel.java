package com.shc.silenceengine.geom3d;

import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.Vector3;

/**
 * @author Sri Harsha Chilakapati
 */
public class OBJModel extends Model
{
    public static class Material
    {
        public Face[] faces;

    }

    public static class Face
    {
        public Vertex v1;
        public Vertex v2;
        public Vertex v3;
    }
}
