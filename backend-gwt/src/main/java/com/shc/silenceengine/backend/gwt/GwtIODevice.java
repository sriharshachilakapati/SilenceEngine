package com.shc.silenceengine.backend.gwt;

import com.shc.silenceengine.io.DirectBuffer;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.io.FileReader;
import com.shc.silenceengine.io.IODevice;
import com.shc.silenceengine.io.ImageReader;

/**
 * @author Sri Harsha Chilakapati
 */
public class GwtIODevice implements IODevice
{
    private FileReader  fileReader  = new GwtFileReader();
    private ImageReader imageReader = new GwtImageReader();

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

    @Override
    public FileReader getFileReader()
    {
        return fileReader;
    }

    @Override
    public ImageReader getImageReader()
    {
        return imageReader;
    }
}
