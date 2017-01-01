/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 Sri Harsha Chilakapati
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

package com.shc.silenceengine.collision;

import com.shc.silenceengine.math.Ray;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.math.geom3d.Polyhedron;

import java.util.List;

import static com.shc.silenceengine.utils.MathUtils.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class Collision3D
{
    private static Response tmpResponse = new Response();

    public static boolean testPolyhedronCollision(Polyhedron a, Polyhedron b)
    {
        return testPolyhedronCollision(a, b, null);
    }

    public static boolean testPolyhedronCollision(Polyhedron a, Polyhedron b, Response response)
    {
        if (response == null)
            response = tmpResponse.clear();

        Vector3 tmpAxis = Vector3.REUSABLE_STACK.pop();
        Vector3 tmpEdge1 = Vector3.REUSABLE_STACK.pop();
        Vector3 tmpEdge2 = Vector3.REUSABLE_STACK.pop();

        Vector3 v1, v2, v3;

        for (int v = 0; v < a.vertexCount() - 2; v++)
        {
            if ((v & 1) != 0)
            {
                // The Clock-Wise order
                v1 = a.getVertex(v);
                v2 = a.getVertex(v + 1);
                v3 = a.getVertex(v + 2);
            }
            else
            {
                // The Counter-Clock-Wise order
                v1 = a.getVertex(v);
                v2 = a.getVertex(v + 2);
                v3 = a.getVertex(v + 1);
            }

            tmpEdge1.set(v2).add(a.getPosition()).subtract(v1);
            tmpEdge2.set(v3).add(a.getPosition()).subtract(v1);

            tmpAxis.set(tmpEdge1).cross(tmpEdge2).normalize();

            // Do not test zero length axis
            if (tmpAxis.lengthSquared() == 0)
                continue;

            if (isSeparatingAxis(a, b, tmpAxis, response))
            {
                Vector3.REUSABLE_STACK.push(tmpAxis);
                Vector3.REUSABLE_STACK.push(tmpEdge1);
                Vector3.REUSABLE_STACK.push(tmpEdge2);

                return false;
            }
        }

        for (int v = 0; v < b.vertexCount() - 2; v++)
        {
            if ((v & 1) != 0)
            {
                // The Clock-Wise order
                v1 = b.getVertex(v);
                v2 = b.getVertex(v + 1);
                v3 = b.getVertex(v + 2);
            }
            else
            {
                // The Counter-Clock-Wise order
                v1 = b.getVertex(v);
                v2 = b.getVertex(v + 2);
                v3 = b.getVertex(v + 1);
            }

            tmpEdge1.set(v2).subtract(v1);
            tmpEdge2.set(v3).subtract(v1);

            tmpAxis.set(tmpEdge1).cross(tmpEdge2).normalize();

            // Do not test zero length axis
            if (tmpAxis.lengthSquared() == 0)
                continue;

            if (isSeparatingAxis(a, b, tmpAxis, response))
            {
                Vector3.REUSABLE_STACK.push(tmpAxis);
                Vector3.REUSABLE_STACK.push(tmpEdge1);
                Vector3.REUSABLE_STACK.push(tmpEdge2);

                return false;
            }
        }

        response.a = a;
        response.b = b;
        response.intersection = true;
        response.overlapV.set(response.overlapN).scale(response.overlap);

        Vector3.REUSABLE_STACK.push(tmpAxis);
        Vector3.REUSABLE_STACK.push(tmpEdge1);
        Vector3.REUSABLE_STACK.push(tmpEdge2);

        return true;
    }

    public static boolean isSeparatingAxis(Polyhedron a, Polyhedron b, Vector3 axis, Response response)
    {
        if (response == null)
            response = tmpResponse.clear();

        Vector3 tmpOffset = Vector3.REUSABLE_STACK.pop();
        Vector2 tmpRangeA = Vector2.REUSABLE_STACK.pop();
        Vector2 tmpRangeB = Vector2.REUSABLE_STACK.pop();

        Vector3 offset = tmpOffset.set(b.getPosition()).subtract(a.getPosition());
        float projectedOffset = offset.dot(axis);

        Vector2 rangeA = flattenPoints(a.getVertices(), axis, tmpRangeA);
        Vector2 rangeB = flattenPoints(b.getVertices(), axis, tmpRangeB);

        rangeB.add(projectedOffset, projectedOffset);

        if (rangeA.x > rangeB.y || rangeB.x > rangeA.y)
        {
            Vector3.REUSABLE_STACK.push(tmpOffset);
            Vector2.REUSABLE_STACK.push(tmpRangeA);
            Vector2.REUSABLE_STACK.push(tmpRangeB);

            return true;
        }

        float overlap;

        if (rangeA.x < rangeB.x)
        {
            response.aInB = false;

            if (rangeA.y < rangeB.y)
            {
                overlap = rangeA.y - rangeB.x;
                response.bInA = false;
            }
            else
            {
                float option1 = rangeA.y - rangeB.x;
                float option2 = rangeB.y - rangeA.x;
                overlap = option1 < option2 ? option1 : -option2;
            }
        }
        else
        {
            response.bInA = false;

            if (rangeA.y > rangeB.y)
            {
                overlap = rangeA.y - rangeB.x;
                response.aInB = false;
            }
            else
            {
                float option1 = rangeA.y - rangeB.x;
                float option2 = rangeB.y - rangeA.x;
                overlap = option1 < option2 ? option1 : -option2;
            }
        }

        overlap = Math.abs(overlap);

        if (overlap < response.overlap)
        {
            response.overlap = overlap;
            response.overlapN.set(axis.normalize());

            if (overlap < 0)
                response.overlapN.negate();
        }

        Vector3.REUSABLE_STACK.push(tmpOffset);
        Vector2.REUSABLE_STACK.push(tmpRangeA);
        Vector2.REUSABLE_STACK.push(tmpRangeB);

        return false;
    }

    public static boolean testPolyhedronRay(Polyhedron polyhedron, Ray ray)
    {
        Vector3 v1, v2, v3;

        for (int v = 0; v < polyhedron.vertexCount() - 2; v++)
        {
            if ((v & 1) != 0)
            {
                // The Clock-Wise order
                v1 = polyhedron.getVertex(v);
                v2 = polyhedron.getVertex(v + 1);
                v3 = polyhedron.getVertex(v + 2);
            }
            else
            {
                // The Counter-Clock-Wise order
                v1 = polyhedron.getVertex(v);
                v2 = polyhedron.getVertex(v + 2);
                v3 = polyhedron.getVertex(v + 1);
            }

            if (testTriangleRay(v1, v2, v3, ray))
                return true;
        }

        return false;
    }

    public static boolean testTriangleRay(Vector3 v1, Vector3 v2, Vector3 v3, Ray ray)
    {
        // https://en.wikipedia.org/wiki/M%C3%B6ller%E2%80%93Trumbore_intersection_algorithm
        boolean result = false;

        // Find vectors for two edges sharing v1
        Vector3 e1 = Vector3.REUSABLE_STACK.pop();
        Vector3 e2 = Vector3.REUSABLE_STACK.pop();

        e1.set(v2).subtract(v1);  // e1 = v2 - v1
        e2.set(v3).subtract(v1);  // e2 = v3 - v1

        // Begin calculating determinant - also used for calculating u perimeter
        Vector3 p = Vector3.REUSABLE_STACK.pop();
        p.set(ray.direction).cross(e2);  // p = cross(rayDir, e2)

        // If determinant is near zero, ray lies in plane of triangle or ray is parallel to plan of triangle
        float det = e1.dot(p);

        Vector3 t1 = null;
        Vector3 q = null;

        // Not culling
        if (det > -EPSILON && det < EPSILON)
            result = false;
        else
        {
            float invDet = 1f / det;

            t1 = Vector3.REUSABLE_STACK.pop();
            t1.set(ray.origin).subtract(v1); // t1 = rayOrigin - v1

            // Calculate u perimeter and test bound
            float u = t1.dot(p) * invDet;

            // The intersection lies outside of the triangle
            if (u < 0f || u > 1f)
                result = false;
            else
            {
                // Prepare to test v parameter
                q = Vector3.REUSABLE_STACK.pop();
                q.set(t1).cross(e1); // q = cross(t1, e1)

                // Calculate v parameter and test bound
                float v = ray.direction.dot(q) * invDet;

                // The intersection lies outside of the triangle
                if (v < 0f || v + u > 1f)
                    result = false;
                else
                {
                    float t = e2.dot(q) * invDet;
                    if (t > EPSILON)
                        // Intersection!!
                        result = true;
                }
            }
        }

        // Free all temporary variables
        Vector3.REUSABLE_STACK.push(e1);
        Vector3.REUSABLE_STACK.push(e2);
        Vector3.REUSABLE_STACK.push(p);

        if (t1 != null)
            Vector3.REUSABLE_STACK.push(t1);
        if (q != null)
            Vector3.REUSABLE_STACK.push(q);

        return result;
    }

    private static Vector2 flattenPoints(List<Vector3> vertices, Vector3 axis, Vector2 projection)
    {
        float min = axis.dot(vertices.get(0));
        float max = min;

        for (Vector3 v : vertices)
        {
            float dot = axis.dot(v);

            if (dot < min) min = dot;
            if (dot > max) max = dot;
        }

        return projection.set(min, max);
    }

    public static Response getResponse()
    {
        return tmpResponse;
    }

    public static class Response
    {
        private Polyhedron a;
        private Polyhedron b;

        private Vector3 overlapV;
        private Vector3 overlapN;

        private float overlap;

        private boolean aInB;
        private boolean bInA;
        private boolean intersection;

        public Response()
        {
            a = b = null;
            overlapV = new Vector3();
            overlapN = new Vector3();

            clear();
        }

        public Response clear()
        {
            aInB = true;
            bInA = true;
            intersection = false;

            overlap = Float.POSITIVE_INFINITY;
            return this;
        }

        public Polyhedron getPolygonA()
        {
            return a;
        }

        public Polyhedron getPolygonB()
        {
            return b;
        }

        public Vector3 getMinimumTranslationVector()
        {
            return intersection ? overlapV : Vector3.ZERO;
        }

        public Vector3 getOverlapAxis()
        {
            return intersection ? overlapN : Vector3.ZERO;
        }

        public float getOverlapDistance()
        {
            return intersection ? overlap : 0;
        }

        public boolean isAInsideB()
        {
            return aInB && intersection;
        }

        public boolean isBInsideA()
        {
            return bInA && intersection;
        }

        @Override
        public String toString()
        {
            return "Response{" +
                   "a=" + getPolygonA() +
                   ", b=" + getPolygonB() +
                   ", overlapV=" + getMinimumTranslationVector() +
                   ", overlapN=" + getOverlapAxis() +
                   ", overlap=" + getOverlapDistance() +
                   ", aInB=" + isAInsideB() +
                   ", bInA=" + isBInsideA() +
                   '}';
        }
    }
}
