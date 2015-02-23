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

import com.shc.silenceengine.scene.Scene;
import com.shc.silenceengine.scene.entity.Entity3D;

import java.util.HashMap;

/**
 * Interface that specifies how a 2D Scene collider should behave. In SilenceEngine, a SceneCollider is an object that
 * manages the collision detection of all the objects in a scene automatically for you. <p> The ISceneCollider2D defines
 * a SceneCollider for 2D entities in the scene, i.e., the objects which extend from the Entity3D class. By default,
 * collisions will not be checked for the entities that are present. You are required to register the types of entities
 * to test for collisions using the register() method like in this example. <p>
 * <pre>
 *     register(Player.class, Enemy.class);
 *     register(Player.class, EnemyBullet.class);
 * </pre>
 * <p> In the above example, collisions are checked between Player-Enemy and Player-EnemyBullet, and others are just
 * ignored.
 *
 * @author Sri Harsha Chilakapati
 */
public interface ISceneCollider3D
{
    // The collision map, used to store registered classes
    HashMap<Class<? extends Entity3D>, Class<? extends Entity3D>> collisionMap = new HashMap<>();

    /**
     * @return The scene that this ISceneCollider2D is using to resolve collisions.
     */
    public Scene getScene();

    /**
     * Sets the scene that this ISceneCollider2D should use to get the entities and check for collisions.
     *
     * @param scene The scene to be used.
     */
    public void setScene(Scene scene);

    /**
     * Registers collisions between two types type1 and type2. Every object of type1 in the Scene is tested against
     * every object of type2 in the Scene for collisions. You should not be worrying about the performance, since the
     * implementation classes will take care of Broad phase collision detection and reduce a lot of unnecessary checks.
     *
     * @param type1 The first type of the Entity3D
     * @param type2 The second type of the Entity3D
     */
    public default void register(Class<? extends Entity3D> type1, Class<? extends Entity3D> type2)
    {
        collisionMap.put(type1, type2);
    }

    /**
     * Checks for collisions between every entity in the scene that belongs to the registered types registered using the
     * register() method.
     */
    public void checkCollisions();
}
