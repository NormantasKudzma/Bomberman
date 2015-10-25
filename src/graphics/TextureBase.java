package graphics;


public abstract class TextureBase {
	/** The height of the image */
	protected int height;

	/** The width of the image */
	protected int width;

	/** The width of the texture */
	protected int texWidth;

	/** The height of the texture */
	protected int texHeight;

	/** The ratio of the width of the image to the texture */
	protected float widthRatio;

	/** The ratio of the height of the image to the texture */
	protected float heightRatio;

	public abstract void bind();

	public void setHeight(int height) {
		this.height = height;
		setHeight();
	}

	public void setWidth(int width) {
		this.width = width;
		setWidth();
	}

	public int getImageHeight() {
		return height;
	}

	public int getImageWidth() {
		return width;
	}

	public float getHeight() {
		return heightRatio;
	}

	public float getWidth() {
		return widthRatio;
	}

	public void setTextureHeight(int texHeight) {
		this.texHeight = texHeight;
		setHeight();
	}

	public void setTextureWidth(int texWidth) {
		this.texWidth = texWidth;
		setWidth();
	}

	private void setHeight() {
		if (texHeight != 0) {
			heightRatio = ((float) height) / texHeight;
		}
	}

	private void setWidth() {
		if (texWidth != 0) {
			widthRatio = ((float) width) / texWidth;
		}	
	}
}
