#ifdef GL_ES
    precision mediump float;
#endif

uniform sampler2D spriteTex;
uniform vec4 texCoords;
uniform vec4 color;
uniform bool customTexCoords;

varying vec2 vTexCoords;

void main()
{
    vec2 tc = customTexCoords ? vTexCoords : texCoords;
    vec4 texel = texture2D(spriteTex, tc);

    gl_FragColor = vec4(texel.rgb + color.rgb, texel.a * color.a);
}
