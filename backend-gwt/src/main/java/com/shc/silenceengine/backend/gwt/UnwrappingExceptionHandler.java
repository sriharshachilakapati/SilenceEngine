package com.shc.silenceengine.backend.gwt;

import com.google.gwt.core.client.GWT;
import com.shc.silenceengine.core.SilenceEngine;

/**
 * @author Sri Harsha Chilakapati
 */
public class UnwrappingExceptionHandler implements GWT.UncaughtExceptionHandler
{
    @Override
    public void onUncaughtException(Throwable e)
    {
        Throwable exceptionToDisplay = clipUmbrellaExceptions(e);
        SilenceEngine.log.getRootLogger().error(exceptionToDisplay.getMessage());
    }

    private Throwable clipUmbrellaExceptions(Throwable throwable)
    {
        Throwable result = throwable;
        while (result instanceof com.google.web.bindery.event.shared.UmbrellaException)
        {
            result = ((com.google.web.bindery.event.shared.UmbrellaException) result)
                    .getCauses().iterator().next();
        }
        return result;
    }

}
