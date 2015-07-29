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

package com.shc.silenceengine.tests;

import com.shc.silenceengine.core.Display;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.cameras.OrthoCam;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.scene.tiled.TmxMap;
import com.shc.silenceengine.scene.tiled.TmxTileSet;
import com.shc.silenceengine.scene.tiled.layers.TmxObjectLayer;
import com.shc.silenceengine.scene.tiled.layers.TmxTileLayer;
import com.shc.silenceengine.scene.tiled.objects.TmxObject;
import com.shc.silenceengine.scene.tiled.objects.TmxPoint;
import com.shc.silenceengine.scene.tiled.objects.TmxPolyLine;
import com.shc.silenceengine.scene.tiled.objects.TmxPolygon;
import com.shc.silenceengine.scene.tiled.renderers.TmxMapRenderer;
import com.shc.silenceengine.scene.tiled.tiles.TmxAnimationFrame;
import com.shc.silenceengine.scene.tiled.tiles.TmxTile;
import com.shc.silenceengine.utils.Logger;

import java.io.IOException;

/**
 * @author Sri Harsha Chilakapati
 */
public class TmxMapTest extends Game
{
    private OrthoCam camera;
    private TmxMap   map;

    private TmxMapRenderer renderer;

    public TmxMapTest(TmxMap map)
    {
        super();
        this.map = map;
    }

    public static void main(String[] args) throws IOException
    {
        Logger.setPrintTimeStamps(false);
        Logger.addLogStream(FilePath.getExternalFile(System.getProperty("user.home") + "/TmxMapTest.log"));

        TmxMap map = new TmxMap(FilePath.getResourceFile("resources/example-isometric.tmx"));

        printHeader("Map");
        Logger.info("Version:          " + map.getVersion());
        Logger.info("Orientation:      " + map.getOrientation());
        Logger.info("Background Color: " + map.getBackgroundColor());
        Logger.info("Render Order:     " + map.getRenderOrder());
        Logger.info("Stagger Axis:     " + map.getStaggerAxis());
        Logger.info("Stagger Index:    " + map.getStaggerIndex());

        Logger.info("");

        Logger.info("Width:       " + map.getWidth());
        Logger.info("Height:      " + map.getHeight());
        Logger.info("Tile Width:  " + map.getTileWidth());
        Logger.info("Tile Height: " + map.getTileHeight());

        int i = 0;
        for (TmxTileSet tileSet : map.getTileSets())
        {
            printHeader("TileSet " + i);

            Logger.info("Name:         " + tileSet.getName());
            Logger.info("Margin:       " + tileSet.getMargin());
            Logger.info("Spacing:      " + tileSet.getSpacing());
            Logger.info("First GID:    " + tileSet.getFirstGID());
            Logger.info("Image Width:  " + tileSet.getImage().getWidth());
            Logger.info("Image Height: " + tileSet.getImage().getHeight());
            Logger.info("Image Source: " + tileSet.getImage().getSource());
            Logger.info("Trans Color:  " + tileSet.getImage().getTrans());

            for (TmxTile tile : tileSet.getTiles())
            {
                tile.getProperties().forEach((k, v) -> Logger.info(k + " => " + v));

                if (tile.isAnimated())
                {
                    Logger.info("Tile is animated: " + tile.getFrameCount() + " frames with total duration of " +
                                tile.getTotalDuration() + "ms");

                    int j = 0;
                    for (TmxAnimationFrame frame : tile.getFrames())
                    {
                        Logger.info(String.format("\tFrame %d: TileID = %d, Duration = %dms", j, frame.getTileID(), frame.getDuration()));
                        j++;
                    }
                }
            }

            i++;
        }

        i = 0;
        for (TmxTileLayer tileLayer : map.getTileLayers())
        {
            printHeader(String.format("Tile Layer: %02d Name: %s", i, tileLayer.getName()));

            for (int y = 0; y < tileLayer.getHeight(); y++)
            {
                String info = "";

                for (int x = 0; x < tileLayer.getWidth(); x++)
                {
                    if (tileLayer.getTileTileSetIndex(x, y) != -1)
                    {
                        info += String.format("%03d(%03d) ", tileLayer.getTileID(x, y), tileLayer.getTileGID(x, y));

                        if (tileLayer.isTileFlippedHorizontally(x, y))
                            info += "h";
                        else
                            info += " ";

                        if (tileLayer.isTileFlippedVertically(x, y))
                            info += "v";
                        else
                            info += " ";

                        if (tileLayer.isTileFlippedDiagonally(x, y))
                            info += "d";
                        else
                            info += " ";
                    }
                    else
                        info += "........    ";
                }

                Logger.info(info);
            }

            i++;
        }

        i = 0;
        for (TmxObjectLayer objectLayer : map.getObjectLayers())
        {
            printHeader(String.format("Object Layer: %02d Name: %s", i, objectLayer.getName()));

            int j = 0;
            for (TmxObject object : objectLayer.getObjects())
            {
                Logger.info("Object " + j);
                Logger.info("Name: " + object.getName());
                Logger.info("Position: [" + object.getX() + ", " + object.getY() + "]");
                Logger.info("Size: [" + object.getWidth() + ", " + object.getHeight() + "]");

                TmxPolygon polygon = object.getPolygon();
                if (polygon != null)
                {
                    for (int k = 0; k < polygon.getNumPoints(); k++)
                    {
                        TmxPoint point = polygon.getPoint(k);
                        Logger.info("Polygon point " + k + " [" + point.x + ", " + point.y + "]");
                    }
                }

                TmxPolyLine polyLine = object.getPolyLine();
                if (polyLine != null)
                {
                    for (int k = 0; k < polyLine.getNumPoints(); k++)
                    {
                        TmxPoint point = polyLine.getPoint(k);
                        Logger.info("Polygon point " + k + " [" + point.x + ", " + point.y + "]");
                    }
                }

                j++;
            }

            i++;
        }

        new TmxMapTest(map).start();
    }

    private static void printHeader(String title)
    {
        Logger.info("======================================================================================");
        Logger.info(title);
        Logger.info("======================================================================================");
    }

    @Override
    public void init()
    {
        camera = new OrthoCam(Display.getWidth(), Display.getHeight());
        renderer = TmxMapRenderer.create(map);

        SilenceEngine.graphics.setClearColor(Color.GRAY);
    }

    @Override
    public void resize()
    {
        camera.initProjection(Display.getWidth(), Display.getHeight());
        camera.center(map.getWidth() * map.getTileWidth() / 2, map.getHeight() * map.getTileHeight() / 2);
    }

    @Override
    public void update(float delta)
    {
        if (Keyboard.isClicked(Keyboard.KEY_ESCAPE))
            Game.end();

        Display.setTitle("SilenceEngine TmxMapTest | FPS: " + getFPS() +
                         " | UPS: " + getUPS() + " | RC: " + SilenceEngine.graphics.renderCallsPerFrame);
    }

    @Override
    public void render(float delta, Batcher batcher)
    {
        camera.apply();
        renderer.render(batcher);
    }

    @Override
    public void dispose()
    {
        renderer.dispose();
    }
}
