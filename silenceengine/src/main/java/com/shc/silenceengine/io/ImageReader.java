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

package com.shc.silenceengine.io;

import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.Image;
import com.shc.silenceengine.utils.functional.Promise;
import com.shc.silenceengine.utils.functional.UniCallback;

/**
 * @author Sri Harsha Chilakapati
 */
public abstract class ImageReader
{
    public Promise<Image> readImage(FilePath file)
    {
        return new Promise<>((resolve, reject) -> readImage(file, resolve, reject));
    }

    public void readImage(FilePath filePath, UniCallback<Image> uniCallback)
    {
        readImage(filePath, uniCallback, SilenceEngine.log.getRootLogger()::error);
    }

    public void readImage(FilePath filePath, UniCallback<Image> uniCallback, UniCallback<Throwable> error)
    {
        SilenceEngine.io.getFileReader().readBinaryFile(filePath, directBuffer ->
                readImage(directBuffer, image ->
                {
                    // Free the direct buffer
                    SilenceEngine.io.free(directBuffer);

                    // Invoke the on complete handler
                    uniCallback.invoke(image);
                }, error), error);
    }

    public void readImage(DirectBuffer memory, UniCallback<Image> uniCallback)
    {
        readImage(memory, uniCallback, SilenceEngine.log.getRootLogger()::error);
    }

    public abstract void readImage(DirectBuffer memory, UniCallback<Image> uniCallback, UniCallback<Throwable> error);
}
