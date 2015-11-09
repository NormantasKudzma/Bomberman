package graphics;

import game.Entity;

import java.util.ArrayList;

import utils.Vector2;

public class BaseDialog extends Entity<Sprite2D> implements IClickable{
	private ArrayList<Button> clickables;
	private boolean isVisible = false;
	private Button lastClickable;
	
	public BaseDialog(){
		
	}
	
	public void addClickable(Button clickable){
		clickables.add(clickable);
	}

	public ArrayList<Button> getClickables(){
		return clickables;
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

	@Override
	public boolean onClick(Vector2 pos) {
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
	
	public void setVisible(boolean isVisible){
		this.isVisible = isVisible;
	}

	@Override
	public boolean onHover(Vector2 pos) {
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
