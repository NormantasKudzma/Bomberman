package game;

import graphics.Sprite2D;
import graphics.SpriteAnimation;
import graphics.SpriteAnimation.Direction;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import utils.Config;
import utils.ConfigManager;
import utils.Pair;
import utils.Paths;
import utils.Vector2;
import controls.AbstractController;
import controls.ControllerEventListener;
import controls.ControllerKeybind;
import controls.ControllerManager;
import controls.EController;

public class PlayerEntity extends Entity<SpriteAnimation> {
	private float moveSpeed = 0.005f;
	private Vector2 moveDirection = new Vector2();
	private AbstractController keyboard;
	private SpriteAnimation sprite;
	private Direction dir;
	private EntityManager entityManager = EntityManager.getInstance();
	private int Index;

	public PlayerEntity(String animationName, int playerIndex) {
		super();
		sprite = new SpriteAnimation(animationName);
		this.setSprite(sprite);
		Index = playerIndex;
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		getPosition().add(moveDirection);
		moveDirection.set(0, 0);
	}

	public void readKeybindings(String path) {
		Config<String, String> readPairs = ConfigManager.loadConfigAsPairs(
				Paths.DEFAULT_KEYBINDS, true);
		keyboard = ControllerManager.getInstance().getController(
				EController.getFromString((String) readPairs.firstLine.value));
		for (Pair<String, String> i : readPairs.contents) {
			try {
				keyboard.addKeybind(Long.parseLong(i.key), new K1(getClass()
						.getMethod(i.value), this));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		keyboard.startController();
	}

	public void moveUp() {
		moveDirection.setY(moveSpeed);
		sprite.setDirection(dir.UP);
	}

	public void moveDown() {
		moveDirection.setY(-moveSpeed);
		sprite.setDirection(dir.DOWN);
	}

	public void moveLeft() {
		moveDirection.setX(-moveSpeed);
		sprite.setDirection(dir.LEFT);
	}

	public void moveRight() {
		moveDirection.setX(moveSpeed);
		sprite.setDirection(dir.RIGHT);
	}

	public void plantBomb() {
		entityManager.createEntity(EntityManager.EntityType.BOMB, this
				.getPosition().copy(), "bomb.jpg", this.Index);
	}

	@Override
	protected void initEntity() {
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
