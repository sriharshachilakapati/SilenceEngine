#ifdef GL_ES
    precision mediump float;
#endif

uniform mat4 proj;
uniform mat4 view;
uniform mat4 sprite;

attribute vec4 position;

varying vec2 vTexCoords;

void main()
{
    vTexCoords = position.zw;
    gl_Position = proj * view * sprite * vec4(position.xy, 0.0, 1.0);
}
