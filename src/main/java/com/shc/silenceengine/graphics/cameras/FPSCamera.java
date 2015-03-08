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

package com.shc.silenceengine.graphics.cameras;

import com.shc.silenceengine.core.Display;
import com.shc.silenceengine.graphics.opengl.GL3Context;
import com.shc.silenceengine.math.Matrix4;
import com.shc.silenceengine.math.Quaternion;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.utils.TransformUtils;
import org.lwjgl.opengl.GL11;

public class FPSCamera extends BaseCamera
{
    private Matrix4 mProj;
    private Matrix4 mView;

    private Vector3    position;
    private Quaternion rotation;

    public FPSCamera()
    {
        this(70, Display.getAspectRatio(), 0.01f, 100);
    }

    public FPSCamera(float fovy, float aspect, float zNear, float zFar)
    {
        mProj = TransformUtils.createPerspective(fovy, aspect, zNear, zFar).copy();
        mView = new Matrix4();

        position = new Vector3(0, 0, 1);
        rotation = new Quaternion();
    }

    public FPSCamera moveForward(float amount)
    {
        return move(getForward(), amount);
    }

    public FPSCamera move(Vector3 dir, float amount)
    {
        Vector3 deltaMove = position.add(dir.normalizeSelf().scaleSelf(amount));

        // Restrict y-component
        deltaMove.y = 0;

        position = position.add(deltaMove);

        return this;
    }

    public Vector3 getForward()
    {
        return rotation.multiply(Vector3.AXIS_Z.negate());
    }

    public FPSCamera moveBackward(float amount)
    {
        return move(getForward().negate(), amount);
    }

    public FPSCamera moveLeft(float amount)
    {
        return move(getRight().negate(), amount);
    }

    public Vector3 getRight()
    {
        return rotation.multiply(Vector3.AXIS_X);
    }

    public FPSCamera moveRight(float amount)
    {
        return move(getRight(), amount);
    }

    public FPSCamera moveUp(float amount)
    {
        return move(getUp(), amount);
    }

    public Vector3 getUp()
    {
        return rotation.multiply(Vector3.AXIS_Y);
    }

    public FPSCamera moveDown(float amount)
    {
        return move(getUp().negate(), amount);
    }

    public FPSCamera rotateX(float angle)
    {
        Quaternion xRot = new Quaternion(Vector3.AXIS_X, angle);
        rotation = rotation.multiply(xRot);

        return this;
    }

    public FPSCamera rotateY(float angle)
    {
        Quaternion yRot = new Quaternion(Vector3.AXIS_Y, angle);
        rotation = yRot.multiply(rotation);

        return this;
    }

    public FPSCamera initProjection(float fovy, float aspect, float zNear, float zFar)
    {
        mProj = TransformUtils.createPerspective(fovy, aspect, zNear, zFar).copy();
        return this;
    }

    public FPSCamera initProjection(float width, float height)
    {
        return initProjection(0, width, height, 0, 0.01f, 100f);
    }

    public FPSCamera initProjection(float left, float right, float bottom, float top, float zNear, float zFar)
    {
        mProj = TransformUtils.createFrustum(left, right, bottom, top, zNear, zFar).copy();
        return this;
    }

    public void apply()
    {
        super.apply();

        Vector3 temp = Vector3.REUSABLE_STACK.pop();

        mView.initIdentity()
                .multiplySelf(TransformUtils.createTranslation(temp.set(position).negateSelf()))
                .multiplySelf(TransformUtils.createRotation(rotation));

        Vector3.REUSABLE_STACK.push(temp);

        // Enable Depth Testing
        GL3Context.enable(GL11.GL_DEPTH_TEST);
    }

    @Override
    public Matrix4 getProjection()
    {
        return mProj;
    }

    @Override
    public Matrix4 getView()
    {
        return mView;
    }

    public Vector3 getPosition()
    {
        return position;
    }

    public void setPosition(Vector3 position)
    {
        this.position.set(position);
    }
}
