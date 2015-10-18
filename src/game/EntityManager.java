package game;

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
		PLAYER;
	}
	public void createEntity(EntityType type, Vector2 pos, String spriteName, int playerIndex){
		switch(type){
			case PLAYER:{
				entityList.add(createPlayer(pos, spriteName, playerIndex));
			}
			case BOMB: {
				entityList.add(createBomb(pos, spriteName));
			}
			default:{
				// Invalid request,
			}
		}
	}
	public PlayerEntity createPlayer(Vector2 pos, String spriteName, int playerIndex){
		PlayerEntity player = new PlayerEntity();
		player.addSprite(Paths.TEXTURES + spriteName);
		player.setPosition(pos);
		player.readKeybindings(Paths.PLAYER_DATA + playerIndex + ".txt");
		return player;
		
	}
	public BombEntity createBomb(Vector2 pos, String spriteName){
		BombEntity bomb = new BombEntity();
		bomb.addSprite(Paths.TEXTURES + spriteName);
		bomb.setPosition(pos);
		return bomb;
	}
	public ArrayList<Entity> getEntityList(){
		return entityList;
	}
	public static EntityManager getInstance(){
		return INSTANCE;
	}
}
