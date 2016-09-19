uniform sampler2D tex;

in vec4 vColor;
in vec2 vTexCoords;

void main()
{
    vec4 texColor = texture(tex, vTexCoords);
    g_FragColor = texColor * vColor;
}