#version 330 core

uniform sampler2D textureID;

uniform vec4 ambientLight;

in vec4 vColor;
in vec2 vTexCoords;

layout(location = 0) out vec4 fragColor;

void main()
{
    vec4 texColor = texture(textureID, vTexCoords);

    fragColor = vec4(min(texColor.rgb + vColor.rgb, vec3(1.0)), texColor.a * vColor.a);

    fragColor += ambientLight;
    fragColor = clamp(fragColor, ambientLight, vec4(1.0));
}
