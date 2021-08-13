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
	
	@Override
	public boolean equals(Object o){
		if (o == this) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> other = (Pair<?, ?>)o;
        if(other.getKey().equals(getKey()) && other.getValue().equals(getValue()))
        	return true;
        return false;
        
	}
	
	@Override
	public int hashCode(){
		return 31 * getKey().hashCode() + getValue().hashCode();
	}
}
