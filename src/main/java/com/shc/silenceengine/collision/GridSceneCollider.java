package com.shc.silenceengine.collision;

import com.shc.silenceengine.entity.Entity2D;
import com.shc.silenceengine.scene.Scene;
import com.shc.silenceengine.scene.SceneNode;

import java.util.ArrayList;
import java.util.List;

/**
 * An implementation of the SceneCollider2D that resolves collisions
 * using a Grid. The GridSceneCollider is efficient for all maps that
 * has entities less than 500. If more, use QuadTreeSceneCollider.
 *
 * @author Sri Harsha Chilakapati
 */
public class GridSceneCollider implements SceneCollider2D
{
    // The Scene and the grid
    private Scene scene;
    private Grid  grid;

    // The list of entities
    private List<Entity2D> entities;

    /**
     * Creates a GridSceneCollider with the required properties to create
     * the grid that this collider should use to resolve collisions.
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
    public void setScene(Scene scene)
    {
        this.scene = scene;
    }

    @Override
    public Scene getScene()
    {
        return scene;
    }

    @Override
    public void checkCollisions()
    {
        grid.clear();
        entities.clear();

        // Add all the entities to the list
        for (SceneNode child : scene.getChildren())
            if (child instanceof Entity2D)
            {
                Entity2D entity = (Entity2D) child;

                grid.insert(entity);
                entities.add(entity);
            }

        // Iterate and check collisions
        for (Class<? extends Entity2D> class1 : collisionMap.keySet())
            for (Entity2D entity : entities)
                if (class1.isInstance(entity))
                {
                    List<Entity2D> collidables = grid.retrieve(entity);

                    for (Entity2D entity2 : collidables)
                        if (collisionMap.get(class1).isInstance(entity2))
                            // Check collision
                            if (entity.getPolygon().getBounds().intersects(entity2.getPolygon().getBounds()))
                                if (entity.getPolygon().intersects(entity2.getPolygon()))
                                    entity.collision(entity2);
                }
    }
}
