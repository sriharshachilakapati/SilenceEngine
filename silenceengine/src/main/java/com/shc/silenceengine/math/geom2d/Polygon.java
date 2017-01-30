/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 Sri Harsha Chilakapati
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
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.Image;
import com.shc.silenceengine.math.Vector2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
public class Polygon
{
    private Vector2       position;
    private List<Vector2> vertices;
    private float         rotation;

    private float minX;
    private float minY;
    private float maxX;
    private float maxY;

    private float scaleX;
    private float scaleY;

    private Rectangle bounds;

    public Polygon()
    {
        this.vertices = new ArrayList<>();
        this.position = new Vector2();

        scaleX = scaleY = 1;

        clearVertices();
    }

    public static Polygon createConvexHull(Image image)
    {
        return createConvexHull(image, 0, 0, image.getWidth(), image.getHeight());
    }

    public static Polygon createConvexHull(Image image, Rectangle src)
    {
        return createConvexHull(image, (int) src.x, (int) src.y, (int) src.width, (int) src.height);
    }

    public static Polygon createConvexHull(Image image, int srcX, int srcY, int srcW, int srcH)
    {
        Polygon polygon = new Polygon();

        List<Vector2> vertices = new ArrayList<>();
        Color pixelOut = Color.REUSABLE_STACK.pop();

        // Start scanning the image from left to right, and then from top to bottom
        for (int y = srcY; y < Math.min(srcY + srcH, image.getHeight()); y++)
        {
            boolean found = false;

            for (int x = srcX; x < Math.min(srcX + srcW, image.getWidth()); x++)
            {
                image.getPixel(x, y, pixelOut);

                // Add the pixel if it is the corner of the src rect and is not transparent
                if ((srcX == x || srcX + srcW - 1 == x) && pixelOut.a != 0)
                {
                    vertices.add(new Vector2(x, y));
                    found = !found;
                }

                // Add the first non-transparent pixel
                else if (!found && pixelOut.a != 0)
                {
                    vertices.add(new Vector2(x, y));
                    found = true;
                }
                // Add the last non-transparent pixel
                else if (found && pixelOut.a == 0)
                {
                    vertices.add(new Vector2(x - 1, y));
                    found = false;
                }
            }
        }

        // We now have a concave polygon, It's time to compute a convex hull over it. This follows Andrew's
        // algorithm (Page 65, Real-time Collision Detection by Christer Ericson). Code adapted from wiki at
        // https://en.wikibooks.org/wiki/Algorithm_Implementation/Geometry/Convex_hull/Monotone_chain

        List<Vector2> convexHull = new ArrayList<>();

        // Sort the vertices lexicographically (x first, and y in case of tie)
        Collections.sort(vertices, (v1, v2) -> v1.x == v2.x ? (int) (v1.y - v2.y) : (int) (v1.x - v2.x));

        int k = 0;

        Vector2 edge1 = Vector2.REUSABLE_STACK.pop();
        Vector2 edge2 = Vector2.REUSABLE_STACK.pop();

        // Calculate the lower hull
        for (Vector2 vertex : vertices)
        {
            while (k >= 2)
            {
                Vector2 o = convexHull.get(k - 2);
                Vector2 a = convexHull.get(k - 1);

                edge1.set(o).subtract(a);
                edge2.set(o).subtract(vertex);

                if (edge1.zCross(edge2) <= 0)
                    k--;
                else
                    break;
            }

            if (convexHull.size() <= k++)
                convexHull.add(vertex);
            else
                convexHull.add(k - 1, vertex);
        }

        // Calculate the upper hull
        for (int i = vertices.size() - 2, t = k + 1; i >= 0; i--)
        {
            while (k >= t)
            {
                Vector2 o = convexHull.get(k - 2);
                Vector2 a = convexHull.get(k - 1);
                Vector2 b = vertices.get(i);

                edge1.set(o).subtract(a);
                edge2.set(o).subtract(b);

                if (edge1.zCross(edge2) <= 0)
                    k--;
                else
                    break;
            }

            if (convexHull.size() <= k++)
                convexHull.add(vertices.get(i));
            else
                convexHull.add(k - 1, vertices.get(i));
        }

        // Remove duplicate vertices
        if (k > 1)
            convexHull = convexHull.subList(0, k - 1);

        vertices = convexHull;

        Vector2.REUSABLE_STACK.push(edge1);
        Vector2.REUSABLE_STACK.push(edge2);

        // We got the vertices that surround the image, but they are in the order of the hull.
        // Sort them into clock-wise order
        Vector2 imgCenter = Vector2.REUSABLE_STACK.pop().set((srcX + srcW) / 2f, (srcY + srcH) / 2f);
        Collections.sort(vertices, (v1, v2) -> (int) (v1.angle(imgCenter)) - (int) (v2.angle(imgCenter)));

        Vector2.REUSABLE_STACK.push(imgCenter);
        Color.REUSABLE_STACK.push(pixelOut);

        for (Vector2 v : vertices)
            polygon.addVertex(v);

        polygon.translate(-imgCenter.x, -imgCenter.y);

        return polygon;
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

        scaleX *= sx;
        scaleY *= sy;
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
            bounds.setCenter(position.x, position.y);
    }

    public void setPosition(float x, float y)
    {
        position.x = x;
        position.y = y;

        if (bounds != null)
            bounds.setCenter(position.x, position.y);
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
        rotate(angle, 0, 0);
    }

    public void rotate(float angle, float originX, float originY)
    {
        rotation += angle;

        if (angle == 0)
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
               position.equals(polygon.position) &&
               vertices.equals(polygon.vertices);
    }

    @Override
    public String toString()
    {
        return "Polygon{" +
               "position=" + position +
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
        scale(scale.x / scaleX, scale.y / scaleY);
    }

    public void setScale(int sx, int sy)
    {
        scale(sx / scaleX, sy / scaleY);
    }
}
