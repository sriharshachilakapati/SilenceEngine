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

package com.shc.silenceengine.math;

import com.shc.silenceengine.graphics.cameras.Camera;
import com.shc.silenceengine.math.geom2d.Polygon;
import com.shc.silenceengine.math.geom3d.Polyhedron;
import com.shc.silenceengine.math.geom3d.Sphere;

/**
 * This class represents the Frustum, the volume of the camera view. Contains useful functions to check whether a shape
 * exists completely inside, or intersects the view of the camera.
 *
 * @author Sri Harsha Chilakapati
 */
public class Frustum
{
    // The plane locations in the array
    public static final int LEFT   = 0;
    public static final int RIGHT  = 1;
    public static final int TOP    = 2;
    public static final int BOTTOM = 3;
    public static final int NEAR   = 4;
    public static final int FAR    = 5;

    // The frustum corner locations in the array
    public static final int TOP_LEFT_FAR      = 0;
    public static final int TOP_RIGHT_FAR     = 1;
    public static final int TOP_RIGHT_NEAR    = 2;
    public static final int TOP_LEFT_NEAR     = 3;
    public static final int BOTTOM_LEFT_FAR   = 4;
    public static final int BOTTOM_RIGHT_FAR  = 5;
    public static final int BOTTOM_RIGHT_NEAR = 6;
    public static final int BOTTOM_LEFT_NEAR  = 7;

    // The 2D frustum corner locations in the array
    public static final int TOP_LEFT     = 0;
    public static final int TOP_RIGHT    = 1;
    public static final int BOTTOM_LEFT  = 2;
    public static final int BOTTOM_RIGHT = 3;

    // The array of all the planes of the frustum
    private Plane[] planes;

    // The frustum matrix
    private Matrix4 frustumMatrix;

    // The frustum corners, polygon vertices, polygon and polyhedron
    private Vector3[]  frustumCorners;
    private Vector2[]  frustumPolygonVertices;
    private Polygon    frustumPolygon;
    private Polyhedron frustumPolyhedron;

    public Frustum()
    {
        // Create the planes array
        planes = new Plane[6];
        for (int i = 0; i < planes.length; i++)
            planes[i] = new Plane();

        // Create the frustum matrix and corners array
        frustumMatrix = new Matrix4().initIdentity();
        frustumCorners = new Vector3[8];

        for (int i = 0; i < 8; i++)
            frustumCorners[i] = new Vector3();

        frustumPolygonVertices = new Vector2[4];

        // Create the frustum polygon
        frustumPolygon = new Polygon();
        for (int i = 0; i < 4; i++)
        {
            frustumPolygonVertices[i] = new Vector2();
            frustumPolygon.addVertex(frustumPolygonVertices[i]);
        }

        // Create the frustum polyhedron
        frustumPolyhedron = new Polyhedron();
        frustumPolyhedron.addVertex(frustumCorners[BOTTOM_LEFT_NEAR]);
        frustumPolyhedron.addVertex(frustumCorners[BOTTOM_RIGHT_NEAR]);
        frustumPolyhedron.addVertex(frustumCorners[TOP_LEFT_NEAR]);
        frustumPolyhedron.addVertex(frustumCorners[TOP_RIGHT_NEAR]);

        frustumPolyhedron.addVertex(frustumCorners[TOP_RIGHT_NEAR]);
        frustumPolyhedron.addVertex(frustumCorners[BOTTOM_RIGHT_NEAR]);
        frustumPolyhedron.addVertex(frustumCorners[TOP_RIGHT_FAR]);
        frustumPolyhedron.addVertex(frustumCorners[BOTTOM_RIGHT_FAR]);

        frustumPolyhedron.addVertex(frustumCorners[BOTTOM_RIGHT_FAR]);
        frustumPolyhedron.addVertex(frustumCorners[BOTTOM_LEFT_FAR]);
        frustumPolyhedron.addVertex(frustumCorners[TOP_RIGHT_FAR]);
        frustumPolyhedron.addVertex(frustumCorners[TOP_LEFT_FAR]);

        frustumPolyhedron.addVertex(frustumCorners[TOP_LEFT_FAR]);
        frustumPolyhedron.addVertex(frustumCorners[BOTTOM_LEFT_FAR]);
        frustumPolyhedron.addVertex(frustumCorners[TOP_LEFT_NEAR]);
        frustumPolyhedron.addVertex(frustumCorners[BOTTOM_LEFT_NEAR]);

        frustumPolyhedron.addVertex(frustumCorners[BOTTOM_LEFT_NEAR]);
        frustumPolyhedron.addVertex(frustumCorners[BOTTOM_LEFT_FAR]);
        frustumPolyhedron.addVertex(frustumCorners[BOTTOM_RIGHT_NEAR]);
        frustumPolyhedron.addVertex(frustumCorners[BOTTOM_RIGHT_FAR]);

        frustumPolyhedron.addVertex(frustumCorners[BOTTOM_RIGHT_FAR]);
        frustumPolyhedron.addVertex(frustumCorners[TOP_LEFT_NEAR]);

        frustumPolyhedron.addVertex(frustumCorners[TOP_LEFT_NEAR]);
        frustumPolyhedron.addVertex(frustumCorners[TOP_RIGHT_NEAR]);
        frustumPolyhedron.addVertex(frustumCorners[TOP_LEFT_FAR]);
        frustumPolyhedron.addVertex(frustumCorners[TOP_RIGHT_FAR]);
    }

    public Frustum update(Camera camera)
    {
        return update(camera.getProjection(), camera.getView());
    }

    public Frustum update(Matrix4 projection, Matrix4 view)
    {
        // Calculate the frustum matrix
        frustumMatrix.set(projection).multiply(view);

        // Extract the frustum volume planes
        planes[LEFT].set(frustumMatrix.get(0, 3) + frustumMatrix.get(0, 0),
                frustumMatrix.get(1, 3) + frustumMatrix.get(1, 0),
                frustumMatrix.get(2, 3) + frustumMatrix.get(2, 0),
                frustumMatrix.get(3, 3) + frustumMatrix.get(3, 0));

        planes[RIGHT].set(frustumMatrix.get(0, 3) - frustumMatrix.get(0, 0),
                frustumMatrix.get(1, 3) - frustumMatrix.get(1, 0),
                frustumMatrix.get(2, 3) - frustumMatrix.get(2, 0),
                frustumMatrix.get(3, 3) - frustumMatrix.get(3, 0));

        planes[TOP].set(frustumMatrix.get(0, 3) - frustumMatrix.get(0, 1),
                frustumMatrix.get(1, 3) - frustumMatrix.get(1, 1),
                frustumMatrix.get(2, 3) - frustumMatrix.get(2, 1),
                frustumMatrix.get(3, 3) - frustumMatrix.get(3, 1));

        planes[BOTTOM].set(frustumMatrix.get(0, 3) + frustumMatrix.get(0, 1),
                frustumMatrix.get(1, 3) + frustumMatrix.get(1, 1),
                frustumMatrix.get(2, 3) + frustumMatrix.get(2, 1),
                frustumMatrix.get(3, 3) + frustumMatrix.get(3, 1));

        planes[NEAR].set(frustumMatrix.get(0, 3) + frustumMatrix.get(0, 2),
                frustumMatrix.get(1, 3) + frustumMatrix.get(1, 2),
                frustumMatrix.get(2, 3) + frustumMatrix.get(2, 2),
                frustumMatrix.get(3, 3) + frustumMatrix.get(3, 2));

        planes[FAR].set(frustumMatrix.get(0, 3) - frustumMatrix.get(0, 2),
                frustumMatrix.get(1, 3) - frustumMatrix.get(1, 2),
                frustumMatrix.get(2, 3) - frustumMatrix.get(2, 2),
                frustumMatrix.get(3, 3) - frustumMatrix.get(3, 2));

        // Find the corner points of the volume (Polyhedron is updated automatically)
        Plane.intersection(planes[TOP], planes[LEFT], planes[FAR], frustumCorners[TOP_LEFT_FAR]);
        Plane.intersection(planes[TOP], planes[RIGHT], planes[FAR], frustumCorners[TOP_RIGHT_FAR]);
        Plane.intersection(planes[TOP], planes[RIGHT], planes[NEAR], frustumCorners[TOP_RIGHT_NEAR]);
        Plane.intersection(planes[TOP], planes[LEFT], planes[NEAR], frustumCorners[TOP_LEFT_NEAR]);

        Plane.intersection(planes[BOTTOM], planes[LEFT], planes[FAR], frustumCorners[BOTTOM_LEFT_FAR]);
        Plane.intersection(planes[BOTTOM], planes[RIGHT], planes[FAR], frustumCorners[BOTTOM_RIGHT_FAR]);
        Plane.intersection(planes[BOTTOM], planes[RIGHT], planes[NEAR], frustumCorners[BOTTOM_RIGHT_NEAR]);
        Plane.intersection(planes[BOTTOM], planes[LEFT], planes[NEAR], frustumCorners[BOTTOM_LEFT_NEAR]);

        // Calculate the 2D frustum polygon
        frustumPolygonVertices[TOP_LEFT].set(frustumCorners[TOP_LEFT_NEAR].x, frustumCorners[TOP_LEFT_NEAR].y);
        frustumPolygonVertices[TOP_RIGHT].set(frustumCorners[TOP_RIGHT_NEAR].x, frustumCorners[TOP_RIGHT_NEAR].y);
        frustumPolygonVertices[BOTTOM_RIGHT].set(frustumCorners[BOTTOM_RIGHT_NEAR].x, frustumCorners[BOTTOM_RIGHT_NEAR].y);
        frustumPolygonVertices[BOTTOM_LEFT].set(frustumCorners[BOTTOM_LEFT_NEAR].x, frustumCorners[BOTTOM_LEFT_NEAR].y);

        return this;
    }

    public boolean intersects(Polygon polygon)
    {
        Vector2 center = polygon.getCenter();

        // If the center point is inside the frustum, then the polygon should be intersecting
        if (isInside(center.x, center.y, 0))
            return true;

        // If there is at least one point that is inside the frustum, then the polygon should be intersecting
        for (Vector2 v : polygon.getVertices())
            if (isInside(v.x + polygon.getPosition().x, v.y + polygon.getPosition().y, planes[NEAR].d))
                return true;

        // Otherwise, there maybe one edge that is intersecting the frustum
        return polygon.intersects(frustumPolygon);
    }

    public boolean isInside(Polygon polygon)
    {
        // Definitely not inside if the center is not in frustum
        if (!isInside(polygon.getCenter().x, polygon.getCenter().y, planes[NEAR].d))
            return false;

        boolean inside = false;

        Vector3 temp = Vector3.REUSABLE_STACK.pop();
        for (Vector2 v : polygon.getVertices())
        {
            temp.set(v.x, v.y, planes[NEAR].d).add(polygon.getPosition(), 0);
            inside = isInside(temp);

            if (!inside)
                break;
        }
        Vector3.REUSABLE_STACK.push(temp);

        return inside;
    }

    public boolean intersects(Polyhedron polyhedron)
    {
        Vector3 position = polyhedron.getPosition();

        // If the center point is in the frustum, then it should be intersecting
        if (isInside(position))
            return true;

        // Now check if the AABB of the polyhedron intersects the frustum
        if (intersects(position, polyhedron.getWidth(), polyhedron.getHeight(), polyhedron.getThickness()))
        {
            // Special case, if the polyhedron is a sphere, then there is no need to test fully
            if (polyhedron instanceof Sphere)
                return intersects(position, ((Sphere) polyhedron).getRadius());

            // Check if at least one point of the polyhedron is inside frustum.
            for (Vector3 v : polyhedron.getVertices())
                if (isInside(v.x + position.x, v.y + position.y, v.z + position.z))
                    return true;

            // The only remaining chance for intersection is whether an edge intersects with the frustum.
            return polyhedron.intersects(frustumPolyhedron);
        }

        // If this fails too, then there is no chance that the polyhedron intersects
        return false;
    }

    public boolean intersects(Vector3 position, float radius)
    {
        if (isInside(position))
            return true;

        for (Plane plane : planes)
            if (plane.normal.dot(position) + radius + plane.d < 0)
                return false;

        return true;
    }

    public boolean intersects(Vector3 position, float width, float height, float thickness)
    {
        if (isInside(position))
            return true;

        float halfWidth = width / 2;
        float halfHeight = height / 2;
        float halfThickness = thickness / 2;

        float x = position.x;
        float y = position.y;
        float z = position.z;

        for (Plane plane : planes)
        {
            if (plane.testPoint(x + halfWidth, y + halfHeight, z + halfThickness) == Plane.Side.BACK &&
                plane.testPoint(x + halfWidth, y + halfHeight, z - halfThickness) == Plane.Side.BACK &&
                plane.testPoint(x + halfWidth, y - halfHeight, z + halfThickness) == Plane.Side.BACK &&
                plane.testPoint(x + halfWidth, y - halfHeight, z - halfThickness) == Plane.Side.BACK &&
                plane.testPoint(x - halfWidth, y + halfHeight, z + halfThickness) == Plane.Side.BACK &&
                plane.testPoint(x - halfWidth, y + halfHeight, z - halfThickness) == Plane.Side.BACK &&
                plane.testPoint(x - halfWidth, y - halfHeight, z + halfThickness) == Plane.Side.BACK &&
                plane.testPoint(x - halfWidth, y - halfHeight, z - halfThickness) == Plane.Side.BACK)
                return false;
        }

        return true;
    }

    public boolean isInside(Polyhedron polyhedron)
    {
        // Definitely not inside if the center is not in frustum
        if (!isInside(polyhedron.getPosition()))
            return false;

        boolean inside = false;

        Vector3 temp = Vector3.REUSABLE_STACK.pop();
        for (Vector3 v : polyhedron.getVertices())
        {
            temp.set(v).add(polyhedron.getPosition());
            inside = isInside(temp);

            if (!inside)
                break;
        }
        Vector3.REUSABLE_STACK.push(temp);

        return inside;
    }

    public boolean isInside(Vector3 point, float width, float height, float thickness)
    {
        if (!isInside(point))
            return false;

        for (Plane plane : planes)
        {
            float m = plane.normal.dot(point);
            float n = width / 2 * Math.abs(point.x - width / 2) +
                      height / 2 * Math.abs(point.y - height / 2) +
                      thickness / 2 * Math.abs(point.z - thickness / 2);

            if (m + n < 0)
                return false;
        }

        return true;
    }

    public boolean isInside(Vector3 point, float radius)
    {
        if (!isInside(point))
            return false;

        for (Plane plane : planes)
        {
            float m = plane.normal.dot(point);
            float n = radius * Math.abs(point.x - radius) +
                      radius * Math.abs(point.y - radius) +
                      radius * Math.abs(point.z - radius);

            if (m + n < 0)
                return false;
        }

        return true;
    }

    public boolean isInside(Vector3 point)
    {
        return isInside(point.x, point.y, point.z);
    }

    public boolean isInside(float x, float y, float z)
    {
        boolean inside = false;

        for (Plane plane : planes)
        {
            if (!(inside = plane.testPoint(x, y, z) == Plane.Side.FRONT))
                break;
        }

        return inside;
    }

    public Plane getPlane(int id)
    {
        return planes[id];
    }

    public Vector3 getCorner(int id)
    {
        return frustumCorners[id];
    }

    public Vector2 getCorner2D(int id)
    {
        return frustumPolygonVertices[id];
    }

    public Polygon getPolygon()
    {
        return frustumPolygon;
    }

    public Polyhedron getPolyhedron()
    {
        return frustumPolyhedron;
    }

    @Override
    public String toString()
    {
        return "Frustum{" +
               "planeLeft=" + getPlane(LEFT) +
               ", planeRight=" + getPlane(RIGHT) +
               ", planeTop=" + getPlane(TOP) +
               ", planeBottom=" + getPlane(BOTTOM) +
               ", planeNear=" + getPlane(NEAR) +
               ", planeFar=" + getPlane(FAR) +
               '}';
    }
}
