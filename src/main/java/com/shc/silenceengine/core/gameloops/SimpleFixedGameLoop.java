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

package com.shc.silenceengine.core.gameloops;

import com.shc.silenceengine.core.Display;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.core.GameLoop;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.graphics.Graphics2D;
import com.shc.silenceengine.backend.lwjgl3.opengl.GL3Context;
import com.shc.silenceengine.utils.GameTimer;
import com.shc.silenceengine.utils.Logger;
import com.shc.silenceengine.utils.TimeUtils;

/**
 * @author Sri Harsha Chilakapati
 */
public class SimpleFixedGameLoop extends GameLoop
{
    // GameLoop constants
    private final double SECOND = TimeUtils.convert(1, TimeUtils.Unit.SECONDS, TimeUtils.getDefaultTimeUnit());

    private int updatesProcessed;
    private int framesProcessed;
    private int targetUps;

    private double lastUPSUpdate;
    private double lastFPSUpdate;
    private double frameTime;

    private boolean stopQuitsJVM;
    private boolean paused;

    public SimpleFixedGameLoop()
    {
        setTargetUpdatesPerSecond(60);
        setStopQuitsJVM(true);
    }

    public SimpleFixedGameLoop setStopQuitsJVM(boolean stopQuitsJVM)
    {
        this.stopQuitsJVM = stopQuitsJVM;
        return this;
    }

    @Override
    public void start()
    {
        if (running)
            throw new SilenceException("Game loop is already running.");

        running = true;

        // Initialize the Game
        Logger.info("Initializing the Game resources");
        game.init();
        Runtime.getRuntime().gc();
        Logger.info("Game initialized successfully, proceeding to the main loop");

        running = true;

        // The Game Loop
        while (true)
        {
            // Start a frame in the game loop
            SilenceEngine.getInstance().beginFrame();

            if (Display.isCloseRequested())
            {
                stop();
                break;
            }

            if (!isRunning()) break;

            if (Display.wasResized())
            {
                GL3Context.viewport(0, 0, Display.getWidth(), Display.getHeight());
                Graphics2D.getInstance().getCamera().initProjection(Display.getWidth(), Display.getHeight());
                game.resize();

                if (Game.getGameState() != null)
                    Game.getGameState().resize();
            }

            if (Display.wasDirty())
            {
                // End the old frame and start a new frame
                SilenceEngine.getInstance().endFrame();
                SilenceEngine.getInstance().beginFrame();
            }

            // Input needs to be updated even faster!
            SilenceEngine.input.beginFrame();
            {
                if (!paused)
                {
                    game.update((float) frameTime);

                    if (Game.getGameState() != null)
                        Game.getGameState().update((float) frameTime);
                }

                if (!isRunning()) break;

                GameTimer.updateTimers((float) frameTime);
            }
            SilenceEngine.input.endFrame();

            updatesProcessed++;

            double currentTime = TimeUtils.currentTime();

            if (currentTime - lastUPSUpdate >= SECOND)
            {
                ups = updatesProcessed;
                updatesProcessed = 0;
                lastUPSUpdate = currentTime;
            }

            game.render(0, SilenceEngine.graphics.getBatcher());

            if (Game.getGameState() != null)
                Game.getGameState().render(0, SilenceEngine.graphics.getBatcher());

            if (!isRunning())
                break;

            framesProcessed++;

            if (currentTime - lastFPSUpdate >= SECOND)
            {
                fps = framesProcessed;
                framesProcessed = 0;
                lastFPSUpdate = currentTime;
            }

            SilenceEngine.getInstance().endFrame();

            try
            {
                Thread.sleep((long) TimeUtils.convert(SECOND - frameTime,
                        TimeUtils.getDefaultTimeUnit(),
                        TimeUtils.Unit.MILLIS));
            }
            catch (InterruptedException e)
            {
                Logger.trace(e);
            }
        }

        stop();
    }

    @Override
    public void pause()
    {
        paused = true;
    }

    @Override
    public void resume()
    {
        paused = false;
    }

    @Override
    public void stop()
    {
        Logger.info("Disposing the Game resources");

        game.dispose();
        SilenceEngine.getInstance().dispose();

        Logger.info("This game has been terminated successfully");

        running = false;

        if (stopQuitsJVM)
            System.exit(0);
    }

    public int getTargetUpdatesPerSecond()
    {
        return targetUps;
    }

    public SimpleFixedGameLoop setTargetUpdatesPerSecond(int targetUps)
    {
        this.targetUps = targetUps;
        this.frameTime = SECOND / targetUps;

        return this;
    }
}
