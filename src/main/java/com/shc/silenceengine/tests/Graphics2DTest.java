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

import com.shc.silenceengine.core.Display;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.Graphics2D;
import com.shc.silenceengine.graphics.Paint;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.math.geom2d.Circle;
import com.shc.silenceengine.math.geom2d.Ellipse;
import com.shc.silenceengine.math.geom2d.Rectangle;

/**
 * @author Sri Harsha Chilakapati
 */
public class Graphics2DTest extends Game
{
    private Texture texture;

    private Paint gradientPaint;
    private Paint originalPaint;

    private Rectangle rectangle;
    private Circle    circle;
    private Ellipse   ellipse;

    public static void main(String[] args)
    {
        new Graphics2DTest().start();
    }

    public void init()
    {
        Graphics2D g2d = SilenceEngine.graphics.getGraphics2D();

        texture = Texture.fromFilePath(FilePath.getResourceFile("resources/texture2.png"));

        gradientPaint = new Paint(Color.RED, Color.BLUE, Paint.Gradient.DIAGONAL_LEFT_TO_RIGHT);
        originalPaint = g2d.getPaint();

        rectangle = new Rectangle(530, 190, 100, 100);
        circle = new Circle(500, 350, 50);
        ellipse = new Ellipse(650, 400, 50, 100);

        SilenceEngine.graphics.setClearColor(Color.DARK_SLATE_GRAY);
    }

    public void update(float delta)
    {
        if (Keyboard.isClicked(Keyboard.KEY_ESCAPE))
            end();

        rectangle.rotate(90 * delta);

        Display.setTitle("FPS: " + Game.getFPS() + " | UPS: " + Game.getUPS() + " | RC: " + SilenceEngine.graphics.renderCallsPerFrame);
    }

    public void render(float delta, Batcher batcher)
    {
        Graphics2D g2d = SilenceEngine.graphics.getGraphics2D();
        g2d.setPaint(originalPaint);

        g2d.setColor(Color.RED);
        g2d.drawRect(10, 50, 100, 100);

        g2d.setColor(Color.YELLOW_GREEN);
        g2d.fillRect(0, 100, 200, 150);

        g2d.setColor(Color.BLANCHED_ALMOND);
        g2d.fillOval(250, 100, 150, 50);

        g2d.drawTexture(texture, 100, 100, 254, 252);
        g2d.drawString("Hello World", 10, 10);

        g2d.drawTexturedPolygon(texture, rectangle);
        g2d.drawTexturedPolygon(texture, circle);
        g2d.drawTexturedPolygon(texture, ellipse);

        g2d.setPaint(gradientPaint);
        g2d.fillRect(200, 200, 200, 200);
        g2d.fillCircle(500, 100, 50);
    }

    public void dispose()
    {
        texture.dispose();
    }
}
