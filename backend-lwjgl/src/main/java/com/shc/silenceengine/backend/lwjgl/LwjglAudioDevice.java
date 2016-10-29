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

package com.shc.silenceengine.backend.lwjgl;

import com.shc.silenceengine.audio.AudioDevice;
import com.shc.silenceengine.audio.openal.ALBuffer;
import com.shc.silenceengine.backend.lwjgl.soundreaders.OggReader;
import com.shc.silenceengine.backend.lwjgl.soundreaders.WaveReader;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.io.DirectBuffer;
import com.shc.silenceengine.utils.TaskManager;
import com.shc.silenceengine.utils.functional.UniCallback;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.openal.ALC10.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class LwjglAudioDevice extends AudioDevice
{
    private long device;
    private long context;

    public LwjglAudioDevice()
    {
        device = alcOpenDevice((ByteBuffer) null);
        ALCCapabilities deviceCaps = ALC.createCapabilities(device);

        context = alcCreateContext(device, (IntBuffer) null);
        alcMakeContextCurrent(context);
        AL.createCapabilities(deviceCaps);

        SilenceEngine.eventManager.addDisposeHandler(this::cleanUp);
    }

    @Override
    public int alGenBuffers()
    {
        return AL10.alGenBuffers();
    }

    @Override
    public void alBufferData(int id, int format, DirectBuffer data, int frequency)
    {
        AL10.alBufferData(id, format, (ByteBuffer) data.nativeBuffer(), frequency);
    }

    @Override
    public void alDeleteBuffers(int... buffers)
    {
        for (int buffer : buffers)
            AL10.alDeleteBuffers(buffer);
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
        for (int source : sources)
            AL10.alDeleteSources(source);
    }

    @Override
    public void readToALBuffer(AudioFormat format, DirectBuffer data, UniCallback<ALBuffer> onDecoded, UniCallback<Throwable> onError)
    {
        try
        {
            if (!isSupported(format))
                throw new SilenceException("Error, cannot decode unsupported format");

            switch (format)
            {
                case OGG:
                {
                    OggReader reader = new OggReader(data);

                    TaskManager.runOnUpdate(() ->
                    {
                        ALBuffer alBuffer = new ALBuffer();
                        alBuffer.uploadData(new LwjglDirectBuffer(reader.getData()), reader.getFormat(), reader.getSampleRate());

                        onDecoded.invoke(alBuffer);
                    });

                    break;
                }

                case WAV:
                {
                    WaveReader reader = new WaveReader(data);

                    TaskManager.runOnUpdate(() ->
                    {
                        ALBuffer alBuffer = new ALBuffer();
                        alBuffer.uploadData(new LwjglDirectBuffer(reader.getData()), reader.getFormat(), reader.getSampleRate());

                        onDecoded.invoke(alBuffer);
                    });

                    break;
                }
            }
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
                return true;
            case MP3:
                return false;
            case WAV:
                return true;
            case WEBM:
                return false;
        }

        return false;
    }

    private void cleanUp()
    {
        alcCloseDevice(device);
        alcDestroyContext(context);
    }
}
