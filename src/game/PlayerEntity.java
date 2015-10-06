package game;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import utils.Vector2;

public class PlayerEntity extends Entity {
	Vector2 moveDirection;
	
	@Override
	public void update(float deltaTime){
		getPosition().add(moveDirection);
		moveDirection = null;
	}
	public void readKeybindings(){
		// The name of the file to open.
        String fileName = "DefaultKeybinds";

        // This will reference one line at a time
        String line = null;

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }   

            // Always close files.
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");                  
            // Or we could just do this: 
            // ex.printStackTrace();
        }
	}
	public void moveUp(){
		moveDirection.setY(0.0002f);
	}
	public void moveDown(){
		moveDirection.setY(-0.0002f);
	}
	public void moveLeft(){
		moveDirection.setX(-0.0002f);	
	}
	public void moveRight(){
		moveDirection.setX(0.0002f);
	}
	

}
