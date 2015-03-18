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
        return copy().translateSelf(v);
    }

    public Transform translateSelf(Vector2 v)
    {
        return translateSelf(new Vector3(v, 0));
    }

    public Transform translateSelf(Vector3 v)
    {
        tMatrix.multiplySelf(TransformUtils.createTranslation(v));
        return this;
    }

    public Transform copy()
    {
        return new Transform().applySelf(tMatrix);
    }

    public Transform applySelf(Matrix4 matrix)
    {
        tMatrix.multiplySelf(matrix);
        return this;
    }

    public Transform translate(Vector3 v)
    {
        return copy().translateSelf(v);
    }

    public Transform rotate(Vector3 axis, float angle)
    {
        return copy().rotateSelf(axis, angle);
    }

    public Transform rotateSelf(Vector3 axis, float angle)
    {
        tMatrix.multiplySelf(TransformUtils.createRotation(axis, angle));
        return this;
    }

    public Transform scale(Vector2 scale)
    {
        return copy().scaleSelf(scale);
    }

    public Transform scaleSelf(Vector2 scale)
    {
        Vector3 temp = Vector3.REUSABLE_STACK.pop();
        scaleSelf(temp.set(scale.x, scale.y, 0));
        Vector3.REUSABLE_STACK.push(temp);

        return this;
    }

    public Transform scaleSelf(Vector3 scale)
    {
        tMatrix.multiplySelf(TransformUtils.createScaling(scale));
        return this;
    }

    public Transform scale(Vector3 scale)
    {
        return copy().scaleSelf(scale);
    }

    public Transform apply(Transform transform)
    {
        return copy().applySelf(transform);
    }

    public Transform applySelf(Transform transform)
    {
        return applySelf(transform.getMatrix());
    }

    public Matrix4 getMatrix()
    {
        return tMatrix;
    }

    public Transform apply(Matrix4 matrix)
    {
        return copy().applySelf(matrix);
    }

    public Transform applyInverseSelf(Matrix4 matrix)
    {
        Matrix4 temp = Matrix4.REUSABLE_STACK.pop();

        temp.set(matrix).invertSelf();
        applySelf(temp);
        Matrix4.REUSABLE_STACK.push(temp);

        return this;
    }

    public Transform applyInverse(Matrix4 matrix)
    {
        return copy().applyInverseSelf(matrix);
    }

    public Transform apply(Quaternion q)
    {
        return copy().applySelf(q);
    }

    public Transform applySelf(Quaternion q)
    {
        return applySelf(TransformUtils.createRotation(q));
    }

    public Transform applyInverse(Quaternion q)
    {
        return copy().applyInverseSelf(q);
    }

    public Transform applyInverseSelf(Quaternion q)
    {
        return applyInverseSelf(TransformUtils.createRotation(q));
    }

    public Transform set(Transform t)
    {
        return reset().applySelf(t);
    }

    public Transform invertSelf()
    {
        tMatrix.invertSelf();
        return this;
    }

    public Transform invert()
    {
        return copy().invertSelf();
    }

    public Transform reset()
    {
        tMatrix.initIdentity();
        return this;
    }
}
