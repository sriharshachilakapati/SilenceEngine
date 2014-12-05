package com.shc.silenceengine.tests;

import com.shc.silenceengine.core.Display;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.Texture;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.math.Vector2;

/**
 * @author Sri Harsha Chilakapati
 */
public class TextureTest extends Game
{
    private Batcher batcher;
    private Texture texture;

    public void init()
    {
        Display.setClearColor(Color.CORN_FLOWER_BLUE);

        batcher = new Batcher();
        texture = Texture.fromResource("resources/logo.png");
    }

    public void update(long delta)
    {
        if (Keyboard.isPressed(Keyboard.KEY_ESCAPE))
            end();

        if (Keyboard.isPressed(Keyboard.KEY_F1))
            Display.setFullScreen(!Display.isFullScreen());
    }

    public void render(long delta)
    {
        texture.bind();

        batcher.begin();
        {
            if (Keyboard.isPressed(Keyboard.KEY_SPACE))
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
        texture.dispose();
        batcher.dispose();
    }

    public static void main(String[] args)
    {
        new TextureTest().start();
    }
}
