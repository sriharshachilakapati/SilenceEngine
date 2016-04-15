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

package com.shc.silenceengine.core;

/**
 * A GameState is used to control the different kinds of states the game can be in. These can also be thought of as
 * modes. Generally a game has at least a Main Menu, Play and Pause state. You should also use different states for when
 * there will be a major change in game mechanics. Ex. A 2D game changes from a Side-Scroller to a Top-Down
 * Perspective.
 *
 * @author Sri Harsha Chilakapati
 */
public abstract class GameState
{
    /**
     * Called when the game switches to this <code>GameState</code> through <code>setGameState()</code>.
     *
     * @see com.shc.silenceengine.core.Game#setGameState(GameState)
     */
    public void onEnter()
    {
    }

    /**
     * Update game logic. This is called internally at a default 60 times a second.
     *
     * @param delta The frame rate time in seconds
     */
    public void update(float delta)
    {
    }

    /**
     * Renders this state. This is called internally whenever the display needs to be updated.
     *
     * @param delta   The frame rate time in seconds
     */
    public void render(float delta)
    {
    }

    /**
     * Called internally when the game window has been resized
     */
    public void resized()
    {
    }

    /**
     * Called internally when the game switches from this state to a different one. Use this for cleaning up any objects
     * still in use.
     */
    public void onLeave()
    {
    }
}
