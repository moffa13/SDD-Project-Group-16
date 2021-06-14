package Default;

public class Pair <T, V> {
	private T _t;
	private V _v;
	
	public T getKey() {
		return _t;
	}
	
	public V getValue() {
		return _v;
	}
	
	public Pair(T t, V v) {
		_t = t;
		_v = v;
	}
}
