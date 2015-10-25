package web;

import graphics.Sprite2D;
import utils.Vector2;

import com.googlecode.gwtgl.binding.WebGLTexture;

public class WebSprite2D extends Sprite2D{
	private WebTexture texture;
	
	public WebSprite2D(){
		
	}
	
	public WebSprite2D(String path){
		this(path, new Vector2(), null);
	}
	
	public WebSprite2D(WebTexture tex){
		this(tex, new Vector2(), null);
	}
	
	public WebSprite2D(String path, Vector2 tl, Vector2 br){		
		loadTexture(path);
		topLeft = tl;
		if (br == null) {
			//botRight = new Vector2(texture.getWidth(), texture.getHeight());
		}
		else {
			botRight = br;
		}
	}
	
	public WebSprite2D(WebTexture tex, Vector2 tl, Vector2 br){
		texture = tex;
		topLeft = tl;
		if (br == null) {
			//botRight = new Vector2(texture.getWidth(), texture.getHeight());
		}
		else {
			botRight = br;
		}
	}
	
	public WebTexture getWebTexture(){
		return texture;
	}
	
	@Override
	public void loadTexture(String path) {

		//internalScale.set(DEFAULT_SPRITE_SIZE / texture, DEFAULT_SPRITE_SIZE / texture);
	}
	
	@Override
	public void render(Vector2 position, float rotation, Vector2 scale) {
		
		
	}
}
