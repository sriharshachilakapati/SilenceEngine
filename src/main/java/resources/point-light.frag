#version 330 core

uniform mat4 mTransform;
uniform mat4 camProj;
uniform mat4 camView;

uniform sampler2D textureID;

uniform vec4 ambient;

in vec4 vColor;
in vec4 vNormal;
in vec4 vPosition;
in vec2 vTexCoords;

layout(location = 0) out vec4 fragColor;

// The light structure
uniform struct Light
{
    vec3 position;
    vec4 color;
}
light;

void main()
{
    // Create the texture color
    vec4 texColor = texture(textureID, vTexCoords);

    // Create the baseColor from texture color and vColor
    vec4 baseColor = vec4(min(texColor.rgb + vColor.rgb, vec3(1.0)), texColor.a * vColor.a);

    // Calculate the point light
    mat4 modelMatrix = camProj * camView * mTransform;
    mat4 lightMatrix = camProj * camView;

    mat3 normalMatrix = transpose(inverse(mat3(modelMatrix)));
    vec3 normal = normalMatrix * vec3(vNormal);

    vec3 fragPosition = vec3(modelMatrix * vPosition);
    vec3 surfaceToLight = vec3(lightMatrix * vec4(light.position, 1)) - fragPosition;

    float brightness = dot(normal, surfaceToLight) / (length(surfaceToLight) * length(normal));
    brightness = clamp(brightness, 0, 1);

    fragColor = brightness * light.color * baseColor;
}
