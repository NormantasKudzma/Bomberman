package game;

import org.jbox2d.dynamics.BodyType;

import graphics.Sprite2D;
import utils.Vector2;

public class WallEntity extends Entity<Sprite2D> {

	@Override
	protected void initEntity() {
		super.initEntity();
		Vector2 fullScale = getScale().copy().mul(sprite.getInternalScale());
		body.attachBoxCollider(fullScale, new Vector2(), 0);
		body.getBody().setType(BodyType.STATIC);
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
	}
}
