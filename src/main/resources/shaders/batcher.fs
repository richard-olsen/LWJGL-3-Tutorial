#version 120

uniform sampler2D atlas;

varying vec2 tex_coords;

void main() {
	gl_FragColor = texture2D(atlas, tex_coords);
}

