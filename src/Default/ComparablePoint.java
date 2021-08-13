package Default;
import java.awt.geom.Point2D;
import java.util.HashSet;

/**
 * Represents a point which can be compared to other points according to the Map Overlay technic
 * @author moffa
 *
 */
public class ComparablePoint implements Comparable<ComparablePoint> {

	public final Point2D.Double _p;
	private HashSet<Segment> _lowerSegment = new HashSet<>();
	private HashSet<Segment> _upperSegment = new HashSet<>();

	public ComparablePoint(Point2D.Double p){
		_p = p;
	}
	
	/**
	 * Add a lower segment to the list of the point's lower segments
	 * @param s A segment
	 */
	public void addLowerSegment(Segment s){
		_lowerSegment.add(s);
	}
	
	/**
	 * Return the lower segments of the point, if any
	 * @return A segment
	 */
	public HashSet<Segment> getLowerSegments(){
		return _lowerSegment;
	}
	
	/**
	 * Add an upper segment to the list of the point's upper segments
	 * @param s A segment
	 */
	public void addUpperSegment(Segment s){
		_upperSegment.add(s);
	}
	
	/**
	 * Return the upper segments of the point, if any
	 * @return A segment
	 */
	public HashSet<Segment> getUpperSegments(){
		return _upperSegment;
	}
	
	/**
	 * Convert the ComparablePoint to a simple point
	 * @return
	 */
	public Point2D.Double toPoint(){
		return _p;
	}

	
	@Override
	/**
	 * Compares to another point
	 * My point is greater if it has bigger Y or a smaller X
	 */
	public int compareTo(ComparablePoint o) {
		if(equals(o)) return 0;
		if(
				Utilities.approxGreater(_p.y, o._p.y) ||
				(Utilities.approxEqual(_p.y, o._p.y) && Utilities.approxSmaller(_p.x, o._p.x))
		) return 1;
		return -1;
	}
	
	@Override
	/**
	 * A ComparablePoint equals another if the coordinates are the same
	 */
	public boolean equals(Object o){
		if (o == this) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return toPoint().equals(((ComparablePoint)o).toPoint());
	}	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_p == null) ? 0 : _p.hashCode());
		return result;
	}
	
}
