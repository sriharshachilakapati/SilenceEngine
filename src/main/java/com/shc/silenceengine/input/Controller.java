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
 * @author Sri Harsha Chilakapati
 */
public class Controller
{
    public static enum Type
    {
        PS3, PS4, XBOX, GENERIC
    }

    // Generic Button Constants
    public static final int GENERIC_BUTTON_1 = 1;
    public static final int GENERIC_BUTTON_2 = 2;
    public static final int GENERIC_BUTTON_3 = 3;
    public static final int GENERIC_BUTTON_4 = 4;
    public static final int GENERIC_BUTTON_5 = 5;
    public static final int GENERIC_BUTTON_6 = 6;
    public static final int GENERIC_BUTTON_7 = 7;
    public static final int GENERIC_BUTTON_8 = 8;
    public static final int GENERIC_BUTTON_9 = 9;
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
    public static final int GENERIC_BUTTON_L1 = 5;
    public static final int GENERIC_BUTTON_L2 = 7;
    public static final int GENERIC_BUTTON_R1 = 6;
    public static final int GENERIC_BUTTON_R2 = 8;
    public static final int GENERIC_BUTTON_SELECT = 9;
    public static final int GENERIC_BUTTON_START = 10;

    // Generic D-PAD constants, You need to optimize for other controllers
    public static final int GENERIC_DPAD_UP = 13;
    public static final int GENERIC_DPAD_RIGHT = 14;
    public static final int GENERIC_DPAD_DOWN = 15;
    public static final int GENERIC_DPAD_LEFT = 16;

    // Generic AXE constants
    public static final int GENERIC_AXE_LEFT_X  = 1;
    public static final int GENERIC_AXE_LEFT_Y    = 2;
    public static final int GENERIC_AXE_RIGHT_X = 3;
    public static final int GENERIC_AXE_RIGHT_Y   = 4;

    // XBOX Button constants
    public static final int XBOX_BUTTON_A = 1;
    public static final int XBOX_BUTTON_B = 2;
    public static final int XBOX_BUTTON_X = 3;
    public static final int XBOX_BUTTON_Y = 4;
    public static final int XBOX_BUTTON_LB = 5;
    public static final int XBOX_BUTTON_RB = 6;
    public static final int XBOX_BUTTON_BACK = 7;
    public static final int XBOX_BUTTON_START = 8;
    public static final int XBOX_BUTTON_LS = 9;
    public static final int XBOX_BUTTON_RS = 10;
    public static final int XBOX_DPAD_UP = 11;
    public static final int XBOX_DPAD_RIGHT = 12;
    public static final int XBOX_DPAD_DOWN = 13;
    public static final int XBOX_DPAD_LEFT = 14;

    // XBOX AXE constants
    public static final int XBOX_LEFT_STICKER_X = 1;
    public static final int XBOX_LEFT_STICKER_Y = 2;
    public static final int XBOX_SHOULDER_TRIGGER = 3;
    public static final int XBOX_RIGHT_STICKER_Y = 4;
    public static final int XBOX_RIGHT_STICKER_X = 5;

    // TODO: ADD MAPPINGS FOR PS3 AND PS4 CONTROLLERS ALSO

    private static Controller[] controllers;

    private int id;
    private String name;
    private int numButtons;
    private int numAxes;
    private Type type;

    private Map<Integer, Boolean> buttons;
    private Map<Integer, Float>   axes;

    private Controller(int id, String name)
    {
        this.id = id;
        this.name = name;

        buttons = new HashMap<>();
        axes = new HashMap<>();

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

    public void printValues()
    {
        buttons.keySet().forEach(i ->
        {
            if (isButtonDown(i))
                System.out.println("Button " + i + " down");
        });

        axes.keySet().forEach(i ->
        {
            float value = getAxe(i);

            if (value != 0)
                System.out.println("Axe " + i + ": " + value);
        });
    }

    private void pollValues()
    {
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

    public boolean isButtonDown(int button)
    {
        return buttons.get(button);
    }

    public float getAxe(int axe)
    {
        return axes.get(axe);
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public int getNumButtons()
    {
        return numButtons;
    }

    public int getNumAxes()
    {
        return numAxes;
    }

    public Type getType()
    {
        return type;
    }

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

    public static void poll()
    {
        for (Controller controller : controllers)
            controller.pollValues();
    }

    public static Controller[] getConnectedControllers()
    {
        return controllers;
    }
}
