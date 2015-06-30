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

import com.shc.silenceengine.collision.Collision2D;
import com.shc.silenceengine.core.IUpdatable;
import com.shc.silenceengine.graphics.Sprite;
import com.shc.silenceengine.graphics.SpriteBatch;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.geom2d.Polygon;
import com.shc.silenceengine.math.geom2d.Rectangle;
import com.shc.silenceengine.utils.IDGenerator;

/**
 * <p> This class represents all the 2D Entities in a Scene. Any entity which is 2D and wants to be in a Scene must
 * extend this class. Here is an example entity.</p>
 *
 * <pre>
 *     public class MyEntity2D extends Entity2D
 *     {
 *         public MyEntity2D(Vector2 position, Polygon polygon)
 *         {
 *             super(new Sprite(Resources.Textures.MY_TEXTURE), polygon);
 *             setPosition(position);
 *         }
 *
 *         public void update(float delta)
 *         {
 *             // Update the entity here
 *         }
 *
 *         public void collision(Entity2D other)
 *         {
 *             // Process collisions here
 *         }
 *     }
 * </pre>
 *
 * <p> Note that the collisions will only be notified if you are using a SceneCollider2D and registered a collision
 * check.</p>
 *
 * @author Sri Harsha Chilakapati
 */
public class Entity2D implements IUpdatable
{
    Vector2 temp  = new Vector2();
    Vector2 temp2 = new Vector2();

    // The sprite
    private Sprite sprite;

    // The position, velocity and the polygon
    private Vector2 position;
    private Vector2 velocity;
    private Polygon polygon;

    // Depth here indicates the ordering of entity rendering.
    // The higher the depth, the first the object is rendered.
    private int depth;
    private int id;

    // Whether this entity is destroyed
    private boolean destroyed;

    /**
     * Constructs an Entity2D to use a polygon that can be used to perform collisions.
     *
     * @param polygon The collision mask.
     */
    public Entity2D(Polygon polygon)
    {
        this(Sprite.EMPTY, polygon);
    }

    /**
     * Constructs a Entity2D to use a Polygon that can be used to perform collisions.
     *
     * @param sprite  The sprite to use for this entity.
     * @param polygon The collision mask.
     */
    public Entity2D(Sprite sprite, Polygon polygon)
    {
        this.sprite = sprite.copy();
        this.polygon = polygon;

        position = new Vector2();
        velocity = new Vector2();

        depth = 0;
        id = IDGenerator.generate();
    }

    /**
     * Prepares this Entity2D for a new frame. This method is not meant to be called by the user and is called by the
     * Scene2D class. This is where the velocity is added to the position after the update is called. This also updates
     * the sprite.
     *
     * @param delta The delta time.
     */
    public void preUpdate(float delta)
    {
        if (isDestroyed())
            return;

        update(delta);

        if (velocity == Vector2.ZERO)
            return;

        Vector2 tempVec2 = Vector2.REUSABLE_STACK.pop();

        // Calculate the new position
        setPosition(tempVec2.set(position).addSelf(velocity));

        Vector2.REUSABLE_STACK.push(tempVec2);

        sprite.update(delta);
        sprite.setRotation(getRotation());
    }

    public void update(float delta)
    {
    }

    /**
     * @return The position of this entity
     */
    public Vector2 getPosition()
    {
        return position;
    }

    /**
     * Sets the position of this entity
     *
     * @param position The new position as a Vector2
     */
    public void setPosition(Vector2 position)
    {
        this.position.set(position);
        polygon.setPosition(position);
    }

    /**
     * @return The center position of the entity
     */
    public Vector2 getCenter()
    {
        return polygon.getCenter();
    }

    /**
     * Sets the center position of this entity. Note that the same rotation is also applied to the polygon this entity
     * is using.
     *
     * @param center The new center position
     */
    public void setCenter(Vector2 center)
    {
        polygon.setCenter(center);
        position.set(polygon.getPosition());

        Vector2 tempVec2 = Vector2.REUSABLE_STACK.pop();

        Vector2.REUSABLE_STACK.push(tempVec2);
    }

    /**
     * Called by the SceneCollider2D instance to notify that a collision event has occurred.
     *
     * @param other The other entity that collided with this entity.
     */
    public void collision(Entity2D other)
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
    public boolean moveTo(Vector2 pos, float speed)
    {
        return moveTo(pos.x, pos.y, speed);
    }

    /**
     * Moves this object to a specified point with a specific speed. Note that the velocity used is independent of
     * vertical or horizontal velocities of this object.
     *
     * @param nx    The new x-position
     * @param ny    The new y-position
     * @param speed The speed with which to move
     *
     * @return True if the new point has been reached
     */
    public boolean moveTo(float nx, float ny, float speed)
    {
        boolean _x = false;
        boolean _y = false;

        float x = position.x;
        float y = position.y;

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

        position.x = x;
        position.y = y;

        polygon.setPosition(position);

        return (_x && _y);
    }

    /**
     * This object bounces back from the other object in a natural way. Please realize that the bounce is not completely
     * accurate because this depends on many properties. But in many situations the effect is good enough.
     *
     * @param other The other entity to bounce against.
     */
    public void bounce(Entity2D other)
    {
        float xd = getBounds().getIntersectionWidth(other.getBounds());
        float yd = getBounds().getIntersectionHeight(other.getBounds());

        float dx = velocity.x;
        float dy = velocity.y;

        if (xd > yd)
            dy = -dy;
        else
            dx = -dx;

        velocity.x = dx;
        velocity.y = dy;

        alignNextTo(other);
    }

    /**
     * Renders this entity using a SpriteBatch and a delta time. The SpriteBatch must be active before calling this
     * method. This also takes delta time into account.
     *
     * @param delta The delta-time. A fraction between 0 and 1, where 0 means this exact frame, and 1 means we're in lag.
     * @param batch The SpriteBatch to which the sprite should be submitted to be rendered.
     */
    public void render(float delta, SpriteBatch batch)
    {
        temp.set(getPosition());
        temp2.set(getVelocity());
        temp2.normalizeSelf();
        temp2.scaleSelf(delta);
        temp.addSelf(temp2);

        batch.addSprite(sprite, temp);
    }

    /**
     * @return The width of the bounding rectangle of this entity
     */
    public float getWidth()
    {
        return polygon.getBounds().getWidth();
    }

    /**
     * @return The height of the bounding rectangle of this entity
     */
    public float getHeight()
    {
        return polygon.getBounds().getHeight();
    }

    /**
     * Aligns this entity over another entity such that they both will collide in the next frame. Note that this method
     * is not guaranteed to be 100% accurate, so if you can, use your own logic. However, this supports any polygons
     * instead of just bounding boxes.
     *
     * @param other The other entity which this entity should be aligned with.
     */
    public void alignNextTo(Entity2D other)
    {
        Vector2 tempVec2 = Vector2.REUSABLE_STACK.pop();
        Vector2 direction = Vector2.REUSABLE_STACK.pop();

        Vector2 tCenter = getCenter();
        Vector2 oCenter = other.getCenter();

        direction.set(tCenter).subtractSelf(oCenter).normalizeSelf();
        setCenter(direction.addSelf(getCenter()));

        Collision2D.Response response = new Collision2D.Response();
        Collision2D.testPolygonCollision(polygon, other.getPolygon(), response);

        setPosition(tempVec2.set(position).subtractSelf(response.getMinimumTranslationVector()));

        Vector2.REUSABLE_STACK.push(tempVec2);
        Vector2.REUSABLE_STACK.push(direction);
    }

    /**
     * @return The collision polygon of this entity
     */
    public Polygon getPolygon()
    {
        return polygon;
    }

    /**
     * Sets the Polygon this entity should use to check for collisions.
     *
     * @param polygon The new collision polygon
     */
    public void setPolygon(Polygon polygon)
    {
        this.polygon = polygon;
    }

    /**
     * Rotates the entity by a specified angle
     *
     * @param angle The angle to rate with (in degrees)
     */
    public void rotate(float angle)
    {
        polygon.rotate(angle);
    }

    /**
     * Gets the depth of this entity.
     *
     * @return The depth of this entity.
     */
    public int getDepth()
    {
        return depth;
    }

    /**
     * Sets the depth layer that this entity lives in the scene.
     *
     * @param depth The new depth of the entity.
     */
    public void setDepth(int depth)
    {
        this.depth = depth;
    }

    /**
     * @return The rotation of this entity.
     */
    public float getRotation()
    {
        return polygon.getRotation();
    }

    /**
     * Sets the rotation of this entity. Note that the same rotation is also applied to the polygon this entity is
     * using.
     *
     * @param rotation The amount of rotation (in degrees)
     */
    public void setRotation(float rotation)
    {
        polygon.setRotation(rotation);
        sprite.setRotation(rotation);
    }

    /**
     * Returns the ID of this Entity. Note that this ID differs per instance and not per class.
     *
     * @return the ID of this Entity.
     */
    public int getID()
    {
        return id;
    }

    /**
     * @return The Rectangle that bounds this entity
     */
    public Rectangle getBounds()
    {
        return polygon.getBounds();
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
        polygon.setPosition(position);
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
        polygon.setPosition(position);
    }

    /**
     * Checks the intersection with another Entity2D.
     *
     * @param other The other Entity2D to test intersection with
     *
     * @return True if intersects, else false.
     */
    public boolean intersects(Entity2D other)
    {
        return polygon.intersects(other.getPolygon());
    }

    /**
     * @return The velocity of this entity
     */
    public Vector2 getVelocity()
    {
        return velocity;
    }

    /**
     * Sets the velocity of this entity
     *
     * @param velocity The velocity as a Vector2
     */
    public void setVelocity(Vector2 velocity)
    {
        this.velocity.set(velocity);
    }

    /**
     * Returns whether this entity is destroyed.
     *
     * @return True if destroyed, else false.
     */
    public boolean isDestroyed()
    {
        return destroyed;
    }

    /**
     * Destroys this entity.
     */
    public void destroy()
    {
        destroyed = true;
    }

    /**
     * Returns the sprite being used by this entity.
     *
     * @return The currently used sprite.
     */
    public Sprite getSprite()
    {
        return sprite;
    }

    /**
     * Sets the sprite that should be used by this entity.
     *
     * @param sprite The new sprite to be used.
     */
    public void setSprite(Sprite sprite)
    {
        this.sprite.set(sprite);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Entity2D entity2D = (Entity2D) o;

        return getDepth() == entity2D.getDepth() &&
               getID() == entity2D.getID() &&
               isDestroyed() == entity2D.isDestroyed() &&
               getSprite().equals(entity2D.getSprite()) &&
               getPosition().equals(entity2D.getPosition()) &&
               getVelocity().equals(entity2D.getVelocity()) &&
               getPolygon().equals(entity2D.getPolygon());
    }

    @Override
    public int hashCode()
    {
        int result = getSprite().hashCode();
        result = 31 * result + getPosition().hashCode();
        result = 31 * result + getVelocity().hashCode();
        result = 31 * result + getPolygon().hashCode();
        result = 31 * result + getDepth();
        result = 31 * result + getID();
        result = 31 * result + (isDestroyed() ? 1 : 0);
        return result;
    }
}
