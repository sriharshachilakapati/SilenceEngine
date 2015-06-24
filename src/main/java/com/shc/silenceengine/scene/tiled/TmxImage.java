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

package com.shc.silenceengine.scene.tiled;

import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.io.FilePath;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author Sri Harsha Chilakapati
 */
public class TmxImage
{
    // The format of the TileImage
    private Format format;

    // The path to the reference image
    private FilePath source;

    // The transparent color on the reference image
    private Color trans;

    // The width and height of the image in pixels
    private int width;
    private int height;

    public void parse(Node node, FilePath tmxPath)
    {
        Element element = (Element) node;

        String sourcePath = element.getAttribute("source");
        source = tmxPath.getParent().getChild(sourcePath.trim());

        width = Integer.parseInt(element.getAttribute("width"));
        height = Integer.parseInt(element.getAttribute("height"));

        trans = Color.TRANSPARENT;

        if (element.hasAttribute("trans"))
        {
            String color = element.getAttribute("trans").trim();

            if (color.startsWith("#"))
                color = color.substring(1);

            trans = new Color(Integer.parseInt(color, 16));
        }
    }

    public Format getFormat()
    {
        return format;
    }

    public FilePath getSource()
    {
        return source;
    }

    public int getWidth()
    {
        return width;
    }

    public Color getTrans()
    {
        return trans;
    }

    public int getHeight()
    {
        return height;
    }

    public enum Format
    {
        PNG, GIF, JPG, BMP, OTHER
    }
}
