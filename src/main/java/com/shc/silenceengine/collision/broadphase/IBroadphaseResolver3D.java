package com.shc.silenceengine.collision.broadphase;

import com.shc.silenceengine.scene.entity.Entity3D;
import com.shc.silenceengine.math.geom3d.Polyhedron;

import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
public interface IBroadphaseResolver3D
{
    public void clear();

    public void insert(Entity3D e);

    public void remove(Entity3D e);

    public default List<Entity3D> retrieve(Entity3D e)
    {
        return retrieve(e.getBounds());
    }

    public List<Entity3D> retrieve(Polyhedron rect);
}
