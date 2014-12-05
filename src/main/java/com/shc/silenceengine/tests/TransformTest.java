package com.shc.silenceengine.tests;

import com.shc.silenceengine.core.Display;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.Texture;
import com.shc.silenceengine.graphics.Transform;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.math.Vector2;

/**
 * @author Sri Harsha Chilakapati
 */
public class TransformTest extends Game
{
    private Transform transform;
    private Texture   texture;
    private Batcher   batcher;

    public void init()
    {
        batcher   = new Batcher();
        transform = new Transform();
        texture   = Texture.fromResource("resources/texture.png");
    }

    public void update(long delta)
    {
        if (Keyboard.isPressed(Keyboard.KEY_ESCAPE))
            end();

        if (Keyboard.isPressed(Keyboard.KEY_LEFT))
            transform.translate(new Vector2(-0.01f, 0));

        if (Keyboard.isPressed(Keyboard.KEY_RIGHT))
            transform.translate(new Vector2(+0.01f, 0));

        if (Keyboard.isPressed(Keyboard.KEY_UP))
            transform.translate(new Vector2(0, +0.01f));

        if (Keyboard.isPressed(Keyboard.KEY_DOWN))
            transform.translate(new Vector2(0, -0.01f));
    }

    public void render(long delta)
    {
        texture.bind();

        batcher.begin();
        {
            batcher.applyTransform(transform);

            if (Keyboard.isAnyKeyPressed())
            {
                batcher.addVertex(new Vector2(-0.5f, +0.5f), Color.GOLDEN_ROD, new Vector2(0, 0));
                batcher.addVertex(new Vector2(+0.5f, +0.5f), Color.GOLDEN_ROD, new Vector2(1, 0));
                batcher.addVertex(new Vector2(+0.5f, -0.5f), Color.GOLDEN_ROD, new Vector2(1, 1));

                batcher.addVertex(new Vector2(-0.5f, +0.5f), Color.GOLDEN_ROD, new Vector2(0, 0));
                batcher.addVertex(new Vector2(-0.5f, -0.5f), Color.GOLDEN_ROD, new Vector2(0, 1));
                batcher.addVertex(new Vector2(+0.5f, -0.5f), Color.GOLDEN_ROD, new Vector2(1, 1));
            }
            else
            {
                batcher.addVertex(new Vector2(-0.5f, +0.5f), new Vector2(0, 0));
                batcher.addVertex(new Vector2(+0.5f, +0.5f), new Vector2(1, 0));
                batcher.addVertex(new Vector2(+0.5f, -0.5f), new Vector2(1, 1));

                batcher.addVertex(new Vector2(-0.5f, +0.5f), new Vector2(0, 0));
                batcher.addVertex(new Vector2(-0.5f, -0.5f), new Vector2(0, 1));
                batcher.addVertex(new Vector2(+0.5f, -0.5f), new Vector2(1, 1));
            }
        }
        batcher.end();
    }

    public void resize()
    {
        Display.setViewport(0, 0, Display.getWidth(), Display.getHeight());
    }

    public void dispose()
    {
        batcher.dispose();
        texture.dispose();
    }

    public static void main(String[] args)
    {
        new TransformTest().start();
    }
}
