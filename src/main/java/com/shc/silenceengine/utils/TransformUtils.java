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

package com.shc.silenceengine.utils;

import com.shc.silenceengine.math.Matrix4;
import com.shc.silenceengine.math.Quaternion;
import com.shc.silenceengine.math.Vector3;

/**
 * <p> This class is the core of the SilenceEngine's GraphicsEngine, and does the job of creating transformation
 * matrices and transformation quaternions. All the other functions call-back to this class. There are two types of
 * functions in this class. </p>
 *
 * <p> The first type of functions accept a parameter called as <code>dest</code>, which is used to store the result in,
 * and the same result is returned by the function. If you passed <code>null</code> as the <code>dest</code> parameter
 * then a new instance of the the matrix or quaternion is created. </p>
 *
 * <p> The second type of functions doesn't accept any extra parameters, and just call the first above mentioned methods
 * with a temporary matrix or quaternion. Keep in mind, though, that the life-time of the returned result is only until
 * you called any function of this class again. The work-around, is to use the <code>REUSABLE_STACK</code> of that
 * required class to generate a temporary instance. </p>
 *
 * <pre>
 *     Matrix4 temp1 = Matrix4.REUSABLE_STACK.pop();
 *     Matrix4 temp2 = Matrix4.REUSABLE_STACK.pop();
 *
 *     Matrix4 scalingMatrix     = TransformUtils.createScaling(scale, temp1);
 *     Matrix4 translationMatrix = TransformUtils.createTranslation(translation, temp2);
 *
 *     // Use the temporary matrices here until they are pushed to the stack again.
 *
 *     Matrix4.REUSABLE_STACK.push(temp1);
 *     Matrix4.REUSABLE_STACK.push(temp2);
 * </pre>
 *
 * <p> It is always encouraged to use the above work-around as you can get full control on the result matrices and the
 * quaternions. Also, the first type of functions can be soon removed from the engine completely. </p>
 *
 * @author Sri Harsha Chilakapati
 */
public final class TransformUtils
{
    // The temporary matrix, vector and quaternion. Will soon be deprecated.
    private static Matrix4    tempMat  = new Matrix4();
    private static Vector3    tempVec  = new Vector3();
    private static Quaternion tempQuat = new Quaternion();

    /**
     * Prevent instantiation, this is just a collection of utility functions.
     */
    private TransformUtils()
    {
    }

    /**
     * Constructs a transformation matrix that translates the world by a vector translation.
     *
     * @param translation The amount to translate on all the axes.
     *
     * @return The transformation that does translation.
     */
    public static Matrix4 createTranslation(Vector3 translation)
    {
        return createTranslation(translation, tempMat);
    }

    public static Matrix4 createTranslation(Vector3 translation, Matrix4 dest)
    {
        if (dest == null)
            dest = new Matrix4();

        Matrix4 result = dest.initIdentity();

        result.set(3, 0, translation.getX())
                .set(3, 1, translation.getY())
                .set(3, 2, translation.getZ());

        return result;
    }

    public static Matrix4 createScaling(Vector3 scale)
    {
        return createScaling(scale, tempMat);
    }

    public static Matrix4 createScaling(Vector3 scale, Matrix4 dest)
    {
        if (dest == null)
            dest = new Matrix4();

        Matrix4 result = dest.initIdentity();

        result.set(0, 0, scale.getX())
                .set(1, 1, scale.getY())
                .set(2, 2, scale.getZ());

        return result;
    }

    public static Matrix4 createRotation(Vector3 axis, float angle)
    {
        return createRotation(axis, angle, tempMat);
    }

    public static Matrix4 createRotation(Vector3 axis, float angle, Matrix4 dest)
    {
        assert axis != Vector3.ZERO;

        if (dest == null)
            dest = new Matrix4();

        Matrix4 result = dest.initIdentity();

        float c = (float) Math.cos(Math.toRadians(angle));
        float s = (float) Math.sin(Math.toRadians(angle));

        Vector3 v = tempVec.set(axis).normalizeSelf();

        result.set(0, 0, v.getX() * v.getX() * (1 - c) + c)
                .set(1, 0, v.getX() * v.getY() * (1 - c) - v.getZ() * s)
                .set(2, 0, v.getX() * v.getZ() * (1 - c) + v.getY() * s);

        result.set(0, 1, v.getY() * v.getX() * (1 - c) + v.getZ() * s)
                .set(1, 1, v.getY() * v.getY() * (1 - c) + c)
                .set(2, 1, v.getY() * v.getZ() * (1 - c) - v.getX() * s);

        result.set(0, 2, v.getX() * v.getZ() * (1 - c) - v.getY() * s)
                .set(1, 2, v.getY() * v.getZ() * (1 - c) + v.getX() * s)
                .set(2, 2, v.getZ() * v.getZ() * (1 - c) + c);

        return result;
    }

    public static Matrix4 createOrtho2d(float left, float right, float bottom, float top, float zNear, float zFar)
    {
        return createOrtho2d(left, right, bottom, top, zNear, zFar, tempMat);
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

    public static Matrix4 createFrustum(float left, float right, float bottom, float top, float zNear, float zFar)
    {
        return createFrustum(left, right, bottom, top, zNear, zFar, tempMat);
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

    public static Matrix4 createPerspective(float fovy, float aspect, float zNear, float zFar)
    {
        return createPerspective(fovy, aspect, zNear, zFar, tempMat);
    }

    public static Matrix4 createPerspective(float fovy, float aspect, float zNear, float zFar, Matrix4 dest)
    {
        assert zFar > zNear;

        if (dest == null)
            dest = new Matrix4();

        Matrix4 result = dest.initZero();

        float tanHalfFovy = (float) Math.tan(Math.toRadians(fovy) / 2);

        result.set(0, 0, 1 / (aspect * tanHalfFovy))
                .set(1, 1, 1 / tanHalfFovy)
                .set(2, 2, (zFar + zNear) / (zNear - zFar))
                .set(2, 3, -1)
                .set(3, 2, (-2 * zFar * zNear) / (zFar - zNear));

        return result;
    }

    public static Matrix4 createLookAtMatrix(Vector3 eye, Vector3 center, Vector3 up)
    {
        return createLookAtMatrix(eye, center, up, tempMat);
    }

    public static Matrix4 createLookAtMatrix(Vector3 eye, Vector3 center, Vector3 up, Matrix4 dest)
    {
        if (dest == null)
            dest = new Matrix4();

        Matrix4 result = dest.initIdentity();

        final Vector3 f = center.subtract(eye).normalizeSelf();
        final Vector3 s = f.cross(up).normalizeSelf();
        final Vector3 u = s.cross(f);

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

        return result;
    }

    public static Quaternion createLookAtQuaternion(Vector3 eye, Vector3 center, Vector3 up)
    {
        return createLookAtQuaternion(eye, center, up, tempQuat);
    }

    public static Quaternion createLookAtQuaternion(Vector3 eye, Vector3 center, Vector3 up, Quaternion dest)
    {
        Vector3 temp = Vector3.REUSABLE_STACK.pop();

        Vector3 forward = temp.set(center).subtractSelf(eye).normalizeSelf();

        Vector3 negativeZ = tempVec.set(Vector3.AXIS_Z).negateSelf();

        float dot = negativeZ.dot(forward);

        if (Math.abs(dot + 1) < 0.000001f)
            return dest.set(up.x, up.y, up.z, (float) Math.PI);

        if (Math.abs(dot - 1) < 0.000001f)
            return dest.set();

        float rotAngle = MathUtils.acos(dot);
        Vector3 rotAxis = negativeZ.crossSelf(forward).normalizeSelf();

        return dest.set(rotAxis, rotAngle);
    }

    public static Matrix4 createRotation(Quaternion q)
    {
        return createRotation(q, tempMat);
    }

    public static Matrix4 createRotation(Quaternion q, Matrix4 dest)
    {
        if (dest == null)
            dest = new Matrix4();

        q.normalizeSelf();

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
                .set(0, 1, 2.0f * (xy - wz))
                .set(0, 2, 2.0f * (xz + wy));

        result.set(1, 0, 2.0f * (xy + wz))
                .set(1, 1, 1.0f - 2.0f * (x2 + z2))
                .set(1, 2, 2.0f * (yz - wx));

        result.set(2, 0, 2.0f * (xz - wy))
                .set(2, 1, 2.0f * (yz + wx))
                .set(2, 2, 1.0f - 2.0f * (x2 + y2));

        return result;
    }
}
