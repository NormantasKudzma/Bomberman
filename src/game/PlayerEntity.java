package game;

import graphics.SpriteAnimation;
import utils.Config;
import utils.ConfigManager;
import utils.Pair;
import utils.Paths;
import utils.Vector2;
import controls.AbstractController;
import controls.ControllerEventListener;
import controls.ControllerManager;
import controls.EController;

public class PlayerEntity extends Entity<SpriteAnimation> {
	private float moveSpeed = 0.5f;
	private Vector2 moveDirection = new Vector2();
	private AbstractController keyboard;
	private int bombLimit = 1;
	private int availableBombs = bombLimit;
	private Game game;
	
	public PlayerEntity(Game game){
		this.game = game;
	}

	@Override
	public void initEntity() {
		super.initEntity();
		getScale().mul(0.45f, 0.82f);
		Vector2 fullScale = sprite.getHalfSize().copy().mul(getScale()).mul(2.0f).mul(0.9f);
		body.attachBoxCollider(fullScale, new Vector2(0, 0), 0);
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		applyForce(moveDirection);
		moveDirection.reset();
	}

	public void readKeybindings() {
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

	public void placeBomb(){
		if (availableBombs <= 0){
			return;
		}
		--availableBombs;
		BombEntity bomb = new BombEntity(this);
		bomb.initEntity();
		bomb.setPosition(getPosition().copy());
		game.addEntity(bomb);
	}
	
	public void freeBomb(){
		if (availableBombs < bombLimit){
			++availableBombs;
		}
	}
	
	class K1 implements ControllerEventListener {
		java.lang.reflect.Method metodas;
		PlayerEntity player;

		public K1(java.lang.reflect.Method m, PlayerEntity p) {
			player = p;
			metodas = m;
		}

		@Override
		public void handleEvent(long mask, Vector2 pos, int... params) {
			try {
				metodas.invoke(player);
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
