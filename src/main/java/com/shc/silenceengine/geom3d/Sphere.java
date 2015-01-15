package com.shc.silenceengine.geom3d;

import com.shc.silenceengine.math.Vector3;

/**
 * @author Sri Harsha Chilakapati
 */
public class Sphere extends Polyhedron
{
    private float radius;

    public Sphere(Vector3 position, float radius)
    {
        setPosition(position);

        this.radius = radius;

        updateVertices();
    }

    private void updateVertices()
    {
        clearVertices();

        // TODO:
    }

    public void rotate(Vector3 axis, float angle)
    {
        // No need to do anything here, Spheres don't rotate
    }

    public boolean intersects(Polyhedron other)
    {
        if (other instanceof Sphere)
        {
            Sphere o = (Sphere) other;
            return getPosition().distanceSquared(o.getPosition()) <= (radius + o.radius) * (radius + o.radius);
        }

        return super.intersects(other);
    }
}
