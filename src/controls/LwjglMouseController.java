package controls;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class LwjglMouseController extends AbstractController{
	/**
	 *  0 - not clicked
	 *  1 - on click
	 *  2+ - hold
	 */
	int buttonStates[];
	
	public LwjglMouseController(){
		buttonStates = new int[Mouse.getButtonCount()];
	}

	@Override
	public void pollController() {
		int x = Mouse.getX();
		int y = Mouse.getY();
		int intmask;
		
		for (ControllerKeybind bind : keyBindings){
			intmask = bind.getIntmask();
			if (Mouse.isButtonDown(intmask))
			{
				bind.getCallback().handleEvent(intmask, x, y, ++buttonStates[intmask]);					
			}
			else {
				buttonStates[intmask] = 0;
			}
		}
	}

	@Override
	public void startController() {
		//stub
	}

	@Override
	protected void destroyController() {
		//stub
	}
}
