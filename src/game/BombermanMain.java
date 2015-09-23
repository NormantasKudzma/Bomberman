package game;

import graphics.Sprite2D;

import java.nio.ByteBuffer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import controls.AbstractController;
import controls.ControllerEventListener;
import controls.ControllerManager;
import controls.EController;

public class BombermanMain{
	private int frameHeight = 720;
	private int frameWidth = 1280;
	private long windowHandle;
	private Sprite2D sprite;
	
	class K1 implements ControllerEventListener {

		@Override
		public void handleEvent(long eventArg) {
			GLFW.glfwSetWindowShouldClose(windowHandle, GL11.GL_TRUE);
		}
		
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
 
        // Get the resolution of the primary monitor
        ByteBuffer vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        // Center our window
        GLFW.glfwSetWindowPos(windowHandle, (GLFWvidmode.width(vidmode) - frameWidth) / 2, (GLFWvidmode.height(vidmode) - frameHeight) / 2);
 
        // Make the OpenGL context current
        GLFW.glfwMakeContextCurrent(windowHandle);
        // Enable v-sync
        GLFW.glfwSwapInterval(1);

        GLContext.createFromCurrent();
        // Make the window visible
        GLFW.glfwShowWindow(windowHandle);
        
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
        
        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while (GLFW.glfwWindowShouldClose(windowHandle) == GL11.GL_FALSE) {
            GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        	GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        	GL11.glOrtho(0, frameWidth, 0, frameHeight, 0, 20);
        	GL11.glMatrixMode(GL11.GL_MODELVIEW);
        	GL11.glLoadIdentity();
        	
        	//sprite.render();
        	
        	GLFW.glfwSwapBuffers(windowHandle); // swap the color buffers
 
            // Poll for window events. The key callback above will only be
            // invoked during this call.
        	ControllerManager.getInstance().pollControllers();
        }
	}
	
	private void run(){
		try {
            init();
            loop();
 
            // Release window and window callbacks
            GLFW.glfwDestroyWindow(windowHandle);
        } finally {
            // Terminate GLFW and release the GLFWerrorfun
            GLFW.glfwTerminate();
        }
	}

	public static void main(String [] args){
		new BombermanMain().run();
	}
}
