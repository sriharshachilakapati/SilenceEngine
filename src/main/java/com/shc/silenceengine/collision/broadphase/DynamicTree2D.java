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
public class DynamicTree2D
{
    private Node root;
    private List<Entity2D> retrieveList;
    private Map<Integer, Node> nodeMap;

    public DynamicTree2D()
    {
        retrieveList = new ArrayList<>();
        nodeMap = new HashMap<>();
    }

    public void clear()
    {
        // Clear the retrieveList and the nodeMap
        retrieveList.clear();
        nodeMap.clear();

        // Clear the root node
        root = null;
    }

    public void insert(Entity2D e)
    {
        if (root == null)
        {
            // Create the root first
            root = new Node();
            root.setLeaf(e);
            root.updateAABB();

            // Map the node ID
            nodeMap.put(e.getID(), root);
        }
        else
        {
            // Create the leaf node and insert it
            Node node = new Node();
            node.setLeaf(e);
            node.updateAABB();
            insertNode(node, root);

            // Map the node ID
            nodeMap.put(e.getID(), node);
        }
    }

    private void insertNode(Node node, Node parent)
    {
        if (parent.isLeaf())
        {
            // If it's a leaf, split it into half
            Node newParent = new Node();
            newParent.parent = parent.parent;
            parent.setBranch(node, newParent);

            parent = newParent;
        }
        else
        {
            // It's not a leaf, insert into the nearest leaf
            final AABB aabb0 = parent.left.aabb;
            final AABB aabb1 = parent.right.aabb;

            final float dist0 = aabb0.min.distanceSquared(node.aabb.min);
            final float dist1 = aabb1.min.distanceSquared(node.aabb.min);

            if (dist0 < dist1)
                insertNode(node, parent.left);
            else
                insertNode(node, parent.right);
        }

        // Update the parent AABB
        parent.updateAABB();
    }

    public void remove(Entity2D e)
    {
        // Simply return if there is no entity in this tree
        if (!nodeMap.containsKey(e.getID()))
            return;

        // Get the node, clear the data and remove it
        Node node = nodeMap.get(e.getID());

        node.data = null;
        nodeMap.remove(e.getID());

        removeNode(node);
    }

    private void removeNode(Node node)
    {
        // Get the parent node
        Node parent = node.parent;

        if (parent != null)
        {
            Node sibling = node.getSibling();

            if (parent.parent != null)
            {
                sibling.parent = parent.parent;

                if (parent == parent.parent.left)
                    parent.parent.left = sibling;
                else
                    parent.parent.right = sibling;
            }
            else
            {
                root = sibling;
                sibling.parent = null;
            }

            parent.updateAABB();
        }
        else
            root = null;
    }

    public List<Entity2D> retrieve(Entity2D e)
    {
        retrieveList.clear();

        if (root != null)
        {
            AABB entityAABB = new AABB();
            entityAABB.wrap(e.getBounds());

            queryNode(root, entityAABB);
        }

        return retrieveList;
    }

    private void queryNode(Node node, AABB aabb)
    {
        if (node.aabb.intersects(aabb))
        {
            if (node.isLeaf())
                retrieveList.add(node.data);
            else
            {
                queryNode(node.left, aabb);
                queryNode(node.right, aabb);
            }
        }
    }

    // THE AABB class is here!
    private static class AABB
    {
        public Vector2 min;
        public Vector2 max;

        public AABB()
        {
            min = new Vector2();
            max = new Vector2();
        }

        public void wrap(Rectangle r)
        {
            min = r.getPosition().copy();
            max = min.add(r.getWidth(), r.getHeight());
        }

        public void combine(AABB o)
        {
            min = MathUtils.min(min, o.min);
            max = MathUtils.max(max, o.max);
        }

        public boolean intersects(AABB o)
        {
            return !(o.min.x > max.x
                    || o.max.x < min.x
                    || o.min.y > max.y
                    || o.max.y < min.y);
        }
    }

    // TREE NODE class is here!
    private static class Node
    {
        public Node parent;
        public Node left;
        public Node right;

        public AABB aabb;
        public Entity2D data;

        public Node()
        {
            this(null, null);
        }

        public Node(Node parent, Entity2D data)
        {
            this.parent = parent;
            this.data = data;

            this.aabb = new AABB();
            this.left = null;
            this.right = null;
        }

        public void setBranch(Node left, Node right)
        {
            left.parent = this;
            right.parent = this;

            this.left = left;
            this.right = right;
        }

        public void setLeaf(Entity2D data)
        {
            this.data = data;
            this.left = null;
            this.right = null;

            aabb.wrap(data.getBounds());
        }

        public void updateAABB()
        {
            if (isLeaf())
            {
                if (data != null)
                    aabb.wrap(data.getBounds());
            }
            else
            {
                aabb.min = left.aabb.min.copy();
                aabb.max = left.aabb.max.copy();

                if (right != null)
                    aabb.combine(right.aabb);
            }
        }

        public Node getSibling()
        {
            return this == parent.left ? parent.right : this;
        }

        public boolean isLeaf()
        {
            return left == null;
        }
    }
}
