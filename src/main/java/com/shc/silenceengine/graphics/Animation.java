package com.shc.silenceengine.graphics;

import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
public class Animation
{
    private List<Texture> frames;
    private List<Float> frameLengths;

    private IAnimationStartCallback startCallback;
    private IAnimationPauseCallback pauseCallback;
    private IAnimationResumeCallback resumeCallback;
    private IAnimationEndCallback endCallback;

    private int currentFrame;
    private float time;

    private boolean active;

    public Animation()
    {
        // Construct with empty callbacks
        this(() -> {
        }, () -> {
        }, () -> {
        }, () -> {
        });
    }

    public Animation(IAnimationStartCallback startCallback,
                     IAnimationPauseCallback pauseCallback,
                     IAnimationResumeCallback resumeCallback,
                     IAnimationEndCallback endCallback)
    {
        this.startCallback = startCallback;
        this.pauseCallback = pauseCallback;
        this.resumeCallback = resumeCallback;
        this.endCallback = endCallback;

        frames = new ArrayList<>();
        frameLengths = new ArrayList<>();

        this.active = false;
    }

    public void addFrame(Texture texture, float length, TimeUtils.Unit unit)
    {
        frames.add(texture);
        frameLengths.add((float) TimeUtils.convert(length, unit, TimeUtils.Unit.SECONDS));
    }

    public void start()
    {
        if (active)
            return;

        active = true;

        if (currentFrame == 0)
            startCallback.invoke();
        else
            resumeCallback.invoke();
    }

    public void pause()
    {
        if (!active)
            return;

        active = false;
        pauseCallback.invoke();
    }

    public void update(float delta)
    {
        if (!active)
            return;

        time += delta;

        if (time > frameLengths.get(currentFrame))
        {
            time -= frameLengths.get(currentFrame);
            currentFrame++;

            if (currentFrame >= frameLengths.size())
                stop();
        }
    }

    public void stop()
    {
        if (!active)
            return;

        active = false;
        currentFrame = 0;

        endCallback.invoke();
    }

    public Texture getCurrentFrame()
    {
        return frames.get(currentFrame);
    }

    public float getCurrentFrameLength()
    {
        return frameLengths.get(currentFrame);
    }

    public void setStartCallback(IAnimationStartCallback startCallback)
    {
        if (startCallback == null)
            startCallback = () -> {
            };

        this.startCallback = startCallback;
    }

    public void setPauseCallback(IAnimationPauseCallback pauseCallback)
    {
        if (pauseCallback == null)
            pauseCallback = () -> {
            };

        this.pauseCallback = pauseCallback;
    }

    public void setResumeCallback(IAnimationResumeCallback resumeCallback)
    {
        if (resumeCallback == null)
            resumeCallback = () -> {
            };

        this.resumeCallback = resumeCallback;
    }

    public void setEndCallback(IAnimationEndCallback endCallback)
    {
        if (endCallback == null)
            endCallback = () -> {
            };

        this.endCallback = endCallback;
    }

    @FunctionalInterface
    public static interface IAnimationStartCallback
    {
        public void invoke();
    }

    @FunctionalInterface
    public static interface IAnimationPauseCallback
    {
        public void invoke();
    }

    @FunctionalInterface
    public static interface IAnimationResumeCallback
    {
        public void invoke();
    }

    @FunctionalInterface
    public static interface IAnimationEndCallback
    {
        public void invoke();
    }
}
