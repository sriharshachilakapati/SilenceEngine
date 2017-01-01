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

    public void parse(XmlTag element)
    {
        id = Integer.parseInt(element.getAttribute("id").value);

        List<XmlTag> nodes = element.getTagsByName("properties");

        if (nodes.size() > 0)
            properties.parse(nodes.get(0));

        nodes = element.getTagsByName("animation");
        if (nodes.size() > 0)
        {
            animated = true;

            List<XmlTag> tiles = nodes.get(0).getTagsByName("frame");

            for (int i = 0; i < tiles.size(); i++)
            {
                XmlTag frame = tiles.get(i);

                int tileID = Integer.parseInt(frame.getAttribute("tileid").value);
                int duration = Integer.parseInt(frame.getAttribute("duration").value);

                TmxAnimationFrame animation = new TmxAnimationFrame(tileID, duration);
                frames.add(animation);
                totalDuration += duration;
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
