/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2015 Sri Harsha Chilakapati
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.shc.silenceengine.tests;

import com.shc.silenceengine.core.Display;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Sprite;
import com.shc.silenceengine.graphics.SpriteBatch;
import com.shc.silenceengine.graphics.cameras.OrthoCam;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.utils.GameTimer;
import com.shc.silenceengine.utils.MathUtils;
import com.shc.silenceengine.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
public class SpriteBatchTest extends Game
{
    private OrthoCam camera;

    private Texture texture1;
    private Texture texture2;

    private List<Sprite>  sprites;
    private List<Vector2> positions;

    private GameTimer timer;

    public static void main(String[] args)
    {
        new SpriteBatchTest().start();
    }

    @Override
    public void init()
    {
        Display.setVSync(false);

        camera = new OrthoCam().initProjection(Display.getWidth(), Display.getHeight());
        texture1 = Texture.fromResource("resources/texture.png");
        texture2 = Texture.fromResource("resources/texture2.png");

        sprites = new ArrayList<>();
        positions = new ArrayList<>();

        timer = new GameTimer(0.5, TimeUtils.Unit.SECONDS);
        timer.setCallback(() ->
        {
            defineSprites();
            timer.start();
        });
        timer.start();
    }

    @Override
    public void resize()
    {
        camera.initProjection(Display.getWidth(), Display.getHeight());
    }

    @Override
    public void update(float delta)
    {
        if (Keyboard.isPressed(Keyboard.KEY_ESCAPE))
            Game.end();

        Display.setTitle(
                "SpriteBatch Test" +
                " | UPS: " + Game.getUPS() +
                " | FPS: " + Game.getFPS() +
                " | RC: " + SilenceEngine.graphics.renderCallsPerFrame
        );
    }

    @Override
    public void render(float delta, Batcher batcher)
    {
        camera.apply();
        SpriteBatch batch = SilenceEngine.graphics.getSpriteBatch();

        batch.begin();
        {
            for (int i = 0; i < sprites.size(); i++)
            {
                batch.addSprite(sprites.get(i), positions.get(i));
            }
        }
        batch.end();
    }

    @Override
    public void dispose()
    {
        texture1.dispose();
        texture2.dispose();
    }

    private void defineSprites()
    {
        sprites.clear();
        positions.clear();

        for (int i = 0; i < 10; i++)
        {
            sprites.add(new Sprite(
                    i % 2 == 0 ? texture1 : texture2,   // Either texture1 or texture2
                    MathUtils.random_range(1, 3),       // A x-scale between 1 and 3
                    MathUtils.random_range(1, 3),       // A y-scale between 1 and 3
                    MathUtils.random(360)               // A random rotation between 0 and 360
            ));
        }

        for (int y = 0; y < 2; y++)
        {
            for (int x = 0; x < 5; x++)
                positions.add(new Vector2(Display.getWidth() / 5 * x, Display.getHeight() / 2 * y));
        }
    }
}
