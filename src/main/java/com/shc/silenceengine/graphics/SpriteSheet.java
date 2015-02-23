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
