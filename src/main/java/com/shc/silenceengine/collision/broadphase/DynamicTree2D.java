package com.shc.silenceengine.collision.broadphase;

import com.shc.silenceengine.entity.Entity2D;
import com.shc.silenceengine.geom2d.Rectangle;
import com.shc.silenceengine.math.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
public class DynamicTree2D
{
    private Node root;
    private List<Entity2D> retrieveList;

    public DynamicTree2D()
    {
        root = new Node();
        retrieveList = new ArrayList<>();
    }

    public void insert(Entity2D e)
    {
        AABB entityAABB = new AABB();
        entityAABB.min = e.getPosition().copy();
        entityAABB.max = entityAABB.min.add(e.getWidth(), e.getHeight());

        insert(e, root, entityAABB);
    }

    private void insert(Entity2D e, Node node, AABB entityAABB)
    {
        // Clear the data of the node
        node.data = null;

        // If the parent node is a leaf, attach to the left of parent
        if (node.isLeaf())
        {
            Node eNode = new Node();
            eNode.setData(e);
            eNode.setParent(node);

            node.setLeft(eNode);
            eNode.updateAABB();

            System.out.println(eNode.aabb);
        }
        else
        {
            // It is not a leaf node, there must be a left child
            if (node.left.aabb.intersects(entityAABB))
            {
                insert(e, node.left, entityAABB);
            }
            else
            {
                // It should clearly go into the right child
                if (node.right == null)
                {
                    Node eNode = new Node();
                    eNode.setData(e);
                    eNode.setParent(node);

                    node.setRight(eNode);
                    eNode.updateAABB();

                    System.out.println(eNode.aabb);
                    return;
                }

                // There is already a right child, insert into it
                insert(e, node.right, entityAABB);
            }
        }
    }

    public void remove(Entity2D e)
    {
        removeNode(root, e);
    }

    private void remove(Node node)
    {
        if (node.parent == null)
            return;

        if (node == node.parent.left)
        {
            // Check if right node exists
            if (node.parent.right != null)
            {
                // Replace the left node with the right
                node.parent.left = node.parent.right;
                node.parent.right = null;

                node.parent.updateAABB();

                node.parent = null;
                node.aabb = null;
                node.left = null;
                node.right = null;
                node.data = null;

                return;
            }

            // There is no right node, this is the only child of parent
            remove(node.parent);
            return;
        }

        // This is the right node, simply remove this node
        node.parent.right = null;
        node.parent.updateAABB();

        node.parent = null;
        node.data = null;
        node.aabb = null;
        node.left = null;
        node.right = null;
    }

    private void removeNode(Node node, Entity2D e)
    {
        if (node.isLeaf() && node.data == e)
            remove(node);

        if (node.left != null)
            removeNode(node.left, e);

        if (node.right != null)
            removeNode(node.right, e);
    }

    public List<Entity2D> retrieve(Entity2D e)
    {
        return retrieve(e.getBounds());
    }

    public List<Entity2D> retrieve(Rectangle bounds)
    {
        retrieveList.clear();

        AABB entityAABB = new AABB();
        entityAABB.min = bounds.getPosition();
        entityAABB.max = entityAABB.min.add(bounds.getWidth(), bounds.getHeight());

        queryNode(root, entityAABB);

        return retrieveList;
    }

    private void queryNode(Node node, AABB aabb)
    {
        if (node.isLeaf())
        {
            if (node.aabb.intersects(aabb))
                retrieveList.add(node.data);

            return;
        }

        if (node.left != null)
            queryNode(node.left, aabb);

        if (node.right != null)
            queryNode(node.right, aabb);
    }

    public void clear()
    {
        clearNode(root);
    }

    private void clearNode(Node node)
    {
        if (node.left != null)
            clearNode(node.left);

        if (node.right != null)
            clearNode(node.right);

        node.data = null;
        node.aabb = null;
        node.left = null;
        node.right = null;
        node.parent = null;
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

        public boolean intersects(AABB o)
        {
            return max.x >= o.min.x && max.y >= o.min.y && min.x <= o.max.x && min.y <= o.max.y;
        }

        @Override
        public String toString()
        {
            return "AABB{" +
                    "min=" + min +
                    ", max=" + max +
                    '}';
        }
    }

    private static class Node
    {
        private Node parent;

        private Node left;
        private Node right;

        private AABB aabb;
        private Entity2D data;

        public Node()
        {
            parent = null;
            left   = null;
            right  = null;

            aabb = new AABB();
            data = null;
        }

        public void updateAABB()
        {
            if (aabb == null)
                aabb = new AABB();

            // If it is a leaf, the AABB simply bounds the data
            if (isLeaf())
            {
                if (data == null)
                    return;

                aabb.min = data.getPosition();
                aabb.max = aabb.min.add(data.getWidth(), data.getHeight());
            }
            else
            {
                // Not a leaf, create a large AABB for left and right
                aabb.min = left.aabb.min.copy();

                if (right != null)
                    aabb.max = right.aabb.max.copy();
                else
                    aabb.max = left.aabb.max.copy();
            }

            // Update the parent AABB
            if (parent != null)
                parent.updateAABB();
        }

        public boolean isLeaf()
        {
            return left == null;
        }

        public Node getParent()
        {
            return parent;
        }

        public void setParent(Node parent)
        {
            this.parent = parent;
        }

        public Node getLeft()
        {
            return left;
        }

        public void setLeft(Node left)
        {
            this.left = left;
        }

        public Node getRight()
        {
            return right;
        }

        public void setRight(Node right)
        {
            this.right = right;
        }

        public AABB getAABB()
        {
            return aabb;
        }

        public void setAABB(AABB aabb)
        {
            this.aabb = aabb;
        }

        public Entity2D getData()
        {
            return data;
        }

        public void setData(Entity2D data)
        {
            this.data = data;
        }
    }
}
