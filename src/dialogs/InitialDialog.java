package dialogs;

import graphics.BaseDialog;
import graphics.Button;
import graphics.ComponentContainer;
import graphics.Label;
import graphics.SpriteAnimation;

import java.util.Random;

import utils.Vector2;

public class InitialDialog extends BaseDialog{
	public static final String SKINS[] = new String[] { "healer_f.json", "mage_m.json", "ninja_f.json", "ranger_m.json", "townfolk_f.json", "warrior_m.json" };
	
	private Label title;
	private SpriteAnimation skins[];
	private Vector2 corners[];
	private Vector2 skinPos[];
	private Vector2 skinScale = new Vector2(1.0f, 1.0f);
	
	public InitialDialog() {
		super("InitialDialog");
	}

	@Override
	protected void initialize() {
		super.initialize();
		
		corners = new Vector2[]{new Vector2(0.75f, 1.25f), new Vector2(1.25f, 1.25f), new Vector2(0.75f, 0.75f), new Vector2(1.25f, 0.75f)};		
		skins = new SpriteAnimation[4];
		skinPos = new Vector2[4];
		
		for (int i = 0; i < skins.length; i++){
			skins[i] = new SpriteAnimation(SKINS[i]);
			skinPos[i] = new Vector2(corners[i].copy()).add(0.0f, 0.15f);
		}
		
		title = new Label("Bomberman v0.91");
		title.setScale(new Vector2(1.0f, 1.0f));
		title.setPosition(new Vector2(1.0f, 1.8f));
		addChild(title);
		
		for (int i = 0; i < corners.length; i++){
			final int ii = i;
			
			Button changeSkin = new Button(){
				int num = ii;
				int spriteNr = ii;
				
				public boolean onClick(Vector2 pos) {
					if (isMouseOver(pos)){
						spriteNr = (spriteNr + 1) % SKINS.length;
						skins[num] = new SpriteAnimation(SKINS[spriteNr]);
						return true;
					}
					return false;
				};
			};
			
			changeSkin.setText("New Player " + i);
			changeSkin.setScale(new Vector2(6.0f, 2.0f));
			changeSkin.setPosition(corners[i]);
			addChild(changeSkin);
		}
	}
	
	@Override
	public void render(Vector2 position, float rotation, Vector2 scale) {
		super.render(position, rotation, scale);
		for (int i = 0; i < skins.length; i++){
			skins[i].render(skinPos[i], rotation, skinScale);
		}
	}
}
