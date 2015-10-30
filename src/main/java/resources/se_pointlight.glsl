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

//@include once se_material.glsl

struct PointLight
{
    float intensity;
    float range;
    vec3 position;
    vec4 color;
};

vec4 se_calculate_point_light(mat4 worldMatrix, mat4 modelMatrix, vec4 position, vec4 normal, PointLight light, Material material)
{
    mat4 lightMatrix = worldMatrix;
    mat3 normalMatrix = transpose(inverse(mat3(modelMatrix)));

    // The necessary information to calculate the point light
    vec3 surfaceNormal = normalMatrix * normal.xyz;
    vec3 surfacePosition = (modelMatrix * position).xyz;
    vec3 surfaceToLight = vec3(lightMatrix * vec4(light.position, 1)) - surfacePosition;
    vec3 surfaceToEye = normalize(reflect(-normalize(surfaceToLight), surfaceNormal));

    // Check if the object is out of range
    if (length(surfaceToLight) > light.range)
        return vec4(0.0);

    // The different components of light
    vec4 ambientLight = vec4(0.0);
    vec4 diffuseLight = vec4(0.0);
    vec4 specularLight = vec4(0.0);

    // Invert the normal for lighting the second side
    if (!gl_FrontFacing)
        surfaceNormal = -surfaceNormal;

    // Calculate the ambient light first
    ambientLight = light.color * material.ambientColor;

    // Calculate the diffuse light
    float diffuseFactor = clamp(max(0.0, dot(surfaceNormal, normalize(surfaceToLight))), 0.0, 1.0);

    if (diffuseFactor > 0.0)
    {
        diffuseLight = light.color * material.diffuseColor * diffuseFactor;

        // Calculate the specular light
        float specularFactor = pow(max(0.0, dot(normalize(surfaceToLight), surfaceToEye)), material.specularPower);

        if (specularFactor > 0.0)
            specularLight = light.color * material.specularColor * specularFactor;
    }

    // Calculate the final point light
    return (ambientLight + diffuseLight + specularLight) * light.intensity;
}