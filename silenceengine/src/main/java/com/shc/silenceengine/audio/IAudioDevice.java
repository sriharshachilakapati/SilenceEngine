package com.shc.silenceengine.audio;

import com.shc.silenceengine.io.DirectBuffer;

/**
 * @author Sri Harsha Chilakapati
 */
public interface IAudioDevice
{
    int alGenBuffers();

    void alBufferData(int id, int format, DirectBuffer data, int frequency);

    void alDeleteBuffers(int... buffers);

    int alGetError();

    int alGenSources();

    void alSourcei(int id, int param, int value);

    void alSourcef(int id, int param, float value);

    void alSource3f(int id, int param, float v1, float v2, float v3);

    void alSourcePlay(int id);

    void alSourcePause(int id);

    void alSourceRewind(int id);

    void alSourceStop(int id);

    int alGetSourcei(int id, int parameter);

    void alDeleteSources(int... sources);

    class Constants
    {
        /**
         * General tokens.
         */
        public static final int
                AL_INVALID = 0xFFFFFFFF,
                AL_NONE    = 0x0,
                AL_FALSE   = 0x0,
                AL_TRUE    = 0x1;

        /**
         * Error conditions.
         */
        public static final int
                AL_NO_ERROR          = 0x0,
                AL_INVALID_NAME      = 0xA001,
                AL_INVALID_ENUM      = 0xA002,
                AL_INVALID_VALUE     = 0xA003,
                AL_INVALID_OPERATION = 0xA004,
                AL_OUT_OF_MEMORY     = 0xA005;

        /**
         * Numerical queries.
         */
        public static final int
                AL_DOPPLER_FACTOR = 0xC000,
                AL_DISTANCE_MODEL = 0xD000;

        /**
         * String queries.
         */
        public static final int
                AL_VENDOR     = 0xB001,
                AL_VERSION    = 0xB002,
                AL_RENDERER   = 0xB003,
                AL_EXTENSIONS = 0xB004;

        /**
         * Distance attenuation models.
         */
        public static final int
                AL_INVERSE_DISTANCE         = 0xD001,
                AL_INVERSE_DISTANCE_CLAMPED = 0xD002;

        /**
         * Source types.
         */
        public static final int
                AL_SOURCE_ABSOLUTE = 0x201,
                AL_SOURCE_RELATIVE = 0x202;

        /**
         * Listener and Source attributes.
         */
        public static final int
                AL_POSITION = 0x1004,
                AL_VELOCITY = 0x1006,
                AL_GAIN     = 0x100A;

        /**
         * Source attributes.
         */
        public static final int
                AL_CONE_INNER_ANGLE = 0x1001,
                AL_CONE_OUTER_ANGLE = 0x1002,
                AL_PITCH            = 0x1003,
                AL_DIRECTION        = 0x1005,
                AL_LOOPING          = 0x1007,
                AL_BUFFER           = 0x1009,
                AL_SOURCE_STATE     = 0x1010,
                AL_CONE_OUTER_GAIN  = 0x1022,
                AL_SOURCE_TYPE      = 0x1027;

        /**
         * Source state.
         */
        public static final int
                AL_INITIAL = 0x1011,
                AL_PLAYING = 0x1012,
                AL_PAUSED  = 0x1013,
                AL_STOPPED = 0x1014;

        /**
         * Listener attributes.
         */
        public static final int AL_ORIENTATION = 0x100F;

        /**
         * Queue state.
         */
        public static final int
                AL_BUFFERS_QUEUED    = 0x1015,
                AL_BUFFERS_PROCESSED = 0x1016;

        /**
         * Gain bounds.
         */
        public static final int
                AL_MIN_GAIN = 0x100D,
                AL_MAX_GAIN = 0x100E;

        /**
         * Distance model attributes.
         */
        public static final int
                AL_REFERENCE_DISTANCE = 0x1020,
                AL_ROLLOFF_FACTOR     = 0x1021,
                AL_MAX_DISTANCE       = 0x1023;

        /**
         * Buffer attributes.
         */
        public static final int
                AL_FREQUENCY = 0x2001,
                AL_BITS      = 0x2002,
                AL_CHANNELS  = 0x2003,
                AL_SIZE      = 0x2004;

        /**
         * Buffer formats.
         */
        public static final int
                AL_FORMAT_MONO8    = 0x1100,
                AL_FORMAT_MONO16   = 0x1101,
                AL_FORMAT_STEREO8  = 0x1102,
                AL_FORMAT_STEREO16 = 0x1103;

        /**
         * Buffer state.
         */
        public static final int
                AL_UNUSED    = 0x2010,
                AL_PENDING   = 0x2011,
                AL_PROCESSED = 0x2012;
    }
}
