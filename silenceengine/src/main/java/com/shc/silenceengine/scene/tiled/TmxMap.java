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

import com.shc.easyxml.Xml;
import com.shc.easyxml.XmlTag;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.scene.tiled.layers.TmxImageLayer;
import com.shc.silenceengine.scene.tiled.layers.TmxMapLayer;
import com.shc.silenceengine.scene.tiled.layers.TmxObjectLayer;
import com.shc.silenceengine.scene.tiled.layers.TmxTileLayer;
import com.shc.silenceengine.utils.functional.UniCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * A TiledMap represents the data format of the TMX file format, used by the Tiled Map Editor.
 *
 * @author Sri Harsha Chilakapati
 */
public class TmxMap
{
    public static final int FLIPPED_HORIZONTALLY_FLAG = 0x80000000;
    public static final int FLIPPED_VERTICALLY_FLAG   = 0x40000000;
    public static final int FLIPPED_DIAGONALLY_FLAG   = 0x20000000;

    private FilePath filePath;
    private Color    backgroundColor;

    private double version;

    private Orientation  orientation;
    private RenderOrder  renderOrder;
    private StaggerAxis  staggerAxis;
    private StaggerIndex staggerIndex;

    private int width;
    private int height;
    private int tileWidth;
    private int tileHeight;
    private int nextObjectID;
    private int hexSideLength;

    private List<TmxMapLayer>    layers;
    private List<TmxTileLayer>   tileLayers;
    private List<TmxImageLayer>  imageLayers;
    private List<TmxObjectLayer> objectLayers;
    private List<TmxTileSet>     tileSets;

    private TmxProperties properties;

    private TmxMap()
    {
    }

    public static void load(FilePath filePath, UniCallback<TmxMap> callback)
    {
        TmxMap map = new TmxMap();

        map.version = 1.0;

        map.layers = new ArrayList<>();
        map.tileLayers = new ArrayList<>();
        map.imageLayers = new ArrayList<>();
        map.objectLayers = new ArrayList<>();
        map.tileSets = new ArrayList<>();

        map.properties = new TmxProperties();

        map.orientation = Orientation.ORTHOGONAL;
        map.renderOrder = RenderOrder.RIGHT_DOWN;
        map.staggerAxis = StaggerAxis.NONE;
        map.staggerIndex = StaggerIndex.NONE;

        map.backgroundColor = Color.TRANSPARENT;

        map.filePath = filePath;

        SilenceEngine.io.getFileReader().readTextFile(filePath, xml ->
        {
            try
            {
                XmlTag mapNode = Xml.parse(xml);

                if (!mapNode.name.equals("map"))
                    throw new SilenceException("Invalid TMX map file. The first child must be a <map> element.");

                map.parse(mapNode);

                callback.invoke(map);
            }
            catch (Exception e)
            {
                if (!(e instanceof SilenceException))
                    SilenceException.reThrow(e);
            }
        });
    }

    public Orientation getOrientation()
    {
        return orientation;
    }

    public RenderOrder getRenderOrder()
    {
        return renderOrder;
    }

    public StaggerAxis getStaggerAxis()
    {
        return staggerAxis;
    }

    public StaggerIndex getStaggerIndex()
    {
        return staggerIndex;
    }

    public FilePath getFilePath()
    {
        return filePath;
    }

    public Color getBackgroundColor()
    {
        return backgroundColor;
    }

    public double getVersion()
    {
        return version;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public int getTileWidth()
    {
        return tileWidth;
    }

    public int getTileHeight()
    {
        return tileHeight;
    }

    public int getNextObjectID()
    {
        return nextObjectID;
    }

    public int getHexSideLength()
    {
        return hexSideLength;
    }

    public TmxMapLayer getLayer(int index)
    {
        return layers.get(index);
    }

    public int getNumLayers()
    {
        return layers.size();
    }

    public List<TmxMapLayer> getLayers()
    {
        return layers;
    }

    public TmxTileLayer getTileLayer(int index)
    {
        return tileLayers.get(index);
    }

    public int getNumTileLayers()
    {
        return tileLayers.size();
    }

    public List<TmxTileLayer> getTileLayers()
    {
        return tileLayers;
    }

    public TmxObjectLayer getObjectLayer(int index)
    {
        return objectLayers.get(index);
    }

    public int getNumObjectLayers()
    {
        return objectLayers.size();
    }

    public List<TmxObjectLayer> getObjectLayers()
    {
        return objectLayers;
    }

    public TmxImageLayer getImageLayer(int index)
    {
        return imageLayers.get(index);
    }

    public int getNumImageLayers()
    {
        return imageLayers.size();
    }

    public List<TmxImageLayer> getImageLayers()
    {
        return imageLayers;
    }

    public int findTileSetIndex(long gid)
    {
        gid &= ~(FLIPPED_HORIZONTALLY_FLAG | FLIPPED_VERTICALLY_FLAG | FLIPPED_DIAGONALLY_FLAG);

        for (int i = getNumTileSets() - 1; i >= 0; i--)
        {
            if (gid >= getTileset(i).getFirstGID())
                return i;
        }

        return -1;
    }

    public TmxTileSet findTileset(int gid)
    {
        for (int i = getNumTileSets() - 1; i >= 0; i--)
        {
            if (gid >= getTileset(i).getFirstGID())
                return getTileset(i);
        }

        return null;
    }

    public TmxTileSet getTileset(int index)
    {
        return tileSets.get(index);
    }

    public int getNumTileSets()
    {
        return tileSets.size();
    }

    public List<TmxTileSet> getTileSets()
    {
        return tileSets;
    }

    public TmxProperties getProperties()
    {
        return properties;
    }

    private void parse(XmlTag element)
    {
        version = Double.parseDouble(element.getAttribute("version").value);
        width = Integer.parseInt(element.getAttribute("width").value);
        height = Integer.parseInt(element.getAttribute("height").value);
        tileWidth = Integer.parseInt(element.getAttribute("tilewidth").value);
        tileHeight = Integer.parseInt(element.getAttribute("tileheight").value);
        nextObjectID = Integer.parseInt(element.getAttribute("nextobjectid").value);

        if (element.getAttribute("background") != null)
        {
            String hexColor = element.getAttribute("background").value.trim();
            if (hexColor.startsWith("#"))
                hexColor = hexColor.substring(1);

            backgroundColor = new Color(Integer.parseInt(hexColor, 16));
        }

        orientation = Orientation.valueOf(element.getAttribute("orientation").value.trim().toUpperCase());

        if (element.getAttribute("renderorder") != null)
        {
            switch (element.getAttribute("renderorder").value.trim().toLowerCase())
            {
                case "right-down":
                    renderOrder = RenderOrder.RIGHT_DOWN;
                    break;
                case "right-up":
                    renderOrder = RenderOrder.RIGHT_UP;
                    break;
                case "left-down":
                    renderOrder = RenderOrder.LEFT_DOWN;
                    break;
                case "left-up":
                    renderOrder = RenderOrder.LEFT_UP;
                    break;
            }
        }

        if (element.getAttribute("staggeraxis") != null)
        {
            switch (element.getAttribute("staggeraxis").value.trim().toLowerCase())
            {
                case "x":
                    staggerAxis = StaggerAxis.AXIS_X;
                    break;
                case "y":
                    staggerAxis = StaggerAxis.AXIS_Y;
                    break;
            }
        }

        if (element.getAttribute("staggerindex") != null)
        {
            switch (element.getAttribute("staggerindex").value.trim().toLowerCase())
            {
                case "even":
                    staggerIndex = StaggerIndex.EVEN;
                    break;
                case "odd":
                    staggerIndex = StaggerIndex.ODD;
                    break;
            }
        }

        if (element.getAttribute("hexsidelength") != null)
            hexSideLength = Integer.parseInt(element.getAttribute("hexsidelength").value);

        // Read all other elements
        for (XmlTag child : element.children)
        {
            switch (child.name.trim().toLowerCase())
            {
                case "properties":
                    properties.parse(child);
                    break;

                case "tileset":
                    TmxTileSet tileSet = new TmxTileSet();
                    tileSet.parse(child, filePath);
                    tileSets.add(tileSet);
                    break;

                case "layer":
                    TmxTileLayer tileLayer = new TmxTileLayer(this);
                    tileLayer.parse(child);
                    tileLayers.add(tileLayer);
                    break;

                case "imagelayer":
                    TmxImageLayer imageLayer = new TmxImageLayer(this);
                    imageLayer.parse(child);
                    imageLayers.add(imageLayer);
                    break;

                case "objectgroup":
                    TmxObjectLayer objectLayer = new TmxObjectLayer(this);
                    objectLayer.parse(child);
                    objectLayers.add(objectLayer);
                    break;
            }
        }

        layers.addAll(tileLayers);
        layers.addAll(imageLayers);
        layers.addAll(objectLayers);
    }

    /**
     * The orientation of the TiledMap.
     */
    public enum Orientation
    {
        ORTHOGONAL, ISOMETRIC, STAGGERED, HEXAGONAL
    }

    /**
     * The render order of the TiledMap
     */
    public enum RenderOrder
    {
        RIGHT_DOWN, RIGHT_UP, LEFT_DOWN, LEFT_UP
    }

    /**
     * The stagger axis for the map (if the map is not hexagonal)
     */
    public enum StaggerAxis
    {
        AXIS_X, AXIS_Y, NONE
    }

    /**
     * The stagger index for the map. Applies to hexagonal and isometric staggered maps.
     */
    public enum StaggerIndex
    {
        NONE, EVEN, ODD
    }
}
