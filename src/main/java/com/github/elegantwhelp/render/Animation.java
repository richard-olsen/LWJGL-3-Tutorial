package com.github.elegantwhelp.render;

import com.github.elegantwhelp.io.Timer;

public class Animation {
	private int tileIndex;
	private int tileIndexEnd;
	private int tileIndexBegin;
	
	private double elapsedTime;
	private double currentTime;
	private double lastTime;
	private double fps;
	
	public Animation(int beginIndex, int endIndex, int fps, String filename) {
		this.tileIndexBegin = this.tileIndex =  beginIndex;
		this.tileIndexEnd = endIndex;
		this.elapsedTime = 0;
		this.currentTime = 0;
		this.lastTime = Timer.getTime();
		this.fps = 1.0 / fps;
	}
	
	public void animate() {
		this.currentTime = Timer.getTime();
		this.elapsedTime += currentTime - lastTime;
		
		if (elapsedTime >= fps) {
			elapsedTime = 0;
			tileIndex++;
		}
		
		if (tileIndex > tileIndexEnd) tileIndex = tileIndexBegin;
		
		this.lastTime = currentTime;
	}
	
	public int getIndex() { return tileIndex; }
}
