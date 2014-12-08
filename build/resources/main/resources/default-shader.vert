#version 330 core

uniform mat4 mTransform;
uniform mat4 camProj;
uniform mat4 camView;

layout(location = 0) in vec4 pos;
layout(location = 1) in vec4 col;
layout(location = 2) in vec2 tex;

out vec4 vColor;
out vec2 vTexCoords;

void main()
{
    vColor      = col;
    vTexCoords  = tex;
    gl_Position = camProj * camView * mTransform * pos;
}
