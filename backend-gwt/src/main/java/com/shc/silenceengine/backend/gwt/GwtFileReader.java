package com.shc.silenceengine.backend.gwt;

import com.google.gwt.xhr.client.XMLHttpRequest;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.io.FileReader;

/**
 * @author Sri Harsha Chilakapati
 */
public class GwtFileReader extends FileReader
{
    @Override
    public void readFile(FilePath file, OnComplete onComplete)
    {
        // Create a XMLHttpRequest to load the file into a direct buffer
        XMLHttpRequest request = XMLHttpRequest.create();
        request.open("POST", file.getAbsolutePath());

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
}
