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

import com.shc.silenceengine.collision.broadphase.IBroadphase2D;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.scene.Scene;
import com.shc.silenceengine.scene.SceneNode;
import com.shc.silenceengine.scene.entity.Entity2D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p> A SceneCollider that checks collisions between 2D entities in a scene. SceneCollider2D is an object that
 * manages the collision detection of all the objects in a scene automatically for you. The SceneCollider2D defines
 * a SceneCollider for 2D entities in the scene, i.e., the objects which extend from the Entity2D class. By default,
 * collisions will not be checked for the entities that are present. You are required to register the types of entities
 * to test for collisions using the register() method like in this example. </p>
 *
 * <pre>
 *     register(Player.class, Enemy.class);
 *     register(Player.class, EnemyBullet.class);
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
    private Map<Class<? extends Entity2D>, List<Class<? extends Entity2D>>> collisionMap = new HashMap<>();

    // The Scene and the grid
    private Scene         scene;
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
    public Scene getScene()
    {
        return scene;
    }

    /**
     * Sets the scene that this SceneCollider2D should use to get the entities and check for collisions.
     *
     * @param scene The scene to be used.
     */
    public void setScene(Scene scene)
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
    public void register(Class<? extends Entity2D> type1, Class<? extends Entity2D> type2)
    {
        if (!collisionMap.containsKey(type1))
            collisionMap.put(type1, new ArrayList<>());

        collisionMap.get(type1).add(type2);
    }

    /**
     * Checks for collisions between every entity in the scene that belongs to the registered types registered using the
     * register() method.
     */
    public void checkCollisions()
    {
        if (scene.getChildren().size() != childrenInScene)
        {
            entities.clear();
            broadphase.clear();
            childrenInScene = 0;

            for (SceneNode child : scene.getChildren())
            {
                if (child instanceof Entity2D)
                {
                    Entity2D entity = (Entity2D) child;

                    broadphase.insert(entity);
                    entities.add(entity);
                }

                childrenInScene++;
            }
        }

        // Update the broadphase for repositioned entities
        for (Entity2D entity : entities)
        {
            Vector2 velocity = entity.getVelocity();

            if (velocity.x != 0 || velocity.y != 0)
            {
                broadphase.remove(entity);
                broadphase.insert(entity);
            }
        }

        // Iterate and check collisions
        for (Class<? extends Entity2D> class1 : collisionMap.keySet())
            for (Entity2D entity : entities)
                if (class1.isInstance(entity))
                {
                    List<Entity2D> collidables = broadphase.retrieve(entity);

                    for (Entity2D entity2 : collidables)
                        for (Class<? extends Entity2D> class2 : collisionMap.get(class1))
                            if (class2.isInstance(entity2) && entity != entity2)
                                // Check collision
                                if (entity.getPolygon().intersects(entity2.getPolygon()))
                                    entity.collision(entity2);
                }
    }
}
