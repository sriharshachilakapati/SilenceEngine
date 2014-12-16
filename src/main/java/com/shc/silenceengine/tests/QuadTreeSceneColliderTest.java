package com.shc.silenceengine.tests;

import com.shc.silenceengine.collision.QuadTreeSceneCollider;
import com.shc.silenceengine.core.Display;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.entity.Entity2D;
import com.shc.silenceengine.geom2d.Rectangle;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.OrthoCam;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.scene.Scene;
import com.shc.silenceengine.utils.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class QuadTreeSceneColliderTest extends Game
{
    private Scene                 scene;
    private QuadTreeSceneCollider collider;
    private OrthoCam              cam;

    public void init()
    {
        cam = new OrthoCam().initProjection(Display.getWidth(), Display.getHeight());

        // Create and initialize the scene
        scene = new Scene();
        for (int x = 48; x < 480; x += 48)
        {
            scene.addChild(new Box(new Vector2(x, 48)));
            scene.addChild(new Box(new Vector2(x, 380)));
        }
        scene.addChild(new Player(new Vector2(400, 300)));
        scene.init();

        // Create the collider
        collider = new QuadTreeSceneCollider(640, 480);
        collider.setScene(scene);

        // Register for collisions between entities
        collider.register(Player.class, Box.class);
    }

    public void update(double delta)
    {
        if (Keyboard.isPressed(Keyboard.KEY_ESCAPE))
            end();

        scene.update(delta);
        collider.checkCollisions();
    }

    public void render(double delta, Batcher batcher)
    {
        cam.apply();
        scene.render(delta, batcher);
    }

    public void resize()
    {
        cam.initProjection(Display.getWidth(), Display.getHeight());
    }

    public void dispose()
    {
        scene.destroy();
    }

    public static void main(String[] args)
    {
        new QuadTreeSceneColliderTest().start();
    }

    public static class Box extends Entity2D
    {
        public Box(Vector2 position)
        {
            setPolygon(new Rectangle(0, 0, 48, 48));
            setPosition(position);
        }

        public void render(double delta, Batcher batcher)
        {
            RenderUtils.fillPolygon(batcher, getPolygon(), Color.CORN_FLOWER_BLUE);
            RenderUtils.tracePolygon(batcher, getPolygon(), Color.RED);
        }
    }

    public static class Player extends Entity2D
    {
        private Color color;

        public Player(Vector2 position)
        {
            setPolygon(new Rectangle(0, 0, 48, 48));
            setPosition(position);

            color = Color.random();
        }

        public void update(double delta)
        {
            Vector2 velocity = new Vector2();

            float speed = 4;

            if (Keyboard.isPressed(Keyboard.KEY_UP))
                velocity.y -= speed;

            if (Keyboard.isPressed(Keyboard.KEY_DOWN))
                velocity.y += speed;

            if (Keyboard.isPressed(Keyboard.KEY_LEFT))
                velocity.x -= speed;

            if (Keyboard.isPressed(Keyboard.KEY_RIGHT))
                velocity.x += speed;

            setVelocity(velocity);
        }

        public void collision(Entity2D other)
        {
            color = Color.random();
        }

        public void render(double delta, Batcher batcher)
        {
            RenderUtils.fillPolygon(batcher, getPolygon(), color);
            RenderUtils.tracePolygon(batcher, getPolygon(), Color.GREEN);
        }
    }
}

