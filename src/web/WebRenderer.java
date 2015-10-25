package web;

import com.googlecode.gwtgl.binding.WebGLRenderingContext;

public class WebRenderer {
	private static WebGLRenderingContext glContext;
	
	public static WebGLRenderingContext get(){
		return glContext;
	}
	
	public static void set(WebGLRenderingContext ctx){
		glContext = ctx;
	}
}
