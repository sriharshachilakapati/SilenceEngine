/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 Sri Harsha Chilakapati
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

package com.shc.silenceengine.backend.android;

import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.utils.functional.Promise;
import com.shc.silenceengine.utils.functional.TriCallback;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Sri Harsha Chilakapati
 */
abstract class AndroidFilePath extends FilePath
{
    /**
     * Constructs an instance of FilePath by taking a path string, and a type.
     *
     * @param path The path string of the path
     * @param type The type of the file, one of {@link Type#EXTERNAL} or {@link Type#RESOURCE}.
     */
    AndroidFilePath(String path, Type type)
    {
        super(path, type);
    }

    public abstract InputStream getInputStream() throws IOException;

    public abstract OutputStream getOutputStream(boolean append) throws IOException;

    @Override
    public Promise<Void> copyTo(FilePath path)
    {
        TriCallback<Boolean, Boolean, Boolean> function = (thisIsDirectory, pathIsDirectory, thisExists) ->
        {
            if (thisIsDirectory && !pathIsDirectory)
                throw new SilenceException("Cannot copy a directory into a file.");

            if (!thisIsDirectory && pathIsDirectory)
                throw new SilenceException("Cannot copy a file into a directory.");

            if (!thisExists)
                throw new SilenceException("Cannot copy a non existing file.");

            byte[] buffer = new byte[1024];
            int length;

            try (InputStream inputStream = getInputStream(); OutputStream outputStream = ((AndroidFilePath) path).getOutputStream(false))
            {
                while ((length = inputStream.read(buffer)) > 0)
                    outputStream.write(buffer, 0, length);
            }
            catch (IOException e)
            {
                SilenceException.reThrow(e);
            }
        };

        return new Promise<>((resolve, reject) ->
                this.isDirectory().then(thisIsDirectory ->
                                path.isDirectory().then(pathIsDirectory ->
                                                this.exists().then(thisExists ->
                                                {
                                                    function.invoke(thisIsDirectory, pathIsDirectory, thisExists);
                                                    resolve.invoke(null);
                                                }, reject),
                                        reject),
                        reject));
    }
}
