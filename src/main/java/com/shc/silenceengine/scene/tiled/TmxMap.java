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
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.scene.tiled.layers.TmxImageLayer;
import com.shc.silenceengine.scene.tiled.layers.TmxMapLayer;
import com.shc.silenceengine.scene.tiled.layers.TmxObjectLayer;
import com.shc.silenceengine.scene.tiled.layers.TmxTileLayer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
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

    public TmxMap(FilePath filePath)
    {
        version = 1.0;

        layers = new ArrayList<>();
        tileLayers = new ArrayList<>();
        imageLayers = new ArrayList<>();
        objectLayers = new ArrayList<>();
        tileSets = new ArrayList<>();

        properties = new TmxProperties();

        orientation = Orientation.ORTHOGONAL;
        renderOrder = RenderOrder.RIGHT_DOWN;
        staggerAxis = StaggerAxis.NONE;
        staggerIndex = StaggerIndex.NONE;

        backgroundColor = Color.TRANSPARENT;

        this.filePath = filePath;

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        try
        {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.parse(filePath.getInputStream());
            document.getDocumentElement().normalize();

            Node mapNode = document.getDocumentElement();

            if (!mapNode.getNodeName().equals("map"))
                throw new SilenceException("Invalid TMX map file. The first child must be a <map> element.");

            parse(mapNode);
        }
        catch (Exception e)
        {
            if (!(e instanceof SilenceException))
                SilenceException.reThrow(e);
        }
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

    public int findTileSetIndex(int gid)
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

    private void parse(Node node)
    {
        Element element = (Element) node;

        version = Double.parseDouble(element.getAttribute("version"));
        width = Integer.parseInt(element.getAttribute("width"));
        height = Integer.parseInt(element.getAttribute("height"));
        tileWidth = Integer.parseInt(element.getAttribute("tilewidth"));
        tileHeight = Integer.parseInt(element.getAttribute("tileheight"));
        nextObjectID = Integer.parseInt(element.getAttribute("nextobjectid"));

        if (element.hasAttribute("background"))
        {
            String hexColor = element.getAttribute("background").trim();
            if (hexColor.startsWith("#"))
                hexColor = hexColor.substring(1);

            backgroundColor = new Color(Integer.parseInt(hexColor, 16));
        }

        orientation = Orientation.valueOf(element.getAttribute("orientation").trim().toUpperCase());

        if (element.hasAttribute("renderorder"))
        {
            switch (element.getAttribute("renderorder").trim().toLowerCase())
            {
                case "right-down": renderOrder = RenderOrder.RIGHT_DOWN; break;
                case "right-up":   renderOrder = RenderOrder.RIGHT_UP;   break;
                case "left-down":  renderOrder = RenderOrder.LEFT_DOWN;  break;
                case "left-up":    renderOrder = RenderOrder.LEFT_UP;    break;
            }
        }

        if (element.hasAttribute("staggeraxis"))
        {
            switch (element.getAttribute("staggeraxis").trim().toLowerCase())
            {
                case "x": staggerAxis = StaggerAxis.AXIS_X; break;
                case "y": staggerAxis = StaggerAxis.AXIS_Y; break;
            }
        }

        if (element.hasAttribute("staggerindex"))
        {
            switch (element.getAttribute("staggerindex").trim().toLowerCase())
            {
                case "even": staggerIndex = StaggerIndex.EVEN; break;
                case "odd":  staggerIndex = StaggerIndex.ODD;  break;
            }
        }

        if (element.hasAttribute("hexsidelength"))
            hexSideLength = Integer.parseInt(element.getAttribute("hexsidelength"));

        // Read all other elements
        Node child = node.getFirstChild();

        while (child != null)
        {
            switch (child.getNodeName().trim().toLowerCase())
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

            child = child.getNextSibling();
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
