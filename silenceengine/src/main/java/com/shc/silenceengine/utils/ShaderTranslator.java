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

package com.shc.silenceengine.utils;

import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.opengl.Shader;

/**
 * A highly experimental shader translator. Uses #defines in GLSL to compile a subset of GLSL 330 on GLSL ES.
 *
 * @author Sri Harsha Chilakapati
 */
public class ShaderTranslator
{
    public static String makeSafe(Shader.Type type, String... source)
    {
        StringBuilder sb = new StringBuilder();

        SilenceEngine.Platform platform = SilenceEngine.display.getPlatform();

        if (!source[0].contains("#version"))
        {
            String version = null;

            if (platform != SilenceEngine.Platform.HTML5 && platform != SilenceEngine.Platform.ANDROID)
                version = "330 core";

            if (version != null)
            {
                sb.append("#version ").append(version).append("\n");

                if (type == Shader.Type.FRAGMENT_SHADER)
                    sb.append("out vec4 g_FragColor;\n");
            }
            else
            {
                sb.append("#define in ").append(type == Shader.Type.VERTEX_SHADER ? "attribute\n" : "varying\n");

                if (type == Shader.Type.VERTEX_SHADER)
                    sb.append("#define out varying\n");

                sb.append("#define texture texture2D\n")
                        .append("#define g_FragColor gl_FragColor\n")
                        .append("precision mediump float;\n");
            }

            sb.append("#line 0\n");
        }

        for (String s : source)
            sb.append(s);

        return sb.toString();
    }
}
