package com.shc.silenceengine.tests;

import com.shc.silenceengine.audio.AudioDevice;
import com.shc.silenceengine.audio.Sound;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.io.FilePath;

/**
 * @author Sri Harsha Chilakapati
 */
public class SoundTest extends SilenceTest
{
    private Sound sound;

    @Override
    public void init()
    {
        SilenceEngine.io.getFileReader().readBinaryFile(FilePath.getResourceFile("test_resources/shoot.wav"), data ->
                SilenceEngine.audio.readToALBuffer(AudioDevice.AudioFormat.WAV, data, buffer ->
                        sound = new Sound(buffer)));
    }

    @Override
    public void update(float deltaTime)
    {
        if (Keyboard.isKeyTapped(Keyboard.KEY_ESCAPE))
            SilenceEngine.display.close();

        if (sound != null)
        {
            if (Keyboard.isKeyTapped(Keyboard.KEY_SPACE))
                sound.play();
        }
    }

    @Override
    public void dispose()
    {
        if (sound != null)
            sound.buffer.dispose();
    }
}
