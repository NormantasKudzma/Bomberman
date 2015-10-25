package web;

import graphics.TextureBase;

import com.googlecode.gwtgl.binding.WebGLRenderingContext;
import com.googlecode.gwtgl.binding.WebGLTexture;

public class WebTexture extends TextureBase {
	// Wrapped texture
	public WebGLTexture tex;

	public WebTexture(){}	
	
	public void bind() {
		WebRenderer.get().bindTexture(WebGLRenderingContext.TEXTURE_2D, tex);
	}
}
