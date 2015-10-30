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

package com.shc.silenceengine.utils;

import com.shc.silenceengine.io.FilePath;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
public final class ShaderProcessor
{
    private ShaderProcessor()
    {
    }

    public static String process(FilePath codePath)
    {
        return process(codePath, new ArrayList<>());
    }

    private static String process(FilePath codePath, List<String> included)
    {
        StringBuilder builder = new StringBuilder();
        String[] lines = FileUtils.readLinesToStringArray(codePath);

        if (lines != null)
        {
            for (int line = 0; line < lines.length; line++)
            {
                if (lines[line].trim().startsWith("//@include once"))
                {
                    // Include the sources
                    String include = lines[line].replace("//@include once", "").trim();

                    if (!included.contains(include))
                    {
                        FilePath includePath = codePath.getParent().getChild(include);
                        builder.append(process(includePath, included));

                        // Add a #line pragma at the end to get correct line numbers
                        // if there are any errors in the shader code.
                        builder.append("\n#line ").append(line + 1).append("\n");

                        included.add(include);
                    }
                }
                else if (lines[line].trim().startsWith("//@include"))
                {
                    // Include the sources
                    String include = lines[line].replace("//@include", "").trim();
                    FilePath includePath = codePath.getParent().getChild(include);
                    builder.append(process(includePath, included));

                    // Add a #line pragma at the end to get correct line numbers
                    // if there are any errors in the shader code.
                    builder.append("\n#line ").append(line + 1).append("\n");
                    included.add(include);
                }
                else
                    builder.append(lines[line]).append("\n");
            }
        }

        return builder.toString();
    }
}
