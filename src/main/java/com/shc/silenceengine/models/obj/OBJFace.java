package com.shc.silenceengine.models.obj;

import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.models.Material;

/**
 * @author Sri Harsha Chilakapati
 */
public class OBJFace
{
    private Vector3 vertex;
    private Vector3 normal;
    private Vector3 texCoords;

    private Material material;

    public OBJFace(Vector3 vertex, Vector3 normal, Vector3 texCoords, Material material)
    {
        this.vertex = vertex;
        this.normal = normal;
        this.material = material;
        this.texCoords = texCoords;

        if (material == null)
        {
            // If there is no material, create a default one
            material = new Material();
            material.setName("Default");
        }
    }

    /**
     * @return The vertex indices
     */
    public Vector3 getVertex()
    {
        return vertex;
    }

    /**
     * @return The normal indices
     */
    public Vector3 getNormal()
    {
        return normal;
    }

    /**
     * @return The Texture Indices
     */
    public Vector3 getTexCoord()
    {
        return texCoords;
    }

    /**
     * @return The material of the face
     */
    public Material getMaterial()
    {
        return material;
    }
}
