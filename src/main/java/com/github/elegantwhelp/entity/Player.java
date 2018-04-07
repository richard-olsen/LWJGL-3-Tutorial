package com.github.elegantwhelp.entity;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import com.github.elegantwhelp.io.Window;
import com.github.elegantwhelp.render.Animation;
import com.github.elegantwhelp.render.Camera;
import com.github.elegantwhelp.world.World;

public class Player extends Entity {
	public static final int ANIM_IDLE = 0;
	public static final int ANIM_WALK = 1;
	public static final int ANIM_SIZE = 2;
	
	public Player(Transform transform) {
		super(ANIM_SIZE, transform);
		setAnimation(ANIM_IDLE, new Animation(0, 3, 2, "player/idle"));
		setAnimation(ANIM_WALK, new Animation(4, 7, 2, "player/walking"));
	}
	
	@Override
	public void update(float delta, Window window, Camera camera, World world) {
		Vector2f movement = new Vector2f();
		if (window.getInput().isKeyDown(GLFW.GLFW_KEY_A)) movement.add(-7.5f * delta, 0);
		
		if (window.getInput().isKeyDown(GLFW.GLFW_KEY_D)) movement.add(7.5f * delta, 0);
		
		if (window.getInput().isKeyDown(GLFW.GLFW_KEY_W)) movement.add(0, -7.5f * delta);
		
		if (window.getInput().isKeyDown(GLFW.GLFW_KEY_S)) movement.add(0, 7.5f * delta);
		
		move(movement);
		
		if (movement.x != 0 || movement.y != 0)
			useAnimation(ANIM_WALK);
		else 
			useAnimation(ANIM_IDLE);
		
		// Reuse movement instead of allocating a new vector
		camera.getPosition().lerp(transform.pos.add(bounding_box.getHalfExtent(), movement).mul(world.getScale()), 5 * delta);
		//camera.getPosition().set(new Vector3f((transform.pos.add(bounding_box.getHalfExtent(), new Vector2f())).mul(world.getScale()), 0));
		
		super.update(delta, window, camera, world);
	}
}
