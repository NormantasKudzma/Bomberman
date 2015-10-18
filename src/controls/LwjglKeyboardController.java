package controls;

import org.lwjgl.LWJGLUtil;
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
					oneClickCallback = null;
				}
				
				for (ControllerKeybind bind : keyBindings){
					if (GLFW.glfwGetKey(windowHandle, bind.getIntmask()) != GLFW.GLFW_RELEASE)
					{
						bind.getCallback().handleEvent(bind.getBitmask());					
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
	
	public void setWindowHandle(long windowHandle){
		this.windowHandle = windowHandle;
	}
	
	@Override
	public void startController() {
		GLFW.glfwSetKeyCallback(windowHandle, glfwCallback);
	}
}
