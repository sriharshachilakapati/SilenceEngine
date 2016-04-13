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

import com.shc.silenceengine.collision.CollisionTag;
import com.shc.silenceengine.collision.broadphase.IBroadphase3D;
import com.shc.silenceengine.scene.Scene3D;
import com.shc.silenceengine.scene.components.CollisionComponent3D;
import com.shc.silenceengine.scene.components.TransformComponent3D;
import com.shc.silenceengine.scene.entity.Entity3D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p> A SceneCollider that checks collisions between 3D entities in a scene. SceneCollider3D is an object that manages
 * the collision detection of all the objects in a scene automatically for you. The SceneCollider3D defines a
 * SceneCollider for 3D entities in the scene, i.e., the objects which extend from the Entity3D class. By default,
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
public class SceneCollider3D
{
    // The collision map, used to store registered classes
    private Map<CollisionTag, List<CollisionTag>> collisionMap = new HashMap<>();

    // The Scene and the broadphase
    private Scene3D       scene;
    private IBroadphase3D broadphase;

    // Number of children in the scene
    private int childrenInScene;

    // The list of entities
    private List<Entity3D> entities;

    public SceneCollider3D(IBroadphase3D broadphase)
    {
        this.broadphase = broadphase;
        entities = new ArrayList<>();
    }

    /**
     * @return The scene that this ISceneCollider3D is using to resolve collisions.
     */
    public Scene3D getScene()
    {
        return scene;
    }

    /**
     * Sets the scene that this SceneCollider3D should use to get the entities and check for collisions.
     *
     * @param scene The scene to be used.
     */
    public void setScene(Scene3D scene)
    {
        this.scene = scene;
    }

    /**
     * Registers collisions between two types type1 and type2. Every object of type1 in the Scene is tested against
     * every object of type2 in the Scene for collisions. You should not be worrying about the performance, since the
     * implementation classes will take care of Broad phase collision detection and reduce a lot of unnecessary checks.
     *
     * @param type1 The first type of the Entity3D
     * @param type2 The second type of the Entity3D
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
        if (scene.entities.size() != childrenInScene)
        {
            entities.clear();
            broadphase.clear();
            childrenInScene = 0;

            for (Entity3D entity : scene.entities)
            {
                CollisionComponent3D component = entity.getComponent(CollisionComponent3D.class);

                if (component != null)
                {
                    broadphase.insert(component);
                    entities.add(entity);
                }

                childrenInScene++;
            }
        }

        // Update the broadphase for repositioned entities
        for (Entity3D entity : entities)
        {
            TransformComponent3D transform = entity.getComponent(TransformComponent3D.class);
            CollisionComponent3D collision = entity.getComponent(CollisionComponent3D.class);

            if (transform.transformed)
            {
                broadphase.remove(collision);
                broadphase.insert(collision);
            }
        }

        // Iterate and check collisions
        for (CollisionTag type1 : collisionMap.keySet())
        {
            for (Entity3D entity : entities)
            {
                CollisionComponent3D collision = entity.getComponent(CollisionComponent3D.class);

                if (type1 == collision.tag)
                {
                    List<CollisionComponent3D> collidables = broadphase.retrieve(collision);

                    for (CollisionTag type2 : collisionMap.get(type1))
                    {
                        for (CollisionComponent3D collidable : collidables)
                            if (collidable.tag == type2)
                                if (collision.polyhedron.intersects(collidable.polyhedron))
                                    collision.callback.handleCollision(collision.entity);
                    }
                }
            }
        }
    }
}
