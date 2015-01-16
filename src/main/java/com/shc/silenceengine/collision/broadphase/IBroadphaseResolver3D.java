package com.shc.silenceengine.collision.broadphase;

import com.shc.silenceengine.entity.Entity3D;
import com.shc.silenceengine.geom3d.Polyhedron;

import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
public interface IBroadphaseResolver3D
{
    public void clear();

    public void insert(Entity3D e);

    public void remove(Entity3D e);

    public List<Entity3D> retrieve(Polyhedron rect);

    public default List<Entity3D> retrieve(Entity3D e)
    {
        return retrieve(e.getBounds());
    }
}
