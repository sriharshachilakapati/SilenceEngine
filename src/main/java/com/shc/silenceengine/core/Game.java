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

import com.shc.silenceengine.backend.lwjgl3.Lwjgl3Starter;
import com.shc.silenceengine.backend.lwjgl3.glfw.GLFW3;
import com.shc.silenceengine.core.gameloops.FixedCatchingUpGameLoop;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.utils.Logger;
import org.lwjgl.Version;
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
    private GameState gameState;
    private GameLoop  gameLoop;

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
        return instance.gameLoop.getUpdatesPerSecond();
    }

    /**
     * @return number of frames rendered in last second
     */
    public static int getFPS()
    {
        return instance.gameLoop.getFramesPerSecond();
    }

    /**
     * @return The target updates per second
     */
    public static int getTargetUPS()
    {
        if (getInstance().gameLoop instanceof FixedCatchingUpGameLoop)
            ((FixedCatchingUpGameLoop) getInstance().gameLoop).getTargetUpdatesPerSecond();

        return Integer.MAX_VALUE;
    }

    /**
     * Sets the target logic speed of the Game.
     *
     * @param targetUPS The number of steps the game should try to make in a second
     */
    public static void setTargetUPS(int targetUPS)
    {
        if (getInstance().gameLoop instanceof FixedCatchingUpGameLoop)
            ((FixedCatchingUpGameLoop) getInstance().gameLoop).setTargetUpdatesPerSecond(targetUPS);
    }

    /**
     * Returns the current active game state.
     *
     * @return The current active game state.
     */
    public static GameState getGameState()
    {
        return getInstance().gameState;
    }

    /**
     * Sets the current game state.
     *
     * @param gameState The new game state to be marked as active.
     */
    public static void setGameState(GameState gameState)
    {
        if (getGameState() != null)
            getGameState().onLeave();

        Game.getInstance().gameState = gameState;

        if (getGameState() != null)
            getGameState().onEnter();

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
        return getInstance().gameLoop.isRunning();
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
     * Returns the game loop used by this game.
     *
     * @return The GameLoop that is running this game.
     */
    public static GameLoop getGameLoop()
    {
        return getInstance().gameLoop;
    }

    /**
     * Kills the running game!
     */
    public static void end()
    {
        if (getInstance().gameLoop.isRunning())
            getInstance().gameLoop.stop();
    }

    /**
     * Starts the game. Initiates the game life-cycle and starts the main game-loop. This uses the
     * {@link FixedCatchingUpGameLoop} with the max frameskip of 10 and updates the logic at 60 times per second.
     */
    public void start()
    {
        start(new FixedCatchingUpGameLoop()
                .setMaxSkippedFrames(10)
                .setTargetUpdatesPerSecond(60));
    }

    /**
     * Starts the game. Initiates the game life-cycle and starts the main game-loop.
     *
     * @param gameLoop The GameLoop implementation to be used.
     */
    public void start(GameLoop gameLoop)
    {
        instance = this;
        gameLoop.setGame(this);
        this.gameLoop = gameLoop;

        // Load the natives
        Logger.info("Initializing LWJGL library. Extracting natives.");
        Lwjgl3Starter.start();  // For now, will be refactored soon

        // LWJGL configuration
        Configuration.DEBUG.set(DEVELOPMENT);
        Configuration.DISABLE_CHECKS.set(!DEVELOPMENT);
        Configuration.DEBUG_STREAM.set(true);

        // Copy LWJGL logs to the logger
        Configuration.setDebugStreamConsumer((logMessage) ->
        {
            logMessage = logMessage.trim().replaceAll("[\\r\\n]", "");

            if (logMessage.contains("Failed") || logMessage.contains("[GL]") || logMessage.contains("[AL]") || logMessage.contains("[CL]"))
                Logger.warn(logMessage);
            else
                Logger.info(logMessage);
        });

        Logger.info("LWJGL version " + Version.getVersion() + " is initialised");

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

        gameLoop.start();
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
