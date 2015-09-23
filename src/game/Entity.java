package game;

import graphics.Sprite2D;

import org.jbox2d.dynamics.Body;

import physics.ICollidable;
import physics.PhysicsWorld;
import utils.Vector2;

public abstract class Entity implements ICollidable {	
	private Sprite2D sprite;
	private Body body;
	private boolean toBeDestroyed;
	
	// Internals
	private Vector2 bodyPosition = new Vector2();
	private float bodyRotation;
	private Vector2 bodyScale = new Vector2();
	
	public Entity(){
		toBeDestroyed = false;
	}

	public void addSprite(String path){
		sprite = new Sprite2D(path);
	}
	
	public void destroy(){
		//stub
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
	
	public Sprite2D getSprite(){
		return sprite;
	}
	
	@Override
	public void handleCollision() {
		// TODO Auto-generated method stub		
	}
	
	public boolean isDestroyed(){
		return toBeDestroyed;
	}
	
	public void markForDestruction(){
		toBeDestroyed = true;
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
	
	public void setSprite(Sprite2D spr){
		sprite = spr;
	}
	
	public void render(){
		sprite.render(bodyPosition, bodyRotation, bodyScale);
	}
	
	public abstract void update(float deltaTime);
}
