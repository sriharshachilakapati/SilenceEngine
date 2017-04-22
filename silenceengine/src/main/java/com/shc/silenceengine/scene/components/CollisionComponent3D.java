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
import com.shc.silenceengine.math.Transform;
import com.shc.silenceengine.math.Transforms;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.math.geom3d.Polyhedron;
import com.shc.silenceengine.scene.Component;

/**
 * @author Sri Harsha Chilakapati
 */
public class CollisionComponent3D extends Component
{
    public CollisionCallback callback;
    public CollisionTag      tag;
    public Polyhedron        polyhedron;

    public CollisionComponent3D(CollisionTag tag, Polyhedron polyhedron)
    {
        this.tag = tag;
        this.polyhedron = polyhedron;
    }

    public CollisionComponent3D(CollisionTag tag, Polyhedron polyhedron, CollisionCallback callback)
    {
        this.tag = tag;
        this.polyhedron = polyhedron;
        this.callback = callback;
    }

    @Override
    protected void onUpdate(float elapsedTime)
    {
        if (!transformComponent.hasChanged())
            return;

        Vector3 temp = Vector3.REUSABLE_STACK.pop();

        Transform worldTransform = transformComponent.getWorldTransform();

        Transforms.getTranslation(worldTransform, temp);
        polyhedron.setPosition(temp);
        Transforms.getScaling(worldTransform, temp);
        polyhedron.setScale(temp);
        Transforms.getRotation(worldTransform, temp);
        polyhedron.setRotation(temp);

        Vector3.REUSABLE_STACK.push(temp);
    }

    @FunctionalInterface
    public interface CollisionCallback
    {
        void handleCollision(CollisionComponent3D other);
    }
}
