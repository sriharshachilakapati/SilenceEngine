package com.shc.silenceengine.entity;

import com.shc.silenceengine.geom2d.Polygon;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.scene.SceneNode;

/**
 * @author Sri Harsha Chilakapati
 */
public class Entity2D extends SceneNode
{
    private Vector2 position;
    private Vector2 velocity;
    private Polygon polygon;

    public Entity2D()
    {
        position = new Vector2();
        velocity = new Vector2();
    }

    public Entity2D(Polygon polygon)
    {
        this.polygon = polygon;
    }

    public void preUpdate(double delta)
    {
        super.preUpdate(delta);

        position.add(velocity);
        polygon.setPosition(position);
    }

    public Vector2 getPosition()
    {
        return position;
    }

    public void setPosition(Vector2 position)
    {
        this.position = position;
        polygon.setPosition(position);
    }

    public Vector2 getVelocity()
    {
        return velocity;
    }

    public void setVelocity(Vector2 velocity)
    {
        this.velocity = velocity;
    }

    public Polygon getPolygon()
    {
        return polygon;
    }

    public void setPolygon(Polygon polygon)
    {
        this.polygon = polygon;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Entity2D entity2D = (Entity2D) o;

        if (!polygon.equals(entity2D.polygon)) return false;
        if (!position.equals(entity2D.position)) return false;
        if (!velocity.equals(entity2D.velocity)) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = position.hashCode();
        result = 31 * result + velocity.hashCode();
        result = 31 * result + polygon.hashCode();
        return result;
    }

    @Override
    public String toString()
    {
        return "Entity2D{" +
               "position=" + position +
               ", velocity=" + velocity +
               ", polygon=" + polygon +
               '}';
    }
}
