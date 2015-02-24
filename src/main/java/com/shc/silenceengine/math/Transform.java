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

package com.shc.silenceengine.math;

import com.shc.silenceengine.utils.ReusableStack;
import com.shc.silenceengine.utils.TransformUtils;

/**
 * @author Sri Harsha Chilakapati
 */
public class Transform
{
    public static final ReusableStack<Transform> REUSABLE_STACK = new ReusableStack<>(Transform.class);

    // The transformation matrix
    private Matrix4 tMatrix;

    public Transform()
    {
        tMatrix = new Matrix4();
    }

    public Transform translate(Vector2 v)
    {
        return translate(new Vector3(v, 0));
    }

    public Transform translate(Vector3 v)
    {
        tMatrix.multiplySelf(TransformUtils.createTranslation(v));
        return this;
    }

    public Transform rotate(Vector3 axis, float angle)
    {
        tMatrix.multiplySelf(TransformUtils.createRotation(axis, angle));
        return this;
    }

    public Transform scale(Vector2 scale)
    {
        return scale(new Vector3(scale, 0));
    }

    public Transform scale(Vector3 scale)
    {
        tMatrix.multiplySelf(TransformUtils.createScaling(scale));
        return this;
    }

    public Transform apply(Transform transform)
    {
        return apply(transform.getMatrix());
    }

    public Transform apply(Matrix4 matrix)
    {
        tMatrix.multiplySelf(matrix);
        return this;
    }

    public Matrix4 getMatrix()
    {
        return tMatrix;
    }

    public Transform apply(Quaternion q)
    {
        return apply(TransformUtils.createRotation(q));
    }

    public Transform copy()
    {
        return new Transform().apply(tMatrix);
    }

    public Transform set(Transform t)
    {
        return reset().apply(t);
    }

    public Transform reset()
    {
        tMatrix.initIdentity();
        return this;
    }
}
