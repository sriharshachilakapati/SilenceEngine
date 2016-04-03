package com.shc.silenceengine.tests;

import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.opengl.GLContext;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.input.Touch;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.logging.Logger;

/**
 * @author Sri Harsha Chilakapati
 */
public class TestGame extends Game
{
    private Logger logger;

    @Override
    public void init()
    {
        logger = SilenceEngine.log.getLogger("Test");

        if (SilenceEngine.display.getPlatform() == SilenceEngine.Platform.HTML5)
            SilenceEngine.display.setIcon(FilePath.getExternalFile("http://silenceengine.goharsha.com/img/favicon.ico"));
        else
            SilenceEngine.display.setIcon(FilePath.getExternalFile("F:\\source\\img\\logo_512x512.png"));

        SilenceEngine.display.setTitle("Hello World!!");

        if (SilenceEngine.display.getPlatform() != SilenceEngine.Platform.HTML5)
            SilenceEngine.input.setSimulateTouch(true);
    }

    @Override
    public void update(float deltaTime)
    {
        if (Keyboard.isKeyTapped(Keyboard.KEY_ESCAPE))
            SilenceEngine.display.close();

        if (Keyboard.isKeyTapped(Keyboard.KEY_ENTER))
            SilenceEngine.log.getRootLogger().info("Enter tapped");

        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
            SilenceEngine.log.getRootLogger().info("Space down");

        if (Keyboard.isKeyTapped(Keyboard.KEY_F))
            SilenceEngine.display.setFullscreen(!SilenceEngine.display.isFullscreen());

        if (Touch.isFingerDown(Touch.FINGER_0))
            logger.info("Finger 0 down!! Position: " + Touch.getFingerPosition(Touch.FINGER_0));

        if (Touch.isFingerTapped(Touch.FINGER_1))
            logger.info("Finger 1 tapped!! Position: " + Touch.getFingerPosition(Touch.FINGER_1));
    }

    @Override
    public void render(float delta)
    {
        GLContext.clearColor(Color.CORN_FLOWER_BLUE);
    }
}
