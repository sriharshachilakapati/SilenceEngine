package com.shc.silenceengine;

import com.shc.silenceengine.graphics.Shader;
import com.shc.silenceengine.utils.TimeUtils;

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
 *         public void update(long delta) {}
 *
 *         // Render to screen
 *         public void render(long delta) {}
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
    static
    {
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

        // Load the natives
        NativesLoader.load();
    }

    private static boolean running = false;

    /**
     * Initialize the Game. Loads the resources, and
     * sets the game states.
     */
    public void init() {}

    /**
     * Performs game logic. Also, it is a place to check
     * for input, collisions, what-not, everything except
     * rendering.
     *
     * @param delta It is the time taken by the last update (in ms)
     */
    public void update(long delta) {}

    /**
     * Renders the game to the OpenGL Scene.
     *
     * @param delta It is the time taken by the last render (in ms)
     */
    public void render(long delta) {}

    /**
     * Handle the window-resize event. Used to set the view-port
     * and re-size the camera.
     */
    public void resize() {}

    /**
     * Properly disposes all the resources created in init method
     */
    public void dispose() {}

    /**
     * Starts the game. Initiates the game life-cycle and starts
     * the main game-loop.
     */
    public void start()
    {
        running = true;

        Display.create();
        Shader.loadDefaultShader();

        init();

        Display.show();

        long lastTime = TimeUtils.currentMillis();
        long thisTime;

        long delta;

        while (running)
        {
            if (Display.isCloseRequested())
                break;

            glClear(GL_COLOR_BUFFER_BIT);

            thisTime = TimeUtils.currentMillis();
            delta    = thisTime - lastTime;

            // Apply Shaders
            Shader.CURRENT.use();

            update(delta);
            render(delta);

            // Unbind Shaders
            Shader.unbind();

            if (Display.wasResized())
                resize();

            lastTime = thisTime;

            Display.update();
        }

        dispose();
        Shader.DEFAULT.dispose();
        Display.destroy();
    }

    /**
     * Kills the running game!
     */
    public static void end()
    {
        running = false;
    }
}
