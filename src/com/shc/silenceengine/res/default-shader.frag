#version 330 core

uniform sampler2D texture;

in vec4 vColor;
in vec2 vTexCoords;

layout(location = 0) out vec4 fragColor;

void main()
{
    vec4 texColor = texture(texture, vTexCoords);
    vec4 finalColor = vColor;

    if (texColor.a != 0.0)
        finalColor *= texColor;

    fragColor = vColor;
}
