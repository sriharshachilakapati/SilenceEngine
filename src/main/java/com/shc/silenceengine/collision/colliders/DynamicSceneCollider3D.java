package com.shc.silenceengine.collision.colliders;

import com.shc.silenceengine.collision.broadphase.DynamicTree3D;
import com.shc.silenceengine.entity.Entity3D;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.scene.Scene;
import com.shc.silenceengine.scene.SceneNode;

import java.util.ArrayList;
import java.util.List;

/**
 * An implementation of the ISceneCollider3D that resolves collisions
 * using a Dynamic tree. The GridSceneCollider is efficient for all
 * maps has entities less than 500. If more, use QuadTreeSceneCollider.
 *
 * @author Sri Harsha Chilakapati
 */
public class DynamicSceneCollider3D implements ISceneCollider3D
{
    // The Scene and the tree
    private Scene scene;
    private DynamicTree3D tree;

    // Number of children in the scene
    private int childrenInScene;

    // The list of entities
    private List<Entity3D> entities;

    /**
     *
     */
    public DynamicSceneCollider3D()
    {
        tree = new DynamicTree3D();
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
            tree.clear();
            childrenInScene = 0;

            for (SceneNode child : scene.getChildren())
            {
                if (child instanceof Entity3D)
                {
                    Entity3D entity = (Entity3D) child;

                    tree.insert(entity);
                    entities.add(entity);
                }

                childrenInScene++;
            }
        }

        // Update the grid for repositioned entities
        for (Entity3D entity : entities)
        {
            Vector3 velocity = entity.getVelocity();

            if (velocity.x != 0 || velocity.y != 0 || velocity.z != 0)
            {
                tree.remove(entity);
                tree.insert(entity);
            }
        }

        // Iterate and check collisions
        for (Class<? extends Entity3D> class1 : collisionMap.keySet())
            for (Entity3D entity : entities)
                if (class1.isInstance(entity))
                {
                    List<Entity3D> collidables = tree.retrieve(entity);

                    for (Entity3D entity2 : collidables)
                        if (collisionMap.get(class1).isInstance(entity2) && entity != entity2)
                            // Check collision
                            if (entity.getPolyhedron().intersects(entity2.getPolyhedron()))
                                entity.collision(entity2);
                }
    }
}
