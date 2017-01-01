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

package com.shc.silenceengine.scene.tiled;

import com.shc.easyxml.XmlTag;
import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.scene.tiled.tiles.TmxTerrain;
import com.shc.silenceengine.scene.tiled.tiles.TmxTile;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
public class TmxTileSet
{
    private int firstGID;

    private String name;

    private int tileWidth;
    private int tileHeight;
    private int spacing;
    private int margin;

    private Vector2  tileOffset;
    private TmxImage image;

    private List<TmxTerrain> terrainTypes;
    private List<TmxTile>    tiles;

    private TmxProperties properties;

    public TmxTileSet()
    {
        tileOffset = new Vector2();

        terrainTypes = new ArrayList<>();
        tiles = new ArrayList<>();

        properties = new TmxProperties();
    }

    public void parse(XmlTag element, FilePath path)
    {
        firstGID = Integer.parseInt(element.getAttribute("firstgid").value);

        if (element.getAttribute("source") != null)
        {
            throw new SilenceException("External tile sets aren't supported yet");
        }

        tileWidth = Integer.parseInt(element.getAttribute("tilewidth").value);
        tileHeight = Integer.parseInt(element.getAttribute("tileheight").value);
        margin = element.getAttribute("margin") != null ? Integer.parseInt(element.getAttribute("margin").value) : 0;
        spacing = element.getAttribute("spacing") != null ? Integer.parseInt(element.getAttribute("spacing").value) : 0;

        name = element.getAttribute("name").value;

        List<XmlTag> nodes = element.getTagsByName("tileoffset");
        if (nodes.size() > 0)
        {
            XmlTag childElement = nodes.get(0);

            tileOffset.x = Float.parseFloat(childElement.getAttribute("x").value);
            tileOffset.y = Float.parseFloat(childElement.getAttribute("y").value);
        }

        nodes = element.getTagsByName("terraintypes");
        if (nodes.size() > 0)
        {
            for (XmlTag terrain : nodes)
            {
                TmxTerrain terrainType = new TmxTerrain();
                terrainType.parse(terrain);
                terrainTypes.add(terrainType);
            }
        }

        nodes = element.getTagsByName("image");
        if (nodes.size() > 0)
        {
            image = new TmxImage();
            image.parse(nodes.get(0), path);
        }

        int tileCount = (image.getWidth() / tileWidth) * (image.getHeight() / tileHeight);

        for (int tID = tiles.size(); tID < tileCount; tID++)
        {
            TmxTile tile = new TmxTile(tID);
            tiles.add(tile);
        }

        nodes = element.getTagsByName("tile");
        if (nodes.size() > 0)
        {
            for (int i = 0; i < nodes.size(); i++)
            {
                XmlTag tileNode = nodes.get(i);

                TmxTile tile = new TmxTile();
                tile.parse(tileNode);

                tiles.get(tile.getID()).parse(tileNode);
            }
        }

        nodes = element.getTagsByName("properties");
        if (nodes.size() > 0)
        {
            properties.parse(nodes.get(0));
        }
    }

    public int getFirstGID()
    {
        return firstGID;
    }

    public String getName()
    {
        return name;
    }

    public int getTileWidth()
    {
        return tileWidth;
    }

    public int getTileHeight()
    {
        return tileHeight;
    }

    public int getSpacing()
    {
        return spacing;
    }

    public int getMargin()
    {
        return margin;
    }

    public Vector2 getTileOffset()
    {
        return tileOffset;
    }

    public TmxImage getImage()
    {
        return image;
    }

    public List<TmxTerrain> getTerrainTypes()
    {
        return terrainTypes;
    }

    public TmxTile getTile(long id)
    {
        for (TmxTile tile : tiles)
        {
            if (tile.getID() == id)
                return tile;
        }

        return null;
    }

    public List<TmxTile> getTiles()
    {
        return tiles;
    }

    public TmxProperties getProperties()
    {
        return properties;
    }
}
