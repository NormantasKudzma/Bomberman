package graphics;

import utils.Vector2;

public class Label implements IRenderable{
	private SimpleFont font = new SimpleFont("");
	private Vector2 position = new Vector2(1, 1);
	private Vector2 scale = new Vector2(1, 1);
	
	public Label(){
		
	}
	
	public Label(String text){
		font.setText(text);
	}
	
	public String getText(){
		return font.getText();
	}
	
	public void setText(String text){
		font.setText(text);
	}

	public void render(){
		render(position, 0, scale);
	}
	
	@Override
	public void render(Vector2 position, float rotation, Vector2 scale) {
		font.render(position, rotation, scale);
	}

	public Vector2 getScale() {
		return scale;
	}

	public void setScale(Vector2 scale) {
		this.scale = scale;
	}

	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
	}
}
