/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2015 Sri Harsha Chilakapati
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

#version 330 core

/**
 * The Material structure stores the details about the material
 */
struct Material
{
    vec4 ambientColor;
    vec4 diffuseColor;
    vec4 specularColor;

    float dissolve;
    float specularPower;
    float illumination;
};

// The light structure
struct Light
{
    float intensity;
    float range;
    vec3 position;
    vec4 color;
};

uniform mat4 mTransform;
uniform mat4 camProj;
uniform mat4 camView;

uniform sampler2D textureID;
uniform Material material;
uniform Light light;

in vec4 vColor;
in vec4 vNormal;
in vec4 vPosition;
in vec2 vTexCoords;

layout(location = 0) out vec4 fragColor;

vec4 getBaseColor()
{
    // Create the texture color
    vec4 texColor = texture(textureID, vTexCoords);

    return vec4(min(texColor.rgb + vColor.rgb, vec3(1.0)), texColor.a * vColor.a);
}

vec4 getPointLight()
{
    mat4 modelMatrix = camProj * camView * mTransform;
    mat4 lightMatrix = camProj * camView;

    mat3 normalMatrix = transpose(inverse(mat3(modelMatrix)));
    vec3 normal = normalize(normalMatrix * vec3(vNormal));

    vec3 surfacePos = vec3(modelMatrix * vPosition);
    vec3 surfaceToLight = vec3(lightMatrix * vec4(light.position, 1)) - surfacePos;
    vec3 surfaceToEye = reflect(-normalize(surfaceToLight), normal);

    // The brightness
    float brightness = light.intensity * clamp(max(0.0, dot(normal, normalize(surfaceToLight))), 0.0, 1.0);

    // Ambient light
    vec4 ambient = material.dissolve * material.ambientColor * light.color * light.intensity;

    // Check if in range
    if (light.range < length(surfaceToLight))
        return vec4(0.0);

    brightness /= length(surfaceToEye);

    // Diffuse light
    float diffuseCoefficient = material.illumination / brightness;
    vec4 diffuse = diffuseCoefficient * material.diffuseColor * light.color;

    // Specular light
    float specularCoefficient = 0.0;

    if(diffuseCoefficient > 0.0)
        specularCoefficient = pow(max(0.0, dot(normalize(surfaceToLight), surfaceToEye)), material.specularPower);

    vec4 specular = specularCoefficient * material.specularColor * light.color;

    // Attenuation
    float distanceToLight = length(surfaceToLight);
    float attenuation = 1.0 / (1.0 + material.illumination * pow(distanceToLight, 2));

    // The linear color
    vec3 linearColor = vec3(ambient + attenuation * light.color * (diffuse + specular));

    // Gamma correction
    vec3 gamma = vec3(1.0/2.2);
    return brightness * min(vec4(1.0), vec4(pow(linearColor, gamma), getBaseColor().a));
}

void main()
{
    fragColor = getBaseColor() * getPointLight();
}
