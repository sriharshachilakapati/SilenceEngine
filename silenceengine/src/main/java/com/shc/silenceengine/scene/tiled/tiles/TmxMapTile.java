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

import static com.shc.silenceengine.scene.tiled.TmxMap.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class TmxMapTile
{
    private int tileSetID;
    private int id;
    private int gid;

    private boolean flippedHorizontally;
    private boolean flippedVertically;
    private boolean flippedDiagonally;

    public TmxMapTile(int gid, int tileSetFirstID, int tileSetID)
    {
        this.tileSetID = tileSetID;

        flippedHorizontally = (gid & FLIPPED_HORIZONTALLY_FLAG) != 0;
        flippedVertically = (gid & FLIPPED_VERTICALLY_FLAG) != 0;
        flippedDiagonally = (gid & FLIPPED_DIAGONALLY_FLAG) != 0;

        this.gid = gid & ~(FLIPPED_HORIZONTALLY_FLAG | FLIPPED_VERTICALLY_FLAG | FLIPPED_DIAGONALLY_FLAG);
        this.id = gid - tileSetFirstID;
    }

    public int getTileSetID()
    {
        return tileSetID;
    }

    public int getID()
    {
        return id;
    }

    public int getGID()
    {
        return gid;
    }

    public boolean isFlippedHorizontally()
    {
        return flippedHorizontally;
    }

    public boolean isFlippedVertically()
    {
        return flippedVertically;
    }

    public boolean isFlippedDiagonally()
    {
        return flippedDiagonally;
    }
}
