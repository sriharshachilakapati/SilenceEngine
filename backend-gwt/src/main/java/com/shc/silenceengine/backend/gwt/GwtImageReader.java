package com.shc.silenceengine.backend.gwt;

import com.google.gwt.canvas.dom.client.ImageData;
import com.google.gwt.typedarrays.shared.ArrayBuffer;
import com.google.gwt.typedarrays.shared.ArrayBufferView;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.Image;
import com.shc.silenceengine.io.DirectBuffer;
import com.shc.silenceengine.io.ImageReader;

/**
 * @author Sri Harsha Chilakapati
 */
public class GwtImageReader extends ImageReader
{
    private static void jsLoadedCallback(ImageData pixels, int width, int height, OnComplete onComplete)
    {
        Image image = new Image(width, height);

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
    public void readImage(DirectBuffer memory, OnComplete onComplete)
    {
        getImage(((ArrayBufferView) memory.nativeBuffer()).buffer(), onComplete);
    }

    private native void getImage(ArrayBuffer memory, OnComplete onComplete) /*-{
        var arrayBufferView = new Uint8Array(memory.buffer);
        var blob = new Blob([arrayBufferView], {type: "image/jpeg"});

        var urlCreator = $wnd.URL || $wnd.webkitURL || $wnd.mozURL;
        var imageUrl = urlCreator.createObjectURL(blob);

        var img = $doc.createElement("img");
        img.src = imageUrl;
        img.style.display = "none";

        img.onload = function ()
        {
            var canvas = $doc.createElement("canvas");

            canvas.width = img.width;
            canvas.height = img.height;

            var ctx = canvas.getContext("2d");

            ctx.drawImage(img, 0, 0, img.width, img.height);
            var pix = ctx.getImageData(0, 0, img.width, img.height);

            @com.shc.silenceengine.backend.gwt.GwtImageReader::jsLoadedCallback(*)(pix, img.width, img.height, onComplete);
        };

        $doc.appendChild(img);
    }-*/;
}
