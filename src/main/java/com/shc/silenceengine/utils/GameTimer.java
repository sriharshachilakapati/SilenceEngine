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

package com.shc.silenceengine.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
public class GameTimer
{
    private static List<GameTimer> timers = new ArrayList<>();
    private TimerCallback callback;
    private double        time;
    private double        elapsed;
    private boolean       active;

    public GameTimer(double time, TimeUtils.Unit unit)
    {
        this.time = TimeUtils.convert(time, unit, TimeUtils.getDefaultTimeUnit());
    }

    public static void updateTimers(float delta)
    {
        for (int i = 0; i < timers.size(); i++)
        {
            GameTimer timer = timers.get(i);
            timer.update(delta);

            if (!timer.isActive())
            {
                timers.remove(timer);
                i--;
            }
        }
    }

    public void update(float delta)
    {
        if (active)
        {
            elapsed += delta;

            if (elapsed >= time)
            {
                stop();
                callback.invoke();
            }
        }
    }

    public void stop()
    {
        active = false;
    }

    public boolean isActive()
    {
        return active;
    }

    public void start()
    {
        active = true;
        elapsed = 0;

        if (timers.contains(this))
            return;

        timers.add(this);
    }

    public void setCallback(TimerCallback callback)
    {
        this.callback = callback;
    }

    public void setTime(float time, TimeUtils.Unit unit)
    {
        this.time = TimeUtils.convert(time, unit, TimeUtils.getDefaultTimeUnit());
        elapsed = 0;
    }

    public static interface TimerCallback
    {
        public void invoke();
    }
}
