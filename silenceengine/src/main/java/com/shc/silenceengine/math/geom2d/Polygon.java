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

package com.shc.silenceengine.math.geom2d;

import com.shc.silenceengine.collision.Collision2D;
import com.shc.silenceengine.math.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
public class Polygon
{
    private Vector2       position;
    private Vector2       center;
    private List<Vector2> vertices;
    private float         rotation;

    private float minX;
    private float minY;
    private float maxX;
    private float maxY;

    private Rectangle bounds;
    private Vector2   scale;

    public Polygon()
    {
        this.vertices = new ArrayList<>();
        this.position = new Vector2();
        this.center = new Vector2();

        clearVertices();
    }

    public void clearVertices()
    {
        vertices.clear();

        minX = minY = Float.POSITIVE_INFINITY;
        maxX = maxY = Float.NEGATIVE_INFINITY;

        rotation = 0;
    }

    public void addVertex(float x, float y)
    {
        addVertex(new Vector2(x, y));
    }

    public void addVertex(Vector2 v)
    {
        vertices.add(v);

        minX = Math.min(v.x, minX);
        minY = Math.min(v.y, minY);

        maxX = Math.max(v.x, maxX);
        maxY = Math.max(v.y, maxY);
    }

    public void scale(float s)
    {
        scale(s, s);
    }

    public void scale(float sx, float sy)
    {
        for (Vector2 v : vertices)
            v.scale(sx, sy);
    }

    public boolean intersects(Polygon other)
    {
        return Collision2D.testPolygonCollision(this, other, null);
    }

    public boolean contains(Vector2 p)
    {
        int i, j = getVertices().size() - 1;
        boolean oddNodes = false;

        Vector2 vi = Vector2.REUSABLE_STACK.pop();
        Vector2 vj = Vector2.REUSABLE_STACK.pop();

        for (i = 0; i < getVertices().size(); j = i++)
        {
            vi.set(getVertex(i)).add(position);
            vj.set(getVertex(j)).add(position);

            if ((((vi.y <= p.y) && (p.y < vj.y)) ||
                 ((vj.y <= p.y) && (p.y < vi.y))) &&
                (p.x < (vj.x - vi.x) * (p.y - vi.y) / (vj.y - vi.y) + vi.x))
                oddNodes = !oddNodes;
        }

        Vector2.REUSABLE_STACK.push(vi);
        Vector2.REUSABLE_STACK.push(vj);

        return oddNodes;
    }

    public List<Vector2> getVertices()
    {
        return vertices;
    }

    public Vector2 getVertex(int index)
    {
        return vertices.get(index);
    }

    public Polygon copy()
    {
        Polygon p = new Polygon();
        p.setPosition(getPosition());

        for (Vector2 v : vertices)
            p.addVertex(v.copy());

        return p;
    }

    public int vertexCount()
    {
        return vertices.size();
    }

    public Vector2 getPosition()
    {
        return position;
    }

    public void setPosition(Vector2 v)
    {
        this.position.set(v);

        if (bounds != null)
            bounds.setPosition(position);
    }

    public Vector2 getCenter()
    {
        if (vertexCount() == 0)
            center.set(position);
        else
            center.set(position).add((maxX - minX) / 2, (maxY - minY) / 2);

        return center;
    }

    public void setCenter(Vector2 center)
    {
        this.center.set(center);

        position.set(center);

        if (vertexCount() != 0)
            position.subtract((maxX - minX) / 2, (maxY - minY) / 2);
    }

    public void setPosition(float x, float y)
    {
        position.x = x;
        position.y = y;

        center.set(position).add((maxX - minX) / 2, (maxY - minY) / 2);

        if (bounds != null)
            bounds.setPosition(position);
    }

    public Rectangle getBounds()
    {
        updateBounds();
        return bounds;
    }

    private void updateBounds()
    {
        if (bounds == null)
            bounds = new Rectangle();

        float minX, minY, maxX, maxY;

        minX = minY = Float.POSITIVE_INFINITY;
        maxX = maxY = Float.NEGATIVE_INFINITY;

        for (Vector2 vertex : vertices)
        {
            minX = Math.min(minX, vertex.x);
            minY = Math.min(minY, vertex.y);
            maxX = Math.max(maxX, vertex.x);
            maxY = Math.max(maxY, vertex.y);
        }

        bounds.set(position.x + minX, position.y + minY, maxX - minX, maxY - minY);
    }

    public float getRotation()
    {
        return rotation;
    }

    public void setRotation(float rotation)
    {
        if (this.rotation == rotation)
            return;

        rotate(rotation - this.rotation);
    }

    public void rotate(float angle)
    {
        rotate(angle, (maxX - minX) / 2, (maxY - minY) / 2);
    }

    public void rotate(float angle, float originX, float originY)
    {
        rotation += angle;

        if (angle == 0 || this instanceof Circle)
            return;

        for (Vector2 vertex : vertices)
            vertex.subtract(originX, originY).rotate(angle).add(originX, originY);
    }

    public void translate(float x, float y)
    {
        for (Vector2 v : vertices)
            v.add(x, y);
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

    public float getMinX()
    {
        return minX;
    }

    public float getMinY()
    {
        return minY;
    }

    public float getMaxX()
    {
        return maxX;
    }

    public float getMaxY()
    {
        return maxY;
    }

    public void setScale(Vector2 scale)
    {
        this.scale.set(scale);
    }
}
