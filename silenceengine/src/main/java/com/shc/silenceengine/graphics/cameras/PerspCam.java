/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2016 Sri Harsha Chilakapati
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

import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.opengl.GLContext;
import com.shc.silenceengine.math.Matrix4;
import com.shc.silenceengine.math.Quaternion;
import com.shc.silenceengine.math.Transforms;
import com.shc.silenceengine.math.Vector3;

import static com.shc.silenceengine.graphics.IGraphicsDevice.Constants.GL_DEPTH_TEST;

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
        this(70, SilenceEngine.display.getAspectRatio(), 0.01f, 100);
    }

    public PerspCam(float fovy, float aspect, float zNear, float zFar)
    {
        mProj = Transforms.createPerspective(fovy, aspect, zNear, zFar, new Matrix4());
        mView = new Matrix4();

        position = new Vector3(0, 0, 1);
        rotation = new Quaternion();

        forward = new Vector3(0, 0, -1);
        right = new Vector3(0, 0, 1);
        up = new Vector3(0, 1, 0);
    }

    public PerspCam lookAt(Vector3 point)
    {
        return lookAt(point, getUp().normalize());
    }

    public PerspCam lookAt(Vector3 point, Vector3 up)
    {
        Transforms.createLookAtQuaternion(position, point, up, rotation);
        return this;
    }

    public Vector3 getUp()
    {
        return rotation.multiply(up.set(Vector3.AXIS_Y), up).normalize();
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
        position.add(dir.normalize().scale(amount));
        return this;
    }

    public Vector3 getForward()
    {
        return rotation.multiply(forward.set(Vector3.AXIS_Z).negate(), forward).normalize();
    }

    public PerspCam moveBackward(float amount)
    {
        return move(getForward().negate(), amount);
    }

    public PerspCam moveLeft(float amount)
    {
        return move(getRight().negate(), amount);
    }

    public Vector3 getRight()
    {
        return rotation.multiply(Vector3.AXIS_X, right).normalize();
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
        return move(getUp().negate(), amount);
    }

    public PerspCam rotateX(float angle)
    {
        Quaternion tempQuat = Quaternion.REUSABLE_STACK.pop();

        Quaternion xRot = tempQuat.set(Vector3.AXIS_X, angle);
        rotation.multiply(xRot);

        Quaternion.REUSABLE_STACK.push(tempQuat);

        return this;
    }

    public PerspCam rotateY(float angle)
    {
        Quaternion tempQuat = Quaternion.REUSABLE_STACK.pop();

        Quaternion yRot = tempQuat.set(Vector3.AXIS_Y, angle);
        rotation.set(yRot.multiply(rotation));

        Quaternion.REUSABLE_STACK.push(tempQuat);

        return this;
    }

    public PerspCam lerp(PerspCam p, float alpha)
    {
        position.lerp(p.position, alpha);
        rotation.lerp(p.rotation, alpha);

        return this;
    }

    public PerspCam slerp(PerspCam p, float alpha)
    {
        position.lerp(p.position, alpha);
        rotation.slerp(p.rotation, alpha);

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
                .multiply(Transforms.createRotation(tempQuat.set(rotation).invert(), tempMat4))
                .multiply(Transforms.createTranslation(tempVec3.set(position).negate(), tempMat4));

        Vector3.REUSABLE_STACK.push(tempVec3);
        Matrix4.REUSABLE_STACK.push(tempMat4);

        Quaternion.REUSABLE_STACK.push(tempQuat);

        // Enable Depth Testing
        GLContext.enable(GL_DEPTH_TEST);
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
