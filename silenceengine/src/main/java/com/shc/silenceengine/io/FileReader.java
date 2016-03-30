package com.shc.silenceengine.io;

/**
 * @author Sri Harsha Chilakapati
 */
public abstract class FileReader
{
    @FunctionalInterface
    public interface OnComplete<T>
    {
        void invoke(T data);
    }

    public abstract void readBinaryFile(FilePath file, OnComplete<DirectBuffer> onComplete);

    public abstract void readTextFile(FilePath file, OnComplete<String> onComplete);
}
