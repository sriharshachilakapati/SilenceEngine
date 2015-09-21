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

package com.shc.silenceengine.input;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * A class for handling polled Controller input.
 *
 * @author Sri Harsha Chilakapati
 * @author Josh "ShadowLordAlpha"
 */
public class Controller
{
    // Generic Button Constants
    public static final int GENERIC_BUTTON_1  = 1;
    public static final int GENERIC_BUTTON_2  = 2;
    public static final int GENERIC_BUTTON_3  = 3;
    public static final int GENERIC_BUTTON_4  = 4;
    public static final int GENERIC_BUTTON_5  = 5;
    public static final int GENERIC_BUTTON_6  = 6;
    public static final int GENERIC_BUTTON_7  = 7;
    public static final int GENERIC_BUTTON_8  = 8;
    public static final int GENERIC_BUTTON_9  = 9;
    public static final int GENERIC_BUTTON_10 = 10;
    public static final int GENERIC_BUTTON_11 = 11;
    public static final int GENERIC_BUTTON_12 = 12;
    public static final int GENERIC_BUTTON_13 = 13;
    public static final int GENERIC_BUTTON_14 = 14;
    public static final int GENERIC_BUTTON_15 = 15;
    public static final int GENERIC_BUTTON_16 = 16;
    public static final int GENERIC_BUTTON_17 = 17;
    public static final int GENERIC_BUTTON_18 = 18;
    public static final int GENERIC_BUTTON_19 = 19;
    public static final int GENERIC_BUTTON_20 = 20;
    public static final int GENERIC_BUTTON_21 = 21;
    public static final int GENERIC_BUTTON_22 = 22;
    public static final int GENERIC_BUTTON_23 = 23;
    public static final int GENERIC_BUTTON_24 = 24;
    public static final int GENERIC_BUTTON_25 = 25;
    public static final int GENERIC_BUTTON_26 = 26;
    public static final int GENERIC_BUTTON_27 = 27;
    public static final int GENERIC_BUTTON_28 = 28;
    public static final int GENERIC_BUTTON_29 = 29;
    public static final int GENERIC_BUTTON_30 = 30;
    public static final int GENERIC_BUTTON_31 = 31;

    // Named generic buttons
    public static final int GENERIC_BUTTON_L1     = 5;
    public static final int GENERIC_BUTTON_L2     = 7;
    public static final int GENERIC_BUTTON_R1     = 6;
    public static final int GENERIC_BUTTON_R2     = 8;
    public static final int GENERIC_BUTTON_SELECT = 9;
    public static final int GENERIC_BUTTON_START  = 10;

    // Generic D-PAD constants, You need to optimize for other controllers
    public static final int GENERIC_DPAD_UP    = 13;
    public static final int GENERIC_DPAD_RIGHT = 14;
    public static final int GENERIC_DPAD_DOWN  = 15;
    public static final int GENERIC_DPAD_LEFT  = 16;

    // Generic AXE constants
    public static final int GENERIC_AXE_LEFT_X  = 1;
    public static final int GENERIC_AXE_LEFT_Y  = 2;
    public static final int GENERIC_AXE_RIGHT_X = 3;
    public static final int GENERIC_AXE_RIGHT_Y = 4;

    // XBOX Button constants
    public static final int XBOX_BUTTON_A     = 1;
    public static final int XBOX_BUTTON_B     = 2;
    public static final int XBOX_BUTTON_X     = 3;
    public static final int XBOX_BUTTON_Y     = 4;
    public static final int XBOX_BUTTON_LB    = 5;
    public static final int XBOX_BUTTON_RB    = 6;
    public static final int XBOX_BUTTON_BACK  = 7;
    public static final int XBOX_BUTTON_START = 8;
    public static final int XBOX_BUTTON_LS    = 9;
    public static final int XBOX_BUTTON_RS    = 10;
    public static final int XBOX_DPAD_UP      = 11;
    public static final int XBOX_DPAD_RIGHT   = 12;
    public static final int XBOX_DPAD_DOWN    = 13;
    public static final int XBOX_DPAD_LEFT    = 14;

    // XBOX AXE constants
    public static final int XBOX_LEFT_STICKER_X   = 1;
    public static final int XBOX_LEFT_STICKER_Y   = 2;
    public static final int XBOX_SHOULDER_TRIGGER = 3;
    public static final int XBOX_RIGHT_STICKER_Y  = 4;
    public static final int XBOX_RIGHT_STICKER_X  = 5;

    // PS3 Button constants
    public static final int PS3_BUTTON_SELECT   = 0;
    public static final int PS3_BUTTON_LEFT_JS  = 1;
    public static final int PS3_BUTTON_RIGHT_JS = 2;
    public static final int PS3_BUTTON_START    = 3;
    public static final int PS3_DPAD_UP         = 4;
    public static final int PS3_DPAD_RIGHT      = 5;
    public static final int PS3_DPAD_DOWN       = 6;
    public static final int PS3_DPAD_LEFT       = 7;
    public static final int PS3_BUTTON_L2       = 8;
    public static final int PS3_BUTTON_R2       = 9;
    public static final int PS3_BUTTON_L1       = 10;
    public static final int PS3_BUTTON_R1       = 11;
    public static final int PS3_BUTTON_TRIANGLE = 12;
    public static final int PS3_BUTTON_CIRCLE   = 13;
    public static final int PS3_BUTTON_CROSS    = 14;
    public static final int PS3_BUTTON_SQUARE   = 15;
    public static final int PS3_BUTTON_PS       = 16;

    // PS3 AXE constants
    public static final int PS3_AXE_LS_X = 0;
    public static final int PS3_AXE_LS_Y = 1;
    public static final int PS3_AXE_RS_X = 2;
    public static final int PS3_AXE_RS_Y = 3;

    // TODO: Add PS4 controller mappings too

    private static Controller[] controllers;

    private int    id;
    private String name;
    private int    numButtons;
    private int    numAxes;
    private Type   type;

    private Map<Integer, Boolean> buttons;
    private Map<Integer, Float>   axes;
    private Map<Integer, Float>   axesThisFrame;
    private Map<Integer, Float>   axesLastFrame;
    private Map<Integer, Boolean> buttonsThisFrame;
    private Map<Integer, Boolean> buttonsLastFrame;

    /**
     * Constructs a controller object that represents a single joystick that is connected to the system. A controller
     * has a unique ID (GLFWjoystick*) and a name. There is no guarantee GLFW makes that this name is unique, because it
     * is the name of the driver that the OS uses to interface with the controller.
     *
     * @param id   The unique ID of the joystick. It is the number of the joystick, starting from 0.
     * @param name The name of the driver that is used to interface with the controller hardware.
     */
    private Controller(int id, String name)
    {
        this.id = id;
        this.name = name;

        buttons = new HashMap<>();
        axes = new HashMap<>();

        buttonsThisFrame = new HashMap<>();
        buttonsLastFrame = new HashMap<>();

        axesThisFrame = new HashMap<>();
        axesLastFrame = new HashMap<>();

        // Calculate the number of buttons of this controller
        ByteBuffer buttons = glfwGetJoystickButtons(id);
        while (buttons.hasRemaining())
        {
            numButtons++;
            buttons.get();
        }

        // Calculate the number of axes of this controller
        FloatBuffer axes = glfwGetJoystickAxes(id);
        while (axes.hasRemaining())
        {
            numAxes++;
            axes.get();
        }

        // Decide the controller type
        if (name.equalsIgnoreCase("Wireless Controller") && numButtons == 18)
            type = Type.PS4;
        else if (name.equalsIgnoreCase("PLAYSTATION(R)3 Controller") && numButtons == 19)
            type = Type.PS3;
        else if (name.equalsIgnoreCase("Controller") && numButtons == 15)
            type = Type.XBOX;
        else
            type = Type.GENERIC;
    }

    /**
     * poll all controllers for input
     */
    public static void poll()
    {
        for (Controller controller : controllers)
            controller.pollValues();
    }

    /**
     * Call <code>startFrame();</code> on all known connected controllers
     */
    public static void startEventFrame()
    {
        for (Controller controller : controllers)
            controller.startFrame();
    }

    /**
     * Call <code>clearFrame();</code> on all known connected controllers
     */
    public static void clearEventFrame()
    {
        for (Controller controller : controllers)
            controller.clearFrame();
    }

    /**
     * Gets a list of all known connected controllers
     *
     * @return A list of all known connected controllers
     */
    public static Controller[] getConnectedControllers()
    {
        return controllers;
    }

    /**
     * Gets if the button has been pressed this event frame on the controller
     *
     * @param button     The button
     * @param controller The controller
     *
     * @return True if the button has been pressed this event frame on the controller
     */
    public static boolean isPressed(int button, int controller)
    {
        if (controllers == null)
            create();

        return controller < controllers.length && controllers[controller].isPressed(button);
    }

    /**
     * Generate a list of controllers that are able to be used
     */
    public static void create()
    {
        // Create a list to store the controllers
        List<Controller> controllerList = new ArrayList<>();

        // Iterate over all the controllers GLFW supports
        for (int i = GLFW_JOYSTICK_1; i <= GLFW_JOYSTICK_LAST; i++)
        {
            // If the controller is present, add it to the list
            if (glfwJoystickPresent(i) == GL_TRUE)
            {
                String name = glfwGetJoystickName(i);
                controllerList.add(new Controller(i, name));
            }
        }

        // Turn the list into an array
        controllers = new Controller[controllerList.size()];
        controllers = controllerList.toArray(controllers);
    }

    /**
     * Gets if the button has been pressed in this event frame and not the frame before on the controller
     *
     * @param button     The button
     * @param controller The controller
     *
     * @return true if the button has been pressed in this event frame and not the frame before on the controller
     */
    public static boolean isClicked(int button, int controller)
    {
        if (controllers == null)
            create();

        return controller < controllers.length && controllers[controller].isClicked(button);
    }

    /**
     * Gets the current value on the axis for the controller
     *
     * @param axe        The axis
     * @param controller The ID of the controller, starting from 0.
     *
     * @return The current value on the axis for the controller
     */
    public static float getAxe(int axe, int controller)
    {
        if (controllers == null)
            create();

        return controller < controllers.length ? controllers[controller].getAxe(axe) : 0;
    }

    /**
     * Whether the axis is on or not for the controller
     *
     * @param axe        The axis
     * @param controller The ID of the controller, starting from 0.
     *
     * @return Whether the axis is on or not for the controller
     */
    public static float getClickAxe(int axe, int controller)
    {
        if (controllers == null)
            create();

        return controller < controllers.length ? controllers[controller].getClickAxe(axe) : 0;
    }

    /**
     * Polls the controller for input as well as checks to see if the controller is still active
     */
    private void pollValues()
    {
        // Check if controller is still plugged in
        if (!isPresent())
        {
            buttons.keySet().forEach(i -> buttons.put(i, false));
            axes.keySet().forEach(i -> axes.put(i, 0f));

            return;
        }

        // Poll the buttons
        ByteBuffer buttons = glfwGetJoystickButtons(id);
        int buttonID = 0;

        while (buttons.hasRemaining())
        {
            buttonID++;
            this.buttons.put(buttonID, buttons.get() == 1);
        }

        // Poll the axes
        FloatBuffer axes = glfwGetJoystickAxes(id);
        int axeID = 0;

        while (axes.hasRemaining())
        {
            axeID++;

            float value = axes.get();
            if (value > -0.15 && value < 0.15)
                value = 0;

            this.axes.put(axeID, value);
        }
    }

    /**
     * Gets if the controller is still connected
     *
     * @return true if the controller is still connected
     */
    public boolean isPresent()
    {
        return glfwJoystickPresent(id) == GL_TRUE;
    }

    /**
     * Remove all events from the current event frame and add all events from the event buffer into the current event
     * frame
     */
    private void startFrame()
    {
        buttonsThisFrame.clear();
        buttonsThisFrame.putAll(buttons);

        axesThisFrame.clear();
        axesThisFrame.putAll(axes);
    }

    /**
     * Remove all events from the last frame and move all events from the current event frame into the last frame list.
     */
    private void clearFrame()
    {
        buttonsLastFrame.clear();
        buttonsLastFrame.putAll(buttonsThisFrame);

        axesLastFrame.clear();
        axesLastFrame.putAll(axesThisFrame);
    }

    /**
     * Gets if the button has been pressed this event frame
     *
     * @param button The button
     *
     * @return true if the button has been pressed this event frame
     */
    public boolean isPressed(int button)
    {
        if (!buttonsLastFrame.containsKey(button))
            buttonsLastFrame.put(button, false);

        if (!buttonsThisFrame.containsKey(button))
            buttonsThisFrame.put(button, false);

        return buttonsLastFrame.get(button) || buttonsThisFrame.get(button);
    }

    /**
     * Gets if the button has been pressed in this event frame and not the frame before
     *
     * @param button The button
     *
     * @return true if the button has been pressed in this event frame and not the frame before
     */
    public boolean isClicked(int button)
    {
        if (!buttonsLastFrame.containsKey(button))
            buttonsLastFrame.put(button, false);

        if (!buttonsThisFrame.containsKey(button))
            buttonsThisFrame.put(button, false);

        return buttonsThisFrame.get(button) && !buttonsLastFrame.get(button);
    }

    /**
     * Gets the current value on the axis
     *
     * @param axe The axis
     *
     * @return The current value on the axis
     */
    public float getAxe(int axe)
    {
        return axes.get(axe);
    }

    /**
     * Whether the axis is on or not
     *
     * @param axe The axis
     *
     * @return Whether the axis is on or not
     */
    public float getClickAxe(int axe)
    {
        if (axesLastFrame.get(axe) != 0)
            return 0;

        return axesThisFrame.get(axe);
    }

    /**
     * Debug method to see button and axis presses on a controller this is the same as calling
     * <code>printValues(false);</code>
     */
    public void printValues()
    {
        printValues(false);
    }

    /**
     * Debug method to see button and axis presses on a controller
     *
     * @param repeat If true, the values are printed even if the button or axis is pressed and held down.
     */
    public void printValues(boolean repeat)
    {
        buttons.keySet().forEach(i ->
        {
            boolean condition = repeat ? isPressed(i) : isClicked(i);

            if (condition)
                System.out.println("Button " + i + " down");
        });

        axes.keySet().forEach(i ->
        {
            float value = repeat ? getAxe(i) : getClickAxe(i);

            if (value != 0)
                System.out.println("Axe " + i + ": " + value);
        });
    }

    /**
     * Gets the ID of the controller
     *
     * @return The ID of the controller
     */
    public int getId()
    {
        return id;
    }

    /**
     * Gets the name of the controller
     *
     * @return The name of the controller
     */
    public String getName()
    {
        return name;
    }

    /**
     * Gets the number of buttons on the controller
     *
     * @return The number of button on the controller
     */
    public int getNumButtons()
    {
        return numButtons;
    }

    /**
     * Gets the number of axes the controller has
     *
     * @return The number of axes the controller has
     */
    public int getNumAxes()
    {
        return numAxes;
    }

    /**
     * Gets the type of controller
     *
     * @return The type of controller
     */
    public Type getType()
    {
        return type;
    }

    /**
     * Enum denoting the different types of controllers
     */
    public enum Type
    {
        PS3, PS4, XBOX, GENERIC
    }
}
