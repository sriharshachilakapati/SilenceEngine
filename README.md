![logo](http://silenceengine.goharsha.com/img/logo_wide.png)

## What is SilenceEngine?

SilenceEngine is a 2D/3D game engine that takes care of low level aspects of game development like graphics, input handling, asset loading and collision detection for you, meaning you only need to make your game. It lets you focus on the game play and game design, by doing most of the hard work for you.

## Features

The main feature of SilenceEngine is it's simplicity, and it greatly reduces the amount of code you need to write by taking care of almost everything automatically for you. Though it is meant to take care of everything automatically, it is also completely customizable. Here are a list of features of it.

- __Truly Cross Platform__:

    SilenceEngine games are truly cross platform, it lets you compile to Desktop (Windows / Linux / MacOS), Android and also HTML5, all from a single code base. You can chose which platform(s) to target, and also extend it with platform specific code easily.

- __State Based Games__:

    SilenceEngine supports separating the game logic into different states, like intro state, story state, play state, high score state, game over state, and any other state that you are going to create. This allows you to keep stuff separate and your code base clean and easy to read.    

- __Completely Customizable__:

    SilenceEngine is designed to be completely customizable. You can change everything from the `ResourceLoader` to the Game Loop, and you can also change the entity parameters. It is totally flexible and also easy to use. It is finally up to you whether you want to extend the components, or to re-implement them in __your__ way.

- __Automatic Collision Detection__:

    SilenceEngine features with automatic collision detection. All you need to do is give your entities a collision shape, and register the classes in the collider, and the collisions, along with collision response is done for you, in both 2D and 3D. The `CollisionEngine` uses _SAT_ (Separating Axis Theorem) to determine collisions, and hence you get your collision response with good accuracy.

- __Tiled MAP Editor Support__:

    SilenceEngine has support for loading and rendering maps made with the [Tiled Map Editor](http://mapeditor.org/). Currently supports automatically rendering of Orthogonal and Isometric maps, but the support will soon increase to all other map formats. The parser can parse any TMX format (not compressed, and XML only) though.

- __Object Oriented Wrappers for OpenGL and OpenAL__:

    If you think that all the above features are not useful for you, and you want more performance and you love going low level, SilenceEngine provides object oriented wrapper classes for OpenGL and OpenAL. It is up to you how to use them, and other parts of the SilenceEngine cooperates with you.

The above list is only half what SilenceEngine offers to you. It is currently in heavy development, and more features are yet to arrive. In the meanwhile, take a look at the source code, and the example games to get an idea of how to use this engine.

## Is it only for games?

Perhaps not, you can create any form of OpenGL application using SilenceEngine. It provides you with OpenGL classes in the `com.shc.silenceengine.graphics.opengl` package, which cleanly wraps the OpenGL functions into Java classes, making them more easy to use. All you have to take care of is that you must call the `dispose()` on those objects when you no longer need them.

It is also easier to use in development mode as we will check for OpenGL errors after every call to the OpenGL functions and report you the errors, if any exist, in the form of `GLException` allowing you to get rid of the errors quickly and easily. By the way, everything in SilenceEngine is modern, and there is no deprecated stuff. I'm proud to say that this engine only uses OpenGL 3.3 (no deprecated OpenGL).

## Licence

 - The engine, and all the backends in this repo are licenced under MIT licence, which you can find [here](http://choosealicense.com/licenses/mit/).
 - This desktop backend uses [LWJGL 3](http://lwjgl.org). You can find it's licence [here](https://github.com/LWJGL/lwjgl3/blob/master/doc/LICENSE.txt).
 - The GWT backend uses [WebGL4J](https://github.com/sriharshachilakapati/WebGL4J/), [GWT-AL](https://github.com/sriharshachilakapati/GWT-AL/) and [GwtOpentype](https://github.com/sriharshachilakapati/GwtOpentype/) libraries which operate on MIT licence.
 - The Android backend uses [AndroidOpenAL](https://github.com/sriharshachilakapati/AndroidOpenAL/) library which uses LGPL v3 licence.
 - The engine uses [EasyJSON](https://github.com/sriharshachilakapati/EasyJSON/) and [EasyXML](https://github.com/sriharshachilakapati/EasyXML/) libraries which are released under MIT licence.

## Links

 - SilenceEngine website ([http://silenceengine.goharsha.com/](http://silenceengine.goharsha.com))
 - SilenceEngine forum ([http://silenceengine.goharsha.com/forum/](http://silenceengine.goharsha.com/forum/))
 - SilenceEngine JavaDocs (Less updated) ([http://silenceengine.goharsha.com/javadoc/](http://silenceengine.goharsha.com/javadoc/))
 - Blox (Incomplete, SilenceEngine 3D Demo Game) ([https://github.com/sriharshachilakapati/Blox/](https://github.com/sriharshachilakapati/Blox/))
