/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2016 Sri Harsha Chilakapati
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
import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.scene.tiled.TmxMap;
import com.shc.silenceengine.scene.tiled.TmxTileSet;
import com.shc.silenceengine.scene.tiled.tiles.TmxMapTile;

import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
public class TmxTileLayer extends TmxMapLayer
{
    private TmxMapTile[] tileMap;

    private Encoding    encoding;
    private Compression compression;

    public TmxTileLayer(TmxMap map)
    {
        super(map, "", 0, 0, map.getWidth(), map.getHeight(), 1.0f, true, TmxLayerType.TILE);

        encoding = Encoding.XML;
        compression = Compression.NONE;
    }

    public void parse(XmlTag element)
    {
        name = element.getAttribute("name").value;

        x = element.getAttribute("x") != null ? Integer.parseInt(element.getAttribute("x").value) : 0;
        y = element.getAttribute("y") != null ? Integer.parseInt(element.getAttribute("y").value) : 0;

        opacity = element.getAttribute("opacity") != null ? Float.parseFloat(element.getAttribute("opacity").value) : 1.0f;
        visible = element.getAttribute("visible") == null || Boolean.parseBoolean(element.getAttribute("visible").value);

        List<XmlTag> nodes = element.getTagsByName("properties");
        if (nodes.size() > 0)
            properties.parse(nodes.get(0));

        tileMap = new TmxMapTile[width * height];

        XmlTag dataElement = element.getTagsByName("data").get(0);

        if (dataElement.getAttribute("encoding") != null)
        {
            switch (dataElement.getAttribute("encoding").value.trim().toLowerCase())
            {
                case "base64":
                    encoding = Encoding.BASE64;
                    break;
                case "csv":
                    encoding = Encoding.CSV;
                    break;

                default:
                    encoding = Encoding.XML;
            }
        }

        if (dataElement.getAttribute("compression") != null)
        {
            switch (dataElement.getAttribute("compression").value.trim().toLowerCase())
            {
                case "gzip":
                    compression = Compression.GZIP;
                    break;
                case "zlib":
                    compression = Compression.ZLIB;
                    break;

                default:
                    compression = Compression.NONE;
            }
        }

        switch (encoding)
        {
            case XML:
                parseXML(dataElement);
                break;

            case BASE64:
                throw new SilenceException("Compressed maps are unsupported.");

            case CSV:
                parseCSV(dataElement.text);
                break;
        }
    }

    private void parseXML(XmlTag node)
    {
        List<XmlTag> nodes = node.getTagsByName("tile");

        for (int tileCount = 0; tileCount < nodes.size(); tileCount++)
        {
            XmlTag tileElement = nodes.get(tileCount);

            int gid = (int) Long.parseLong((tileElement.getAttribute("gid").value)) & 0xff;
            int tileSetIndex = map.findTileSetIndex(gid);

            if (tileSetIndex != -1)
            {
                TmxTileSet tileSet = map.getTileset(tileSetIndex);
                tileMap[tileCount] = new TmxMapTile(gid, tileSet.getFirstGID(), tileSetIndex);
            }
            else
                tileMap[tileCount] = new TmxMapTile(gid, 0, -1);
        }
    }

    private void parseCSV(String csv)
    {
        String[] tokens = csv.split(",");
        int tileCount = 0;

        for (String token : tokens)
        {
            int gid = (int) (Long.parseLong(token.trim()) & 0xFF);

            int tileSetIndex = map.findTileSetIndex(gid);

            if (tileSetIndex != -1)
            {
                TmxTileSet tileSet = map.getTileset(tileSetIndex);
                tileMap[tileCount] = new TmxMapTile(gid, tileSet.getFirstGID(), tileSetIndex);
            }
            else
                tileMap[tileCount] = new TmxMapTile(gid, 0, -1);

            tileCount++;
        }
    }

    public int getTileID(int x, int y)
    {
        return tileMap[y * width + x].getID();
    }

    public int getTileGID(int x, int y)
    {
        return tileMap[y * width + x].getGID();
    }

    public int getTileTileSetIndex(int x, int y)
    {
        return tileMap[y * width + x].getTileSetID();
    }

    public boolean isTileFlippedHorizontally(int x, int y)
    {
        return tileMap[y * width + x].isFlippedHorizontally();
    }

    public boolean isTileFlippedVertically(int x, int y)
    {
        return tileMap[y * width + x].isFlippedVertically();
    }

    public boolean isTileFlippedDiagonally(int x, int y)
    {
        return tileMap[y * width + x].isFlippedDiagonally();
    }

    public TmxMapTile getTile(int x, int y)
    {
        return tileMap[y * width + x];
    }

    public Encoding getEncoding()
    {
        return encoding;
    }

    public Compression getCompression()
    {
        return compression;
    }

    public enum Encoding
    {
        XML, BASE64, CSV
    }

    public enum Compression
    {
        NONE, GZIP, ZLIB
    }
}
