package com.shc.silenceengine.collision.sat2d;

import com.shc.silenceengine.geom2d.Polygon;
import com.shc.silenceengine.math.Vector2;

import java.util.List;

/**
 * @author Sri Harsha CHilakapati
 */
public final class SAT2D
{
    private SAT2D()
    {
    }

    private static Response2D tmpResponse = new Response2D();

    public static Vector2 flattenPoints(List<Vector2> vertices, Vector2 normal)
    {
        float min = Float.MAX_VALUE;
        float max = -min;

        for (Vector2 vertex : vertices)
        {
            float dot = vertex.dot(normal);

            if (dot < min) min = dot;
            if (dot > max) max = dot;
        }

        return new Vector2(min, max);
    }

    public static boolean isSeparatingAxis(Polygon a, Polygon b, Vector2 axis, Response2D response)
    {
        if (response == null)
            response = tmpResponse.clear();

        Vector2 offset = b.getPosition().subtract(a.getPosition());

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

    public static boolean testPolygonCollision(Polygon a, Polygon b, Response2D response)
    {
        if (response == null)
            response = tmpResponse.clear();

        for (int i = 0; i < a.vertexCount(); i++)
        {
            Vector2 e1 = a.getVertex(i);
            Vector2 e2 = a.getVertex((i + 1) % a.vertexCount());

            Vector2 edge = e2.subtract(e1);
            Vector2 normal = edge.perpendicular().normalize();

            if (isSeparatingAxis(a, b, normal, response))
                return false;
        }

        for (int i = 0; i < b.vertexCount(); i++)
        {
            Vector2 e1 = b.getVertex(i);
            Vector2 e2 = b.getVertex((i + 1) % b.vertexCount());

            Vector2 edge = e2.subtract(e1);
            Vector2 normal = edge.perpendicular().normalize();

            if (isSeparatingAxis(a, b, normal, response))
                return false;
        }

        response.a = a;
        response.b = b;
        response.overlapV = response.overlapN.scale(response.overlap);

        return true;
    }

    public static Response2D getResponse()
    {
        return tmpResponse;
    }
}
