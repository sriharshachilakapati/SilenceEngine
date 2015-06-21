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

import com.shc.silenceengine.scene.tiled.TmxProperties;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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

    public void parse(Node node)
    {
        Element element = (Element) node;

        name = element.hasAttribute("name") ? element.getAttribute("name") : "TmxObject";
        type = element.hasAttribute("type") ? element.getAttribute("name") : "TmxObject";

        id = Integer.parseInt(element.getAttribute("id"));
        x = Integer.parseInt(element.getAttribute("x"));
        y = Integer.parseInt(element.getAttribute("y"));
        width = Integer.parseInt(element.getAttribute("width"));
        height = Integer.parseInt(element.getAttribute("height"));
        gid = element.hasAttribute("gid") ? Integer.parseInt(element.getAttribute("gid")) : -1;
        rotation = element.hasAttribute("rotation") ? Integer.parseInt(element.getAttribute("rotation")) : 0;

        visible = !element.hasAttribute("visible") || Boolean.parseBoolean(element.getAttribute("visible"));

        NodeList nodes = element.getElementsByTagName("ellipse");
        if (nodes.getLength() > 0)
        {
            ellipse = new TmxEllipse();
            ellipse.set(x, y, width, height);
        }

        nodes = element.getElementsByTagName("polygon");
        if (nodes.getLength() > 0)
        {
            polygon = new TmxPolygon();
            polygon.parse(nodes.item(0));
        }

        nodes = element.getElementsByTagName("polyline");
        if (nodes.getLength() > 0)
        {
            polyLine = new TmxPolyLine();
            polyLine.parse(nodes.item(0));
        }

        nodes = element.getElementsByTagName("properties");
        if (nodes.getLength() > 0)
            properties.parse(nodes.item(0));
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
