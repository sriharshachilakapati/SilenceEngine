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

import com.shc.silenceengine.math.geom2d.Rectangle;
import com.shc.silenceengine.scene.components.CollisionComponent2D;
import com.shc.silenceengine.utils.MathUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A Grid based collision resolver. Reduces the number of collision checks and increases performance. This class
 * implements the broad phase collision detection. Though this class is public, you should be using the ISceneCollider2D
 * interface with the scene.
 *
 * @author Sri Harsha Chilakapati
 */
public class Grid implements IBroadphase2D
{
    // A spatial partitioned structure to hold elements
    private List<List<List<CollisionComponent2D>>> grid;

    // Private stuff, self explanatory
    private int rows;
    private int cols;
    private int cellWidth;
    private int cellHeight;

    // A list of short-listed entities
    private List<CollisionComponent2D> retrieveList;

    /**
     * Creates and initializes the Grid
     *
     * @param mapWidth   The width of map (in pixels)
     * @param mapHeight  The height of map (in pixels)
     * @param cellWidth  The width of each cell
     * @param cellHeight The height of each cell
     */
    public Grid(int mapWidth, int mapHeight, int cellWidth, int cellHeight)
    {
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;

        rows = (mapHeight + cellHeight - 1) / cellHeight;
        cols = (mapWidth + cellWidth - 1) / cellWidth;

        grid = new ArrayList<>();

        for (int i = 0; i < cols; i++)
        {
            grid.add(new ArrayList<>());

            for (int j = 0; j < rows; j++)
            {
                grid.get(i).add(new ArrayList<>());
            }
        }

        retrieveList = new ArrayList<>();

        clear();
    }

    /**
     * Clears the Grid, removes all entities
     */
    public void clear()
    {
        for (int i = 0; i < cols; i++)
        {
            for (int j = 0; j < rows; j++)
                grid.get(i).get(j).clear();
        }
    }

    /**
     * Inserts an component into the grid, by partitioning the available space
     *
     * @param entity The component to be added to the grid.
     */
    public void insert(CollisionComponent2D entity)
    {
        Rectangle bounds = entity.polygon.getBounds();

        int topLeftX = MathUtils.clamp((int) (bounds.x) / cellWidth, 0, cols - 1);
        int topLeftY = MathUtils.clamp((int) (bounds.y) / cellHeight, 0, rows - 1);
        int bottomRightX = MathUtils.clamp((int) (bounds.x + bounds.width - 1) / cellWidth, 0, cols - 1);
        int bottomRightY = MathUtils.clamp((int) (bounds.y + bounds.height - 1) / cellHeight, 0, rows - 1);

        for (int x = topLeftX; x <= bottomRightX; x++)
        {
            for (int y = topLeftY; y <= bottomRightY; y++)
            {
                grid.get(x).get(y).add(entity);
            }
        }
    }

    /**
     * Removes an component from the Grid
     *
     * @param entity The component to be removed
     */
    public void remove(CollisionComponent2D entity)
    {
        Rectangle bounds = entity.polygon.getBounds();

        int topLeftX = MathUtils.clamp((int) (bounds.x) / cellWidth, 0, cols - 1);
        int topLeftY = MathUtils.clamp((int) (bounds.y) / cellHeight, 0, rows - 1);
        int bottomRightX = MathUtils.clamp((int) (bounds.x + bounds.width - 1) / cellWidth, 0, cols - 1);
        int bottomRightY = MathUtils.clamp((int) (bounds.y + bounds.height - 1) / cellHeight, 0, rows - 1);

        for (int x = topLeftX; x <= bottomRightX; x++)
        {
            for (int y = topLeftY; y <= bottomRightY; y++)
            {
                if (grid.get(x).get(y).contains(entity))
                    grid.get(x).get(y).remove(entity);
            }
        }
    }

    @Override
    public List<CollisionComponent2D> retrieve(Rectangle bounds)
    {
        retrieveList.clear();

        int topLeftX = MathUtils.clamp((int) (bounds.x) / cellWidth, 0, cols - 1);
        int topLeftY = MathUtils.clamp((int) (bounds.y) / cellHeight, 0, rows - 1);
        int bottomRightX = MathUtils.clamp((int) (bounds.x + bounds.width - 1) / cellWidth, 0, cols - 1);
        int bottomRightY = MathUtils.clamp((int) (bounds.y + bounds.height - 1) / cellHeight, 0, rows - 1);

        for (int x = topLeftX; x <= bottomRightX; x++)
        {
            for (int y = topLeftY; y <= bottomRightY; y++)
            {
                List<CollisionComponent2D> cell = grid.get(x).get(y);

                for (CollisionComponent2D entity : cell)
                    retrieveList.add(entity);
            }
        }

        return retrieveList;
    }

    /**
     * Simplification method to insert bulk data
     *
     * @param list The list of Entity2Ds
     */
    public void insertAll(List<CollisionComponent2D> list)
    {
        for (CollisionComponent2D entity : list)
            insert(entity);
    }
}
