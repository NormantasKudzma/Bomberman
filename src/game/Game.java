package game;

import graphics.PhysicsDebugDraw;

import java.util.ArrayList;

import org.jbox2d.common.OBBViewportTransform;

import physics.PhysicsWorld;
import utils.Paths;

public class Game {
	private static final int NUM_VELOCITY_ITERATIONS = 2;
	private static final int NUM_POSITION_ITERATIONS = 4;
	
	private ArrayList<Integer> destroyList = new ArrayList<Integer>();
	private ArrayList<Entity> entityList = new ArrayList<Entity>();
	private PhysicsWorld physicsWorld = PhysicsWorld.getInstance();
	
	public Game(){
		physicsWorld.setDebugDraw(new PhysicsDebugDraw(new OBBViewportTransform()));
	}
	
	/** Game destruction method. This method will be called last.
	 *  Any resources that must be released, should be released here.
	 */
	public void destroy(){
		//stub
	}
	
	/** Game initialization (creating entities, loading map etc.) goes here
	 * 
	 */
	public void init(){
		Entity e = new Entity(){

			@Override
			public void update(float deltaTime) {
				//getPosition().add(0.0002f, 0.0002f);
			}
			
		};
		e.setPosition(0, 0);
		e.addSprite(Paths.TEXTURES + "smetona.jpg");
		e.initEntity();
		
		entityList.add(e);
	}
	
	/** Render method - call render for each and every entity
	 * 
	 */
	public void render(){
		//physicsWorld.drawDebugData();
		
		for (Entity e : entityList){
			e.render();
		}
	}
	
	/**
	 * Main game update method. Physics and entities should be moved during update.
	 * @param deltaTime - time that has passed since last frame (in ms)
	 */
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
