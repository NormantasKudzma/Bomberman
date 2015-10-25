package utils;

public class Globals {
	public enum Platform {
		WEBAPP(""),
		NATIVE(""),
		OTHER("");
		
		private String path;
		
		private Platform(String p){
			path = p;
		}
		
		public String getPath(){
			return path;
		}
		
		public static Platform fromString(String name){
			for (Platform i : values()){
				if (i.toString().equalsIgnoreCase(name)){
					return i;
				}
			}
			return OTHER;
		}
	}
	
	public static final Platform PLATFORM = Platform.fromString(System.getProperty("platform"));
	public static final boolean WEB = (PLATFORM == Platform.WEBAPP ? true : false);
}
