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

import com.shc.silenceengine.scene.tiled.TmxImage;
import com.shc.silenceengine.scene.tiled.TmxMap;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Sri Harsha Chilakapati
 */
public class TmxImageLayer extends TmxMapLayer
{
    private TmxImage image;

    public TmxImageLayer(TmxMap map)
    {
        super(map, "", 0, 0, map.getWidth(), map.getHeight(), 1.0f, true, TmxLayerType.IMAGE);
    }

    public void parse(Node node)
    {
        Element element = (Element) node;

        name = element.getAttribute("name");

        x = element.hasAttribute("x") ? Integer.parseInt(element.getAttribute("x")) : 0;
        y = element.hasAttribute("y") ? Integer.parseInt(element.getAttribute("y")) : 0;

        opacity = element.hasAttribute("opacity") ? Float.parseFloat(element.getAttribute("opacity")) : 1.0f;
        visible = !element.hasAttribute("visible") || Boolean.parseBoolean(element.getAttribute("visible"));

        NodeList nodes = element.getElementsByTagName("image");
        if (nodes.getLength() > 0)
        {
            image = new TmxImage();
            image.parse(nodes.item(0), getMap().getFilePath());
        }

        nodes = element.getElementsByTagName("properties");
        if (nodes.getLength() > 0)
            properties.parse(nodes.item(0));
    }

    public TmxImage getImage()
    {
        return image;
    }
}
