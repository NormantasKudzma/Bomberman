package physics;

import game.Entity;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

public class PhysicsWorld {
	private static final PhysicsWorld INSTANCE = new PhysicsWorld();
	private World world;
	
	private PhysicsWorld(){
		
	}
	
	public static PhysicsWorld getInstance(){
		return INSTANCE;
	}
	
	public World getWorld(){
		return world;
	}
	
	public Body getBodyFromDef(BodyDef def){
		return world.createBody(def);
	}
	
	public Body getNewBody(Entity e){
		BodyDef bodyDef = new BodyDef();
		bodyDef.allowSleep = true;
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.userData = e;
		return world.createBody(bodyDef);
	}
	
	public BodyDef getRawBodyDef(){
		return new BodyDef();
	}
}
