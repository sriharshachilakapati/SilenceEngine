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

package com.shc.silenceengine.collision.colliders;

import com.shc.silenceengine.collision.broadphase.Grid;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.scene.Scene;
import com.shc.silenceengine.scene.SceneNode;
import com.shc.silenceengine.scene.entity.Entity2D;

import java.util.ArrayList;
import java.util.List;

/**
 * An implementation of the ISceneCollider2D that resolves collisions using a Grid. The GridSceneCollider is efficient
 * for all maps that has entities less than 500. If more, use QuadTreeSceneCollider.
 *
 * @author Sri Harsha Chilakapati
 */
public class GridSceneCollider implements ISceneCollider2D
{
    // The Scene and the grid
    private Scene scene;
    private Grid  grid;

    // Number of children in the scene
    private int childrenInScene;

    // The list of entities
    private List<Entity2D> entities;

    /**
     * Creates a GridSceneCollider with the required properties to create the grid that this collider should use to
     * resolve collisions.
     *
     * @param mapWidth   The width of the Scene (in pixels)
     * @param mapHeight  The height of the scene (in pixels)
     * @param cellWidth  The width of a grid cell (in pixels)
     * @param cellHeight The height of a grid cell (in pixels)
     */
    public GridSceneCollider(int mapWidth, int mapHeight, int cellWidth, int cellHeight)
    {
        grid = new Grid(mapWidth, mapHeight, cellWidth, cellHeight);
        entities = new ArrayList<>();
    }

    @Override
    public Scene getScene()
    {
        return scene;
    }

    @Override
    public void setScene(Scene scene)
    {
        this.scene = scene;
    }

    @Override
    public void checkCollisions()
    {
        if (scene.getChildren().size() != childrenInScene)
        {
            entities.clear();
            grid.clear();
            childrenInScene = 0;

            for (SceneNode child : scene.getChildren())
            {
                if (child instanceof Entity2D)
                {
                    Entity2D entity = (Entity2D) child;

                    grid.insert(entity);
                    entities.add(entity);
                }

                childrenInScene++;
            }
        }

        // Update the grid for repositioned entities
        for (Entity2D entity : entities)
        {
            Vector2 velocity = entity.getVelocity();

            if (velocity.x != 0 || velocity.y != 0)
            {
                grid.remove(entity);
                grid.insert(entity);
            }
        }

        // Iterate and check collisions
        for (Class<? extends Entity2D> class1 : collisionMap.keySet())
            for (Entity2D entity : entities)
                if (class1.isInstance(entity))
                {
                    List<Entity2D> collidables = grid.retrieve(entity);

                    for (Entity2D entity2 : collidables)
                        if (collisionMap.get(class1).isInstance(entity2) && entity != entity2)
                            // Check collision
                            if (entity.getPolygon().intersects(entity2.getPolygon()))
                                entity.collision(entity2);
                }
    }
}
