package com.shc.silenceengine.backend.gwt;

import com.shc.silenceengine.io.DirectBuffer;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.io.IODevice;

/**
 * @author Sri Harsha Chilakapati
 */
public class GwtIODevice implements IODevice
{
    @Override
    public DirectBuffer create(int sizeInBytes)
    {
        return new GwtDirectBuffer(sizeInBytes);
    }

    @Override
    public void free(DirectBuffer directBuffer)
    {
        // Will be taken care by GC
    }

    @Override
    public FilePath createResourceFilePath(String path)
    {
        return new GwtFilePath(path, FilePath.Type.RESOURCE);
    }

    @Override
    public FilePath createExternalFilePath(String path)
    {
        return new GwtFilePath(path, FilePath.Type.EXTERNAL);
    }
}
