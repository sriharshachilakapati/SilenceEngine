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

package com.shc.silenceengine.scene.tiled;

import com.shc.easyxml.XmlTag;
import com.shc.silenceengine.utils.MathUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Sri Harsha Chilakapati
 */
public class TmxProperties
{
    private Map<String, Object> properties;

    public TmxProperties()
    {
        properties = new HashMap<>();
    }

    public TmxProperties put(String key, Object value)
    {
        properties.put(key, value);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key)
    {
        return (T) properties.get(key);
    }

    public <T> T get(String key, T defaultValue)
    {
        T value = get(key);
        return value == null ? defaultValue : value;
    }

    public boolean contains(String key)
    {
        return properties.containsKey(key);
    }

    public TmxProperties clear()
    {
        properties.clear();
        return this;
    }

    public Map<String, Object> getPropertiesMap()
    {
        return properties;
    }

    public Set<String> getKeySet()
    {
        return getPropertiesMap().keySet();
    }

    public Collection<Object> getValues()
    {
        return getPropertiesMap().values();
    }

    public void parse(XmlTag node)
    {
        for (XmlTag property : node.getTagsByName("property"))
        {
            String name = property.getAttribute("name").value;
            String value = property.getAttribute("value").value;

            if (MathUtils.isBoolean(value))
                put(name, Boolean.parseBoolean(value));

            else if (MathUtils.isShort(value))
                put(name, Short.parseShort(value));

            else if (MathUtils.isInteger(value))
                put(name, Integer.parseInt(value));

            else if (MathUtils.isLong(value))
                put(name, Long.parseLong(value));

            else if (MathUtils.isFloat(value))
                put(name, Float.parseFloat(value));

            else if (MathUtils.isDouble(value))
                put(name, Double.parseDouble(value));

            else
                put(name, value);
        }
    }
}
