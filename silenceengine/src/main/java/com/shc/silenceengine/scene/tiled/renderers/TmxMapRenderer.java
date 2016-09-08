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

package com.shc.silenceengine.scene.tiled.renderers;

import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.graphics.DynamicRenderer;
import com.shc.silenceengine.graphics.opengl.GLContext;
import com.shc.silenceengine.graphics.opengl.Primitive;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.io.ImageReader;
import com.shc.silenceengine.scene.tiled.TmxMap;
import com.shc.silenceengine.scene.tiled.TmxTileSet;
import com.shc.silenceengine.scene.tiled.layers.TmxImageLayer;
import com.shc.silenceengine.scene.tiled.layers.TmxMapLayer;
import com.shc.silenceengine.scene.tiled.layers.TmxTileLayer;
import com.shc.silenceengine.scene.tiled.tiles.TmxAnimationFrame;
import com.shc.silenceengine.scene.tiled.tiles.TmxTile;
import com.shc.silenceengine.utils.TimeUtils;
import com.shc.silenceengine.utils.functional.SimpleCallback;
import com.shc.silenceengine.utils.functional.UniCallback;

import java.util.HashMap;
import java.util.Map;

import static com.shc.silenceengine.graphics.IGraphicsDevice.Constants.*;

/**
 * @author Sri Harsha Chilakapati
 */
public abstract class TmxMapRenderer
{
    protected TmxMap map;

    protected Map<String, Texture>       textureMap;
    protected Map<TmxTile, TileAnimator> tileAnimators;

    public static void create(TmxMap map, UniCallback<TmxMapRenderer> callback)
    {
        switch (map.getOrientation())
        {
            case ISOMETRIC:
                TmxIsometricMapRenderer.create(map, callback);
                break;
            case ORTHOGONAL:
                TmxOrthogonalMapRenderer.create(map, callback);
                break;

            default:
                throw new SilenceException("A TmxMapRenderer has not yet been implemented for "
                                           + map.getOrientation() + " orientation");
        }
    }

    protected void init(TmxMap map, SimpleCallback onInit)
    {
        textureMap = new HashMap<>();
        tileAnimators = new HashMap<>();

        this.map = map;

        SimpleCallback finishCallback = onInit;
        ImageReader imageReader = SilenceEngine.io.getImageReader();

        for (TmxTileSet tileSet : map.getTileSets())
        {
            FilePath path = tileSet.getImage().getSource();

            if (!textureMap.containsKey(path.getAbsolutePath()))
            {
                SimpleCallback lastFinishCallback = finishCallback;

                finishCallback = () ->
                        imageReader.readImage(path, image ->
                        {
                            textureMap.put(path.getAbsolutePath(), Texture.fromImage(image));
                            image.dispose();

                            lastFinishCallback.invoke();
                        });
            }

            for (TmxTile tile : tileSet.getTiles())
            {
                if (tile.isAnimated())
                {
                    TileAnimator animator = new TileAnimator(tile);
                    tileAnimators.put(tile, animator);
                }
            }
        }

        for (TmxImageLayer imageLayer : map.getImageLayers())
        {
            FilePath path = imageLayer.getImage().getSource();

            if (!textureMap.containsKey(path.getAbsolutePath()))
            {
                SimpleCallback lastFinishCallback = finishCallback;

                finishCallback = () ->
                        imageReader.readImage(path, image ->
                        {
                            textureMap.put(path.getAbsolutePath(), Texture.fromImage(image));
                            image.dispose();

                            lastFinishCallback.invoke();
                        });
            }
        }

        finishCallback.invoke();
    }

    public void update(float delta)
    {
        for (TileAnimator animator : tileAnimators.values())
            animator.update(delta);
    }

    public void render(DynamicRenderer renderer)
    {
        GLContext.enable(GL_BLEND);
        GLContext.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        renderBackgroundColor(renderer);

        // Render the image and tile layers
        for (TmxMapLayer mapLayer : map.getLayers())
        {
            if (mapLayer instanceof TmxTileLayer)
                renderTileLayer(renderer, (TmxTileLayer) mapLayer);

            if (mapLayer instanceof TmxImageLayer)
                renderImageLayer(renderer, (TmxImageLayer) mapLayer);
        }
    }

    protected void renderBackgroundColor(DynamicRenderer renderer)
    {
        // Render the background rectangle
        renderer.begin(Primitive.TRIANGLE_FAN);
        {
            renderer.vertex(0, 0);
            renderer.color(map.getBackgroundColor());

            renderer.vertex(map.getWidth() * map.getTileWidth(), 0);
            renderer.color(map.getBackgroundColor());

            renderer.vertex(map.getWidth() * map.getTileWidth(), map.getHeight() * map.getTileHeight());
            renderer.color(map.getBackgroundColor());

            renderer.vertex(0, map.getHeight() * map.getTileHeight());
            renderer.color(map.getBackgroundColor());
        }
        renderer.end();
    }

    public void renderImageLayers(DynamicRenderer renderer, int... layerIDs)
    {
        if (layerIDs == null || layerIDs.length == 0)
        {
            for (TmxImageLayer imageLayer : map.getImageLayers())
                renderImageLayer(renderer, imageLayer);
        }
        else
        {
            for (int layerIndex : layerIDs)
            {
                if (layerIndex < map.getNumImageLayers())
                    renderImageLayer(renderer, map.getImageLayer(layerIndex));
            }
        }
    }

    protected abstract void renderImageLayer(DynamicRenderer renderer, TmxImageLayer imageLayer);

    public void renderTileLayers(DynamicRenderer renderer, int... layerIDs)
    {
        if (layerIDs == null || layerIDs.length == 0)
        {
            for (TmxTileLayer tileLayer : map.getTileLayers())
                renderTileLayer(renderer, tileLayer);
        }
        else
        {
            for (int layerIndex : layerIDs)
            {
                if (layerIndex > map.getNumTileLayers())
                    renderTileLayer(renderer, map.getTileLayer(layerIndex));
            }
        }
    }

    protected abstract void renderTileLayer(DynamicRenderer renderer, TmxTileLayer tileLayer);

    public void dispose()
    {
        for (Texture texture : textureMap.values())
            texture.dispose();
    }

    public TmxMap getMap()
    {
        return map;
    }

    protected static class TileAnimator
    {
        private TmxTile tile;

        private int   currentFrameIndex;
        private float elapsedDuration;

        public TileAnimator(TmxTile tile)
        {
            this.tile = tile;
            elapsedDuration = 0;
            currentFrameIndex = 0;
        }

        public void update(float delta)
        {
            elapsedDuration += TimeUtils.convert(delta, TimeUtils.getDefaultTimeUnit(), TimeUtils.Unit.MILLIS);

            if (elapsedDuration >= tile.getFrames().get(currentFrameIndex).getDuration())
            {
                currentFrameIndex = (currentFrameIndex + 1) % tile.getFrames().size();
                elapsedDuration = 0;
            }
        }

        public TmxAnimationFrame getCurrentFrame()
        {
            return tile.getFrames().get(currentFrameIndex);
        }
    }
}
