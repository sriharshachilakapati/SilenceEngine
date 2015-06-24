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

import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.opengl.Primitive;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.scene.tiled.TmxMap;
import com.shc.silenceengine.scene.tiled.TmxTileSet;
import com.shc.silenceengine.scene.tiled.layers.TmxImageLayer;
import com.shc.silenceengine.scene.tiled.layers.TmxMapLayer;
import com.shc.silenceengine.scene.tiled.layers.TmxTileLayer;

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
        switch (map.getOrientation())
        {
            case ISOMETRIC:  return new TmxIsometricMapRenderer(map);
            case ORTHOGONAL: return new TmxOrthogonalMapRenderer(map);
        }

        throw new SilenceException("A TmxMapRenderer has not yet been implemented for "
                                   + map.getOrientation() + " orientation");
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
        // Render the background rectangle
        batcher.begin(Primitive.TRIANGLE_FAN);
        {
            batcher.vertex(0, 0);
            batcher.color(map.getBackgroundColor());

            batcher.vertex(map.getWidth() * map.getTileWidth(), 0);
            batcher.color(map.getBackgroundColor());

            batcher.vertex(map.getWidth() * map.getTileWidth(), map.getHeight() * map.getTileHeight());
            batcher.color(map.getBackgroundColor());

            batcher.vertex(0, map.getHeight() * map.getTileHeight());
            batcher.color(map.getBackgroundColor());
        }
        batcher.end();

        // Render the image and tile layers
        for (TmxMapLayer mapLayer : map.getLayers())
        {
            if (mapLayer instanceof TmxTileLayer)
                renderTileLayer(batcher, (TmxTileLayer) mapLayer);

            if (mapLayer instanceof TmxImageLayer)
                renderImageLayer(batcher, (TmxImageLayer) mapLayer);
        }
    }

    public void renderImageLayers(Batcher batcher, int... layerIDs)
    {
        if (layerIDs == null || layerIDs.length == 0)
        {
            for (TmxImageLayer imageLayer : map.getImageLayers())
                renderImageLayer(batcher, imageLayer);
        }
        else
        {
            for (int layerIndex : layerIDs)
            {
                if (layerIndex < map.getNumImageLayers())
                    renderImageLayer(batcher, map.getImageLayer(layerIndex));
            }
        }
    }

    protected abstract void renderImageLayer(Batcher batcher, TmxImageLayer imageLayer);

    public void renderTileLayers(Batcher batcher, int... layerIDs)
    {
        if (layerIDs == null || layerIDs.length == 0)
        {
            for (TmxTileLayer tileLayer : map.getTileLayers())
                renderTileLayer(batcher, tileLayer);
        }
        else
        {
            for (int layerIndex : layerIDs)
            {
                if (layerIndex > map.getNumTileLayers())
                    renderTileLayer(batcher, map.getTileLayer(layerIndex));
            }
        }
    }

    protected abstract void renderTileLayer(Batcher batcher, TmxTileLayer tileLayer);

    public void dispose()
    {
        textureMap.values().forEach(Texture::dispose);
    }

    public TmxMap getMap()
    {
        return map;
    }
}
