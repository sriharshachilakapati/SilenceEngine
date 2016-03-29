package com.shc.silenceengine.io;

/**
 * @author Sri Harsha Chilakapati
 */
public abstract class FileReader
{
    @FunctionalInterface
    public interface OnComplete
    {
        void invoke(DirectBuffer data);
    }

    public abstract void readFile(FilePath file, OnComplete onComplete);
}
