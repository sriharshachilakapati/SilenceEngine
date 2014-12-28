package com.shc.silenceengine.audio;

import java.nio.Buffer;

/**
 * @author Sri Harsha Chilakapati
 */
public interface ISoundReader
{
    public Buffer getData();

    public int getSampleRate();

    public int getFormat();
}
