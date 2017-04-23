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

package com.shc.silenceengine.collision.colliders;

import com.shc.silenceengine.collision.CollisionTag;
import com.shc.silenceengine.collision.broadphase.DynamicTree3D;
import com.shc.silenceengine.collision.broadphase.IBroadphase3D;
import com.shc.silenceengine.scene.Entity;
import com.shc.silenceengine.scene.Scene;
import com.shc.silenceengine.scene.components.CollisionComponent3D;
import com.shc.silenceengine.utils.functional.BiCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <p> A Collision System that checks collisions between 3D entities in a scene. CollisionSystem3D is an object that
 * manages the collision detection of all the objects in a scene automatically for you. The CollisionSystem3D defines a
 * system for 3D entities in the scene, i.e., the objects which contain the CollisionComponent3D. By default,
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
public class CollisionSystem3D implements BiCallback<Scene, Float>
{
    // The collision map, used to store registered classes
    private Map<CollisionTag, List<CollisionTag>> collisionMap = new HashMap<>();

    // The broadphase, entities and the numEntities
    private IBroadphase3D broadphase;
    private List<Entity> entities = new ArrayList<>();

    public CollisionSystem3D()
    {
        this(new DynamicTree3D());
    }

    public CollisionSystem3D(IBroadphase3D broadphase)
    {
        this.broadphase = broadphase;
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

    @Override
    public void invoke(Scene scene, Float elapsedTime)
    {
        Iterator<Entity> it = entities.iterator();
        while (it.hasNext())
        {
            Entity e = it.next();
            if (e.isDestroyed())
            {
                it.remove();
                e.forEachComponentOfType(CollisionComponent3D.class, broadphase::remove);
            }
            else if (e.transformComponent.hasChanged())
                e.forEachComponentOfType(CollisionComponent3D.class, broadphase::update);
        }

        scene.forEachEntityWithComponent(CollisionComponent3D.class, e ->
        {
            if (!entities.contains(e))
            {
                entities.add(e);
                e.forEachComponentOfType(CollisionComponent3D.class, broadphase::insert);
            }
        });

        // Iterate and check collisions
        for (CollisionTag type1 : collisionMap.keySet())
        {
            for (Entity entity : entities)
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
                                    collision.callback.handleCollision(collidable);
                    }
                }
            }
        }
    }
}
