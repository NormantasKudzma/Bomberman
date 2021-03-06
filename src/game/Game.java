package game;

import graphics.BaseDialog;
import graphics.IClickable;
import graphics.PhysicsDebugDraw;
import graphics.Sprite2D;
import graphics.SpriteAnimation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import physics.PhysicsWorld;
import utils.Paths;
import utils.Vector2;

public class Game implements IUpdatable, IClickable {
	private static final int NUM_VELOCITY_ITERATIONS = 2;
	private static final int NUM_POSITION_ITERATIONS = 4;

	private ArrayList<Integer> destroyList = new ArrayList<Integer>();
	private ArrayList<Entity> entityList = new ArrayList<Entity>();
	private ArrayList<PlayerEntity> playerList = new ArrayList<PlayerEntity>();
	private ArrayList<BaseDialog> dialogList = new ArrayList<BaseDialog>();
	private PhysicsWorld physicsWorld = PhysicsWorld.getInstance();
	private boolean isGameOver = false;

	public Game() {

	}

	public void addDialog(BaseDialog d){
		dialogList.add(d);
	}
	
	public void addEntity(Entity e){
		entityList.add(e);
		if (e instanceof PlayerEntity){
			playerList.add((PlayerEntity) e);
			isGameOver = false;
		}
	}
	
	/**
	 * Game destruction method. This method will be called last. Any resources
	 * that must be released, should be released here.
	 */
	public void destroy() {
		for (Entity i : entityList) {
			i.destroy();
		}
		entityList.clear();
	}

	public BaseDialog getDialog(String name){
		BaseDialog d;
		for (int i = 0; i < dialogList.size(); i++){
			if ((d = dialogList.get(i)).getName().equals(name)){
				return d;
			}
		}
		return null;
	}
	
	public ArrayList<Entity> getEntityList(){
		return entityList;
	}
	
	/**
	 * Game initialization (creating entities, loading map etc.) goes here
	 * 
	 */
	public void init() {
		initMap();

		PlayerEntity p = new PlayerEntity(this);
		p.readKeybindings();
		p.setSprite(new SpriteAnimation("ranger_f.json"));
		p.initEntity();
		p.setPosition(0.3f, 0.3f);
		entityList.add(p);
	}

	public void initMap() {
		final int mapSize = 33;
		char[][] map = new char[mapSize][mapSize];
		try {
			URL url = Thread.currentThread().getContextClassLoader().getResource(Paths.MAPS + "map01");
			System.out.println("Tryload " + url.toString());
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			int i = 0; // array i index
			int j = 0; // array j index

			int c = 0;
			try {
				while ((c = reader.read()) != -1) {
					char character = (char) c;
					if (character == 'W' || character == ' ') {
						map[i][j] = character;
						j++;
					}
					if (j == mapSize) {
						i++;
						j = 0;
					}
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		for (int i = 0; i < mapSize; i++) {
			for (int j = 0; j < mapSize; j++) {
				switch (map[i][j]) {
				case 'W':
					createWall(i, j);
					break;
				default:
					break;
				}

			}
		}
	}

	private void createWall(int i, int j) {
		WallEntity wall = new WallEntity();
		wall.setSprite(new Sprite2D(Paths.TEXTURES + "wall.jpg"));
		Vector2 scale = wall.getSprite().getInternalScale();
		wall.initEntity();
		wall.setPosition(i * scale.x + scale.x * 0.5f, j * scale.y + scale.y * 0.5f);
		entityList.add(wall);
	}

	/**
	 * Render method - call render for each and every entity
	 * 
	 */
	public void render() {
		// If there are visible dialogs, don't render the game
		BaseDialog d;
		for (int i = 0; i < dialogList.size(); i++){
			if ((d = dialogList.get(i)).isVisible()){
				d.render();
				return;
			}
		}
		
		for (int i = 0 ; i < entityList.size(); ++i){
			entityList.get(i).render();
		}
		
		PhysicsDebugDraw.render();
	}

	public void setDialogVisible(String name, boolean isVisible){
		BaseDialog d;
		for (int i = 0; i < dialogList.size(); i++){
			if ((d = dialogList.get(i)).getName().equals(name)){
				d.setVisible(isVisible);
				return;
			}
		}
	}
	
	/**
	 * Main game update method. Physics and entities should be moved during
	 * update.
	 * 
	 * @param deltaTime - time that has passed since last frame (in ms)
	 */
	public void update(float deltaTime) {
		// If there are visible dialogs, don't update the game
		BaseDialog d;
		for (int i = 0; i < dialogList.size(); i++){
			if ((d = dialogList.get(i)).isVisible()){
				d.update(deltaTime);
				return;
			}
		}
		
		// Update physics
		physicsWorld.getWorld().step(deltaTime, NUM_VELOCITY_ITERATIONS, NUM_POSITION_ITERATIONS);

		// Update all entities
		Entity e;
		for (int i = 0; i < entityList.size(); i++) {
			e = entityList.get(i);
			e.update(deltaTime);
			if (e.isDestroyed()) {
				destroyList.add(i);
			}
		}

		// Delete entities which are marked for destruction
		for (Integer i : destroyList) {
			entityList.get(i).destroy();
			entityList.remove((int)i);
		}
		destroyList.clear();
		
		if (playerList.isEmpty()){
			isGameOver = true;
		}
	}
	
	public boolean isGameOver(){
		return isGameOver;
	}
	
	@Override
	public boolean isMouseOver(Vector2 pos) {
		return true;
	}

	@Override
	public boolean onHover(Vector2 pos) {
		for (int i = 0; i < dialogList.size(); i++){
			if (dialogList.get(i).onHover(pos)){
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onClick(Vector2 pos) {
		for (int i = 0; i < dialogList.size(); i++){
			if (dialogList.get(i).onClick(pos)){
				return true;
			}
		}
		return false;
	}
}
