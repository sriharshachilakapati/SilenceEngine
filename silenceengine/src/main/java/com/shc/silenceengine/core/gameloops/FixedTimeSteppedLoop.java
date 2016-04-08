/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2016 Sri Harsha Chilakapati
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
 *
 */

package com.shc.silenceengine.core.gameloops;

import com.shc.silenceengine.core.IGameLoop;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.opengl.GLContext;
import com.shc.silenceengine.utils.TimeUtils;

import static com.shc.silenceengine.graphics.IGraphicsDevice.Constants.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class FixedTimeSteppedLoop implements IGameLoop
{
    private final int   targetUpdatesPerSecond;
    private final float frameTime;

    private final int maxFrameSkips;

    private int frames;
    private int framesPerSecond;
    private int updates;
    private int updatesPerSecond;

    private double gameTime = -1;
    private double lastStatsTime;

    public FixedTimeSteppedLoop()
    {
        this(60, 5);
    }

    public FixedTimeSteppedLoop(int targetUpdatesPerSecond)
    {
        this(targetUpdatesPerSecond, 5);
    }

    public FixedTimeSteppedLoop(int targetUpdatesPerSecond, int maxFrameSkips)
    {
        this.targetUpdatesPerSecond = targetUpdatesPerSecond;
        this.frameTime = (float) (TimeUtils.convert(1, TimeUtils.Unit.SECONDS) / targetUpdatesPerSecond);

        this.maxFrameSkips = maxFrameSkips;
    }

    @Override
    public void performLoopFrame()
    {
        double now = TimeUtils.currentTime();

        if (gameTime == -1)
            gameTime = now;

        int skippedFrames = 0;

        while (gameTime <= now && skippedFrames <= maxFrameSkips)
        {
            if (updates <= targetUpdatesPerSecond)
            {
                updates++;
                SilenceEngine.eventManager.raiseUpdateEvent(frameTime);
            }

            gameTime += frameTime;
            skippedFrames++;
        }

        frames++;
        GLContext.clear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        SilenceEngine.eventManager.raiseRenderEvent(frameTime);

        if (now - lastStatsTime >= TimeUtils.convert(1, TimeUtils.Unit.SECONDS))
        {
            updatesPerSecond = updates;
            framesPerSecond = frames;

            updates = frames = 0;
            lastStatsTime = now;
        }
    }

    @Override
    public int getFPS()
    {
        return framesPerSecond;
    }

    @Override
    public int getUPS()
    {
        return updatesPerSecond;
    }
}
