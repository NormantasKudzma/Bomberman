package game;

import org.jbox2d.callbacks.QueryCallback;
import org.jbox2d.collision.AABB;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;

import physics.PhysicsWorld;

import graphics.Sprite2D;
import utils.Paths;
import utils.Vector2;

public class BombEntity extends Entity<Sprite2D> implements QueryCallback{
	private PlayerEntity owner;
	
	public BombEntity(PlayerEntity player){
		owner = player;
	}
	
	@Override
	protected void initEntity() {
		super.initEntity();
		setSprite(new Sprite2D(Paths.TEXTURES + "bomb.png"));
		getScale().mul(2.0f);
		Vector2 fullScale = getScale().copy().mul(sprite.getInternalScale()).mul(0.6f);
		body.attachBoxCollider(fullScale, new Vector2(), 0);
		body.getBody().setType(BodyType.STATIC);
		setLifetime(3.0f);
	}
	
	@Override
	public void onDestroy() {
		if (owner != null){
			owner.freeBomb();
		}
		
		AABB upDown = new AABB(getPosition().copy().sub(0.0625f, 0.0625f * 3).toVec2(), getPosition().copy().add(0.0625f, 0.0625f * 3).toVec2());
		PhysicsWorld.getInstance().getWorld().queryAABB(this, upDown);
	}

	@Override
	public boolean reportFixture(Fixture f) {
		if (f.m_body != null && f.m_body.m_userData instanceof Entity){
			Entity e = (Entity)f.m_body.m_userData;
			if (e != this && !e.isDestroyed()){
				if (e.isDestructible()){
					e.markForDestruction();
				}
			}
		}
		return true;
	}
}
