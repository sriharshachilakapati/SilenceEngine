package com.shc.silenceengine.geom2d;

import com.shc.silenceengine.math.Vector2;

import java.util.ArrayList;
import java.util.Collections;

import static com.shc.silenceengine.utils.MathUtils.add;

/**
 * @author Sri Harsha Chilakapati
 */
public class Polygon
{
    private Vector2            position;
    private ArrayList<Vector2> vertices;
    private float              rotation;

    private float minX;
    private float minY;
    private float maxX;
    private float maxY;

    public Polygon(Vector2 position, Vector2... vertices)
    {
        this.vertices = new ArrayList<>();
        this.position = position;

        Collections.addAll(this.vertices, vertices);
    }

    public Polygon(Vector2 position, ArrayList<Vector2> vertices)
    {
        this.position = position;
        this.vertices = new ArrayList<>();
        this.vertices.addAll(vertices);
    }

    public Polygon()
    {
        this.vertices = new ArrayList<>();
        this.position = new Vector2();
    }

    protected void clearVertices()
    {
        vertices.clear();

        minX = minY = Float.MAX_VALUE;
        maxX = maxY = Float.MIN_VALUE;
    }

    protected void addVertex(Vector2 v)
    {
        vertices.add(v);

        minX = Math.min(v.getX(), minX);
        minY = Math.min(v.getY(), minY);

        maxX = Math.max(v.getX(), maxX);
        maxY = Math.max(v.getY(), maxX);
    }

    public void rotate(float angle)
    {
        if (angle == 0 || this instanceof Circle)
            return;

        float s = (float) Math.sin(angle);
        float c = (float) Math.cos(angle);

        float width = maxX - minX;
        float height = maxY - minY;

        float originX = width / 2;
        float originY = height / 2;

        for (Vector2 vertex : vertices)
        {
            vertex.subtract(originX, originY);

            float xNew = vertex.getX() * c - vertex.getY() * s;
            float yNew = vertex.getX() * s + vertex.getY() * c;

            vertex.setX(xNew + originX);
            vertex.setY(yNew + originY);
        }

        rotation += angle;
    }

    public boolean intersects(Polygon other)
    {
        for (int x = 0; x < 2; x++)
        {
            Polygon polygon = (x == 0) ? this : other;

            for (int i1 = 0; i1 < polygon.vertexCount(); i1++)
            {
                int i2 = (i1 + 1) % polygon.vertexCount();
                Vector2 p1 = add(polygon.getVertex(i1), polygon.getPosition());
                Vector2 p2 = add(polygon.getVertex(i2), polygon.getPosition());

                Vector2 normal = new Vector2(p2.getY() - p1.getY(), p1.getX() - p2.getX());

                double minA = Double.MAX_VALUE;
                double maxA = Double.MIN_VALUE;
                double minB = minA;
                double maxB = maxA;

                for (Vector2 p : this.getVertices())
                {
                    double projected = normal.dot(add(p, position));

                    if (projected < minA) minA = projected;
                    if (projected > maxA) maxA = projected;
                }

                for (Vector2 p : other.getVertices())
                {
                    double projected = normal.dot(add(p, other.getPosition()));

                    if (projected < minB) minB = projected;
                    if (projected > maxB) maxB = projected;
                }

                if (maxA < minB || maxB < minA)
                    return false;
            }
        }

        return true;
    }

    public boolean contains(Point p)
    {
        int i, j = getVertices().size() - 1;
        boolean oddNodes = false;

        for (i = 0; i < getVertices().size(); j = i++)
        {
            Vector2 vi = add(getVertex(i), position);
            Vector2 vj = add(getVertex(j), position);

            if ((((vi.getY() <= p.getY()) && (p.getY() < vj.getY())) ||
                 ((vj.getY() <= p.getY()) && (p.getY() < vi.getY()))) &&
                (p.getX() < (vj.getX() - vi.getX()) * (p.getY() - vi.getY()) / (vj.getY() - vi.getY()) + vi.getX()))
                oddNodes = !oddNodes;
        }

        return oddNodes;
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

    public Vector2 getPosition()
    {
        return position;
    }

    public Rectangle getBounds()
    {
        return new Rectangle(position.getX(), position.getY(), maxX - minX, maxY - minY);
    }

    public void setPosition(Vector2 v)
    {
        this.position = v;
    }

    public float getRotation()
    {
        return rotation;
    }

    public void setRotation(float rotation)
    {
        rotate(rotation - this.rotation);
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
