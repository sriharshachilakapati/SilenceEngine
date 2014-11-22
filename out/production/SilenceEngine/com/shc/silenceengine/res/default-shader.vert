#version 330 core

layout(location = 0) in vec4 pos;
layout(location = 1) in vec4 col;

out vec4 vColor;

void main()
{
    vColor = col;
    gl_Position = pos;
}
