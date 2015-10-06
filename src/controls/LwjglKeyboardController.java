package controls;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

public class LwjglKeyboardController extends AbstractController{
	private GLFWKeyCallback glfwCallback;	
	private long windowHandle;
	
	public LwjglKeyboardController(){
		glfwCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long windowHandle, int key, int scancode, int action, int mods) {
				if (defaultCallback != null){
					defaultCallback.getCallback().handleEvent(key);
				}
				
				if (oneClickCallback != null){
					oneClickCallback.getCallback().handleEvent(key);
				}
				
				for (ControllerKeybind bind : keyBindings){
					if ((bind.getBitmask() & key) != 0){
						bind.getCallback().handleEvent(key);
					}
				}
			}
		};
	}
	
	@Override
	protected void destroyController() {
		glfwCallback.release();
	}
	
	public void pollController() {
		GLFW.glfwPollEvents();
	}
	
	@Override
	public void startController(long window) {
		if (window != 0){
			windowHandle = window;
			GLFW.glfwSetKeyCallback(window, glfwCallback);
		}
		else {
			System.err.println("LWJGLKEYBOARDCONTROLLER::START_CONTROLLER, windowhandle must not be 0");
		}
	}
}
