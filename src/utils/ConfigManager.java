package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class ConfigManager {
	public class Config<K, V>{
		public Pair<Object, Object> firstLine;
		public ArrayList<Pair<K, V>> contents = new ArrayList<Pair<K, V>>();
	}
	
	private static final ConfigManager INSTANCE = new ConfigManager();
	
	private String commentDelim = "#";
	private String kvDelim = "=";
	
	private ConfigManager(){
		
	}
	
	public static ConfigManager getInstance(){
		return INSTANCE;
	}
	
	public Config<String, String> loadConfigAsPairs(String path){
		return loadConfigAsPairs(path, false);
	}
	
	public Config<String, String> loadConfigAsPairs(String path, boolean isHeaderUnique){
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			Config<String, String> cfg = new Config<String, String>();
			String line;
			String [] params;
			
			while ((line = br.readLine()) != null){
				if (line.isEmpty() || line.startsWith(commentDelim)){
					continue;
				}
				
				params = line.split(kvDelim);
				// Discard mangled lines
				if (params.length != 2){
					continue;
				}
				
				if (isHeaderUnique){
					isHeaderUnique = false;
					cfg.firstLine = new Pair<Object, Object>(params[0], params[1]);
					continue;
				}
				
				cfg.contents.add(new Pair<String, String>(params[0], params[1]));
			}
			br.close();
			return cfg;
		}
		catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
