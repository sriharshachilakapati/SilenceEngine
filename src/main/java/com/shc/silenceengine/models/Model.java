package com.shc.silenceengine.models;

import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.models.obj.OBJModel;
import com.shc.silenceengine.scene.SceneNode;

/**
 * @author Sri Harsha Chilakapati
 */
public abstract class Model extends SceneNode
{
    public abstract void update(float delta);

    public abstract void render(float delta, Batcher batcher);

    public abstract void dispose();

    public static Model load(String filename)
    {
        if (filename.endsWith(".obj"))
            return new OBJModel(filename);

        throw new SilenceException("The model type you are trying to load is unsupported.");
    }
}
