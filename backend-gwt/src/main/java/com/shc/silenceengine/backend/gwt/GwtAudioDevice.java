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

package com.shc.silenceengine.backend.gwt;

import com.google.gwt.typedarrays.shared.ArrayBufferView;
import com.shc.gwtal.client.openal.AL;
import com.shc.gwtal.client.openal.AL10;
import com.shc.gwtal.client.openal.ALContext;
import com.shc.gwtal.client.openal.AudioDecoder;
import com.shc.gwtal.client.webaudio.AudioContextException;
import com.shc.silenceengine.audio.AudioDevice;
import com.shc.silenceengine.audio.openal.ALBuffer;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.io.DirectBuffer;
import com.shc.silenceengine.utils.functional.UniCallback;

/**
 * @author Sri Harsha Chilakapati
 */
public class GwtAudioDevice extends AudioDevice
{
    private ALContext context;

    public GwtAudioDevice()
    {
        try
        {
            context = ALContext.create();
            AL.setCurrentContext(context);
        }
        catch (AudioContextException e)
        {
            SilenceEngine.log.getRootLogger().error(e);
        }
    }

    @Override
    public int alGenBuffers()
    {
        return AL10.alGenBuffers();
    }

    @Override
    public void alBufferData(int id, int format, DirectBuffer data, int frequency)
    {
        AL10.alBufferData(id, format, ((ArrayBufferView) data.nativeBuffer()).buffer(), frequency);
    }

    @Override
    public void alDeleteBuffers(int... buffers)
    {
        AL10.alDeleteBuffers(buffers);
    }

    @Override
    public int alGetError()
    {
        return AL10.alGetError();
    }

    @Override
    public int alGenSources()
    {
        return AL10.alGenSources();
    }

    @Override
    public void alSourcei(int id, int param, int value)
    {
        AL10.alSourcei(id, param, value);
    }

    @Override
    public void alSourcef(int id, int param, float value)
    {
        AL10.alSourcef(id, param, value);
    }

    @Override
    public void alSource3f(int id, int param, float v1, float v2, float v3)
    {
        AL10.alSource3f(id, param, v1, v2, v3);
    }

    @Override
    public void alSourcePlay(int id)
    {
        AL10.alSourcePlay(id);
    }

    @Override
    public void alSourcePause(int id)
    {
        AL10.alSourcePause(id);
    }

    @Override
    public void alSourceRewind(int id)
    {
        AL10.alSourceRewind(id);
    }

    @Override
    public void alSourceStop(int id)
    {
        AL10.alSourceStop(id);
    }

    @Override
    public int alGetSourcei(int id, int parameter)
    {
        return AL10.alGetSourcei(id, parameter);
    }

    @Override
    public void alDeleteSources(int... sources)
    {
        AL10.alDeleteSources(sources);
    }

    @Override
    public void readToALBuffer(AudioFormat format, DirectBuffer data, UniCallback<ALBuffer> onDecoded, UniCallback<Throwable> onError)
    {
        try
        {
            if (!isSupported(format))
                throw new SilenceException("Audio format " + format + " is not supported.");

            AudioDecoder.decodeAudio(((ArrayBufferView) data.nativeBuffer()).buffer(),
                    alBufferID -> onDecoded.invoke(new ALBuffer(alBufferID)),
                    reason ->
                    {
                        throw new SilenceException("Error decoding: " + reason);
                    });
        }
        catch (Throwable e)
        {
            onError.invoke(e);
        }
    }

    @Override
    public boolean isSupported(AudioFormat format)
    {
        switch (format)
        {
            case OGG:
                return AudioDecoder.isSupported(AudioDecoder.FileFormat.OGG);
            case MP3:
                return AudioDecoder.isSupported(AudioDecoder.FileFormat.MP3);
            case WAV:
                return AudioDecoder.isSupported(AudioDecoder.FileFormat.WAV);
            case WEBM:
                return AudioDecoder.isSupported(AudioDecoder.FileFormat.WEBM);
        }

        return false;
    }
}
