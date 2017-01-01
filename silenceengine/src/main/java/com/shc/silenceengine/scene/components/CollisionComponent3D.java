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

package com.shc.silenceengine.scene.components;

import com.shc.silenceengine.collision.CollisionTag;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.math.geom3d.Polyhedron;
import com.shc.silenceengine.scene.entity.Entity3D;
import com.shc.silenceengine.utils.IDGenerator;

/**
 * @author Sri Harsha Chilakapati
 */
public class CollisionComponent3D implements IComponent3D
{
    public final long id = IDGenerator.generate();

    public CollisionCallback callback;
    public CollisionTag      tag;
    public Polyhedron        polyhedron;

    public Entity3D entity;

    public CollisionComponent3D(CollisionTag tag, Polyhedron polyhedron)
    {
        this.tag = tag;
        this.polyhedron = polyhedron;
    }

    public CollisionComponent3D(CollisionTag tag, Polyhedron polyhedron, CollisionCallback callback)
    {
        this.tag = tag;
        this.callback = callback;
        this.polyhedron = polyhedron;
    }

    @Override
    public void init(Entity3D entity)
    {
        this.entity = entity;
    }

    @Override
    public void update(float deltaTime)
    {
        if (!entity.transformComponent.transformed)
            return;

        Vector3 tPosition = Vector3.REUSABLE_STACK.pop();
        Vector3 tScale = Vector3.REUSABLE_STACK.pop();
        Vector3 tRotation = Vector3.REUSABLE_STACK.pop();

        tPosition.set(entity.position);
        tScale.set(entity.scale);
        tRotation.set(entity.rotation);

        Entity3D parent = entity.parent;

        while (parent != null)
        {
            tRotation.add(parent.rotation);
            tScale.scale(parent.scale.x, parent.scale.y, parent.scale.z);
            tPosition.rotate(parent.rotation).add(parent.position);

            parent = parent.parent;
        }

        polyhedron.setPosition(tPosition);
        polyhedron.setScale(tScale);
        polyhedron.setRotation(tRotation);

        Vector3.REUSABLE_STACK.push(tPosition);
        Vector3.REUSABLE_STACK.push(tScale);
        Vector3.REUSABLE_STACK.push(tRotation);
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
        void handleCollision(Entity3D other, CollisionComponent3D component);
    }
}
