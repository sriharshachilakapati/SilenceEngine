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

package com.shc.silenceengine.utils;

import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.math.Vector4;

/**
 * Contains some Math utility functions, and trigonometry functions that accept degrees as the angle units.
 * SilenceEngine uses degrees everywhere instead of radians.
 *
 * @author Sri Harsha Chilakapati
 */
public final class MathUtils
{
    /**
     * The Epsilon, useful for comparing in math algorithms.
     */
    public static final double EPSILON = 0.000001;

    private MathUtils()
    {
    }

    /**
     * Returns the trigonomic tangent of an angle in degrees. The result will be NaN if the angle is NaN or Infinity.
     *
     * @param angle The angle in degrees.
     *
     * @return The trigonomic tangent.
     */
    public static float tan(float angle)
    {
        return (float) Math.tan(Math.toRadians(angle));
    }

    /**
     * Returns the trigonomic sine of an angle in degrees. The result will be NaN if the angle is NaN or Infinity.
     *
     * @param angle The angle in degrees.
     *
     * @return The trigonomic sine.
     */
    public static float sin(float angle)
    {
        return (float) Math.sin(Math.toRadians(angle));
    }

    /**
     * Returns the trigonomic cosine of an angle in degrees. The result will be NaN if the angle is NaN or Infinity.
     *
     * @param angle The angle in degrees.
     *
     * @return The trigonomic cosine.
     */
    public static float cos(float angle)
    {
        return (float) Math.cos(Math.toRadians(angle));
    }

    /**
     * Returns the trigonomic arc-cosine of a value in degrees. The result will be NaN if the value is NaN, or if its
     * absolute value is greater than 1.
     *
     * @param value The value whose arc-cosine has to be calculated
     *
     * @return The arc-cosine in degrees.
     */
    public static float acos(float value)
    {
        return (float) Math.toDegrees(Math.acos(value));
    }

    /**
     * Returns the trigonomic arc-sine of a value in degrees. The result will be NaN if the value is NaN, or if its
     * absolute value is greater than 1.
     *
     * @param value The value whose arc-sine has to be calculated
     *
     * @return The arc-sine in degrees.
     */
    public static float asin(float value)
    {
        return (float) Math.toDegrees(Math.asin(value));
    }

    /**
     * Returns the trigonomic arc-tangent of a value in degrees. The result will be NaN if the value is NaN, or if its
     * absolute value is greater than 1.
     *
     * @param value The value whose arc-tangent has to be calculated
     *
     * @return The arc-tangent in degrees.
     */
    public static float atan(float value)
    {
        return (float) Math.toDegrees(Math.atan(value));
    }

    /**
     * Returns the angle <i>theta</i> from the conversion of rectangular coordinates ({@code x},&nbsp;{@code y}) to
     * polar coordinates (r,&nbsp;<i>theta</i>). This method computes the phase <i>theta</i> by computing an arc tangent
     * of {@code y/x} in the range of -<i>pi</i> to <i>pi</i> and returns the angle in degrees. All the special cases
     * of {@code Math.atan2} apply here.
     *
     * @param y the ordinate coordinate
     * @param x the abscissa coordinate
     *
     * @return the <i>theta</i> component of the point (<i>r</i>,&nbsp;<i>theta</i>) in polar coordinates that
     * corresponds to the point (<i>x</i>,&nbsp;<i>y</i>) in Cartesian coordinates.
     */
    public static float atan2(float y, float x)
    {
        return (float) Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Returns a new vector which is component-wise minimum of the given two vectors.
     *
     * @param v1 The first vector
     * @param v2 The second vector
     *
     * @return The new component-wise vector
     */
    public static Vector2 min(Vector2 v1, Vector2 v2)
    {
        return new Vector2(v1.x < v2.x ? v1.x : v2.x, v1.y < v2.y ? v1.y : v2.y);
    }

    /**
     * Returns a new vector which is component-wise minimum of the given two vectors.
     *
     * @param v1   The first vector
     * @param v2   The second vector
     * @param dest The destination vector
     *
     * @return The new component-wise vector
     */
    public static Vector2 min(Vector2 v1, Vector2 v2, Vector2 dest)
    {
        return dest.set(v1.x < v2.x ? v1.x : v2.x, v1.y < v2.y ? v1.y : v2.y);
    }

    /**
     * Returns a new vector which is component-wise maximum of the given two vectors.
     *
     * @param v1 The first vector
     * @param v2 The second vector
     *
     * @return The new component-wise vector
     */
    public static Vector2 max(Vector2 v1, Vector2 v2)
    {
        return new Vector2(v1.x > v2.x ? v1.x : v2.x, v1.y > v2.y ? v1.y : v2.y);
    }

    /**
     * Returns a new vector which is component-wise maximum of the given two vectors.
     *
     * @param v1   The first vector
     * @param v2   The second vector
     * @param dest The destination vector
     *
     * @return The new component-wise vector
     */
    public static Vector2 max(Vector2 v1, Vector2 v2, Vector2 dest)
    {
        return dest.set(v1.x > v2.x ? v1.x : v2.x, v1.y > v2.y ? v1.y : v2.y);
    }

    /**
     * Returns a new vector which is component-wise minimum of the given two vectors.
     *
     * @param v1 The first vector
     * @param v2 The second vector
     *
     * @return The new component-wise vector
     */
    public static Vector3 min(Vector3 v1, Vector3 v2)
    {
        return new Vector3(v1.x < v2.x ? v1.x : v2.x, v1.y < v2.y ? v1.y : v2.y, v1.z < v2.z ? v1.z : v2.z);
    }

    /**
     * Returns a new vector which is component-wise minimum of the given two vectors.
     *
     * @param v1   The first vector
     * @param v2   The second vector
     * @param dest The destination vector
     *
     * @return The new component-wise vector
     */
    public static Vector3 min(Vector3 v1, Vector3 v2, Vector3 dest)
    {
        return dest.set(v1.x < v2.x ? v1.x : v2.x, v1.y < v2.y ? v1.y : v2.y, v1.z < v2.z ? v1.z : v2.z);
    }

    /**
     * Returns a new vector which is component-wise maximum of the given two vectors.
     *
     * @param v1 The first vector
     * @param v2 The second vector
     *
     * @return The new component-wise vector
     */
    public static Vector3 max(Vector3 v1, Vector3 v2)
    {
        return new Vector3(v1.x > v2.x ? v1.x : v2.x, v1.y > v2.y ? v1.y : v2.y, v1.z > v2.z ? v1.z : v2.z);
    }

    /**
     * Returns a new vector which is component-wise maximum of the given two vectors.
     *
     * @param v1   The first vector
     * @param v2   The second vector
     * @param dest The destination vector
     *
     * @return The new component-wise vector
     */
    public static Vector3 max(Vector3 v1, Vector3 v2, Vector3 dest)
    {
        return dest.set(v1.x > v2.x ? v1.x : v2.x, v1.y > v2.y ? v1.y : v2.y, v1.z > v2.z ? v1.z : v2.z);
    }

    /**
     * Returns a new vector which is component-wise minimum of the given two vectors.
     *
     * @param v1 The first vector
     * @param v2 The second vector
     *
     * @return The new component-wise vector
     */
    public static Vector4 min(Vector4 v1, Vector4 v2)
    {
        return new Vector4(v1.x < v2.x ? v1.x : v2.x, v1.y < v2.y ? v1.y : v2.y, v1.z < v2.z ? v1.z : v2.z, v1.w < v2.w ? v1.w : v2.w);
    }

    /**
     * Returns a new vector which is component-wise minimum of the given two vectors.
     *
     * @param v1   The first vector
     * @param v2   The second vector
     * @param dest The destination vector
     *
     * @return The new component-wise vector
     */
    public static Vector4 min(Vector4 v1, Vector4 v2, Vector4 dest)
    {
        return dest.set(v1.x < v2.x ? v1.x : v2.x, v1.y < v2.y ? v1.y : v2.y, v1.z < v2.z ? v1.z : v2.z, v1.w < v2.w ? v1.w : v2.w);
    }

    /**
     * Returns a new vector which is component-wise maximum of the given two vectors.
     *
     * @param v1 The first vector
     * @param v2 The second vector
     *
     * @return The new component-wise vector
     */
    public static Vector4 max(Vector4 v1, Vector4 v2)
    {
        return new Vector4(v1.x > v2.x ? v1.x : v2.x, v1.y > v2.y ? v1.y : v2.y, v1.z > v2.z ? v1.z : v2.z, v1.w > v2.w ? v1.w : v2.w);
    }

    /**
     * Returns a new vector which is component-wise maximum of the given two vectors.
     *
     * @param v1   The first vector
     * @param v2   The second vector
     * @param dest The destination vector
     *
     * @return The new component-wise vector
     */
    public static Vector4 max(Vector4 v1, Vector4 v2, Vector4 dest)
    {
        return dest.set(v1.x > v2.x ? v1.x : v2.x, v1.y > v2.y ? v1.y : v2.y, v1.z > v2.z ? v1.z : v2.z, v1.w > v2.w ? v1.w : v2.w);
    }

    /**
     * Converts a {@code value} in range {@code (oldMin, oldMax)} to the new range {@code (newMin, newMax)}.
     *
     * @param value  The value in the old range
     * @param oldMin The minimum (inclusive) value of the old range
     * @param oldMax The maximum (inclusive) value of the old range
     * @param newMin The minimum (inclusive) value of the new range
     * @param newMax The maximum (inclusive) value of the new range
     *
     * @return The value in the new range defined by {@code (newMin, newMax)}
     */
    public static int convertRange(int value, int oldMin, int oldMax, int newMin, int newMax)
    {
        return (((value - oldMin) * (newMax - newMin)) / (oldMax - oldMin)) + newMin;
    }

    /**
     * Converts a {@code value} in range {@code (oldMin, oldMax)} to the new range {@code (newMin, newMax)}.
     *
     * @param value  The value in the old range
     * @param oldMin The minimum (inclusive) value of the old range
     * @param oldMax The maximum (inclusive) value of the old range
     * @param newMin The minimum (inclusive) value of the new range
     * @param newMax The maximum (inclusive) value of the new range
     *
     * @return The value in the new range defined by {@code (newMin, newMax)}
     */
    public static float convertRange(float value, float oldMin, float oldMax, float newMin, float newMax)
    {
        return (((value - oldMin) * (newMax - newMin)) / (oldMax - oldMin)) + newMin;
    }

    /**
     * Converts a {@code value} in range {@code (oldMin, oldMax)} to the new range {@code (newMin, newMax)}.
     *
     * @param value  The value in the old range
     * @param oldMin The minimum (inclusive) value of the old range
     * @param oldMax The maximum (inclusive) value of the old range
     * @param newMin The minimum (inclusive) value of the new range
     * @param newMax The maximum (inclusive) value of the new range
     *
     * @return The value in the new range defined by {@code (newMin, newMax)}
     */
    public static short convertRange(short value, short oldMin, short oldMax, short newMin, short newMax)
    {
        return (short) ((((value - oldMin) * (newMax - newMin)) / (oldMax - oldMin)) + newMin);
    }

    /**
     * Converts a {@code value} in range {@code (oldMin, oldMax)} to the new range {@code (newMin, newMax)}.
     *
     * @param value  The value in the old range
     * @param oldMin The minimum (inclusive) value of the old range
     * @param oldMax The maximum (inclusive) value of the old range
     * @param newMin The minimum (inclusive) value of the new range
     * @param newMax The maximum (inclusive) value of the new range
     *
     * @return The value in the new range defined by {@code (newMin, newMax)}
     */
    public static long convertRange(long value, long oldMin, long oldMax, long newMin, long newMax)
    {
        return (((value - oldMin) * (newMax - newMin)) / (oldMax - oldMin)) + newMin;
    }

    /**
     * Converts a {@code value} in range {@code (oldMin, oldMax)} to the new range {@code (newMin, newMax)}.
     *
     * @param value  The value in the old range
     * @param oldMin The minimum (inclusive) value of the old range
     * @param oldMax The maximum (inclusive) value of the old range
     * @param newMin The minimum (inclusive) value of the new range
     * @param newMax The maximum (inclusive) value of the new range
     *
     * @return The value in the new range defined by {@code (newMin, newMax)}
     */
    public static double convertRange(double value, double oldMin, double oldMax, double newMin, double newMax)
    {
        return (((value - oldMin) * (newMax - newMin)) / (oldMax - oldMin)) + newMin;
    }

    /**
     * Clamps a given {@code value} between the provided {@code min} and {@code max} values.
     *
     * @param val The value to be clamped
     * @param min The minimum value
     * @param max The maximum value
     *
     * @return The clamped value
     */
    public static int clamp(int val, int min, int max)
    {
        val = Math.min(val, max);
        val = Math.max(val, min);

        return val;
    }

    /**
     * Clamps a given {@code value} between the provided {@code min} and {@code max} values.
     *
     * @param val The value to be clamped
     * @param min The minimum value
     * @param max The maximum value
     *
     * @return The clamped value
     */
    public static long clamp(long val, long min, long max)
    {
        val = Math.min(val, max);
        val = Math.max(val, min);

        return val;
    }

    /**
     * Clamps a given {@code value} between the provided {@code min} and {@code max} values.
     *
     * @param val The value to be clamped
     * @param min The minimum value
     * @param max The maximum value
     *
     * @return The clamped value
     */
    public static double clamp(double val, double min, double max)
    {
        val = Math.min(val, max);
        val = Math.max(val, min);

        return val;
    }

    /**
     * Clamps a given {@code value} between the provided {@code min} and {@code max} values.
     *
     * @param val The value to be clamped
     * @param min The minimum value
     * @param max The maximum value
     *
     * @return The clamped value
     */
    public static short clamp(short val, short min, short max)
    {
        val = (short) Math.min(val, max);
        val = (short) Math.max(val, min);

        return val;
    }

    /**
     * Clamps a given {@code value} between the provided {@code min} and {@code max} values.
     *
     * @param val The value to be clamped
     * @param min The minimum value
     * @param max The maximum value
     *
     * @return The clamped value
     */
    public static float clamp(float val, float min, float max)
    {
        val = Math.min(val, max);
        val = Math.max(val, min);

        return val;
    }

    /**
     * Clamps a given {@code vector} between the provided {@code min} and {@code max} vectors component-wise.
     *
     * @param vector The value to be clamped
     * @param min    The minimum value
     * @param max    The maximum value
     *
     * @return The clamped vector
     */
    public static Vector2 clamp(Vector2 vector, Vector2 min, Vector2 max)
    {
        Vector2 result = new Vector2();

        result.x = clamp(vector.x, min.x, max.x);
        result.y = clamp(vector.y, min.y, max.y);

        return result;
    }

    /**
     * Clamps a given {@code vector} between the provided {@code min} and {@code max} vectors component-wise.
     *
     * @param vector The value to be clamped
     * @param min    The minimum value
     * @param max    The maximum value
     *
     * @return The clamped vector
     */
    public static Vector3 clamp(Vector3 vector, Vector3 min, Vector3 max)
    {
        Vector3 result = new Vector3();

        result.x = clamp(vector.x, min.x, max.x);
        result.y = clamp(vector.y, min.y, max.y);
        result.z = clamp(vector.z, min.z, max.z);

        return result;
    }

    /**
     * Clamps a given {@code vector} between the provided {@code min} and {@code max} vectors component-wise.
     *
     * @param vector The value to be clamped
     * @param min    The minimum value
     * @param max    The maximum value
     *
     * @return The clamped vector
     */
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
    public static int randomRange(int x1, int x2)
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
    public static double frac(double x)
    {
        return x % 1.0;
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
    public static double pointDistance(double x1, double y1, double x2, double y2)
    {
        return Math.sqrt(((x2 - x1) * (x2 - x1))
                         + ((y2 - y1) * (y2 - y1)));
    }

    /**
     * Returns the distance between point (x1,y1,z1) and point (x2,y2,z2).
     *
     * @param x1 The abscissa of first point
     * @param y1 The ordinate of first point
     * @param z1 The depth of first point
     * @param x2 The abscissa of second point
     * @param y2 The ordinate of second point
     * @param z2 The depth of second point
     *
     * @return The distance between two points
     */
    public static double pointDistance(double x1, double y1, double z1, double x2, double y2, double z2)
    {
        return Math.sqrt(((x2 - x1) * (x2 - x1))
                         + ((y2 - y1) * (y2 - y1))
                         + ((y2 - y1) * (y2 - y1)));
    }

    /**
     * Returns whether there is a chance of given {@code percentage}. It works by generating a random number from 0 to
     * 100, and checking if the given {@code percentage} is greater than the generated random number.
     *
     * @param percentage The percentage (between 0 - 100, inclusive) to test chance
     *
     * @return True if there is a chance, otherwise false
     */
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

    /**
     * Tests if a given {@code object} is a boolean value. Unlike {@code instanceof} test, this method also returns for
     * strings whose values are matched by the regular expression {@code (true)|(false)|(yes)|(no)}.
     *
     * @param object The object to be tested as a Boolean
     *
     * @return True if the value is a boolean, otherwise false
     */
    public static boolean isBoolean(Object object)
    {
        if (object instanceof Boolean)
            return true;

        // If it is not a Boolean instance, it can be a string instance with a boolean value
        return object instanceof String &&
               ((String) object).matches("(true)|(false)|(yes)|(no)");
    }

    /**
     * Tests if a given {@code object} is an integer value. Unlike {@code instanceof} test, this method also returns for
     * strings whose values are successfully parsed by {@link Integer#parseInt(String)} method.
     *
     * @param object The object to be tested as a Integer
     *
     * @return True if the value is an integer, otherwise false
     */
    public static boolean isInteger(Object object)
    {
        if (object instanceof Integer)
            return true;

        if (object instanceof String)
            try
            {
                return ("" + Integer.parseInt((String) object)).equalsIgnoreCase((String) object);
            }
            catch (Exception e)
            {
                return false;
            }

        return false;
    }

    /**
     * Tests if a given {@code object} is an Short value. Unlike {@code instanceof} test, this method also returns for
     * strings whose values are successfully parsed by {@link Short#parseShort(String)} method.
     *
     * @param object The object to be tested as a short
     *
     * @return True if the value is a short, otherwise false
     */
    public static boolean isShort(Object object)
    {
        if (object instanceof Short)
            return true;

        if (object instanceof String)
            try
            {
                return ("" + Short.parseShort((String) object)).equalsIgnoreCase((String) object);
            }
            catch (Exception e)
            {
                return false;
            }

        return false;
    }

    /**
     * Tests if a given {@code object} is an Float value. Unlike {@code instanceof} test, this method also returns for
     * strings whose values are successfully parsed by {@link Float#parseFloat(String)} method.
     *
     * @param object The object to be tested as a float
     *
     * @return True if the value is a float, otherwise false
     */
    public static boolean isFloat(Object object)
    {
        if (object instanceof Float)
            return true;

        if (object instanceof String)
            try
            {
                return ("" + Float.parseFloat((String) object)).equalsIgnoreCase((String) object);
            }
            catch (Exception e)
            {
                return false;
            }

        return false;
    }

    /**
     * Tests if a given {@code object} is an Long value. Unlike {@code instanceof} test, this method also returns for
     * strings whose values are successfully parsed by {@link Long#parseLong(String)} method.
     *
     * @param object The object to be tested as a long
     *
     * @return True if the value is a long, otherwise false
     */
    public static boolean isLong(Object object)
    {
        if (object instanceof Long)
            return true;

        if (object instanceof String)
            try
            {
                return ("" + Long.parseLong((String) object)).equalsIgnoreCase((String) object);
            }
            catch (Exception e)
            {
                return false;
            }

        return false;
    }

    /**
     * Tests if a given {@code object} is an Double value. Unlike {@code instanceof} test, this method also returns for
     * strings whose values are successfully parsed by {@link Double#parseDouble(String)} method.
     *
     * @param object The object to be tested as a double
     *
     * @return True if the value is a double, otherwise false
     */
    public static boolean isDouble(Object object)
    {
        if (object instanceof Double)
            return true;

        if (object instanceof String)
            try
            {
                return ("" + Double.parseDouble((String) object)).equalsIgnoreCase((String) object);
            }
            catch (Exception e)
            {
                return false;
            }

        return false;
    }
}
