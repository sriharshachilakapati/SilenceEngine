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

import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.graphics.opengl.Primitive;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.math.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
public class SpriteBatch
{
    private List<Sprite>  sprites;
    private List<Integer> indices;
    private List<Vector2> positions;

    private boolean active;

    public SpriteBatch()
    {
        sprites = new ArrayList<>();
        indices = new ArrayList<>();
        positions = new ArrayList<>();

        active = false;
    }

    public void begin()
    {
        if (active)
            throw new SilenceException("SpriteBatch already active");

        sprites.clear();
        indices.clear();
        positions.clear();

        active = true;
    }

    public void flush()
    {
        if (sprites.size() == 0)
            return;

        sortSprites();

        Batcher batcher = SilenceEngine.graphics.getBatcher();

        Texture originalTexture = Texture.CURRENT;

        Texture texture = sprites.get(indices.get(0)).getTexture();
        texture.bind();

        Vector2 temp = Vector2.REUSABLE_STACK.pop();

        batcher.begin(Primitive.TRIANGLES);
        {
            for (int i : indices)
            {
                Sprite sprite = sprites.get(i);
                Vector2 position = positions.get(i);

                Texture t = sprite.getTexture();

                if (t.getID() != texture.getID())
                {
                    batcher.end();

                    texture = t;
                    t.bind();

                    batcher.begin(Primitive.TRIANGLES);
                }

                // Triangle 1
                batcher.vertex(temp.set(-t.getWidth() / 2, -t.getHeight() / 2)  // Top-left
                        .rotateSelf(sprite.getRotation())
                        .scaleSelf(sprite.getScaleX(), sprite.getScaleY())
                        .addSelf(position).addSelf(t.getWidth() / 2, t.getHeight() / 2));
                batcher.texCoord(t.getMinU(), t.getMinV());
                batcher.vertex(temp.set(t.getWidth() / 2, -t.getHeight() / 2)   // Top-right
                        .rotateSelf(sprite.getRotation())
                        .scaleSelf(sprite.getScaleX(), sprite.getScaleY())
                        .addSelf(position).addSelf(t.getWidth() / 2, t.getHeight() / 2));
                batcher.texCoord(t.getMaxU(), t.getMinV());
                batcher.vertex(temp.set(-t.getWidth() / 2, t.getHeight() / 2)   // Bottom-left
                        .rotateSelf(sprite.getRotation())
                        .scaleSelf(sprite.getScaleX(), sprite.getScaleY())
                        .addSelf(position).addSelf(t.getWidth() / 2, t.getHeight() / 2));
                batcher.texCoord(t.getMinU(), t.getMaxV());

                // Triangle 2
                batcher.vertex(temp.set(t.getWidth() / 2, -t.getHeight() / 2)   // Top-right
                        .rotateSelf(sprite.getRotation())
                        .scaleSelf(sprite.getScaleX(), sprite.getScaleY())
                        .addSelf(position).addSelf(t.getWidth() / 2, t.getHeight() / 2));
                batcher.texCoord(t.getMaxU(), t.getMinV());
                batcher.vertex(temp.set(t.getWidth() / 2, t.getHeight() / 2)   // Bottom-right
                        .rotateSelf(sprite.getRotation())
                        .scaleSelf(sprite.getScaleX(), sprite.getScaleY())
                        .addSelf(position).addSelf(t.getWidth() / 2, t.getHeight() / 2));
                batcher.texCoord(t.getMaxU(), t.getMaxV());
                batcher.vertex(temp.set(-t.getWidth() / 2, t.getHeight() / 2)   // Bottom-left
                        .rotateSelf(sprite.getRotation())
                        .scaleSelf(sprite.getScaleX(), sprite.getScaleY())
                        .addSelf(position).addSelf(t.getWidth() / 2, t.getHeight() / 2));
                batcher.texCoord(t.getMinU(), t.getMaxV());
            }
        }
        batcher.end();

        Vector2.REUSABLE_STACK.push(temp);

        sprites.clear();
        indices.clear();
        positions.clear();

        originalTexture.bind();
    }

    public void end()
    {
        if (!active)
            throw new SilenceException("SpriteBatch is not active");

        flush();
        active = false;
    }

    private void sortSprites()
    {
        // Only sort the indices
        indices.sort((i, j) -> sprites.get(i).getTexture().getID() < sprites.get(j).getTexture().getID() ? 1 : -1);
    }

    public void addSprite(Sprite sprite, Vector2 position)
    {
        sprites.add(sprite);
        positions.add(position);
        indices.add(sprites.size() - 1);
    }
}
