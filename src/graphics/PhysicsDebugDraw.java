package graphics;

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.FloatBuffer;

import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.collision.AABB;
import org.jbox2d.common.Color3f;
import org.jbox2d.common.IViewportTransform;
import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import physics.PhysicsBody;
import physics.PhysicsWorld;

import utils.Paths;
import utils.Vector2;

public class PhysicsDebugDraw {
	 public static void render(){
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor3f(1.0f, 0.25f, 0.1f);
		Vector2 localpos;
		AABB aabb;
		Vector2 ul, br;
		for (PhysicsBody b : PhysicsWorld.getInstance().getBodyList()){
			//localpos = b.getPosition();
			aabb = b.getBody().getFixtureList().getAABB(0);
			ul = Vector2.fromVec2(aabb.upperBound)/*.sub(localpos)*/;
			br = Vector2.fromVec2(aabb.lowerBound)/*.add(localpos)*/;
			GL11.glPushMatrix();
			//GL11.glTranslatef(localpos.x, localpos.y, 0);
			//GL11.glRectf(br.x, br.y, ul.x, ul.y);
			GL11.glLineWidth(2.0f);
			GL11.glBegin(GL11.GL_LINE_LOOP);
			GL11.glVertex2f(br.x, br.y);
			GL11.glVertex2f(br.x, ul.y);
			GL11.glVertex2f(ul.x, ul.y);
			GL11.glVertex2f(ul.x, br.y);
		    GL11.glEnd();
			GL11.glPopMatrix();
		}
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	 }
}
