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

import com.shc.silenceengine.io.FilePath;

import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
public class TileSet
{
    // The global tile id of this tileset
    private int firstGID;

    // The source FilePath of the TSX file, if this tileset is stored externally
    private FilePath source;

    // The name of this tileset
    private String name;

    // The width and height (maximum) of the tiles in this tileset
    private int tileWidth;
    private int tileHeight;

    // The spacing and margin of this TileSet
    private int spacing;
    private int margin;

    // The image used by this tileset
    private TileImage image;

    // The terrain types in this tileset
    private TerrainTypes terrainTypes;

    // The list of Tiles in this tileset
    private List<Tile> tilesList;
}
