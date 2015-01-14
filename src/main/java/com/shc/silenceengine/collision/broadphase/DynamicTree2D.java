package com.shc.silenceengine.collision.broadphase;

import com.shc.silenceengine.entity.Entity2D;
import com.shc.silenceengine.geom2d.Rectangle;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.utils.MathUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sri Harsha Chilakapati
 */
public class DynamicTree2D implements IBroadphaseResolver2D
{
    private Node root;
    private List<Entity2D> retrieveList;
    private Map<Integer, Node> nodeMap;

    public DynamicTree2D()
    {
        nodeMap = new HashMap<>();
        retrieveList = new ArrayList<>();
    }

    @Override
    public void clear()
    {
        nodeMap.clear();
        root = null;
    }

    @Override
    public void insert(Entity2D e)
    {
        Node node = new Node();
        node.entity = e;
        node.aabb = AABB.create(e);

        nodeMap.put(e.getID(), node);

        insert(node);
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

            AABB union = AABB.union(aabb, itemAABB);
            float unionPerimeter = union.getPerimeter();

            float cost = 2 * unionPerimeter;
            float descendCost = 2 * (unionPerimeter - perimeter);

            Node left = node.left;
            Node right = node.right;

            float costLeft;
            if (left.isLeaf())
            {
                AABB u = AABB.union(left.aabb, itemAABB);
                costLeft = u.getPerimeter() + descendCost;
            }
            else
            {
                AABB u = AABB.union(left.aabb, itemAABB);
                costLeft = u.getPerimeter() - left.aabb.getPerimeter() + descendCost;
            }

            float costRight;
            if (right.isLeaf())
            {
                AABB u = AABB.union(right.aabb, itemAABB);
                costRight = u.getPerimeter() + descendCost;
            }
            else
            {
                AABB u = AABB.union(right.aabb, itemAABB);
                costRight = u.getPerimeter() - right.aabb.getPerimeter() + descendCost;
            }

            if (cost < costLeft && cost < costRight)
                break;

            node = (costLeft < costRight) ? left : right;
        }

        Node parent = node.parent;
        Node newParent = new Node();
        newParent.parent = node.parent;
        newParent.aabb = AABB.union(node.aabb, itemAABB);

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

            node.aabb = AABB.union(left.aabb, right.aabb);

            node = node.parent;
        }
    }

    @Override
    public void remove(Entity2D e)
    {
        Node node = nodeMap.get(e.getID());

        if (node != null)
        {
            remove(node);
            nodeMap.remove(e.getID());
        }
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

                n.aabb = AABB.union(left.aabb, right.aabb);

                n = n.parent;
            }
        }
        else
        {
            root = other;
            other.parent = null;
        }
    }

    @Override
    public List<Entity2D> retrieve(Rectangle rect)
    {
        retrieveList.clear();

        queryNode(new AABB(rect.getPosition(), rect.getWidth(), rect.getHeight()), root);

        return retrieveList;
    }

    private void queryNode(AABB aabb, Node node)
    {
        if (node == null)
            return;

        if (node.aabb.intersects(aabb))
        {
            if (node.isLeaf())
                retrieveList.add(node.entity);
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

        public AABB(Vector2 min, float width, float height)
        {
            this.min = min.copy();
            this.max = min.add(width, height);
        }

        public AABB(AABB aabb)
        {
            this.min = aabb.min.copy();
            this.max = aabb.max.copy();
        }

        public void union(AABB aabb)
        {
            min = MathUtils.min(min, aabb.min);
            max = MathUtils.max(max, aabb.max);
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

        public static AABB create(Entity2D entity)
        {
            return new AABB(entity.getPosition(), entity.getWidth(), entity.getHeight());
        }

        public static AABB union(AABB aabb1, AABB aabb2)
        {
            AABB aabb = new AABB(aabb1);
            aabb.union(aabb2);

            return aabb;
        }
    }

    private static class Node
    {
        public Node parent;
        public Node left;
        public Node right;

        public Entity2D entity;
        public AABB aabb;

        public boolean isLeaf()
        {
            return left == null;
        }
    }
}
