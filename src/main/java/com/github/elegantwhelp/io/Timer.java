package com.github.elegantwhelp.io;

public class Timer {
	public static double getTime() {
		return (double) System.nanoTime() / (double) 1000000000L;
	}
}
