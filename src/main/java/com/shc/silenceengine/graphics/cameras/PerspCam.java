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
import com.shc.silenceengine.backend.lwjgl3.opengl.GL3Context;
import com.shc.silenceengine.math.Matrix4;
import com.shc.silenceengine.math.Quaternion;
import com.shc.silenceengine.math.Transforms;
import com.shc.silenceengine.math.Vector3;
import org.lwjgl.opengl.GL11;

/**
 * @author Sri Harsha Chilakapati
 */
public class PerspCam extends BaseCamera
{
    private Matrix4 mProj;
    private Matrix4 mView;

    private Vector3    position;
    private Quaternion rotation;

    private Vector3 forward;
    private Vector3 right;
    private Vector3 up;

    public PerspCam()
    {
        this(70, Display.getAspectRatio(), 0.01f, 100);
    }

    public PerspCam(float fovy, float aspect, float zNear, float zFar)
    {
        mProj = Transforms.createPerspective(fovy, aspect, zNear, zFar);
        mView = new Matrix4();

        position = new Vector3(0, 0, 1);
        rotation = new Quaternion();

        forward = new Vector3(0, 0, -1);
        right = new Vector3(0, 0, 1);
        up = new Vector3(0, 1, 0);
    }

    public PerspCam lookAt(Vector3 point)
    {
        return lookAt(point, getUp().normalizeSelf());
    }

    public PerspCam lookAt(Vector3 point, Vector3 up)
    {
        Transforms.createLookAtQuaternion(position, point, up, rotation);
        return this;
    }

    public Vector3 getUp()
    {
        return rotation.multiply(up.set(Vector3.AXIS_Y), up).normalizeSelf();
    }

    public PerspCam lookAt(Vector3 position, Vector3 point, Vector3 up)
    {
        return setPosition(position).lookAt(point, up);
    }

    public PerspCam moveForward(float amount)
    {
        return move(getForward(), amount);
    }

    public PerspCam move(Vector3 dir, float amount)
    {
        position.addSelf(dir.normalizeSelf().scaleSelf(amount));
        return this;
    }

    public Vector3 getForward()
    {
        return rotation.multiply(forward.set(Vector3.AXIS_Z).negateSelf(), forward).normalizeSelf();
    }

    public PerspCam moveBackward(float amount)
    {
        return move(getForward().negateSelf(), amount);
    }

    public PerspCam moveLeft(float amount)
    {
        return move(getRight().negateSelf(), amount);
    }

    public Vector3 getRight()
    {
        return rotation.multiply(Vector3.AXIS_X, right).normalizeSelf();
    }

    public PerspCam moveRight(float amount)
    {
        return move(getRight(), amount);
    }

    public PerspCam moveUp(float amount)
    {
        return move(getUp(), amount);
    }

    public PerspCam moveDown(float amount)
    {
        return move(getUp().negateSelf(), amount);
    }

    public PerspCam rotateX(float angle)
    {
        Quaternion tempQuat = Quaternion.REUSABLE_STACK.pop();

        Quaternion xRot = tempQuat.set(Vector3.AXIS_X, angle);
        rotation.multiplySelf(xRot);

        Quaternion.REUSABLE_STACK.push(tempQuat);

        return this;
    }

    public PerspCam rotateY(float angle)
    {
        Quaternion tempQuat = Quaternion.REUSABLE_STACK.pop();

        Quaternion yRot = tempQuat.set(Vector3.AXIS_Y, angle);
        rotation.set(yRot.multiplySelf(rotation));

        Quaternion.REUSABLE_STACK.push(tempQuat);

        return this;
    }

    public PerspCam lerp(PerspCam p, float alpha)
    {
        position.lerpSelf(p.position, alpha);
        rotation.lerpSelf(p.rotation, alpha);

        return this;
    }

    public PerspCam slerp(PerspCam p, float alpha)
    {
        position.lerpSelf(p.position, alpha);
        rotation.slerpSelf(p.rotation, alpha);

        return this;
    }

    public PerspCam initProjection(float fovy, float aspect, float zNear, float zFar)
    {
        Transforms.createPerspective(fovy, aspect, zNear, zFar, mProj);
        return this;
    }

    public PerspCam initProjection(float width, float height)
    {
        return initProjection(0, width, height, 0, 0.01f, 100f);
    }

    public PerspCam initProjection(float left, float right, float bottom, float top, float zNear, float zFar)
    {
        Transforms.createFrustum(left, right, bottom, top, zNear, zFar, mProj);
        return this;
    }

    public void apply()
    {
        super.apply();

        Vector3 tempVec3 = Vector3.REUSABLE_STACK.pop();
        Matrix4 tempMat4 = Matrix4.REUSABLE_STACK.pop();

        Quaternion tempQuat = Quaternion.REUSABLE_STACK.pop();

        mView.initIdentity()
                .multiplySelf(Transforms.createRotation(tempQuat.set(rotation).invertSelf(), tempMat4))
                .multiplySelf(Transforms.createTranslation(tempVec3.set(position).negateSelf(), tempMat4));

        Vector3.REUSABLE_STACK.push(tempVec3);
        Matrix4.REUSABLE_STACK.push(tempMat4);

        Quaternion.REUSABLE_STACK.push(tempQuat);

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

    public PerspCam setPosition(Vector3 position)
    {
        this.position.set(position);
        return this;
    }

    public Quaternion getRotation()
    {
        return rotation;
    }

    public PerspCam setRotation(Quaternion rotation)
    {
        this.rotation.set(rotation);
        return this;
    }
}
