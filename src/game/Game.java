package game;

import java.util.ArrayList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import physics.PhysicsWorld;

import utils.Vector2;

public class Game {
	private static final int NUM_VELOCITY_ITERATIONS = 2;
	private static final int NUM_POSITION_ITERATIONS = 4;
	
	private ArrayList<Integer> destroyList = new ArrayList<Integer>();
	private ArrayList<Entity> entityList = new ArrayList<Entity>();
	private PhysicsWorld physicsWorld = PhysicsWorld.getInstance();
	
	public Game(){
		
	}
	
	public void render(){
		//physicsWorld.drawDebugData();
		
		for (Entity e : entityList){
			e.render();
		}
	}
	
	public void update(float deltaTime){
		// Update physics
		physicsWorld.getWorld().step(deltaTime, NUM_VELOCITY_ITERATIONS, NUM_POSITION_ITERATIONS);
		
		// Update all entities
		Entity e;
		for (int i = 0; i < entityList.size(); i++){
			e = entityList.get(i);
			e.update(deltaTime);
			if (e.isDestroyed()){
				destroyList.add(i);
			}
		}
		
		// Delete entities which are marked for destruction
		for (Integer i : destroyList){
			entityList.get(i).destroy();
			entityList.remove(i);
		}
		destroyList.clear();
	}
}