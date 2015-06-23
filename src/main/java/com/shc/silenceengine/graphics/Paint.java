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

package com.shc.silenceengine.graphics;

/**
 * @author Sri Harsha Chilakapati
 */
public class Paint
{
    private Color topLeft;
    private Color topRight;
    private Color bottomLeft;
    private Color bottomRight;

    public Paint(Paint paint)
    {
        this();
        set(paint);
    }

    public Paint(Color topLeft, Color topRight, Color bottomLeft, Color bottomRight)
    {
        this.topLeft = topLeft.copy();
        this.topRight = topRight.copy();
        this.bottomLeft = bottomLeft.copy();
        this.bottomRight = bottomRight.copy();
    }

    public Paint(Color color)
    {
        this(color, color, color, color);
    }

    public Paint()
    {
        this(Color.WHITE);
    }

    public Paint(Color c1, Color c2, Gradient gradient)
    {
        this();

        switch (gradient)
        {
            case LINEAR_TOP_TO_BOTTOM:
                setTopLeftColor(c1);
                setTopRightColor(c1);
                setBottomLeftColor(c2);
                setBottomRightColor(c2);
                break;

            case LINEAR_LEFT_TO_RIGHT:
                setTopLeftColor(c1);
                setTopRightColor(c2);
                setBottomLeftColor(c1);
                setBottomRightColor(c2);
                break;

            case DIAGONAL_LEFT_TO_RIGHT:
                setTopLeftColor(c1);
                setTopRightColor(c2);
                setBottomLeftColor(c2);
                setBottomRightColor(c1);
                break;

            case DIAGONAL_RIGHT_TO_LEFT:
                setTopLeftColor(c2);
                setTopRightColor(c1);
                setBottomLeftColor(c1);
                setBottomRightColor(c2);
                break;
        }
    }

    public Color getTopLeftColor()
    {
        return topLeft;
    }

    public void setTopLeftColor(Color topLeft)
    {
        this.topLeft.set(topLeft);
    }

    public Color getTopRightColor()
    {
        return topRight;
    }

    public void setTopRightColor(Color topRight)
    {
        this.topRight.set(topRight);
    }

    public Color getBottomLeftColor()
    {
        return bottomLeft;
    }

    public void setBottomLeftColor(Color bottomLeft)
    {
        this.bottomLeft.set(bottomLeft);
    }

    public Color getBottomRightColor()
    {
        return bottomRight;
    }

    public void setBottomRightColor(Color bottomRight)
    {
        this.bottomRight.set(bottomRight);
    }

    public Color getColor(float u, float v, Color dest)
    {
        Color temp1 = Color.REUSABLE_STACK.pop();
        Color temp2 = Color.REUSABLE_STACK.pop();

        Color c1 = temp1.set(topLeft).lerpSelf(topRight, u);
        Color c2 = temp2.set(bottomLeft).lerpSelf(bottomRight, u);

        dest.set(c1).lerpSelf(c2, v);

        Color.REUSABLE_STACK.push(temp1);
        Color.REUSABLE_STACK.push(temp2);

        return dest;
    }

    public void setColor(Color color)
    {
        setTopLeftColor(color);
        setTopRightColor(color);
        setBottomLeftColor(color);
        setBottomRightColor(color);
    }

    public void set(Paint paint)
    {
        setTopLeftColor(paint.getTopLeftColor());
        setTopRightColor(paint.getTopRightColor());
        setBottomLeftColor(paint.getBottomLeftColor());
        setBottomRightColor(paint.getBottomRightColor());
    }

    public enum Gradient
    {
        LINEAR_TOP_TO_BOTTOM, LINEAR_LEFT_TO_RIGHT, DIAGONAL_LEFT_TO_RIGHT, DIAGONAL_RIGHT_TO_LEFT
    }
}
