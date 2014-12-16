package com.shc.silenceengine.collision;

import com.shc.silenceengine.entity.Entity2D;
import com.shc.silenceengine.scene.Scene;

/**
 * @author Sri Harsha Chilakapati
 */
public interface SceneCollider2D
{
    public void setScene(Scene scene);

    public Scene getScene();

    public void register(Class<? extends Entity2D> type1, Class<? extends Entity2D> type2);

    public void checkCollisions();
}
