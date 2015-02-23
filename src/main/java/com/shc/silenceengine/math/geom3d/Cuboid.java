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

import com.shc.silenceengine.math.Vector3;

/**
 * @author Sri Harsha Chilakapati
 */
public class Cuboid extends Polyhedron
{
    private float width;
    private float height;
    private float thickness;

    private Vector3[] vertices;

    public Cuboid(Vector3 position, float width, float height, float thickness)
    {
        this();

        this.width = width;
        this.height = height;
        this.thickness = thickness;

        setPosition(position);

        updateVertices();
    }

    public Cuboid()
    {
        vertices = new Vector3[26];

        for (int i = 0; i < vertices.length; i++)
            vertices[i] = new Vector3();

        width = height = thickness = 1;

        setPosition(Vector3.ZERO);
        updateVertices();
    }

    private void updateVertices()
    {
        clearVertices();

        // Front face
        addVertex(vertices[0].set(-width / 2, -height / 2, +thickness / 2));
        addVertex(vertices[1].set(+width / 2, -height / 2, +thickness / 2));
        addVertex(vertices[2].set(-width / 2, +height / 2, +thickness / 2));
        addVertex(vertices[3].set(+width / 2, +height / 2, +thickness / 2));

        // Right face
        addVertex(vertices[4].set(+width / 2, +height / 2, +thickness / 2));
        addVertex(vertices[5].set(+width / 2, -height / 2, +thickness / 2));
        addVertex(vertices[6].set(+width / 2, +height / 2, -thickness / 2));
        addVertex(vertices[7].set(+width / 2, -height / 2, -thickness / 2));

        // Back face
        addVertex(vertices[8].set(+width / 2, -height / 2, -thickness / 2));
        addVertex(vertices[9].set(-width / 2, -height / 2, -thickness / 2));
        addVertex(vertices[10].set(+width / 2, +height / 2, -thickness / 2));
        addVertex(vertices[11].set(-width / 2, +height / 2, -thickness / 2));

        // Left face
        addVertex(vertices[12].set(-width / 2, +height / 2, -thickness / 2));
        addVertex(vertices[13].set(-width / 2, -height / 2, -thickness / 2));
        addVertex(vertices[14].set(-width / 2, +height / 2, +thickness / 2));
        addVertex(vertices[15].set(-width / 2, -height / 2, +thickness / 2));

        // Bottom face
        addVertex(vertices[16].set(-width / 2, -height / 2, +thickness / 2));
        addVertex(vertices[17].set(-width / 2, -height / 2, -thickness / 2));
        addVertex(vertices[18].set(+width / 2, -height / 2, +thickness / 2));
        addVertex(vertices[19].set(+width / 2, -height / 2, -thickness / 2));

        // Move to top
        addVertex(vertices[20].set(+width / 2, -height / 2, -thickness / 2));
        addVertex(vertices[21].set(-width / 2, +height / 2, +thickness / 2));

        // Top face
        addVertex(vertices[22].set(-width / 2, +height / 2, +thickness / 2));
        addVertex(vertices[23].set(+width / 2, +height / 2, +thickness / 2));
        addVertex(vertices[24].set(-width / 2, +height / 2, -thickness / 2));
        addVertex(vertices[25].set(+width / 2, +height / 2, -thickness / 2));
    }

    public Cuboid(Vector3 min, Vector3 max)
    {
        this();

        Vector3 size = max.subtract(min);

        width = size.x;
        height = size.y;
        thickness = size.z;

        setPosition(min.add(max).scaleSelf(0.5f));

        updateVertices();
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