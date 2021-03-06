package graphics;

import utils.Vector2;

public class Label extends Component{
	private SimpleFont font = new SimpleFont("");
	
	public Label(){
		this.initEntity();
		setVisible(true);
	}
	
	public Label(String text){
		setText(text);
	}
	
	public String getText(){
		return font.getText();
	}
	
	public void setText(String text){
		font.setText(text);
	}
	
	@Override
	public void render() {
		render(getPosition(), getRotation(), getScale());
	}
	
	@Override
	public void render(Vector2 position, float rotation, Vector2 scale) {
		font.render(position, rotation, scale);
	}
}
