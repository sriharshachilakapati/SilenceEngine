package com.shc.silenceengine.collision;

import com.shc.silenceengine.geom3d.Polyhedron;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.Vector3;

import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
public class Collision3D
{
    private static Response tmpResponse = new Response();

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

    public static boolean isSeparatingAxis(Polyhedron a, Polyhedron b, Vector3 axis, Response response)
    {
        if (response == null)
            response = tmpResponse.clear();

        Vector3 tmpOffset = Vector3.REUSABLE_STACK.pop();
        Vector2 tmpRangeA = Vector2.REUSABLE_STACK.pop();
        Vector2 tmpRangeB = Vector2.REUSABLE_STACK.pop();

        Vector3 offset = tmpOffset.set(b.getPosition()).subtractSelf(a.getPosition());
        float projectedOffset = offset.dot(axis);

        Vector2 rangeA = flattenPoints(a.getVertices(), axis, tmpRangeA);
        Vector2 rangeB = flattenPoints(b.getVertices(), axis, tmpRangeB);

        rangeB.addSelf(projectedOffset, projectedOffset);

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
            response.overlapN.set(axis.normalizeSelf());

            if (overlap < 0)
                response.overlapN.negateSelf();
        }

        Vector3.REUSABLE_STACK.push(tmpOffset);
        Vector2.REUSABLE_STACK.push(tmpRangeA);
        Vector2.REUSABLE_STACK.push(tmpRangeB);

        return false;
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

            tmpEdge1.set(v2).addSelf(a.getPosition()).subtractSelf(v1);
            tmpEdge2.set(v3).addSelf(a.getPosition()).subtractSelf(v1);

            tmpAxis.set(tmpEdge1).crossSelf(tmpEdge2).normalizeSelf();

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

            tmpEdge1.set(v2).subtractSelf(v1);
            tmpEdge2.set(v3).subtractSelf(v1);

            tmpAxis.set(tmpEdge1).crossSelf(tmpEdge2).normalizeSelf();

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
        response.overlapV.set(response.overlapN).scaleSelf(response.overlap);

        Vector3.REUSABLE_STACK.push(tmpAxis);
        Vector3.REUSABLE_STACK.push(tmpEdge1);
        Vector3.REUSABLE_STACK.push(tmpEdge2);

        return true;
    }

    public static boolean testPolyhedronCollision(Polyhedron a, Polyhedron b)
    {
        return testPolyhedronCollision(a, b, null);
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
                    "a=" + a +
                    ", b=" + b +
                    ", overlapV=" + overlapV +
                    ", overlapN=" + overlapN +
                    ", overlap=" + overlap +
                    ", aInB=" + aInB +
                    ", bInA=" + bInA +
                    '}';
        }
    }
}
