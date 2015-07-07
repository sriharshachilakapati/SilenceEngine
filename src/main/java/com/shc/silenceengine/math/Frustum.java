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
    private Vector4 planeLeft;
    private Vector4 planeRight;
    private Vector4 planeTop;
    private Vector4 planeBottom;
    private Vector4 planeNear;
    private Vector4 planeFar;

    private Matrix4 frustumMatrix;

    private Vector3[]  frustumCorners;
    private Vector2[]  frustumPolygonVertices;
    private Polygon    frustumPolygon;
    private Polyhedron frustumPolyhedron;

    public Frustum()
    {
        planeLeft = new Vector4();
        planeRight = new Vector4();
        planeTop = new Vector4();
        planeBottom = new Vector4();
        planeNear = new Vector4();
        planeFar = new Vector4();

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
        frustumPolyhedron.addVertex(frustumCorners[7]);
        frustumPolyhedron.addVertex(frustumCorners[6]);
        frustumPolyhedron.addVertex(frustumCorners[3]);
        frustumPolyhedron.addVertex(frustumCorners[2]);

        frustumPolyhedron.addVertex(frustumCorners[2]);
        frustumPolyhedron.addVertex(frustumCorners[6]);
        frustumPolyhedron.addVertex(frustumCorners[1]);
        frustumPolyhedron.addVertex(frustumCorners[5]);

        frustumPolyhedron.addVertex(frustumCorners[5]);
        frustumPolyhedron.addVertex(frustumCorners[4]);
        frustumPolyhedron.addVertex(frustumCorners[1]);
        frustumPolyhedron.addVertex(frustumCorners[0]);

        frustumPolyhedron.addVertex(frustumCorners[0]);
        frustumPolyhedron.addVertex(frustumCorners[4]);
        frustumPolyhedron.addVertex(frustumCorners[3]);
        frustumPolyhedron.addVertex(frustumCorners[7]);

        frustumPolyhedron.addVertex(frustumCorners[7]);
        frustumPolyhedron.addVertex(frustumCorners[4]);
        frustumPolyhedron.addVertex(frustumCorners[6]);
        frustumPolyhedron.addVertex(frustumCorners[5]);

        frustumPolyhedron.addVertex(frustumCorners[5]);
        frustumPolyhedron.addVertex(frustumCorners[3]);

        frustumPolyhedron.addVertex(frustumCorners[3]);
        frustumPolyhedron.addVertex(frustumCorners[2]);
        frustumPolyhedron.addVertex(frustumCorners[0]);
        frustumPolyhedron.addVertex(frustumCorners[1]);
    }

    public Frustum update(BaseCamera camera)
    {
        // Calculate the frustum matrix
        frustumMatrix.set(camera.getView()).multiplySelf(camera.getProjection());

        // Extract the frustum volume planes
        planeLeft.set(frustumMatrix.get(0, 3) + frustumMatrix.get(0, 0),
                frustumMatrix.get(1, 3) + frustumMatrix.get(1, 0),
                frustumMatrix.get(2, 3) + frustumMatrix.get(2, 0),
                frustumMatrix.get(3, 3) + frustumMatrix.get(3, 0))
                .normalize3Self();

        planeRight.set(frustumMatrix.get(0, 3) - frustumMatrix.get(0, 0),
                frustumMatrix.get(1, 3) - frustumMatrix.get(1, 0),
                frustumMatrix.get(2, 3) - frustumMatrix.get(2, 0),
                frustumMatrix.get(3, 3) - frustumMatrix.get(3, 0))
                .normalize3Self();

        planeTop.set(frustumMatrix.get(0, 3) - frustumMatrix.get(0, 1),
                frustumMatrix.get(1, 3) - frustumMatrix.get(1, 1),
                frustumMatrix.get(2, 3) - frustumMatrix.get(2, 1),
                frustumMatrix.get(3, 3) - frustumMatrix.get(3, 1))
                .normalize3Self();

        planeBottom.set(frustumMatrix.get(0, 3) + frustumMatrix.get(0, 1),
                frustumMatrix.get(1, 3) + frustumMatrix.get(1, 1),
                frustumMatrix.get(2, 3) + frustumMatrix.get(2, 1),
                frustumMatrix.get(3, 3) + frustumMatrix.get(3, 1))
                .normalize3Self();

        planeNear.set(frustumMatrix.get(0, 3) + frustumMatrix.get(0, 2),
                frustumMatrix.get(1, 3) + frustumMatrix.get(1, 2),
                frustumMatrix.get(2, 3) + frustumMatrix.get(2, 2),
                frustumMatrix.get(3, 3) + frustumMatrix.get(3, 2))
                .normalize3Self();

        planeFar.set(frustumMatrix.get(0, 3) - frustumMatrix.get(0, 2),
                frustumMatrix.get(1, 3) - frustumMatrix.get(1, 2),
                frustumMatrix.get(2, 3) - frustumMatrix.get(2, 2),
                frustumMatrix.get(3, 3) - frustumMatrix.get(3, 2))
                .normalize3Self();

        // Find the corner points of the volume (Polyhedron is updated automatically)
        frustumCorners[0].set(-planeLeft.w, planeTop.w, -planeFar.w);
        frustumCorners[1].set(planeRight.w, planeTop.w, -planeFar.w);
        frustumCorners[2].set(planeRight.w, planeTop.w, planeNear.w);
        frustumCorners[3].set(-planeLeft.w, planeTop.w, planeNear.w);

        frustumCorners[4].set(-planeLeft.w, -planeBottom.w, -planeFar.w);
        frustumCorners[5].set(planeRight.w, -planeBottom.w, -planeFar.w);
        frustumCorners[6].set(planeRight.w, -planeBottom.w, planeNear.w);
        frustumCorners[7].set(-planeLeft.w, -planeBottom.w, planeNear.w);

        // Calculate the 2D frustum polygon
        frustumPolygonVertices[0].set(frustumCorners[0].x, -frustumCorners[0].y);
        frustumPolygonVertices[1].set(frustumCorners[1].x, -frustumCorners[1].y);
        frustumPolygonVertices[2].set(frustumCorners[6].x, -frustumCorners[6].y);
        frustumPolygonVertices[3].set(frustumCorners[7].x, -frustumCorners[7].y);

        return this;
    }

    public boolean intersects(Polygon polygon)
    {
        return polygon.intersects(frustumPolygon);
    }

    public boolean isInside(Polygon polygon)
    {
        boolean inside = false;

        Vector3 temp = Vector3.REUSABLE_STACK.pop();
        for (Vector2 v : polygon.getVertices())
        {
            temp.set(v.x, v.y, planeNear.z).addSelf(polygon.getPosition(), 0);
            inside = isPointInside(temp);

            if (inside)
                break;
        }
        Vector3.REUSABLE_STACK.push(temp);

        return inside;
    }

    public boolean intersects(Polyhedron polyhedron)
    {
        return polyhedron.intersects(frustumPolyhedron);
    }

    public boolean isInside(Polyhedron polyhedron)
    {
        boolean inside = false;

        Vector3 temp = Vector3.REUSABLE_STACK.pop();
        for (Vector3 v : polyhedron.getVertices())
        {
            temp.set(v).addSelf(polyhedron.getPosition());
            inside = isPointInside(temp);

            if (inside)
                break;
        }
        Vector3.REUSABLE_STACK.push(temp);

        return inside;
    }

    public boolean isPointInside(Vector3 point)
    {
        return checkPlane(point, planeLeft) && checkPlane(point, planeRight) &&
               checkPlane(point, planeTop) && checkPlane(point, planeBottom) &&
               checkPlane(point, planeNear) && checkPlane(point, planeFar);
    }

    private boolean checkPlane(Vector3 point, Vector4 plane)
    {
        return plane.x * point.x + plane.y * point.y + plane.z * point.z + plane.w >= 0;
    }

    public Vector4 getPlaneLeft()
    {
        return planeLeft;
    }

    public Vector4 getPlaneRight()
    {
        return planeRight;
    }

    public Vector4 getPlaneTop()
    {
        return planeTop;
    }

    public Vector4 getPlaneBottom()
    {
        return planeBottom;
    }

    public Vector4 getPlaneNear()
    {
        return planeNear;
    }

    public Vector4 getPlaneFar()
    {
        return planeFar;
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
