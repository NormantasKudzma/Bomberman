package graphics;

import game.Entity;

import java.lang.reflect.Method;

import utils.Vector2;

public class Button extends Entity<Sprite2D> implements IClickable{
	Object callbackObject;
	Method callbackMethod;

	public Button(){}
	
	public Button(Object obj, Method m){
		callbackObject = obj;
		callbackMethod = m;
	}
	
	public boolean isClicked(Vector2 clickPos){
		return clickPos.x < getPosition().x + sprite.getHalfSize().x &&
			   clickPos.x > getPosition().x - sprite.getHalfSize().x &&
			   clickPos.y < getPosition().y + sprite.getHalfSize().y &&
			   clickPos.y > getPosition().y - sprite.getHalfSize().y;
	}
	
	@Override
	public void onClick() {
		try {
			callbackMethod.invoke(callbackObject);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setCallbackMethod(Method m){
		callbackMethod = m;
	}
	
	public void setCallbackObject(Object obj){
		callbackObject = obj;
	}
}
