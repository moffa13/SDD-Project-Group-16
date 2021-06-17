package Default;

public class Utilities {
	public static boolean approxEqual(double d1, double d2){
		return Math.abs(d2 - d1) < 1e-8;
	}
	public static boolean approxSmaller(double d1, double d2){
		return !approxEqual(d1, d2) && d1 < d2;
	}
	public static boolean approxGreater(double d1, double d2){
		return !approxEqual(d1, d2) && d1 > d2;
	}
	public static boolean approxGreaterEqual(double d1, double d2){
		return approxEqual(d1, d2) || d1 > d2;
	}
	public static boolean approxSmallerEqual(double d1, double d2){
		return approxEqual(d1, d2) || d1 < d2;
	}
}
