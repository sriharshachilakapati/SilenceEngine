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

package com.shc.silenceengine.core;

import com.shc.silenceengine.annotations.PlatformAndroid;
import com.shc.silenceengine.annotations.PlatformDesktop;
import com.shc.silenceengine.annotations.PlatformHTML5;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.utils.TimeUtils;
import com.shc.silenceengine.utils.functional.SimpleCallback;

/**
 * The interface that abstracts the display between multiple platforms. SilenceEngine will only support one display per
 * game and multiple windows is not an option due to the limitation of WebGL platform.
 *
 * @author Sri Harsha Chilakapati
 */
public interface IDisplayDevice
{
    /**
     * Returns the platform that the display device is opened on.
     *
     * @return The platform enum value for the running platform.
     */
    @PlatformDesktop
    @PlatformHTML5
    @PlatformAndroid
    SilenceEngine.Platform getPlatform();

    /**
     * Changes the size of the display on the screen.
     *
     * @param width  The new width.
     * @param height The new height.
     */
    @PlatformDesktop
    @PlatformHTML5
    void setSize(int width, int height);

    /**
     * Returns whether the display is actually in fullscreen mode.
     *
     * @return The state of fullscreen display.
     */
    @PlatformDesktop
    @PlatformHTML5
    @PlatformAndroid
    boolean isFullscreen();

    /**
     * <p>Changes the fullscreen state of the display. The size is ignored, and the resolution is chosen to be the
     * maximum resolution available on the device.</p>
     *
     * <h3>GWT Platform Notes:</h3>
     *
     * <p>On the HTML5 platform, the change takes some time to happen, as it is based on some hacks. Web browsers
     * require that fullscreen change request should be done only upon user interaction, and cancels any requests that
     * are made without user interaction, such as switching to fullscreen mode while initialization.</p>
     *
     * <p>To prevent this issue, and switch to fullscreen mode at any cost, SilenceEngine uses a hack, it delays the
     * request until a user event happens, like a key press, mouse click, or even mouse move.</p>
     *
     * <p>To make it feel natural, you have to call it only after the init methods. You should also only use method in
     * an if clause that checks user input. For example, the following is valid:</p>
     *
     * <pre>
     *     if (Keyboard.isKeyTapped(Keyboard.KEY_ENTER)
     *           &amp;&amp; Keyboard.isKeyDown(Keyboard.KEY_LEFT_ALT))
     *         SilenceEngine.display.setFullscreen(true);
     * </pre>
     *
     * <p>In the above case this is smooth enough that we check on key tap (usually the immediate frame) and request it,
     * then the request is considered when the key goes up. But it is considered a bad practice to just call this method
     * without checking for user input.</p>
     *
     * @param fullscreen The new state of fullscreen.
     */
    @PlatformDesktop
    @PlatformHTML5
    void setFullscreen(boolean fullscreen);

    /**
     * Centers the display on screen. This method only affects the desktop platform.
     */
    @PlatformDesktop
    void centerOnScreen();

    /**
     * Sets the position of the display on the screen. This method only affects the desktop platform.
     *
     * @param x The x-coordinate of the screen position.
     * @param y The y-coordinate of the screen position.
     */
    @PlatformDesktop
    void setPosition(int x, int y);

    /**
     * Returns the coordinate space width of the display window.
     *
     * @return The coordinate space width of the display.
     */
    @PlatformDesktop
    @PlatformHTML5
    @PlatformAndroid
    int getWidth();

    /**
     * Returns the coordinate space height of the display window.
     *
     * @return The coordinate space height of the display.
     */
    @PlatformDesktop
    @PlatformHTML5
    @PlatformAndroid
    int getHeight();

    /**
     * Returns the title of the display.
     *
     * @return The title of the display.
     */
    @PlatformDesktop
    @PlatformHTML5
    String getTitle();

    /**
     * Sets the display title. On desktop, the title will be the window title, and on HTML5, it will change the title of
     * the page that is displayed on the tab.
     *
     * @param title The new title string to use.
     */
    @PlatformDesktop
    @PlatformHTML5
    void setTitle(String title);

    /**
     * Sets the icon of the display. Must point to an image (PNG, BMP, JPEG are supported). For HTML5, use a .ICO file.
     *
     * @param filePath The file path to the image.
     */
    @PlatformDesktop
    @PlatformHTML5
    default void setIcon(FilePath filePath)
    {
        setIcon(filePath, () ->
        {
        });
    }

    /**
     * Sets the icon of the display. Must point to an image (PNG, BMP, JPEG are supported). For HTML5, use a .ICO file.
     *
     * @param filePath The file path to the image.
     * @param success  The success callback to be called after setting the icon.
     */
    @PlatformDesktop
    @PlatformHTML5
    void setIcon(FilePath filePath, SimpleCallback success);

    /**
     * Makes the desktop window close, and the game to exit. This is not honoured in HTML5 platform, as the game can
     * only be terminated by the user closing the tab.
     */
    @PlatformDesktop
    void close();

    /**
     * Returns the current timestamp in nanoseconds. Normally you don't want to use this method, and instead, use the
     * {@link TimeUtils} class instead, it offers more customization.
     *
     * @return The current timestamp in nanoseconds.
     */
    @PlatformDesktop
    @PlatformHTML5
    double nanoTime();

    /**
     * Sets the VSync (Vertical Synchronization) status of the display, if true, the frame rate will be limited to the
     * refresh rate of the monitor. This is true by default. For HTML5 platform, VSync will always be true.
     *
     * @param vSync True to turn on vertical synchronization, False to turn it off.
     */
    @PlatformDesktop
    void setVSync(boolean vSync);

    /**
     * Returns whether the display is in focus. In the case of HTML5 platform, it says whether the window is in focus or
     * not, and at the same time the game is in the active tab.
     *
     * @return True if the display has focus, false otherwise.
     */
    @PlatformDesktop
    @PlatformHTML5
    boolean hasFocus();

    /**
     * Returns the aspect ratio of the display device. It is nothing but the width / height ratio.
     *
     * @return The aspect ratio of the display.
     */
    @PlatformDesktop
    @PlatformHTML5
    @PlatformAndroid
    default float getAspectRatio()
    {
        return (float) getWidth() / (float) getHeight();
    }

    /**
     * The window grabs the mouse, locks it from moving the cursor, especially useful for 3D games.
     *
     * @param grabMouse The state of the mouse, {@code true} for locking the curse, {@code false} otherwise.
     */
    @PlatformDesktop
    @PlatformHTML5
    void setGrabMouse(boolean grabMouse);
}
