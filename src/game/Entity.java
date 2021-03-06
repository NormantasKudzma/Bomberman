package game;

import graphics.IRenderable;
import physics.ICollidable;
import physics.PhysicsBody;
import physics.PhysicsWorld;
import utils.Vector2;

public abstract class Entity<S extends IRenderable & IUpdatable> implements ICollidable, IRenderable, IUpdatable {
	protected PhysicsBody body;
	protected boolean isLifetimeFinite = false;
	protected boolean isDestructible = true;
	protected float lifetime = 0.0f;
	protected S sprite;
	protected boolean toBeDestroyed;
	
	public Entity(){
		toBeDestroyed = false;
	}
	
	public void applyForce(Vector2 dir){
		body.applyForce(dir);
	}
	
	public void destroy(){
		body.destroyBody();
	}
	
	public Vector2 getPosition(){
		return body.getPosition();
	}
	
	public float getRotation(){
		return body.getRotation();
	}
	
	public Vector2 getScale(){
		return body.getScale();
	}
	
	public S getSprite(){
		return sprite;
	}
	
	@Override
	public void handleCollision() {
		// TODO Auto-generated method stub
	}
	
	protected void initEntity(){
		if (body == null){
			body = PhysicsWorld.getInstance().getNewBody(this);
		}
	}
	
	public boolean isDestroyed(){
		return toBeDestroyed;
	}
	
	public boolean isDestructible(){
		return isDestructible;
	}
	
	public void markForDestruction(){
		onDestroy();
		toBeDestroyed = true;
	}
	
	public void onDestroy(){}
	
	public void setLifetime(float time){
		lifetime = time;
		isLifetimeFinite = true;
	}
	
	public void setPosition(Vector2 pos){
		body.setPosition(pos);
	}
	
	public void setPosition(float x, float y){
		body.setPosition(x, y);
	}
	
	public void setRotation(float angle){
		body.setRotation(angle);
	}
	
	/**
	 * Set scale for sprite, 
	 * WARNING: DOES NOT RESIZE COLLIDERS
	 * @param scale - desired sprite scale
	 */
	public void setScale(Vector2 scale){
		body.setScale(scale);
	}
	
	public void setSprite(S spr){
		sprite = spr;
	}
	
	public void render(){
		render(body.getPosition(), body.getRotation(), body.getScale());
	}
	
	public void render(Vector2 position, float rotation, Vector2 scale){
		sprite.render(position, rotation, scale);
	}
	
	public void update(float deltaTime){
		if (sprite != null){
			sprite.update(deltaTime);
		}
		
		if (isLifetimeFinite){
			lifetime -= deltaTime;
			if (lifetime <= 0.0f){
				markForDestruction();
			}
		}
	}
}
