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
import com.shc.silenceengine.graphics.opengl.Primitive;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.scene.tiled.TmxMap;
import com.shc.silenceengine.scene.tiled.TmxTileSet;
import com.shc.silenceengine.scene.tiled.layers.TmxImageLayer;
import com.shc.silenceengine.scene.tiled.layers.TmxTileLayer;
import com.shc.silenceengine.scene.tiled.tiles.TmxMapTile;

/**
 * @author Sri Harsha Chilakapati
 */
public class TmxIsometricMapRenderer extends TmxMapRenderer
{
    private Vector2 tempVector = new Vector2();

    public TmxIsometricMapRenderer(TmxMap map)
    {
        super(map);
    }

    private Vector2 orthoToIso(float x, float y)
    {
        tempVector.x = (x - y) * map.getTileWidth() / 2;
        tempVector.y = (x + y) * map.getTileHeight() / 2;

        return tempVector.addSelf(map.getWidth() * map.getTileWidth() / 2, 0);
    }

    @Override
    protected void renderBackgroundColor(Batcher batcher)
    {
        // Render the background rectangle
        batcher.begin(Primitive.TRIANGLE_FAN);
        {
            batcher.vertex(orthoToIso(0, 0));
            batcher.color(map.getBackgroundColor());

            batcher.vertex(orthoToIso(map.getWidth(), 0));
            batcher.color(map.getBackgroundColor());

            batcher.vertex(orthoToIso(map.getWidth(), map.getHeight()));
            batcher.color(map.getBackgroundColor());

            batcher.vertex(orthoToIso(0, map.getHeight()));
            batcher.color(map.getBackgroundColor());
        }
        batcher.end();
    }

    @Override
    protected void renderImageLayer(Batcher batcher, TmxImageLayer imageLayer)
    {
        if (!imageLayer.isVisible())
            return;

        Texture originalTexture = Texture.CURRENT;

        textureMap.get(imageLayer.getImage().getSource().getAbsolutePath()).bind();
        batcher.begin(Primitive.TRIANGLE_FAN);
        {
            float tileWidth = map.getTileWidth();
            float tileHeight = map.getTileHeight();

            float posX = (imageLayer.getY() * tileWidth / 2) + (imageLayer.getX() * tileWidth / 2);
            float posY = (imageLayer.getX() * tileHeight / 2) - (imageLayer.getY() * tileHeight / 2);

            batcher.vertex(posX, posY);
            batcher.texCoord(0, 0);

            batcher.vertex(posX + imageLayer.getWidth() * map.getTileWidth(), posY);
            batcher.texCoord(1, 0);

            batcher.vertex(posX + imageLayer.getWidth() * map.getTileWidth(), imageLayer.getY() + imageLayer.getHeight() * map.getTileHeight());
            batcher.texCoord(1, 1);

            batcher.vertex(posX, imageLayer.getY() + imageLayer.getHeight() * map.getTileHeight());
            batcher.texCoord(0, 1);
        }
        batcher.end();

        originalTexture.bind();
    }

    @Override
    protected void renderTileLayer(Batcher batcher, TmxTileLayer tileLayer)
    {
        if (!tileLayer.isVisible())
            return;

        Texture original = Texture.CURRENT;
        Texture current = textureMap.get(map.getTileset(0).getImage().getSource().getAbsolutePath());
        current.bind();

        batcher.begin(Primitive.TRIANGLES);
        {
            for (int x = 0; x < tileLayer.getWidth(); x++)
            {
                for (int y = 0; y < tileLayer.getHeight(); y++)
                {
                    TmxMapTile mapTile = tileLayer.getTile(x, y);

                    if (mapTile.getTileSetID() == -1)
                        continue;

                    TmxTileSet tileSet = map.getTileset(mapTile.getTileSetID());

                    Texture texture = textureMap.get(tileSet.getImage().getSource().getAbsolutePath());

                    if (texture.getID() != current.getID())
                    {
                        batcher.end();
                        current = texture;
                        texture.bind();
                        batcher.begin(Primitive.TRIANGLES);
                    }

                    int numColsPerRow = tileSet.getImage().getWidth() / tileSet.getTileWidth();

                    int tileSetCol = (mapTile.getGID() - tileSet.getFirstGID()) % numColsPerRow;
                    int tileSetRow = (mapTile.getGID() - tileSet.getFirstGID()) / numColsPerRow;

                    float tileWidth = tileSet.getTileWidth();
                    float tileHeight = tileSet.getTileHeight();

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

                    float uvCorrectionX = (0.5f / tileSet.getImage().getWidth());
                    float uvCorrectionY = (0.5f / tileSet.getImage().getHeight());

                    // Draw the tile
                    batcher.vertex(orthoToIso(x, y).addSelf(-tileWidth / 2, 0).addSelf(0, 0));
                    batcher.texCoord(minU + uvCorrectionX, minV + uvCorrectionY);

                    batcher.vertex(orthoToIso(x, y).addSelf(-tileWidth / 2, 0).addSelf(flipZ ? 0 : tileWidth, flipZ ? tileHeight : 0));
                    batcher.texCoord(maxU - uvCorrectionX, minV + uvCorrectionY);

                    batcher.vertex(orthoToIso(x, y).addSelf(-tileWidth / 2, 0).addSelf(flipZ ? tileWidth : 0, flipZ ? 0 : tileHeight));
                    batcher.texCoord(minU + uvCorrectionX, maxV - uvCorrectionY);

                    batcher.vertex(orthoToIso(x, y).addSelf(-tileWidth / 2, 0).addSelf(flipZ ? 0 : tileWidth, flipZ ? tileHeight : 0));
                    batcher.texCoord(maxU - uvCorrectionX, minV + uvCorrectionY);

                    batcher.vertex(orthoToIso(x, y).addSelf(-tileWidth / 2, 0).addSelf(tileWidth, tileHeight));
                    batcher.texCoord(maxU - uvCorrectionX, maxV - uvCorrectionY);

                    batcher.vertex(orthoToIso(x, y).addSelf(-tileWidth / 2, 0).addSelf(flipZ ? tileWidth : 0, flipZ ? 0 : tileHeight));
                    batcher.texCoord(minU + uvCorrectionX, maxV - uvCorrectionY);
                }
            }
        }
        batcher.end();

        original.bind();
    }

    @Override
    public void dispose()
    {
        textureMap.values().forEach(Texture::dispose);
    }
}
