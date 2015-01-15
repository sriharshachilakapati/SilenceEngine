package com.shc.silenceengine.geom3d;

import com.shc.silenceengine.math.Vector3;

/**
 * @author Sri Harsha Chilakapati
 */
public class Cuboid extends Polyhedron
{
    private float width;
    private float height;
    private float thickness;

    public Cuboid(Vector3 position, float width, float height, float thickness)
    {
        this.width = width;
        this.height = height;
        this.thickness = thickness;

        setPosition(position);

        updateVertices();
    }

    public Cuboid(Vector3 min, Vector3 max)
    {
        Vector3 size = max.subtract(min);

        width = size.x;
        height = size.y;
        thickness = size.z;

        setPosition(min.add(max).scale(0.5f));

        updateVertices();
    }

    private void updateVertices()
    {
        clearVertices();

        // Front face
        addVertex(new Vector3(-width, -height, +thickness));
        addVertex(new Vector3(+width, -height, +thickness));
        addVertex(new Vector3(-width, +height, +thickness));
        addVertex(new Vector3(+width, +height, +thickness));

        // Right face
        addVertex(new Vector3(+width, +height, +thickness));
        addVertex(new Vector3(+width, -height, +thickness));
        addVertex(new Vector3(+width, +height, -thickness));
        addVertex(new Vector3(+width, -height, -thickness));

        // Back face
        addVertex(new Vector3(+width, -height, -thickness));
        addVertex(new Vector3(-width, -height, -thickness));
        addVertex(new Vector3(+width, +height, -thickness));
        addVertex(new Vector3(-width, +height, -thickness));

        // Left face
        addVertex(new Vector3(-width, +height, -thickness));
        addVertex(new Vector3(-width, -height, -thickness));
        addVertex(new Vector3(-width, +height, +thickness));
        addVertex(new Vector3(-width, -height, +thickness));

        // Bottom face
        addVertex(new Vector3(-width, -height, +thickness));
        addVertex(new Vector3(-width, -height, -thickness));
        addVertex(new Vector3(+width, -height, +thickness));
        addVertex(new Vector3(+width, -height, -thickness));

        // Move to top
        addVertex(new Vector3(+width, -height, -thickness));
        addVertex(new Vector3(-width, +height, +thickness));

        // Top face
        addVertex(new Vector3(-width, +height, +thickness));
        addVertex(new Vector3(+width, +height, +thickness));
        addVertex(new Vector3(-width, +height, -thickness));
        addVertex(new Vector3(+width, +height, -thickness));
    }

    public float getWidth()
    {
        return width;
    }

    public float getHeight()
    {
        return height;
    }

    public float getThickness()
    {
        return thickness;
    }
}
