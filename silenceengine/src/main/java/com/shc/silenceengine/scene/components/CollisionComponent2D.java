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
 *
 */

package com.shc.silenceengine.scene.components;

import com.shc.silenceengine.collision.CollisionTag;
import com.shc.silenceengine.math.geom2d.Polygon;
import com.shc.silenceengine.scene.entity.Entity2D;
import com.shc.silenceengine.utils.IDGenerator;

/**
 * @author Sri Harsha Chilakapati
 */
public class CollisionComponent2D implements IComponent2D
{
    public final long id = IDGenerator.generate();

    public CollisionCallback callback;
    public CollisionTag      tag;
    public Polygon           polygon;

    public Entity2D entity;

    public CollisionComponent2D(CollisionTag tag, Polygon polygon, CollisionCallback callback)
    {
        this.tag = tag;
        this.polygon = polygon;
        this.callback = callback;
    }

    @Override
    public void init(Entity2D entity)
    {
        this.entity = entity;
    }

    @Override
    public void update(float deltaTime)
    {
        polygon.setPosition(entity.position);
        polygon.setRotation(entity.rotation);
        polygon.setScale(entity.scale);
    }

    @Override
    public void render(float deltaTime)
    {
    }

    @Override
    public void dispose()
    {
    }

    @FunctionalInterface
    public interface CollisionCallback
    {
        void handleCollision(Entity2D other);
    }
}
