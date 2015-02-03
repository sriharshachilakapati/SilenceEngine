package com.shc.silenceengine.collision;

import com.shc.silenceengine.geom2d.Polygon;
import com.shc.silenceengine.math.Vector2;

import java.util.List;

/**
 * This class contains the utilities for checking collisions in 2D.
 * Contains helper methods to check using SAT (Separating Axis Theorem)
 * This class also calculates the response, like how much the polygons
 * have overlapped, and in what direction they overlapped.
 *
 * @author Sri Harsha CHilakapati
 */
public final class Collision2D
{
    private Collision2D()
    {
    }

    private static Response tmpResponse = new Response();

    private static Vector2 tmpRangeA;
    private static Vector2 tmpRangeB;
    private static Vector2 tmpOffset;
    private static Vector2 tmpNormal;

    static
    {
        tmpRangeA = new Vector2();
        tmpRangeB = new Vector2();
        tmpOffset = new Vector2();
        tmpNormal = new Vector2();
    }

    private static Vector2 flattenPoints(List<Vector2> vertices, Vector2 normal, Vector2 projection)
    {
        float min = Float.MAX_VALUE;
        float max = -min;

        for (Vector2 vertex : vertices)
        {
            float dot = vertex.dot(normal);

            if (dot < min) min = dot;
            if (dot > max) max = dot;
        }

        return projection.set(min, max);
    }

    public static boolean isSeparatingAxis(Polygon a, Polygon b, Vector2 axis, Response response)
    {
        if (response == null)
            response = tmpResponse.clear();

        Vector2 offset = tmpOffset.set(b.getPosition()).subtractSelf(a.getPosition());

        float projectedOffset = offset.dot(axis);

        Vector2 rangeA = flattenPoints(a.getVertices(), axis, tmpRangeA);
        Vector2 rangeB = flattenPoints(b.getVertices(), axis, tmpRangeB);

        rangeB.addSelf(projectedOffset, projectedOffset);

        if (rangeA.x > rangeB.y || rangeB.x > rangeA.y)
            return true;

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

        return false;
    }

    public static boolean testPolygonCollision(Polygon a, Polygon b, Response response)
    {
        if (response == null)
            response = tmpResponse.clear();

        for (int i = 0; i < a.vertexCount(); i++)
        {
            Vector2 e1 = a.getVertex(i);
            Vector2 e2 = a.getVertex((i + 1) % a.vertexCount());

            Vector2 edge = tmpNormal.set(e2).subtractSelf(e1);
            Vector2 normal = edge.perpendicularSelf().normalizeSelf();

            if (isSeparatingAxis(a, b, normal, response))
                return false;
        }

        for (int i = 0; i < b.vertexCount(); i++)
        {
            Vector2 e1 = b.getVertex(i);
            Vector2 e2 = b.getVertex((i + 1) % b.vertexCount());

            Vector2 edge = tmpNormal.set(e2).subtractSelf(e1);
            Vector2 normal = edge.perpendicularSelf().normalizeSelf();

            if (isSeparatingAxis(a, b, normal, response))
                return false;
        }

        response.a = a;
        response.b = b;
        response.overlapV.set(response.overlapN).scaleSelf(response.overlap);
        response.intersection = true;

        return true;
    }

    public static Response getResponse()
    {
        return tmpResponse;
    }

    /**
     * @author Sri Harsha Chilakapati
     */
    public static class Response
    {
        // The polygons
        private Polygon a;
        private Polygon b;

        private Vector2 overlapV;
        private Vector2 overlapN;

        private float overlap;

        private boolean aInB;
        private boolean bInA;
        private boolean intersection;

        public Response()
        {
            a = b = null;
            overlapV = new Vector2();
            overlapN = new Vector2();

            clear();
        }

        public Response clear()
        {
            aInB = true;
            bInA = true;
            intersection = false;

            overlap = Float.MAX_VALUE;
            return this;
        }

        public Polygon getPolygonA()
        {
            return a;
        }

        public Polygon getPolygonB()
        {
            return b;
        }

        public Vector2 getMinimumTranslationVector()
        {
            return intersection ? overlapV : Vector2.ZERO;
        }

        public Vector2 getOverlapAxis()
        {
            return intersection ? overlapN : Vector2.ZERO;
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
