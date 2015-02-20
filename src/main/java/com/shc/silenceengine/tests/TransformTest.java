package com.shc.silenceengine.tests;

import com.shc.silenceengine.core.Display;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.math.Transform;
import com.shc.silenceengine.math.Vector2;

/**
 * @author Sri Harsha Chilakapati
 */
public class TransformTest extends Game
{
    private Transform transform;
    private Texture texture;

    public static void main(String[] args)
    {
        new TransformTest().start();
    }

    public void dispose()
    {
        texture.dispose();
    }

    public void init()
    {
        transform = new Transform();
        texture = Texture.fromResource("resources/texture2.png");
    }

    public void update(float delta)
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

    public void render(float delta, Batcher batcher)
    {
        texture.bind();

        batcher.applyTransform(transform);
        batcher.begin();
        {
            if (Keyboard.isAnyKeyPressed())
            {
                batcher.vertex(0, 0.5f);
                batcher.color(Color.GOLDEN_ROD);
                batcher.texCoord(0.5f, 0);

                batcher.vertex(-0.5f, -0.5f);
                batcher.color(Color.GOLDEN_ROD);
                batcher.texCoord(0, 1);

                batcher.vertex(0.5f, -0.5f);
                batcher.color(Color.GOLDEN_ROD);
                batcher.texCoord(1, 1);
            }
            else
            {
                batcher.vertex(0, 0.5f);
                batcher.texCoord(0.5f, 0);

                batcher.vertex(-0.5f, -0.5f);
                batcher.texCoord(0, 1);

                batcher.vertex(0.5f, -0.5f);
                batcher.texCoord(1, 1);
            }
        }
        batcher.end();
    }

    public void resize()
    {
        Display.setViewport(0, 0, Display.getWidth(), Display.getHeight());
    }
}
