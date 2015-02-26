#SilenceEngine

![logo](http://silenceengine.goharsha.com/img/logo.png)

A 2D/3D Game Engine written on top of LWJGL3

##What is SilenceEngine?

SilenceEngine is a 2D/3D game engine that takes care of low level aspects of game development like graphics, input handling, asset loading and collision detection for you, meaning you only need to make your game. It lets you focus on the game play and game design, by doing most of the hard work for you.

## Features

The main feature of SilenceEngine is it's simplicity, and it greatly reduces the amount of code you need to write by taking care of almost everything automatically for you. Though it is meant to take care of everything automatically, it is also completely customizable. Here are a list of features of it.

- __State Based Games__:

      SilenceEngine supports separating the game logic into different states, like intro state, story state, play state, high score state, game over state, and any other state that you are going to create. This allows you to keep stuff separate and your code base clean and easy to read.

- __Module Based Engine__:

    SilenceEngine is based on different modules, it is a collection of modules called as _engines_. There are _core_ engine, _audio_ engine, _graphics_ engine, _input_ engine, and _collision_ engine, and all these engines make up the SilenceEngine. It is up to you on how you use it, you can use all these as a whole, or you could use each of them separately. Either way, they work.

- __Completely Customizable__:

    SilenceEngine is designed to be completely customizable. You can change everything from the `ResourceLoader` to the Game Loop, and you can also change the entity parameters. It is totally flexible and also easy to use. It is finally up to you whether you want to extend the components, or to re-implement them in __your__ way.

- __Automatic Collision Detection__:

    SilenceEngine features with automatic collision detection. All you need to do is give your entities a collision shape, and register the classes in the collider, and the collisions, along with collision response is done for you, in both 2D and 3D. The `CollisionEngine` uses _SAT_ (Separating Axis Theorem) to determine collisions, and hence you get your collision response with good accuracy.

- __Object Oriented Wrappers for OpenGL and OpenAL__:

  If you think that all the above features are not useful for you, and you want more performance and you love going low level, SilenceEngine provides object oriented wrapper classes for OpenGL and OpenAL. It is up to you how to use them, and other parts of the SilenceEngine cooperates with you.

The above list is only half what SilenceEngine offers to you. It is currently in heavy development, and more features are yet to arrive. In the meanwhile, take a look at the source code, and the example games to get an idea of how to use this engine.

##Is it only for games?

Perhaps not, you can create any form of OpenGL application using SilenceEngine. It provides you with OpenGL classes in the `com.shc.silenceengine.graphics.opengl` package, which cleanly wraps the OpenGL functions into Java classes, making them more easy to use. All you have to take care of is that you must call the `dispose()` on those objects when you no longer need them.

It is also easier to use in development mode as we will check for OpenGL errors after every call to the OpenGL functions and report you the errors, if any exist, in the form of `GLException` allowing you to get rid of the errors quickly and easily. By the way, everything in SilenceEngine is modern, and there is no deprecated stuff. I'm proud to say that this engine only uses OpenGL 3.3 (no deprecated OpenGL).

##Licence

 - This is licenced under MIT licence, which you can find [here](http://choosealicense.com/licenses/mit/).
 - This game engine uses JOrbis library from JCraft which are licenced under [LGPL](http://choosealicense.com/licenses/lgpl-3.0/).
 - This game engine uses J-Ogg library from [j-ogg.de](http://www.j-ogg.de/) on a free permissive licence.
 - This game engine is written on top of [LWJGL 3](http://lwjgl.org). You can find it's licence [here](https://github.com/LWJGL/lwjgl3/blob/master/doc/LICENSE.txt).
 
##Links

 - SilenceEngine website ([http://silenceengine.goharsha.com/](http://silenceengine.goharsha.com))
 - SilenceEngine forum ([http://silenceengine.goharsha.com/forum/](http://silenceengine.goharsha.com/forum/))
 - SilenceEngine JavaDocs (Less updated) ([http://silenceengine.goharsha.com/javadoc/](http://silenceengine.goharsha.com/javadoc/))
 - ScorpionHunter (SilenceEngine 2D Demo Game) ([https://github.com/sriharshachilakapati/ScorpionHunter/](https://github.com/sriharshachilakapati/ScorpionHunter/))
 - Blox (Incomplete, SilenceEngine 3D Demo Game) ([https://github.com/sriharshachilakapati/Blox/](https://github.com/sriharshachilakapati/Blox/))
