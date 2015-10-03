package physics;

import org.jbox2d.common.IViewportTransform;
import org.jbox2d.common.Vec2;

public class PhysicsTransform implements IViewportTransform{

	@Override
	public Vec2 getCenter() {
		return new Vec2(1, 1);
	}

	@Override
	public Vec2 getExtents() {
		return new Vec2(2, 2);
	}

	@Override
	public void getScreenToWorld(Vec2 arg0, Vec2 arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getScreenVectorToWorld(Vec2 arg0, Vec2 arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getWorldToScreen(Vec2 arg0, Vec2 arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getWorldVectorToScreen(Vec2 arg0, Vec2 arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isYFlip() {
		return false;
	}

	@Override
	public void setCamera(float arg0, float arg1, float arg2) {
		// a big nope
	}

	@Override
	public void setCenter(Vec2 arg0) {
		// nope
	}

	@Override
	public void setCenter(float arg0, float arg1) {
		// nope
	}

	@Override
	public void setExtents(Vec2 arg0) {
		// nope
	}

	@Override
	public void setExtents(float arg0, float arg1) {
		// nope
	}

	@Override
	public void setYFlip(boolean isFlipped) {
		// nope
	}
}
