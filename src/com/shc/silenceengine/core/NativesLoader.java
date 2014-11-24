package com.shc.silenceengine.core;

import java.io.*;
import java.nio.file.Files;

/**
 * @author Sri Harsha Chilakapati
 */
class NativesLoader
{
    private static final String OS   = System.getProperty("os.name").toLowerCase();
    private static final String ARCH = System.getProperty("os.arch").toLowerCase();

    public static void load()
    {
        try
        {
            File tmp = Files.createTempDirectory("SilenceEngine").toFile();

            tmp.deleteOnExit();

            if (OS.contains("win"))
            {
                if (ARCH.contains("86"))
                {
                    extractLibrary(tmp, "/windows/x86/lwjgl.dll");
                    extractLibrary(tmp, "/windows/x86/OpenAL32.dll");
                }
                else
                {
                    extractLibrary(tmp, "/windows/x64/lwjgl.dll");
                    extractLibrary(tmp, "/windows/x64/OpenAL64.dll");
                }
            }
            else if (OS.contains("mac"))
            {
                extractLibrary(tmp, "/macosx/x64/liblwjgl.dylib");
                extractLibrary(tmp, "/macosx/x64/libopenal.dylib");
            }
            else if (OS.contains("nux"))
            {
                if (ARCH.contains("86"))
                {
                    extractLibrary(tmp, "/linux/x86/liblwjgl.so");
                    extractLibrary(tmp, "/linux/x86/libopenal.so");
                }
                else
                {
                    extractLibrary(tmp, "/linux/x64/liblwjgl.so");
                    extractLibrary(tmp, "/linux/x64/libopenal.so");
                }
            }

            System.setProperty("org.lwjgl.librarypath", tmp.getAbsolutePath());
        }
        catch (Exception e)
        {
            throw new SilenceException(e.getMessage());
        }
    }

    private static void extractLibrary(File dir, String path)
    {
        if (!path.startsWith("/"))
            throw new IllegalArgumentException("The path has to be absolute! (Start with a '/' character)");

        InputStream is = NativesLoader.class.getResourceAsStream(path);

        String[] parts    = path.replaceAll("\\\\", "/").split("/");
        String   filename = (parts.length > 1) ? parts[parts.length-1] : null;

        if (filename == null)
            throw new SilenceException("Filename is null. Did you pass a directory?");

        try
        {
            File tmp = new File(dir, filename);
            tmp.deleteOnExit();
            FileOutputStream os = new FileOutputStream(tmp);

            byte[] buffer = new byte[1024];
            int readBytes;

            try
            {
                while ((readBytes = is.read(buffer)) != -1)
                    os.write(buffer, 0, readBytes);
            }
            finally
            {
                os.close();
                is.close();
            }
        }
        catch (Exception e)
        {
            throw new SilenceException(e.getMessage());
        }
    }
}
