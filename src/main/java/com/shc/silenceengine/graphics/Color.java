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

package com.shc.silenceengine.graphics;

import com.shc.silenceengine.math.Vector4;
import com.shc.silenceengine.utils.MathUtils;
import com.shc.silenceengine.utils.ReusableStack;

/**
 * A Color class with RGBA colors. Has 140 predefined colors from <a href=http://www.w3schools.com/cssref/css_colornames.asp>http://www.w3schools.com/cssref/css_colornames.asp</a>
 *
 * @author Sri Harsha Chilakapati
 */
public class Color extends Vector4
{
    public static final ReusableStack<Color> REUSABLE_STACK = new ReusableStack<>(Color.class);

    // Primitive colors
    public static final Color BLACK = new Color(0x000000);
    public static final Color RED   = new Color(0xFF0000);
    public static final Color GREEN = new Color(0x00FF00);
    public static final Color BLUE  = new Color(0x0000FF);
    public static final Color WHITE = new Color(0xFFFFFF);

    // Other colors (legal HTML colors) from
    // http://www.w3schools.com/cssref/css_colornames.asp

    public static final Color ALICE_BLUE          = new Color(0xF0F8FF);
    public static final Color ANTIQUE_WHITE       = new Color(0xFAEBD7);
    public static final Color AQUA                = new Color(0x00FFFF);
    public static final Color AQUA_MARINE         = new Color(0x7FFFD4);
    public static final Color AZURE               = new Color(0xF0FFFF);
    public static final Color BEIGE               = new Color(0xF5F5DC);
    public static final Color BISQUE              = new Color(0xFFE4CE);
    public static final Color BLANCHED_ALMOND     = new Color(0xFFEBCD);
    public static final Color BLUE_VIOLET         = new Color(0x8A2BE2);
    public static final Color BROWN               = new Color(0xA52A2A);
    public static final Color BURLY_WOOD          = new Color(0xDEB887);
    public static final Color CADET_BLUE          = new Color(0x5F9EA0);
    public static final Color CHART_REUSE         = new Color(0x7FFF00);
    public static final Color CHOCOLATE           = new Color(0xD2691E);
    public static final Color CORAL               = new Color(0xFF7F50);
    public static final Color CORN_FLOWER_BLUE    = new Color(0x6495ED);
    public static final Color CORN_SILK           = new Color(0xFFF8DC);
    public static final Color CRIMSON             = new Color(0xDC143C);
    public static final Color CYAN                = new Color(0x00FFFF);
    public static final Color DARK_BLUE           = new Color(0x00008B);
    public static final Color DARK_CYAN           = new Color(0x008B8B);
    public static final Color DARK_GOLDEN_ROD     = new Color(0xB8860B);
    public static final Color DARK_GRAY           = new Color(0xA9A9A9);
    public static final Color DARK_GREEN          = new Color(0x006400);
    public static final Color DARK_KHAKI          = new Color(0xBDB76B);
    public static final Color DARK_MAGENTA        = new Color(0x8B008B);
    public static final Color DARK_OLIVE_GREEN    = new Color(0x556B2F);
    public static final Color DARK_ORANGE         = new Color(0xFF8C00);
    public static final Color DARK_ORCHID         = new Color(0x9932CC);
    public static final Color DARK_RED            = new Color(0x8B0000);
    public static final Color DARK_SALMON         = new Color(0xE9967A);
    public static final Color DARK_SEA_GREEN      = new Color(0x8FBC8F);
    public static final Color DARK_SLATE_BLUE     = new Color(0x483D8B);
    public static final Color DARK_SLATE_GRAY     = new Color(0x2F4F4F);
    public static final Color DARK_TURQUOISE      = new Color(0x00CED1);
    public static final Color DARK_VIOLET         = new Color(0x9400D3);
    public static final Color DEEP_PINK           = new Color(0xFF1493);
    public static final Color DEEP_SKY_BLUE       = new Color(0x00BFFF);
    public static final Color DIM_GRAY            = new Color(0x696969);
    public static final Color DODGER_BLUE         = new Color(0x1E90FF);
    public static final Color FIRE_BRICK          = new Color(0xB22222);
    public static final Color FLORAL_WHITE        = new Color(0xFFFAF0);
    public static final Color FOREST_GREEN        = new Color(0x228B22);
    public static final Color FUHSIA              = new Color(0xFF00FF);
    public static final Color GAINSBORO           = new Color(0xDCDCDC);
    public static final Color GHOST_WHITE         = new Color(0xF8F8FF);
    public static final Color GOLD                = new Color(0xFFD700);
    public static final Color GOLDEN_ROD          = new Color(0xDAA520);
    public static final Color GRAY                = new Color(0x808080);
    public static final Color GREEN_YELLOW        = new Color(0xADFF2F);
    public static final Color HONEY_DEW           = new Color(0xF0FFF0);
    public static final Color HOT_PINK            = new Color(0xFF69B4);
    public static final Color INDIAN_RED          = new Color(0xCD5C5C);
    public static final Color INDIGO              = new Color(0x4B0082);
    public static final Color IVORY               = new Color(0xFFFFF0);
    public static final Color KHAKI               = new Color(0xF0E68C);
    public static final Color LAVENDER            = new Color(0xE6E6FA);
    public static final Color LAVENDER_BLUSH      = new Color(0xFFF0F5);
    public static final Color LAWN_GREEN          = new Color(0x7CFC00);
    public static final Color LEMON_CHIFFON       = new Color(0xFFFACD);
    public static final Color LIGHT_BLUE          = new Color(0xADD8E6);
    public static final Color LIGHT_CORAL         = new Color(0xF08080);
    public static final Color LIGHT_CYAN          = new Color(0xE0FFFF);
    public static final Color LIGHT_GOLDEN_ROD    = new Color(0xFAFAD2);  // Actually LIGHT_GOLDEN_ROD_YELLOW
    public static final Color LIGHT_GRAY          = new Color(0xD3D3D3);
    public static final Color LIGHT_GREEN         = new Color(0x90EE90);
    public static final Color LIGHT_PINK          = new Color(0xFFB6C1);
    public static final Color LIGHT_SALMON        = new Color(0xFFA07A);
    public static final Color LIGHT_SEA_GREEN     = new Color(0x20B2AA);
    public static final Color LIGHT_SKY_BLUE      = new Color(0x87CEFA);
    public static final Color LIGHT_SLATE_GRAY    = new Color(0x778899);
    public static final Color LIGHT_STEEL_BLUE    = new Color(0xB0C4DE);
    public static final Color LIGHT_YELLOW        = new Color(0xFFFFE0);
    public static final Color LIME                = new Color(0x00FF00);
    public static final Color LIME_GREEN          = new Color(0x32CD32);
    public static final Color LINEN               = new Color(0xFAF0E6);
    public static final Color MAGENTA             = new Color(0xFF00FF);
    public static final Color MAROON              = new Color(0x800000);
    public static final Color MEDIUM_AQUA_MARINE  = new Color(0x66CDAA);
    public static final Color MEDIUM_BLUE         = new Color(0x0000CD);
    public static final Color MEDIUM_ORCHID       = new Color(0xBA55D3);
    public static final Color MEDIUM_PURPLE       = new Color(0x9370DB);
    public static final Color MEDIUM_SEA_GREEN    = new Color(0x3CB371);
    public static final Color MEDIUM_SLATE_BLUE   = new Color(0x7B68EE);
    public static final Color MEDIUM_SPRING_GREEN = new Color(0x00FA9A);
    public static final Color MEDIUM_TURQUOISE    = new Color(0x48D1CC);
    public static final Color MEDIUM_VIOLET_RED   = new Color(0xC71585);
    public static final Color MIDNIGHT_BLUE       = new Color(0x191970);
    public static final Color MINT_CREAM          = new Color(0xF5FFFA);
    public static final Color MISTY_ROSE          = new Color(0xFFE4E1);
    public static final Color MOCCASIN            = new Color(0xFFE4B5);
    public static final Color NOVAJO_WHITE        = new Color(0xFFDEAD);
    public static final Color NAVY                = new Color(0x000080);
    public static final Color OLD_LACE            = new Color(0xFDF5E6);
    public static final Color OLIVE               = new Color(0x808000);
    public static final Color OLIVE_DRAB          = new Color(0x6B8E23);
    public static final Color ORANGE              = new Color(0xFFA500);
    public static final Color ORANGE_RED          = new Color(0xFF4500);
    public static final Color ORCHID              = new Color(0xDA70D6);
    public static final Color PALE_GOLDEN_ROD     = new Color(0xEEE8AA);
    public static final Color PALE_GREEN          = new Color(0x98FB98);
    public static final Color PALE_TURQUOISE      = new Color(0xAFEEEE);
    public static final Color PALE_VIOLET_RED     = new Color(0xDB7093);
    public static final Color PAPAYA_WHIP         = new Color(0xFFEFD5);
    public static final Color PEACH_PUFF          = new Color(0xFFDAB9);
    public static final Color PERU                = new Color(0xCD853F);
    public static final Color PINK                = new Color(0xFFC0CB);
    public static final Color PLUM                = new Color(0xDDA0DD);
    public static final Color POWDER_BLUE         = new Color(0xDDA0DD);
    public static final Color PURPLE              = new Color(0x800080);
    public static final Color ROSY_BROWN          = new Color(0xBC8F8F);
    public static final Color ROYAL_BLUE          = new Color(0x4169E1);
    public static final Color SADDLE_BROWN        = new Color(0x8B4513);
    public static final Color SALMON              = new Color(0xFA8072);
    public static final Color SANDY_BROWN         = new Color(0xF4AF60);
    public static final Color SEA_GREEN           = new Color(0x2E8B57);
    public static final Color SEA_SHELL           = new Color(0xFFF5EE);
    public static final Color SIENNA              = new Color(0xA0522D);
    public static final Color SILVER              = new Color(0xC0C0C0);
    public static final Color SKY_BLUE            = new Color(0x87CEEB);
    public static final Color SLATE_BLUE          = new Color(0x6A5ACD);
    public static final Color SLATE_GRAY          = new Color(0x708090);
    public static final Color SNOW                = new Color(0xFFFAFA);
    public static final Color SPRING_GREEN        = new Color(0x00FF7F);
    public static final Color STEEL_BLUE          = new Color(0x4682B4);
    public static final Color TAN                 = new Color(0xD2B48C);
    public static final Color TEAL                = new Color(0x008080);
    public static final Color THISTLE             = new Color(0xD8BFD8);
    public static final Color TOMATO              = new Color(0xFF6347);
    public static final Color TURQUOISE           = new Color(0x40E0D0);
    public static final Color VIOLET              = new Color(0xEE82EE);
    public static final Color WHEAT               = new Color(0xF5DEB3);
    public static final Color WHITE_SMOKE         = new Color(0xF5F5F5);
    public static final Color YELLOW              = new Color(0xFFFF00);
    public static final Color YELLOW_GREEN        = new Color(0x9ACD32);

    public static final Color TRANSPARENT = new Color(0x00000000);

    public Color()
    {
        this(0, 0, 0, 1);
    }

    public Color(float r, float g, float b, float a)
    {
        set(r, g, b, a);
    }

    public Color set(float r, float g, float b, float a)
    {
        x = MathUtils.clamp(r, 0, 1);
        y = MathUtils.clamp(g, 0, 1);
        z = MathUtils.clamp(b, 0, 1);
        w = MathUtils.clamp(a, 0, 1);

        return this;
    }

    public Color add(float r, float g, float b, float a)
    {
        return new Color(x + r, y + g, z + b, w + a);
    }

    public Color addSelf(float r, float g, float b, float a)
    {
        return set(x + r, y + g, z + b, w + a);
    }

    public Color copy()
    {
        return new Color(x, y, z, w);
    }

    public Color subtract(float r, float g, float b, float a)
    {
        return add(-r, -g, -b, -a);
    }

    public Color subtractSelf(float r, float g, float b, float a)
    {
        return addSelf(-r, -g, -b, -a);
    }

    public Color(int rgba)
    {
        set(rgba);
    }

    public Color set(int rgba)
    {
        float r = ((rgba & 0x00FF0000) >> 16) / 255f;
        float g = ((rgba & 0x0000FF00) >> 8) / 255f;
        float b = (rgba & 0x000000FF) / 255f;
        float a = 1 - ((rgba & 0xFF000000) >> 24) / 255f;

        setR(r);
        setG(g);
        setB(b);
        setA(a);

        return this;
    }

    public Color(float r, float g, float b)
    {
        this(r, g, b, 1);
    }

    public static Color random()
    {
        return new Color((float) Math.random(), (float) Math.random(), (float) Math.random(), (float) Math.random());
    }

    public Color set(float r, float g, float b)
    {
        return set(r, g, b, 1);
    }

    public Color set(Color color)
    {
        return set(color.x, color.y, color.z, color.w);
    }

    public Color add(Color c)
    {
        return new Color(x + c.x, y + c.y, z + c.z, w + c.w);
    }

    public Color addSelf(Color c)
    {
        return set(x + c.x, y + c.y, z + c.z, w + c.w);
    }

    public Color add(float r, float g, float b)
    {
        return add(r, g, b, 1);
    }

    public Color addSelf(float r, float g, float b)
    {
        return addSelf(r, g, b, 1);
    }

    public Color subtract(Color c)
    {
        return add(-c.x, -c.y, -c.z, -c.w);
    }

    public Color subtractSelf(Color c)
    {
        return addSelf(-c.x, -c.y, -c.z, -c.w);
    }

    public Color subtract(float r, float g, float b)
    {
        return subtract(r, g, b, 1);
    }

    public Color subtractSelf(float r, float g, float b)
    {
        return subtractSelf(r, g, b, 1);
    }

    public Color multiply(Color c)
    {
        return new Color(x * c.x, y * c.y, z * c.z, w * c.w);
    }

    public Color multiplySelf(Color c)
    {
        return set(x * c.x, y * c.y, z * c.z, w * c.w);
    }

    public Color multiply(float r, float g, float b)
    {
        return multiply(r, g, b, 1);
    }

    public Color multiply(float r, float g, float b, float a)
    {
        return new Color(x * r, y * g, z * b, w * a);
    }

    public Color multiplySelf(float r, float g, float b)
    {
        return multiplySelf(r, g, b, 1);
    }

    public Color multiplySelf(float r, float g, float b, float a)
    {
        return set(x * r, y * g, z * b, w * a);
    }

    public float getRed()
    {
        return getR();
    }

    public void setRed(float r)
    {
        setR(r);
    }

    public float getGreen()
    {
        return getG();
    }

    public void setGreen(float g)
    {
        setG(g);
    }

    public float getBlue()
    {
        return getB();
    }

    public void setBlue(float b)
    {
        setB(b);
    }

    public float getAlpha()
    {
        return getA();
    }

    public void setAlpha(float a)
    {
        setA(a);
    }

    public Color randomSelf()
    {
        return set((float) Math.random(), (float) Math.random(), (float) Math.random(), (float) Math.random());
    }
}
