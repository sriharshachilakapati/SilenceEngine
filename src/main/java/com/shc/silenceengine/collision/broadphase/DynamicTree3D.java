package com.shc.silenceengine.collision.broadphase;

import com.shc.silenceengine.entity.Entity3D;
import com.shc.silenceengine.geom3d.Polyhedron;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.utils.MathUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sri Harsha Chilakapati
 */
public class DynamicTree3D implements IBroadphaseResolver3D
{
    private Node root;
    private List<Entity3D> retrieveList;
    private Map<Integer, Node> nodeMap;

    public DynamicTree3D()
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
    public void insert(Entity3D e)
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
    public void remove(Entity3D e)
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
    public List<Entity3D> retrieve(Polyhedron cube)
    {
        retrieveList.clear();

        queryNode(new AABB(cube.getPosition(), cube.getWidth(), cube.getHeight(), cube.getThickness()), root);

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
        public Vector3 min;
        public Vector3 max;

        public AABB()
        {
            min = new Vector3();
            max = new Vector3();
        }

        public AABB(Vector3 min, float width, float height, float thickness)
        {
            this.min = min.copy();
            this.max = min.add(width, height, thickness);
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
                    !(min.y > aabb.max.y || max.y < aabb.min.y) &&
                    !(min.z > aabb.max.z || max.z < aabb.min.z);
        }

        public float getPerimeter()
        {
            return 2 * (this.max.x - this.min.x + this.max.y - this.min.y + this.max.z - this.min.z);
        }

        public static AABB create(Entity3D entity)
        {
            AABB aabb = new AABB();

            float x = entity.getWidth() / 2;
            float y = entity.getHeight() / 2;
            float z = entity.getThickness() / 2;

            aabb.min = entity.getPosition().subtract(x, y, z);
            aabb.max = entity.getPosition().add(x, y, z);

            return aabb;
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

        public Entity3D entity;
        public AABB aabb;

        public boolean isLeaf()
        {
            return left == null;
        }
    }
}
