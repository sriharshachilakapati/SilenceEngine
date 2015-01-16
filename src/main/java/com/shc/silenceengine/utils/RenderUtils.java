package com.shc.silenceengine.utils;

import com.shc.silenceengine.geom2d.Polygon;
import com.shc.silenceengine.geom3d.Polyhedron;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.opengl.Primitive;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.Vector3;

/**
 * @author Sri Harsha Chilakapati
 */
public final class RenderUtils
{
    private RenderUtils()
    {
    }

    public static void tracePolygon(Batcher b, Polygon polygon)
    {
        tracePolygon(b, polygon, Color.WHITE);
    }

    public static void tracePolygon(Batcher b, Polygon polygon, Color color)
    {
        tracePolygon(b, polygon, Vector2.ZERO, color);
    }

    public static void tracePolygon(Batcher b, Polygon polygon, Vector2 position, Color color)
    {
        b.begin(Primitive.LINE_LOOP);
        {
            for (Vector2 vertex : polygon.getVertices())
            {
                b.vertex(vertex.add(polygon.getPosition().add(position)));
                b.color(color);
            }
        }
        b.end();
    }

    public static void fillPolygon(Batcher b, Polygon polygon)
    {
        fillPolygon(b, polygon, Color.WHITE);
    }

    public static void fillPolygon(Batcher b, Polygon polygon, Color color)
    {
        fillPolygon(b, polygon, Vector2.ZERO, color);
    }

    public static void fillPolygon(Batcher b, Polygon polygon, Vector2 position, Color color)
    {
        b.begin(Primitive.TRIANGLE_FAN);
        {
            for (Vector2 vertex : polygon.getVertices())
            {
                b.vertex(vertex.add(polygon.getPosition().add(position)));
                b.color(color);
            }
        }
        b.end();
    }

    public static void tracePolyhedron(Batcher b, Polyhedron Polyhedron)
    {
        tracePolyhedron(b, Polyhedron, Color.WHITE);
    }

    public static void tracePolyhedron(Batcher b, Polyhedron Polyhedron, Color color)
    {
        tracePolyhedron(b, Polyhedron, Vector3.ZERO, color);
    }

    public static void tracePolyhedron(Batcher b, Polyhedron polyhedron, Vector3 position, Color color)
    {
        b.begin(Primitive.LINE_STRIP);
        {
            for (Vector3 vertex : polyhedron.getVertices())
            {
                b.vertex(vertex.add(polyhedron.getPosition().add(position)));
                b.color(color);
            }
        }
        b.end();
    }

    public static void fillPolyhedron(Batcher b, Polyhedron polyhedron)
    {
        fillPolyhedron(b, polyhedron, Color.WHITE);
    }

    public static void fillPolyhedron(Batcher b, Polyhedron polyhedron, Color color)
    {
        fillPolyhedron(b, polyhedron, Vector3.ZERO, color);
    }

    public static void fillPolyhedron(Batcher b, Polyhedron polyhedron, Vector3 position, Color color)
    {
        b.begin(Primitive.TRIANGLE_STRIP);
        {
            for (Vector3 vertex : polyhedron.getVertices())
            {
                b.vertex(vertex.add(polyhedron.getPosition().add(position)));
                b.color(color);
            }
        }
        b.end();
    }
}
