package graphics;

import game.Entity;

import java.util.ArrayList;

import utils.Vector2;

public class BaseDialog extends Entity<Sprite2D> implements IClickable{
	private ArrayList<Button> clickables;
	private boolean isVisible = false;
	
	public BaseDialog(){
		
	}
	
	public void addClickable(Button clickable){
		clickables.add(clickable);
	}

	public ArrayList<Button> getClickables(){
		return clickables;
	}
	
	@Override
	public boolean isClicked(Vector2 clickPos) {
		if (!isVisible){
			return false;
		}
		
		boolean ret = clickPos.x < getPosition().x + sprite.getHalfSize().x &&
				      clickPos.x > getPosition().x - sprite.getHalfSize().x &&
				      clickPos.y < getPosition().y + sprite.getHalfSize().y &&
				      clickPos.y > getPosition().y - sprite.getHalfSize().y;
				      
	    if (ret){
	    	for (Button clickable : clickables){
	    		if (clickable.isClicked(clickPos)){
	    			clickable.onClick();
	    			break;
	    		}
	    	}
	    }
				      
		return ret;
	}

	@Override
	public void onClick() {
		//stub
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
}
