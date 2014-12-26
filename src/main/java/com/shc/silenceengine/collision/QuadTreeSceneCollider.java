package com.shc.silenceengine.collision;

import com.shc.silenceengine.entity.Entity2D;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.scene.Scene;
import com.shc.silenceengine.scene.SceneNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * An implementation of SceneCollider2D that resolves collisions in a
 * scene containing 2D entities using a QuadTree. The QuadTreeSceneCollider
 * is efficient for very large scenes containing
 *
 * @author Sri Harsha Chilakapati
 */
public class QuadTreeSceneCollider implements SceneCollider2D
{
    private Scene          scene;
    private QuadTree       quadTree;
    private List<Entity2D> entities;

    private int childrenInScene;

    /**
     * Constructs a QuadTreeSceneCollider with the size of the map
     *
     * @param mapWidth  The width of the map (in pixels)
     * @param mapHeight The height of the map (in pixels)
     */
    public QuadTreeSceneCollider(int mapWidth, int mapHeight)
    {
        quadTree = new QuadTree(mapWidth, mapHeight);
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
        if (scene.getChildren().size() != childrenInScene)
        {
            entities.clear();
            quadTree.clear();

            for (SceneNode child : scene.getChildren())
            {
                if (child instanceof Entity2D)
                {
                    Entity2D entity = (Entity2D) child;

                    quadTree.insert(entity);
                    entities.add(entity);
                }

                childrenInScene++;
            }
        }

        // Update the QuadTree for repositioned entities
        for (Entity2D entity : entities)
        {
            if (entity.getVelocity() != Vector2.ZERO)
            {
                quadTree.remove(entity);
                quadTree.insert(entity);
            }
        }

        // Iterate and check collisions
        for (Class<? extends Entity2D> class1 : collisionMap.keySet())
            for (Entity2D entity : entities)
                if (class1.isInstance(entity))
                {
                    List<Entity2D> collidables = quadTree.retrieve(entity);

                    for (Entity2D entity2 : collidables)
                        if (collisionMap.get(class1).isInstance(entity2))
                            // Check collision
                            if (entity.getPolygon().intersects(entity2.getPolygon()))
                                entity.collision(entity2);
                }
    }
}
