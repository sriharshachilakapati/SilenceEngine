#ifdef GL_ES
    precision mediump float;
#endif

uniform mat4 proj;
uniform mat4 view;

attribute vec4 position;
attribute vec4 color;
attribute vec2 texCoords;

varying vec4 vColor;
varying vec2 vTexCoords;

void main()
{
    vColor = color;
    vTexCoords = texCoords;

    gl_Position = proj * view * position;
}