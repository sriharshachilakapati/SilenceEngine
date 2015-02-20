package com.shc.silenceengine.utils;

import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.core.SilenceException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * A class that takes care of LWJGL natives, but also other natives. It removes the head-ache of managing natives, and
 * it manages
 * loading the natives from <pre>lwjgl-natives.jar</pre> file, or
 * from anywhere in the class-path. <p> However, if you want to disable this component, you can do so by
 * setting the property <pre>NativesLoader</pre> to false.
 *
 * @author Sri Harsha Chilakapati
 */
public class NativesLoader
{
    private static File nativesDir;

    /**
     * Loads the natives from the JAR resources
     */
    public static void loadLWJGL()
    {
        // If NativesLoader is disabled in properties, do nothing
        if (System.getProperty("NativesLoader", "true").equalsIgnoreCase("false"))
            return;

        try
        {
            // Create temporary Directory
            nativesDir = Files.createTempDirectory("SilenceEngine").toFile();

            // Delete the temp dir on exit
            nativesDir.deleteOnExit();

            // Set the LWJGL library path
            System.setProperty("java.library.path", nativesDir.getAbsolutePath());

            switch (SilenceEngine.getPlatform())
            {
                case WINDOWS_32:
                    loadLibrary("/windows/x86/lwjgl.dll");
                    loadLibrary("/windows/x86/OpenAL32.dll");
                    break;

                case WINDOWS_64:
                    loadLibrary("/windows/x64/lwjgl.dll");
                    loadLibrary("/windows/x64/OpenAL32.dll");
                    break;

                case MACOSX:
                    loadLibrary("/macosx/x64/liblwjgl.dylib");
                    loadLibrary("/macosx/x64/libopenal.dylib");
                    break;

                case LINUX_32:
                    loadLibrary("/linux/x86/liblwjgl.so");
                    loadLibrary("/linux/x86/libopenal.so");
                    break;

                case LINUX_64:
                    loadLibrary("/linux/x64/liblwjgl.so");
                    loadLibrary("/linux/x64/libopenal.so");
                    break;

                case UNKNOWN:
                    throw new SilenceException("SilenceEngine does not support your Operating System. We're sorry :(");
            }
        }
        catch (Exception e)
        {
            // Oops, something went wrong, print to stack trace
            SilenceException.reThrow(e);
        }
    }

    /**
     * Extracts a file in JAR with path to a directory in FileSystem
     *
     * @param path The path of the library in JAR to extract
     */
    public static void loadLibrary(String path)
    {
        if (!path.startsWith("/"))
            throw new IllegalArgumentException("The path has to be absolute! (Start with a '/' character)");

        // Get the filename from path
        String[] parts = path.replaceAll("\\\\", "/").split("/");
        String filename = (parts.length > 1) ? parts[parts.length - 1] : null;

        if (filename == null)
            throw new SilenceException("Filename is null. Did you pass a directory?");

        try
        {
            // Create a file in the DIR which deletes itself
            File tmp = new File(nativesDir, filename);
            tmp.deleteOnExit();

            // Create the OutputStream

            // Extract the native library
            byte[] buffer = new byte[1024];
            int readBytes;

            try (FileOutputStream os = new FileOutputStream(tmp); InputStream is = NativesLoader.class.getResourceAsStream(path))
            {
                while ((readBytes = is.read(buffer)) != -1)
                    os.write(buffer, 0, readBytes);
            }

            // Load the library
            System.load(tmp.getAbsolutePath());
        }
        catch (Exception e)
        {
            // Error again, :( Throw an exception
            SilenceException.reThrow(e);
        }
    }
}
