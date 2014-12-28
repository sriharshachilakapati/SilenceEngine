package com.shc.silenceengine.tests;

import com.shc.silenceengine.audio.Sound;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.input.Keyboard;

/**
 * @author Sri Harsha Chilakapati
 */
public class SoundTest extends Game
{
    private Sound music;

    public void init()
    {
        music = new Sound("resources/music.wav");
        music.setLooping(true);
        music.play();
    }

    public void update(float delta)
    {
        if (Keyboard.isClicked(Keyboard.KEY_ESCAPE))
            end();
    }

    public void dispose()
    {
        music.dispose();
    }

    public static void main(String[] args)
    {
        new SoundTest().start();
    }
}
