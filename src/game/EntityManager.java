package game;

import graphics.Sprite2D;
import graphics.SpriteAnimation;

import java.util.ArrayList;

import utils.Paths;
import utils.Vector2;

public class EntityManager {
	private ArrayList<Entity> entityList = new ArrayList<Entity>();
	private static final EntityManager INSTANCE = new EntityManager();
	public EntityManager() {
		
	}
	enum EntityType {
		BOMB,
		INVALID,
		WALL,
		PLAYER;
	}
	public ArrayList<Entity> getEntityList(){
		return entityList;
	}
	public static EntityManager getInstance(){
		return INSTANCE;
	}
	public void createEntity(EntityType type, Vector2 pos, String spriteName, int playerIndex){
		switch(type){
			case PLAYER:{
				entityList.add(createPlayer(pos, spriteName, playerIndex));
				break;
			}
			case BOMB: {
				entityList.add(createBomb(pos, spriteName, playerIndex));
				break;
			}
			/*case WALL: {
				entityList.add(createWall(i, j, spriteName));
				break;
			}*/
			default:{
				// Invalid request,
				break;
			}
		}
	}
	
	public void createEntity(EntityType type, String spriteName,
			int i, int j){
		switch(type){
			case WALL: {
				entityList.add(createWall(i, j, spriteName));
				break;
			}
			default:{
				// Invalid request,
				break;
			}
		}
	}
	public PlayerEntity createPlayer(Vector2 pos, String animationName, int playerIndex){
		PlayerEntity player = new PlayerEntity(animationName, playerIndex);
		player.setPosition(pos);
		player.setScale(new Vector2(0.5f, 0.5f));
		player.readKeybindings(Paths.DEFAULT_KEYBINDS);
		return player;
		
	}
	public BombEntity createBomb(Vector2 pos, String spriteName, int playerIndex){
		BombEntity bomb = new BombEntity();
		//bomb.setSprite(new SpriteAnimation(spriteName));
		bomb.setSprite(new Sprite2D(Paths.TEXTURES + spriteName));
		bomb.setPosition(pos);
		return bomb;
	}
	private WallEntity createWall(int i, int j, String spriteName){
		WallEntity wall = new WallEntity();
		wall.setSprite(new Sprite2D(Paths.TEXTURES + spriteName));
		wall.setPosition(i * 0.06f, j * 0.06f);
		return wall;
	}
}
