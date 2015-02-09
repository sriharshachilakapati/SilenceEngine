package com.shc.silenceengine.tests;

import com.shc.silenceengine.collision.Collision2D;
import com.shc.silenceengine.core.Display;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.geom2d.Circle;
import com.shc.silenceengine.geom2d.Rectangle;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.cameras.OrthoCam;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.input.Mouse;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.utils.RenderUtils;

/**
 * @author Sri Harsha Chilakapati
 */
public class SATResponseTest extends Game
{
    private Circle    circle;
    private Rectangle rectangle;

    private OrthoCam cam;

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

    public static void main(String[] args)
    {
        new SATResponseTest().start();
    }
}
