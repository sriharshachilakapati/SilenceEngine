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

import com.shc.silenceengine.audio.openal.ALFormat;
import com.shc.silenceengine.core.SilenceException;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * <p> An interface that specifies how a sound reader should behave. There are a lot of formats for the sound resources,
 * and they should be decoded to the raw byte samples before they can be played by OpenAL. </p> <p> <p> This interface
 * allows you to extend the playback support for other formats by letting you write your own implementation. To play an
 * audio file, you need at least three things about it, namely the format of the samples, it's bit-rate, and the
 * samples. It is the job of the sound reader to decode this information from the resource file. </p> <p> <p> To allow
 * you to easily use such additional readers, this interface provides you with a way to plug those readers by using
 * file-name extensions to determine the file type. The requirement is that every class that implements this interface
 * should contain a constructor that accepts an <code>InputStream</code> as an argument. Then you need to register that
 * class before you begin loading the sound. </p> <p>
 * <pre>
 *     public static void register()
 *     {
 *         ISoundReader.register("myext1", MyextReader.class);
 *         ISoundReader.register("myext2", MyextReader.class);
 *     }
 * </pre>
 * <p> <p> This is how you register the readers, so that SilenceEngine can use them to read your sound resource file. To
 * ease it further, add the above method to your class and make a call to it before you start loading the resources.
 * That does mean that you have to register the readers before using {@link com.shc.silenceengine.core.ResourceLoader}
 * class. </p>
 *
 * @author Sri Harsha Chilakapati
 */
public interface ISoundReader
{
    /**
     * A mapping of ISoundReaders based on the extension.
     */
    static Map<String, Class<? extends ISoundReader>> registeredReaders = new HashMap<>();

    /**
     * Registers an extension to a class that is a sound reader.
     *
     * @param extension   The file name extension of the sound resource.
     * @param readerClass The class of the sound reader that reads that resource.
     *
     * @throws SilenceException If there is no constructor that accepts an InputStream.
     */
    public static void register(String extension, Class<? extends ISoundReader> readerClass)
    {
        try
        {
            // Check if there is a constructor that accepts an InputStream
            if (readerClass.getDeclaredConstructor(InputStream.class) == null)
                throw new SilenceException("The class " + readerClass.getName() + " doesn't have a constructor that accepts an InputStream");
        }
        catch (Exception e)
        {
            // Rethrow the exception as a runtime SilenceException
            SilenceException.reThrow(e);
        }

        // Add the extension and the class in the map.
        registeredReaders.put(extension.toLowerCase().trim(), readerClass);
    }

    /**
     * Creates a ISoundReader based on an extension and an InputStream
     *
     * @param extension The file name extension of the resource file.
     * @param resource  An InputStream to stream the data of the resource.
     *
     * @return The created ISoundReader instance.
     *
     * @throws SilenceException If there is no registered reader for this file type.
     */
    public static ISoundReader create(String extension, InputStream resource)
    {
        // Get the reader class from the registry
        Class<? extends ISoundReader> readerClass = registeredReaders.get(extension.toLowerCase().trim());

        try
        {
            // Get the constructor that accepts an InputStream and return a new instance.
            Constructor<? extends ISoundReader> constructor = readerClass.getDeclaredConstructor(InputStream.class);
            return constructor.newInstance(resource);
        }
        catch (Exception e)
        {
            // Failed to find a constructor. Only occurs if there is no such file type.
            SilenceException.reThrow(e);
        }

        return null;
    }

    /**
     * Method borrowed from LWJGL 3 demos, this converts stereo and mono data samples to the internal format of OpenAL.
     *
     * @param samples The Byte array of audio samples
     * @param stereo  Whether to convert to stereo audio
     *
     * @return The ByteBuffer containing fixed samples.
     */
    public static ByteBuffer convertAudioBytes(byte[] samples, boolean stereo)
    {
        ByteBuffer dest = ByteBuffer.allocateDirect(samples.length);
        dest.order(ByteOrder.nativeOrder());

        ByteBuffer src = ByteBuffer.wrap(samples);
        src.order(ByteOrder.LITTLE_ENDIAN);

        if (stereo)
        {
            ShortBuffer dest_short = dest.asShortBuffer();
            ShortBuffer src_short = src.asShortBuffer();

            while (src_short.hasRemaining())
                dest_short.put(src_short.get());
        }
        else
        {
            while (src.hasRemaining())
                dest.put(src.get());
        }

        dest.rewind();
        return dest;
    }

    /**
     * @return The decoded samples buffer.
     */
    public Buffer getData();

    /**
     * @return The sample rate, or the frequency, of the data samples.
     */
    public int getSampleRate();

    /**
     * @return The OpenAL compatible format of the data samples.
     */
    public ALFormat getFormat();
}
