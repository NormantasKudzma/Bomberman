package graphics;


import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;

import game.IUpdatable;

import java.io.IOException;


import java.io.IOException;

import utils.Vector2;



public class Sprite2D implements IRenderable, IUpdatable {
	private static final float DEFAULT_SPRITE_SIZE = 0.0625f;
	
	private Texture texture;	// Sprite's texture
	private Vector2 internalScale = new Vector2(1.0f, 1.0f);	// Sprite size in game units (0;0)->(2;2)
	private Vector2 topLeft;
	private Vector2 botRight;
	
	public Sprite2D(){}
	
	public Sprite2D(String path){
		this(path, new Vector2(), null);
	}
	
	public Sprite2D(Texture tex){
		this(tex, new Vector2(), null);
	}
	
	public Sprite2D(String path, Vector2 tl, Vector2 br){		
		loadTexture(path);
		topLeft = tl;
		if (br == null) {
			botRight = new Vector2(texture.getWidth(), texture.getHeight());
		}
		else {
			botRight = br;
		}
	}
	
	public Sprite2D(Texture tex, Vector2 tl, Vector2 br){
		texture = tex;
		topLeft = tl;
		if (br == null) {
			botRight = new Vector2(texture.getWidth(), texture.getHeight());
		}
		else {
			botRight = br;
		}
	}
	
	public Vector2 getSize(){
		return new Vector2(texture.getImageWidth(), texture.getImageHeight());
	}
	
	public Texture getTexture(){
		return texture;
	}
	
	public void loadTexture(String path){
		try {
			texture = TextureLoader.getInstance().getTexture(path);
			internalScale.set(DEFAULT_SPRITE_SIZE / texture.getWidth(), DEFAULT_SPRITE_SIZE / texture.getHeight());
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public void render(Vector2 position){
		render(position, 0.0f);
	}
	
	public void render(Vector2 position, float rotation){
		render(position, rotation, Vector2.one);
	}
	
	@Override
	public void render(Vector2 position, float rotation, Vector2 scale) {
		// store the current model matrix
        glPushMatrix();
        glLoadIdentity();
        // bind to the appropriate texture for this sprite
        texture.bind();
 
        // translate to the right location and prepare to draw
        glTranslatef(position.x - 1.0f - topLeft.x * scale.x, -position.y + 1.0f - topLeft.y * scale.y, 0);        
        glScalef(scale.x(), scale.y(), 1.0f);
        glScalef(internalScale.x, internalScale.y, 1.0f);
        glRotatef(rotation, 0, 0, 1.0f);
 
        // draw a quad textured to match the sprite
        glBegin(GL_QUADS);
        {
            glTexCoord2f(topLeft.x, topLeft.y);
            glVertex2f(topLeft.x, topLeft.y);
 
            glTexCoord2f(topLeft.x, botRight.y);
            glVertex2f(topLeft.x, botRight.y);
 
            glTexCoord2f(botRight.x, botRight.y);
            glVertex2f(botRight.x, botRight.y);
 
            glTexCoord2f(botRight.x, topLeft.y);
            glVertex2f(botRight.x, topLeft.y);           
        }
        glEnd();
 
        // restore the model view matrix to prevent contamination
        glPopMatrix();
	}
	
	public void setClippingBounds(Vector2 topLeftCorner, Vector2 bottomRightCorner){
		topLeft = topLeftCorner;
		botRight = bottomRightCorner;
	}

	
	@Override
	public void update(float deltaTime) {
		// stub
	}
}
