package com.shc.silenceengine.utils;

/**
 * @author Sri Harsha Chilakapati
 */
public final class MathUtils
{
    private MathUtils()
    {
    }

    public static int clamp(int val, int min, int max)
    {
        val = Math.min(val, max);
        val = Math.max(val, min);

        return val;
    }

    public static float clamp(float val, float min, float max)
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

    /**
     * Returns a random real number between 0 and x. The number is always
     * smaller than x.
     *
     * @param x The maximum range
     * @return A random real number
     */
    public static int random(int x)
    {
        return (int) (Math.floor(Math.random() * x));
    }

    /**
     * Returns a random real number between x1 (inclusive) and x2 (exclusive).
     *
     * @param x1 The inclusive
     * @param x2 The exclusive
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
     * @return A random value present in the array
     */
    public static int choose(int[] values)
    {
        return (values[random(values.length + 1)]);
    }

    /**
     * Returns the fractional part of x, that is, the part behind the decimal
     * dot.
     *
     * @param x The real number
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
     * Returns the inclination of the line-segment drawn from (x1, y1) to
     * (x2, y2) in degrees
     *
     * @param x1 The abscissa of first point
     * @param y1 The ordinate of first point
     * @param x2 The abscissa of second point
     * @param y2 The ordinate of second point
     * @return The direction in degrees
     */
    public static int getDirection(float x1, float y1, float x2, float y2)
    {
        return (int) Math.toDegrees(Math.atan((y2 - y1) / (x2 - x1)));
    }
}
