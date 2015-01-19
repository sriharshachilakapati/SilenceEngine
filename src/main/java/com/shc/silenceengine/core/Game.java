package com.shc.silenceengine.core;

import com.shc.silenceengine.audio.openal.ALContext;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.opengl.GL3Context;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.input.Mouse;
import com.shc.silenceengine.utils.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import static org.lwjgl.opengl.GL11.*;

/**
 * The basic class for all the games made with SilenceEngine. Every game
 * will simply extend this Game class, and call the start method to play.
 * <p>
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
 * <p>
 * Creating a game in SilenceEngine is as simple as that. This is the
 * skeleton of your game.
 *
 * @author Sri Harsha Chilakapati
 */
public class Game
{
    public static final String VERSION = "0.0.2";

    static
    {
        // We need to start AWT in Headless mode, Needed for AWT to work on OS X
        System.setProperty("java.awt.headless", "true");

        // Every exception occurs after SilenceException, even
        // the uncaught exceptions are thrown as runtime exceptions
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler()
        {
            @Override
            public void uncaughtException(Thread t, Throwable e)
            {
                try
                {
                    Writer result = new StringWriter();
                    PrintWriter printWriter = new PrintWriter(result);
                    e.printStackTrace(printWriter);

                    throw new SilenceException(result.toString());
                }
                catch (SilenceException ex)
                {
                    ex.printStackTrace();
                    System.exit(-1);
                }
            }
        });

        // Set target UPS
        setTargetUPS(60);
    }

    /**
     * Specifies the development status of the game. Before distributing
     * make sure to change this to false, leaving this enabled causes the
     * GLExceptions, if any, to be displayed to the user.
     */
    public static boolean development = true;

    // Is the game running?
    private static boolean running = false;

    // Game logic rate
    private static int ups       = 60;
    private static int targetUPS = 60;

    // Game frame rate
    private static int fps = 60;

    private static Batcher batcher;
    private static GameState gameState;

    /**
     * Initialize the Game. Loads the resources, and
     * sets the game states.
     */
    public void init()
    {
    }

    /**
     * Performs game logic. Also, it is a place to check
     * for input, collisions, what-not, everything except
     * rendering.
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
     * Handle the window-resize event. Used to set the view-port
     * and re-size the camera.
     */
    public void resize()
    {
    }

    /**
     * Properly disposes all the resources created in init method
     */
    public void dispose()
    {
    }

    /**
     * Starts the game. Initiates the game life-cycle and starts
     * the main game-loop.
     */
    public void start()
    {
        Logger.log("Initializing SilenceEngine version " + VERSION);

        Logger.log("Starting to load natives");

        // Load the natives
        NativesLoader.load();

        Logger.log("Natives loaded successfully");

        running = true;

        Logger.log("Initializing Display");

        // Create and show the display
        Display.create();
        Display.show();

        Logger.log("Initializing OpenAL context");

        // Initialize OpenAL
        ALContext.getInstance().init();

        Logger.log("Initialized OpenGL version " + glGetString(GL_VERSION));
        Logger.log("Initializing Game");

        // Initialize the Game
        init();

        Logger.log("Game initialized successfully, proceeding to the main loop");

        // GameLoop constants
        final double frameTime = 1.0/targetUPS;
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

        previousTime = TimeUtils.currentSeconds();

        while (true)
        {
            if (Display.isCloseRequested() || !isRunning())
                break;

            if (Display.wasResized())
            {
                GL3Context.viewport(0, 0, Display.getWidth(), Display.getHeight());
                resize();

                if (gameState != null)
                    gameState.resize();
            }

            currentTime = TimeUtils.currentSeconds();
            elapsed = currentTime - previousTime;

            lag += elapsed;

            while (lag > frameTime && skippedFrames < maxFrameSkips)
            {
                Keyboard.startEventFrame();
                Mouse.startEventFrame();

                update((float) frameTime);

                if (gameState != null)
                    gameState.update((float) frameTime);

                GameTimer.updateTimers((float) frameTime);

                Keyboard.clearEventFrame();
                Mouse.clearEventFrame();

                updatesProcessed++;
                lag -= frameTime;

                skippedFrames++;

                if (currentTime - lastUPSUpdate >= 1000)
                {
                    ups = updatesProcessed;
                    updatesProcessed = 0;
                    lastUPSUpdate = currentTime;
                }
            }

            float lagOffset = (float) (lag / frameTime);

            GL3Context.clear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            Texture.setActiveUnit(0);
            render(lagOffset, batcher);

            if (gameState != null)
                gameState.render(lagOffset, batcher);

            framesProcessed++;

            if (currentTime - lastFPSUpdate >= 1000)
            {
                fps = framesProcessed;
                framesProcessed = 0;
                lastFPSUpdate = currentTime;
            }

            Display.update();

            skippedFrames = 0;

            previousTime = currentTime;
        }

        Logger.log("Disposing the loaded resources");

        // Dispose the Batcher
        batcher.dispose();

        dispose();

        // Dispose OpenAL and Display
        ALContext.getInstance().dispose();
        Display.destroy();

        Logger.log("This game has been terminated successfully.");
    }

    /**
     * Kills the running game!
     */
    public static void end()
    {
        running = false;
    }

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
    public static int getFPS() { return fps; }

    /**
     * @return The target updates per second
     */
    public static int getTargetUPS()
    {
        return targetUPS;
    }

    /**
     * @return True if running, else false
     */
    public static boolean isRunning()
    {
        return running;
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
    }
}
