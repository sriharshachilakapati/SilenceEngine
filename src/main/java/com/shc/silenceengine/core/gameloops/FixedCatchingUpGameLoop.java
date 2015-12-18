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
public class FixedCatchingUpGameLoop extends GameLoop
{
    // GameLoop constants
    private final double SECOND = TimeUtils.convert(1, TimeUtils.Unit.SECONDS, TimeUtils.getDefaultTimeUnit());

    private int updatesProcessed;
    private int framesProcessed;
    private int skippedFrames;
    private int maxSkippedFrames;
    private int targetUps;

    private double currentTime;
    private double previousTime;

    private double lag;

    private double lastUPSUpdate;
    private double lastFPSUpdate;
    private double frameTime;

    private boolean stopQuitsJVM;
    private boolean paused;

    public FixedCatchingUpGameLoop()
    {
        setTargetUpdatesPerSecond(60);
        setMaxSkippedFrames(10);
        setStopQuitsJVM(true);
    }

    public FixedCatchingUpGameLoop setMaxSkippedFrames(int maxSkippedFrames)
    {
        this.maxSkippedFrames = maxSkippedFrames;
        return this;
    }

    public FixedCatchingUpGameLoop setStopQuitsJVM(boolean stopQuitsJVM)
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

        previousTime = TimeUtils.currentTime();

        running = true;

        // The Game Loop
        loop:
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

            currentTime = TimeUtils.currentTime();
            double elapsed = currentTime - previousTime;

            lag += elapsed;

            while (lag > frameTime && skippedFrames < maxSkippedFrames)
            {
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

                    if (!isRunning()) break loop;

                    GameTimer.updateTimers((float) frameTime);
                }
                SilenceEngine.input.endFrame();

                updatesProcessed++;
                lag -= frameTime;

                skippedFrames++;

                if (currentTime - lastUPSUpdate >= SECOND)
                {
                    ups = updatesProcessed;
                    updatesProcessed = 0;
                    lastUPSUpdate = currentTime;
                }
            }

            float lagOffset = (float) (lag / frameTime);
            game.render(lagOffset, SilenceEngine.graphics.getBatcher());

            if (Game.getGameState() != null)
                Game.getGameState().render(lagOffset, SilenceEngine.graphics.getBatcher());

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

            skippedFrames = 0;

            previousTime = currentTime;
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
        currentTime = previousTime = TimeUtils.currentTime();
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

    public FixedCatchingUpGameLoop setTargetUpdatesPerSecond(int targetUps)
    {
        this.targetUps = targetUps;
        this.frameTime = SECOND / targetUps;

        return this;
    }
}
