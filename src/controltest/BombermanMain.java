package controltest;

import java.nio.ByteBuffer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import control.AbstractController;
import control.ControllerEventListener;
import control.ControllerManager;
import control.EController;

public class BombermanMain{
	private long windowHandle;	
	
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
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL11.GL_TRUE); // the window will be resizable
 
        int WIDTH = 300;
        int HEIGHT = 300;
 
        // Create the window
        windowHandle = GLFW.glfwCreateWindow(WIDTH, HEIGHT, "Hello World!", 0, 0);
        if (windowHandle == 0){
            throw new RuntimeException("Failed to create the GLFW window");
        }
 
        // Get the resolution of the primary monitor
        ByteBuffer vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        // Center our window
        GLFW.glfwSetWindowPos(windowHandle, (GLFWvidmode.width(vidmode) - WIDTH) / 2, (GLFWvidmode.height(vidmode) - HEIGHT) / 2);
 
        // Make the OpenGL context current
        GLFW.glfwMakeContextCurrent(windowHandle);
        // Enable v-sync
        GLFW.glfwSwapInterval(1);
 
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
	}
	
	private void loop() {
		// This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the ContextCapabilities instance and makes the OpenGL
        // bindings available for use.
        //GL.createCapabilities(false); // valid for latest build
        GLContext.createFromCurrent(); // use this line instead with the 3.0.0a build
 
        // Set the clear color
        GL11.glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
 
        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while (GLFW.glfwWindowShouldClose(windowHandle) == GL11.GL_FALSE) {
        	GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); // clear the framebuffer
 
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
