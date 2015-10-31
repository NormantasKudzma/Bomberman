package game;

import static org.lwjgl.opengl.GL11.GL_CLAMP;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import graphics.SpriteAnimation;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import utils.Vector2;
import audio.AudioManager;
import controls.ControllerManager;

public class Main {
	private static final int TARGET_FPS = 60;
	//private static final long SLEEP_DELTA = 1000 / TARGET_FPS + 1; // Target sleep time between frames
	//private static final long SLEEP_MIN = 5; // Absolute minimum sleep time between frames (if too slow render)

	private int frameHeight = 720;
	private int frameWidth = 1280;
	private long deltaTime;
	private Game game;
	private long t0, t1; // Frame start (t0) and frame end (t1) time

	SpriteAnimation anim;
	
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
		
		anim = new SpriteAnimation("townfolk_m.json");
		AudioManager.playMusic("menu.ogg");
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
			anim.update(deltaTime * 0.001f);

			// Prepare for rendering
			GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glOrtho(0, frameWidth, 0, frameHeight, 0, 20);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();

			// Render game and swap buffers
			game.render();
			anim.render(new Vector2(0.25f, 0.25f), 0, new Vector2(0.5f, -0.5f));
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
