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

    private static Vector2 flattenPoints(List<Vector3> vertices, Vector3 axis)
    {
        float min = axis.dot(vertices.get(0));
        float max = min;

        for (Vector3 v : vertices)
        {
            float dot = axis.dot(v);

            if (dot < min) min = dot;
            if (dot > max) max = dot;
        }

        return new Vector2(min, max);
    }

    public static boolean isSeparatingAxis(Polyhedron a, Polyhedron b, Vector3 axis, Response response)
    {
        if (response == null)
            response = tmpResponse.clear();

        Vector3 offset = b.getPosition().subtract(a.getPosition());
        float projectedOffset = offset.dot(axis);

        Vector2 rangeA = flattenPoints(a.getVertices(), axis);
        Vector2 rangeB = flattenPoints(b.getVertices(), axis);

        rangeB = rangeB.add(projectedOffset, projectedOffset);

        if (rangeA.x > rangeB.y || rangeB.x > rangeA.y)
            return true;

        float overlap = 0;

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
                response.overlapN = response.overlapN.negate();
        }

        return false;
    }

    public static boolean testPolyhedronCollision(Polyhedron a, Polyhedron b, Response response)
    {
        if (response == null)
            response = tmpResponse.clear();

        for (int i = 0; i < a.vertexCount(); i++)
        {
            Vector3 e1 = a.getVertex(i);
            Vector3 e2 = a.getVertex((i + 1) % a.vertexCount());

            Vector3 axis = e1.cross(e2).normalize();

            if (isSeparatingAxis(a, b, axis, response))
                return false;
        }

        for (int i = 0; i < b.vertexCount(); i++)
        {
            Vector3 e1 = b.getVertex(i);
            Vector3 e2 = b.getVertex((i + 1) % b.vertexCount());

            Vector3 axis = e1.cross(e2).normalize();

            if (isSeparatingAxis(a, b, axis, response))
                return false;
        }

        response.a = a;
        response.b = b;
        response.overlapV = response.overlapN.scale(response.overlap);

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

            overlap = Float.MAX_VALUE;
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
            return overlapV;
        }

        public Vector3 getOverlapAxis()
        {
            return overlapN;
        }

        public float getOverlapDistance()
        {
            return overlap;
        }

        public boolean isAInsideB()
        {
            return aInB;
        }

        public boolean isBInsideA()
        {
            return bInA;
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
