package game;

import static org.lwjgl.opengl.GL11.GL_CLAMP;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import graphics.Sprite2D;

import org.jbox2d.collision.AABB;
import org.jbox2d.dynamics.BodyType;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import physics.PhysicsBody;
import physics.PhysicsWorld;
import utils.Paths;
import utils.Vector2;
import audio.AudioManager;
import controls.ControllerManager;

public class Main {
	private static final int TARGET_FPS = 60;
	//private static final long SLEEP_DELTA = 1000 / TARGET_FPS + 1; // Target sleep time between frames
	//private static final long SLEEP_MIN = 5; // Absolute minimum sleep time between frames (if too slow render)

	private int frameHeight = 640;
	private int frameWidth = 720;
	private long deltaTime;
	private Game game;
	private long t0, t1; // Frame start/end time
	
	private void destroy() {
		game.destroy();
		AudioManager.destroy();

		Display.destroy();
	}

	private void init() {
		try {
			Display.setTitle("Bomberman. The real deal.");
			Display.setResizable(false);
			Display.setDisplayMode(new DisplayMode(frameWidth, frameHeight));
			Display.create();
		}
		catch (Exception e){
			e.printStackTrace();
		}

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);

		// Create and initialize game
		game = new Game();
		game.init();
		
		//AudioManager.playMusic("menu.ogg");
		//AudioManager.playSound(SoundType.BOMB_EXPLODE);
	}

	private void loop() {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glViewport(0, 0, frameWidth, frameHeight);
		GL11.glEnable(GL11.GL_BLEND);
	    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
   
		while (!Display.isCloseRequested()) {
		    t0 = System.currentTimeMillis(); 
			deltaTime = t0 - t1;
			t1 = t0;
			
			// Poll controllers for input
			ControllerManager.getInstance().pollControllers();

			// Update game logic
			game.update(deltaTime * 0.001f);

			// Prepare for rendering
			GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			GL11.glOrtho(0, frameWidth, 0, frameHeight, 0, 20);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			GL11.glTranslatef(-1.0f, -1.0f, 0.0f);
			//GL11.glScalef(1.0f, -1.0f, 1.0f);

			// Render game and swap buffers
			game.render();
			
			Display.update();
			Display.sync(TARGET_FPS);
		}
	}

	public void run() {
		init();
		loop();
		destroy();
	}

	public static void main(String[] args) {
		new Main().run();
	}
}
