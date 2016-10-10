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

import com.shc.silenceengine.math.geom2d.Rectangle;
import com.shc.silenceengine.scene.components.CollisionComponent2D;

import java.util.ArrayList;
import java.util.List;

/**
 * A QuadTree implementation to reduce collision checks. Every level contains a maximum of 10 objects and the tree sub
 * divides on exceeding this limit.
 *
 * @author Sri Harsha Chilakapati
 */
public class QuadTree implements IBroadphase2D
{
    // The MAX_OBJECTS and LEVEL constants
    private static final int MAX_OBJECTS = 10;
    private int level;

    // The objects list
    private List<CollisionComponent2D> objects;
    // The retrieve list
    private List<CollisionComponent2D> retrieveList;

    // The bounds of this tree
    private Rectangle bounds;

    // Branches of this tree a.k.a the quadrants
    private QuadTree[] nodes;

    /**
     * Constructs a QuadTree that covers a rectangle [0, 0, mapWidth, mapHeight]
     *
     * @param mapWidth  The width of the map (in pixels)
     * @param mapHeight The height of the map (in pixels)
     */
    public QuadTree(int mapWidth, int mapHeight)
    {
        this(0, new Rectangle(0, 0, mapWidth, mapHeight));
    }

    /**
     * Construct a QuadTree with custom values. Used to create sub trees or branches
     *
     * @param l The level of this tree
     * @param b The bounds of this tree
     */
    public QuadTree(int l, Rectangle b)
    {
        level = l;
        bounds = b;

        objects = new ArrayList<>();
        retrieveList = new ArrayList<>();

        nodes = new QuadTree[4];
    }

    /**
     * Set's the bounds of this tree.
     *
     * @param x      The x-coordinate
     * @param y      The y-coordinate
     * @param width  The width
     * @param height The height
     */
    public void setBounds(int x, int y, int width, int height)
    {
        bounds.set(x, y, width, height);
        clear();
        split();
    }

    /**
     * Clear this tree. Also clears any subtrees.
     */
    public void clear()
    {
        objects.clear();
        for (int i = 0; i < nodes.length; i++)
        {
            if (nodes[i] != null)
            {
                nodes[i].clear();
                nodes[i] = null;
            }
        }
    }

    /**
     * Insert an object into this tree
     */
    public void insert(CollisionComponent2D r)
    {
        if (nodes[0] != null)
        {
            int index = getIndex(r);
            if (index != -1)
            {
                nodes[index].insert(r);
                return;
            }
        }

        objects.add(r);

        if (objects.size() > MAX_OBJECTS)
        {
            if (nodes[0] == null)
            {
                split();
            }
            for (int i = 0; i < objects.size(); i++)
            {
                int index = getIndex(objects.get(i));
                if (index != -1)
                {
                    nodes[index].insert(objects.remove(i));
                }
            }
        }
    }

    public void remove(CollisionComponent2D e)
    {
        if (nodes[0] != null)
        {
            int index = getIndex(e);
            if (index != -1)
            {
                nodes[index].remove(e);
                return;
            }
        }

        objects.remove(e);
    }

    /**
     * Returns the collidable objects with the given rectangle
     */
    public List<CollisionComponent2D> retrieve(Rectangle r)
    {
        retrieveList.clear();
        int index = getIndex(r);

        if (index != -1 && nodes[0] != null)
        {
            retrieveList = nodes[index].retrieve(r);
        }

        retrieveList.addAll(objects);
        return retrieveList;
    }

    // Split the tree into 4 quadrants
    private void split()
    {
        int subWidth = (int) (bounds.width / 2);
        int subHeight = (int) (bounds.height / 2);

        int x = (int) bounds.x;
        int y = (int) bounds.y;

        nodes[0] = new QuadTree(level + 1, new Rectangle(x + subWidth, y, subWidth, subHeight));
        nodes[1] = new QuadTree(level + 1, new Rectangle(x, y, subWidth, subHeight));
        nodes[2] = new QuadTree(level + 1, new Rectangle(x, y + subHeight, subWidth, subHeight));
        nodes[3] = new QuadTree(level + 1, new Rectangle(x + subWidth, y + subHeight, subWidth, subHeight));
    }

    // Get the index of an object
    private int getIndex(CollisionComponent2D entity)
    {
        return getIndex(entity.polygon.getBounds());
    }

    // Get the index of a rectangle
    private int getIndex(Rectangle r)
    {
        int index = -1;

        double verticalMidpoint = bounds.x + (bounds.width / 2);
        double horizontalMidpoint = bounds.y + (bounds.height / 2);

        boolean topQuadrant = (r.y < horizontalMidpoint && r.y + r.height < horizontalMidpoint);
        boolean bottomQuadrant = (r.y > horizontalMidpoint);

        if (r.x < verticalMidpoint && r.x + r.width < verticalMidpoint)
        {
            if (topQuadrant)
            {
                index = 1;
            }
            else if (bottomQuadrant)
            {
                index = 2;
            }
        }
        else if (r.x > verticalMidpoint)
        {
            if (topQuadrant)
            {
                index = 0;
            }
            else if (bottomQuadrant)
            {
                index = 3;
            }
        }
        return index;
    }

    /**
     * Insert an ArrayList of objects into this tree.
     *
     * @param entities The list of entities.
     */
    public void insertAll(List<CollisionComponent2D> entities)
    {
        for (CollisionComponent2D entity : entities)
            insert(entity);
    }
}
