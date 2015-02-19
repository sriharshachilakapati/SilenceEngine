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

        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
    }

    public Texture getCell(int row, int column)
    {
        float minU = (column * cellWidth + 0.5f) / texture.getWidth();
        float minV = (row * cellHeight + 0.5f) / texture.getHeight();
        float maxU = ((column + 1) * cellWidth - 1.5f) / texture.getWidth();
        float maxV = ((row + 1) * cellHeight - 1.5f) / texture.getHeight();

        return texture.getSubTexture(minU, minV, maxU, maxV, cellWidth, cellHeight);
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
