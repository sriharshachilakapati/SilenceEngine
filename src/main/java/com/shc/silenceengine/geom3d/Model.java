package com.shc.silenceengine.geom3d;

import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Transform;
import com.shc.silenceengine.math.Vector3;

/**
 * @author Sri Harsha Chilakapati
 */
public class Model
{
    private Vector3   position;
    private Transform transform;

    public Model()
    {
        position  = new Vector3();
        transform = new Transform();
    }

    public void update(float delta)
    {
    }

    public void render(float delta, Batcher batcher)
    {
    }

    public Vector3 getPosition()
    {
        return position;
    }

    public void setPosition(Vector3 position)
    {
        setPosition(position.x, position.y, position.z);
    }

    public void setPosition(float x, float y, float z)
    {
        position.set(x, y, z);
    }

    public Transform getTransform()
    {
        return transform;
    }
}
