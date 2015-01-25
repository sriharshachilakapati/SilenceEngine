package com.shc.silenceengine.models.md2;

import com.shc.silenceengine.math.Vector3;

/**
 * @author Sri Harsha Chilakapati
 */
public class MD2Vertex
{
    /**
     * The position of this vertex used to store the coordinates.
     */
    public Vector3 position;

    /**
     * The index to the normal of this vertex used for lighting.
     */
    public float lightNormalIndex;
}
