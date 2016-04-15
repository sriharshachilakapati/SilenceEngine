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

import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.opengl.GLContext;
import com.shc.silenceengine.math.Matrix4;
import com.shc.silenceengine.math.Transforms;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.Vector3;

import static com.shc.silenceengine.graphics.IGraphicsDevice.Constants.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class OrthoCam extends BaseCamera
{
    private Matrix4 mProj;
    private Matrix4 mView;

    private float width;
    private float height;

    public OrthoCam()
    {
        this(SilenceEngine.display.getWidth(), SilenceEngine.display.getHeight());
    }

    public OrthoCam(float width, float height)
    {
        this(0, width, height, 0);
    }

    public OrthoCam(float left, float right, float bottom, float top)
    {
        width = right - left;
        height = bottom - top;
        mProj = Transforms.createOrtho2d(left, right, bottom, top, 0, 100, new Matrix4());
        mView = new Matrix4().initIdentity();
    }

    public OrthoCam translate(Vector2 v)
    {
        Matrix4 tempMatrix = Matrix4.REUSABLE_STACK.pop();
        Vector3 tempVector = Vector3.REUSABLE_STACK.pop();

        mView.multiply(Transforms.createTranslation(tempVector.set(v.x, v.y, 0), tempMatrix));

        Matrix4.REUSABLE_STACK.push(tempMatrix);
        Vector3.REUSABLE_STACK.push(tempVector);
        return this;
    }

    public OrthoCam translateTo(float x, float y)
    {
        Matrix4 tempMatrix = Matrix4.REUSABLE_STACK.pop();
        Vector3 tempVector = Vector3.REUSABLE_STACK.pop();

        mView.initIdentity().multiply(Transforms.createTranslation(tempVector.set(x, y, 0), tempMatrix));

        Matrix4.REUSABLE_STACK.push(tempMatrix);
        Vector3.REUSABLE_STACK.push(tempVector);
        return this;
    }

    public OrthoCam translateTo(Vector2 v)
    {
        Matrix4 tempMatrix = Matrix4.REUSABLE_STACK.pop();
        Vector3 tempVector = Vector3.REUSABLE_STACK.pop();

        mView.initIdentity().multiply(Transforms.createTranslation(tempVector.set(v.x, v.y, 0), tempMatrix));

        Matrix4.REUSABLE_STACK.push(tempMatrix);
        Vector3.REUSABLE_STACK.push(tempVector);
        return this;
    }

    public OrthoCam center(float x, float y)
    {
        return center(new Vector2(x, y));
    }

    public OrthoCam center(Vector2 v)
    {
        mView.initIdentity();
        float x = (width / 2) - v.x;
        float y = (height / 2) - v.y;

        return translate(x, y);
    }

    public OrthoCam translate(float x, float y)
    {
        Matrix4 tempMatrix = Matrix4.REUSABLE_STACK.pop();
        Vector3 tempVector = Vector3.REUSABLE_STACK.pop();

        mView.multiply(Transforms.createTranslation(tempVector.set(x, y, 0), tempMatrix));

        Matrix4.REUSABLE_STACK.push(tempMatrix);
        Vector3.REUSABLE_STACK.push(tempVector);
        return this;
    }

    public OrthoCam rotate(Vector3 axis, float angle)
    {
        Matrix4 tempMatrix = Matrix4.REUSABLE_STACK.pop();

        mView.multiply(Transforms.createRotation(axis, angle, tempMatrix));

        Matrix4.REUSABLE_STACK.push(tempMatrix);
        return this;
    }

    public OrthoCam initProjection(float width, float height)
    {
        return initProjection(0, width, height, 0);
    }

    public OrthoCam initProjection(float left, float right, float bottom, float top)
    {
        width = right - left;
        height = bottom - top;
        Transforms.createOrtho2d(left, right, bottom, top, 0, 100, mProj);
        return this;
    }

    public void apply()
    {
        super.apply();

        // Disable depth testing
        GLContext.disable(GL_DEPTH_TEST);
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
}
