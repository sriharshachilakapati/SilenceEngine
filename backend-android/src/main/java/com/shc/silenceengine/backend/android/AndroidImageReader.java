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

package com.shc.silenceengine.backend.android;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.Image;
import com.shc.silenceengine.io.DirectBuffer;
import com.shc.silenceengine.io.ImageReader;
import com.shc.silenceengine.utils.TaskManager;
import com.shc.silenceengine.utils.functional.UniCallback;

import java.io.IOException;

/**
 * @author Sri Harsha Chilakapati
 */
public class AndroidImageReader extends ImageReader
{
    private static int calculateInSampleSize(BitmapFactory.Options options)
    {
        float width = options.outWidth;
        float height = options.outHeight;

        double inSampleSize = 1D;

        float reqWidth = width;
        float reqHeight = height;

        if (reqWidth > reqHeight)
        {
            reqWidth = Math.min(2048, reqWidth);
            reqHeight = reqWidth * (height / width);
        }
        else
        {
            reqHeight = Math.min(2048, reqHeight);
            reqWidth = reqHeight * (width / height);
        }

        if (width > reqWidth || height > reqHeight)
        {
            int halfWidth = (int) (width / 2);
            int halfHeight = (int) (height / 2);

            while (halfWidth / inSampleSize > reqWidth && halfHeight / inSampleSize > reqHeight)
                inSampleSize *= 2;
        }

        return (int) inSampleSize;
    }

    @Override
    public void readImage(DirectBuffer memory, UniCallback<Image> uniCallback, UniCallback<Throwable> onError)
    {
        AsyncRunner.runAsync(() ->
        {
            try
            {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;

                BitmapFactory.decodeStream(new DirectBufferInputStream(memory), null, options);

                int originalWidth = options.outWidth;
                int originalHeight = options.outHeight;

                options.inSampleSize = calculateInSampleSize(options);
                options.inJustDecodeBounds = false;

                Bitmap bitmap = BitmapFactory.decodeStream(new DirectBufferInputStream(memory), null, options);

                if (bitmap == null)
                    throw new SilenceException(new IOException("Error decoding image from memory"));

                Image image = new Image(bitmap.getWidth(), bitmap.getHeight(), originalWidth, originalHeight);

                for (int x = 0; x < image.getWidth(); x++)
                    for (int y = 0; y < image.getHeight(); y++)
                    {
                        int pixel = bitmap.getPixel(x, y);

                        float a = android.graphics.Color.alpha(pixel) / 255f;
                        float r = android.graphics.Color.red(pixel) / 255f;
                        float g = android.graphics.Color.green(pixel) / 255f;
                        float b = android.graphics.Color.blue(pixel) / 255f;

                        image.setPixel(x, y, new Color(r, g, b, a));
                    }

                bitmap.recycle();
                bitmap = null;

                return () -> TaskManager.runOnUpdate(() -> uniCallback.invoke(image));
            }
            catch (Throwable e)
            {
                return () -> onError.invoke(e);
            }
        });
    }
}
