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

package com.shc.silenceengine.scene.tiled.layers;

import com.shc.silenceengine.scene.tiled.TmxMap;
import com.shc.silenceengine.scene.tiled.TmxProperties;

/**
 * @author Sri Harsha Chilakapati
 */
public class TmxMapLayer
{
    protected static int nextParseOrder = 0;

    protected TmxMap map;
    protected String name;

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected int zOrder;
    protected int parseOrder;

    protected float        opacity;
    protected boolean      visible;
    protected TmxLayerType type;

    protected TmxProperties properties;

    public TmxMapLayer(TmxMap map, String name,
                       int x, int y, int width, int height,
                       float opacity, boolean visible, TmxLayerType type)
    {
        this.map = map;
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.opacity = opacity;
        this.visible = visible;
        this.type = type;

        properties = new TmxProperties();

        ++nextParseOrder;
    }

    public TmxMap getMap()
    {
        return map;
    }

    public String getName()
    {
        return name;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public int getZOrder()
    {
        return zOrder;
    }

    public void setZOrder(int zOrder)
    {
        this.zOrder = zOrder;
    }

    public int getParseOrder()
    {
        return parseOrder;
    }

    public float getOpacity()
    {
        return opacity;
    }

    public boolean isVisible()
    {
        return visible;
    }

    public TmxLayerType getType()
    {
        return type;
    }

    public TmxProperties getProperties()
    {
        return properties;
    }

    public enum TmxLayerType
    {
        TILE, OBJECT, IMAGE
    }
}
