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

import com.shc.androidopenal.AL;
import com.shc.androidopenal.ALC;
import com.shc.androidopenal.ALCcontext;
import com.shc.androidopenal.ALCdevice;
import com.shc.silenceengine.audio.AudioDevice;
import com.shc.silenceengine.audio.openal.ALBuffer;
import com.shc.silenceengine.backend.android.soundreaders.OggReader;
import com.shc.silenceengine.backend.android.soundreaders.WavReader;
import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.io.DirectBuffer;
import com.shc.silenceengine.io.PrimitiveSize;
import com.shc.silenceengine.utils.TaskManager;
import com.shc.silenceengine.utils.functional.UniCallback;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static com.shc.silenceengine.audio.AudioDevice.Constants.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class AndroidAudioDevice extends AudioDevice
{
    // List of all the sources, and the paused sources. The sources will be paused on out of focus,
    // and will be resumed automatically on getting focus back.
    private final List<Integer> sources       = new ArrayList<>();
    private final List<Integer> pausedSources = new ArrayList<>();

    // The temp buffer is used to store the result from AndroidOpenAL.
    private final IntBuffer temp = ByteBuffer.allocateDirect(PrimitiveSize.INT)
            .order(ByteOrder.nativeOrder())
            .asIntBuffer();

    public AndroidAudioDevice()
    {
        ALCdevice device = ALC.alcOpenDevice();
        ALCcontext context = ALC.alcCreateContext(device, null);

        ALC.alcMakeContextCurrent(context);
    }

    @Override
    public int alGenBuffers()
    {
        IntBuffer intBuffer = ByteBuffer.allocateDirect(PrimitiveSize.INT).order(ByteOrder.nativeOrder()).asIntBuffer();
        AL.alGenBuffers(1, intBuffer);
        return intBuffer.get(0);
    }

    @Override
    public void alBufferData(int id, int format, DirectBuffer data, int frequency)
    {
        AL.alBufferData(id, format, (Buffer) data.nativeBuffer(), data.sizeBytes(), frequency);
    }

    @Override
    public void alDeleteBuffers(int... buffers)
    {
        DirectBuffer directBuffer = DirectBuffer.wrap(buffers);
        AL.alDeleteBuffers(buffers.length, ((ByteBuffer) directBuffer.nativeBuffer()).asIntBuffer());
        DirectBuffer.free(directBuffer);
    }

    @Override
    public int alGetError()
    {
        return AL.alGetError();
    }

    @Override
    public int alGenSources()
    {
        IntBuffer intBuffer = ByteBuffer.allocateDirect(PrimitiveSize.INT).order(ByteOrder.nativeOrder()).asIntBuffer();
        AL.alGenSources(1, intBuffer);

        int source = intBuffer.get(0);
        sources.add(source);

        return source;
    }

    @Override
    public void alSourcei(int id, int param, int value)
    {
        AL.alSourcei(id, param, value);
    }

    @Override
    public void alSourcef(int id, int param, float value)
    {
        AL.alSourcef(id, param, value);
    }

    @Override
    public void alSource3f(int id, int param, float v1, float v2, float v3)
    {
        AL.alSource3f(id, param, v1, v2, v3);
    }

    @Override
    public void alSourcePlay(int id)
    {
        AL.alSourcePlay(id);
    }

    @Override
    public void alSourcePause(int id)
    {
        AL.alSourcePause(id);
    }

    @Override
    public void alSourceRewind(int id)
    {
        AL.alSourceRewind(id);
    }

    @Override
    public void alSourceStop(int id)
    {
        AL.alSourceStop(id);
    }

    @Override
    public int alGetSourcei(int id, int parameter)
    {
        IntBuffer intBuffer = ByteBuffer.allocateDirect(PrimitiveSize.INT).order(ByteOrder.nativeOrder()).asIntBuffer();
        AL.alGetSourcei(id, parameter, intBuffer);
        return intBuffer.get(0);
    }

    @Override
    public void alDeleteSources(int... sources)
    {
        DirectBuffer directBuffer = DirectBuffer.wrap(sources);
        AL.alDeleteSources(sources.length, ((ByteBuffer) directBuffer.nativeBuffer()).asIntBuffer());
        DirectBuffer.free(directBuffer);

        for (int i : sources)
            this.sources.remove((Integer) i);
    }

    @Override
    public void readToALBuffer(AudioFormat format, DirectBuffer data, UniCallback<ALBuffer> onDecoded, UniCallback<Throwable> onError)
    {
        try
        {
            if (!isSupported(format))
                throw new SilenceException("Cannot parse sound. The format is unsupported: " + format);

            if (format == AudioFormat.WAV)
                new Thread(() ->
                {
                    WavReader reader = new WavReader(data);

                    ALBuffer alBuffer = new ALBuffer();
                    alBuffer.uploadData(new AndroidDirectBuffer(reader.data), reader.alFormat, reader.sampleRate);

                    TaskManager.runOnUpdate(() -> onDecoded.invoke(alBuffer));
                }).start();

            else if (format == AudioFormat.OGG)
                new Thread(() ->
                {
                    OggReader reader = new OggReader(data);

                    ALBuffer alBuffer = new ALBuffer();
                    alBuffer.uploadData(new AndroidDirectBuffer(reader.getData()), reader.getFormat(), reader.getSampleRate());

                    TaskManager.runOnUpdate(() -> onDecoded.invoke(alBuffer));
                }).start();
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
            case WAV:
                return true;

            case OGG:
                return true;
        }

        return false;
    }

    public void onFocusLost()
    {
        pausedSources.clear();

        for (int source : sources)
        {
            AL.alGetSourcei(source, AL_SOURCE_STATE, temp);

            // Pause the source explicitly if it is looping or playing
            if (temp.get(0) == AL_PLAYING || temp.get(0) == AL_LOOPING)
            {
                pausedSources.add(source);
                alSourcePause(source);
            }
        }
    }

    public void onFocusGain()
    {
        for (int source : pausedSources)
            alSourcePlay(source);

        pausedSources.clear();
    }
}
