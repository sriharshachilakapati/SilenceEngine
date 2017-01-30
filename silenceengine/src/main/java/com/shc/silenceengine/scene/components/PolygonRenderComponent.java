/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 Sri Harsha Chilakapati
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

package com.shc.silenceengine.scene.components;

import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.DynamicRenderer;
import com.shc.silenceengine.graphics.IGraphicsDevice;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.geom2d.Polygon;
import com.shc.silenceengine.scene.entity.Entity2D;

/**
 * @author Sri Harsha Chilakapati
 */
public class PolygonRenderComponent implements IComponent2D
{
    private DynamicRenderer renderer;
    private Polygon polygon;

    public PolygonRenderComponent(Polygon polygon)
    {
        this(polygon, IGraphicsDevice.Renderers.dynamic);
    }

    public PolygonRenderComponent(Polygon polygon, DynamicRenderer renderer)
    {
        this.polygon = polygon;
        this.renderer = renderer;
    }

    @Override
    public void init(Entity2D entity)
    {
    }

    @Override
    public void update(float deltaTime)
    {
    }

    @Override
    public void render(float deltaTime)
    {
        Vector2 temp = Vector2.REUSABLE_STACK.pop();

        for (Vector2 v : polygon.getVertices())
        {
            renderer.vertex(temp.set(v).add(polygon.getPosition()));
            renderer.color(Color.RED);
        }

        Vector2.REUSABLE_STACK.push(temp);
    }

    @Override
    public void dispose()
    {
    }
}
