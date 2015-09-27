package physics;

import game.Entity;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;

import utils.Vector2;

/**
 * Wrapper for box2d Body class.
 * @author Nor-Vartotojas
 *
 */
public class PhysicsBody {
	private Body body;
	
	// Internal variables for faster getters
	private Vector2 bodyPosition = new Vector2();
	private float bodyRotation;
	private Vector2 bodyScale = new Vector2();
	
	/**
	 * Creates a body with default settings.
	 */
	public PhysicsBody(){
		createBody(null, null);
	}
	
	/**
	 * Creates a body with a defined user data.
	 * @param e - Entity that will be set to UserData.
	 */
	public PhysicsBody(Entity e){
		createBody(null, e);
	}
	
	/**
	 * Creates a body from body definition.
	 * @param def - Body Definition.
	 */
	public PhysicsBody(BodyDef def){
		createBody(def, null);
	}
	
	private void createBody(BodyDef def, Entity e){
		if (def == null){
			def = new BodyDef();
			def.allowSleep = true;
			def.type = BodyType.DYNAMIC;
			def.userData = e;
		}
		body = PhysicsWorld.getInstance().getWorld().createBody(def);
	}
	
	public void destroyBody(){
		PhysicsWorld.getInstance().getWorld().destroyBody(body);
		body = null;
	}
	
	public Vector2 getPosition(){
		return bodyPosition;
	}
	
	public float getRotation(){
		return bodyRotation;
	}
	
	public Vector2 getScale(){
		return bodyScale;
	}
	
	public void setPosition(Vector2 pos){
		bodyPosition = pos;
		body.setTransform(Vector2.toVec2(pos), bodyRotation);
	}
	
	public void setPosition(float x, float y){
		bodyPosition.set(x, y);
		setPosition(bodyPosition);
	}
	
	public void setRotation(float angle){
		bodyRotation = angle;
		body.setTransform(Vector2.toVec2(bodyPosition), angle);
	}
	
	/**
	 * Set scale for sprite, 
	 * WARNING: DOES NOT RESIZE COLLIDERS
	 * @param scale - desired sprite scale
	 */
	public void setScale(Vector2 scale){
		bodyScale = scale;
	}
}
