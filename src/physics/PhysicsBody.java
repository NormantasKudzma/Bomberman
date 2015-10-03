package physics;

import game.Entity;

import java.util.ArrayList;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Rot;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;

import utils.Vector2;

/**
 * Wrapper for box2d Body class.
 * @author Nor-Vartotojas
 *
 */
public class PhysicsBody {
	private Body body;
	private ArrayList<Fixture> fixtureList = new ArrayList<Fixture>(1);
	
	// Internal variables for faster getters
	private Vector2 bodyPosition = new Vector2();
	private float bodyRotation;
	private Vector2 bodyScale = new Vector2(1.0f, -1.0f);
	
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
		
	public void attachBoxCollider(Vector2 size, Vector2 position, float rotation){		
		PolygonShape polygon = new PolygonShape();
		polygon.setAsBox(size.x(), size.y());
		if (position != null){
			polygon.centroid(new Transform(position.toVec2(), new Rot(rotation)));
		}
		
		attachCollider(polygon);
	}
	
	public void attachCircleCollider(float radius, Vector2 position){
		CircleShape circle = new CircleShape();
		circle.setRadius(radius);
		if (position != null){
			circle.m_p.set(position.toVec2());
		}
		
		attachCollider(circle);
	}
	
	public void attachPolygonCollider(Vector2 [] vertices){
		Vec2 [] vec2Verts = new Vec2[vertices.length];
		for (int i = 0; i < vertices.length; i++){
			vec2Verts[i] = vertices[i].toVec2();
		}
		
		PolygonShape polygon = new PolygonShape();
		polygon.set(vec2Verts, vec2Verts.length);
		
		attachCollider(polygon);
	}
	
	private void attachCollider(Shape shape){
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.userData = this;

		fixtureList.add(body.createFixture(fixtureDef));
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
		for (Fixture i : fixtureList){
			body.destroyFixture(i);
		}
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
		scale.setY(-scale.y());
		bodyScale = scale;
	}
}
