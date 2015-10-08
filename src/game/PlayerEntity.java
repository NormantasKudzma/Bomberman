package game;


import game.Main.K1;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import controls.AbstractController;
import controls.ControllerEventListener;
import controls.ControllerKeybind;
import controls.ControllerManager;
import controls.EController;
import utils.Paths;
import utils.Vector2;

public class PlayerEntity extends Entity {
	private Vector2 moveDirection;
	private PlayerEntity p;
	private AbstractController keyboard;
	private java.lang.reflect.Method method, metodas;
	private long s;

	@Override
	public void update(float deltaTime){
		if(moveDirection != null){
		   getPosition().add(moveDirection);
		   System.out.println("kas kas");
		   moveDirection.set(0, 0);
		}
	}
	public void readKeybindings(){
		keyboard = ControllerManager.getInstance()
			    .getController(EController.LWJGLKEYBOARDCONTROLLER);
		keyboard.startController();
		p = new PlayerEntity();
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
	        		  method = p.getClass().getMethod(s1);
	        		} catch (SecurityException e) {
	        		  // ...
	        		} catch (NoSuchMethodException e) {
	        		  // ...
	        		}
	        	}	
	        	counter += 1;
	        }
	        System.out.println("kas kas1");
	        ControllerKeybind keybind = new ControllerKeybind(s, new K1(){
    			  java.lang.reflect.Method metodas = method;});
	    }
	}
	public void moveUp(){
		moveDirection.setY(0.0002f);
		System.out.println("kas kas2");
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

		@Override
		public void handleEvent(long mask) {
			System.out.println("kas kas4");
			try {
				metodas.invoke(p, mask);
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
