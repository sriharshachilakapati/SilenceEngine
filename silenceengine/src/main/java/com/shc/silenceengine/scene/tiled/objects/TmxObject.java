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

package com.shc.silenceengine.scene.tiled.objects;

import com.shc.easyxml.XmlTag;
import com.shc.silenceengine.scene.tiled.TmxProperties;

import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
public class TmxObject
{
    private String name;
    private String type;

    private int x;
    private int y;
    private int width;
    private int height;
    private int id;
    private int gid;

    private double  rotation;
    private boolean visible;

    private TmxEllipse  ellipse;
    private TmxPolygon  polygon;
    private TmxPolyLine polyLine;

    private TmxProperties properties;

    public TmxObject()
    {
        properties = new TmxProperties();
    }

    public void parse(XmlTag element)
    {
        name = element.getAttribute("name") != null ? element.getAttribute("name").value : "TmxObject";
        type = element.getAttribute("type") != null ? element.getAttribute("name").value : "TmxObject";

        id = Integer.parseInt(element.getAttribute("id").value);
        x = Integer.parseInt(element.getAttribute("x").value);
        y = Integer.parseInt(element.getAttribute("y").value);
        width = Integer.parseInt(element.getAttribute("width").value);
        height = Integer.parseInt(element.getAttribute("height").value);
        gid = element.getAttribute("gid") != null ? Integer.parseInt(element.getAttribute("gid").value) : -1;
        rotation = element.getAttribute("rotation") != null ? Integer.parseInt(element.getAttribute("rotation").value) : 0;

        visible = element.getAttribute("visible") == null || Boolean.parseBoolean(element.getAttribute("visible").value);

        List<XmlTag> nodes = element.getTagsByName("ellipse");
        if (nodes.size() > 0)
        {
            ellipse = new TmxEllipse();
            ellipse.set(x, y, width, height);
        }

        nodes = element.getTagsByName("polygon");
        if (nodes.size() > 0)
        {
            polygon = new TmxPolygon();
            polygon.parse(nodes.get(0));
        }

        nodes = element.getTagsByName("polyline");
        if (nodes.size() > 0)
        {
            polyLine = new TmxPolyLine();
            polyLine.parse(nodes.get(0));
        }

        nodes = element.getTagsByName("properties");
        if (nodes.size() > 0)
            properties.parse(nodes.get(0));
    }

    public String getName()
    {
        return name;
    }

    public String getType()
    {
        return type;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public int getID()
    {
        return id;
    }

    public int getGID()
    {
        return gid;
    }

    public double getRotation()
    {
        return rotation;
    }

    public boolean isVisible()
    {
        return visible;
    }

    public TmxEllipse getEllipse()
    {
        return ellipse;
    }

    public TmxPolygon getPolygon()
    {
        return polygon;
    }

    public TmxPolyLine getPolyLine()
    {
        return polyLine;
    }

    public TmxProperties getProperties()
    {
        return properties;
    }
}
