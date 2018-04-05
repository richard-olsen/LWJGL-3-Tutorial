#version 120

attribute vec2 vertices;
attribute vec2 textures;

uniform mat4 cam_projection;

varying vec2 tex_coords;

void main() {
	tex_coords = textures;
	gl_Position = cam_projection * vec4(vertices, 0, 1);
}

