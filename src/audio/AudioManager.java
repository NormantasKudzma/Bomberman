package audio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALC11;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALContext;
import org.lwjgl.openal.ALDevice;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.stb.STBVorbisInfo;

import utils.ConfigManager;
import utils.Pair;
import utils.Paths;

public class AudioManager {
	public enum SoundType {
		BOMB_EXPLODE	(Paths.SOUNDS + "bombexplosion.ogg"),
		BOMB_PLACED		(Paths.SOUNDS + "bombplaced.ogg"),
		FIRST_BLOOD		(Paths.SOUNDS + "firstblood.ogg"),
		DOUBLE_KILL		(Paths.SOUNDS + "doublekill.ogg"),
		TRIPLE_KILL		(Paths.SOUNDS + "triplekill.ogg"),
		MONSTER_KILL	(Paths.SOUNDS + "monsterkill.ogg"),
		POWERUP_PICKUP	(Paths.SOUNDS + "poweruppickup.ogg"),
		INVALID(null);
		
		private String filename;
		
		private SoundType(String f){
			filename = f;
		}
		
		public String getFilename(){
			return filename;
		}
		
		public static SoundType fromString(String str){
			for (int i = 0; i < values().length; i++){
				if (values()[i].getFilename().equals(str)){
					return values()[i];
				}
			}
			return INVALID;
		}
	}
	
	private static final int NUM_CHANNELS = 4;
	
	/** Buffers hold sound data. */
	private static IntBuffer buffer = BufferUtils.createIntBuffer(NUM_CHANNELS);

	/** Sources are points emitting sound. */
	private static IntBuffer source = BufferUtils.createIntBuffer(NUM_CHANNELS);

	private static HashMap<String, Pair<STBVorbisInfo, ByteBuffer>> soundList = new HashMap<String, Pair<STBVorbisInfo, ByteBuffer>>(SoundType.values().length + 10);
	private static ALDevice device;
	private static ALCCapabilities capabilities;
	private static ALContext context;
	private static STBVorbisInfo info = new STBVorbisInfo();
	private static int soundChannel = NUM_CHANNELS - 1;
	
	private AudioManager(){}
	
	static {
		device = ALDevice.create(null);
		capabilities = device.getCapabilities();

		System.out.println("OpenALC10: " + capabilities.OpenALC10);
		System.out.println("OpenALC11: " + capabilities.OpenALC11);
		System.out.println("caps.ALC_EXT_EFX = " + capabilities.ALC_EXT_EFX);

		if ( capabilities.OpenALC11 ) {
			List<String> devices = ALC.getStringList(0L, ALC11.ALC_ALL_DEVICES_SPECIFIER);
			for ( int i = 0; i < devices.size(); i++ ) {
				System.out.println(i + ": " + devices.get(i));
			}
		}

		String defaultDeviceSpecifier = ALC10.alcGetString(0L, ALC10.ALC_DEFAULT_DEVICE_SPECIFIER);
		System.out.println("Default device: " + defaultDeviceSpecifier);

		context = ALContext.create(device);
		AL10.alGenSources(source);
		AL10.alGenBuffers(buffer);

		System.out.println("ALC_FREQUENCY: " + ALC10.alcGetInteger(device.getPointer(), ALC10.ALC_FREQUENCY) + "Hz");
		System.out.println("ALC_REFRESH: " + ALC10.alcGetInteger(device.getPointer(), ALC10.ALC_REFRESH) + "Hz");
		System.out.println("ALC_SYNC: " + (ALC10.alcGetInteger(device.getPointer(), ALC10.ALC_SYNC) == ALC10.ALC_TRUE));
		System.out.println("ALC_MONO_SOURCES: " + ALC10.alcGetInteger(device.getPointer(), ALC11.ALC_MONO_SOURCES));
		System.out.println("ALC_STEREO_SOURCES: " + ALC10.alcGetInteger(device.getPointer(), ALC11.ALC_STEREO_SOURCES));
		
		// Loading all initial sounds and putting them into sound list
		Pair<STBVorbisInfo, ByteBuffer> pair;
		for (SoundType stype : SoundType.values()){
			if (stype == SoundType.INVALID){
				continue;
			}
			pair = new Pair<STBVorbisInfo, ByteBuffer>(new STBVorbisInfo(), null);
			pair.value = readVorbis(stype.getFilename(), 1024 * 32, pair.key);
			soundList.put(stype.getFilename(), pair);
		}
	}
	
	public static final void destroy(){
		if (context != null && device != null){
			try {
				context.destroy();
				device.destroy();
			}
			catch (Exception e){
				e.printStackTrace();
			}
		}

		AL10.alDeleteSources(source);
		AL10.alDeleteBuffers(buffer);
	}
	
	private static ByteBuffer readVorbis(String resource, int bufferSize, STBVorbisInfo info) {
		ByteBuffer vorbis;
		try {
			vorbis = ConfigManager.ioResourceToByteBuffer(resource, bufferSize);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		IntBuffer error = BufferUtils.createIntBuffer(1);
		long decoder = STBVorbis.stb_vorbis_open_memory(vorbis, error, null);
		if ( decoder == 0 )
			throw new RuntimeException("Failed to open Ogg Vorbis file. Error: " + error.get(0));

		STBVorbis.stb_vorbis_get_info(decoder, info.buffer());

		int channels = info.getChannels();

		int lengthSamples = STBVorbis.stb_vorbis_stream_length_in_samples(decoder);

		ByteBuffer pcm = BufferUtils.createByteBuffer(lengthSamples * 2);

		STBVorbis.stb_vorbis_get_samples_short_interleaved(decoder, channels, pcm, lengthSamples);
		STBVorbis.stb_vorbis_close(decoder);

		return pcm;
	}
	
	public static void playMusic(String path) {
		path = Paths.MUSIC + path;		
		ByteBuffer pcm = readVorbis(path, 32 * 1024, info);
		
		AL10.alSourcef(source.get(0), AL10.AL_PITCH, 1.0f);
	    AL10.alSourcef(source.get(0), AL10.AL_GAIN, 1.0f);
		AL10.alBufferData(buffer.get(0), AL10.AL_FORMAT_STEREO16, pcm, info.getSampleRate());
		AL10.alSourcei(source.get(0), AL10.AL_BUFFER, buffer.get(0));
		AL10.alSourcePlay(source.get(0));
	
		System.out.println("AudioManager::Now playing [" + path + "]. SampleRate [" + info.getSampleRate() + "]");
	}
	
	public static void playSound(SoundType sound){
		playSound(sound.getFilename());
	}
	
	public static void playSound(String path){
		Pair<STBVorbisInfo, ByteBuffer> s = soundList.get(path);
		if (path == null || s == null){
			Pair<STBVorbisInfo, ByteBuffer> stbInfo = new Pair<STBVorbisInfo, ByteBuffer>(new STBVorbisInfo(), null);
			soundList.put(path, stbInfo);
			stbInfo.value = readVorbis(path, 1024 * 32, stbInfo.key);
			s = soundList.get(path);
		}
		
		AL10.alSourcef(source.get(soundChannel), AL10.AL_PITCH, 2.0f);
	    AL10.alSourcef(source.get(soundChannel), AL10.AL_GAIN, 2.0f);
		AL10.alBufferData(buffer.get(soundChannel), AL10.AL_FORMAT_MONO16, s.value, s.key.getSampleRate());
		AL10.alSourcei(source.get(soundChannel), AL10.AL_BUFFER, buffer.get(soundChannel));
		AL10.alSourcePlay(source.get(soundChannel));
		
		// Reset used sound channel counter
		soundChannel--;
		if (soundChannel <= 0){
			soundChannel = NUM_CHANNELS - 1;
		}
	}
	
	public static void stopMusic(){
		AL10.alSourceStop(source.get(0));
	}
}
