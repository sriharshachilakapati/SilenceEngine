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

package com.shc.silenceengine.utils;

import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.opengl.Primitive;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.math.geom2d.Polygon;
import com.shc.silenceengine.math.geom3d.Polyhedron;

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
        Vector2 tempVec2 = Vector2.REUSABLE_STACK.pop();

        b.begin(Primitive.LINE_LOOP);
        {
            for (Vector2 vertex : polygon.getVertices())
            {
                b.vertex(tempVec2.set(vertex).addSelf(polygon.getPosition()).addSelf(position));
                b.color(color);
                b.normal(Vector3.AXIS_Z);
            }
        }
        b.end();

        Vector2.REUSABLE_STACK.push(tempVec2);
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
        Vector2 tempVec2 = Vector2.REUSABLE_STACK.pop();

        b.begin(Primitive.TRIANGLE_FAN);
        {
            for (Vector2 vertex : polygon.getVertices())
            {
                b.vertex(tempVec2.set(vertex).addSelf(polygon.getPosition()).addSelf(position));
                b.color(color);
                b.normal(Vector3.AXIS_Z);
            }
        }
        b.end();

        Vector2.REUSABLE_STACK.push(tempVec2);
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
        Vector3 tempVec31 = Vector3.REUSABLE_STACK.pop();
        Vector3 tempVec32 = Vector3.REUSABLE_STACK.pop();
        Vector3 tempVec33 = Vector3.REUSABLE_STACK.pop();

        // For the normals!
        Vector3 tempVec34 = Vector3.REUSABLE_STACK.pop();
        Vector3 tempVec35 = Vector3.REUSABLE_STACK.pop();
        Vector3 tempVec36 = Vector3.REUSABLE_STACK.pop();

        b.begin(Primitive.LINE_STRIP);
        {
            Vector3 v1;
            Vector3 v2;
            Vector3 v3;

            // Convert Triangle Strip vertices to Triangles
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

                // Set the position of the vertices
                v1 = tempVec31.set(v1).addSelf(polyhedron.getPosition()).addSelf(position);
                v2 = tempVec32.set(v2).addSelf(polyhedron.getPosition()).addSelf(position);
                v3 = tempVec33.set(v3).addSelf(polyhedron.getPosition()).addSelf(position);

                // Compute the edges of the triangle
                tempVec34.set(v3).subtractSelf(v1);
                tempVec35.set(v2).subtractSelf(v1);

                // The normal will be the cross of the edges
                Vector3 normal = tempVec36.set(tempVec34).crossSelf(tempVec35);
                normal.addSelf(tempVec34.crossSelf(tempVec35));

                // Draw the triangle as a line strip
                b.vertex(v1);
                b.color(color);
                b.normal(normal);

                b.vertex(v2);
                b.color(color);
                b.normal(normal);

                b.vertex(v3);
                b.color(color);
                b.normal(normal);
            }
        }
        b.end();

        Vector3.REUSABLE_STACK.push(tempVec31);
        Vector3.REUSABLE_STACK.push(tempVec32);
        Vector3.REUSABLE_STACK.push(tempVec33);

        Vector3.REUSABLE_STACK.push(tempVec34);
        Vector3.REUSABLE_STACK.push(tempVec35);
        Vector3.REUSABLE_STACK.push(tempVec36);
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
        Vector3 tempVec31 = Vector3.REUSABLE_STACK.pop();
        Vector3 tempVec32 = Vector3.REUSABLE_STACK.pop();
        Vector3 tempVec33 = Vector3.REUSABLE_STACK.pop();

        // For the normals!
        Vector3 tempVec34 = Vector3.REUSABLE_STACK.pop();
        Vector3 tempVec35 = Vector3.REUSABLE_STACK.pop();
        Vector3 tempVec36 = Vector3.REUSABLE_STACK.pop();

        b.begin(Primitive.TRIANGLES);
        {
            Vector3 v1;
            Vector3 v2;
            Vector3 v3;

            // Convert Triangle Strip vertices to Triangles
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

                // Set the position of the vertices
                v1 = tempVec31.set(v1).addSelf(polyhedron.getPosition()).addSelf(position);
                v2 = tempVec32.set(v2).addSelf(polyhedron.getPosition()).addSelf(position);
                v3 = tempVec33.set(v3).addSelf(polyhedron.getPosition()).addSelf(position);

                // Compute the edges of the triangle
                tempVec34.set(v3).subtractSelf(v1);
                tempVec35.set(v2).subtractSelf(v1);

                // The normal will be the cross of the edges
                Vector3 normal = tempVec36.set(tempVec34).crossSelf(tempVec35);
                normal.addSelf(tempVec34.crossSelf(tempVec35));

                // Draw the triangle on the screen
                b.vertex(v1);
                b.color(color);
                b.normal(normal);

                b.vertex(v2);
                b.color(color);
                b.normal(normal);

                b.vertex(v3);
                b.color(color);
                b.normal(normal);
            }
        }
        b.end();

        Vector3.REUSABLE_STACK.push(tempVec31);
        Vector3.REUSABLE_STACK.push(tempVec32);
        Vector3.REUSABLE_STACK.push(tempVec33);

        Vector3.REUSABLE_STACK.push(tempVec34);
        Vector3.REUSABLE_STACK.push(tempVec35);
        Vector3.REUSABLE_STACK.push(tempVec36);
    }
}
