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

import com.shc.silenceengine.graphics.DynamicRenderer;
import com.shc.silenceengine.graphics.opengl.Primitive;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.scene.tiled.TmxMap;
import com.shc.silenceengine.scene.tiled.TmxTileSet;
import com.shc.silenceengine.scene.tiled.layers.TmxImageLayer;
import com.shc.silenceengine.scene.tiled.layers.TmxTileLayer;
import com.shc.silenceengine.scene.tiled.tiles.TmxMapTile;
import com.shc.silenceengine.scene.tiled.tiles.TmxTile;
import com.shc.silenceengine.utils.functional.UniCallback;

/**
 * @author Sri Harsha Chilakapati
 */
public class TmxOrthogonalMapRenderer extends TmxMapRenderer
{
    public static void create(TmxMap map, UniCallback<TmxMapRenderer> callback)
    {
        TmxOrthogonalMapRenderer renderer = new TmxOrthogonalMapRenderer();
        renderer.init(map, () -> callback.invoke(renderer));
    }

    protected void renderImageLayer(DynamicRenderer renderer, TmxImageLayer imageLayer)
    {
        if (!imageLayer.isVisible())
            return;

        Texture originalTexture = Texture.CURRENT;

        textureMap.get(imageLayer.getImage().getSource().getAbsolutePath()).bind();
        renderer.begin(Primitive.TRIANGLE_FAN);
        {
            renderer.vertex(imageLayer.getX(), imageLayer.getY());
            renderer.texCoord(0, 0);

            renderer.vertex(imageLayer.getX() + imageLayer.getWidth() * map.getTileWidth(), imageLayer.getY());
            renderer.texCoord(1, 0);

            renderer.vertex(imageLayer.getX() + imageLayer.getWidth() * map.getTileWidth(), imageLayer.getY() + imageLayer.getHeight() * map.getTileHeight());
            renderer.texCoord(1, 1);

            renderer.vertex(imageLayer.getX(), imageLayer.getY() + imageLayer.getHeight() * map.getTileHeight());
            renderer.texCoord(0, 1);
        }
        renderer.end();

        originalTexture.bind();
    }

    protected void renderTileLayer(DynamicRenderer renderer, TmxTileLayer tileLayer)
    {
        if (!tileLayer.isVisible())
            return;

        Texture original = Texture.CURRENT;
        Texture current = textureMap.get(map.getTileset(0).getImage().getSource().getAbsolutePath());
        current.bind();

        renderer.begin(Primitive.TRIANGLES);
        {
            for (int x = 0; x < tileLayer.getWidth(); x++)
            {
                for (int y = 0; y < tileLayer.getHeight(); y++)
                {
                    TmxMapTile mapTile = tileLayer.getTile(x, y);

                    if (mapTile.getTileSetID() == -1)
                        continue;

                    TmxTileSet tileSet = map.getTileset(mapTile.getTileSetID());
                    TmxTile tile = tileSet.getTile(mapTile.getGID() - tileSet.getFirstGID());

                    Texture texture = textureMap.get(tileSet.getImage().getSource().getAbsolutePath());

                    if (texture.getID() != current.getID())
                    {
                        renderer.end();
                        current = texture;
                        texture.bind();
                        renderer.begin(Primitive.TRIANGLES);
                    }

                    int tileID = mapTile.getGID() - tileSet.getFirstGID();
                    if (tile.isAnimated())
                        tileID = tileAnimators.get(tile).getCurrentFrame().getTileID();

                    int numColsPerRow = tileSet.getImage().getWidth() / tileSet.getTileWidth();

                    int tileSetCol = tileID % numColsPerRow;
                    int tileSetRow = tileID / numColsPerRow;

                    float tileWidth = map.getTileWidth();
                    float tileHeight = map.getTileHeight();

                    // The position of the tile in the world
                    float posX = x * tileWidth;
                    float posY = y * tileWidth;

                    // The clip space of the tile in the texture
                    float clipX = (tileSet.getMargin() + (tileSet.getTileWidth() + tileSet.getSpacing()) * tileSetCol);
                    float clipY = (tileSet.getMargin() + (tileSet.getTileHeight() + tileSet.getSpacing()) * tileSetRow);

                    // The texture coordinates of the tile
                    float minU = clipX / tileSet.getImage().getWidth();
                    float maxU = (clipX + tileWidth) / tileSet.getImage().getWidth();
                    float minV = clipY / tileSet.getImage().getHeight();
                    float maxV = (clipY + tileHeight) / tileSet.getImage().getHeight();

                    // Flip the texture coordinates to flip the tile
                    boolean flipX = mapTile.isFlippedHorizontally();
                    boolean flipY = mapTile.isFlippedVertically();
                    boolean flipZ = mapTile.isFlippedDiagonally();

                    if (flipZ)
                    {
                        flipX = !flipX;
                        flipY = !flipY;
                    }

                    if (flipX)
                    {
                        float temp = minU;
                        minU = maxU;
                        maxU = temp;
                    }

                    if (flipY)
                    {
                        float temp = minV;
                        minV = maxV;
                        maxV = temp;
                    }

                    float uvCorrectionX = (0.2f / tileSet.getImage().getWidth());
                    float uvCorrectionY = (0.2f / tileSet.getImage().getHeight());

                    renderer.flushOnOverflow(6);

                    // Draw the tile
                    renderer.vertex(posX, posY);
                    renderer.texCoord(minU + uvCorrectionX, minV + uvCorrectionY);

                    renderer.vertex(flipZ ? posX : posX + tileWidth, flipZ ? posY + tileHeight : posY);
                    renderer.texCoord(maxU - uvCorrectionX, minV + uvCorrectionY);

                    renderer.vertex(flipZ ? posX + tileWidth : posX, flipZ ? posY : posY + tileHeight);
                    renderer.texCoord(minU + uvCorrectionX, maxV - uvCorrectionY);

                    renderer.vertex(flipZ ? posX : posX + tileWidth, flipZ ? posY + tileHeight : posY);
                    renderer.texCoord(maxU - uvCorrectionX, minV + uvCorrectionY);

                    renderer.vertex(posX + tileWidth, posY + tileHeight);
                    renderer.texCoord(maxU - uvCorrectionX, maxV - uvCorrectionY);

                    renderer.vertex(flipZ ? posX + tileWidth : posX, flipZ ? posY : posY + tileHeight);
                    renderer.texCoord(minU + uvCorrectionX, maxV - uvCorrectionY);
                }
            }
        }
        renderer.end();

        original.bind();
    }
}
