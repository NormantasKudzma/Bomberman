package graphics;

import game.Entity;

import java.util.ArrayList;

import utils.Paths;
import utils.Vector2;

public class BaseDialog extends Entity<Sprite2D> implements IClickable{
	protected ArrayList<Button> clickables = new ArrayList<Button>();
	protected boolean isVisible = false;
	protected Button lastClickable;
	protected String name = "BaseDialog";
	
	public BaseDialog(String name){
		this.name = name;
		this.setSprite(new Sprite2D(Paths.UI + "square_blue.png"));
		initEntity();
		getScale().mul(40f);
		setPosition(1.0f, 1.0f);
		initialize();
	}
	
	public void addClickable(Button clickable){
		clickables.add(clickable);
	}

	public ArrayList<Button> getClickables(){
		return clickables;
	}
	
	public String getName(){
		return name;
	}
	
	protected void initialize(){
		
	}
	
	@Override
	public boolean isMouseOver(Vector2 pos) {
		if (!isVisible){
			return false;
		}
		
		return  pos.x < getPosition().x + sprite.getRenderOffset().x &&
				pos.x > getPosition().x - sprite.getRenderOffset().x &&
				pos.y < getPosition().y + sprite.getRenderOffset().y &&
			    pos.y > getPosition().y - sprite.getRenderOffset().y;
	}

	public boolean isVisible(){
		return isVisible;
	}
	
	@Override
	public boolean onClick(Vector2 pos) {
		if (!isVisible){
			return false;
		}
		
		boolean ret = isMouseOver(pos);
		
		if (lastClickable != null){
			lastClickable.onClick(pos);
		}
		
		return ret;
	}
	
	@Override
	public void render() {
		if (isVisible){
			super.render();
			for (Button clickable : clickables){
				clickable.render();
			}
		}
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setVisible(boolean isVisible){
		this.isVisible = isVisible;
	}

	@Override
	public boolean onHover(Vector2 pos) {
		if (!isVisible){
			return false;
		}
		
		boolean ret = isMouseOver(pos);
		
		lastClickable = null;
		if (ret){
			for (Button clickable : clickables){
				if (clickable.onHover(pos)){
					lastClickable = clickable;
					break;
				}
			}
		}
	     
		return ret;
	}
}
