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
        addVertex(new Vector3(-width / 2, -height / 2, +thickness / 2));
        addVertex(new Vector3(+width / 2, -height / 2, +thickness / 2));
        addVertex(new Vector3(-width / 2, +height / 2, +thickness / 2));
        addVertex(new Vector3(+width / 2, +height / 2, +thickness / 2));

        // Right face
        addVertex(new Vector3(+width / 2, +height / 2, +thickness / 2));
        addVertex(new Vector3(+width / 2, -height / 2, +thickness / 2));
        addVertex(new Vector3(+width / 2, +height / 2, -thickness / 2));
        addVertex(new Vector3(+width / 2, -height / 2, -thickness / 2));

        // Back face
        addVertex(new Vector3(+width / 2, -height / 2, -thickness / 2));
        addVertex(new Vector3(-width / 2, -height / 2, -thickness / 2));
        addVertex(new Vector3(+width / 2, +height / 2, -thickness / 2));
        addVertex(new Vector3(-width / 2, +height / 2, -thickness / 2));

        // Left face
        addVertex(new Vector3(-width / 2, +height / 2, -thickness / 2));
        addVertex(new Vector3(-width / 2, -height / 2, -thickness / 2));
        addVertex(new Vector3(-width / 2, +height / 2, +thickness / 2));
        addVertex(new Vector3(-width / 2, -height / 2, +thickness / 2));

        // Bottom face
        addVertex(new Vector3(-width / 2, -height / 2, +thickness / 2));
        addVertex(new Vector3(-width / 2, -height / 2, -thickness / 2));
        addVertex(new Vector3(+width / 2, -height / 2, +thickness / 2));
        addVertex(new Vector3(+width / 2, -height / 2, -thickness / 2));

        // Move to top
        addVertex(new Vector3(+width / 2, -height / 2, -thickness / 2));
        addVertex(new Vector3(-width / 2, +height / 2, +thickness / 2));

        // Top face
        addVertex(new Vector3(-width / 2, +height / 2, +thickness / 2));
        addVertex(new Vector3(+width / 2, +height / 2, +thickness / 2));
        addVertex(new Vector3(-width / 2, +height / 2, -thickness / 2));
        addVertex(new Vector3(+width / 2, +height / 2, -thickness / 2));
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

    public void set(float width, float height, float thickness, Vector3 position)
    {
        setPosition(position);

        this.width = width;
        this.height = height;
        this.thickness = thickness;

        updateVertices();
    }
}