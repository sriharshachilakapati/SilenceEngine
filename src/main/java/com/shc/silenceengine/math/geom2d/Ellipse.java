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

package com.shc.silenceengine.math.geom2d;

import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.utils.MathUtils;

/**
 * @author Sri Harsha Chilakapati
 */
public class Ellipse extends Polygon
{
    public Ellipse(Vector2 position, float rx, float ry)
    {
        setPosition(position);

        updateVertices(rx, ry);
    }

    private void updateVertices(float rx, float ry)
    {
        clearVertices();

        float x = getPosition().x;
        float y = getPosition().y;

        for (int i = 0; i < 360; i++)
        {
            addVertex(new Vector2(x + MathUtils.cos(i) * rx,
                                         y + MathUtils.sin(i) * ry));
        }
    }

    public float getRadiusX()
    {
        return getBounds().getWidth() / 2;
    }

    public void setRadiusX(float rx)
    {
        float rotation = getRotation();
        updateVertices(rx, getRadiusY());
        setRotation(rotation);
    }

    public float getRadiusY()
    {
        return getBounds().getHeight() / 2;
    }

    public void setRadiusY(float ry)
    {
        float rotation = getRotation();
        updateVertices(getRadiusX(), ry);
        setRotation(rotation);
    }
}
