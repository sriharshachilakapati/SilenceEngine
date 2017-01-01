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

package com.shc.silenceengine.backend.gwt;

import com.google.gwt.canvas.dom.client.ImageData;
import com.google.gwt.typedarrays.shared.ArrayBuffer;
import com.google.gwt.typedarrays.shared.ArrayBufferView;
import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.Image;
import com.shc.silenceengine.io.DirectBuffer;
import com.shc.silenceengine.io.ImageReader;
import com.shc.silenceengine.utils.functional.UniCallback;

/**
 * @author Sri Harsha Chilakapati
 */
public class GwtImageReader extends ImageReader
{
    private static void jsLoadedCallback(ImageData pixels, int width, int height, int oWidth, int oHeight,
                                         UniCallback<Image> onComplete)
    {
        Image image = new Image(width, height, oWidth, oHeight);

        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
                image.setPixel(x, y, new Color(
                        pixels.getRedAt(x, y) / 255f,
                        pixels.getGreenAt(x, y) / 255f,
                        pixels.getBlueAt(x, y) / 255f,
                        pixels.getAlphaAt(x, y) / 255f
                ));
        }

        onComplete.invoke(image);
    }

    @Override
    public void readImage(DirectBuffer memory, UniCallback<Image> onComplete, UniCallback<Throwable> onError)
    {
        getImage(((ArrayBufferView) memory.nativeBuffer()).buffer(), onComplete, e -> onError.invoke(new SilenceException(e)));
    }

    private native void getImage(ArrayBuffer memory, UniCallback<Image> onComplete, UniCallback<String> onError) /*-{
        var arrayBufferView = new Uint8Array(memory);
        var blob = new Blob([arrayBufferView], {type: "image/jpeg"});

        var urlCreator = $wnd.URL || $wnd.webkitURL || $wnd.mozURL;
        var imageUrl = urlCreator.createObjectURL(blob);

        var img = $doc.createElement("img");
        img.src = imageUrl;
        img.style.display = "none";

        function isPowerOfTwo(x)
        {
            return (x & (x - 1)) == 0;
        }

        function nextHighestPowerOfTwo(x)
        {
            --x;
            for (var i = 1; i < 32; i <<= 1)
            {
                x = x | x >> i;
            }
            return x + 1;
        }

        img.onload = function ()
        {
            var canvas = $doc.createElement("canvas");

            canvas.width = isPowerOfTwo(img.width) ? img.width : nextHighestPowerOfTwo(img.width);
            canvas.height = isPowerOfTwo(img.height) ? img.height : nextHighestPowerOfTwo(img.height);

            var ctx = canvas.getContext("2d");

            ctx.drawImage(img, 0, 0, canvas.width, canvas.height);
            var pix = ctx.getImageData(0, 0, canvas.width, canvas.height);

            $doc.body.removeChild(img);

            @com.shc.silenceengine.backend.gwt.GwtImageReader::jsLoadedCallback(*)(pix, canvas.width, canvas.height,
                img.width, img.height, onComplete);
        };

        img.onerror = function (e)
        {
            onError.@com.shc.silenceengine.utils.functional.UniCallback::invoke(*)("Image fetch failed, bad network");
        };

        $doc.body.appendChild(img);
    }-*/;
}
