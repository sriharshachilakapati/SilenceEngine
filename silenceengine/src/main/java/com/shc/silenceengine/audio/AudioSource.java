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

package com.shc.silenceengine.audio;

import com.shc.silenceengine.math.Vector3;

/**
 * <p>An AudioSource is like a speaker that lives in a 3D world. Each sound will be played through a source. This class
 * allows for specifying the spatial properties like the position, the velocity and direction of the source.</p>
 *
 * @author Sri Harsha Chilakapati
 */
public class AudioSource
{
    /**
     * The position vector of the source. This is relative to the origin (0, 0, 0).
     */
    public final Vector3 position = new Vector3();

    /**
     * The velocity vector of the source. This velocity is not added to the position every frame like one would expect,
     * but is used to render doppler effect.
     */
    public final Vector3 velocity = new Vector3();

    /**
     * The direction vector of the source. This is used to specify the orientation of the speaker, where it is looking
     * at is the question.
     */
    public final Vector3 direction = new Vector3();

    boolean updated = true;

    /**
     * Marks this source for updating. This causes the AudioScene to apply these properties to all the playing sounds.
     */
    public void update()
    {
        // The updating to ALSource will happen in AudioScene
        updated = true;
    }
}
