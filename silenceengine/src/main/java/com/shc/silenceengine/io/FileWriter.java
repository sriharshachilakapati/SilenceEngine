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

package com.shc.silenceengine.io;

import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.utils.functional.Promise;
import com.shc.silenceengine.utils.functional.SimpleCallback;
import com.shc.silenceengine.utils.functional.UniCallback;

/**
 * @author Sri Harsha Chilakapati
 */
public abstract class FileWriter
{
    public Promise<Void> write(String text, FilePath file, boolean append)
    {
        return new Promise<>((resolve, reject) -> write(text, file, append, () -> resolve.invoke(null), reject));
    }

    public void write(String text, FilePath file, boolean append, SimpleCallback onSuccess)
    {
        write(text, file, append, onSuccess, SilenceException::reThrow);
    }

    public abstract void write(String text, FilePath file, boolean append, SimpleCallback onSuccess,
                               UniCallback<Throwable> onError);

    public Promise<Void> write(DirectBuffer buffer, FilePath file, boolean append)
    {
        return new Promise<>((resolve, reject) -> write(buffer, file, append, () -> resolve.invoke(null), reject));
    }

    public void write(DirectBuffer buffer, FilePath file, boolean append, SimpleCallback onSuccess)
    {
        write(buffer, file, append, onSuccess, SilenceException::reThrow);
    }

    public abstract void write(DirectBuffer buffer, FilePath file, boolean append, SimpleCallback onSuccess,
                               UniCallback<Throwable> onError);
}
