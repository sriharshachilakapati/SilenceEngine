package com.shc.silenceengine.geom2d;

import com.shc.silenceengine.math.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
public class Polygon
{
    private Vector2            position;
    private Vector2            center;
    private List<Vector2>      vertices;
    private float              rotation;

    private float minX;
    private float minY;
    private float maxX;
    private float maxY;

    private Rectangle bounds;

    public Polygon()
    {
        this.vertices = new ArrayList<>();
        this.position = new Vector2();
        this.center   = new Vector2();

        clearVertices();
    }

    protected void clearVertices()
    {
        vertices.clear();

        minX = minY = Float.POSITIVE_INFINITY;
        maxX = maxY = Float.NEGATIVE_INFINITY;
    }

    protected void addVertex(Vector2 v)
    {
        vertices.add(v);

        minX = Math.min(v.x, minX);
        minY = Math.min(v.y, minY);

        maxX = Math.max(v.x, maxX);
        maxY = Math.max(v.y, maxY);
    }

    public void rotate(float angle)
    {
        angle = (float) Math.toRadians(angle);

        if (angle == 0 || this instanceof Circle)
            return;

        float s = (float) Math.sin(angle);
        float c = (float) Math.cos(angle);

        float width = maxX - minX;
        float height = maxY - minY;

        float originX = width / 2;
        float originY = height / 2;

        float minX, maxX, minY, maxY;

        minX = minY = Float.POSITIVE_INFINITY;
        maxX = maxY = Float.NEGATIVE_INFINITY;

        for (Vector2 vertex : vertices)
        {
            Vector2 v = vertex.subtract(originX, originY);

            float xNew = v.getX() * c - v.getY() * s;
            float yNew = v.getX() * s + v.getY() * c;

            vertex.setX(xNew + originX);
            vertex.setY(yNew + originY);

            minX = Math.min(xNew + originX, minX);
            minY = Math.min(yNew + originY, minY);

            maxX = Math.max(xNew + originX, maxX);
            maxY = Math.max(yNew + originY, maxY);
        }

        rotation += Math.toDegrees(angle);
        bounds = new Rectangle(position.getX() + minX, position.getY() + minY, maxX - minX, maxY - minY);
    }

    public boolean intersects(Polygon other)
    {
        for (int x = 0; x < 2; x++)
        {
            Polygon polygon = (x == 0) ? this : other;

            for (int i1 = 0; i1 < polygon.vertexCount(); i1++)
            {
                int i2 = (i1 + 1) % polygon.vertexCount();
                Vector2 p1 = polygon.getVertex(i1).add(polygon.getPosition());
                Vector2 p2 = polygon.getVertex(i2).add(polygon.getPosition());

                Vector2 normal = new Vector2(p2.getY() - p1.getY(), p1.getX() - p2.getX());

                double minA = Double.POSITIVE_INFINITY;
                double maxA = Double.NEGATIVE_INFINITY;
                double minB = minA;
                double maxB = maxA;

                for (Vector2 p : this.getVertices())
                {
                    double projected = normal.dot(p.add(position));

                    if (projected < minA) minA = projected;
                    if (projected > maxA) maxA = projected;
                }

                for (Vector2 p : other.getVertices())
                {
                    double projected = normal.dot(p.add(other.getPosition()));

                    if (projected < minB) minB = projected;
                    if (projected > maxB) maxB = projected;
                }

                // Separating axis
                if (maxA < minB || maxB < minA)
                    return false;
            }
        }

        return true;
    }

//    public Vector2 getNearestVertex(Vector2 vertex)
//    {
//        Vector2 nearest = vertex;
//        Vector2 direction = vertex.subtract(center);
//
//        float angle = (float) Math.atan2(direction.y, direction.x);
//
//        float cosAngle = (float) Math.cos(angle);
//        float sinAngle = (float) Math.sin(angle);
//
//        while (!contains(nearest))
//        {
//            nearest = nearest.subtract(new Vector2(cosAngle, sinAngle));
//            System.out.println(nearest);
//        }
//
//        return nearest;
//    }

//    public double getOverlapDistance(Polygon other)
//    {
//        double overlap = 0;
//
//        for (int x = 0; x < 2; x++)
//        {
//            Polygon polygon = (x == 0) ? this : other;
//
//            for (int i1 = 0; i1 < polygon.vertexCount(); i1++)
//            {
//                int i2 = (i1 + 1) % polygon.vertexCount();
//                Vector2 p1 = polygon.getVertex(i1).add(polygon.getPosition());
//                Vector2 p2 = polygon.getVertex(i2).add(polygon.getPosition());
//
//                Vector2 normal = new Vector2(p2.getY() - p1.getY(), p1.getX() - p2.getX()).normalize();
//
//                double minA = Double.POSITIVE_INFINITY;
//                double maxA = Double.NEGATIVE_INFINITY;
//                double minB = minA;
//                double maxB = maxA;
//
//                for (Vector2 p : this.getVertices())
//                {
//                    double projected = normal.dot(p.add(position));
//
//                    if (projected < minA) minA = projected;
//                    if (projected > maxA) maxA = projected;
//                }
//
//                for (Vector2 p : other.getVertices())
//                {
//                    double projected = normal.dot(p.add(other.getPosition()));
//
//                    if (projected < minB) minB = projected;
//                    if (projected > maxB) maxB = projected;
//                }
//
//                if (!(maxA < minB || maxB < minA))
//                {
//                    // Not a separating axis, calculate penetration
//                    if (minA < minB)
//                    {
//                        // A ends before B does. We have to pull A out of B
//                        if (maxA < maxB)
//                        {
//                            overlap = maxA - minB;
//                            // B is fully inside A.  Pick the shortest way out.
//                        }
//                        else
//                        {
//                            double option1 = maxA - minB;
//                            double option2 = maxB - minA;
//                            overlap = option1 < option2 ? option1 : -option2;
//                        }
//                        // B starts further left than A
//                    }
//                    else
//                    {
//                        // B ends before A ends. We have to push A out of B
//                        if (maxA > maxB)
//                        {
//                            overlap = minA - maxB;
//                            // A is fully inside B.  Pick the shortest way out.
//                        }
//                        else
//                        {
//                            double option1 = maxA - minB;
//                            double option2 = maxB - minA;
//                            overlap = option1 < option2 ? option1 : -option2;
//                        }
//                    }
//
//                    overlap = Math.abs(overlap);
//                }
//                else
//                    return 0;
//            }
//        }
//
//        return overlap;
//    }

    public boolean contains(Vector2 p)
    {
        int i, j = getVertices().size() - 1;
        boolean oddNodes = false;

        for (i = 0; i < getVertices().size(); j = i++)
        {
            Vector2 vi = getVertex(i).add(position);
            Vector2 vj = getVertex(j).add(position);

            if ((((vi.getY() <= p.getY()) && (p.getY() < vj.getY())) ||
                 ((vj.getY() <= p.getY()) && (p.getY() < vi.getY()))) &&
                (p.getX() < (vj.getX() - vi.getX()) * (p.getY() - vi.getY()) / (vj.getY() - vi.getY()) + vi.getX()))
                oddNodes = !oddNodes;
        }

        return oddNodes;
    }

    public Polygon copy()
    {
        Polygon p = new Polygon();
        p.setPosition(getPosition());
        vertices.forEach(p::addVertex);

        return p;
    }

    public int vertexCount()
    {
        return vertices.size();
    }

    public List<Vector2> getVertices()
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

    public Vector2 getCenter()
    {
        return center;
    }

    public void setCenter(Vector2 center)
    {
        setPosition(center.subtract(new Vector2((maxX - minX)/2, (maxY - minY)/2)));
    }

    public Rectangle getBounds()
    {
        if (bounds == null)
            bounds = new Rectangle(position.getX(), position.getY(), maxX - minX, maxY - minY);

        return bounds;
    }

    public void setPosition(Vector2 v)
    {
        this.position = v;
        this.center   = position.add(new Vector2((maxX - minX)/2, (maxY - minY)/2));

        if (bounds != null)
            bounds.setPosition(v);
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
        if (o == null || getClass() != o.getClass()) return false;

        Polygon polygon = (Polygon) o;

        return Float.compare(polygon.maxX, maxX) == 0 &&
               Float.compare(polygon.maxY, maxY) == 0 &&
               Float.compare(polygon.minX, minX) == 0 &&
               Float.compare(polygon.minY, minY) == 0 &&
               Float.compare(polygon.rotation, rotation) == 0 &&
               bounds.equals(polygon.bounds) &&
               center.equals(polygon.center) &&
               position.equals(polygon.position) &&
               vertices.equals(polygon.vertices);
    }

    @Override
    public int hashCode()
    {
        int result = position.hashCode();
        result = 31 * result + center.hashCode();
        result = 31 * result + vertices.hashCode();
        result = 31 * result + (rotation != +0.0f ? Float.floatToIntBits(rotation) : 0);
        result = 31 * result + (minX != +0.0f ? Float.floatToIntBits(minX) : 0);
        result = 31 * result + (minY != +0.0f ? Float.floatToIntBits(minY) : 0);
        result = 31 * result + (maxX != +0.0f ? Float.floatToIntBits(maxX) : 0);
        result = 31 * result + (maxY != +0.0f ? Float.floatToIntBits(maxY) : 0);
        result = 31 * result + bounds.hashCode();
        return result;
    }

    @Override
    public String toString()
    {
        return "Polygon{" +
               "position=" + position +
               ", center=" + center +
               ", vertices=" + vertices +
               ", rotation=" + rotation +
               ", minX=" + minX +
               ", minY=" + minY +
               ", maxX=" + maxX +
               ", maxY=" + maxY +
               ", bounds=" + bounds +
               '}';
    }
}
