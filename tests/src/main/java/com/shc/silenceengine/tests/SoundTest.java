/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2016 Sri Harsha Chilakapati
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.shc.silenceengine.tests;

import com.shc.silenceengine.audio.AudioDevice;
import com.shc.silenceengine.audio.Sound;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.input.Touch;
import com.shc.silenceengine.io.FilePath;

/**
 * @author Sri Harsha Chilakapati
 */
public class SoundTest extends SilenceTest
{
    private Sound sound1;
    private Sound sound2;

    private boolean playS1;

    @Override
    public void init()
    {
        SilenceEngine.io.getFileReader().readBinaryFile(FilePath.getResourceFile("test_resources/shoot.wav"), data ->
                SilenceEngine.audio.readToALBuffer(AudioDevice.AudioFormat.WAV, data, buffer ->
                        sound1 = new Sound(buffer)));

        SilenceEngine.io.getFileReader().readBinaryFile(FilePath.getResourceFile("test_resources/siren.ogg"), data ->
                SilenceEngine.audio.readToALBuffer(AudioDevice.AudioFormat.OGG, data, buffer ->
                        sound2 = new Sound(buffer)));
    }

    @Override
    public void update(float deltaTime)
    {
        if (Keyboard.isKeyTapped(Keyboard.KEY_ESCAPE))
            SilenceEngine.display.close();

        if (Keyboard.isKeyTapped(Keyboard.KEY_SPACE) || Touch.isFingerTapped(Touch.FINGER_0))
        {
            Sound sound = (playS1 = !playS1) ? sound1 : sound2;

            if (sound != null)
                sound.play();
        }
    }

    @Override
    public void dispose()
    {
        if (sound1 != null)
            sound1.buffer.dispose();

        if (sound2 != null)
            sound2.buffer.dispose();
    }
}
