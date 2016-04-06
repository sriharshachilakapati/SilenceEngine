package com.shc.silenceengine.tests;

import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.input.Touch;
import com.shc.silenceengine.logging.Logger;

/**
 * @author Sri Harsha Chilakapati
 */
public class TouchTest extends SilenceTest
{
    private Logger logger;

    @Override
    public void init()
    {
        logger = SilenceEngine.log.getLogger("TouchTest");

        // Touch inputs needs to be simulated if we want to convert mouse events as
        // touch events. The touch screen work even if we are not touching it.
        SilenceEngine.input.setSimulateTouch(true);

        SilenceEngine.display.setTitle("TouchTest");
    }

    @Override
    public void update(float delta)
    {
        if (Keyboard.isKeyTapped(Keyboard.KEY_ESCAPE))
            SilenceEngine.display.close();

        if (Touch.isFingerTapped(Touch.FINGER_0))
            logger.info("Finger 0 tapped");

        if (Touch.isFingerDown(Touch.FINGER_1))
            logger.info("Finger 1 is down");
    }
}
