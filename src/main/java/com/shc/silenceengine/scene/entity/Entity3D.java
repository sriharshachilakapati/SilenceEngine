/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2015 Sri Harsha Chilakapati
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.shc.silenceengine.scene.entity;

import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.math.geom3d.Cuboid;
import com.shc.silenceengine.math.geom3d.Polyhedron;
import com.shc.silenceengine.scene.SceneNode;

/**
 * This class represents all the 3D Entities in a Scene. Any entity which is 2D and wants to be in a Scene must extend
 * this class. Here is an example entity. <p>
 * <pre>
 *     public class MyEntity3D extends Entity3D
 *     {
 *         public MyEntity3D(Vector3 position, Polyhedron polyhedron)
 *         {
 *             setPolyhedron(polyhedron);
 *             setPosition(position);
 *         }
 *
 *         public void update(float delta)
 *         {
 *             // Update the entity here
 *         }
 *
 *         public void render(float delta, Batcher batcher)
 *         {
 *             // Render the entity to the screen
 *         }
 *
 *         public void collision(Entity3D other)
 *         {
 *             // Process collisions here
 *         }
 *     }
 * </pre>
 * <p> Note that the collisions will only be notified if you are using a ISceneCollider3D and registered a collision
 * check.
 *
 * @author Sri Harsha Chilakapati
 */
public class Entity3D extends SceneNode
{
    // The position, velocity and the polygon
    private Vector3    position;
    private Vector3    velocity;
    private Polyhedron polyhedron;

    /**
     * Constructs a Entity3D to use a Polyhedron that can be used to perform collisions.
     *
     * @param polyhedron The collision mask.
     */
    public Entity3D(Polyhedron polyhedron)
    {
        this();
        this.polyhedron = polyhedron;
    }

    /**
     * The default constructor.
     */
    public Entity3D()
    {
        position = new Vector3();
        velocity = new Vector3();
    }

    /**
     * Prepares this Entity3D for a new frame. This method is not meant to be called by the user and is called by the
     * SceneGraph.
     *
     * @param delta The delta time.
     */
    public void preUpdate(float delta)
    {
        if (isDestroyed())
            return;

        update(delta);

        if (velocity == Vector3.ZERO)
            return;

        // Calculate the new position
        position.addSelf(velocity);
        polyhedron.setPosition(position);

        // Update the transforms
        updateTransforms();
    }

    /**
     * Prepares this Entity3D for a new frame. This method is not meant to be called by the user and is called by the
     * SceneGraph.
     *
     * @param delta   The delta time.
     * @param batcher The Batcher to batch rendering.
     */
    public void preRender(float delta, Batcher batcher)
    {
        if (isDestroyed())
            return;

        super.preRender(delta, batcher);
    }

    private void updateTransforms()
    {
        getLocalTransform().reset()
                .rotateSelf(Vector3.AXIS_X, polyhedron.getRotationX())
                .rotateSelf(Vector3.AXIS_Z, polyhedron.getRotationZ())
                .rotateSelf(Vector3.AXIS_Y, polyhedron.getRotationY())
                .translateSelf(getPosition());
    }

    /**
     * @return The position of this entity
     */
    public Vector3 getPosition()
    {
        return position;
    }

    /**
     * Sets the position of this entity
     *
     * @param position The new position as a Vector3
     */
    public void setPosition(Vector3 position)
    {
        this.position.set(position);
        polyhedron.setPosition(position);
        updateTransforms();
    }

    /**
     * Called by the ISceneCollider3D instance to notify that a collision event has occurred.
     *
     * @param other The other entity that collided with this entity.
     */
    public void collision(Entity3D other)
    {
    }

    /**
     * Moves this object to a specified point with a specific speed. Note that the velocity used is independent of
     * vertical or horizontal velocities of this object.
     *
     * @param pos   The new position vector to move to
     * @param speed The speed with which to move
     *
     * @return True if the new point has been reached
     */
    public boolean moveTo(Vector3 pos, float speed)
    {
        return moveTo(pos.x, pos.y, pos.z, speed);
    }

    /**
     * Moves this object to a specified point with a specific speed. Note that the velocity used is independent of
     * vertical or horizontal velocities of this object.
     *
     * @param nx    The new x-position
     * @param ny    The new y-position
     * @param nz    The new z-position
     * @param speed The speed with which to move
     *
     * @return True if the new point has been reached
     */
    public boolean moveTo(float nx, float ny, float nz, float speed)
    {
        boolean _x = false;
        boolean _y = false;
        boolean _z = false;

        float x = position.x;
        float y = position.y;
        float z = position.z;

        int distance = (int) Math.sqrt((double) ((x - nx) * (x - nx) + (y - ny) * (y - ny)));

        float vel = Math.min(distance, speed);

        if (x > nx)
            x -= vel;
        else if (x < nx)
            x += vel;
        else
            _x = true;

        if (y > ny)
            y -= vel;
        else if (y < ny)
            y += vel;
        else
            _y = true;

        if (z > nz)
            z -= vel;
        else if (z < nz)
            z += vel;
        else
            _z = true;

        position.x = x;
        position.y = y;
        position.z = z;

        polyhedron.setPosition(position);

        return (_x && _y && _z);
    }

    /**
     * Rotates the entity by a specified angle
     *
     * @param rx The angle to rate with on X-axis (in degrees)
     * @param ry The angle to rate with on Y-axis (in degrees)
     * @param rz The angle to rate with on Z-axis (in degrees)
     */
    public void rotate(float rx, float ry, float rz)
    {
        polyhedron.rotate(rx, ry, rz);
        updateTransforms();
    }

    /**
     * @return The Cuboid that bounds this entity
     */
    public Cuboid getBounds()
    {
        return polyhedron.getBounds();
    }

    /**
     * @return The x-coordinate of this entity
     */
    public float getX()
    {
        return position.getX();
    }

    /**
     * Sets the x-coordinate of the position
     *
     * @param x The x-coordinate of the position
     */
    public void setX(float x)
    {
        position.setX(x);
        polyhedron.setPosition(position);
        updateTransforms();
    }

    /**
     * @return The y-coordinate of this entity
     */
    public float getY()
    {
        return position.getY();
    }

    /**
     * Sets the y-coordinate of the position
     *
     * @param y The y-coordinate of the position
     */
    public void setY(float y)
    {
        position.setY(y);
        polyhedron.setPosition(position);
        updateTransforms();
    }

    /**
     * @return The z-coordinate of this entity
     */
    public float getZ()
    {
        return position.getZ();
    }

    /**
     * Sets the z-coordinate of the position
     *
     * @param z The z-coordinate of the position
     */
    public void setZ(float z)
    {
        position.setZ(z);
        polyhedron.setPosition(position);
        updateTransforms();
    }

    /**
     * @return The width of the bounding box of this entity
     */
    public float getWidth()
    {
        return polyhedron.getBounds().getWidth();
    }

    /**
     * @return The height of the bounding box of this entity
     */
    public float getHeight()
    {
        return polyhedron.getBounds().getHeight();
    }

    /**
     * @return The thickness of the bounding box of this entity
     */
    public float getThickness()
    {
        return polyhedron.getBounds().getThickness();
    }

    /**
     * Checks the intersection with another Entity3D.
     *
     * @param other The other Entity3D to test intersection with
     *
     * @return True if intersects, else false.
     */
    public boolean intersects(Entity3D other)
    {
        return polyhedron.intersects(other.getPolyhedron());
    }

    /**
     * @return The collision polyhedron of this entity
     */
    public Polyhedron getPolyhedron()
    {
        return polyhedron;
    }

    /**
     * Sets the Polyhedron this entity should use to check for collisions.
     *
     * @param polyhedron The new collision polyhedron
     */
    public void setPolyhedron(Polyhedron polyhedron)
    {
        this.polyhedron = polyhedron;
    }

    /**
     * @return The velocity of this entity
     */
    public Vector3 getVelocity()
    {
        return velocity;
    }

    /**
     * Sets the velocity of this entity
     *
     * @param velocity The velocity as a Vector3
     */
    public void setVelocity(Vector3 velocity)
    {
        this.velocity = velocity;
    }

    @Override
    public int hashCode()
    {
        int result = position.hashCode();
        result = 31 * result + velocity.hashCode();
        result = 31 * result + polyhedron.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Entity3D entity3D = (Entity3D) o;

        return polyhedron.equals(entity3D.polyhedron) &&
               position.equals(entity3D.position) &&
               velocity.equals(entity3D.velocity);

    }

    @Override
    public String toString()
    {
        return "Entity3D{" +
               "position=" + position +
               ", velocity=" + velocity +
               ", polyhedron=" + polyhedron +
               '}';
    }
}
