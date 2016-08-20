#ifdef GL_ES
    precision mediump float;
#endif

uniform sampler2D tex;

varying vec4 vColor;
varying vec2 vTexCoords;

void main()
{
    vec4 texColor = texture2D(tex, vTexCoords);
    gl_FragColor = vec4(texColor.rgb + vColor.rgb, texColor.a * vColor.a);
}