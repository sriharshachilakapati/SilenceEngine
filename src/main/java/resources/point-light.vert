#version 330 core

uniform mat4 mTransform;
uniform mat4 camProj;
uniform mat4 camView;

layout(location = 0) in vec4 pos;
layout(location = 1) in vec4 col;
layout(location = 2) in vec2 tex;
layout(location = 3) in vec4 norm;

out vec4 vColor;
out vec2 vTexCoords;
out vec4 vNormal;
out vec4 vPosition;

void main()
{
    vColor      = col;
    vTexCoords  = tex;
    vNormal     = norm;
    vPosition   = pos;

    gl_Position = camProj * camView * mTransform * pos;
}