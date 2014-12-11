package com.shc.silenceengine.graphics.opengl;

import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.math.Vector2;

/**
 * @author Sri Harsha Chilakapati
 */
public class SubTexture extends Texture
{
    private Texture parent;
    private float   minU;
    private float   minV;
    private float   maxU;
    private float   maxV;

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
        return maxU * parent.getWidth() - minU * parent.getWidth();
    }

    public float getHeight()
    {
        return maxV * parent.getHeight() - minV * parent.getHeight();
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
