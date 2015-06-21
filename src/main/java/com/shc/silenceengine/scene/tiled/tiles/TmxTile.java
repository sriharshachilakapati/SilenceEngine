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

package com.shc.silenceengine.scene.tiled.tiles;

import com.shc.silenceengine.scene.tiled.TmxProperties;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
public class TmxTile
{
    private int id;
    private int totalDuration;

    private boolean animated;

    private List<TmxAnimationFrame> frames;

    private TmxProperties properties;

    public TmxTile()
    {
        this(0);
    }

    public TmxTile(int id)
    {
        this.id = id;

        frames = new ArrayList<>();
        properties = new TmxProperties();
    }

    public void parse(Node node)
    {
        Element element = (Element) node;

        id = Integer.parseInt(element.getAttribute("id"));

        NodeList nodes = element.getElementsByTagName("properties");

        if (nodes.getLength() > 0)
            properties.parse(nodes.item(0));

        nodes = element.getElementsByTagName("animation");
        if (nodes.getLength() > 0)
        {
            animated = true;

            Node frameNode = nodes.item(0).getFirstChild();

            while (frameNode != null)
            {
                Element frame = (Element) frameNode;

                int tileID = Integer.parseInt(frame.getAttribute("tileid"));
                int duration = Integer.parseInt(frame.getAttribute("duration"));

                TmxAnimationFrame animation = new TmxAnimationFrame(tileID, totalDuration);
                frames.add(animation);
                totalDuration += duration;

                frameNode = frameNode.getNextSibling();
            }
        }
    }

    public int getID()
    {
        return id;
    }

    public int getTotalDuration()
    {
        return totalDuration;
    }

    public int getFrameCount()
    {
        return frames.size();
    }

    public boolean isAnimated()
    {
        return animated;
    }

    public List<TmxAnimationFrame> getFrames()
    {
        return frames;
    }

    public TmxProperties getProperties()
    {
        return properties;
    }
}
