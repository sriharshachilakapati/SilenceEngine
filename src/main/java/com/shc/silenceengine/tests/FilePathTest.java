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

import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.utils.Logger;

/**
 * @author Sri Harsha Chilakapati
 */
public class FilePathTest
{
    public static void main(String[] args)
    {
        FilePath resources = FilePath.getResourceFile("resources/");
        Logger.log(resources);

        FilePath texture1 = resources.getChild("texture.png");
        Logger.log(texture1);

        FilePath parent = resources.getParent();
        Logger.log(parent);

        FilePath nonExistantResource = FilePath.getResourceFile("resources/nonExistingTexture.png");
        Logger.log(nonExistantResource);

        FilePath externalFile = FilePath.getExternalFile("C:/Windows/explorer.exe");
        Logger.log(externalFile);

        FilePath externalParent = externalFile.getParent();
        Logger.log(externalParent);

        FilePath externalChild = externalParent.getChild("system32/calc.exe");
        Logger.log(externalChild);
    }
}
