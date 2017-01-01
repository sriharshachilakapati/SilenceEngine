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

package com.shc.silenceengine.math;

import com.shc.silenceengine.utils.ReusableStack;

/**
 * A data structure used to represent a Ray in three dimensional world space. The representation is done by an origin,
 * that is the start of the ray which is a position vector, and direction, a directional vector to denote the direction
 * of propagation of the ray.
 *
 * @author Sri Harsha Chilakapati
 */
public class Ray
{
    /**
     * ReusableStack instance for use to claim temporary objects
     */
    public static final ReusableStack<Ray> REUSABLE_STACK = new ReusableStack<>(Ray::new);

    /**
     * The origin of the ray
     */
    public final Vector3 origin = new Vector3();

    /**
     * The direction of propagation of the ray
     */
    public final Vector3 direction = new Vector3();

    /**
     * Get a point on the ray with distance from the origin
     *
     * @param distance The distance from the origin
     * @param dest     The vector to place the new point in
     *
     * @return The new point on the ray
     */
    public Vector3 getPoint(float distance, Vector3 dest)
    {
        if (dest == null)
            dest = new Vector3();

        return dest.set(direction).scale(distance).add(origin);
    }

    @Override
    public int hashCode()
    {
        int result = origin.hashCode();
        result = 31 * result + direction.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ray ray = (Ray) o;

        return origin.equals(ray.origin) && direction.equals(ray.direction);
    }

    @Override
    public String toString()
    {
        return "Ray{" +
               "origin=" + origin +
               ", direction=" + direction +
               '}';
    }
}
