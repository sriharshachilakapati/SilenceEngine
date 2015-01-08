package com.shc.silenceengine.collision.sat2d;

import com.shc.silenceengine.geom2d.Polygon;
import com.shc.silenceengine.math.Vector2;

/**
 * @author Sri Harsha Chilakapati
 */
public class Response2D
{
    public Polygon a;
    public Polygon b;

    public Vector2 overlapV;
    public Vector2 overlapN;

    public float overlap;

    public boolean aInB;
    public boolean bInA;

    public Response2D()
    {
        a = b = null;
        overlapV = new Vector2();
        overlapN = new Vector2();

        clear();
    }

    public Response2D clear()
    {
        aInB = true;
        bInA = true;

        overlap = Float.MAX_VALUE;
        return this;
    }

    @Override
    public String toString()
    {
        return "Response2D{" +
                "a=" + a +
                ", b=" + b +
                ", overlapV=" + overlapV +
                ", overlapN=" + overlapN +
                ", overlap=" + overlap +
                ", aInB=" + aInB +
                ", bInA=" + bInA +
                '}';
    }
}
