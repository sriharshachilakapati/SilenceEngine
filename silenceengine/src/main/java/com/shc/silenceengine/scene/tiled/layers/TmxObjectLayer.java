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

import com.shc.easyxml.XmlTag;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.scene.tiled.TmxMap;
import com.shc.silenceengine.scene.tiled.objects.TmxObject;

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

    public void parse(XmlTag element)
    {
        name = element.getAttribute("name").value;

        if (element.getAttribute("color") != null)
        {
            String colorString = element.getAttribute("color").value.trim();
            if (colorString.startsWith("#"))
                colorString = colorString.substring(1);

            color = new Color(Integer.decode(colorString));
        }

        opacity = element.getAttribute("opacity") != null ? Float.parseFloat(element.getAttribute("opacity").value) : 1.0f;
        visible = element.getAttribute("visible") == null|| Boolean.parseBoolean(element.getAttribute("visible").value);

        List<XmlTag> nodes = element.getTagsByName("properties");
        if (nodes.size() > 0)
            properties.parse(nodes.get(0));

        nodes = element.getTagsByName("object");

        for (int i = 0; i < nodes.size(); i++)
        {
            XmlTag objectNode = nodes.get(i);

            TmxObject object = new TmxObject();
            object.parse(objectNode);

            objects.add(object);
        }
    }
}
