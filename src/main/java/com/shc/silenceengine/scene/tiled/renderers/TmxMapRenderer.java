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

package com.shc.silenceengine.scene.tiled.renderers;

import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.scene.tiled.TmxMap;
import com.shc.silenceengine.scene.tiled.TmxTileSet;
import com.shc.silenceengine.scene.tiled.layers.TmxImageLayer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sri Harsha Chilakapati
 */
public abstract class TmxMapRenderer
{
    protected TmxMap map;
    protected Map<String, Texture> textureMap;

    public static TmxMapRenderer create(TmxMap map)
    {
        if (map.getOrientation() == TmxMap.Orientation.ORTHOGONAL)
            return new TmxOrthogonalMapRenderer(map);

        return null;
    }

    public TmxMapRenderer(TmxMap map)
    {
        textureMap = new HashMap<>();

        this.map = map;

        for (TmxTileSet tileSet : map.getTileSets())
        {
            FilePath path = tileSet.getImage().getSource();

            if (!textureMap.containsKey(path.getAbsolutePath()))
                textureMap.put(path.getAbsolutePath(), Texture.fromFilePath(path));
        }

        for (TmxImageLayer imageLayer : map.getImageLayers())
        {
            FilePath path = imageLayer.getImage().getSource();

            if (!textureMap.containsKey(path.getAbsolutePath()))
                textureMap.put(path.getAbsolutePath(), Texture.fromFilePath(path));
        }
    }

    public void render(Batcher batcher)
    {
        renderImageLayers(batcher);
        renderTileLayers(batcher);
    }

    public abstract void renderImageLayers(Batcher batcher, int... layerIDs);

    public abstract void renderTileLayers(Batcher batcher, int... layerIDs);

    public void dispose()
    {
        textureMap.values().forEach(Texture::dispose);
    }

    public TmxMap getMap()
    {
        return map;
    }
}
