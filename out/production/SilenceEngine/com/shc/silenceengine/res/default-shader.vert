#version 330 core

layout(location = 0) in vec4 pos;
layout(location = 1) in vec4 col;
layout(location = 2) in vec2 tex;

out vec4 vColor;
out vec2 vTexCoords;

void main()
{
    vColor      = col;
    vTexCoords  = tex;
    gl_Position = pos;
}
