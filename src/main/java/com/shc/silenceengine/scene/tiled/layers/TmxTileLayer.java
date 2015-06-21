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

import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.scene.tiled.TmxMap;
import com.shc.silenceengine.scene.tiled.tiles.TmxMapTile;
import com.shc.silenceengine.scene.tiled.TmxTileSet;
import com.shc.silenceengine.utils.CompressionUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Base64;

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

    public void parse(Node node)
    {
        Element element = (Element) node;

        name = element.getAttribute("name");

        x = element.hasAttribute("x") ? Integer.parseInt(element.getAttribute("x")) : 0;
        y = element.hasAttribute("y") ? Integer.parseInt(element.getAttribute("y")) : 0;

        opacity = element.hasAttribute("opacity") ? Float.parseFloat(element.getAttribute("opacity")) : 1.0f;
        visible = !element.hasAttribute("visible") || Boolean.parseBoolean(element.getAttribute("visible"));

        NodeList nodes = element.getElementsByTagName("properties");
        if (nodes.getLength() > 0)
            properties.parse(nodes.item(0));

        tileMap = new TmxMapTile[width * height];

        Element dataElement = (Element) element.getElementsByTagName("data").item(0);

        if (dataElement.hasAttribute("encoding"))
        {
            switch (dataElement.getAttribute("encoding").trim().toLowerCase())
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

        if (dataElement.hasAttribute("compression"))
        {
            switch (dataElement.getAttribute("compression").trim().toLowerCase())
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
                parseBase64(dataElement.getTextContent());
                break;

            case CSV:
                parseCSV(dataElement.getTextContent());
                break;
        }
    }

    private void parseXML(Node node)
    {
        NodeList nodes = ((Element) node).getElementsByTagName("tile");

        for (int tileCount = 0; tileCount < nodes.getLength(); tileCount++)
        {
            Element tileElement = (Element) nodes.item(tileCount);

            int gid = Integer.parseInt(tileElement.getAttribute("gid"));
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

    private void parseBase64(String base64)
    {
        byte[] decodedData = Base64.getDecoder().decode(base64.trim());

        try
        {
            if (compression == Compression.GZIP)
                decodedData = CompressionUtils.decompressGZIP(decodedData);

            if (compression == Compression.ZLIB)
                decodedData = CompressionUtils.decompressZLIB(decodedData);
        }
        catch (Exception e)
        {
            SilenceException.reThrow(e);
        }

        IntBuffer intBuffer = ByteBuffer.wrap(decodedData).order(ByteOrder.LITTLE_ENDIAN).asIntBuffer();
        int[] out = new int[intBuffer.remaining()];
        intBuffer.get(out);

        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                int gid = out[y * width + x];

                int tileSetIndex = map.findTileSetIndex(gid);

                if (tileSetIndex != -1)
                {
                    TmxTileSet tileSet = map.getTileset(tileSetIndex);
                    tileMap[y * width + x] = new TmxMapTile(gid, tileSet.getFirstGID(), tileSetIndex);
                }
                else
                    tileMap[y * width + x] = new TmxMapTile(gid, 0, -1);
            }
        }
    }

    private void parseCSV(String csv)
    {
        String[] tokens = csv.split(",");
        int tileCount = 0;

        for (String token : tokens)
        {
            int gid = Integer.parseInt(token.trim());

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
