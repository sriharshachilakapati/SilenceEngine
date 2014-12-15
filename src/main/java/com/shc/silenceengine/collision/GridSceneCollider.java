package com.shc.silenceengine.collision;

import com.shc.silenceengine.entity.Entity2D;
import com.shc.silenceengine.scene.Scene;

import java.util.HashMap;

/**
 * @author Sri Harsha Chilakapati
 */
public class GridSceneCollider implements SceneCollider2D
{
    private Scene scene;
    private HashMap<Class<Entity2D>, Class<Entity2D>> collisionMap;



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
    public void register(Class<Entity2D> type1, Class<Entity2D> type2)
    {
        collisionMap.put(type1, type2);
    }

    @Override
    public void checkCollisions()
    {

    }
}
