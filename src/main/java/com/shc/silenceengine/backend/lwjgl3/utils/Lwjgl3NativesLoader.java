/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2016 Sri Harsha Chilakapati
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

package com.shc.silenceengine.backend.lwjgl3.utils;

import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.io.FilePath;
import org.lwjgl.system.Configuration;

import java.io.IOException;
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
public class Lwjgl3NativesLoader
{
    private static FilePath nativesDir;
    private static boolean  lwjglLoaded;

    /**
     * Loads the natives from the JAR resources
     */
    public static void loadLWJGL()
    {
        // If NativesLoader is disabled in properties, do nothing
        if (System.getProperty("NativesLoader", "true").equalsIgnoreCase("false"))
            return;

        if (lwjglLoaded)
            return;

        // Cleanup natives on Windows, without which the temp folder will
        // be flooded with these DLL files, because the auto delete wont
        // work due to the lock that Windows JVM implementation keeps on
        // the native libraries.
        cleanupExtractedNatives();

        try
        {
            if (nativesDir == null)
                createNativesDir();

            // Set the LWJGL library path
            System.setProperty("java.library.path", nativesDir.getPath());
            System.setProperty("org.lwjgl.librarypath", nativesDir.getPath());

            switch (SilenceEngine.getPlatform())
            {
                case WINDOWS_32:
                    Configuration.LIBRARY_NAME_GLFW.set("glfw32");
                    Configuration.LIBRARY_NAME_LWJGL.set("lwjgl32");
                    Configuration.LIBRARY_NAME_OPENAL.set("OpenAL32");
                    Configuration.LIBRARY_NAME_JEMALLOC.set("jemalloc32");

                    loadLibrary("glfw32.dll");
                    loadLibrary("lwjgl32.dll");
                    loadLibrary("OpenAL32.dll");
                    loadLibrary("jemalloc32.dll");
                    break;

                case WINDOWS_64:
                    Configuration.LIBRARY_NAME_GLFW.set("glfw");
                    Configuration.LIBRARY_NAME_LWJGL.set("lwjgl");
                    Configuration.LIBRARY_NAME_OPENAL.set("OpenAL");
                    Configuration.LIBRARY_NAME_JEMALLOC.set("jemalloc");

                    loadLibrary("glfw.dll");
                    loadLibrary("lwjgl.dll");
                    loadLibrary("OpenAL.dll");
                    loadLibrary("jemalloc.dll");
                    break;

                case MACOSX:
                    Configuration.LIBRARY_NAME_GLFW.set("libglfw");
                    Configuration.LIBRARY_NAME_LWJGL.set("liblwjgl");
                    Configuration.LIBRARY_NAME_OPENAL.set("libopenal");
                    Configuration.LIBRARY_NAME_JEMALLOC.set("libjemalloc");

                    loadLibrary("libglfw.dylib");
                    loadLibrary("liblwjgl.dylib");
                    loadLibrary("libopenal.dylib");
                    loadLibrary("libjemalloc.dylib");
                    break;

                case LINUX_32:
                    Configuration.LIBRARY_NAME_GLFW.set("libglfw32");
                    Configuration.LIBRARY_NAME_LWJGL.set("liblwjgl32");
                    Configuration.LIBRARY_NAME_OPENAL.set("libopenal32");
                    Configuration.LIBRARY_NAME_JEMALLOC.set("libjemalloc32");

                    loadLibrary("libglfw32.so");
                    loadLibrary("liblwjgl32.so");
                    loadLibrary("libopenal32.so");
                    loadLibrary("libjemalloc32.so");
                    break;

                case LINUX_64:
                    Configuration.LIBRARY_NAME_GLFW.set("libglfw");
                    Configuration.LIBRARY_NAME_LWJGL.set("liblwjgl");
                    Configuration.LIBRARY_NAME_OPENAL.set("libopenal");
                    Configuration.LIBRARY_NAME_JEMALLOC.set("libjemalloc");

                    loadLibrary("libglfw.so");
                    loadLibrary("liblwjgl.so");
                    loadLibrary("libopenal.so");
                    loadLibrary("libjemalloc.so");
                    break;

                case UNKNOWN:
                    throw new SilenceException("SilenceEngine does not support your Operating System. We're sorry :(");
            }

            lwjglLoaded = true;
        }
        catch (Exception e)
        {
            // Oops, something went wrong, print to stack trace
            SilenceException.reThrow(e);
            lwjglLoaded = false;
        }
    }

    private static void createNativesDir() throws IOException
    {
        // Create temporary Directory
        nativesDir = FilePath.getExternalFile(Files.createTempDirectory("SilenceEngine").toFile().getAbsolutePath());

        // Delete the temp dir on exit
        nativesDir.deleteOnExit();
    }

    /**
     * Cleans up natives on Windows, without which the temp folder will be flooded with these DLL files, because the
     * auto delete wont work due to the lock that Windows JVM implementation keeps on the native libraries.
     */
    private static void cleanupExtractedNatives()
    {
        // This is not at all needed on other platforms except Windows
        SilenceEngine.Platform platform = SilenceEngine.getPlatform();

        switch (platform)
        {
            case WINDOWS_32:
            case WINDOWS_64:
                break;

            // Don't perform this cleanup on non windows platforms
            default:
                return;
        }

        try
        {
            FilePath temp = FilePath.getExternalFile(System.getProperty("java.io.tmpdir"));

            for (FilePath file : temp.listFiles())
            {
                if (file.isDirectory())
                {
                    if (file.getPath().contains("Silence") && file.getPath().contains("Engine"))
                    {
                        int tryCount;
                        boolean success;

                        for (FilePath nativeFile : file.listFiles())
                        {
                            tryCount = 0;
                            success = false;

                            while (!success && tryCount++ < 5)
                                success = nativeFile.delete();
                        }

                        tryCount = 0;
                        success = false;


                        while (!success && tryCount++ < 5)
                            success = file.delete();
                    }
                }
            }
        }
        catch (Exception e)
        {
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
        // Get the filename from path
        String[] parts = path.replaceAll("\\\\", "/").split("/");
        String filename = (parts.length > 0) ? parts[parts.length - 1] : null;

        if (filename == null)
            throw new SilenceException("Filename is null. Did you pass a directory?");

        try
        {
            if (nativesDir == null)
                createNativesDir();

            // Create a file in the DIR which deletes itself
            FilePath tmp = nativesDir.getChild(filename);
            tmp.deleteOnExit();

            // Copy (extract) the resource file
            FilePath nativeLib = FilePath.getResourceFile(path);
            nativeLib.copyTo(tmp);

            // Load the extracted native library
            System.load(tmp.getPath());
        }
        catch (Exception e)
        {
            // Error again, :( Throw an exception
            SilenceException.reThrow(e);
        }
    }
}
