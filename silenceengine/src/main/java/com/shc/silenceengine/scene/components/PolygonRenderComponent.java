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

/**
 * @author Sri Harsha Chilakapati
 */
public class PolygonRenderComponent extends Component2D
{
    private DynamicRenderer renderer;
    private Polygon polygon;

    public final Color color = new Color();

    public PolygonRenderComponent()
    {
        this(Color.RED);
    }

    public PolygonRenderComponent(Color color)
    {
        this(null, color);
    }

    public PolygonRenderComponent(Polygon polygon)
    {
        this(polygon, Color.RED);
    }

    public PolygonRenderComponent(Polygon polygon,  Color color)
    {
        this(polygon, color, IGraphicsDevice.Renderers.dynamic);
    }

    public PolygonRenderComponent(Polygon polygon, Color color, DynamicRenderer renderer)
    {
        this.polygon = polygon;
        this.renderer = renderer;
        this.color.set(color);
    }

    @Override
    public void init()
    {
        if (polygon == null)
            polygon = entity.getComponent(CollisionComponent2D.class).polygon;
    }

    @Override
    public void render(float deltaTime)
    {
        Vector2 temp = Vector2.REUSABLE_STACK.pop();

        for (int i = 0; i < polygon.vertexCount(); i++)
        {
            Vector2 v0 = polygon.getVertex(i);
            Vector2 v1 = polygon.getVertex((i + 1) % polygon.vertexCount());

            renderer.vertex(temp.set(v0).add(polygon.getPosition()));
            renderer.color(color);

            renderer.vertex(temp.set(v1).add(polygon.getPosition()));
            renderer.color(color);
        }

        Vector2.REUSABLE_STACK.push(temp);
    }
}
