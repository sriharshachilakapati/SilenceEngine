/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2015 Sri Harsha Chilakapati
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

package com.shc.silenceengine.tests;

import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.utils.Logger;

import java.io.IOException;

/**
 * @author Sri Harsha Chilakapati
 */
public class FilePathTest
{
    public static void main(String[] args) throws IOException
    {
        Logger.setPrintTimeStamps(false);
        Logger.addLogStream(FilePath.getExternalFile(System.getProperty("java.io.tmpdir") + "/FilePathTest.log"));

        Logger.info("Trying to locate the 'resources' directory in classpath\n");

        FilePath resources = FilePath.getResourceFile("resources");
        logFilePath(resources);

        Logger.info("\nTrying to list all files in the 'resources' directory\n");
        resources.listFiles().forEach(FilePathTest::logFilePath);

        Logger.info("\nGetting the child 'texture.png' from the 'resources' in classpath\n");
        FilePath texture1 = resources.getChild("texture.png");
        logFilePath(texture1);

        Logger.info("\nChecking if the parent of the texture file is the resources directory\n");
        FilePath parent = resources.getParent();
        logFilePath(parent);
        Logger.info(">> " + (parent.equals(resources)));

        Logger.info("\nTrying to create a path to a non existing resource\n");
        FilePath nonExistantResource = FilePath.getResourceFile("nonExistingTexture.png");
        logFilePath(nonExistantResource);

        Logger.info("\nGetting an external file\n");
        FilePath externalFile = FilePath.getExternalFile(System.getProperty("user.dir") + "/Test.exe");
        logFilePath(externalFile);

        Logger.info("\nTrying to get the parent of the external file\n");
        FilePath externalParent = externalFile.getParent();
        logFilePath(externalParent);

        Logger.info("\nListing all the files in the external directory\n");
        externalParent.getParent().listFiles().forEach(FilePathTest::logFilePath);

        Logger.info("\nGetting the child 'com/shc' of the external directory\n");
        FilePath externalChild = externalParent.getChild("Test.exe");
        logFilePath(externalChild);

        Logger.info("\nTrying to create a new file, and copy a resource to it\n");
        FilePath newFile = FilePath.getExternalFile(System.getProperty("java.io.tmpdir") + "/Test.png");
        logFilePath(newFile);
        texture1.copyTo(newFile);
        logFilePath(newFile);

        Logger.info("\nTrying to delete the newly created file\n");
        newFile.delete();
        logFilePath(newFile);
    }

    private static void logFilePath(FilePath path)
    {
        Logger.info(String.format(">> Path='%-35s' Name='%-25s' Extension='.%-4s' Type='%s' Directory=%-5s Exists=%-5s Size=%-8d Bytes",
                path.getPath(), path.getName(), path.getExtension(), "" + path.getType(), "" + path.isDirectory(),
                "" + path.exists(), path.sizeInBytes()));
    }
}
