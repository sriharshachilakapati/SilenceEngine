package com.shc.silenceengine.utils;

import com.shc.silenceengine.geom2d.Polygon;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.opengl.Primitive;
import com.shc.silenceengine.math.Vector2;

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
        tracePolygon(b, polygon, Color.TRANSPARENT);
    }

    public static void tracePolygon(Batcher b, Polygon polygon, Color color)
    {
        b.begin(Primitive.LINE_LOOP);
        {
            for (Vector2 vertex : polygon.getVertices())
            {
                b.vertex(vertex.add(polygon.getPosition()));
                b.color(color);
            }
        }
        b.end();
    }

    public static void fillPolygon(Batcher b, Polygon polygon)
    {
        fillPolygon(b, polygon, Color.TRANSPARENT);
    }

    public static void fillPolygon(Batcher b, Polygon polygon, Color color)
    {
        b.begin(Primitive.TRIANGLE_FAN);
        {
            for (Vector2 vertex : polygon.getVertices())
            {
                b.vertex(vertex.add(polygon.getPosition()));
                b.color(color);
            }
        }
        b.end();
    }
}
