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
    private Map<Entity2D, Node> nodeMap;

    public DynamicTree2D()
    {
        retrieveList = new ArrayList<>();
        nodeMap = new HashMap<>();
    }

    public void clear()
    {
        retrieveList.clear();
        nodeMap.clear();

        root = null;
    }

    public void insert(Entity2D e)
    {
        if (root == null)
        {
            root = new Node();
            root.setLeaf(e);
            root.updateAABB();

            nodeMap.put(e, root);
        }
        else
        {
            Node node = new Node();
            node.setLeaf(e);
            insertNode(node, root);

            nodeMap.put(e, node);
        }

        System.out.println(getHeight(root) + "    |    " + nodeMap.size());
    }

    private void insertNode(Node node, Node parent)
    {
        if (parent.isLeaf())
        {
            Node newParent = new Node();
            newParent.parent = parent.parent;
            newParent.setBranch(node, parent);

            parent = newParent;
        }
        else
        {
            final AABB aabb0 = parent.left.aabb;
            final AABB aabb1 = parent.right.aabb;

            final float dist0 = aabb0.min.distanceSquared(node.aabb.min);
            final float dist1 = aabb1.min.distanceSquared(node.aabb.min);

            if (dist0 < dist1)
                insertNode(node, parent.left);
            else
                insertNode(node, parent.right);
        }

        parent.updateAABB();
    }

    public void remove(Entity2D e)
    {
        if (!nodeMap.containsKey(e))
            return;

        Node node = nodeMap.get(e);

        node.data = null;
        nodeMap.remove(e);

        removeNode(node);
    }

    private void removeNode(Node node)
    {
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

    private int getHeight(Node node)
    {
        if (node == null)
            return 0;

        return 1 + Math.max(getHeight(node.left), getHeight(node.right));
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
                aabb.wrap(data.getBounds());
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
