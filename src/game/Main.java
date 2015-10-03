package game;

import static org.lwjgl.opengl.GL11.GL_CLAMP;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import graphics.Sprite2D;

import java.nio.ByteBuffer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import utils.Vector2;
import controls.AbstractController;
import controls.ControllerEventListener;
import controls.ControllerManager;
import controls.EController;

public class Main {
	private static final int TARGET_FPS = 60;
	private static final long SLEEP_DELTA = 1000 / TARGET_FPS;	// Maximum sleep time between frames
	private static final long SLEEP_MIN = 5;					// Absolute minimum sleep time between frames (if too slow render)
	
	private int frameHeight = 720;
	private int frameWidth = 1280;
	private long deltaTime;
	private Game game;	
	private long t0, t1;				// Frame start (t0) and frame end (t1) time
	private long windowHandle;
	private Sprite2D sprite;
	
	class K1 implements ControllerEventListener {

		@Override
		public void handleEvent(long eventArg) {
			GLFW.glfwSetWindowShouldClose(windowHandle, GL11.GL_TRUE);
		}
		
	}

	private void destroy(){
		game.destroy();
        
        // Release window and window callbacks
        GLFW.glfwDestroyWindow(windowHandle);
	}
	
	private void init() { 
        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (GLFW.glfwInit() != GL11.GL_TRUE){
            throw new IllegalStateException("Unable to initialize GLFW");
        }
 
        // Configure our window
        GLFW.glfwDefaultWindowHints(); // optional, the current window hints are already the default
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL11.GL_FALSE); // the window will stay hidden after creation
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL11.GL_FALSE); // the window will be resizable
 
        // Create the window
        windowHandle = GLFW.glfwCreateWindow(frameWidth, frameHeight, "Hello World!", 0, 0);
        if (windowHandle == 0){
            throw new RuntimeException("Failed to create the GLFW window");
        }

        ByteBuffer vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        // Center our window
        GLFW.glfwSetWindowPos(windowHandle, (GLFWvidmode.width(vidmode) - frameWidth) / 2, (GLFWvidmode.height(vidmode) - frameHeight) / 2);
        GLFW.glfwMakeContextCurrent(windowHandle);
        GLFW.glfwSwapInterval(1);
        GLContext.createFromCurrent();
        GLFW.glfwShowWindow(windowHandle);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
        
        // Create and initialize game
        game = new Game();
        game.init();
        
        AbstractController keyboard = ControllerManager.getInstance().getController(EController.LWJGLKEYBOARDCONTROLLER);
        keyboard.addKeybind(GLFW.GLFW_KEY_ESCAPE, new K1());
        keyboard.startController(windowHandle);
        
        AbstractController nintendo = ControllerManager.getInstance().getController(EController.USBCONTROLLER);
        if (nintendo != null){
	        nintendo.startController();
	        nintendo.addKeybind(0xffffffff & ~nintendo.getDefaultBitmaskValue(), new K1());
        }
        else {
        	System.out.println("Nintendo controller is null");
        }
        
        sprite = new Sprite2D("smetona.jpg");
	}
	
	private void loop() {
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
    	GL11.glViewport(0, 0, frameWidth, frameHeight);
        
        while (GLFW.glfwWindowShouldClose(windowHandle) == GL11.GL_FALSE) {
        	t0 = System.currentTimeMillis();
        	
        	// Poll controllers for input
        	ControllerManager.getInstance().pollControllers();
        	
        	// Update game logic
        	game.update(deltaTime);
        	
        	// Prepare for rendering
            GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        	GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        	GL11.glOrtho(0, frameWidth, 0, frameHeight, 0, 20);
        	GL11.glMatrixMode(GL11.GL_MODELVIEW);
        	GL11.glLoadIdentity();
        	
        	// Render game and swap buffers
        	game.render();
        	GLFW.glfwSwapBuffers(windowHandle);
        	
        	// Calculate difference between frame start and frame end and set thread to sleep
        	t1 = System.currentTimeMillis();        	
        	deltaTime = t1 - t0;
        	deltaTime = Math.max(SLEEP_MIN, SLEEP_DELTA - deltaTime);
        	
        	try {
				Thread.sleep(deltaTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
	}
	
	private void run(){
		try {
            init();
            loop();
            destroy();
        } finally {
            // Terminate GLFW and release the GLFWerrorfun
            GLFW.glfwTerminate();
        }
	}

	public static void main(String [] args){
		new Main().run();
	}
}