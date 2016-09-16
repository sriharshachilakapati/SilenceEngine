/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2016 Sri Harsha Chilakapati
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

import com.shc.silenceengine.math.Transform;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
public class SpriteBatch
{
    private List<Integer>   indices    = new ArrayList<>();
    private List<Float>     opacities  = new ArrayList<>();
    private List<Sprite>    sprites    = new ArrayList<>();
    private List<Transform> transforms = new ArrayList<>();
    private List<Integer>   layers     = new ArrayList<>();
    private List<Color>     tints      = new ArrayList<>();

    private SpriteRenderer spriteRenderer;

    public SpriteBatch(SpriteRenderer spriteRenderer)
    {
        this.spriteRenderer = spriteRenderer;
    }

    public void begin()
    {
        opacities.clear();
        indices.clear();
        sprites.clear();
        transforms.clear();
        layers.clear();
        tints.clear();
    }

    public void render(Sprite sprite, Transform transform)
    {
        render(sprite, transform, Color.BLACK);
    }

    public void render(Sprite sprite, Transform transform, Color tint)
    {
        render(sprite, transform, tint, 1);
    }

    public void render(Sprite sprite, Transform transform, Color tint, float opacity)
    {
        render(sprite, transform, tint, opacity, 0);
    }

    public void render(Sprite sprite, Transform transform, Color tint, float opacity, int layer)
    {
        sprites.add(sprite);
        transforms.add(transform);
        tints.add(tint);
        opacities.add(opacity);
        layers.add(layer);
        indices.add(indices.size());
    }

    public void end()
    {
        Collections.sort(indices, (i1, i2) ->
        {
            int layer1 = layers.get(i1);
            int layer2 = layers.get(i2);

            if (layer1 == layer2)
            {
                int texID1 = sprites.get(i1).getCurrentFrame().getID();
                int texID2 = sprites.get(i2).getCurrentFrame().getID();

                return texID1 - texID2;
            }

            return layer2 - layer1;
        });

        spriteRenderer.begin();

        for (int i : indices)
        {
            Sprite sprite = sprites.get(i);
            Transform transform = transforms.get(i);
            Color tint = tints.get(i);
            float opacity = opacities.get(i);

            spriteRenderer.render(sprite, transform, tint, opacity);
        }

        spriteRenderer.end();
    }
}
