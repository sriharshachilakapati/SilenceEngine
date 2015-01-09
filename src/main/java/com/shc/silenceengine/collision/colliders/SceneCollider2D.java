package com.shc.silenceengine.collision.colliders;

import com.shc.silenceengine.entity.Entity2D;
import com.shc.silenceengine.scene.Scene;

import java.util.HashMap;

/**
 * Interface that specifies how a 2D Scene collider should behave.
 * In SilenceEngine, a SceneCollider is an object that manages
 * the collision detection of all the objects in a scene automatically
 * for you.
 * <p>
 * The SceneCollider2D defines a SceneCollider for 2D entities in the scene,
 * i.e., the objects which extend from the Entity2D class. By default,
 * collisions will not be checked for the entities that are present.
 * You are required to register the types of entities to test for collisions
 * using the register() method like in this example.
 * <p>
 * <pre>
 *     register(Player.class, Enemy.class);
 *     register(Player.class, EnemyBullet.class);
 * </pre>
 * <p>
 * In the above example, collisions are checked between Player-Enemy
 * and Player-EnemyBullet, and others are just ignored.
 *
 * @author Sri Harsha Chilakapati
 */
public interface SceneCollider2D
{
    // The collision map, used to store registered classes
    HashMap<Class<? extends Entity2D>, Class<? extends Entity2D>> collisionMap = new HashMap<>();

    /**
     * Sets the scene that this SceneCollider2D should use to get
     * the entities and check for collisions.
     *
     * @param scene The scene to be used.
     */
    public void setScene(Scene scene);

    /**
     * @return The scene that this SceneCollider2D is using to
     *         resolve collisions.
     */
    public Scene getScene();

    /**
     * Registers collisions between two types type1 and type2. Every
     * object of type1 in the Scene is tested against every object of
     * type2 in the Scene for collisions. You should not be worrying
     * about the performance, since the implementation classes will
     * take care of Broad phase collision detection and reduce a lot
     * of unnecessary checks.
     *
     * @param type1 The first type of the Entity2D
     * @param type2 The second type of the Entity2D
     */
    public default void register(Class<? extends Entity2D> type1, Class<? extends Entity2D> type2)
    {
        collisionMap.put(type1, type2);
    }

    /**
     * Checks for collisions between every entity in the scene that
     * belongs to the registered types registered using the register() method.
     */
    public void checkCollisions();
}
