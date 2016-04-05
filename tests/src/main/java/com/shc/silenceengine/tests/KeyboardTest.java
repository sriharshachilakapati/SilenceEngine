package com.shc.silenceengine.tests;

import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.logging.Logger;

/**
 * @author Sri Harsha Chilakapati
 */
public class KeyboardTest extends SilenceTest
{
    private Logger logger;

    @Override
    public void init()
    {
        logger = SilenceEngine.log.getLogger("KeyboardTest");

        SilenceEngine.display.setTitle("KeyboardTest");

        logger.info("Test initialized successfully");
    }

    @Override
    public void update(float delta)
    {
        if (Keyboard.isKeyTapped(Keyboard.KEY_ESCAPE))
            SilenceEngine.display.close();

        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
            logger.info("SPACE key down");

        if (Keyboard.isKeyTapped(Keyboard.KEY_ENTER))
            logger.info("ENTER key tapped");
    }

    @Override
    public void dispose()
    {
        logger.info("Dispose event!! Success.");
    }
}
