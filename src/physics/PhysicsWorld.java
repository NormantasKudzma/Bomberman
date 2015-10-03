package physics;

import game.Entity;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.World;

public class PhysicsWorld {
	private static final PhysicsWorld INSTANCE = new PhysicsWorld();
	private World world;
	
	private PhysicsWorld(){
		world = new World(new Vec2());
	}
	
	public static PhysicsWorld getInstance(){
		return INSTANCE;
	}
	
	public World getWorld(){
		return world;
	}
	
	public PhysicsBody getBodyFromDef(BodyDef def){
		return new PhysicsBody(def);
	}
	
	public PhysicsBody getNewBody(Entity e){
		return new PhysicsBody(e);
	}
	
	public BodyDef getRawBodyDef(){
		return new BodyDef();
	}
}
