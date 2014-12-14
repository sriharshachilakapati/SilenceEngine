package com.shc.silenceengine.tests;

import com.shc.silenceengine.core.Display;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.geom2d.Circle;
import com.shc.silenceengine.geom2d.Rectangle;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.OrthoCam;
import com.shc.silenceengine.graphics.opengl.Primitive;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.utils.TimeUtils;

import static com.shc.silenceengine.utils.MathUtils.*;

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

    public void update(double delta)
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
        rectangle.rotate((float) delta);
        rectangle2.rotate((float) delta);
        circle.rotate((float) delta);
        circle2.rotate((float) delta);
    }

    public void render(double delta, Batcher batcher)
    {
        cam.apply();

        // Did collision happen?
        boolean collision = rectangle.intersects(circle);

        // Determine the color and primitive
        Color rectColor     = collision ? Color.RED : Color.WHITE;
        Primitive primitive = collision ? Primitive.LINE_LOOP : Primitive.TRIANGLE_FAN;

        // Draw first rectangle
        batcher.begin(primitive);
        {
            for (int i = 0; i < rectangle.vertexCount(); i++)
            {
                batcher.vertex(add(rectangle.getVertex(i), rectangle.getPosition()));
                batcher.color(rectColor);
            }
        }
        batcher.end();

        // Draw second rectangle
        batcher.begin(primitive);
        {
            for (int i = 0; i < circle.vertexCount(); i++)
            {
                batcher.vertex(add(circle.getVertex(i), circle.getPosition()));
                batcher.color(rectColor);
            }
        }
        batcher.end();

        // Did the collision happen?
        collision = rectangle2.intersects(circle2);

        // Determine color and primitive again
        rectColor = collision ? Color.RED : Color.WHITE;
        primitive = collision ? Primitive.LINE_LOOP : Primitive.TRIANGLE_FAN;

        // Draw second rectangle
        batcher.begin(primitive);
        {
            for (int i = 0; i < rectangle2.vertexCount(); i++)
            {
                batcher.vertex(add(rectangle2.getVertex(i), rectangle2.getPosition()));
                batcher.color(rectColor);
            }
        }
        batcher.end();

        // Draw second circle
        batcher.begin(primitive);
        {
            for (int i = 0; i < circle2.vertexCount(); i++)
            {
                batcher.vertex(add(circle2.getVertex(i), circle2.getPosition()));
                batcher.color(rectColor);
            }
        }
        batcher.end();
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
