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

package com.shc.silenceengine.utils;

import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.math.Vector4;

/**
 * @author Sri Harsha Chilakapati
 */
public final class MathUtils
{
    private MathUtils()
    {
    }

    public static int abs(int value)
    {
        return Math.abs(value);
    }

    public static float abs(float value)
    {
        return Math.abs(value);
    }

    public static short abs(short value)
    {
        return (short) Math.abs(value);
    }

    public static double abs(double value)
    {
        return Math.abs(value);
    }

    public static long abs(long value)
    {
        return Math.abs(value);
    }

    public static float cot(float angle)
    {
        return 1f / tan(angle);
    }

    public static float tan(float angle)
    {
        return sin(angle) / cos(angle);
    }

    public static float sin(float angle)
    {
        return (float) Math.sin(Math.toRadians(angle));
    }

    public static float cos(float angle)
    {
        return (float) Math.cos(Math.toRadians(angle));
    }

    public static float sec(float angle)
    {
        return 1f / cos(angle);
    }

    public static float csc(float angle)
    {
        return 1f / sin(angle);
    }

    public static float acos(float value)
    {
        return (float) Math.toDegrees(Math.acos(value));
    }

    public static float asin(float value)
    {
        return (float) Math.toDegrees(Math.asin(value));
    }

    public static float atan2(float y, float x)
    {
        return (float) Math.toDegrees(Math.atan2(y, x));
    }

    public static int sqrt(int value)
    {
        return (int) Math.sqrt(value);
    }

    public static float sqrt(float value)
    {
        return (float) Math.sqrt(value);
    }

    public static short sqrt(short value)
    {
        return (short) Math.sqrt(value);
    }

    public static long sqrt(long value)
    {
        return (long) Math.sqrt(value);
    }

    public static double sqrt(double value)
    {
        return Math.sqrt(value);
    }

    public static Vector2 min(Vector2 v1, Vector2 v2)
    {
        return new Vector2(v1.x < v2.x ? v1.x : v2.x, v1.y < v2.y ? v1.y : v2.y);
    }

    public static Vector2 max(Vector2 v1, Vector2 v2)
    {
        return new Vector2(v1.x > v2.x ? v1.x : v2.x, v1.y > v2.y ? v1.y : v2.y);
    }

    public static Vector3 min(Vector3 v1, Vector3 v2)
    {
        return new Vector3(v1.x < v2.x ? v1.x : v2.x, v1.y < v2.y ? v1.y : v2.y, v1.z < v2.z ? v1.z : v2.z);
    }

    public static Vector3 max(Vector3 v1, Vector3 v2)
    {
        return new Vector3(v1.x > v2.x ? v1.x : v2.x, v1.y > v2.y ? v1.y : v2.y, v1.z > v2.z ? v1.z : v2.z);
    }

    public static Vector4 min(Vector4 v1, Vector4 v2)
    {
        return new Vector4(v1.x < v2.x ? v1.x : v2.x, v1.y < v2.y ? v1.y : v2.y, v1.z < v2.z ? v1.z : v2.z, v1.w < v2.w ? v1.w : v2.w);
    }

    public static Vector4 max(Vector4 v1, Vector4 v2)
    {
        return new Vector4(v1.x > v2.x ? v1.x : v2.x, v1.y > v2.y ? v1.y : v2.y, v1.z > v2.z ? v1.z : v2.z, v1.w > v2.w ? v1.w : v2.w);
    }

    public static int convertRange(int value, int oldMin, int oldMax, int newMin, int newMax)
    {
        return (((value - oldMin) * (newMax - newMin)) / (oldMax - oldMin)) + newMin;
    }

    public static float convertRange(float value, float oldMin, float oldMax, float newMin, float newMax)
    {
        return (((value - oldMin) * (newMax - newMin)) / (oldMax - oldMin)) + newMin;
    }

    public static short convertRange(short value, short oldMin, short oldMax, short newMin, short newMax)
    {
        return (short) ((((value - oldMin) * (newMax - newMin)) / (oldMax - oldMin)) + newMin);
    }

    public static long convertRange(long value, long oldMin, long oldMax, long newMin, long newMax)
    {
        return (((value - oldMin) * (newMax - newMin)) / (oldMax - oldMin)) + newMin;
    }

    public static double convertRange(double value, double oldMin, double oldMax, double newMin, double newMax)
    {
        return (((value - oldMin) * (newMax - newMin)) / (oldMax - oldMin)) + newMin;
    }

    public static int clamp(int val, int min, int max)
    {
        val = Math.min(val, max);
        val = Math.max(val, min);

        return val;
    }

    public static long clamp(long val, long min, long max)
    {
        val = Math.min(val, max);
        val = Math.max(val, min);

        return val;
    }

    public static double clamp(double val, double min, double max)
    {
        val = Math.min(val, max);
        val = Math.max(val, min);

        return val;
    }

    public static short clamp(short val, short min, short max)
    {
        val = (short) Math.min(val, max);
        val = (short) Math.max(val, min);

        return val;
    }

    public static Vector2 clamp(Vector2 vector, Vector2 min, Vector2 max)
    {
        Vector2 result = new Vector2();

        result.x = clamp(vector.x, min.x, max.x);
        result.y = clamp(vector.y, min.y, max.y);

        return result;
    }

    public static float clamp(float val, float min, float max)
    {
        val = Math.min(val, max);
        val = Math.max(val, min);

        return val;
    }

    public static Vector3 clamp(Vector3 vector, Vector3 min, Vector3 max)
    {
        Vector3 result = new Vector3();

        result.x = clamp(vector.x, min.x, max.x);
        result.y = clamp(vector.y, min.y, max.y);
        result.z = clamp(vector.z, min.z, max.z);

        return result;
    }

    public static Vector4 clamp(Vector4 vector, Vector4 min, Vector4 max)
    {
        Vector4 result = new Vector4();

        result.x = clamp(vector.x, min.x, max.x);
        result.y = clamp(vector.y, min.y, max.y);
        result.z = clamp(vector.z, min.z, max.z);
        result.w = clamp(vector.w, min.w, max.w);

        return result;
    }

    /**
     * Returns a random real number between x1 (inclusive) and x2 (exclusive).
     *
     * @param x1 The inclusive
     * @param x2 The exclusive
     *
     * @return A random real number between x1 and x2
     */
    public static int random_range(int x1, int x2)
    {
        return (int) (Math.floor(x1 + (Math.random() * (x2 - x1))));
    }

    /**
     * Returns one of the arguments chosen randomly.
     *
     * @param values The array containing values
     *
     * @return A random value present in the array
     */
    public static int choose(int[] values)
    {
        return (values[random(values.length + 1)]);
    }

    /**
     * Returns a random real number between 0 and x. The number is always smaller than x.
     *
     * @param x The maximum range
     *
     * @return A random real number
     */
    public static int random(int x)
    {
        return (int) (Math.floor(Math.random() * x));
    }

    /**
     * Returns the fractional part of x, that is, the part behind the decimal dot.
     *
     * @param x The real number
     *
     * @return The fractional part of x
     */
    public static int frac(double x)
    {
        return Integer.parseInt(String.valueOf(x).replaceAll("^.*\\.", ""));
    }

    /**
     * Returns x to the power n.
     *
     * @param x The base
     * @param n The exponent
     *
     * @return x to the power n.
     */
    public static int power(int x, int n)
    {
        return (int) (Math.pow(x, n));
    }

    /**
     * Returns the average of the values.
     *
     * @param values The array of integers
     *
     * @return The mean of the values
     */
    public static int mean(int[] values)
    {
        int result = 0;

        for (int value : values)
        {
            result += value;
        }

        result /= values.length;
        return result;
    }

    /**
     * Returns the distance between point (x1,y1) and point (x2,y2).
     *
     * @param x1 The abscissa of first point
     * @param y1 The ordinate of first point
     * @param x2 The abscissa of second point
     * @param y2 The ordinate of second point
     *
     * @return The distance between two points
     */
    public static int point_distance(int x1, int y1, int x2, int y2)
    {
        return (int) (Math.sqrt(((x2 - x1) * (x2 - x1))
                                + ((y2 - y1) * (y2 - y1))));
    }

    public static boolean chance(int percentage)
    {
        return random(100) <= percentage;
    }

    /**
     * Returns the inclination of the line-segment drawn from (x1, y1) to (x2, y2) in degrees
     *
     * @param x1 The abscissa of first point
     * @param y1 The ordinate of first point
     * @param x2 The abscissa of second point
     * @param y2 The ordinate of second point
     *
     * @return The direction in degrees
     */
    public static float getDirection(float x1, float y1, float x2, float y2)
    {
        return atan((y2 - y1) / (x2 - x1));
    }

    public static float atan(float value)
    {
        return (float) Math.toDegrees(Math.atan(value));
    }
}
