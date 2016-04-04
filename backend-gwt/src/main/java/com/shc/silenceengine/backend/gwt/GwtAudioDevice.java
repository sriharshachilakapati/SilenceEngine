package com.shc.silenceengine.backend.gwt;

import com.google.gwt.typedarrays.shared.ArrayBufferView;
import com.shc.gwtal.client.openal.AL10;
import com.shc.gwtal.client.openal.ALContext;
import com.shc.gwtal.client.openal.AudioDecoder;
import com.shc.gwtal.client.webaudio.AudioContextException;
import com.shc.silenceengine.audio.AudioDevice;
import com.shc.silenceengine.audio.openal.ALBuffer;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.io.DirectBuffer;

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
    public void readToALBuffer(AudioFormat format, DirectBuffer data, OnDecodeComplete onDecoded)
    {
        if (!isSupported(format))
            throw new SilenceException("Audio format " + format + " is not supported.");

        AudioDecoder.decodeAudio(((ArrayBufferView) data.nativeBuffer()).buffer(),
                alBufferID -> onDecoded.accept(new ALBuffer(alBufferID)),
                reason ->
                {
                    throw new SilenceException("Error decoding: " + reason);
                });
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
