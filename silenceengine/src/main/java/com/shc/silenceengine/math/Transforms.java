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

import com.shc.silenceengine.utils.MathUtils;

/**
 * <p>This class is the core of the SilenceEngine's GraphicsEngine, and does the job of creating transformation matrices
 * and transformation quaternions. All the other functions call-back to this class.</p>
 *
 * <pre>
 *     Matrix4 temp1 = Matrix4.REUSABLE_STACK.pop();
 *     Matrix4 temp2 = Matrix4.REUSABLE_STACK.pop();
 *
 *     Matrix4 scalingMatrix     = Transforms.createScaling(scale, temp1);
 *     Matrix4 translationMatrix = Transforms.createTranslation(translation, temp2);
 *
 *     // Use the temporary matrices here until they are pushed to the stack again.
 *
 *     Matrix4.REUSABLE_STACK.push(temp1);
 *     Matrix4.REUSABLE_STACK.push(temp2);
 * </pre>
 *
 * @author Sri Harsha Chilakapati
 */
public final class Transforms
{
    /**
     * Prevent instantiation, this is just a collection of utility functions.
     */
    private Transforms()
    {
    }

    public static Matrix4 createTranslation(Vector3 translation, Matrix4 dest)
    {
        if (dest == null)
            dest = new Matrix4();

        Matrix4 result = dest.initIdentity();

        result.set(3, 0, translation.x)
                .set(3, 1, translation.y)
                .set(3, 2, translation.z);

        return result;
    }

    public static Matrix4 createScaling(Vector3 scale, Matrix4 dest)
    {
        if (dest == null)
            dest = new Matrix4();

        Matrix4 result = dest.initIdentity();

        result.set(0, 0, scale.x)
                .set(1, 1, scale.y)
                .set(2, 2, scale.z);

        return result;
    }

    public static Matrix4 createRotation(Vector3 axis, float angle, Matrix4 dest)
    {
        if (dest == null)
            dest = new Matrix4();

        Matrix4 result = dest.initIdentity();

        float c = MathUtils.cos(angle);
        float s = MathUtils.sin(angle);

        Vector3 nAxis = Vector3.REUSABLE_STACK.pop().set(axis).normalize();
        Vector3 tempV = Vector3.REUSABLE_STACK.pop().set(nAxis).scale(1f - c);

        result.set(0, 0, c + tempV.x * nAxis.x)
                .set(0, 1, tempV.x * nAxis.y + s * nAxis.z)
                .set(0, 2, tempV.x * nAxis.z - s * nAxis.y);

        result.set(1, 0, tempV.y * nAxis.x - s * nAxis.z)
                .set(1, 1, c + tempV.y * nAxis.y)
                .set(1, 2, tempV.y * nAxis.z + s * nAxis.x);

        result.set(2, 0, tempV.z * nAxis.x + s * nAxis.y)
                .set(2, 1, tempV.z * nAxis.y - s * nAxis.x)
                .set(2, 2, c + tempV.z * nAxis.z);

        Vector3.REUSABLE_STACK.push(nAxis);
        Vector3.REUSABLE_STACK.push(tempV);

        return result;
    }

    public static Matrix4 createOrtho2d(float left, float right, float bottom, float top, float zNear, float zFar, Matrix4 dest)
    {
        if (dest == null)
            dest = new Matrix4();

        Matrix4 result = dest.initZero();

        result.set(0, 0, 2 / (right - left))
                .set(1, 1, 2 / (top - bottom))
                .set(2, 2, -2 / (zFar - zNear))
                .set(3, 0, -(right + left) / (right - left))
                .set(3, 1, -(top + bottom) / (top - bottom))
                .set(3, 2, -(zFar + zNear) / (zFar - zNear))
                .set(3, 3, 1);

        return result;
    }

    public static Matrix4 createFrustum(float left, float right, float bottom, float top, float zNear, float zFar, Matrix4 dest)
    {
        assert zFar > zNear;

        if (dest == null)
            dest = new Matrix4();

        Matrix4 result = dest.initZero();

        result.set(0, 0, (2 * zNear) / (right - left))
                .set(1, 1, (2 * zNear) / (top - bottom))
                .set(2, 0, (right + left) / (right - left))
                .set(2, 1, (top + bottom) / (top - bottom))
                .set(2, 2, (zFar + zNear) / (zNear - zFar))
                .set(2, 3, -1)
                .set(3, 2, (-2 * zFar * zNear) / (zFar - zNear));

        return result;
    }

    public static Matrix4 createPerspective(float fovy, float aspect, float zNear, float zFar, Matrix4 dest)
    {
        if (dest == null)
            dest = new Matrix4();

        Matrix4 result = dest.initZero();

        float tanHalfFovy = MathUtils.tan(fovy / 2);

        result.set(0, 0, 1 / (aspect * tanHalfFovy))
                .set(1, 1, 1 / tanHalfFovy)
                .set(2, 2, (zFar + zNear) / (zNear - zFar))
                .set(2, 3, -1)
                .set(3, 2, (-2 * zFar * zNear) / (zFar - zNear));

        return result;
    }

    public static Matrix4 createLookAtMatrix(Vector3 eye, Vector3 center, Vector3 up, Matrix4 dest)
    {
        if (dest == null)
            dest = new Matrix4();

        Matrix4 result = dest.initIdentity();

        Vector3 f = Vector3.REUSABLE_STACK.pop();
        Vector3 s = Vector3.REUSABLE_STACK.pop();
        Vector3 u = Vector3.REUSABLE_STACK.pop();

        f.set(center).subtract(eye).normalize();
        s.set(f).cross(up).normalize();
        u.set(s).cross(f);

        result.set(0, 0, s.x)
                .set(1, 0, s.y)
                .set(2, 0, s.z);

        result.set(0, 1, u.x)
                .set(1, 1, u.y)
                .set(2, 1, u.z);

        result.set(0, 2, -f.x)
                .set(1, 2, -f.y)
                .set(2, 2, -f.z);

        result.set(3, 0, -s.dot(eye))
                .set(3, 1, -u.dot(eye))
                .set(3, 2, f.dot(eye));

        Vector3.REUSABLE_STACK.push(f);
        Vector3.REUSABLE_STACK.push(s);
        Vector3.REUSABLE_STACK.push(u);

        return result;
    }

    public static Quaternion createLookAtQuaternion(Vector3 eye, Vector3 center, Vector3 up, Quaternion dest)
    {
        if (dest == null)
            dest = new Quaternion();

        Vector3 temp1 = Vector3.REUSABLE_STACK.pop();
        Vector3 temp2 = Vector3.REUSABLE_STACK.pop();

        Vector3 forward = temp1.set(center).subtract(eye).normalize();
        Vector3 negativeZ = temp2.set(Vector3.AXIS_Z).negate();

        float dot = negativeZ.dot(forward);

        if (Math.abs(dot + 1) < 0.000001f)
        {
            Vector3.REUSABLE_STACK.push(temp1);
            Vector3.REUSABLE_STACK.push(temp2);

            return dest.set(up.x, up.y, up.z, (float) Math.PI);
        }

        if (Math.abs(dot - 1) < 0.000001f)
        {
            Vector3.REUSABLE_STACK.push(temp1);
            Vector3.REUSABLE_STACK.push(temp2);

            return dest.set();
        }

        float rotAngle = MathUtils.acos(dot);
        Vector3 rotAxis = negativeZ.cross(forward).normalize();

        dest.set(rotAxis, rotAngle);

        Vector3.REUSABLE_STACK.push(temp1);
        Vector3.REUSABLE_STACK.push(temp2);

        return dest;
    }

    public static Matrix4 createRotation(Quaternion q, Matrix4 dest)
    {
        if (dest == null)
            dest = new Matrix4();

        q.normalize();

        Matrix4 result = dest.initIdentity();

        float x2 = q.x * q.x;
        float y2 = q.y * q.y;
        float z2 = q.z * q.z;
        float xy = q.x * q.y;
        float xz = q.x * q.z;
        float yz = q.y * q.z;
        float wx = q.w * q.x;
        float wy = q.w * q.y;
        float wz = q.w * q.z;

        result.set(0, 0, 1.0f - 2.0f * (y2 + z2))
                .set(0, 1, 2.0f * (xy + wz))
                .set(0, 2, 2.0f * (xz - wy));

        result.set(1, 0, 2.0f * (xy - wz))
                .set(1, 1, 1.0f - 2.0f * (x2 + z2))
                .set(1, 2, 2.0f * (yz + wx));

        result.set(2, 0, 2.0f * (xz + wy))
                .set(2, 1, 2.0f * (yz - wx))
                .set(2, 2, 1.0f - 2.0f * (x2 + y2));

        return result;
    }
}
