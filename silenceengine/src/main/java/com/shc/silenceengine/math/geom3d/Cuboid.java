/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2016 Sri Harsha Chilakapati
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
public class Cuboid
{
    public float   width;
    public float   height;
    public float   thickness;
    public Vector3 position;

    public Cuboid(Vector3 position, float width, float height, float thickness)
    {
        this.width = width;
        this.height = height;
        this.thickness = thickness;

        this.position = new Vector3(position);
    }

    public Cuboid()
    {
        width = height = thickness = 1;
        position = new Vector3();
    }

    public Cuboid(Vector3 min, Vector3 max)
    {
        Vector3 size = max.subtract(min);

        width = size.x;
        height = size.y;
        thickness = size.z;

        max.add(min);

        position = new Vector3(min).add(max).scale(0.5f);
    }

    public boolean intersects(Cuboid c)
    {
        final float tHalfWidth = width / 2;
        final float tHalfHeight = height / 2;
        final float tHalfThickness = thickness / 2;

        final float cHalfWidth = c.width / 2;
        final float cHalfHeight = c.height / 2;
        final float cHalfThickness = c.thickness / 2;

        final float aMinX = position.x - tHalfWidth;
        final float aMaxX = position.x + tHalfWidth;
        final float aMinY = position.y - tHalfHeight;
        final float aMaxY = position.y + tHalfHeight;
        final float aMinZ = position.z - tHalfThickness;
        final float aMaxZ = position.z + tHalfThickness;

        final float bMinX = c.position.x - cHalfWidth;
        final float bMaxX = c.position.x + cHalfWidth;
        final float bMinY = c.position.y - cHalfHeight;
        final float bMaxY = c.position.y + cHalfHeight;
        final float bMinZ = c.position.z - cHalfThickness;
        final float bMaxZ = c.position.z + cHalfThickness;

        return (aMinX <= bMaxX && aMaxX >= bMinX) &&
               (aMinY <= bMaxY && aMaxY >= bMinY) &&
               (aMinZ <= bMaxZ && aMaxZ >= bMinZ);
    }

    public Polyhedron createPolyhedron()
    {
        return createPolyhedron(null);
    }

    public Polyhedron createPolyhedron(Polyhedron polyhedron)
    {
        if (polyhedron == null)
            polyhedron = new Polyhedron();

        polyhedron.clearVertices();

        // Precomputed dimensions
        final float halfWidth = width / 2;
        final float halfHeight = height / 2;
        final float halfThickness = thickness / 2;

        // Front face
        polyhedron.addVertex(-halfWidth, -halfHeight, +halfThickness);
        polyhedron.addVertex(+halfWidth, -halfHeight, +halfThickness);
        polyhedron.addVertex(-halfWidth, +halfHeight, +halfThickness);
        polyhedron.addVertex(+halfWidth, +halfHeight, +halfThickness);

        // Right face
        polyhedron.addVertex(+halfWidth, +halfHeight, +halfThickness);
        polyhedron.addVertex(+halfWidth, -halfHeight, +halfThickness);
        polyhedron.addVertex(+halfWidth, +halfHeight, -halfThickness);
        polyhedron.addVertex(+halfWidth, -halfHeight, -halfThickness);

        // Back face
        polyhedron.addVertex(+halfWidth, -halfHeight, -halfThickness);
        polyhedron.addVertex(-halfWidth, -halfHeight, -halfThickness);
        polyhedron.addVertex(+halfWidth, +halfHeight, -halfThickness);
        polyhedron.addVertex(-halfWidth, +halfHeight, -halfThickness);

        // Left face
        polyhedron.addVertex(-halfWidth, +halfHeight, -halfThickness);
        polyhedron.addVertex(-halfWidth, -halfHeight, -halfThickness);
        polyhedron.addVertex(-halfWidth, +halfHeight, +halfThickness);
        polyhedron.addVertex(-halfWidth, -halfHeight, +halfThickness);

        // Bottom face
        polyhedron.addVertex(-halfWidth, -halfHeight, +halfThickness);
        polyhedron.addVertex(-halfWidth, -halfHeight, -halfThickness);
        polyhedron.addVertex(+halfWidth, -halfHeight, +halfThickness);
        polyhedron.addVertex(+halfWidth, -halfHeight, -halfThickness);

        // Move to top
        polyhedron.addVertex(+halfWidth, -halfHeight, -halfThickness);
        polyhedron.addVertex(-halfWidth, +halfHeight, +halfThickness);

        // Top face
        polyhedron.addVertex(-halfWidth, +halfHeight, +halfThickness);
        polyhedron.addVertex(+halfWidth, +halfHeight, +halfThickness);
        polyhedron.addVertex(-halfWidth, +halfHeight, -halfThickness);
        polyhedron.addVertex(+halfWidth, +halfHeight, -halfThickness);

        return polyhedron;
    }

    public void set(float width, float height, float thickness, Vector3 position)
    {
        this.position.set(position);

        this.width = width;
        this.height = height;
        this.thickness = thickness;
    }

    public float getIntersectionWidth(Cuboid aabb)
    {
        float tx1 = this.position.x - this.width / 2;
        float rx1 = aabb.position.x - aabb.width / 2;

        float tx2 = tx1 + this.width;
        float rx2 = rx1 + aabb.width;

        return tx2 > rx2 ? rx2 - tx1 : tx2 - rx1;
    }

    public float getIntersectionHeight(Cuboid aabb)
    {
        float ty1 = this.position.y - this.height / 2;
        float ry1 = aabb.position.y - aabb.height / 2;

        float ty2 = ty1 + this.height;
        float ry2 = ry1 + aabb.height;

        return ty2 > ry2 ? ry2 - ty1 : ty2 - ry1;
    }

    public float getIntersectionThickness(Cuboid aabb)
    {
        float tz1 = this.position.z - this.thickness / 2;
        float rz1 = aabb.position.z - aabb.thickness / 2;

        float tz2 = tz1 + this.thickness;
        float rz2 = rz1 + aabb.thickness;

        return tz2 > rz2 ? rz2 - tz1 : tz2 - rz1;
    }

    public void set(Cuboid cuboid)
    {
        position.set(cuboid.position);
        width = cuboid.width;
        height = cuboid.height;
        thickness = cuboid.thickness;
    }
}