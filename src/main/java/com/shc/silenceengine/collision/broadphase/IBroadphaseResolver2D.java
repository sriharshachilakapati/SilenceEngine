package com.shc.silenceengine.collision.broadphase;

import com.shc.silenceengine.entity.Entity2D;
import com.shc.silenceengine.geom2d.Rectangle;

import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
public interface IBroadphaseResolver2D
{
    public void clear();

    public void insert(Entity2D e);

    public void remove(Entity2D e);

    public default List<Entity2D> retrieve(Entity2D e)
    {
        return retrieve(e.getBounds());
    }

    public List<Entity2D> retrieve(Rectangle rect);
}
