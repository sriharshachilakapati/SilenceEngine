package com.shc.silenceengine.tests;

import com.shc.silenceengine.core.Display;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.input.Keyboard;

/**
 * @author Sri Harsha Chilakapati
 */
public class GameTest extends Game
{
    public static void main(String[] args)
    {
        new GameTest().start();
    }

    public void init()
    {
        Display.setTitle("GameTest");
    }

    @Override
    public void update(float delta)
    {
        if (Keyboard.isPressed(Keyboard.KEY_ESCAPE))
            end();

        if (Keyboard.isClicked(Keyboard.KEY_F1))
        {
            Display.setFullScreen(!Display.isFullScreen());

            if (Display.isFullScreen())
                Display.hideCursor();
            else
                Display.showCursor();
        }

        Display.setTitle("Total Memory: " + (getTotalMemory() / 1048576) + "MB / Free Memory: " + (getFreeMemory() / 1048576) + "MB / Used Memory: " + (getUsedMemory() / 1048576) + "MB");
    }

    @Override
    public void render(float delta, Batcher batcher)
    {
        batcher.begin();
        {
            batcher.vertex(0, 0.5f);
            batcher.color(Color.CORN_FLOWER_BLUE);

            batcher.vertex(-0.5f, -0.5f);
            batcher.color(Color.INDIAN_RED);

            batcher.vertex(0.5f, -0.5f);
            batcher.color(Color.OLIVE_DRAB);

            batcher.vertex(1, 1, 0);
            batcher.vertex(1, -1, 0);
            batcher.vertex(0.5f, 0, 1);
        }
        batcher.end();
    }

    @Override
    public void resize()
    {
        Display.setViewport(0, 0, Display.getWidth(), Display.getHeight());
    }
}
