package com.github.elegantwhelp.entity;

import org.joml.*;

import com.github.elegantwhelp.assets.Assets;
import com.github.elegantwhelp.collision.AABB;
import com.github.elegantwhelp.collision.Collision;
import com.github.elegantwhelp.io.Window;
import com.github.elegantwhelp.render.*;
import com.github.elegantwhelp.world.World;

public abstract class Entity {
	protected AABB bounding_box;
	// private Texture texture;
	protected Animation[] animations;
	private int use_animation;
	
	protected Transform transform;
	
	public Entity(int max_animations, Transform transform) {
		this.animations = new Animation[max_animations];
		
		this.transform = transform;
		this.use_animation = 0;
		
		//bounding_box = new AABB(new Vector2f(transform.pos.x, transform.pos.y), new Vector2f(transform.scale.x, transform.scale.y));
		bounding_box = AABB.createAABB(transform.pos, transform.pos.add(transform.scale, new Vector2f()));
	}
	
	public Transform getTransform() { return transform; }
	
	public AABB getBoundingBox() {
		return bounding_box;
	}
	
	protected void setAnimation(int index, Animation animation) {
		animations[index] = animation;
	}
	
	public void useAnimation(int index) {
		this.use_animation = index;
	}
	
	public void move(Vector2f direction) {
		transform.pos.add(direction);
		
		bounding_box.getCenter().add(direction);
	}
	
	private static Vector2f cLength1 = new Vector2f();
	private static Vector2f cLength2 = new Vector2f();
	
	public void collideWithTiles(World world) {
		AABB[] boxes = new AABB[25];
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				boxes[i + j * 5] = world.getTileBoundingBox((int)transform.pos.x + i, (int)transform.pos.y + j);
			}
		}
		
		AABB box = null;
		for (int i = 0; i < boxes.length; i++) {
			if (boxes[i] != null) {
				if (box == null) box = boxes[i];
				
				cLength1 = box.getCenter().sub(bounding_box.getCenter(), cLength1);
				cLength2 = boxes[i].getCenter().sub(bounding_box.getCenter(), cLength2);
				
				if (cLength1.lengthSquared() > cLength2.lengthSquared()) {
					box = boxes[i];
				}
			}
		}
		if (box != null) {
			Collision data = bounding_box.getCollision(box);
			if (data.isIntersecting) {
				bounding_box.correctPosition(box, data);
				transform.pos.set(bounding_box.getCenter().sub(bounding_box.getHalfExtent(), new Vector2f()));
			}
			
			for (int i = 0; i < boxes.length; i++) {
				if (boxes[i] != null) {
					if (box == null) box = boxes[i];
					
					cLength1 = box.getCenter().sub(bounding_box.getCenter(), cLength1);
					cLength2 = boxes[i].getCenter().sub(bounding_box.getCenter(), cLength2);
					
					if (cLength1.lengthSquared() > cLength2.lengthSquared()) {
						box = boxes[i];
					}
				}
			}
			
			data = bounding_box.getCollision(box);
			if (data.isIntersecting) {
				bounding_box.correctPosition(box, data);
				transform.pos.set(bounding_box.getCenter().sub(bounding_box.getHalfExtent(), new Vector2f()));
			}
		}
	}
	
	public abstract void update(float delta, Window window, Camera camera, World world);
	
	public void render(Shader shader, Camera camera, World world) {
		Matrix4f target = camera.getTransformedProjection();
		target.mul(world.getWorldMatrix());
		
		shader.bind();
		shader.setUniform("sampler", 0);
		shader.setUniform("projection", transform.getProjection(target));
		animations[use_animation].bind(0);
		//Assets.getModel().render();
	}
	
	public void collideWithEntity(Entity entity) {
		Collision collision = bounding_box.getCollision(entity.bounding_box);
		
		if (collision.isIntersecting) {
			collision.distance.x /= 2;
			collision.distance.y /= 2;
			
			bounding_box.correctPosition(entity.bounding_box, collision);
			transform.pos.set(bounding_box.getCenter().sub(bounding_box.getHalfExtent(), new Vector2f()));
			
			entity.bounding_box.correctPosition(bounding_box, collision);
			entity.transform.pos.set(entity.bounding_box.getCenter().sub(entity.bounding_box.getHalfExtent(), new Vector2f()));
		}
	}
}
