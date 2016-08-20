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
 */

package com.shc.silenceengine.graphics;

import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.utils.TimeUtils;
import com.shc.silenceengine.utils.functional.SimpleCallback;

/**
 * @author Sri Harsha Chilakapati
 */
public class Sprite
{
    private Animation animation;

    private SimpleCallback startCallback;
    private SimpleCallback pauseCallback;
    private SimpleCallback resumeCallback;
    private SimpleCallback endCallback;

    private int   currentFrame;
    private float time;

    private boolean active;

    public Sprite(Texture texture)
    {
        this(new Animation().addFrame(texture, 1, TimeUtils.Unit.SECONDS));
    }

    public Sprite(Animation animation)
    {
        // Construct with empty callbacks
        this(animation, () -> {
        }, () -> {
        }, () -> {
        }, () -> {
        });
    }

    public Sprite(Texture texture, SimpleCallback startCallback,
                  SimpleCallback pauseCallback,
                  SimpleCallback resumeCallback,
                  SimpleCallback endCallback)
    {
        // Construct with empty callbacks
        this(new Animation().addFrame(texture, 1, TimeUtils.Unit.SECONDS), () -> {
        }, () -> {
        }, () -> {
        }, () -> {
        });
    }

    public Sprite(Animation animation, SimpleCallback startCallback,
                  SimpleCallback pauseCallback,
                  SimpleCallback resumeCallback,
                  SimpleCallback endCallback)
    {
        this.startCallback = startCallback;
        this.pauseCallback = pauseCallback;
        this.resumeCallback = resumeCallback;
        this.endCallback = endCallback;

        this.active = false;
        this.animation = animation;
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

        if (time > animation.getFrameLength(currentFrame))
        {
            time -= animation.getFrameLength(currentFrame);
            currentFrame++;

            if (currentFrame >= animation.size())
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

    public Sprite copy()
    {
        return new Sprite(animation).set(this);
    }

    public Sprite set(Sprite other)
    {
        currentFrame = other.currentFrame;
        time = other.time;

        setStartCallback(other.startCallback);
        setPauseCallback(other.pauseCallback);
        setResumeCallback(other.resumeCallback);
        setEndCallback(other.endCallback);

        return this;
    }

    public Texture getCurrentFrame()
    {
        return animation.getFrameTexture(currentFrame);
    }

    public float getCurrentFrameLength()
    {
        return animation.getFrameLength(currentFrame);
    }

    public boolean isActive()
    {
        return active;
    }

    public void setStartCallback(SimpleCallback startCallback)
    {
        if (startCallback == null)
            startCallback = () -> {
            };

        this.startCallback = startCallback;
    }

    public void setPauseCallback(SimpleCallback pauseCallback)
    {
        if (pauseCallback == null)
            pauseCallback = () -> {
            };

        this.pauseCallback = pauseCallback;
    }

    public void setResumeCallback(SimpleCallback resumeCallback)
    {
        if (resumeCallback == null)
            resumeCallback = () -> {
            };

        this.resumeCallback = resumeCallback;
    }

    public void setEndCallback(SimpleCallback endCallback)
    {
        if (endCallback == null)
            endCallback = () -> {
            };

        this.endCallback = endCallback;
    }
}
