package audio;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;

public class AudioManager {
	 /** Buffers hold sound data. */
	  IntBuffer buffer = BufferUtils.createIntBuffer(1);
	 
	  /** Sources are points emitting sound. */
	  private static IntBuffer source = BufferUtils.createIntBuffer(1);
	 
	  /** Position of the source sound. */
	  private static FloatBuffer sourcePos = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();
	 
	  /** Velocity of the source sound. */
	  private static FloatBuffer sourceVel = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();
	 
	  /** Position of the listener. */
	  private static FloatBuffer listenerPos = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();
	 
	  /** Velocity of the listener. */
	  private static FloatBuffer listenerVel = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();
	 
	  /** Orientation of the listener. (first 3 elements are "at", second 3 are "up") */
	  private static FloatBuffer listenerOri = (FloatBuffer)BufferUtils.createFloatBuffer(6).put(new float[] { 0.0f, 0.0f, -1.0f,  0.0f, 1.0f, 0.0f }).rewind();
	  
	  static {
		// Initialize OpenAL and clear the error bit.
	    try{
	      AL.create();
	    } catch (LWJGLException le) {
	      le.printStackTrace();
	      return;
	    }
	    AL10.alGetError();
	 
	    // Load the wav data.
	    if(loadALData() == AL10.AL_FALSE) {
	      System.out.println("Error loading data.");
	      return;
	    }
	 
	    setListenerValues();
	 
	    // Loop.
	    System.out.println("OpenAL Tutorial 1 - Single Static Source");
	    System.out.println("[Menu]");
	    System.out.println("p - Play the sample.");
	    System.out.println("s - Stop the sample.");
	    System.out.println("h - Pause the sample.");
	    System.out.println("q - Quit the program.");
	    char c = ' ';
	    Scanner stdin = new Scanner(System.in);
	    while(c != 'q') {
	      try {
	        System.out.print("Input: ");
	        c = (char)stdin.nextLine().charAt(0);
	      } catch (Exception ex) {
	        c = 'q';
	      }
	 
	      switch(c) {
	        // Pressing 'p' will begin playing the sample.
	        case 'p': AL10.alSourcePlay(source.get(0)); break;
	 
	        // Pressing 's' will stop the sample from playing.
	        case's': AL10.alSourceStop(source.get(0)); break;
	 
	        // Pressing 'h' will pause the sample.
	        case 'h': AL10.alSourcePause(source.get(0)); break;
	      };
	    }
	    killALData();
	    AL.destroy();
	  }
	  
	  public static void killALData() {
		    AL10.alDeleteSources(source);
		    AL10.alDeleteBuffers(buffer);
		  }
}
