package Default;

public class Utilities {
	static boolean approxEqual(double d1, double d2){
		return Math.abs(d2 - d1) < 0.5;
	}
	static boolean approxSmaller(double d1, double d2){
		return !approxEqual(d1, d2) && d1 < d2;
	}
	static boolean approxGreater(double d1, double d2){
		return !approxEqual(d1, d2) && d1 > d2;
	}
	static boolean approxGreaterEqual(double d1, double d2){
		return approxEqual(d1, d2) || d1 > d2;
	}
	static boolean approxSmallerEqual(double d1, double d2){
		return approxEqual(d1, d2) || d1 < d2;
	}
}
