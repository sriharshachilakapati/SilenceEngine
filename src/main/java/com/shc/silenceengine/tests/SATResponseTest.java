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

package com.shc.silenceengine.tests;

import com.shc.silenceengine.collision.Collision2D;
import com.shc.silenceengine.core.Display;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.cameras.OrthoCam;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.input.Mouse;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.geom2d.Circle;
import com.shc.silenceengine.math.geom2d.Rectangle;
import com.shc.silenceengine.utils.RenderUtils;

/**
 * @author Sri Harsha Chilakapati
 */
public class SATResponseTest extends Game
{
    private Circle    circle;
    private Rectangle rectangle;

    private OrthoCam cam;

    public static void main(String[] args)
    {
        new SATResponseTest().start();
    }

    public void init()
    {
        cam = new OrthoCam().initProjection(Display.getWidth(), Display.getHeight());

        circle = new Circle(200, 200, 100);
        rectangle = new Rectangle(10, 10, 100, 100);
    }

    public void update(float delta)
    {
        if (Keyboard.isClicked(Keyboard.KEY_ESCAPE))
            end();

        circle.setCenter(new Vector2(Mouse.getX(), Mouse.getY()));

        rectangle.rotate(90 * delta);

        if (circle.intersects(rectangle))
        {
            Collision2D.Response response = Collision2D.getResponse();

            System.out.println(response.getOverlapDistance());
            System.out.println(response.getOverlapAxis());
            System.out.println(response.getMinimumTranslationVector());
        }
    }

    public void render(float delta, Batcher batcher)
    {
        cam.apply();

        RenderUtils.tracePolygon(batcher, circle, Color.GREEN);
        RenderUtils.tracePolygon(batcher, rectangle, Color.RED);
    }

    public void resize()
    {
        cam.initProjection(Display.getWidth(), Display.getHeight());
    }
}
