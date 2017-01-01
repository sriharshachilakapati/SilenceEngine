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

package com.shc.silenceengine.collision.broadphase;

import com.shc.silenceengine.math.geom2d.Polygon;
import com.shc.silenceengine.math.geom2d.Rectangle;
import com.shc.silenceengine.scene.components.CollisionComponent2D;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sri Harsha Chilakapati
 * @author Josh "ShadowLordAlpha"
 */
public class DynamicTree2D implements IBroadphase2D
{
    private DynamicTree<AABB, CollisionComponent2D> dynamicTree;
    private Map<CollisionComponent2D, Integer>      proxyMap;

    private AABB queryAABB;

    public DynamicTree2D()
    {
        dynamicTree = new DynamicTree<>(AABB::new);
        proxyMap = new HashMap<>();

        queryAABB = new AABB();
    }

    @Override
    public void clear()
    {
        for (int proxy : proxyMap.values())
            dynamicTree.destroyProxy(proxy);

        proxyMap.clear();
    }

    @Override
    public void insert(CollisionComponent2D e)
    {
        int proxy = dynamicTree.createProxy(new AABB(e.polygon), e);
        proxyMap.put(e, proxy);
    }

    @Override
    public void remove(CollisionComponent2D e)
    {
        int proxy = proxyMap.remove(e);
        dynamicTree.destroyProxy(proxy);
    }

    @Override
    public void update(CollisionComponent2D e)
    {
        int proxy = proxyMap.get(e);
        AABB aabb = dynamicTree.getAABB(proxy);
        aabb.update();
        dynamicTree.updateProxy(proxy);
    }

    @Override
    public List<CollisionComponent2D> retrieve(Rectangle rect)
    {
        queryAABB.rect.set(rect);
        return dynamicTree.query(queryAABB, AABB::intersects);
    }

    private static class AABB implements DynamicTree.AABB
    {
        Rectangle rect;
        Polygon   polygon;

        AABB()
        {
            rect = new Rectangle();
        }

        AABB(Polygon polygon)
        {
            this.polygon = polygon;
            this.rect = polygon.getBounds();
        }

        public static boolean intersects(AABB aabb1, AABB aabb2)
        {
            return aabb1.rect.intersects(aabb2.rect);
        }

        @Override
        public float getPerimeter()
        {
            return 2f * (rect.width + rect.height);
        }

        @Override
        public void setToCombine(DynamicTree.AABB aabb1, DynamicTree.AABB aabb2)
        {
            Rectangle rect1 = ((AABB) aabb1).rect;
            Rectangle rect2 = ((AABB) aabb2).rect;

            final float r1MinX = rect1.x;
            final float r1MinY = rect1.y;
            final float r1MaxX = rect1.x + rect1.width;
            final float r1MaxY = rect1.y + rect1.height;

            final float r2MinX = rect2.x;
            final float r2MinY = rect2.y;
            final float r2MaxX = rect2.x + rect2.width;
            final float r2MaxY = rect2.y + rect2.height;

            final float minX = Math.min(r1MinX, r2MinX);
            final float minY = Math.min(r1MinY, r2MinY);
            final float maxX = Math.max(r1MaxX, r2MaxX);
            final float maxY = Math.max(r1MaxY, r2MaxY);

            rect.set(minX, minY, maxX - minX, maxY - minY);
        }

        void update()
        {
            if (polygon != null)
                rect = polygon.getBounds();
        }
    }
}
