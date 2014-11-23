package com.shc.silenceengine;

import com.shc.silenceengine.graphics.Shader;
import com.shc.silenceengine.utils.TimeUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import static org.lwjgl.opengl.GL11.*;

/**
 * The basic class for all the games made with SilenceEngine.
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

    public void init() {}
    public void update(long delta) {}
    public void render(long delta) {}
    public void resize() {}
    public void dispose() {}

    public void start()
    {
        running = true;

        Display.create();
        Shader.loadDefaultShader();
        Shader.DEFAULT.use();

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

            update(delta);
            render(delta);

            if (Display.wasResized())
                resize();

            lastTime = thisTime;

            Display.update();
        }

        dispose();
        Shader.DEFAULT.dispose();
        Display.destroy();
    }

    public static void end()
    {
        running = false;
    }
}
