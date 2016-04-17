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

package com.shc.silenceengine.backend.gwt;

import com.google.gwt.xhr.client.XMLHttpRequest;
import com.shc.silenceengine.io.DirectBuffer;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.io.FileReader;

/**
 * @author Sri Harsha Chilakapati
 */
public class GwtFileReader extends FileReader
{
    @Override
    public void readBinaryFile(FilePath file, OnComplete<DirectBuffer> onComplete)
    {
        // Create a XMLHttpRequest to load the file into a direct buffer
        XMLHttpRequest request = XMLHttpRequest.create();
        request.open("GET", file.getAbsolutePath());

        // Set to read as ArrayBuffer and attach a handler
        request.setResponseType(XMLHttpRequest.ResponseType.ArrayBuffer);
        request.setOnReadyStateChange(xhr -> {
            if (request.getReadyState() == XMLHttpRequest.DONE)
                // Invoke the onComplete handler
                onComplete.invoke(new GwtDirectBuffer(request.getResponseArrayBuffer()));
        });

        // Send the request
        request.send();
    }

    @Override
    public void readTextFile(FilePath file, OnComplete<String> onComplete)
    {
        // Create a XMLHttpRequest to load the file into a direct buffer
        XMLHttpRequest request = XMLHttpRequest.create();
        request.open("GET", file.getAbsolutePath());

        // Set to read as default mode and attach a handler
        request.setResponseType(XMLHttpRequest.ResponseType.Default);
        request.setOnReadyStateChange(xhr -> {
            if (request.getReadyState() == XMLHttpRequest.DONE)
                // Invoke the onComplete handler
                onComplete.invoke(request.getResponseText());
        });

        // Send the request
        request.send();
    }
}
