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

import java.util.List;

/**
 * A TiledMap represents the data format of the TMX file format, used by the Tiled Map Editor.
 *
 * @author Sri Harsha Chilakapati
 */
public class TmxMap
{
    // The TMX format version, generally 1.0
    protected String version;

    // Map orientation. Can be ORTHOGONAL, ISOMETRIC, or STAGGERED
    protected Orientation orientation;

    // The width and height of map, in tiles
    protected int width;
    protected int height;

    // The width and height of a tile, in pixels
    protected int tileWidth;
    protected int tileHeight;

    // The background color of this map (defaults to Transparent)
    protected Color backgroundColor = Color.TRANSPARENT.copy();

    // The render order of the map (defaults to RIGHT_DOWN)
    protected RenderOrder renderOrder = RenderOrder.RIGHT_DOWN;

    // The list of TileSets used by this TiledMap
    protected List<TmxTileSet> tileSets;

    // The list of MapLayers present in this TiledMap
    protected List<TmxMapLayer> mapLayers;

    // The map properties of this TiledMap
    protected TmxProperties properties;

    /**
     * The orientation of the TiledMap.
     */
    public enum Orientation
    {
        ORTHOGONAL, ISOMETRIC, STAGGERED
    }

    /**
     * The render order of the TiledMap
     */
    public enum RenderOrder
    {
        RIGHT_DOWN, RIGHT_UP, LEFT_DOWN, LEFT_UP
    }
}
