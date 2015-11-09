package graphics;

import utils.Vector2;

public interface IClickable {
	public boolean isClicked(Vector2 clickPos);
	public void onClick();
}
