package com.shc.silenceengine.graphics.opengl;

import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.math.Vector2;

/**
 * @author Sri Harsha Chilakapati
 */
public class SubTexture extends Texture
{
    private Texture parent;

    private float minU;
    private float minV;
    private float maxU;
    private float maxV;

    private float width;
    private float height;

    public SubTexture(Texture parent, Vector2 min, Vector2 max)
    {
        this(parent, min.getX(), min.getY(), max.getX(), max.getY());
    }

    public SubTexture(Texture parent, float minU, float minV, float maxU, float maxV)
    {
        super(parent.getId());

        this.parent = parent;
        this.minU = minU;
        this.minV = minV;

        this.maxU = maxU;
        this.maxV = maxV;

        this.width = (maxU - minU) * parent.getWidth();
        this.height = (maxV - minV) * parent.getHeight();
    }

    public SubTexture(Texture parent, float minU, float minV, float maxU, float maxV, float width, float height)
    {
        this(parent, minU, minV, maxU, maxV);

        this.width = width;
        this.height = height;
    }

    public SubTexture getSubTexture(float minU, float minV, float maxU, float maxV)
    {
        minU = (minU * getWidth()) / parent.getWidth();
        minV = (minV * getHeight()) / parent.getHeight();
        maxU = (maxU * getWidth()) / parent.getWidth();
        maxV = (maxV * getHeight()) / parent.getHeight();

        minU += this.minU;
        minV += this.minV;
        maxU += minU;
        maxV += minV;

        return new SubTexture(parent, minU, minV, maxU, maxV);
    }

    public SubTexture getSubTexture(Vector2 min, Vector2 max)
    {
        return getSubTexture(min.x, min.y, max.x, max.y);
    }

    public float getMinU()
    {
        return minU;
    }

    public float getMaxU()
    {
        return maxU;
    }

    public float getMinV()
    {
        return minV;
    }

    public float getMaxV()
    {
        return maxV;
    }

    public float getWidth()
    {
        return width;
    }

    public float getHeight()
    {
        return height;
    }

    public Texture getParent()
    {
        return parent;
    }

    public void dispose()
    {
        throw new SilenceException("Sub-Textures cannot be disposed! Just ignore them!");
    }
}
