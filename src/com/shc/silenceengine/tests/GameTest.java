package com.shc.silenceengine.tests;

import com.shc.silenceengine.Display;
import com.shc.silenceengine.Game;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.Vector3;
import org.lwjgl.opengl.GL11;

/**
 * @author Sri Harsha Chilakapati
 */
public class GameTest extends Game
{
    Batcher batcher;

    public void init()
    {
        Display.setTitle("GameTest");
        batcher = new Batcher();
    }

    @Override
    public void update(long delta)
    {
        if (Keyboard.isPressed(Keyboard.KEY_ESCAPE))
            end();

        if (Keyboard.isPressed(Keyboard.KEY_F1))
        {
            Display.setFullScreen(!Display.isFullScreen());

            if (Display.isFullScreen())
                Display.hideCursor();
            else
                Display.showCursor();
        }
    }

    @Override
    public void render(long delta)
    {
        batcher.begin();
        {
            batcher.addVertex(new Vector2(+0.0f, +0.5f));
            batcher.addVertex(new Vector2(-0.5f, -0.5f));
            batcher.addVertex(new Vector2(+0.5f, -0.5f));

            batcher.addVertex(new Vector3(+1.0f, +1.0f, +0.0f));
            batcher.addVertex(new Vector3(+1.0f, -1.0f, +0.0f));
            batcher.addVertex(new Vector3(+0.5f, +0.0f, +1.0f));
        }
        batcher.end();
    }

    @Override
    public void resize()
    {
        GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
    }

    @Override
    public void dispose()
    {
        batcher.dispose();
    }

    public static void main(String[] args)
    {
        new GameTest().start();
    }
}
