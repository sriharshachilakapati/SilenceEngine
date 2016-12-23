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

import com.shc.silenceengine.utils.functional.BiPredicate;
import com.shc.silenceengine.utils.functional.Provider;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * A DynamicTree data structure ported to Java from the sources of Box2D physics library. Thanks to the original
 * Box2D author Erin Catto, and Bullet3D author Nathanael Presson. This is a reimplementation that is modified slightly
 * for use in SilenceEngine.
 *
 * @author Sri Harsha Chilakapati
 */
class DynamicTree<AABBType extends DynamicTree.AABB, CollisionType>
{
    private AABBType tempAABB1;
    private AABBType tempAABB2;

    private int root;
    private int freeList;

    private int nodeCount;
    private int nodeCapacity;

    private List<CollisionType> retrieveList;

    private BiPredicate<AABBType, AABBType> intersector;
    private Provider<AABBType>              aabbCreator;

    /**
     * The original source uses a plain array for the nodes, and resizes them using realloc. However,
     * since the same is done by an ArrayList in Java, we modified it to use ArrayList instead.
     */
    private ArrayList<Node<CollisionType, AABBType>> nodes;

    /**
     * We find this in the query method in the original source code, but instead of allocating it every call which is
     * expensive, we allocate it once here and reuse it for every query.
     */
    private Deque<Integer> stack;

    DynamicTree(Provider<AABBType> aabbCreator, BiPredicate<AABBType, AABBType> intersector)
    {
        tempAABB1 = aabbCreator.provide();
        tempAABB2 = aabbCreator.provide();

        this.aabbCreator = aabbCreator;
        this.intersector = intersector;

        nodeCapacity = 16;
        nodeCount = 0;

        nodes = new ArrayList<>();
        stack = new LinkedList<>();

        buildFreeList();

        freeList = 0;

        // The list to return on retrieval
        retrieveList = new ArrayList<>();
    }

    AABBType getAABB(int proxyID)
    {
        return nodes.get(proxyID).aabb;
    }

    List<CollisionType> query(AABBType aabb)
    {
        retrieveList.clear();
        stack.push(root);

        while (stack.size() > 0)
        {
            int nodeID = stack.pop();

            if (nodeID == Node.NULL)
                continue;

            Node<CollisionType, AABBType> node = nodes.get(nodeID);

            if (intersector.test(node.aabb, aabb))
            {
                if (node.isLeaf())
                    retrieveList.add(node.collision);
                else
                {
                    stack.push(node.child1);
                    stack.push(node.child2);
                }
            }
        }

        return retrieveList;
    }

    private int allocateNode()
    {
        // Expand the node pool as needed. This is handled for some part internally by the ArrayList, but we just take
        // the calculations here since ArrayList doesn't allow to query the capacity.

        if (freeList == Node.NULL)
        {
            // We increase the capacity by twice, and we ensure that the ArrayList has the same capacity
            nodeCapacity *= 2;
            nodes.ensureCapacity(nodeCapacity);

            buildFreeList();
        }

        // Peel a node from the free list
        int nodeID = freeList;

        Node<CollisionType, AABBType> node = nodes.get(nodeID);

        freeList = node.parentOrNext;
        node.parentOrNext = Node.NULL;
        node.child1 = Node.NULL;
        node.child2 = Node.NULL;
        node.height = 0;
        node.collision = null;

        nodeCount++;

        return nodeID;
    }

    private void freeNode(int nodeID)
    {
        Node<CollisionType, AABBType> node = nodes.get(nodeID);
        node.parentOrNext = freeList;
        node.height = -1;
        freeList = nodeID;
        nodeCount--;
    }

    int createProxy(AABBType aabb, CollisionType collision)
    {
        int proxyID = allocateNode();

        Node<CollisionType, AABBType> node = nodes.get(proxyID);
        node.aabb = aabb;
        node.collision = collision;
        node.height = 0;

        insertLeaf(proxyID);

        return proxyID;
    }

    void destroyProxy(int proxyID)
    {
        removeLeaf(proxyID);
        freeNode(proxyID);
    }

    void updateProxy(int proxyID)
    {
        removeLeaf(proxyID);
        insertLeaf(proxyID);
    }

    private void insertLeaf(int leaf)
    {

        if (root == Node.NULL)
        {
            root = leaf;
            nodes.get(root).parentOrNext = Node.NULL;
            return;
        }

        // Find the best sibling for this node
        Node<CollisionType, AABBType> leafNode = nodes.get(leaf);
        AABBType leafAABB = leafNode.aabb;
        int index = root;
        Node indexNode;

        while (!(indexNode = nodes.get(index)).isLeaf())
        {
            int child1 = indexNode.child1;
            int child2 = indexNode.child2;

            Node<CollisionType, AABBType> childNode1 = nodes.get(child1);
            Node<CollisionType, AABBType> childNode2 = nodes.get(child2);

            float area = indexNode.aabb.getPerimeter();

            tempAABB1.setToCombine(indexNode.aabb, leafAABB);
            float combinedArea = tempAABB1.getPerimeter();

            // Cost of creating a new parent for this node and the new leaf
            float cost = 2f * combinedArea;

            // Minimum cost of pushing the leaf further down the tree
            float inheritanceCost = 2f * (combinedArea - area);

            // Cost of descending into children
            float cost1 = getChildCost(childNode1, leafAABB, inheritanceCost);
            float cost2 = getChildCost(childNode2, leafAABB, inheritanceCost);

            // Descend according to minimum cost
            if (cost < cost1 && cost < cost2)
                break;

            index = (cost1 < cost2) ? child1 : child2;
        }

        int sibling = index;
        Node siblingNode = nodes.get(sibling);

        // Create a new parent
        int oldParent = siblingNode.parentOrNext;
        int newParent = allocateNode();

        Node<CollisionType, AABBType> newParentNode = nodes.get(newParent);

        newParentNode.parentOrNext = oldParent;
        newParentNode.collision = null;
        newParentNode.aabb.setToCombine(leafAABB, siblingNode.aabb);
        newParentNode.height = siblingNode.height + 1;

        if (oldParent != Node.NULL)
        {
            Node<CollisionType, AABBType> oldParentNode = nodes.get(oldParent);

            // The sibling was not the root
            if (oldParentNode.child1 == sibling)
                oldParentNode.child1 = newParent;
            else
                oldParentNode.child2 = newParent;

            newParentNode.child1 = sibling;
            newParentNode.child2 = leaf;
            siblingNode.parentOrNext = newParent;
            leafNode.parentOrNext = newParent;
        }
        else
        {
            // The sibling was the root
            newParentNode.child1 = sibling;
            newParentNode.child2 = leaf;
            siblingNode.parentOrNext = newParent;
            leafNode.parentOrNext = newParent;
            root = newParent;
        }

        // Walk back up the tree fixing heights and AABBs
        index = leafNode.parentOrNext;

        while (index != Node.NULL)
        {
            index = balance(index);
            indexNode = nodes.get(index);

            int child1 = indexNode.child1;
            int child2 = indexNode.child2;

            Node<CollisionType, AABBType> childNode1 = nodes.get(child1);
            Node<CollisionType, AABBType> childNode2 = nodes.get(child2);

            indexNode.height = 1 + Math.max(childNode1.height, childNode2.height);
            indexNode.aabb.setToCombine(childNode1.aabb, childNode2.aabb);

            index = indexNode.parentOrNext;
        }
    }

    private void removeLeaf(int leaf)
    {
        if (leaf == root)
        {
            root = Node.NULL;
            return;
        }

        Node<CollisionType, AABBType> leafNode = nodes.get(leaf);

        int parent = leafNode.parentOrNext;
        Node<CollisionType, AABBType> parentNode = nodes.get(parent);

        int grandParent = parentNode.parentOrNext;

        int sibling;

        if (parentNode.child1 == leaf)
            sibling = parentNode.child2;
        else
            sibling = parentNode.child1;

        Node<CollisionType, AABBType> siblingNode = nodes.get(sibling);

        if (grandParent != Node.NULL)
        {
            Node<CollisionType, AABBType> grandParentNode = nodes.get(grandParent);

            // Destroy parent and connect sibling to grandParent
            if (grandParentNode.child1 == parent)
                grandParentNode.child1 = sibling;
            else
                grandParentNode.child2 = sibling;

            siblingNode.parentOrNext = grandParent;
            freeNode(parent);

            // Adjust ancestor bounds
            int index = grandParent;

            while (index != Node.NULL)
            {
                index = balance(index);

                Node<CollisionType, AABBType> indexNode = nodes.get(index);
                Node<CollisionType, AABBType> childNode1 = nodes.get(indexNode.child1);
                Node<CollisionType, AABBType> childNode2 = nodes.get(indexNode.child2);

                indexNode.aabb.setToCombine(childNode1.aabb, childNode2.aabb);
                indexNode.height = 1 + Math.max(childNode1.height, childNode2.height);

                index = indexNode.parentOrNext;
            }
        }
        else
        {
            root = sibling;
            siblingNode.parentOrNext = Node.NULL;
            freeNode(parent);
        }
    }

    private int balance(int iA)
    {
        Node<CollisionType, AABBType> nA = nodes.get(iA);

        if (nA.isLeaf() || nA.height < 2)
            return iA;

        int iB = nA.child1;
        int iC = nA.child2;

        Node<CollisionType, AABBType> nB = nodes.get(iB);
        Node<CollisionType, AABBType> nC = nodes.get(iC);

        int balance = nC.height - nB.height;

        // Rotate C up
        if (balance > 1)
        {
            int iF = nC.child1;
            int iG = nC.child2;

            Node<CollisionType, AABBType> nF = nodes.get(iF);
            Node<CollisionType, AABBType> nG = nodes.get(iG);

            // Swap A and C
            nC.child1 = iA;
            nC.parentOrNext = nA.parentOrNext;
            nA.parentOrNext = iC;

            // A's old parent should point to C
            if (nC.parentOrNext != Node.NULL)
            {
                Node<CollisionType, AABBType> parentC = nodes.get(nC.parentOrNext);

                if (parentC.child1 == iA)
                    parentC.child1 = iC;
                else
                    parentC.child2 = iC;
            }
            else
                root = iC;

            // Rotate
            if (nF.height > nG.height)
            {
                nC.child2 = iF;
                nA.child2 = iG;
                nG.parentOrNext = iA;
                nA.aabb.setToCombine(nB.aabb, nG.aabb);
                nC.aabb.setToCombine(nA.aabb, nF.aabb);

                nA.height = 1 + Math.max(nB.height, nG.height);
                nC.height = 1 + Math.max(nA.height, nF.height);
            }
            else
            {
                nC.child2 = iG;
                nA.child2 = iF;
                nF.parentOrNext = iA;
                nA.aabb.setToCombine(nB.aabb, nF.aabb);
                nC.aabb.setToCombine(nA.aabb, nG.aabb);

                nA.height = 1 + Math.max(nB.height, nF.height);
                nC.height = 1 + Math.max(nA.height, nG.height);
            }

            return iC;
        }

        // Rotate B up
        if (balance < -1)
        {
            int iD = nB.child1;
            int iE = nB.child2;

            Node<CollisionType, AABBType> nD = nodes.get(iD);
            Node<CollisionType, AABBType> nE = nodes.get(iE);

            // Swap A and B
            nB.child1 = iA;
            nB.parentOrNext = nA.parentOrNext;
            nA.parentOrNext = iB;

            // A's old parent should point to B
            if (nB.parentOrNext != Node.NULL)
            {
                Node<CollisionType, AABBType> parentB = nodes.get(nB.parentOrNext);

                if (parentB.child1 == iA)
                    parentB.child1 = iB;
                else
                    parentB.child2 = iB;
            }
            else
                root = iB;

            // Rotate
            if (nD.height > nE.height)
            {
                nB.child2 = iD;
                nA.child1 = iE;
                nE.parentOrNext = iA;
                nA.aabb.setToCombine(nC.aabb, nE.aabb);
                nB.aabb.setToCombine(nA.aabb, nD.aabb);

                nA.height = 1 + Math.max(nC.height, nE.height);
                nB.height = 1 + Math.max(nA.height, nD.height);
            }
            else
            {
                nB.child2 = iE;
                nA.child1 = iD;
                nD.parentOrNext = iA;
                nA.aabb.setToCombine(nC.aabb, nD.aabb);
                nB.aabb.setToCombine(nA.aabb, nE.aabb);

                nA.height = 1 + Math.max(nC.height, nD.height);
                nB.height = 1 + Math.max(nA.height, nE.height);
            }

            return iB;
        }

        return iA;
    }

    private float getChildCost(Node<CollisionType, AABBType> childNode, AABBType leafAABB, float inheritanceCost)
    {
        if (childNode.isLeaf())
        {
            tempAABB2.setToCombine(leafAABB, childNode.aabb);
            return tempAABB2.getPerimeter();
        }
        else
        {
            tempAABB2.setToCombine(leafAABB, childNode.aabb);
            float oldArea = childNode.aabb.getPerimeter();
            float newArea = tempAABB2.getPerimeter();
            return (newArea - oldArea) + inheritanceCost;
        }
    }

    private void buildFreeList()
    {
        // Build a LinkedList for free nodes (this is not a separate field, but instead a part of nodes)
        // Everything after the nodeCount in the list works as the free list.

        for (int i = nodeCount; i < nodeCapacity - 1; i++)
        {
            Node<CollisionType, AABBType> node = new Node<>(aabbCreator.provide());

            node.parentOrNext = i + 1;
            node.height = -1;
            node.child1 = node.child2 = Node.NULL;

            nodes.add(node);
        }

        Node<CollisionType, AABBType> lastNode = new Node<>(aabbCreator.provide());
        lastNode.parentOrNext = Node.NULL;
        lastNode.height = -1;
        lastNode.child1 = lastNode.child2 = Node.NULL;
        nodes.add(lastNode);

        freeList = nodeCount;
    }

    interface AABB
    {
        float getPerimeter();
        void setToCombine(AABB aabb1, AABB aabb2);
    }

    /**
     * A node in the dynamic tree.
     */
    private static class Node<CollisionComponent, AABBType extends AABB>
    {
        static final int NULL = -1;

        AABBType aabb;

        CollisionComponent collision;

        /**
         * This is defined in C source of Box2D as a union
         *
         * <pre>
         * union
         * {
         *     int32 parent;
         *     int32 next;
         * }
         * </pre>
         *
         * And hence, we combine it as a single variable in Java
         */
        int parentOrNext;

        int child1;
        int child2;

        /**
         * The height of the sub tree from this node, it is 0 for leaf nodes, and -1 for free nodes.
         */
        int height;

        Node(AABBType aabb)
        {
            this.aabb = aabb;
        }

        boolean isLeaf()
        {
            return child1 == NULL;
        }
    }
}
