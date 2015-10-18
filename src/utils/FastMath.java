package utils;

public class FastMath {
	public static final float asin(float rad) {
		return asin[(int) (rad * radToIndex) & SIN_MASK];
	}
	
	public static final float acos(float rad) {
		return acos[(int) (rad * radToIndex) & SIN_MASK];
	}
	
	public static final float sin(float rad) {
		return sin[(int) (rad * radToIndex) & SIN_MASK];
	}

	public static final float cos(float rad) {
		return cos[(int) (rad * radToIndex) & SIN_MASK];
	}
	
	public static final float asinDeg(float deg){
		return asin[(int) (deg * degToIndex) & SIN_MASK];
	}
	
	public static final float acosDeg(float deg){
		return acos[(int) (deg * degToIndex) & SIN_MASK];
	}

	public static final float sinDeg(float deg) {
		return sin[(int) (deg * degToIndex) & SIN_MASK];
	}

	public static final float cosDeg(float deg) {
		return cos[(int) (deg * degToIndex) & SIN_MASK];
	}

	public static double fastSqrt(double num) {
		// Magic
		return Double.longBitsToDouble(((Double.doubleToLongBits(num) - (1l << 52)) >> 1) + (1l << 61));
	}

	private static final float RAD, DEG;
	private static final int SIN_BITS, SIN_MASK, SIN_COUNT;
	private static final float radFull, radToIndex;
	private static final float degFull, degToIndex;
	private static final float[] sin, cos;
	private static final float[] asin, acos;

	static {
		RAD = (float) Math.PI / 180.0f;
		DEG = 180.0f / (float) Math.PI;

		SIN_BITS = 12;
		SIN_MASK = ~(-1 << SIN_BITS);
		SIN_COUNT = SIN_MASK + 1;

		radFull = (float) (Math.PI * 2.0);
		degFull = (float) (360.0);
		radToIndex = SIN_COUNT / radFull;
		degToIndex = SIN_COUNT / degFull;

		sin = new float[SIN_COUNT];
		cos = new float[SIN_COUNT];
		asin = new float[SIN_COUNT];
		acos = new float[SIN_COUNT];

		double arg = 0.0f;
		for (int i = 0; i < SIN_COUNT; i++) {
			arg = (i + 0.5f) / SIN_COUNT * radFull;
			sin[i] = (float) Math.sin(arg);
			cos[i] = (float) Math.cos(arg);
			asin[i] = (float) Math.asin(arg);
			acos[i] = (float) Math.acos(arg);
		}

		// Four cardinal directions (credits: Nate)
		for (int i = 0; i < 360; i += 90) {
			sin[(int) (i * degToIndex) & SIN_MASK] = (float) Math.sin(i * Math.PI / 180.0);
			cos[(int) (i * degToIndex) & SIN_MASK] = (float) Math.cos(i * Math.PI / 180.0);
			asin[(int) (i * degToIndex) & SIN_MASK] = (float) Math.asin(i * Math.PI / 180.0);
			acos[(int) (i * degToIndex) & SIN_MASK] = (float) Math.acos(i * Math.PI / 180.0);
		}
	}
}
