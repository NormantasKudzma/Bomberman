package graphics;

import game.Entity;

import java.util.ArrayList;

import utils.Paths;
import utils.Vector2;

public class BaseDialog extends Component{
	protected ArrayList<Button> clickables = new ArrayList<Button>();
	protected boolean isVisible = false;
	protected String name = "BaseDialog";
	
	public BaseDialog(String name){
		this.name = name;
		this.setSprite(new Sprite2D(Paths.UI + "square_blue.png"));		
		getScale().mul(40f);
		setPosition(1.0f, 1.0f);		
	}
}
