package game;

import static org.lwjgl.opengl.GL11.GL_CLAMP;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import graphics.Button;
import graphics.TrueTypeFont;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import utils.ConfigManager;
import utils.Paths;
import utils.Vector2;
import audio.AudioManager;
import controls.ControllerEventListener;
import controls.ControllerManager;
import controls.EController;
import controls.LwjglMouseController;

public class Main {
	private static final int TARGET_FPS = 60;
	//private static final long SLEEP_DELTA = 1000 / TARGET_FPS + 1; // Target sleep time between frames
	//private static final long SLEEP_MIN = 5; // Absolute minimum sleep time between frames (if too slow render)

	private int frameHeight = 640;
	private int frameWidth = 720;
	private long deltaTime;
	private Game game;
	private long t0, t1; // Frame start/end time
	
	Button btn;
	TrueTypeFont ttf;
	
	private void destroy() {
		game.destroy();
		AudioManager.destroy();

		Display.destroy();
	}

	private void init(){
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
		
		btn = new Button();
		btn.setText("hi");
		btn.setPosition(1.5f, 1.5f);
		btn.setScale(new Vector2(6.0f, 2.0f));
		
		ttf = new TrueTypeFont(ConfigManager.loadFont(Paths.DEFAULT_FONT, 14), false);
		ttf.setText("HII");
		
		LwjglMouseController c = (LwjglMouseController) ControllerManager.getInstance().getController(EController.LWJGLMOUSECONTROLLER);
		c.addKeybind(0, new ControllerEventListener(){

			@Override
			public void handleEvent(long eventArg, Vector2 pos, int... params) {
				if (params[0] == 1){
					System.out.println("CLICK " + eventArg + "\tx" + pos.x + "\ty" + pos.y + "\tstate " + params[0]);
					System.out.println(btn.onClick(pos));
				}
			}			
		});
		c.setMouseMoveListener(new ControllerEventListener(){
			@Override
			public void handleEvent(long eventArg, Vector2 pos, int... params) {
				btn.onHover(pos);
			}
		});
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
			btn.render();
			//ttf.render(Vector2.one, 0, Vector2.one);
			
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
