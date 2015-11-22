package graphics;

import game.Entity;

import java.lang.reflect.Method;

import utils.Paths;
import utils.Vector2;

public class Button extends Entity<Sprite2D> implements IClickable{
	private Object callbackObject;
	private Method callbackMethod;
	private Sprite2D normalSprite;
	private Sprite2D hoverSprite;
	private SimpleFont font;
	private Vector2 fontScale;
	
	public Button(){
		this(null, null, "");
	}
	
	public Button(Object obj, Method m, String text){
		callbackObject = obj;
		callbackMethod = m;
		normalSprite = new Sprite2D(Paths.UI + "button_green.png");
		hoverSprite = new Sprite2D(Paths.UI + "hover_green.png");
		setSprite(normalSprite);
		//this.text = new TrueTypeFont(ConfigManager.loadFont(Paths.DEFAULT_FONT, 14), false);
		//this.text.setText(text);
		super.initEntity();
	}
	
	public String getText(){
		return font.getText();
	}
	
	@Override
	protected void initEntity() {
		//stub
	}
	
	public boolean isMouseOver(Vector2 pos){
		return pos.x < getPosition().x + sprite.getRenderOffset().x &&
			   pos.x > getPosition().x - sprite.getRenderOffset().x &&
			   pos.y < getPosition().y + sprite.getRenderOffset().y &&
			   pos.y > getPosition().y - sprite.getRenderOffset().y;
	}
	
	@Override
	public boolean onClick(Vector2 pos) {
		boolean ret = isMouseOver(pos);
		if (!ret){
			return ret;
		}
		
		try {
			if (callbackMethod != null && callbackObject != null){
				callbackMethod.invoke(callbackObject);
				return ret;
			}
		}
		catch (Exception e) {
			return ret;		
		}
		return ret;
	}

	public void setCallbackMethod(Method m){
		callbackMethod = m;
	}
	
	public void setCallbackObject(Object obj){
		callbackObject = obj;
	}

	@Override
	public void setScale(Vector2 scale) {
		float min = Math.min(scale.x, scale.y);
		fontScale = (new Vector2(min, min)).mul(0.25f);
		super.setScale(scale);
	}
	
	public void setText(String text){
		if (font == null){
			font = new SimpleFont(text);
		}
		else {
			font.setText(text);
		}
	}
	
	@Override
	public void render(Vector2 position, float rotation, Vector2 scale) {
		super.render(position, rotation, scale);
		if (font != null){
			font.render(position, rotation, fontScale);
		}
	}
	
	@Override
	public boolean onHover(Vector2 pos) {
		boolean ret = isMouseOver(pos);
		if (ret){
			setSprite(hoverSprite);
		}
		else {
			setSprite(normalSprite);
		}
		return ret;
	}
}
