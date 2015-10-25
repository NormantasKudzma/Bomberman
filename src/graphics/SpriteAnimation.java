package graphics;

import org.json.JSONObject;

import utils.ConfigManager;
import utils.Paths;
import utils.Vector2;

public class SpriteAnimation implements IRenderable{
	public enum Direction{
		UP(1),
		RIGHT(0),
		DOWN(3),
		LEFT(2);
		
		private final int i;
		
		private Direction(int index){
			i = index;
		}
		
		public int getIndex(){
			return i;
		}
		
		public static Direction fromInt(int index){
			return Direction.values()[index];
		}
	}
	
	Sprite2D spriteArray[][];
	int currentFrame = 0;
	int currentState = 2;
	float frameDelay = 0.15f;
	boolean isRunning = true;
	int numFrames = 1;
	float timePassed = 0.0f;
	
	public SpriteAnimation(String path){
		loadSpriteSheet(path);
	}
	
	private void loadSpriteSheet(String path){
		JSONObject obj = ConfigManager.loadConfigAsJson(Paths.ANIMATIONS + path);
		Vector2 sheetSize = new Vector2(obj.getInt("width"), obj.getInt("height"));
		int numStates = obj.getInt("numstates");
		Vector2 spriteSize = new Vector2(obj.getInt("sprw"), obj.getInt("sprh"));
		Sprite2D sheet = new Sprite2D(Paths.ANIMATIONS + obj.getString("filename"));
		Vector2 sheetSizeCoef = new Vector2(sheet.getTexture().getWidth(), sheet.getTexture().getHeight());
		spriteArray = new Sprite2D[numStates][];
		numFrames = obj.getInt("numsprites");
		
		JSONObject state, coords;
		Vector2 topLeft, botRight;
		for (int i = 0; i < Direction.values().length; i++){
			state = obj.getJSONObject(Direction.fromInt(i).toString());
			if (state == null){
				continue;
			}
			spriteArray[i] = new Sprite2D[numFrames];
			for (int j = 0; j < numFrames; j++){
				coords = state.getJSONObject("" + j);
				topLeft = new Vector2(coords.getInt("x"), coords.getInt("y"));
				botRight = Vector2.add(spriteSize, topLeft);

				topLeft.mul(sheetSizeCoef);
				botRight.mul(sheetSizeCoef);
				spriteArray[i][j] = new Sprite2D(sheet.getTexture(), 
												new Vector2(topLeft.x / sheetSize.x, topLeft.y / sheetSize.y),
												new Vector2(botRight.x / sheetSize.x, botRight.y / sheetSize.y));
			}
		}
	}
	
	public void setDirection(Direction dir){
		currentState = dir.getIndex();
		currentFrame = 0;
	}
	
	public void setDirection(Vector2 dir){
		int q = (int)(dir.angle() + 0.5f) / 90;
		setDirection(Direction.fromInt(q));
	}
	
	public void setFrameDelay(float delay){
		if (delay > 0.0f){
			frameDelay = delay;
		}
	}

	@Override
	public void render(Vector2 position, float rotation, Vector2 scale) {
		spriteArray[currentState][currentFrame].render(position, rotation, scale);
	}
	
	public void setPaused(boolean isPaused){
		isRunning = isPaused;
		currentFrame = 0;
	}
	
	public void update(float deltaTime){
		if (!isRunning){
			return;
		}
		
		timePassed += deltaTime;
		if (timePassed >= frameDelay){
			timePassed = 0.0f;
			currentFrame = (currentFrame + 1) % numFrames;
		}
	}
}
