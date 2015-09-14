package control;

public class ControllerKeybind {
	private long bitmask;
	private ControllerEventListener callback;
	
	public ControllerKeybind(long bitmask, ControllerEventListener callback){
		this.bitmask = bitmask;
		this.callback = callback;
	}
	
	public long getBitmask(){
		return bitmask;
	}
	
	public ControllerEventListener getCallback(){
		return callback;
	}
	
	public void setBitmask(long bitmask){
		this.bitmask = bitmask;
	}
	
	public void setCallback(ControllerEventListener callback){
		this.callback = callback;
	}
}
