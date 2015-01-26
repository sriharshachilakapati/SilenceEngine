package com.shc.silenceengine.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
public class GameTimer
{
    private static List<GameTimer> timers = new ArrayList<>();

    public static interface TimerCallback
    {
        public void invoke();
    }

    private TimerCallback callback;

    private double time;
    private double elapsed;

    private boolean active;

    public GameTimer(double time, TimeUtils.Unit unit)
    {
        this.time = TimeUtils.convert(time, unit, TimeUtils.getDefaultTimeUnit());
    }

    public void start()
    {
        active = true;
        elapsed = 0;

        if (timers.contains(this))
            return;

        timers.add(this);
    }

    public void stop()
    {
        active = false;
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

    public boolean isActive()
    {
        return active;
    }
}
