package com.shc.silenceengine.geom3d;

import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.Vector3;

/**
 * @author Sri Harsha Chilakapati
 */
public class Vertex
{
    public Vector3 position;
    public Vector3 normal;
    public Vector2 textureCoord;

    public Vertex()
    {
        this(new Vector3(), new Vector3(), new Vector2());
    }

    public Vertex(Vector3 position, Vector3 normal, Vector2 textureCoord)
    {
        this.position     = position;
        this.normal       = normal;
        this.textureCoord = textureCoord;
    }
}
