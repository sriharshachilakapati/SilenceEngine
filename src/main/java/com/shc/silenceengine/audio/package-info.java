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

/**
 * <p>
 * This package is the AudioEngine module of SilenceEngine, which defines the main AudioEngine module, and the Sound
 * class which are sufficient for most games. Games that require more functionality can use the OpenAL classes directly.
 * </p>
 *
 * <p>
 * The {@link com.shc.silenceengine.audio.AudioEngine} class is used to initialize the OpenAL device and context, and
 * also loading different {@link com.shc.silenceengine.audio.Sound} instances that you can use to play the different
 * sound effects in your game. The {@link com.shc.silenceengine.audio.ISoundReader} interface defines how to create a
 * reader that can read the data from any file format.
 * </p>
 *
 * @author Sri Harsha Chilakapati
 */
package com.shc.silenceengine.audio;