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

import com.shc.silenceengine.math.Quaternion;
import com.shc.silenceengine.math.Transform;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.scene.Component;

/**
 * @author Sri Harsha Chilakapati
 */
public class TransformComponent extends Component
{
    private final Vector3    scale    = new Vector3(1, 1, 1);
    private final Vector3    position = new Vector3(0, 0, 0);
    private final Quaternion rotation = new Quaternion();

    private final Transform localTransform = new Transform();
    private final Transform worldTransform = new Transform();

    private TransformComponent parent = null;

    private boolean hasChanged = false;
    private boolean changed    = true;

    protected void reComputeTransforms()
    {
        hasChanged = false;

        if (changed)
        {
            localTransform.reset()
                    .scale(this.scale)
                    .rotate(this.rotation)
                    .translate(this.position);

            hasChanged = true;
            changed = false;

            if (parent == null)
                worldTransform.set(localTransform);
        }

        if (parent != null)
            worldTransform.set(localTransform)
                    .apply(parent.getWorldTransform());
    }

    @Override
    protected void onUpdate(float elapsedTime)
    {
        reComputeTransforms();
    }

    @Override
    protected void onRender(float elapsedTime)
    {
        hasChanged = false;
    }

    public Vector3 getScale()
    {
        return scale;
    }

    public TransformComponent setScale(Vector2 scale)
    {
        changed = true;
        this.scale.set(scale.x, scale.y, 1);
        return this;
    }

    public TransformComponent setScale(Vector3 scale)
    {
        changed = true;
        this.scale.set(scale);
        return this;
    }

    public TransformComponent setScale(float x, float y, float z)
    {
        changed = true;
        this.scale.set(x, y, z);
        return this;
    }

    public TransformComponent setScale(float x, float y)
    {
        changed = true;
        this.scale.set(x, y, 1);
        return this;
    }

    public TransformComponent scale(Vector3 scale)
    {
        changed = true;
        this.scale.scale(scale);
        return this;
    }

    public TransformComponent scale(Vector2 scale)
    {
        changed = true;
        this.scale.scale(scale.x, scale.y, 1);
        return this;
    }

    public TransformComponent scale(float x, float y, float z)
    {
        changed = true;
        this.scale.scale(x, y, z);
        return this;
    }

    public TransformComponent scale(float x, float y)
    {
        changed = true;
        this.scale.scale(x, y, 1);
        return this;
    }

    public Vector3 getPosition()
    {
        return position;
    }

    public TransformComponent setPosition(Vector2 position)
    {
        changed = true;
        this.position.set(position.x, position.y, 0);
        return this;
    }

    public TransformComponent setPosition(Vector3 position)
    {
        changed = true;
        this.position.set(position);
        return this;
    }

    public TransformComponent setPosition(float x, float y, float z)
    {
        changed = true;
        this.position.set(x, y, z);
        return this;
    }

    public TransformComponent setPosition(float x, float y)
    {
        changed = true;
        this.position.set(x, y, 0);
        return this;
    }

    public TransformComponent translate(Vector3 position)
    {
        changed = true;
        this.position.add(position);
        return this;
    }

    public TransformComponent translate(Vector2 position)
    {
        changed = true;
        this.position.add(position.x, position.y, 0);
        return this;
    }

    public TransformComponent translate(float x, float y, float z)
    {
        changed = true;
        this.position.add(x, y, z);
        return this;
    }

    public TransformComponent translate(float x, float y)
    {
        changed = true;
        this.position.add(x, y, 0);
        return this;
    }

    public Quaternion getRotation()
    {
        return rotation;
    }

    public TransformComponent setRotation(Vector3 rotation)
    {
        changed = true;
        this.rotation.set(rotation.x, rotation.y, rotation.z);
        return this;
    }

    public TransformComponent setRotation(Quaternion rotation)
    {
        changed = true;
        this.rotation.set(rotation);
        return this;
    }

    public TransformComponent setRotation(float r)
    {
        changed = true;
        this.rotation.set(0, 0, r);
        return this;
    }

    public TransformComponent setRotation(float x, float y, float z)
    {
        changed = true;
        this.rotation.set(x, y, z);
        return this;
    }

    public TransformComponent rotate(Vector3 rotation)
    {
        changed = true;
        this.rotation.set(rotation.x, rotation.y, rotation.z);
        return this;
    }

    public TransformComponent rotate(Quaternion rotation)
    {
        changed = true;
        this.rotation.multiply(rotation);
        return this;
    }

    public TransformComponent rotate(float r)
    {
        changed = true;
        Quaternion temp = Quaternion.REUSABLE_STACK.pop();
        this.rotation.multiply(temp.set(0, 0, r));
        Quaternion.REUSABLE_STACK.push(temp);
        return this;
    }

    public TransformComponent rotate(float x, float y, float z)
    {
        changed = true;
        Quaternion temp = Quaternion.REUSABLE_STACK.pop();
        this.rotation.multiply(temp.set(x, y, z));
        Quaternion.REUSABLE_STACK.push(temp);
        return this;
    }

    public Transform getLocalTransform()
    {
        return this.localTransform;
    }

    public TransformComponent getParent()
    {
        return parent;
    }

    public void setParent(TransformComponent parent)
    {
        this.parent = parent;
    }

    public Transform getWorldTransform()
    {
        return worldTransform;
    }

    public boolean hasChanged()
    {
        return hasChanged || (parent != null && parent.hasChanged());
    }
}
