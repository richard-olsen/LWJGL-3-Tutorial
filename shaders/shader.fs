#version 120

uniform sampler2D sampler;

varying vec2 tex_coords;

void main() {
	gl_FragColor = texture2D(sampler, tex_coords);
}