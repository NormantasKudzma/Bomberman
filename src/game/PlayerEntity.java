package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

import controls.AbstractController;
import controls.ControllerEventListener;
import controls.ControllerKeybind;
import controls.ControllerManager;
import controls.EController;
import utils.Paths;
import utils.Vector2;

public class PlayerEntity extends Entity {
	private Vector2 moveDirection;
	private AbstractController keyboard;
	private java.lang.reflect.Method method;
	private long s;

	@Override
	public void update(float deltaTime){
		if(moveDirection != null){
		   getPosition().add(moveDirection);
		   moveDirection.set(0, 0);
		}
	}
	public void readKeybindings(){
		keyboard = ControllerManager.getInstance()
			    .getController(EController.LWJGLKEYBOARDCONTROLLER);
		Scanner sc2 = null;
	    try {
	        sc2 = new Scanner(new File(Paths.CONFIGS + "DefaultKeybinds"));
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();  
	    }
	    while (sc2.hasNextLine()) {
	            Scanner s2 = new Scanner(sc2.nextLine());
	            int counter = 1;
	        while (s2.hasNext()) {
	        	if(counter % 2 != 0 && counter == 1) {
	        	   s = s2.nextLong();
	        	   String m = s2.next();
	        	}   
	        	if(counter % 2 != 0 && counter == 3) {
	        		String s1 = s2.next();
	        		try {
	        		    method = this.getClass().getMethod(s1);
	        		} catch (SecurityException e) {
	        		  // ...
	        		} catch (NoSuchMethodException e) {
	        		  // ...
	        		}
	        	}	
	        	counter += 1;
	        }
	        keyboard.addKeybind(new ControllerKeybind(s, new K1(method, this)));
	    }
	    keyboard.startController();
	}
	public void moveUp(){
		moveDirection.setY(0.0002f);
	}
	public void moveDown(){
		moveDirection.setY(-0.0002f);
	}
	public void moveLeft(){
		moveDirection.setX(-0.0002f);	
	}
	public void moveRight(){
		moveDirection.setX(0.0002f);
	}
	class K1 implements ControllerEventListener {
		java.lang.reflect.Method metodas;
		PlayerEntity player;
		public K1(java.lang.reflect.Method m, PlayerEntity p){
			player = p;
			metodas = m;
		}
		@Override
		public void handleEvent(long mask) {
				try {
					metodas.invoke(player);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
              
	}

}
