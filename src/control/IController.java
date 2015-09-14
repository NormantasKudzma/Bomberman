package control;

public interface IController {
	public void addKeybind(long bitmask, ControllerEventListener callback);
	public ControllerKeybind clearKeybind(long bitmask);
	public void clearKeybinds();
	public Iterable<ControllerKeybind> getAllKeybinds();
	public long getDefaultBitmaskValue();
	public ControllerKeybind removeUnmaskedCallback();
	public void setUnmaskedCallback(ControllerEventListener callback);
}
