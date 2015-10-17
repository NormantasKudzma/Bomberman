package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;

import org.json.JSONObject;

public class ConfigManager {	
	private static String commentDelim = "#";
	private static String kvDelim = "=";
	
	public static Config<String, String> loadConfigAsPairs(String path){
		return loadConfigAsPairs(path, false);
	}
	
	public static Config<String, String> loadConfigAsPairs(String path, boolean isHeaderUnique){
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

	public static JSONObject loadConfigAsJson(String path){
		try {
			byte buffer[] = Files.readAllBytes(java.nio.file.Paths.get(path));
			String contents = new String(buffer);
			JSONObject obj = new JSONObject(contents);
			return obj;
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
