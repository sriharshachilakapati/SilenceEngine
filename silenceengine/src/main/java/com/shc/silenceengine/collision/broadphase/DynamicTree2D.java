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

package com.shc.silenceengine.collision.broadphase;

import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.geom2d.Rectangle;
import com.shc.silenceengine.scene.components.CollisionComponent2D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sri Harsha Chilakapati
 * @author Josh "ShadowLordAlpha"
 */
public class DynamicTree2D implements IBroadphase2D
{
    private Node root;

    private List<CollisionComponent2D> retrieveList;

    private Map<Long, Node> nodeMap;
    private Map<Long, AABB> aabbMap;

    private AABB tmpUnion = new AABB();
    private AABB tmpU     = new AABB();

    public DynamicTree2D()
    {
        nodeMap = new HashMap<>();
        aabbMap = new HashMap<>();

        retrieveList = new ArrayList<>();
    }

    @Override
    public void clear()
    {
        nodeMap.clear();
        root = null;
    }

    @Override
    public void insert(CollisionComponent2D e)
    {
        Node node = new Node();
        node.component = e;
        node.aabb = getAABB(e);

        nodeMap.put(e.id, node);

        insert(node);
    }

    @Override
    public void remove(CollisionComponent2D e)
    {
        Node node = nodeMap.get(e.id);

        if (node != null)
        {
            remove(node);
            nodeMap.remove(e.id);
            aabbMap.remove(e.id);
        }
    }

    @Override
    public List<CollisionComponent2D> retrieve(CollisionComponent2D e)
    {
        retrieveList.clear();
        queryNode(getAABB(e), root);
        return retrieveList;
    }

    @Override
    public List<CollisionComponent2D> retrieve(Rectangle rect)
    {
        retrieveList.clear();
        queryNode(new AABB(rect.getPosition(), rect.getWidth(), rect.getHeight()), root);
        return retrieveList;
    }

    private void remove(Node node)
    {
        if (root == null) return;

        if (node == root)
        {
            root = null;
            return;
        }

        Node parent = node.parent;
        Node grandParent = parent.parent;

        Node other = (parent.left == node) ? parent.right : parent.left;

        if (grandParent != null)
        {
            if (grandParent.left == parent)
                grandParent.left = other;
            else
                grandParent.right = other;

            other.parent = grandParent;

            Node n = grandParent;
            while (n != null)
            {
                Node left = n.left;
                Node right = n.right;

                n.aabb = AABB.union(left.aabb, right.aabb, n.aabb);

                n = n.parent;
            }
        }
        else
        {
            root = other;
            other.parent = null;
        }
    }

    private AABB getAABB(CollisionComponent2D e)
    {
        AABB aabb;

        if (aabbMap.containsKey(e.id))
            aabb = aabbMap.get(e.id);
        else
        {
            aabb = new AABB(new Vector2(), new Vector2());
            aabbMap.put(e.id, aabb);
        }

        aabb.min.set(e.polygon.getMinX(), e.polygon.getMinY());
        aabb.max.set(e.polygon.getMaxX(), e.polygon.getMaxY());

        return aabb;
    }

    private void insert(Node item)
    {
        if (root == null)
        {
            root = item;
            return;
        }

        AABB itemAABB = item.aabb;

        Node node = root;

        while (!node.isLeaf())
        {
            AABB aabb = node.aabb;

            float perimeter = aabb.getPerimeter();

            AABB union = AABB.union(aabb, itemAABB, tmpUnion);
            float unionPerimeter = union.getPerimeter();

            float cost = 2 * unionPerimeter;
            float descendCost = 2 * (unionPerimeter - perimeter);

            Node left = node.left;
            Node right = node.right;

            float costLeft;
            if (left.isLeaf())
            {
                AABB u = AABB.union(left.aabb, itemAABB, tmpU);
                costLeft = u.getPerimeter() + descendCost;
            }
            else
            {
                AABB u = AABB.union(left.aabb, itemAABB, tmpU);
                costLeft = u.getPerimeter() - left.aabb.getPerimeter() + descendCost;
            }

            float costRight;
            if (right.isLeaf())
            {
                AABB u = AABB.union(right.aabb, itemAABB, tmpU);
                costRight = u.getPerimeter() + descendCost;
            }
            else
            {
                AABB u = AABB.union(right.aabb, itemAABB, tmpU);
                costRight = u.getPerimeter() - right.aabb.getPerimeter() + descendCost;
            }

            if (cost < costLeft && cost < costRight)
                break;

            node = (costLeft < costRight) ? left : right;
        }

        Node parent = node.parent;
        Node newParent = new Node();
        newParent.parent = node.parent;
        newParent.aabb = AABB.union(node.aabb, itemAABB, newParent.aabb);

        if (parent != null)
        {
            if (parent.left == node)
                parent.left = newParent;
            else
                parent.right = newParent;

            newParent.left = node;
            newParent.right = item;

            node.parent = newParent;
            item.parent = newParent;
        }
        else
        {
            newParent.left = node;
            newParent.right = item;

            node.parent = newParent;
            item.parent = newParent;

            root = newParent;
        }

        node = item.parent;

        while (node != null)
        {
            Node left = node.left;
            Node right = node.right;

            node.aabb = AABB.union(left.aabb, right.aabb, node.aabb);

            node = node.parent;
        }
    }

    private void queryNode(AABB aabb, Node node)
    {
        if (node == null)
            return;

        if (node.aabb.intersects(aabb))
        {
            if (node.isLeaf())
                retrieveList.add(node.component);
            else
            {
                queryNode(aabb, node.left);
                queryNode(aabb, node.right);
            }
        }
    }

    private static class AABB
    {
        public Vector2 min;
        public Vector2 max;

        public AABB()
        {
            min = new Vector2();
            max = new Vector2();
        }

        public AABB(Vector2 min, Vector2 max)
        {
            this.min = min.copy();
            this.max = max.copy();
        }

        public AABB(Vector2 min, float width, float height)
        {
            this.min = min.copy();
            this.max = min.add(width, height);
        }

        // Unused method should possibly delete
        public static AABB create(CollisionComponent2D c)
        {
            Vector2 min = Vector2.REUSABLE_STACK.pop();
            Vector2 max = Vector2.REUSABLE_STACK.pop();

            min.set(c.polygon.getMinX(), c.polygon.getMinY());
            max.set(c.polygon.getMaxX(), c.polygon.getMaxY());

            AABB aabb = new AABB(min, max);

            Vector2.REUSABLE_STACK.push(min);
            Vector2.REUSABLE_STACK.push(max);

            return aabb;
        }

        public static AABB union(AABB aabb1, AABB aabb2, AABB store)
        {
            if (store == null)
                store = new AABB();

            store.min.set(aabb1.min);
            store.max.set(aabb1.max);

            store.union(aabb2);

            return store;
        }

        public void union(AABB aabb)
        {
            min.x = Math.min(aabb.min.x, min.x);
            min.y = Math.min(aabb.min.y, min.y);

            max.x = Math.max(aabb.max.x, max.x);
            max.y = Math.max(aabb.max.y, max.y);
        }

        public boolean intersects(AABB aabb)
        {
            return !(min.x > aabb.max.x || max.x < aabb.min.x) &&
                   !(min.y > aabb.max.y || max.y < aabb.min.y);
        }

        public float getPerimeter()
        {
            return 2 * (this.max.x - this.min.x + this.max.y - this.min.y);
        }
    }

    private static class Node
    {
        public Node parent;
        public Node left;
        public Node right;

        public CollisionComponent2D component;
        public AABB                 aabb;

        public boolean isLeaf()
        {
            return left == null;
        }
    }
}
