package graphics;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import utils.Vector2;

import static org.lwjgl.opengl.GL11.*;

public class Sprite2D implements IRenderable {
	private static final float DEFAULT_SPRITE_SIZE = 0.0625f;
	
	private Texture texture;	// Sprite's texture
	private Vector2 internalScale = new Vector2(1.0f, 1.0f);	// Sprite size in game units (0;0)->(2;2)

	public Sprite2D(){}
	
	public Sprite2D(String path){
		loadTexture(path);
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
        glTranslatef(position.x() - 1.0f, -position.y() + 1.0f, 0);        
        glScalef(scale.x(), scale.y(), 1.0f);
        glScalef(internalScale.x(), internalScale.y(), 1.0f);
        glRotatef(rotation, 0, 0, 1.0f);
 
        // draw a quad textured to match the sprite
        glBegin(GL_QUADS);
        {
            glTexCoord2f(0, 0);
            glVertex2f(0, 0);
 
            glTexCoord2f(0, texture.getHeight());
            glVertex2f(0, texture.getHeight());
 
            glTexCoord2f(texture.getWidth(), texture.getHeight());
            glVertex2f(texture.getWidth(), texture.getHeight());
 
            glTexCoord2f(texture.getWidth(), 0);
            glVertex2f(texture.getWidth(), 0);           
        }
        glEnd();
 
        // restore the model view matrix to prevent contamination
        glPopMatrix();
	}
}
