/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2015 Sri Harsha Chilakapati
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

package com.shc.silenceengine.audio;

import com.shc.silenceengine.audio.openal.ALBuffer;
import com.shc.silenceengine.audio.openal.ALSource;
import com.shc.silenceengine.utils.FileUtils;

import java.io.InputStream;

import static org.lwjgl.openal.AL10.*;

/**
 * Represents a sound resource that is ready to play. This class uses OpenAL sources and buffers underneath using the
 * default listener. This class is very basic, but can be extended.
 *
 * @author Sri Harsha Chilakapati
 */
public class Sound
{
    private ALSource source;
    private ALBuffer buffer;

    private boolean looping;

    /**
     * Creates a new sound object from a resource file name. The resource can be a file, or can be embedded in the JAR
     * directly.
     *
     * @param filename The file name of the resource file.
     */
    public Sound(String filename)
    {
        this(FileUtils.getResource(filename), FileUtils.getExtension(filename));
    }

    /**
     * Creates a new sound object from an input stream and an extension.
     *
     * @param is     The InputStream to read the resource data from.
     * @param format The extension of the file-name to decide the format of the reader.
     */
    public Sound(InputStream is, String format)
    {
        // Create a new ALBuffer
        buffer = new ALBuffer();

        // Create the reader and upload the data to the buffer
        ISoundReader reader = ISoundReader.create(format.toLowerCase(), is);
        buffer.uploadData(reader.getData(), reader.getFormat(), reader.getSampleRate());

        // Create the source and attach the buffer
        source = new ALSource();
        source.attachBuffer(buffer);

        looping = false;
    }

    /**
     * Sets the volume of this sound, a.k.a the gain of the source.
     *
     * @param volume The volume of the sound.
     */
    public void setVolume(float volume)
    {
        source.setParameter(AL_GAIN, volume);
    }

    /**
     * Starts playing this sound. No action will be taken if it was already playing or looping.
     */
    public void play()
    {
        ALSource.State state = source.getState();

        if (state == ALSource.State.PLAYING || state == ALSource.State.LOOPING)
            return;

        source.play();
    }

    /**
     * Pauses the sound playback. No action will be taken if the sound is already paused.
     */
    public void pause()
    {
        ALSource.State state = source.getState();

        if (state == ALSource.State.PAUSED)
            return;

        source.pause();
    }

    /**
     * Stops the sound playback. No action will be taken if the sound is already stopped.
     */
    public void stop()
    {
        if (getState() == ALSource.State.STOPPED)
            return;

        source.stop();
    }

    /**
     * @return The current state of the underlying source.
     */
    public ALSource.State getState()
    {
        return source.getState();
    }

    /**
     * Disposes this sound, and it's resources.
     */
    public void dispose()
    {
        source.dispose();
        buffer.dispose();
    }

    /**
     * @return The ALSource instance that this sound is using.
     */
    public ALSource getSource()
    {
        return source;
    }

    /**
     * @return The ALBuffer that stores this sound's data samples.
     */
    public ALBuffer getBuffer()
    {
        return buffer;
    }

    /**
     * @return True if looping, else false.
     */
    public boolean isLooping()
    {
        return looping;
    }

    /**
     * Sets the looping property of this sound. The parameter will take effect immediately.
     *
     * @param looping The value of the looping property.
     */
    public void setLooping(boolean looping)
    {
        this.looping = looping;
        source.setParameter(AL_LOOPING, looping);
    }
}
