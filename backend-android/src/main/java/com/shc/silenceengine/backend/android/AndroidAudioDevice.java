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

package com.shc.silenceengine.backend.android;

import com.shc.silenceengine.audio.AudioDevice;
import com.shc.silenceengine.audio.openal.ALBuffer;
import com.shc.silenceengine.io.DirectBuffer;
import com.shc.silenceengine.utils.functional.UniCallback;

/**
 * @author Sri Harsha Chilakapati
 */
public class AndroidAudioDevice extends AudioDevice
{
    @Override
    public int alGenBuffers()
    {
        return 0;
    }

    @Override
    public void alBufferData(int id, int format, DirectBuffer data, int frequency)
    {

    }

    @Override
    public void alDeleteBuffers(int... buffers)
    {

    }

    @Override
    public int alGetError()
    {
        return 0;
    }

    @Override
    public int alGenSources()
    {
        return 0;
    }

    @Override
    public void alSourcei(int id, int param, int value)
    {

    }

    @Override
    public void alSourcef(int id, int param, float value)
    {

    }

    @Override
    public void alSource3f(int id, int param, float v1, float v2, float v3)
    {

    }

    @Override
    public void alSourcePlay(int id)
    {

    }

    @Override
    public void alSourcePause(int id)
    {

    }

    @Override
    public void alSourceRewind(int id)
    {

    }

    @Override
    public void alSourceStop(int id)
    {

    }

    @Override
    public int alGetSourcei(int id, int parameter)
    {
        return 0;
    }

    @Override
    public void alDeleteSources(int... sources)
    {

    }

    @Override
    public void readToALBuffer(AudioFormat format, DirectBuffer data, UniCallback<ALBuffer> onDecoded)
    {

    }

    @Override
    public boolean isSupported(AudioFormat format)
    {
        return false;
    }
}
