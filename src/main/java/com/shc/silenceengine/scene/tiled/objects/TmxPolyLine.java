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

package com.shc.silenceengine.scene.tiled.objects;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
public class TmxPolyLine
{
    private List<TmxPoint> points;

    public TmxPolyLine()
    {
        points = new ArrayList<>();
    }

    public TmxPoint getPoint(int index)
    {
        return points.get(index);
    }

    public List<TmxPoint> getPoints()
    {
        return points;
    }

    public int getNumPoints()
    {
        return points.size();
    }

    public void parse(Node node)
    {
        String pointsLine = ((Element) node).getAttribute("points").trim();

        for (String token : pointsLine.split(" "))
        {
            String[] subTokens = token.split(",");

            TmxPoint point = new TmxPoint();
            point.x = Integer.parseInt(subTokens[0].trim());
            point.y = Integer.parseInt(subTokens[1].trim());

            points.add(point);
        }
    }
}