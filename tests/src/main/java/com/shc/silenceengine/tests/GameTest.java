package com.shc.silenceengine.tests;

import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.logging.Logger;

/**
 * @author Sri Harsha Chilakapati
 */
public class GameTest extends SilenceTest
{
    private Logger logger;

    @Override
    public void init()
    {
        logger = SilenceEngine.log.getLogger("GameTest");

        SilenceEngine.display.setTitle("GameTest");

        logger.info("Test initialized successfully");
    }

    @Override
    public void update(float delta)
    {
        logger.info("Update event!! Delta is " + delta);

        if (Keyboard.isKeyTapped(Keyboard.KEY_ESCAPE))
            SilenceEngine.display.close();
    }

    @Override
    public void render(float delta)
    {
        logger.info("Render event!! Delta is " + delta);
    }

    @Override
    public void resized()
    {
        logger.info("Resize event!! Size is " + SilenceEngine.display.getWidth() + "x" + SilenceEngine.display.getHeight());
    }

    @Override
    public void dispose()
    {
        logger.info("Dispose event!! Success.");
    }
}
