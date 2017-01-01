/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 Sri Harsha Chilakapati
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

package com.shc.silenceengine.core;

/**
 * @author Sri Harsha Chilakapati
 */
public abstract class Game
{
    public static boolean DEVELOPMENT = true;

    protected GameState gameState;

    public Game()
    {
        SilenceEngine.eventManager.addUpdateHandler(this::doUpdate);
        SilenceEngine.eventManager.addRenderHandler(this::doRender);
        SilenceEngine.eventManager.addResizeHandler(this::doResized);
        SilenceEngine.eventManager.addDisposeHandler(this::doDispose);
    }

    private void doDispose()
    {
        if (gameState != null)
            gameState.onLeave();

        dispose();
    }

    private void doResized()
    {
        resized();

        if (gameState != null)
            gameState.resized();
    }

    private void doRender(float delta)
    {
        render(delta);

        if (gameState != null)
            gameState.render(delta);
    }

    private void doUpdate(float deltaTime)
    {
        update(deltaTime);

        if (gameState != null)
            gameState.update(deltaTime);
    }

    public void init()
    {
    }

    public void update(float deltaTime)
    {
    }

    public void render(float delta)
    {
    }

    public void dispose()
    {
    }

    public void resized()
    {
    }

    public GameState getGameState()
    {
        return gameState;
    }

    public void setGameState(GameState gameState)
    {
        if (this.gameState != null)
            this.gameState.onLeave();

        this.gameState = gameState;

        if (this.gameState != null)
            this.gameState.onEnter();
    }
}
