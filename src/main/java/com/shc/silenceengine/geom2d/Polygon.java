package com.shc.silenceengine.geom2d;

import com.shc.silenceengine.math.Vector2;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Sri Harsha Chilakapati
 */
public class Polygon
{
    private ArrayList<Vector2> vertices;

    public Polygon(Vector2... vertices)
    {
        this.vertices = new ArrayList<>();

        Collections.addAll(this.vertices, vertices);
    }

    public Polygon(ArrayList<Vector2> vertices)
    {
        this.vertices = new ArrayList<>();
        this.vertices.addAll(vertices);
    }

    public Polygon()
    {
        this.vertices = new ArrayList<>();
    }

    protected void addVertex(Vector2 v)
    {
        vertices.add(v);
    }

    public boolean intersects(Polygon other)
    {
        for (int x=0; x<2; x++)
        {
            Polygon polygon = (x == 0) ? this : other;

            for (int i1=0; i1<polygon.vertexCount(); i1++)
            {
                int     i2 = (i1 + 1) % polygon.vertexCount();
                Vector2 p1 = polygon.getVertex(i1);
                Vector2 p2 = polygon.getVertex(i2);

                Vector2 normal = new Vector2(p2.getY() - p1.getY(), p1.getX() - p2.getX());

                double minA = Double.MAX_VALUE;
                double maxA = Double.MIN_VALUE;
                double minB = minA;
                double maxB = maxA;

                for (Vector2 p : this.getVertices())
                {
                    double projected = normal.dot(p);

                    if (projected < minA) minA = projected;
                    if (projected > maxA) maxA = projected;
                }

                for (Vector2 p : other.getVertices())
                {
                    double projected = normal.dot(p);

                    if (projected < minB) minB = projected;
                    if (projected > maxB) maxB = projected;
                }

                if (maxA < minB || maxB < minA)
                    return false;
            }
        }

        return true;
    }

    public int vertexCount()
    {
        return vertices.size();
    }

    public ArrayList<Vector2> getVertices()
    {
        return vertices;
    }

    public Vector2 getVertex(int index)
    {
        return vertices.get(index);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Polygon)) return false;

        Polygon polygon = (Polygon) o;

        return vertices.equals(polygon.vertices);
    }

    @Override
    public int hashCode()
    {
        return vertices.hashCode();
    }

    @Override
    public String toString()
    {
        return "Polygon{" +
               "vertices=" + vertices +
               '}';
    }
}
