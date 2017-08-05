/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 Sri Harsha Chilakapati
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
import com.shc.silenceengine.utils.ReusableStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
public class SpriteBatch
{
    private static final ReusableStack<BatchEntry> REUSABLE_STACK = new ReusableStack<>(BatchEntry::new);

    private final List<BatchEntry> entries;
    private final SpriteRenderer   spriteRenderer;

    public SpriteBatch(SpriteRenderer spriteRenderer)
    {
        this.entries = new ArrayList<>();
        this.spriteRenderer = spriteRenderer;
    }

    public void begin()
    {
        entries.forEach(REUSABLE_STACK::push);
        entries.clear();
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
        BatchEntry entry = REUSABLE_STACK.pop();

        entry.sprite = sprite;
        entry.transform = transform;
        entry.tint = tint;
        entry.opacity = opacity;
        entry.layer = layer;

        entries.add(entry);
    }

    public void end()
    {
        entries.sort(SpriteBatch::entryComparator);
        spriteRenderer.begin();

        entries.forEach(entry ->
        {
            Sprite sprite = entry.sprite;
            Transform transform = entry.transform;
            Color tint = entry.tint;
            float opacity = entry.opacity;

            spriteRenderer.render(sprite, transform, tint, opacity);
        });

        spriteRenderer.end();
    }

    private static int entryComparator(BatchEntry e1, BatchEntry e2)
    {
        if (e1.layer == e2.layer)
            return e1.sprite.getCurrentFrame().getID() - e2.sprite.getCurrentFrame().getID();

        return e2.layer - e1.layer;
    }

    private static class BatchEntry
    {
        Sprite sprite;
        Color  tint;

        float opacity;
        int   layer;

        Transform transform;
    }
}
