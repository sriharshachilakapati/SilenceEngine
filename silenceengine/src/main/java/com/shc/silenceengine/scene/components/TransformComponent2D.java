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

import com.shc.silenceengine.math.Transform;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.scene.entity.Entity2D;

/**
 * @author Sri Harsha Chilakapati
 */
public class TransformComponent2D extends Component2D
{
    public Transform transform;
    public boolean   transformed;

    private float   oldRotation;
    private Vector2 oldScale;
    private Vector2 oldPosition;

    public TransformComponent2D()
    {
        transform = new Transform();
        oldScale = new Vector2();
        oldPosition = new Vector2();
    }

    @Override
    public void update(float deltaTime)
    {
        transformed = false;

        if (!(oldRotation != entity.rotation ||
              !oldPosition.equals(entity.position) ||
              !oldScale.equals(entity.scale)))
        {
            if (entity.parent == null)
                return;

            if (!entity.parent.transformComponent.transformed)
                return;
        }

        oldRotation = entity.rotation;
        oldPosition.set(entity.position);
        oldScale.set(entity.scale);

        transform.reset();

        Entity2D entity = this.entity;

        while (entity != null)
        {
            transform.scale(entity.scale);
            transform.rotate(Vector3.AXIS_Z, entity.rotation);
            transform.translate(entity.position);

            entity = entity.parent;

            if (entity != null && !entity.transformComponent.transformed)
            {
                transform.apply(entity.transformComponent.transform);
                break;
            }
        }

        transformed = true;
    }
}
