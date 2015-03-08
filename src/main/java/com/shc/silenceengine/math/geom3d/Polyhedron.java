/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2015 Sri Harsha Chilakapati
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.shc.silenceengine.math.geom3d;

import com.shc.silenceengine.collision.Collision3D;
import com.shc.silenceengine.math.Quaternion;
import com.shc.silenceengine.math.Vector3;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
public class Polyhedron
{
    private Vector3 position;

    private List<Vector3> vertices;

    private float minX;
    private float minY;
    private float minZ;
    private float maxX;
    private float maxY;
    private float maxZ;

    private float rotationX;
    private float rotationY;
    private float rotationZ;

    private Cuboid bounds;

    private Quaternion tempQuat;

    public Polyhedron(Polyhedron other)
    {
        this();

        position.set(other.position);

        other.vertices.forEach(vertices::add);
    }

    public Polyhedron()
    {
        vertices = new ArrayList<>();
        position = new Vector3();

        tempQuat = new Quaternion();

        clearVertices();
    }

    public void clearVertices()
    {
        vertices.clear();

        minX = minY = minZ = Float.POSITIVE_INFINITY;
        maxX = maxY = maxZ = Float.NEGATIVE_INFINITY;
    }

    public void addVertex(float x, float y, float z)
    {
        addVertex(new Vector3(x, y, z));
    }

    public void addVertex(Vector3 v)
    {
        vertices.add(v);

        minX = Math.min(minX, v.x);
        minY = Math.min(minY, v.y);
        minZ = Math.min(minZ, v.z);
        maxX = Math.max(maxX, v.x);
        maxY = Math.max(maxY, v.y);
        maxZ = Math.max(maxZ, v.z);
    }

    public void rotate(float rx, float ry, float rz)
    {
        minX = minY = minZ = Float.POSITIVE_INFINITY;
        maxX = maxY = maxZ = Float.NEGATIVE_INFINITY;

        for (Vector3 v : vertices)
        {
            tempQuat.set(rx, ry, rz).multiply(v, v);

            minX = Math.min(minX, v.x);
            minY = Math.min(minY, v.y);
            minZ = Math.min(minZ, v.z);
            maxX = Math.max(maxX, v.x);
            maxY = Math.max(maxY, v.y);
            maxZ = Math.max(maxZ, v.z);
        }

        if (bounds == null)
            bounds = new Cuboid(new Vector3(minX, minY, minZ).add(position),
                                       new Vector3(maxX, maxY, maxZ).add(position));
        else
            bounds.set(maxX - minX, maxY - minY, maxZ - minZ, position);

        rotationX += rx;
        rotationY += ry;
        rotationZ += rz;
    }

    public void scale(float s)
    {
        scale(s, s, s);
    }

    public void scale(float sx, float sy, float sz)
    {
        minX = minY = minZ = Float.POSITIVE_INFINITY;
        maxX = maxY = maxZ = Float.NEGATIVE_INFINITY;

        for (Vector3 v : vertices)
        {
            v.scaleSelf(sx, sy, sz);

            minX = Math.min(minX, v.x);
            minY = Math.min(minY, v.y);
            minZ = Math.min(minZ, v.z);
            maxX = Math.max(maxX, v.x);
            maxY = Math.max(maxY, v.y);
            maxZ = Math.max(maxZ, v.z);
        }

        if (bounds == null)
            bounds = new Cuboid(new Vector3(minX, minY, minZ).add(position),
                                       new Vector3(maxX, maxY, maxZ).add(position));
        else
            bounds.set(maxX - minX, maxY - minY, maxZ - minZ, position);
    }

    public void translate(float x, float y, float z)
    {
        minX = minY = minZ = Float.POSITIVE_INFINITY;
        maxX = maxY = maxZ = Float.NEGATIVE_INFINITY;

        for (Vector3 v : vertices)
        {
            v.addSelf(x, y, z);

            minX = Math.min(minX, v.x);
            minY = Math.min(minY, v.y);
            minZ = Math.min(minZ, v.z);
            maxX = Math.max(maxX, v.x);
            maxY = Math.max(maxY, v.y);
            maxZ = Math.max(maxZ, v.z);
        }

        if (bounds == null)
            bounds = new Cuboid(new Vector3(minX, minY, minZ).add(position),
                                       new Vector3(maxX, maxY, maxZ).add(position));
        else
            bounds.set(maxX - minX, maxY - minY, maxZ - minZ, position);
    }

    public boolean intersects(Polyhedron other)
    {
        return Collision3D.testPolyhedronCollision(this, other);
    }

    public boolean contains(Vector3 p)
    {
        int i, j = getVertices().size() - 1;
        boolean oddNodes = false;

        Vector3 vi = Vector3.REUSABLE_STACK.pop();
        Vector3 vj = Vector3.REUSABLE_STACK.pop();

        for (i = 0; i < getVertices().size(); j = i++)
        {
            vi.set(getVertex(i)).addSelf(position);
            vj.set(getVertex(j)).addSelf(position);

            if ((((vi.getY() <= p.getY()) && (p.getY() < vj.getY())) ||
                 ((vj.getY() <= p.getY()) && (p.getY() < vi.getY())) ||
                 ((vj.getZ() <= p.getZ()) && (p.getZ() < vi.getZ()))) &&
                (p.getX() < (vj.getX() - vi.getX()) * (p.getY() - vi.getY()) / (vj.getY() - vi.getY()) + vi.getX()))
                oddNodes = !oddNodes;
        }

        Vector3.REUSABLE_STACK.push(vi);
        Vector3.REUSABLE_STACK.push(vj);

        return oddNodes;
    }

    public List<Vector3> getVertices()
    {
        return vertices;
    }

    public Vector3 getVertex(int index)
    {
        return vertices.get(index);
    }

    public Polyhedron copy()
    {
        return new Polyhedron(this);
    }

    public Vector3 getPosition()
    {
        return position;
    }

    public void setPosition(Vector3 position)
    {
        this.position.set(position);

        if (bounds != null)
            bounds.setPosition(position);
    }

    public int vertexCount()
    {
        return vertices.size();
    }

    public Cuboid getBounds()
    {
        if (bounds == null)
            bounds = new Cuboid(new Vector3(minX, minY, minZ).add(position),
                                       new Vector3(maxX, maxY, maxZ).add(position));

        return bounds;
    }

    public float getWidth()
    {
        return maxX - minX;
    }

    public float getHeight()
    {
        return maxY - minY;
    }

    public float getThickness()
    {
        return maxZ - minZ;
    }

    public float getRotationX()
    {
        return rotationX;
    }

    public float getRotationY()
    {
        return rotationY;
    }

    public float getRotationZ()
    {
        return rotationZ;
    }
}