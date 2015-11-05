package game;

import graphics.SpriteAnimation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import utils.Paths;
import utils.Vector2;
import controls.AbstractController;
import controls.ControllerEventListener;
import controls.ControllerKeybind;
import controls.ControllerManager;
import controls.EController;

public class PlayerEntity extends Entity<SpriteAnimation> {
	private float moveSpeed = 0.5f;
	private Vector2 moveDirection = new Vector2();
	private AbstractController keyboard;

	@Override
	protected void initEntity() {
		super.initEntity();
		Vector2 fullScale = sprite.getHalfSize().copy().mul(getScale()).mul(2.0f);
		body.attachBoxCollider(fullScale, new Vector2(0, 0), 0);
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		applyForce(moveDirection);
		moveDirection.reset();
	}

	public void readKeybindings() {
		keyboard = ControllerManager.getInstance().getController(
				EController.LWJGLKEYBOARDCONTROLLER);
		try {
			BufferedReader br = new BufferedReader(new FileReader(Paths.CONFIGS + "DefaultKeybinds"));
			String line;
			line = br.readLine(); 	// HACK, skip first line
			while ((line = br.readLine()) != null){
				if (line.isEmpty() || line.startsWith("#")){
					continue;
				}
				
				String [] params = line.split("=");
				if (params.length != 2){
					continue;
				}
				
				long bitmask = Long.parseLong(params[0]);
				Method method = this.getClass().getMethod(params[1]);

				keyboard.addKeybind(new ControllerKeybind(bitmask, new K1(method, this)));
			}
			br.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}
		keyboard.startController();
	}

	public void moveUp() {
		moveDirection.y = moveSpeed;
	}

	public void moveDown() {
		moveDirection.y = -moveSpeed;
	}

	public void moveLeft() {
		moveDirection.x = -moveSpeed;
	}

	public void moveRight() {
		moveDirection.x = moveSpeed;
	}

	class K1 implements ControllerEventListener {
		java.lang.reflect.Method metodas;
		PlayerEntity player;

		public K1(java.lang.reflect.Method m, PlayerEntity p) {
			player = p;
			metodas = m;
		}

		@Override
		public void handleEvent(long mask) {
			try {
				metodas.invoke(player);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

}
