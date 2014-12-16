package com.shc.silenceengine.input;

import com.shc.silenceengine.core.Display;

import static org.lwjgl.glfw.GLFW.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class Mouse
{
    // Constants
    public static final int MOUSE_BUTTON_1 = GLFW_MOUSE_BUTTON_1;
    public static final int MOUSE_BUTTON_2 = GLFW_MOUSE_BUTTON_2;
    public static final int MOUSE_BUTTON_3 = GLFW_MOUSE_BUTTON_3;
    public static final int MOUSE_BUTTON_4 = GLFW_MOUSE_BUTTON_4;
    public static final int MOUSE_BUTTON_5 = GLFW_MOUSE_BUTTON_5;
    public static final int MOUSE_BUTTON_6 = GLFW_MOUSE_BUTTON_6;
    public static final int MOUSE_BUTTON_7 = GLFW_MOUSE_BUTTON_7;
    public static final int MOUSE_BUTTON_8 = GLFW_MOUSE_BUTTON_8;

    public static final int MOUSE_BUTTON_LEFT   = GLFW_MOUSE_BUTTON_LEFT;
    public static final int MOUSE_BUTTON_RIGHT  = GLFW_MOUSE_BUTTON_RIGHT;
    public static final int MOUSE_BUTTON_MIDDLE = GLFW_MOUSE_BUTTON_MIDDLE;

    public static final int MOUSE_BUTTON_LAST = GLFW_MOUSE_BUTTON_LAST;

    public static boolean isButtonDown(int button)
    {
        return glfwGetMouseButton(Display.getDisplayHandle(), button) == GLFW_PRESS;
    }

    public static boolean isButtonReleased(int button)
    {
        return glfwGetMouseButton(Display.getDisplayHandle(), button) == GLFW_RELEASE;
    }

    public static int getX()
    {
        return Display.mouseX;
    }

    public static int getY()
    {
        return Display.mouseY;
    }

    public static int getDX()
    {
        int dx = Display.mouseDX;
        Display.mouseDX = 0;
        return dx;
    }

    public static int getDY()
    {
        int dy = Display.mouseDY;
        Display.mouseDY = 0;
        return dy;
    }
}
