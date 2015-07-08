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

package com.shc.silenceengine.math;

import com.shc.silenceengine.graphics.cameras.BaseCamera;
import com.shc.silenceengine.math.geom2d.Polygon;
import com.shc.silenceengine.math.geom3d.Polyhedron;

/**
 * @author Sri Harsha Chilakapati
 */
public class Frustum
{
    // The plane locations in the array
    private static final int LEFT   = 0;
    private static final int RIGHT  = 1;
    private static final int TOP    = 2;
    private static final int BOTTOM = 3;
    private static final int NEAR   = 4;
    private static final int FAR    = 5;

    // The frustum corner locations in the array
    private static final int LEFT_TOP_FAR      = 0;
    private static final int RIGHT_TOP_FAR     = 1;
    private static final int RIGHT_TOP_NEAR    = 2;
    private static final int LEFT_TOP_NEAR     = 3;
    private static final int LEFT_BOTTOM_FAR   = 4;
    private static final int RIGHT_BOTTOM_FAR  = 5;
    private static final int RIGHT_BOTTOM_NEAR = 6;
    private static final int LEFT_BOTTOM_NEAR  = 7;

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
        frustumPolyhedron.addVertex(frustumCorners[LEFT_BOTTOM_NEAR]);
        frustumPolyhedron.addVertex(frustumCorners[RIGHT_BOTTOM_NEAR]);
        frustumPolyhedron.addVertex(frustumCorners[LEFT_TOP_NEAR]);
        frustumPolyhedron.addVertex(frustumCorners[RIGHT_TOP_NEAR]);

        frustumPolyhedron.addVertex(frustumCorners[RIGHT_TOP_NEAR]);
        frustumPolyhedron.addVertex(frustumCorners[RIGHT_BOTTOM_NEAR]);
        frustumPolyhedron.addVertex(frustumCorners[RIGHT_TOP_FAR]);
        frustumPolyhedron.addVertex(frustumCorners[RIGHT_BOTTOM_FAR]);

        frustumPolyhedron.addVertex(frustumCorners[RIGHT_BOTTOM_FAR]);
        frustumPolyhedron.addVertex(frustumCorners[LEFT_BOTTOM_FAR]);
        frustumPolyhedron.addVertex(frustumCorners[RIGHT_TOP_FAR]);
        frustumPolyhedron.addVertex(frustumCorners[LEFT_TOP_FAR]);

        frustumPolyhedron.addVertex(frustumCorners[LEFT_TOP_FAR]);
        frustumPolyhedron.addVertex(frustumCorners[LEFT_BOTTOM_FAR]);
        frustumPolyhedron.addVertex(frustumCorners[LEFT_TOP_NEAR]);
        frustumPolyhedron.addVertex(frustumCorners[LEFT_BOTTOM_NEAR]);

        frustumPolyhedron.addVertex(frustumCorners[LEFT_BOTTOM_NEAR]);
        frustumPolyhedron.addVertex(frustumCorners[LEFT_BOTTOM_FAR]);
        frustumPolyhedron.addVertex(frustumCorners[RIGHT_BOTTOM_NEAR]);
        frustumPolyhedron.addVertex(frustumCorners[RIGHT_BOTTOM_FAR]);

        frustumPolyhedron.addVertex(frustumCorners[RIGHT_BOTTOM_FAR]);
        frustumPolyhedron.addVertex(frustumCorners[LEFT_TOP_NEAR]);

        frustumPolyhedron.addVertex(frustumCorners[LEFT_TOP_NEAR]);
        frustumPolyhedron.addVertex(frustumCorners[RIGHT_TOP_NEAR]);
        frustumPolyhedron.addVertex(frustumCorners[LEFT_TOP_FAR]);
        frustumPolyhedron.addVertex(frustumCorners[RIGHT_TOP_FAR]);
    }

    public Frustum update(BaseCamera camera)
    {
        // Calculate the frustum matrix
        frustumMatrix.set(camera.getView()).multiplySelf(camera.getProjection());

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
        Plane.intersection(planes[LEFT], planes[TOP], planes[FAR], frustumCorners[LEFT_TOP_FAR]);
        Plane.intersection(planes[RIGHT], planes[TOP], planes[FAR], frustumCorners[RIGHT_TOP_FAR]);
        Plane.intersection(planes[RIGHT], planes[TOP], planes[NEAR], frustumCorners[RIGHT_TOP_NEAR]);
        Plane.intersection(planes[LEFT], planes[TOP], planes[NEAR], frustumCorners[LEFT_TOP_NEAR]);

        Plane.intersection(planes[LEFT], planes[BOTTOM], planes[FAR], frustumCorners[LEFT_BOTTOM_FAR]);
        Plane.intersection(planes[RIGHT], planes[BOTTOM], planes[FAR], frustumCorners[RIGHT_BOTTOM_FAR]);
        Plane.intersection(planes[RIGHT], planes[BOTTOM], planes[NEAR], frustumCorners[RIGHT_BOTTOM_NEAR]);
        Plane.intersection(planes[LEFT], planes[BOTTOM], planes[NEAR], frustumCorners[LEFT_BOTTOM_NEAR]);

        // Calculate the 2D frustum polygon
        frustumPolygonVertices[0].set(frustumCorners[LEFT_TOP_FAR].x, -frustumCorners[LEFT_TOP_FAR].y);
        frustumPolygonVertices[1].set(frustumCorners[RIGHT_TOP_FAR].x, -frustumCorners[RIGHT_TOP_FAR].y);
        frustumPolygonVertices[2].set(frustumCorners[RIGHT_BOTTOM_NEAR].x, -frustumCorners[RIGHT_BOTTOM_NEAR].y);
        frustumPolygonVertices[3].set(frustumCorners[LEFT_BOTTOM_NEAR].x, -frustumCorners[LEFT_BOTTOM_NEAR].y);

        return this;
    }

    public boolean intersects(Polygon polygon)
    {
        // The isInside check is faster than running the whole polygon-polygon intersection check
        return isInside(polygon) || polygon.intersects(frustumPolygon);
    }

    public boolean isInside(Polygon polygon)
    {
        boolean inside = false;

        Vector3 temp = Vector3.REUSABLE_STACK.pop();
        for (Vector2 v : polygon.getVertices())
        {
            temp.set(v.x, v.y, planes[NEAR].d).addSelf(polygon.getPosition(), 0);
            inside = isPointInside(temp);

            if (!inside)
                break;
        }
        Vector3.REUSABLE_STACK.push(temp);

        return inside;
    }

    public boolean intersects(Polyhedron polyhedron)
    {
        // The isInside check is faster than running the whole polyhedron-polyhedron intersection check
        return isInside(polyhedron) || polyhedron.intersects(frustumPolyhedron);
    }

    public boolean isInside(Polyhedron polyhedron)
    {
        boolean inside = false;

        Vector3 temp = Vector3.REUSABLE_STACK.pop();
        for (Vector3 v : polyhedron.getVertices())
        {
            temp.set(v).addSelf(polyhedron.getPosition());
            inside = isPointInside(temp);

            if (!inside)
                break;
        }
        Vector3.REUSABLE_STACK.push(temp);

        return inside;
    }

    public boolean isPointInside(Vector3 point)
    {
        return checkPlane(point, planes[LEFT]) && checkPlane(point, planes[RIGHT]) &&
               checkPlane(point, planes[TOP]) && checkPlane(point, planes[BOTTOM]) &&
               checkPlane(point, planes[NEAR]) && checkPlane(point, planes[FAR]);
    }

    private boolean checkPlane(Vector3 point, Plane plane)
    {
        return plane.getSide(point) == Plane.Side.BACK;
    }

    public Plane getPlaneLeft()
    {
        return planes[LEFT];
    }

    public Plane getPlaneRight()
    {
        return planes[RIGHT];
    }

    public Plane getPlaneTop()
    {
        return planes[TOP];
    }

    public Plane getPlaneBottom()
    {
        return planes[BOTTOM];
    }

    public Plane getPlaneNear()
    {
        return planes[NEAR];
    }

    public Plane getPlaneFar()
    {
        return planes[FAR];
    }

    @Override
    public String toString()
    {
        return "Frustum{" +
               "planeLeft=" + getPlaneLeft() +
               ", planeRight=" + getPlaneRight() +
               ", planeTop=" + getPlaneTop() +
               ", planeBottom=" + getPlaneBottom() +
               ", planeNear=" + getPlaneNear() +
               ", planeFar=" + getPlaneFar() +
               '}';
    }
}
