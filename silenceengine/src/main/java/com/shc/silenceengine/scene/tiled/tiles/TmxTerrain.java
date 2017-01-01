/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 Sri Harsha Chilakapati
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

package com.shc.silenceengine.scene.tiled.tiles;

import com.shc.easyxml.XmlTag;
import com.shc.silenceengine.scene.tiled.TmxProperties;

import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
public class TmxTerrain
{
    // The name of the Terrain type
    private String name;

    // The local tile ID of the tile that represents this terrain
    private int tileID;

    // The properties of this Terrain
    private TmxProperties properties;

    public TmxTerrain()
    {
        properties = new TmxProperties();
    }

    public void parse(XmlTag element)
    {
        name = element.getAttribute("name").value;
        tileID = Integer.parseInt(element.getAttribute("tile").value);

        List<XmlTag> nodes = element.getTagsByName("properties");
        if (nodes.size() > 0)
            properties.parse(nodes.get(0));
    }

    public String getName()
    {
        return name;
    }

    public int getTileID()
    {
        return tileID;
    }

    public TmxProperties getProperties()
    {
        return properties;
    }
}
