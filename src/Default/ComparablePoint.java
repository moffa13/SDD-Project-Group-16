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
	
	public void addLowerSegment(Segment s){
		_lowerSegment.add(s);
	}
	
	public HashSet<Segment> getLowerSegments(){
		return _lowerSegment;
	}
	
	public void addUpperSegment(Segment s){
		_upperSegment.add(s);
	}
	
	public HashSet<Segment> getUpperSegments(){
		return _upperSegment;
	}
	
	public Point2D.Double toPoint(){
		return _p;
	}

	
	@Override
	/**
	 * Compares to another point
	 * My point is greater if it has bigger Y or a smaller X
	 */
	public int compareTo(ComparablePoint o) {
		if(
				Utilities.approxGreater(_p.y, o._p.y) ||
				(Utilities.approxEqual(_p.y, o._p.y) && Utilities.approxSmaller(_p.x, o._p.x))
		) return 1;
		if(Utilities.approxEqual(_p.y, o._p.y) && Utilities.approxEqual(_p.x, o._p.x)) return 0;
		return -1;
	}
	
	
	
}
