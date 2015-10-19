package game;

import graphics.PhysicsDebugDraw;
import graphics.Sprite2D;
import graphics.SpriteAnimation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.jbox2d.common.OBBViewportTransform;

import physics.PhysicsWorld;
import utils.Paths;
import utils.Vector2;
public class Game implements IUpdatable{
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
		PlayerEntity p = new PlayerEntity(); 
		Entity<Sprite2D> e = new Entity<Sprite2D>(){

			@Override
			public void update(float deltaTime) {
				//getPosition().add(0.0002f, 0.0002f);
			}

			@Override
			public void render(Vector2 position, float rotation, Vector2 scale) {
				// TODO Auto-generated method stub
				
			}			
		};
		initMap();
		p.readKeybindings();
		p.setSprite(new SpriteAnimation("ranger_f.json"));
		p.setPosition(1, 1);
		p.initEntity();
		e.setPosition(0, 0);
		e.setSprite(new Sprite2D(Paths.TEXTURES + "smetona.jpg"));
		e.initEntity();
		entityList.add(p);
		entityList.add(e);
		
	}
	
	public void initMap(){
		final int mapSize = 33;
		char[][] map = new char[mapSize][mapSize];		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(Paths.MAPS + "map01"));
			int i = 0; // array i index
			int j = 0; // array j index
			
			int c = 0; 
	        try {
				while((c = reader.read()) != -1) {
				    char character = (char) c;
				    if(character == 'W' || character == ' '){
				    	map[i][j] = character;
				    	j++;
				    }
				    if(j == mapSize){
				    	i++;
				    	j = 0;	
				    }				    
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(int i = 0; i < mapSize; i++)
		{
			for(int j = 0; j < mapSize; j++)
			{
				switch (map[i][j])
				{
					case 'W':
						createWall(i, j);
						break;
					default:
						break;
				}
				
			}
		}	
	}
	
	private void createWall(int i, int j){
		WallEntity wall = new WallEntity();
		wall.setSprite(new Sprite2D(Paths.TEXTURES + "wall.jpg"));
		wall.setPosition(i * 0.06f, j * 0.06f);
		wall.initEntity();
		entityList.add(wall);
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
