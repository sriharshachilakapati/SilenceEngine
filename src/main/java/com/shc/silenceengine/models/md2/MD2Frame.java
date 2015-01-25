package com.shc.silenceengine.models.md2;

import com.shc.silenceengine.math.Vector3;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
public class MD2Frame
{
    /**
     * The scale of this frame.
     */
    public Vector3 scale;

    /**
     * The translate of this frame.
     */
    public Vector3 translate;

    /**
     * The name of this frame.
     */
    public String name;

    /**
     * The first vertex of this frame.
     */
    public List<MD2Vertex> vertices;

    public MD2Frame()
    {
        vertices = new ArrayList<>();
    }
}
