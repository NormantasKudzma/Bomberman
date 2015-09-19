package graphics;

import java.io.IOException;

import utils.Vector2;

import static org.lwjgl.opengl.GL11.*;

public class Sprite2D implements IRenderable {
	private Vector2 position = new Vector2();
	private float rotation = 0.0f;
	private Vector2 scale = new Vector2();
	private Texture texture;	// Sprite's texture
	private int zOrder = 0;		// Render position, lower Z order will be rendered earlier 
								// (ie. Z1 will be rendered before Z20)

	public Sprite2D(){}
	
	public Sprite2D(String path){
		loadTexture(path);
	}
	
	public Vector2 getPosition(){
		return position;
	}
	
	public float getRotation(){
		return rotation;
	}
	
	public Vector2 getScale(){
		return scale;
	}
	
	public Texture getTexture(){
		return texture;
	}
	
	public int getZOrder(){
		return zOrder;
	}
	
	public void loadTexture(String path){
		try {
			texture = TextureLoader.getInstance().getTexture(path);
		}
		catch (IOException e){
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	@Override
	public void render() {
		// store the current model matrix
        glPushMatrix();
 
        // bind to the appropriate texture for this sprite
        texture.bind();
 
        // translate to the right location and prepare to draw
        glTranslatef(position.x(), position.y(), 0);
        
        //glRotatef(rotation, 0, 0, 1);
        
        //glScalef(scale.x(), scale.y(), 0);
 
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
	
	public void setPosition(Vector2 pos){
		position = pos;
	}
	
	public void setPositionX(float x){
		position.setX(x);
	}
	
	public void setPositionY(float y){
		position.setY(y);
	}
	
	public void setPosition(float x, float y){
		position.set(x, y);
	}
	
	public void setRotation(float rot){
		this.rotation = rot;
	}
	
	public void setScale(Vector2 s){
		scale = s;
	}
	
	public void setScaleX(float xs){
		scale.setX(xs);
	}
	
	public void setScaleY(float ys){
		scale.setY(ys);
	}
	
	public void setScale(float x, float y){
		scale.set(x, y);
	}
	
	public void setTexture(Texture tex){
		texture = tex;
	}
	
	public void setZOrder(int z){
		zOrder = z;
	}
}
