package com.shc.silenceengine.core;

import com.shc.silenceengine.annotations.PlatformDesktop;
import com.shc.silenceengine.annotations.PlatformHTML5;
import com.shc.silenceengine.io.FilePath;

/**
 * The interface that abstracts the display between multiple platforms. SilenceEngine will only support one display per
 * game and multiple windows is not an option due to the limitation of WebGL platform.
 *
 * @author Sri Harsha Chilakapati
 */
public interface IDisplayDevice
{
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
     * Changes the fullscreen state of the display. The size is ignored, and the resolution is chosen to be the maximum
     * resolution available on the device.
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
    int getWidth();

    /**
     * Returns the coordinate space height of the display window.
     *
     * @return The coordinate space height of the display.
     */
    @PlatformDesktop
    @PlatformHTML5
    int getHeight();

    /**
     * Sets the display title. On desktop, the title will be the window title, and on HTML5, it will change the title
     * of the page that is displayed on the tab.
     *
     * @param title The new title string to use.
     */
    @PlatformDesktop
    @PlatformHTML5
    void setTitle(String title);

    /**
     * Returns the title of the display.
     *
     * @return The title of the display.
     */
    @PlatformDesktop
    @PlatformHTML5
    String getTitle();

    /**
     * Sets the icon of the display. Must point to an image (PNG, BMP, JPEG are supported). For HTML5, use a .ICO file.
     *
     * @param filePath The file path to the image.
     */
    @PlatformDesktop
    @PlatformHTML5
    void setIcon(FilePath filePath);
}
