package com.shc.silenceengine.geom3d;

import com.shc.silenceengine.math.Quaternion;
import com.shc.silenceengine.math.Vector3;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
public class Mesh
{
    private Vector3       position;
    private List<Vector3> vertices;

    private float minX, maxX;
    private float minY, maxY;
    private float minZ, maxZ;

    // TODO: private Cube bounds

    public Mesh()
    {
        this.position = new Vector3();
        this.vertices = new ArrayList<>();

        clearVertices();
    }

    protected void addVertex(Vector3 vertex)
    {
        vertices.add(vertex);

        minX = Math.min(minX, vertex.x);
        minY = Math.min(minY, vertex.y);
        minZ = Math.min(minZ, vertex.z);

        maxX = Math.max(maxX, vertex.x);
        maxY = Math.max(maxY, vertex.y);
        maxZ = Math.max(maxZ, vertex.z);
    }

    protected void clearVertices()
    {
        vertices.clear();

        minX = minY = minZ = Float.POSITIVE_INFINITY;
        maxX = maxY = maxZ = Float.NEGATIVE_INFINITY;
    }

    public void rotate(Vector3 axis, float angle)
    {
        Quaternion rotation = new Quaternion(axis, angle);

        minX = minY = minZ = Float.POSITIVE_INFINITY;
        maxX = maxY = maxZ = Float.NEGATIVE_INFINITY;

        for (Vector3 vertex : vertices)
        {
            Vector3 rotated = rotation.multiply(vertex);
            vertex.set(rotated.x, rotated.y, rotated.z);

            minX = Math.min(minX, vertex.x);
            minY = Math.min(minY, vertex.y);
            minZ = Math.min(minZ, vertex.z);

            maxX = Math.max(maxX, vertex.x);
            maxY = Math.max(maxY, vertex.y);
            maxZ = Math.max(maxZ, vertex.z);
        }

        // TODO: create new bounds
    }

    public Mesh copy()
    {
        Mesh mesh = new Mesh();
        mesh.position = position.copy();

        vertices.forEach(mesh::addVertex);
        return mesh;
    }

    public int vertexCount()
    {
        return vertices.size();
    }

    public Vector3 getVertex(int i)
    {
        return vertices.get(i);
    }

    public Vector3 getPosition()
    {
        return position;
    }

    public void setPosition(Vector3 position)
    {
        this.position = position;
    }

    public List<Vector3> getVertices()
    {
        return vertices;
    }

    public void setVertices(List<Vector3> vertices)
    {
        this.vertices = vertices;
    }
}
