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

package com.shc.silenceengine.graphics.fonts;

import com.shc.easyxml.Xml;
import com.shc.easyxml.XmlTag;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.Vector4;
import com.shc.silenceengine.utils.functional.SimpleCallback;
import com.shc.silenceengine.utils.functional.UniCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sri harsha Chilakapati
 */
public class BitmapFont implements IFont
{
    public final Map<Integer, Texture> pages = new HashMap<>();
    public final Map<Integer, Char>    chars = new HashMap<>();

    private final Map<Char, Map<Char, Integer>> kerningPairs = new HashMap<>();

    public final Info   info;
    public final Common common;

    private boolean hadKerning = false;

    private BitmapFont(Info info, Common common)
    {
        this.info = info;
        this.common = common;
    }

    public int getKerning(Char first, Char second)
    {
        return hadKerning ? kerningPairs.get(first).get(second) : 0;
    }

    public static void load(FilePath fontDesc, UniCallback<BitmapFont> callback)
    {
        SilenceEngine.io.getFileReader().readTextFile(fontDesc, xmlString ->
        {
            XmlTag font = Xml.parse(xmlString);

            Info info = new Info();
            XmlTag infoTag = font.getTagsByName("info").get(0);

            info.face = infoTag.getAttribute("face").value;
            info.size = Integer.parseInt(infoTag.getAttribute("size").value);
            info.bold = Boolean.parseBoolean(infoTag.getAttribute("bold").value);
            info.italic = Boolean.parseBoolean(infoTag.getAttribute("italic").value);
            info.charset = infoTag.getAttribute("charset").value;
            info.unicode = Boolean.parseBoolean(infoTag.getAttribute("unicode").value);
            info.stretchH = Double.parseDouble(infoTag.getAttribute("stretchH").value);
            info.smooth = Boolean.parseBoolean(infoTag.getAttribute("smooth").value);
            info.aa = Integer.parseInt(infoTag.getAttribute("aa").value);

            String[] padding = infoTag.getAttribute("padding").value.split(",");
            String[] spacing = infoTag.getAttribute("spacing").value.split(",");

            info.padding.set(
                    Float.parseFloat(padding[0]),
                    Float.parseFloat(padding[1]),
                    Float.parseFloat(padding[2]),
                    Float.parseFloat(padding[3])
            );

            info.spacing.set(
                    Float.parseFloat(spacing[0]),
                    Float.parseFloat(spacing[1])
            );

            info.outline = Integer.parseInt(infoTag.getAttribute("outline").value);

            Common common = new Common();
            XmlTag commonTag = font.getTagsByName("common").get(0);

            common.lineHeight = Integer.parseInt(commonTag.getAttribute("lineHeight").value);
            common.base = Integer.parseInt(commonTag.getAttribute("base").value);
            common.scaleW = Integer.parseInt(commonTag.getAttribute("scaleW").value);
            common.scaleH = Integer.parseInt(commonTag.getAttribute("scaleH").value);
            common.pages = Integer.parseInt(commonTag.getAttribute("pages").value);
            common.packed = Integer.parseInt(commonTag.getAttribute("packed").value);
            common.alphaChnl = Integer.parseInt(commonTag.getAttribute("alphaChnl").value);
            common.redChnl = Integer.parseInt(commonTag.getAttribute("redChnl").value);
            common.greenChnl = Integer.parseInt(commonTag.getAttribute("greenChnl").value);
            common.blueChnl = Integer.parseInt(commonTag.getAttribute("blueChnl").value);

            BitmapFont bitmapFont = new BitmapFont(info, common);

            XmlTag charsTag = font.getTagsByName("chars").get(0);

            for (XmlTag charTag : charsTag.children)
            {
                Char fChar = new Char();

                fChar.id = Integer.parseInt(charTag.getAttribute("id").value);
                fChar.x = Integer.parseInt(charTag.getAttribute("x").value);
                fChar.y = Integer.parseInt(charTag.getAttribute("y").value);
                fChar.height = Integer.parseInt(charTag.getAttribute("height").value);
                fChar.width = Integer.parseInt(charTag.getAttribute("width").value);
                fChar.xOffset = Integer.parseInt(charTag.getAttribute("xoffset").value);
                fChar.yOffset = Integer.parseInt(charTag.getAttribute("yoffset").value);
                fChar.xAdvance = Integer.parseInt(charTag.getAttribute("xadvance").value);
                fChar.page = Integer.parseInt(charTag.getAttribute("page").value);
                fChar.chnl = Integer.parseInt(charTag.getAttribute("chnl").value);

                bitmapFont.chars.put(fChar.id, fChar);
            }

            List<XmlTag> kerningTags = font.getTagsByName("kernings");
            if (kerningTags.size() == 1)
            {
                // There is kerning support for this font.
                bitmapFont.hadKerning = true;

                // Initialize kerning pairs
                for (Char a : bitmapFont.chars.values())
                {
                    bitmapFont.kerningPairs.put(a, new HashMap<>());

                    for (Char b : bitmapFont.chars.values())
                        bitmapFont.kerningPairs.get(a).put(b, 0);
                }

                for (XmlTag kerningTag : kerningTags.get(0).children)
                {
                    int first = Integer.parseInt(kerningTag.getAttribute("first").value);
                    int second = Integer.parseInt(kerningTag.getAttribute("second").value);
                    int amount = Integer.parseInt(kerningTag.getAttribute("amount").value);

                    Char fChar = bitmapFont.chars.get(first);
                    Char sChar = bitmapFont.chars.get(second);

                    bitmapFont.kerningPairs.get(fChar).put(sChar, amount);
                }
            }

            XmlTag pagesTag = font.getTagsByName("pages").get(0);

            SimpleCallback loadingCallback = () -> callback.invoke(bitmapFont);

            for (XmlTag page : pagesTag.children)
            {
                int id = Integer.parseInt(page.getAttribute("id").value);
                FilePath file = fontDesc.getParent().getChild(page.getAttribute("file").value);

                // Just because variable used in lambda should be effectively final.
                SimpleCallback finalLoadingCallback = loadingCallback;

                // Reassign the loading callback so the callbacks stack up dynamically.
                loadingCallback = () ->
                        SilenceEngine.io.getImageReader().readImage(file, image ->
                        {
                            Texture texture = Texture.fromImage(image);
                            bitmapFont.pages.put(id, texture);

                            image.dispose();

                            // Invoke the next callback
                            finalLoadingCallback.invoke();
                        });
            }

            loadingCallback.invoke();
        });
    }

    public void dispose()
    {
        for (Texture page : pages.values())
            page.dispose();
    }

    @Override
    public float getWidth(String text)
    {
        float x = 0;
        float width = 0;

        Char last = null;

        for (char ch : text.toCharArray())
        {
            if (ch == '\n')
            {
                width = Math.max(x, width);
                x = 0;
                last = null;
                continue;
            }

            Char curr = chars.get((int) ch);
            curr = curr == null ? chars.get((int) ' ') : curr;

            x += curr.xAdvance;

            if (last != null)
                x += getKerning(last, curr);

            last = curr;
        }

        return Math.max(x, width);
    }

    @Override
    public float getHeight()
    {
        return common.lineHeight;
    }

    public static class Info
    {
        public String  face;
        public int     size;
        public boolean bold;
        public boolean italic;
        public String  charset;
        public boolean unicode;
        public double  stretchH;
        public boolean smooth;
        public int     aa;

        public final Vector4 padding = new Vector4();
        public final Vector2 spacing = new Vector2();

        public int outline;
    }

    public static class Common
    {
        public int lineHeight;
        public int base;
        public int scaleW;
        public int scaleH;
        public int pages;
        public int packed;
        public int alphaChnl;
        public int redChnl;
        public int greenChnl;
        public int blueChnl;
    }

    public static class Char
    {
        private int id;
        public  int x;
        public  int y;
        public  int height;
        public  int width;
        public  int xOffset;
        public  int yOffset;
        public  int xAdvance;
        public  int page;
        public  int chnl;
    }
}
