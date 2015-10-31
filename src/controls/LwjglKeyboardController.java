package controls;

import org.lwjgl.input.Keyboard;

public class LwjglKeyboardController extends AbstractController{	
	public LwjglKeyboardController(){
		
	}
	
	@Override
	protected void destroyController() {
		
	}
	
	public void pollController() {
		int key = Keyboard.getEventKey();
		
		if (defaultCallback != null){
			defaultCallback.getCallback().handleEvent(key);
		}
		
		if (oneClickCallback != null){
			oneClickCallback.getCallback().handleEvent(key);
			oneClickCallback = null;
		}
		
		for (ControllerKeybind bind : keyBindings){
			if (Keyboard.isKeyDown(bind.getIntmask()))
			{
				bind.getCallback().handleEvent(bind.getBitmask());					
			}
		}
	}
	
	@Override
	public void startController() {
		//GLFW.glfwSetKeyCallback(windowHandle, glfwCallback);
	}
}
