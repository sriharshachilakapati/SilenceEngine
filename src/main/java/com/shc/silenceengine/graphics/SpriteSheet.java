package com.shc.silenceengine.graphics;

import com.shc.silenceengine.graphics.opengl.Texture;

/**
 * @author Sri Harsha Chilakapati
 */
public class SpriteSheet
{
    private Texture texture;

    private float cellWidth;
    private float cellHeight;

    public SpriteSheet(Texture texture, float cellWidth, float cellHeight)
    {
        this.texture = texture;

        this.cellWidth  = cellWidth;
        this.cellHeight = cellHeight;
    }

    public Texture getCell(int row, int column)
    {
        float minU = (column * cellWidth) / texture.getWidth();
        float minV = (row * cellHeight) / texture.getHeight();
        float maxU = ((column + 1) * cellWidth) / texture.getWidth();
        float maxV = ((row + 1) * cellHeight) / texture.getHeight();

        return texture.getSubTexture(minU, minV, maxU, maxV);
    }

    public Texture getTexture()
    {
        return texture;
    }

    public float getCellWidth()
    {
        return cellWidth;
    }

    public float getCellHeight()
    {
        return cellHeight;
    }
}
