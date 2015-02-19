package com.shc.silenceengine.models;

import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.math.Transform;
import com.shc.silenceengine.models.obj.OBJModel;

/**
 * @author Sri Harsha Chilakapati
 */
public abstract class Model
{
    public abstract void update(float delta);

    public abstract void render(float delta, Batcher batcher, Transform transform);

    public abstract void dispose();

    public static Model load(String filename)
    {
        if (filename.endsWith(".obj"))
            return new OBJModel(filename);

        throw new SilenceException("The model type you are trying to loadLWJGL is unsupported.");
    }
}
