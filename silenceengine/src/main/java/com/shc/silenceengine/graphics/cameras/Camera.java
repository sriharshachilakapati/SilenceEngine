/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 Sri Harsha Chilakapati
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

package com.shc.silenceengine.graphics.cameras;

import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.input.Mouse;
import com.shc.silenceengine.math.Matrix4;
import com.shc.silenceengine.math.Ray;
import com.shc.silenceengine.math.Vector4;

/**
 * @author Sri Harsha Chilakapati
 */
public abstract class Camera
{
    public static Camera CURRENT;

    public void apply()
    {
        CURRENT = this;
    }

    public abstract Matrix4 getProjection();

    public abstract Matrix4 getView();

    /**
     * Creates a Ray from a point on the screen. This point will be in screen coordinates, so you can simply pass in the
     * mouse position that you get from the {@link Mouse} class.
     *
     * @param screenX The x-coordinate on the screen
     * @param screenY The y-coordinate on the screen
     * @param dest    The ray object to write the ray to
     *
     * @return The created Ray object
     */
    public Ray createRayFromScreenPoint(float screenX, float screenY, Ray dest)
    {
        if (dest == null)
            dest = new Ray();

        // Calculate the ray in NDC
        final float rayNDCx = (screenX / SilenceEngine.display.getWidth() - 0.5f) * 2f;
        final float rayNDCy = (screenY / SilenceEngine.display.getHeight() - 0.5f) * 2f;

        Vector4 rayStartNDC = Vector4.REUSABLE_STACK.pop().set(rayNDCx, rayNDCy, -1, 1);
        Vector4 rayEndNDC = Vector4.REUSABLE_STACK.pop().set(rayNDCx, rayNDCy, 0, 1);

        // Calculate the inverse world matrix
        Matrix4 inverseWorld = Matrix4.REUSABLE_STACK.pop().set(getView())
                .multiply(getProjection()).invert();

        // Calculate the ray in world space
        Vector4 rayStartWorld = Vector4.REUSABLE_STACK.pop();
        inverseWorld.multiply(rayStartNDC, rayStartWorld);
        rayStartWorld.scale(1f / rayStartWorld.w);

        Vector4 rayEndWorld = Vector4.REUSABLE_STACK.pop();
        inverseWorld.multiply(rayEndNDC, rayEndWorld);
        rayEndWorld.scale(1f / rayEndWorld.w);

        // Populate the dest ray object
        dest.origin.set(rayStartWorld.x, rayStartWorld.y, rayStartWorld.z);

        rayEndWorld.subtract(rayStartWorld);
        dest.direction.set(rayEndWorld.x, rayEndWorld.y, rayEndWorld.z).normalize();

        // Push back the temporary objects into the stack
        Vector4.REUSABLE_STACK.push(rayStartNDC);
        Vector4.REUSABLE_STACK.push(rayEndNDC);
        Vector4.REUSABLE_STACK.push(rayStartWorld);
        Vector4.REUSABLE_STACK.push(rayEndWorld);
        Matrix4.REUSABLE_STACK.push(inverseWorld);

        return dest;
    }
}
