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

    public Frustum()
    {
        planeLeft = new Vector4();
        planeRight = new Vector4();
        planeTop = new Vector4();
        planeBottom = new Vector4();
        planeNear = new Vector4();
        planeFar = new Vector4();

        frustumMatrix = new Matrix4().initIdentity();
    }

    public Frustum update(BaseCamera camera)
    {
        frustumMatrix.set(camera.getView()).multiplySelf(camera.getProjection());

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

        return this;
    }

    public boolean intersects(Polygon polygon)
    {
        boolean intersects = false;

        Vector3 temp = Vector3.REUSABLE_STACK.pop();

        for (Vector2 v : polygon.getVertices())
        {
            temp.set(v.x, v.y, planeNear.z).addSelf(polygon.getPosition(), 0);

            intersects = isPointInside(temp);

            if (intersects)
                break;
        }

        Vector3.REUSABLE_STACK.push(temp);

        return intersects;
    }

    public boolean intersects(Polyhedron polyhedron)
    {
        boolean intersects = false;

        Vector3 temp = Vector3.REUSABLE_STACK.pop();

        for (Vector3 v : polyhedron.getVertices())
        {
            temp.set(v).addSelf(polyhedron.getPosition());

            intersects = isPointInside(temp);

            if (intersects)
                break;
        }

        Vector3.REUSABLE_STACK.push(temp);

        return intersects;
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

    public Frustum setPlaneLeft(Vector4 planeLeft)
    {
        this.planeLeft.set(planeLeft);
        return this;
    }

    public Vector4 getPlaneRight()
    {
        return planeRight;
    }

    public Frustum setPlaneRight(Vector4 planeRight)
    {
        this.planeRight.set(planeRight);
        return this;
    }

    public Vector4 getPlaneTop()
    {
        return planeTop;
    }

    public Frustum setPlaneTop(Vector4 planeTop)
    {
        this.planeTop.set(planeTop);
        return this;
    }

    public Vector4 getPlaneBottom()
    {
        return planeBottom;
    }

    public Frustum setPlaneBottom(Vector4 planeBottom)
    {
        this.planeBottom.set(planeBottom);
        return this;
    }

    public Vector4 getPlaneNear()
    {
        return planeNear;
    }

    public Frustum setPlaneNear(Vector4 planeNear)
    {
        this.planeNear.set(planeNear);
        return this;
    }

    public Vector4 getPlaneFar()
    {
        return planeFar;
    }

    public Frustum setPlaneFar(Vector4 planeFar)
    {
        this.planeFar.set(planeFar);
        return this;
    }
}
