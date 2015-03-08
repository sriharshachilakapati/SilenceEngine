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

package com.shc.silenceengine.core;

import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Graphics2D;
import com.shc.silenceengine.graphics.opengl.GL3Context;
import com.shc.silenceengine.utils.GameTimer;
import com.shc.silenceengine.utils.Logger;
import com.shc.silenceengine.utils.TimeUtils;

/**
 * <p>The basic class for all the games made with SilenceEngine. Every game will simply extend this Game class, and call
 * the start method to play.</p>
 *
 * <pre>
 *     public class MyGame extends Game
 *     {
 *         // Initialize the resources
 *         public void init() {}
 *
 *         // Update game logic
 *         public void update(float delta) {}
 *
 *         // Render to screen
 *         public void render(float delta, Batcher batcher) {}
 *
 *         // Handle window resize event
 *         public void resize() {}
 *
 *         // Dispose the resources
 *         public void dispose() {}
 *
 *         public static void main(String[] args)
 *         {
 *             new MyGame().start();
 *         }
 *     }
 * </pre>
 *
 * <p>Creating a game in SilenceEngine is as simple as that. This is the skeleton of your game.</p>
 *
 * @author Sri Harsha Chilakapati
 */
public class Game
{
    // Is the game running?
    private static boolean running = false;
    // Game logic rate
    private static int ups       = 60;
    private static int targetUPS = 60;
    // Game frame rate
    private static int fps = 60;
    private static Batcher   batcher;
    private static GameState gameState;
    // The game instance
    private static Game instance;
    /**
     * Specifies the development status of the game. Before distributing make sure to change this to false, leaving this
     * enabled causes the GLExceptions, if any, to be displayed to the user.
     */
    public static boolean development = true;

    /**
     * @return number of updates done in last second
     */
    public static int getUPS()
    {
        return ups;
    }

    /**
     * @return number of frames rendered in last second
     */
    public static int getFPS()
    {
        return fps;
    }

    /**
     * @return The target updates per second
     */
    public static int getTargetUPS()
    {
        return targetUPS;
    }

    /**
     * Sets the target logic speed of the Game.
     *
     * @param targetUPS The number of steps the game should try to make in a second
     */
    public static void setTargetUPS(int targetUPS)
    {
        Game.targetUPS = targetUPS;
    }

    /**
     * @return The Global Batcher of the Game
     */
    public static Batcher getBatcher()
    {
        return batcher;
    }

    /**
     * Sets the batcher to be passed to the render() method
     *
     * @param batcher The Batcher instance to use
     */
    public static void setBatcher(Batcher batcher)
    {
        Game.batcher = batcher;
    }

    public static GameState getGameState()
    {
        return gameState;
    }

    public static void setGameState(GameState gameState)
    {
        if (Game.gameState != null)
            Game.gameState.onLeave();

        Game.gameState = gameState;

        if (Game.gameState != null)
            Game.gameState.onEnter();

        Runtime.getRuntime().gc();
    }

    public static Graphics2D getGraphics2D()
    {
        return Graphics2D.getInstance();
    }

    public static long getUsedMemory()
    {
        return getTotalMemory() - getFreeMemory();
    }

    public static long getTotalMemory()
    {
        return Runtime.getRuntime().totalMemory();
    }

    public static long getFreeMemory()
    {
        return Runtime.getRuntime().freeMemory();
    }

    /**
     * Starts the game. Initiates the game life-cycle and starts the main game-loop.
     */
    public void start()
    {
        instance = this;

        // Initialize SilenceEngine
        SilenceEngine.getInstance().init();

        // Initialize the Game
        Logger.log("Initializing the Game resources");
        init();
        Runtime.getRuntime().gc();
        Logger.log("Game initialized successfully, proceeding to the main loop");

        // GameLoop constants
        final double second = TimeUtils.convert(1, TimeUtils.Unit.SECONDS, TimeUtils.getDefaultTimeUnit());
        final double frameTime = second / targetUPS;
        final double maxFrameSkips = 10;

        double currentTime;
        double previousTime;
        double elapsed;

        double lag = 0;

        double lastUPSUpdate = 0;
        double lastFPSUpdate = 0;

        int updatesProcessed = 0;
        int framesProcessed = 0;
        int skippedFrames = 0;

        previousTime = TimeUtils.currentTime();

        running = true;

        // The Game Loop
        while (true)
        {
            // Start a frame in the game loop
            SilenceEngine.getInstance().beginFrame();

            if (Display.isCloseRequested() || !isRunning())
            {
                Game.end();
                break;
            }

            if (Display.wasResized())
            {
                GL3Context.viewport(0, 0, Display.getWidth(), Display.getHeight());
                Graphics2D.getInstance().getCamera().initProjection(Display.getWidth(), Display.getHeight());
                resize();

                if (gameState != null)
                    gameState.resize();
            }

            currentTime = TimeUtils.currentTime();
            elapsed = currentTime - previousTime;

            lag += elapsed;

            while (lag > frameTime && skippedFrames < maxFrameSkips)
            {
                if (Display.wasDirty())
                    dirtyDisplay();

                // Input needs to be updated even faster!
                SilenceEngine.input.beginFrame();
                {
                    update((float) frameTime);

                    if (gameState != null)
                        gameState.update((float) frameTime);

                    GameTimer.updateTimers((float) frameTime);
                }
                SilenceEngine.input.endFrame();

                updatesProcessed++;
                lag -= frameTime;

                skippedFrames++;

                if (currentTime - lastUPSUpdate >= second)
                {
                    ups = updatesProcessed;
                    updatesProcessed = 0;
                    lastUPSUpdate = currentTime;
                }
            }

            float lagOffset = (float) (lag / frameTime);
            render(lagOffset, batcher);

            if (gameState != null)
                gameState.render(lagOffset, batcher);

            framesProcessed++;

            if (currentTime - lastFPSUpdate >= second)
            {
                fps = framesProcessed;
                framesProcessed = 0;
                lastFPSUpdate = currentTime;
            }

            SilenceEngine.getInstance().endFrame();
            Display.update();

            skippedFrames = 0;

            previousTime = currentTime;
        }

        Game.end();
    }

    /**
     * Kills the running game!
     */
    public static void end()
    {
        if (!running)
        {
            Logger.log("Disposing the Game resources");

            batcher.dispose();
            instance.dispose();

            SilenceEngine.getInstance().dispose();

            Logger.log("This game has been terminated successfully");
            System.exit(0);
        }

        running = false;
    }

    /**
     * Properly disposes all the resources created in init method
     */
    public void dispose()
    {
    }

    /**
     * @return True if running, else false
     */
    public static boolean isRunning()
    {
        return running;
    }

    /**
     * Initialize the Game. Loads the resources, and sets the game states.
     */
    public void init()
    {
    }

    /**
     * Performs game logic. Also, it is a place to check for input, collisions, what-not, everything except rendering.
     *
     * @param delta It is the time taken by the last update (in ms)
     */
    public void update(float delta)
    {
    }

    /**
     * Renders the game to the OpenGL Scene.
     *
     * @param delta   It is the time taken by the last render (in ms)
     * @param batcher The Batcher to batch OpenGL calls
     */
    public void render(float delta, Batcher batcher)
    {
    }

    /**
     * Handle the window-resize event. Used to set the view-port and re-size the camera.
     */
    public void resize()
    {
    }

    public void dirtyDisplay()
    {
    }
}
