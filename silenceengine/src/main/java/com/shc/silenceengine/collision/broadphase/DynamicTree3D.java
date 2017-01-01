/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2016 Sri Harsha Chilakapati
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

import com.shc.silenceengine.math.Ray;
import com.shc.silenceengine.math.geom3d.Cuboid;
import com.shc.silenceengine.math.geom3d.Polyhedron;
import com.shc.silenceengine.scene.components.CollisionComponent3D;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sri Harsha Chilakapati
 * @author Josh "ShadowLordAlpha"
 */
public class DynamicTree3D implements IBroadphase3D
{
    private DynamicTree<AABB, CollisionComponent3D> dynamicTree;
    private Map<CollisionComponent3D, Integer>      proxyMap;

    private AABB queryAABB;

    public DynamicTree3D()
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
    public void insert(CollisionComponent3D e)
    {
        int proxy = dynamicTree.createProxy(new AABB(e.polyhedron), e);
        proxyMap.put(e, proxy);
    }

    @Override
    public void remove(CollisionComponent3D e)
    {
        int proxy = proxyMap.remove(e);
        dynamicTree.destroyProxy(proxy);
    }

    @Override
    public void update(CollisionComponent3D e)
    {
        int proxy = proxyMap.get(e);
        AABB aabb = dynamicTree.getAABB(proxy);
        aabb.update();
        dynamicTree.updateProxy(proxy);
    }

    @Override
    public List<CollisionComponent3D> retrieve(Cuboid cuboid)
    {
        queryAABB.cuboid.set(cuboid);
        return dynamicTree.query(queryAABB, AABB::intersects);
    }

    @Override
    public List<CollisionComponent3D> retrieve(Ray ray)
    {
        return dynamicTree.query(ray, AABB::intersects);
    }

    private static class AABB implements DynamicTree.AABB
    {
        Cuboid     cuboid;
        Polyhedron polyhedron;

        AABB()
        {
            cuboid = new Cuboid();
        }

        AABB(Polyhedron polyhedron)
        {
            this.polyhedron = polyhedron;
            this.cuboid = polyhedron.getBounds();
        }

        public static boolean intersects(AABB aabb1, AABB aabb2)
        {
            return aabb1.cuboid.intersects(aabb2.cuboid);
        }

        @Override
        public float getPerimeter()
        {
            final float HW = cuboid.height * cuboid.width;
            final float HT = cuboid.height * cuboid.thickness;
            final float WT = cuboid.width * cuboid.thickness;

            return 2f * (HW + HT + WT);
        }

        @Override
        public void setToCombine(DynamicTree.AABB aabb1, DynamicTree.AABB aabb2)
        {
            Cuboid c1 = ((AABB) aabb1).cuboid;
            Cuboid c2 = ((AABB) aabb2).cuboid;

            final float c1HalfWidth = c1.width / 2f;
            final float c1HalfHeight = c1.height / 2f;
            final float c1HalfThickness = c1.thickness / 2f;

            final float c2HalfWidth = c2.width / 2f;
            final float c2HalfHeight = c2.height / 2f;
            final float c2HalfThickness = c2.thickness / 2f;

            final float c1MinX = c1.position.x - c1HalfWidth;
            final float c1MinY = c1.position.y - c1HalfHeight;
            final float c1MinZ = c1.position.z - c1HalfThickness;
            final float c1MaxX = c1.position.x + c1HalfWidth;
            final float c1MaxY = c1.position.y + c1HalfHeight;
            final float c1MaxZ = c1.position.z + c1HalfThickness;

            final float c2MinX = c2.position.x - c2HalfWidth;
            final float c2MinY = c2.position.y - c2HalfHeight;
            final float c2MinZ = c2.position.z - c2HalfThickness;
            final float c2MaxX = c2.position.x + c2HalfWidth;
            final float c2MaxY = c2.position.y + c2HalfHeight;
            final float c2MaxZ = c2.position.z + c2HalfThickness;

            final float minX = Math.min(c1MinX, c2MinX);
            final float minY = Math.min(c1MinY, c2MinY);
            final float minZ = Math.min(c1MinZ, c2MinZ);
            final float maxX = Math.max(c1MaxX, c2MaxX);
            final float maxY = Math.max(c1MaxY, c2MaxY);
            final float maxZ = Math.max(c1MaxZ, c2MaxZ);

            final float width = maxX - minX;
            final float height = maxY - minY;
            final float thickness = maxZ - minZ;

            cuboid.position.set(minX + width / 2, minY + height / 2, minZ + thickness / 2);
            cuboid.width = width;
            cuboid.height = height;
            cuboid.thickness = thickness;
        }

        void update()
        {
            if (polyhedron != null)
                cuboid = polyhedron.getBounds();
        }

        public static boolean intersects(AABB aabb, Ray ray)
        {
            return aabb.cuboid.intersects(ray) && aabb.polyhedron.intersects(ray);
        }
    }
}
