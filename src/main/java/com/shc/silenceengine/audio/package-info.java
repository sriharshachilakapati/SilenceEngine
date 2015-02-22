/**
 * <p>
 * This package is the AudioEngine module of SilenceEngine, which defines the main AudioEngine module, and the Sound
 * class which are sufficient for most games. Games that require more functionality can use the OpenAL classes directly.
 * </p>
 *
 * <p>
 * The {@link com.shc.silenceengine.audio.AudioEngine} class is used to initialize the OpenAL device and context, and
 * also loading different {@link com.shc.silenceengine.audio.Sound} instances that you can use to play the different
 * sound effects in your game. The {@link com.shc.silenceengine.audio.ISoundReader} interface defines how to create a
 * reader that can read the data from any file format.
 * </p>
 *
 * @author Sri Harsha Chilakapati
 */
package com.shc.silenceengine.audio;