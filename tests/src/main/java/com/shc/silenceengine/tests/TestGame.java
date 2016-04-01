package com.shc.silenceengine.tests;

import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.io.FilePath;

/**
 * @author Sri Harsha Chilakapati
 */
public class TestGame extends Game
{
    @Override
    public void init()
    {
        if (SilenceEngine.display.getPlatform() == SilenceEngine.Platform.HTML5)
            SilenceEngine.display.setIcon(FilePath.getExternalFile("http://silenceengine.goharsha.com/img/favicon.ico"));
        else
            SilenceEngine.display.setIcon(FilePath.getExternalFile("F:\\source\\img\\logo_512x512.png"));

        SilenceEngine.display.setTitle("Hello World!!");
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
    }
}
