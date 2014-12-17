package com.shc.silenceengine.tests;

import com.shc.silenceengine.core.Display;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.input.Keyboard;

/**
 * @author Sri Harsha Chilakapati
 */
public class TextureTest extends Game
{
    private Texture texture;

    public void init()
    {
        Display.setClearColor(Color.CORN_FLOWER_BLUE);

        texture = Texture.fromResource("resources/logo.png");
    }

    public void update(float delta)
    {
        if (Keyboard.isPressed(Keyboard.KEY_ESCAPE))
            end();

        if (Keyboard.isPressed(Keyboard.KEY_F1))
            Display.setFullScreen(!Display.isFullScreen());
    }

    public void render(float delta, Batcher batcher)
    {
        texture.bind();

        batcher.begin();
        {
            if (Keyboard.isAnyKeyPressed())
            {
                batcher.vertex(-0.5f, 0.5f);
                batcher.color(Color.GOLDEN_ROD);
                batcher.texCoord(0, 0);

                batcher.vertex(0.5f, 0.5f);
                batcher.color(Color.GOLDEN_ROD);
                batcher.texCoord(1, 0);

                batcher.vertex(-0.5f, -0.5f);
                batcher.color(Color.GOLDEN_ROD);
                batcher.texCoord(0, 1);

                batcher.vertex(0.5f, 0.5f);
                batcher.color(Color.GOLDEN_ROD);
                batcher.texCoord(1, 0);

                batcher.vertex(-0.5f, -0.5f);
                batcher.color(Color.GOLDEN_ROD);
                batcher.texCoord(0, 1);

                batcher.vertex(0.5f, -0.5f);
                batcher.color(Color.GOLDEN_ROD);
                batcher.texCoord(1, 1);
            }
            else
            {
                batcher.vertex(-0.5f, 0.5f);
                batcher.texCoord(0, 0);

                batcher.vertex(0.5f, 0.5f);
                batcher.texCoord(1, 0);

                batcher.vertex(-0.5f, -0.5f);
                batcher.texCoord(0, 1);

                batcher.vertex(0.5f, 0.5f);
                batcher.texCoord(1, 0);

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

    public void dispose()
    {
        texture.dispose();
    }

    public static void main(String[] args)
    {
        new TextureTest().start();
    }
}
