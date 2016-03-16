package com.shc.silenceengine.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Represents that a method will only work on the HTML5 platform (Web) and ignored on other platforms.
 *
 * @author Sri Harsha Chilakapati
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
public @interface PlatformHTML5
{
}
