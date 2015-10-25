package utils;

import org.jbox2d.common.Vec2;

public class Vector2 {
	public static final Vector2 right = new Vector2(1.0f, 0.0f);
	public static final Vector2 left = 	new Vector2(-1.0f, 0.0f);
	public static final Vector2 up = 	new Vector2(0.0f, -1.0f);
	public static final Vector2 down = 	new Vector2(0.0f, 1.0f);
	public static final Vector2 zero = 	new Vector2(0.0f, 0.0f);
	public static final Vector2 one = 	new Vector2(1.0f, 1.0f);
	
	public float x, y;
	
	public Vector2(){
		this(0, 0);
	}
	
	public Vector2(Vector2 i){
		x = i.x;
		y = i.y;
	}
	
	public Vector2(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public float x(){
		return x;
	}
	
	public float y(){
		return y;
	}
	
	public void set(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public void setX(float x){
		this.x = x;
	}
	
	public void setY(float y){
		this.y = y;
	}
	
	public void add(float x, float y){
		this.x += x;
		this.y += y;
	}
	
	public void add(Vector2 i){
		this.x += i.x;
		this.y += i.y;
	}
	
	public float angle(){
		return FastMath.atan2(x, y);
	}
	
	public Vector2 copy(){
		return new Vector2(x, y);
	}
	
	public float dot(Vector2 i){
		return x * i.x + y * i.y;
	}
	
	public float len(){
		return (float)FastMath.fastSqrt(x * x + y * y);
	}
	
	public void mul(float c){
		x *= c;
		y *= c;
	}
	
	public void mul(Vector2 i){
		x *= i.x;
		y *= i.y;
	}
	
	public void sub(Vector2 i){
		this.x -= i.x;
		this.y -= i.y;
	}
	
	public Vec2 toVec2()
	{
		return toVec2(this);
	}
	
	public static Vector2 add(Vector2 i, Vector2 j){
		return new Vector2(i.x + j.x, i.y + j.y);
	}

	public static Vector2 copy(Vector2 i){
		return new Vector2(i.x, i.y);
	}
	
	public static Vector2 fromVec2(Vec2 v){
		return new Vector2(v.x, v.y);
	}
	
	public static Vec2 toVec2(Vector2 v){
		return new Vec2(v.x, v.y);
	}
	
	public static float distanceSqr(Vector2 i, Vector2 j){
		float dx = j.x - i.x;
		float dy = j.y - i.y;
		return dx * dx + dy * dy;
	}
	
	public static float distance(Vector2 i, Vector2 j){
		return (float)Math.sqrt(distanceSqr(i, j));
	}
	
	@Override
	public String toString() {
		return String.format("Vector2[x:%f, y:%f] ", x, y);
	}
}
