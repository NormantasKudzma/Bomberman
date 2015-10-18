package game;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import physics.PhysicsWorld;

import utils.ConfigManager;
import utils.ConfigManager.Config;
import utils.Pair;
import utils.Paths;
import utils.Vector2;
import controls.AbstractController;
import controls.ControllerEventListener;
import controls.ControllerManager;
import controls.EController;

public class PlayerEntity extends Entity {
	private float moveSpeed = 0.005f;
	private Vector2 moveDirection = new Vector2();
	private AbstractController keyboard;
	private EntityManager entityManager = EntityManager.getInstance();
	public PlayerEntity(){
		super();
	}
	@Override
	public void update(float deltaTime) {
		getPosition().add(moveDirection);
		moveDirection.set(0, 0);
	}

	public void readKeybindings(String path) {
		Config<String, String> readPairs = ConfigManager.getInstance().loadConfigAsPairs(Paths.DEFAULT_KEYBINDS, true);
		keyboard = ControllerManager.getInstance().getController(
				EController.getFromString(readPairs.firstLine.value));
		for(Pair<String, String> i : readPairs.contents){
			try {
				keyboard.addKeybind(Long.parseLong(i.key), new K1(getClass().getMethod(i.value), this));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		keyboard.startController();
	}

	public void moveUp() {
		moveDirection.setY(moveSpeed);
	}

	public void moveDown() {
		moveDirection.setY(-moveSpeed);
	}

	public void moveLeft() {
		moveDirection.setX(-moveSpeed);
	}

	public void moveRight() {
		moveDirection.setX(moveSpeed);
	}
	public void plantBomb(){
		entityManager.createEntity(EntityManager.EntityType.BOMB, this.getPosition(), "smetona.jpg", 1);
	}
	
	@Override
	protected void initEntity(){
		super.initEntity();
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
