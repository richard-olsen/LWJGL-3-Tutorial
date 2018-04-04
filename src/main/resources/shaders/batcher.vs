#version 120

attribute vec2 vertices;

uniform mat4 cam_projection;

void main() {
	gl_Position = cam_projection * vec4(vertices, 0, 1);
}

