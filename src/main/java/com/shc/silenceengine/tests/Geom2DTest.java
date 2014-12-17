package com.shc.silenceengine.tests;

import com.shc.silenceengine.core.Display;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.geom2d.Circle;
import com.shc.silenceengine.geom2d.Rectangle;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.OrthoCam;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.utils.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class Geom2DTest extends Game
{
    private Rectangle rectangle, rectangle2;
    private Circle    circle,    circle2;

    private OrthoCam cam;

    public void init()
    {
        Display.setTitle("2D Geometry Test");

        cam = new OrthoCam(Display.getWidth(), Display.getHeight());

        // Construct the geometry
        rectangle  = new Rectangle(100, 100, 100, 100);
        rectangle2 = new Rectangle(500, 100, 100, 100);

        circle  = new Circle(100, 100, 50);
        circle2 = new Circle(500, 100, 50);
    }

    public void update(float delta)
    {
        if (Keyboard.isPressed(Keyboard.KEY_ESCAPE))
            end();

        // Some magic to position the geometry
        float yPos = (float) Math.sin(TimeUtils.currentSeconds());

        rectangle.setY(Display.getHeight()/2f + yPos * Display.getHeight() / 2f - rectangle.getHeight()/2f);
        circle.setY(Display.getHeight()/2f - yPos * Display.getHeight() / 2f);

        rectangle2.setY(circle.getY() - rectangle2.getHeight()/2f);
        circle2.setY(rectangle.getY() + rectangle2.getHeight()/2f);

        // Rotating the geometry
        rectangle.rotate(1);
        rectangle2.rotate(-1);
    }

    public void render(float delta, Batcher batcher)
    {
        cam.apply();

        boolean collision = rectangle.intersects(circle);
        Color   color     = collision ? Color.RED : Color.random();

        if (collision)
        {
            RenderUtils.tracePolygon(batcher, rectangle, color);
            RenderUtils.tracePolygon(batcher, circle, color);
        }
        else
        {
            RenderUtils.fillPolygon(batcher, rectangle, color);
            RenderUtils.fillPolygon(batcher, circle, color);
        }

        RenderUtils.tracePolygon(batcher, rectangle.getBounds(), Color.GREEN);
        RenderUtils.tracePolygon(batcher, circle.getBounds(), Color.GREEN);

        collision = rectangle2.intersects(circle2);
        color     = collision ? Color.RED : Color.random();

        if (collision)
        {
            RenderUtils.tracePolygon(batcher, rectangle2, color);
            RenderUtils.tracePolygon(batcher, circle2, color);
        }
        else
        {
            RenderUtils.fillPolygon(batcher, rectangle2, color);
            RenderUtils.fillPolygon(batcher, circle2, color);
        }

        RenderUtils.tracePolygon(batcher, rectangle2.getBounds(), Color.GREEN);
        RenderUtils.tracePolygon(batcher, circle2.getBounds(), Color.GREEN);
    }

    public void resize()
    {
        cam.initProjection(Display.getWidth(), Display.getHeight());
    }

    public static void main(String[] args)
    {
        new Geom2DTest().start();
    }
}
