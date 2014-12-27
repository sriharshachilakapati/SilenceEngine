package com.shc.silenceengine.entity;

import com.shc.silenceengine.geom2d.Polygon;
import com.shc.silenceengine.geom2d.Rectangle;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.scene.SceneNode;

/**
 * This class represents all the 2D Entities in a Scene. Any entity which
 * is 2D and wants to be in a Scene must extend this class. Here is an
 * example entity.
 * <p>
 * <pre>
 *     public class MyEntity2D extends Entity2D
 *     {
 *         public MyEntity2D(Vector2 position, Polygon polygon)
 *         {
 *             setPolygon(polygon);
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
 *         public void collision(Entity2D other)
 *         {
 *             // Process collisions here
 *         }
 *     }
 * </pre>
 * <p>
 * Note that the collisions will only be notified if you are using a
 * SceneCollider2D and registered a collision check.
 *
 * @author Sri Harsha Chilakapati
 */
public class Entity2D extends SceneNode
{
    // The position, velocity and the polygon
    private Vector2 position;
    private Vector2 velocity;
    private Polygon polygon;

    /**
     * The default constructor.
     */
    public Entity2D()
    {
        position = new Vector2();
        velocity = new Vector2();
    }

    /**
     * Constructs a Entity2D to use a Polygon that can be used
     * to perform collisions.
     *
     * @param polygon The collision mask.
     */
    public Entity2D(Polygon polygon)
    {
        this();
        this.polygon = polygon;
    }

    /**
     * Prepares this Entity2D for a new frame. This method is not
     * meant to be called by the user and is called by the SceneGraph.
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

        // Calculate the new position
        position = position.add(velocity);
        polygon.setPosition(position);

        // Setup the local transform
        getLocalTransform().reset().translate(getPosition().subtract(getCenter()))
                                   .rotate(Vector3.AXIS_Z, polygon.getRotation())
                                   .translate(getCenter());
    }

    /**
     * Prepares this Entity2D for a new frame. This method is not meant
     * to be called by the user and is called by the SceneGraph.
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

    /**
     * Called by the SceneCollider2D instance to notify that a
     * collision event has occurred.
     *
     * @param other The other entity that collided with this entity.
     */
    public void collision(Entity2D other)
    {
    }

    /**
     * Moves this object to a specified point with a specific speed. Note that
     * the velocity used is independent of vertical or horizontal velocities of
     * this object.
     *
     * @param pos   The new position vector to move to
     * @param speed The speed with which to move
     * @return True if the new point has been reached
     */
    public boolean moveTo(Vector2 pos, float speed)
    {
        return moveTo(pos.x, pos.y, speed);
    }

    /**
     * Moves this object to a specified point with a specific speed. Note that
     * the velocity used is independent of vertical or horizontal velocities of
     * this object.
     *
     * @param nx    The new x-position
     * @param ny    The new y-position
     * @param speed The speed with which to move
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
     * This object bounces back from the other object in a natural way. Please
     * realize that the bounce is not completely accurate because this depends
     * on many properties. But in many situations the effect is good enough.
     */
    public void bounce(Entity2D other)
    {
        // We need to align to properly bounce
        alignNextTo(other);

        int xd = (int) ((other.position.x + other.getWidth() / 2) - (position.x + getWidth() / 2));
        int yd = (int) ((other.position.y + other.getHeight() / 2) - (position.y + getHeight() / 2));

        float dx = velocity.x;
        float dy = velocity.y;

        if (xd < 0)
            xd = -xd;

        if (yd < 0)
            yd = -yd;

        if (xd > yd)
            dx = -dx;
        else
            dy = -dy;

        velocity.x = dx;
        velocity.y = dy;
    }

    /**
     * This object is aligned next to an object.
     */
    public void alignNextTo(Entity2D other)
    {
        int xd = (int) ((other.position.x + other.getWidth() / 2) - (position.x + getWidth() / 2));
        int yd = (int) ((other.position.y + other.getHeight() / 2) - (position.y + getHeight() / 2));

        if (xd < 0)
            xd = -xd;

        if (yd < 0)
            yd = -yd;

        if (xd > yd)
            alignHorizontallyTo(other);
        else
            alignVerticallyTo(other);
    }

    /**
     * Align this object horizontally with other
     */
    public void alignHorizontallyTo(Entity2D other)
    {
        if (getX() > other.getX())
        {
            setX(other.getX() + other.getWidth());
        }
        else if (getX() < other.getX())
        {
            setX(other.getX() - getWidth());
        }
    }

    /**
     * Align this object vertically with other
     */
    public void alignVerticallyTo(Entity2D other)
    {
        if (getY() > other.getY())
        {
            setY(other.getY() + other.getHeight());
        }
        else if (getY() < other.getY())
        {
            setY(other.getY() - getHeight());
        }
    }

    /**
     * Rotates the entity by a specified angle
     *
     * @param angle The angle to rate with (in degrees)
     */
    public void rotate(float angle)
    {
        polygon.rotate(angle);

        getLocalTransform().reset().translate(getPosition().subtract(getCenter()))
                           .rotate(Vector3.AXIS_Z, polygon.getRotation())
                           .translate(getCenter());
    }

    /**
     * @return The rotation of this entity.
     */
    public float getRotation()
    {
        return polygon.getRotation();
    }

    /**
     * Sets the rotation of this entity. Note that the same rotation
     * is also applied to the polygon this entity is using.
     *
     * @param rotation The amount of rotation (in degrees)
     */
    public void setRotation(float rotation)
    {
        polygon.setRotation(rotation);

        getLocalTransform().reset().translate(getPosition().subtract(getCenter()))
                           .rotate(Vector3.AXIS_Z, polygon.getRotation())
                           .translate(getCenter());
    }

    /**
     * Sets the center position of this entity. Note that the same
     * rotation is also applied to the polygon this entity is using.
     *
     * @param center The new center position
     */
    public void setCenter(Vector2 center)
    {
        polygon.setCenter(center);
        position = polygon.getPosition();

        getLocalTransform().reset().translate(getPosition().subtract(getCenter()))
                           .rotate(Vector3.AXIS_Z, polygon.getRotation())
                           .translate(getCenter());
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
     * @return The y-coordinate of this entity
     */
    public float getY()
    {
        return position.getY();
    }

    /**
     * Sets the x-coordinate of the position
     * @param x The x-coordinate of the position
     */
    public void setX(float x)
    {
        position.setX(x);
        polygon.setPosition(position);
    }

    /**
     * Sets the y-coordinate of the position
     * @param y The y-coordinate of the position
     */
    public void setY(float y)
    {
        position.setY(y);
        polygon.setPosition(position);
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
     * @return The center position of the entity
     */
    public Vector2 getCenter()
    {
        return polygon.getCenter();
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
        this.position = position;
        polygon.setPosition(position);

        getLocalTransform().reset().translate(getPosition().subtract(getCenter()))
                           .rotate(Vector3.AXIS_Z, polygon.getRotation())
                           .translate(getCenter());
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
        this.velocity = velocity;
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

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Entity2D entity2D = (Entity2D) o;

        return polygon.equals(entity2D.polygon) &&
               position.equals(entity2D.position) &&
               velocity.equals(entity2D.velocity);

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
