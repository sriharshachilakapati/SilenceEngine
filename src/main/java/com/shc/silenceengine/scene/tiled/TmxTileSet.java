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

import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.scene.tiled.tiles.TmxTerrain;
import com.shc.silenceengine.scene.tiled.tiles.TmxTile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
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

    public void parse(Node node, FilePath path)
    {
        Element element = (Element) node;

        firstGID = Integer.parseInt(element.getAttribute("firstgid"));

        try
        {
            if (element.hasAttribute("source"))
            {
                path = path.getParent().getChild(element.getAttribute("source"));

                Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(path.getInputStream());

                node = document.getElementsByTagName("tileset").item(0);
                element = (Element) node;
            }
        }
        catch (Exception e)
        {
            SilenceException.reThrow(e);
        }

        tileWidth = Integer.parseInt(element.getAttribute("tilewidth"));
        tileHeight = Integer.parseInt(element.getAttribute("tileheight"));
        margin = element.hasAttribute("margin") ? Integer.parseInt(element.getAttribute("margin")) : 0;
        spacing = element.hasAttribute("spacing") ? Integer.parseInt(element.getAttribute("spacing")) : 0;

        name = element.getAttribute("name");

        NodeList nodes = element.getElementsByTagName("tileoffset");
        if (nodes.getLength() > 0)
        {
            Element childElement = (Element) nodes.item(0);

            tileOffset.x = Float.parseFloat(childElement.getAttribute("x"));
            tileOffset.y = Float.parseFloat(childElement.getAttribute("y"));
        }

        nodes = element.getElementsByTagName("terraintypes");
        if (nodes.getLength() > 0)
        {
            Node terrain = nodes.item(0);

            while (terrain != null)
            {
                TmxTerrain terrainType = new TmxTerrain();
                terrainType.parse(terrain);
                terrainTypes.add(terrainType);

                terrain = terrain.getNextSibling();
            }
        }

        nodes = element.getElementsByTagName("image");
        if (nodes.getLength() > 0)
        {
            image = new TmxImage();
            image.parse(nodes.item(0), path);
        }

        int tileCount = (image.getWidth() / tileWidth) * (image.getHeight() / tileHeight);

        for (int tID = tiles.size(); tID < tileCount; tID++)
        {
            TmxTile tile = new TmxTile(tID);
            tiles.add(tile);
        }

        nodes = element.getElementsByTagName("tile");
        if (nodes.getLength() > 0)
        {
            Node tileNode = nodes.item(0);

            while (tileNode != null)
            {
                TmxTile tile = new TmxTile();
                tile.parse(tileNode);

                tiles.get(tile.getID()).parse(tileNode);

                tileNode = tileNode.getNextSibling();
            }
        }

        nodes = element.getElementsByTagName("properties");
        if (nodes.getLength() > 0)
        {
            properties.parse(nodes.item(0));
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

    public TmxTile getTile(int id)
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
