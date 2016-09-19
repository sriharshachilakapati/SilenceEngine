uniform mat4 proj;
uniform mat4 view;

in vec4 position;
in vec4 color;
in vec2 texCoords;

out vec4 vColor;
out vec2 vTexCoords;

void main()
{
    vColor = color;
    vTexCoords = texCoords;

    gl_Position = proj * view * position;
}