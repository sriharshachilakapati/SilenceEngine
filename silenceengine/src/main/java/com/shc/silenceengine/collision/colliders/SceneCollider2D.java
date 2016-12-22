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

package com.shc.silenceengine.collision.colliders;

import com.shc.silenceengine.collision.CollisionTag;
import com.shc.silenceengine.collision.broadphase.IBroadphase2D;
import com.shc.silenceengine.scene.Scene2D;
import com.shc.silenceengine.scene.components.CollisionComponent2D;
import com.shc.silenceengine.scene.components.IComponent2D;
import com.shc.silenceengine.scene.entity.Entity2D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p> A SceneCollider that checks collisions between 2D entities in a scene. SceneCollider2D is an object that manages
 * the collision detection of all the objects in a scene automatically for you. The SceneCollider2D defines a
 * SceneCollider for 2D entities in the scene, i.e., the objects which extend from the Entity2D class. By default,
 * collisions will not be checked for the entities that are present. You are required to register the types of entities
 * to test for collisions using the register() method like in this example. </p>
 *
 * <pre>
 *     register(Player.COLLISION_TAG, Enemy.COLLISION_TAG);
 *     register(Player.COLLISION_TAG, EnemyBullet.COLLISION_TAG);
 * </pre>
 *
 * <p> In the above example, collisions are checked between Player-Enemy and Player-EnemyBullet, and others are just
 * ignored. </p>
 *
 * @author Sri Harsha Chilakapati
 */
public class SceneCollider2D
{
    // The collision map, used to store registered classes
    private Map<CollisionTag, List<CollisionTag>> collisionMap = new HashMap<>();

    // The Scene and the broadphase
    private Scene2D       scene;
    private IBroadphase2D broadphase;

    // Number of children in the scene
    private int childrenInScene;

    // The list of entities
    private List<Entity2D> entities;

    public SceneCollider2D(IBroadphase2D broadphase)
    {
        this.broadphase = broadphase;
        entities = new ArrayList<>();
    }

    /**
     * @return The scene that this ISceneCollider2D is using to resolve collisions.
     */
    public Scene2D getScene()
    {
        return scene;
    }

    /**
     * Sets the scene that this SceneCollider2D should use to get the entities and check for collisions.
     *
     * @param scene The scene to be used.
     */
    public void setScene(Scene2D scene)
    {
        this.scene = scene;
    }

    /**
     * Registers collisions between two types type1 and type2. Every object of type1 in the Scene is tested against
     * every object of type2 in the Scene for collisions. You should not be worrying about the performance, since the
     * implementation classes will take care of Broad phase collision detection and reduce a lot of unnecessary checks.
     *
     * @param type1 The first type of the Entity2D
     * @param type2 The second type of the Entity2D
     */
    public void register(CollisionTag type1, CollisionTag type2)
    {
        if (!collisionMap.containsKey(type1))
            collisionMap.put(type1, new ArrayList<>());

        collisionMap.get(type1).add(type2);
    }

    /**
     * Checks for collisions between every component in the scene that belongs to the registered types registered using
     * the register() method.
     */
    public void checkCollisions()
    {
        // If there are no children in the scene, simply return
        if (scene.entities.size() == 0)
        {
            childrenInScene = 0;
            return;
        }

        // Update the list of entities from the list of children in the scene
        if (scene.numEntities() != childrenInScene)
        {
            entities.clear();
            broadphase.clear();
            childrenInScene = 0;

            updateEntities(scene.entities);
        }

        // Update the broadphase for repositioned entities
        for (Entity2D entity : entities)
        {
            for (IComponent2D component : entity.getComponents())
            {
                if (component instanceof CollisionComponent2D)
                {
                    CollisionComponent2D collision = (CollisionComponent2D) component;

                    if (entity.transformComponent.transformed)
                        broadphase.update(collision);
                }
            }
        }

        // Iterate and check collisions
        for (CollisionTag type1 : collisionMap.keySet())
        {
            for (Entity2D entity : entities)
            {
                CollisionComponent2D collision = entity.getComponent(CollisionComponent2D.class);

                if (type1 == collision.tag)
                {
                    List<CollisionComponent2D> collidables = broadphase.retrieve(collision);

                    for (CollisionTag type2 : collisionMap.get(type1))
                    {
                        for (CollisionComponent2D collidable : collidables)
                            if (collidable.tag == type2)
                                if (collision.polygon.intersects(collidable.polygon))
                                    collision.callback.handleCollision(collision.entity, collidable);
                    }
                }
            }
        }
    }

    private void updateEntities(List<Entity2D> entities)
    {
        for (Entity2D entity : entities)
        {
            for (IComponent2D component : entity.getComponents())
            {
                if (component instanceof CollisionComponent2D)
                {
                    broadphase.insert((CollisionComponent2D) component);
                    this.entities.add(entity);
                }
            }

            childrenInScene++;

            if (entity.getChildren().size() != 0)
                updateEntities(entity.getChildren());
        }
    }
}
