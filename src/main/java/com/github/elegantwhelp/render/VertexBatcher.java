package com.github.elegantwhelp.render;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

public class VertexBatcher {
	private FloatBuffer vertices;
	
	public static final int MAX_VERTICES = 100;
	public static final int VERTEX_SIZE = 4;
	
	private int vbo;
	
	private int vertexCount;
	
	public VertexBatcher() {
		vertices = BufferUtils.createFloatBuffer(MAX_VERTICES * VERTEX_SIZE);
		vertexCount = 0;
	}
	
	public void init() {
		vbo = glGenBuffers();
	}
	
	public void destroy() {
		glDeleteBuffers(vbo);
	}
	
	public void addVertex(float x, float y, float u, float v) {
		if (vertexCount >= MAX_VERTICES) {
			return;
		}
		vertices.put(x);
		vertices.put(y);
		vertices.put(u);
		vertices.put(v);
		vertexCount++;
	}
	
//	public void addQuad(float x, float y, float x2, float y2) {
//		addVertex(x, y);		// Top Left
//		addVertex(x2, y);		// Top Right
//		addVertex(x2, y2);		// Bottom Right
//		
//		addVertex(x2, y2);		// Bottom Right
//		addVertex(x, y2);		// Bottom Left
//		addVertex(x, y);		// Top Left
//	}
	
	public void draw() {
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		
		glBindBuffer(GL_ARRAY_BUFFER, vbo);

		vertices.flip();
		glBufferData(GL_ARRAY_BUFFER, vertices, GL_DYNAMIC_DRAW);
		
		glVertexAttribPointer(0, 2, GL_FLOAT, false, VERTEX_SIZE * 4, 0);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, VERTEX_SIZE * 4, 8);
		
		glDrawArrays(GL_TRIANGLES, 0, vertexCount);
		
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(0);
		
		vertices.clear();
		vertexCount = 0;
	}
}
