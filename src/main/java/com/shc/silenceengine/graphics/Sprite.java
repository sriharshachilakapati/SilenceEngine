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

package com.shc.silenceengine.graphics;

import com.shc.silenceengine.core.IUpdatable;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.utils.IDGenerator;
import com.shc.silenceengine.utils.TimeUtils;

/**
 * @author Sri Harsha Chilakapati
 */
public class Sprite implements IUpdatable
{
    public static Sprite EMPTY;

    private Animation animation;

    private int id;

    private float scaleX;
    private float scaleY;
    private float rotation;

    private Color tint;

    public Sprite()
    {
        this(new Animation(), 1, 1, 0);
    }

    public Sprite(Sprite sprite)
    {
        this();
        set(sprite);
    }

    public Sprite(Animation animation)
    {
        this(animation, 1, 1, 0);
    }

    public Sprite(Texture texture)
    {
        this(texture, 1, 1, 0);
    }

    public Sprite(Animation animation, float scaleX, float scaleY, float rotation)
    {
        id = IDGenerator.generate();

        this.animation = animation.copy();
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.rotation = rotation;
        
        tint = new Color(Color.TRANSPARENT);
    }

    public Sprite(Texture texture, float scaleX, float scaleY, float rotation)
    {
        this(new Animation(), scaleX, scaleY, rotation);
        setTexture(texture);
    }

    public void update(float delta)
    {
        animation.update(delta);
    }

    public Sprite copy()
    {
        return new Sprite(this);
    }

    public Sprite set(Sprite sprite)
    {
        animation.set(sprite.getAnimation());

        scaleX = sprite.getScaleX();
        scaleY = sprite.getScaleY();
        rotation = sprite.getRotation();

        return this;
    }

    public Texture getTexture()
    {
        return animation.getCurrentFrame();
    }

    public Sprite setTexture(Texture texture)
    {
        animation.clearFrames();
        animation.setStartCallback(null);
        animation.setPauseCallback(null);
        animation.setResumeCallback(null);
        animation.setEndCallback(null);

        animation.addFrame(texture, 1000, TimeUtils.Unit.SECONDS);

        return this;
    }

    public Animation getAnimation()
    {
        return animation;
    }

    public Sprite setAnimation(Animation animation)
    {
        this.animation.set(animation);
        return this;
    }

    public float getScaleX()
    {
        return scaleX;
    }

    public Sprite setScaleX(float scaleX)
    {
        this.scaleX = scaleX;
        return this;
    }

    public float getScaleY()
    {
        return scaleY;
    }

    public Sprite setScaleY(float scaleY)
    {
        this.scaleY = scaleY;
        return this;
    }

    public float getRotation()
    {
        return rotation;
    }

    public Sprite setRotation(float rotation)
    {
        this.rotation = rotation;
        return this;
    }

    public int getID()
    {
        return id;
    }

    public Color getTint()
    {
        return tint;
    }

    public void setTint(Color tint)
    {
        this.tint.set(tint);
    }
}
