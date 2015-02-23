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
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.utils.TransformUtils;
import org.lwjgl.opengl.GL11;

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
        this(Display.getWidth(), Display.getHeight());
    }

    public OrthoCam(float width, float height)
    {
        this(0, width, height, 0);
    }

    public OrthoCam(float left, float right, float bottom, float top)
    {
        width = right - left;
        height = bottom - top;
        mProj = TransformUtils.createOrtho2d(left, right, bottom, top, 0, 100).copy();
        mView = new Matrix4().initIdentity();
    }

    public OrthoCam translate(Vector2 v)
    {
        mView.multiplySelf(TransformUtils.createTranslation(new Vector3(v, 0)));
        return this;
    }

    public OrthoCam translateTo(float x, float y)
    {
        mView.initIdentity().multiplySelf(TransformUtils.createTranslation(new Vector3(x, y, 0)));
        return this;
    }

    public OrthoCam translateTo(Vector2 v)
    {
        mView.initIdentity().multiplySelf(TransformUtils.createTranslation(new Vector3(v, 0)));
        return this;
    }

    public OrthoCam center(float x, float y)
    {
        return center(new Vector2(x, y));
    }

    public OrthoCam center(Vector2 v)
    {
        mView.initIdentity();
        float x = (width / 2) - v.getX();
        float y = (height / 2) - v.getY();

        return translate(x, y);
    }

    public OrthoCam translate(float x, float y)
    {
        mView.multiplySelf(TransformUtils.createTranslation(new Vector3(x, y, 0)));
        return this;
    }

    public OrthoCam rotate(Vector3 axis, float angle)
    {
        mView.multiplySelf(TransformUtils.createRotation(axis, angle));
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
        mProj = TransformUtils.createOrtho2d(left, right, bottom, top, 0, 100).copy();
        return this;
    }

    public void apply()
    {
        super.apply();

        // Disable depth testing
        GL3Context.disable(GL11.GL_DEPTH_TEST);
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
