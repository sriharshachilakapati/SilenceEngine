package com.shc.silenceengine.io;

import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.Image;

/**
 * @author Sri Harsha Chilakapati
 */
public abstract class ImageReader
{
    @FunctionalInterface
    public interface OnComplete
    {
        void invoke(Image image);
    }

    public void readImage(FilePath filePath, OnComplete onComplete)
    {
        SilenceEngine.io.getFileReader().readFile(filePath, directBuffer ->
                readImage(directBuffer, onComplete));
    }

    public abstract void readImage(DirectBuffer memory, OnComplete onComplete);
}
