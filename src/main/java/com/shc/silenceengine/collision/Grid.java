package com.shc.silenceengine.collision;

import com.shc.silenceengine.entity.Entity2D;
import com.shc.silenceengine.geom2d.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * A Grid based collision resolver. Reduces the number of collision
 * checks and increases performance. This class implements the broad
 * phase collision detection.
 *
 * @author Sri Harsha Chilakapati
 */
public class Grid
{
    // A spatial partitioned structure to hold elements
    private List<List<List<Entity2D>>> grid;

    // Private stuff, self explanatory
    private int rows;
    private int cols;
    private int cellWidth;
    private int cellHeight;

    // A list of short-listed entities
    private List<Entity2D> retrieveList;

    /**
     * Creates and initializes the Grid
     *
     * @param mapWidth  The width of map (in pixels)
     * @param mapHeight The height of map (in pixels)
     * @param cellWidth The width of each cell
     * @param cellHeight The height of each cell
     */
    public Grid(int mapWidth, int mapHeight, int cellWidth, int cellHeight)
    {
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;

        // Calculate rows and columns
        rows = (mapHeight + cellHeight - 1) / cellHeight;
        cols = (mapWidth + cellWidth - 1) / cellWidth;

        // Create the grid
        grid = new ArrayList<>();

        // Create the cells
        for (int i = 0; i < cols; i++)
        {
            grid.add(new ArrayList<List<Entity2D>>());

            for (int j = 0; j < rows; j++)
            {
                grid.get(i).add(new ArrayList<Entity2D>());
            }
        }

        // Create the retrieve list
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
     * Simplification method to insert bulk data
     *
     * @param list The list of Entity2Ds
     */
    public void insertAll(List<Entity2D> list)
    {
        for (Entity2D e : list)
            insert(e);
    }

    /**
     * Inserts an entity into the grid, by partitioning the available space
     * @param entity The entity to be added to the grid.
     */
    public void insert(Entity2D entity)
    {
        Rectangle bounds = entity.getPolygon().getBounds();

        int topLeftX = Math.max(0, (int) (bounds.getX()) / cellWidth);
        int topLeftY = Math.max(0, (int) (bounds.getY()) / cellHeight);
        int bottomRightX = Math.min(cols - 1, (int) (bounds.getX() + bounds.getWidth() - 1) / cellWidth);
        int bottomRightY = Math.min(rows - 1, (int) (bounds.getY() + bounds.getHeight() - 1) / cellHeight);

        for (int x = topLeftX; x <= bottomRightX; x++)
        {
            for (int y = topLeftY; y <= bottomRightY; y++)
            {
                grid.get(x).get(y).add(entity);
            }
        }
    }

    public List<Entity2D> retrieve(Entity2D entity)
    {
        retrieveList.clear();

        Rectangle bounds = entity.getPolygon().getBounds();

        int topLeftX = Math.max(0, (int) (bounds.getX()) / cellWidth);
        int topLeftY = Math.max(0, (int) (bounds.getY()) / cellHeight);
        int bottomRightX = Math.min(cols - 1, (int) (bounds.getX() + bounds.getWidth() - 1) / cellWidth);
        int bottomRightY = Math.min(rows - 1, (int) (bounds.getY() + bounds.getHeight() - 1) / cellHeight);

        for (int x = topLeftX; x <= bottomRightX; x++)
        {
            for (int y = topLeftY; y <= bottomRightY; y++)
            {
                List<Entity2D> cell = grid.get(x).get(y);

                for (Entity2D retrieved : cell)
                {
                    if (retrieved != entity && !retrieveList.contains(retrieved))
                        retrieveList.add(retrieved);
                }
            }
        }

        return retrieveList;
    }

    public void remove(Entity2D entity)
    {
        Rectangle bounds = entity.getPolygon().getBounds();

        int topLeftX = Math.max(0, (int) (bounds.getX()) / cellWidth);
        int topLeftY = Math.max(0, (int) (bounds.getY()) / cellHeight);
        int bottomRightX = Math.min(cols - 1, (int) (bounds.getX() + bounds.getWidth() - 1) / cellWidth);
        int bottomRightY = Math.min(rows - 1, (int) (bounds.getY() + bounds.getHeight() - 1) / cellHeight);

        for (int x = topLeftX; x <= bottomRightX; x++)
        {
            for (int y = topLeftY; y <= bottomRightY; y++)
            {
                if (grid.get(x).get(y).contains(entity))
                    grid.get(x).get(y).remove(entity);
            }
        }
    }
}
