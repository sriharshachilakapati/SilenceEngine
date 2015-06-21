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

package com.shc.silenceengine.scene.tiled.layers;

import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.scene.tiled.TmxMap;
import com.shc.silenceengine.scene.tiled.objects.TmxObject;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
public class TmxObjectLayer extends TmxMapLayer
{
    private Color color;

    private List<TmxObject> objects;

    public TmxObjectLayer(TmxMap map)
    {
        super(map, "", 0, 0, map.getWidth(), map.getHeight(), 1.0f, true, TmxLayerType.OBJECT);

        objects = new ArrayList<>();
        color = Color.TRANSPARENT;
    }

    public TmxObject getObject(int index)
    {
        return objects.get(index);
    }

    public int getNumObjects()
    {
        return objects.size();
    }

    public Color getColor()
    {
        return color;
    }

    public List<TmxObject> getObjects()
    {
        return objects;
    }

    public void parse(Node node)
    {
        Element element = (Element) node;

        name = element.getAttribute("name");

        if (element.hasAttribute("color"))
        {
            String colorString = element.getAttribute("color").trim();
            if (colorString.startsWith("#"))
                colorString = colorString.substring(1);

            color = new Color(Integer.decode(colorString));
        }

        opacity = element.hasAttribute("opacity") ? Float.parseFloat("opacity") : 1.0f;
        visible = !element.hasAttribute("visible") || Boolean.parseBoolean("visible");

        NodeList nodes = element.getElementsByTagName("properties");
        if (nodes.getLength() > 0)
            properties.parse(nodes.item(0));

        nodes = element.getElementsByTagName("object");

        for (int i = 0; i < nodes.getLength(); i++)
        {
            Node objectNode = nodes.item(i);

            TmxObject object = new TmxObject();
            object.parse(objectNode);

            objects.add(object);
        }
    }
}
