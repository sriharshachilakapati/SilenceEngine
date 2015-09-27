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

import com.shc.silenceengine.core.glfw.GLFW3;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Graphics2D;
import com.shc.silenceengine.graphics.opengl.GL3Context;
import com.shc.silenceengine.utils.GameTimer;
import com.shc.silenceengine.utils.Logger;
import com.shc.silenceengine.utils.NativesLoader;
import com.shc.silenceengine.utils.TimeUtils;
import org.lwjgl.system.Configuration;

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
public class Game implements IUpdatable
{
    // Is the game running?
    private static boolean running = false;

    // Game logic rate
    private static int ups       = 60;
    private static int targetUPS = 60;

    // Game frame rate
    private static int fps = 60;

    private static GameState gameState;

    // The game instance
    private static Game instance;

    /**
     * Specifies the development status of the game. Before distributing make sure to change this to false, leaving this
     * enabled causes the GLExceptions, if any, to be displayed to the user.
     */
    public static boolean DEVELOPMENT = true;

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
     * Returns the current active game state.
     *
     * @return The current active game state.
     */
    public static GameState getGameState()
    {
        return gameState;
    }

    /**
     * Sets the current game state.
     *
     * @param gameState The new game state to be marked as active.
     */
    public static void setGameState(GameState gameState)
    {
        if (Game.gameState != null)
            Game.gameState.onLeave();

        Game.gameState = gameState;

        if (Game.gameState != null)
            Game.gameState.onEnter();

        Runtime.getRuntime().gc();
    }

    /**
     * Returns the memory used in bytes. This is calculated by subtracting free memory from the total memory.
     *
     * @return The used memory in bytes.
     */
    public static long getUsedMemory()
    {
        return getTotalMemory() - getFreeMemory();
    }

    /**
     * Returns the total memory available to the JVM process. This is measured in bytes.
     *
     * @return The total memory in bytes.
     */
    public static long getTotalMemory()
    {
        return Runtime.getRuntime().totalMemory();
    }

    /**
     * Returns the available / free memory of the JVM. This is measured in bytes.
     *
     * @return The free memory in bytes.
     */
    public static long getFreeMemory()
    {
        return Runtime.getRuntime().freeMemory();
    }

    /**
     * @return True if running, else false
     */
    public static boolean isRunning()
    {
        return running;
    }

    /**
     * Returns the instance of the Game class that is currently running.
     *
     * @return The instance of the game class.
     */
    public static Game getInstance()
    {
        return instance;
    }

    /**
     * Kills the running game!
     */
    public static void end()
    {
        if (!running)
        {
            Logger.info("Disposing the Game resources");

            instance.dispose();

            SilenceEngine.getInstance().dispose();

            Logger.info("This game has been terminated successfully");
            System.exit(0);
        }

        running = false;
    }

    /**
     * Starts the game. Initiates the game life-cycle and starts the main game-loop.
     */
    public void start()
    {
        instance = this;

        // Load the natives
        NativesLoader.loadLWJGL();

        // LWJGL configuration
        Configuration.DEBUG.set(DEVELOPMENT);
        Configuration.DISABLE_CHECKS.set(!DEVELOPMENT);
        Configuration.DEBUG_STREAM.set(true);

        // Copy LWJGL logs to the logger
        Configuration.setDebugStreamConsumer((logMessage) ->
        {
            if (logMessage.contains("Failed") || logMessage.contains("[GL]") || logMessage.contains("[AL]") || logMessage.contains("[CL]"))
                Logger.warn(logMessage);
            else
                Logger.info(logMessage);
        });

        // Initialize GLFW
        if (!GLFW3.init())
            throw new SilenceException("Error initializing GLFW. Your system is unsupported.");

        // Default Display properties
        Display.setSize(800, 600);
        Display.centerOnScreen();

        // The preInit() method, called before anything else is loaded
        preInit();

        // Initialize SilenceEngine
        SilenceEngine.getInstance().init();

        // Initialize the Game
        Logger.info("Initializing the Game resources");
        init();
        Runtime.getRuntime().gc();
        Logger.info("Game initialized successfully, proceeding to the main loop");

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
                {
                    // End the old frame and start a new frame
                    SilenceEngine.getInstance().endFrame();
                    SilenceEngine.getInstance().beginFrame();
                }

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
            render(lagOffset, SilenceEngine.graphics.getBatcher());

            if (gameState != null)
                gameState.render(lagOffset, SilenceEngine.graphics.getBatcher());

            framesProcessed++;

            if (currentTime - lastFPSUpdate >= second)
            {
                fps = framesProcessed;
                framesProcessed = 0;
                lastFPSUpdate = currentTime;
            }

            SilenceEngine.getInstance().endFrame();

            skippedFrames = 0;

            previousTime = currentTime;
        }

        Game.end();
    }

    /**
     * This method is invoked even before the engine is loaded. Only the LWJGL natives are loaded.
     */
    public void preInit()
    {
    }

    /**
     * Initialize the Game. Loads the resources, and sets the game states.
     */
    public void init()
    {
    }

    /**
     * Handle the window-resize event. Used to set the view-port and re-size the camera.
     */
    public void resize()
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
     * Properly disposes all the resources created in init method
     */
    public void dispose()
    {
    }
}
