package game;

import graphics.Sprite2D;
import physics.ICollidable;
import physics.PhysicsBody;
import physics.PhysicsWorld;
import utils.Vector2;

public abstract class Entity implements ICollidable {	
	private Sprite2D sprite;
	private PhysicsBody body;
	private boolean toBeDestroyed;
	
	public Entity(){
		toBeDestroyed = false;
		initEntity();
	}

	public void addSprite(String path){
		sprite = new Sprite2D(path);
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
	
	public Sprite2D getSprite(){
		return sprite;
	}
	
	@Override
	public void handleCollision() {
		// TODO Auto-generated method stub
	}
	
	protected void initEntity(){
		body = PhysicsWorld.getInstance().getNewBody(this);
	}
	
	public boolean isDestroyed(){
		return toBeDestroyed;
	}
	
	public void markForDestruction(){
		toBeDestroyed = true;
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
	
	public void setSprite(Sprite2D spr){
		sprite = spr;
	}
	
	public void render(){
		sprite.render(body.getPosition(), body.getRotation(), body.getScale());
	}
	
	public abstract void update(float deltaTime);
}
