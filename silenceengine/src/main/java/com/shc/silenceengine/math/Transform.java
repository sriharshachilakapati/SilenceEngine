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

package com.shc.silenceengine.math;

import com.shc.silenceengine.utils.ReusableStack;

/**
 * @author Sri Harsha Chilakapati
 */
public class Transform
{
    public static final ReusableStack<Transform> REUSABLE_STACK = new ReusableStack<>(Transform::new);

    // The transformation matrix
    public final Matrix4 matrix;

    private boolean changed;

    public Transform()
    {
        matrix = new Matrix4();
    }

    public Transform translate(Vector2 v)
    {
        Vector3 temp = Vector3.REUSABLE_STACK.pop();
        translate(temp.set(v.x, v.y, 0));

        Vector3.REUSABLE_STACK.push(temp);

        changed = true;
        return this;
    }

    public Transform copy()
    {
        return new Transform().apply(matrix);
    }

    public Transform translate(Vector3 v)
    {
        Matrix4 temp = Matrix4.REUSABLE_STACK.pop();
        matrix.set(Transforms.createTranslation(v, temp).multiply(matrix));
        Matrix4.REUSABLE_STACK.push(temp);
        changed = true;

        return this;
    }

    public Transform apply(Matrix4 matrix)
    {
        Matrix4 temp = Matrix4.REUSABLE_STACK.pop();
        this.matrix.set(temp.set(matrix).multiply(this.matrix));
        Matrix4.REUSABLE_STACK.push(temp);
        changed = true;
        return this;
    }

    public Transform rotate(Vector3 axis, float angle)
    {
        Matrix4 temp = Matrix4.REUSABLE_STACK.pop();
        matrix.set(Transforms.createRotation(axis, angle, temp).multiply(matrix));
        Matrix4.REUSABLE_STACK.push(temp);
        changed = true;

        return this;
    }

    public Transform rotate(float rx, float ry, float rz)
    {
        Quaternion temp = Quaternion.REUSABLE_STACK.pop();
        temp.set(rx, ry, rz);

        Matrix4 tMat = Matrix4.REUSABLE_STACK.pop();
        matrix.set(Transforms.createRotation(temp, tMat).multiply(matrix));
        Matrix4.REUSABLE_STACK.push(tMat);

        Quaternion.REUSABLE_STACK.push(temp);
        changed = true;
        return this;
    }

    public Transform scale(Vector2 scale)
    {
        Vector3 temp = Vector3.REUSABLE_STACK.pop();
        scale(temp.set(scale.x, scale.y, 0));
        Vector3.REUSABLE_STACK.push(temp);
        changed = true;

        return this;
    }

    public Transform scale(Vector3 scale)
    {
        Matrix4 temp = Matrix4.REUSABLE_STACK.pop();
        matrix.set(Transforms.createScaling(scale, temp).multiply(matrix));
        Matrix4.REUSABLE_STACK.push(temp);
        changed = true;

        return this;
    }

    public Transform apply(Transform transform)
    {
        return apply(transform.matrix);
    }

    public Transform applyInverse(Matrix4 matrix)
    {
        Matrix4 temp = Matrix4.REUSABLE_STACK.pop();

        temp.set(matrix).invert();
        apply(temp);
        Matrix4.REUSABLE_STACK.push(temp);
        changed = true;

        return this;
    }

    public Transform apply(Quaternion q)
    {
        Matrix4 temp = Matrix4.REUSABLE_STACK.pop();
        apply(Transforms.createRotation(q, temp));

        Matrix4.REUSABLE_STACK.push(temp);
        changed = true;
        return this;
    }

    public Transform applyInverse(Quaternion q)
    {
        Matrix4 temp = Matrix4.REUSABLE_STACK.pop();
        applyInverse(Transforms.createRotation(q, temp));

        Matrix4.REUSABLE_STACK.push(temp);
        changed = true;
        return this;
    }

    public Transform set(Transform t)
    {
        return reset().apply(t);
    }

    public Transform reset()
    {
        matrix.initIdentity();
        changed = true;
        return this;
    }

    public Transform invert()
    {
        matrix.invert();
        changed = true;
        return this;
    }

    public boolean hasChanged()
    {
        if (changed)
        {
            changed = false;
            return true;
        }

        return false;
    }

    @Override
    public int hashCode()
    {
        return matrix.hashCode();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transform transform = (Transform) o;

        return matrix.equals(transform.matrix);
    }
}
