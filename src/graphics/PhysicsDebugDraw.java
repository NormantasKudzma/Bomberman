package graphics;

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.FloatBuffer;

import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.common.Color3f;
import org.jbox2d.common.IViewportTransform;
import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import utils.Paths;

public class PhysicsDebugDraw extends DebugDraw{
	private static final float FILL_ALPHA = 0.4f;
	private static final float LINE_ALPHA = 0.7f;
	
	private static final String FONT_NAME = "DisposableDroidBB.ttf";
	
	private TrueTypeFont font;
	
	public PhysicsDebugDraw(IViewportTransform viewport) {
		super(viewport);
		
		try {
			InputStream is = new FileInputStream(new File(Paths.FONTS + FONT_NAME));
			font = new TrueTypeFont(Font.createFont(Font.TRUETYPE_FONT, is), true);
			is.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void drawCircle(Vec2 pos, float radius, Color3f c) {
		GL11.glPushMatrix();
	    GL11.glLineWidth(1);
	    GL11.glColor4f(0.25f, 0.75f, 0.75f, LINE_ALPHA);
		GL11.glTranslatef(pos.x, pos.y, 0);
		GL11.glScalef(radius, radius, 1);

		GL11.glBegin(GL11.GL_LINE_LOOP);
		GL11.glVertex2f(0, 0);
		for(int i = 0; i <= 8; i++){
		    float angle = MathUtils.PI * i * 0.25f;
		    GL11.glVertex2f(MathUtils.cos(angle), MathUtils.sin(angle));
		}
		GL11.glEnd();

		GL11.glPopMatrix();	
	}

	@Override
	public void drawPoint(Vec2 pos, float size, Color3f color) {
		drawSolidCircle(pos, size, null, color);
	}

	@Override
	public void drawSegment(Vec2 st, Vec2 end, Color3f color) {
		GL11.glPushMatrix();
		GL11.glLineWidth(2);
		GL11.glColor4f(color.x, color.y, color.z, LINE_ALPHA);

		GL11.glBegin(GL11.GL_LINE);
		GL11.glVertex2f(st.x, st.y);
		GL11.glVertex2f(end.x, end.y);
		GL11.glEnd();

		GL11.glPopMatrix();
	}

	@Override
	public void drawSolidCircle(Vec2 pos, float radius, Vec2 axis, Color3f color) {
		GL11.glPushMatrix();
		GL11.glTranslatef(pos.x, pos.y, 0);
		GL11.glScalef(radius, radius, 1);
		GL11.glColor4f(color.x, color.y, color.z, FILL_ALPHA);

		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		GL11.glVertex2f(0, 0);
		for(int i = 0; i <= 8; i++){
		    float angle = MathUtils.PI * i * 0.25f;
		    GL11.glVertex2f(MathUtils.cos(angle), MathUtils.sin(angle));
		}
		GL11.glEnd();
		
		GL11.glBegin(GL11.GL_LINE);
	    GL11.glVertex2f(pos.x, pos.y);
	    GL11.glVertex2f(pos.x + axis.x * radius, pos.y + axis.y * radius);
		GL11.glEnd();

		GL11.glPopMatrix();
	}

	@Override
	public void drawSolidPolygon(Vec2[] vertices, int vertexCount, Color3f color) {
		GL11.glPushMatrix();
		//set up vertex array
	    FloatBuffer glverts = BufferUtils.createFloatBuffer(16);
	    GL11.glVertexPointer(2, GL11.GL_FLOAT, glverts); //tell OpenGL where to find vertices
	    GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY); //use vertices in subsequent calls to glDrawArrays
	    
	    //fill in vertex positions as directed by Box2D
	    for (int i = 0; i < vertexCount; i++) {
	    	glverts = glverts.put(vertices[i].x);
	    	glverts = glverts.put(vertices[i].y);
	    }
	    
	    //draw solid area
	    GL11.glColor4f(color.x, color.y, color.z, FILL_ALPHA);
	    GL11.glDrawArrays(GL11.GL_TRIANGLE_FAN, 0, vertexCount);
	  
	    //draw lines
	    GL11.glLineWidth(1);
	    GL11.glColor4f(0, 0, 0, LINE_ALPHA); //purple
	    GL11.glDrawArrays(GL11.GL_LINE_LOOP, 0, vertexCount);
	    GL11.glPopMatrix();
	}

	@Override
	public void drawString(float x, float y, String str, Color3f c) {
		font.drawString(x, y, str, 2f, 2f);
	}

	@Override
	public void drawTransform(Transform xf) {
		// wtf?
		
		Vec2 temp = new Vec2();
		Vec2 temp2 = new Vec2();
	    getWorldToScreenToOut(xf.p, temp);
	    temp2.setZero();
	    float k_axisScale = 0.4f;

	    GL11.glBegin(GL11.GL_LINES);
	    GL11.glColor3f(1, 0, 0);

	    temp2.x = xf.p.x + k_axisScale * xf.q.c;
	    temp2.y = xf.p.y + k_axisScale * xf.q.s;
	    getWorldToScreenToOut(temp2, temp2);
	    GL11.glVertex2f(temp.x, temp.y);
	    GL11.glVertex2f(temp2.x, temp2.y);

	    GL11.glColor3f(0, 1, 0);
	    temp2.x = xf.p.x + -k_axisScale * xf.q.s;
	    temp2.y = xf.p.y + k_axisScale * xf.q.c;
	    getWorldToScreenToOut(temp2, temp2);
	    GL11.glVertex2f(temp.x, temp.y);
	    GL11.glVertex2f(temp2.x, temp2.y);
	    GL11.glEnd();
	}
}
