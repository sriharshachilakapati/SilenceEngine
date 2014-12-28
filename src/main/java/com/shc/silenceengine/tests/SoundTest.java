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
    private Sound audio;

    public void init()
    {
        music = new Sound("resources/music.wav");
        music.setLooping(true);
        music.play();

        audio = new Sound("resources/song.ogg");
    }

    public void update(float delta)
    {
        if (Keyboard.isClicked(Keyboard.KEY_ESCAPE))
            end();

        if (Keyboard.isClicked(Keyboard.KEY_SPACE))
            audio.play();
    }

    public void dispose()
    {
        music.dispose();
        audio.dispose();
    }

    public static void main(String[] args)
    {
        new SoundTest().start();
    }
}
