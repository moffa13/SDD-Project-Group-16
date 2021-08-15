package Default;
import java.awt.geom.Point2D;

public class Segment implements Comparable<Segment> {
	private final ComparablePoint _p1;
	private final ComparablePoint _p2;
	private final Status _status;
	private final int _segGroup;
	
	
	public Segment(Status status, int segGroup, double p1x, double p1y, double p2x, double p2y){
		_status = status;
		_segGroup = segGroup;
		_p1 = new ComparablePoint(new Point2D.Double(p1x, p1y));
		_p2 = new ComparablePoint(new Point2D.Double(p2x, p2y));
	}
	
	public Segment(Status status, int segGroup, ComparablePoint p1, ComparablePoint p2){
		_segGroup = segGroup;
		_status = status;
		this._p1 = p1;
		this._p2 = p2;
	}
	
	/**
	 * Return the segment's group
	 * Used when adding a new set of segments and separate them
	 * Specifically for intersections
	 * @return
	 */
	public int getGroup(){
		return _segGroup;
	}
	
	/**
	 * Return the upper endpoint of the segment
	 * @return A point
	 */
	public ComparablePoint getUpperEndpoint(){
		if(_p1.compareTo(_p2) > 0){
			return _p1;
		}
		
		return _p2;
	}
	
	/**
	 * Return the lower endpoint of the segment
	 * @return A point
	 */
	public ComparablePoint getLowerEndpoint(){
		if(getUpperEndpoint().compareTo(_p1) == 0){
			return _p2;
		}
		
		return _p1;
	}
	
	/**
	 * Return the segment's slope
	 * @return A double precision value
	 */
	public double getSlope(){
		return (_p1._p.y - _p2._p.y) / (_p1._p.x - _p2._p.x);
	}
	
	/**
	 * Return whether or not the segment is vertical
	 * @return A boolean
	 */
	public boolean isVertical(){
		return Utilities.approxEqual(getLowerEndpoint()._p.x, getUpperEndpoint()._p.x);
	}
	
	/**
	 * Find an intersection with an y value
	 * If the segment is horizontal, throw an exception
	 * @param axisY The y value we want to check for intersection
	 * @return A x value corresponding to the intersection with y
	 */
	public double findIntersectionWithY(double axisY){
		
		if(isVertical()){
			return getUpperEndpoint()._p.x;
		}else if(isHorizontal()){
			throw new RuntimeException("Parallel lines");
		}
		
		double m = getSlope();
		double x = _p1._p.x;
		double y = _p1._p.y;
		double p = y - m * x;
		double xIntersect = (axisY - p) / m;

		return xIntersect;
	}
	
	/**
	 * Find an intersection with an x value
	 * If the segment is vertical, throw an exception
	 * @param axisX The x value we want to check for intersection
	 * @return An y value corresponding to the intersection with x
	 */
	public double findIntersectionWithX(double axisX){
		
		if(isHorizontal()){
			return getUpperEndpoint()._p.y;
		}else if(isVertical()){
			throw new RuntimeException("Parallel lines");
		}
		
		double m = getSlope();
		double x = _p1._p.x;
		double y = _p1._p.y;
		double p = y - m * x;
		double yIntersect = (m * axisX) + p;

		return yIntersect;
	}
	
	/**
	 * Whether or not the segment intersects with a point
	 * @param p The point
	 * @return A boolean
	 */
	public boolean intersectWithPoint(ComparablePoint p){
		
		double x1 = getUpperEndpoint()._p.x;
		double x2 = getLowerEndpoint()._p.x;
		
		double y1 = getUpperEndpoint()._p.y;
		double y2 = getLowerEndpoint()._p.y;
		
		if(isVertical()){
			// Segment is vertical, we need to check if one of its point has same x as the point 
			// and y1,y2 is between the y of the point
			return Utilities.approxEqual(x1, p._p.x) && Utilities.approxSmallerEqual(y2, p._p.y) && Utilities.approxGreaterEqual(y1, p._p.y);
		}else if(isHorizontal()){
			// x1 is left, x2 is right
			// Segment is horizontal, we need to check if one of its point has same y as the point
			// and x1,x2 is between the x of the point
			return Utilities.approxEqual(y1, p._p.y) && Utilities.approxSmallerEqual(x1, p._p.x) && Utilities.approxGreaterEqual(x2, p._p.x);
		}else{
			// Segment is not horizontal nor vertical, check intersection with y axis of point 
			// and check whether or not the intersection has the same x value.
			double intersectX = findIntersectionWithY(p._p.y);
			return Utilities.approxEqual(intersectX, p._p.x);
		}
		
	}
	
	/**
	 * Finds an intersection between two segments
	 * @param s the segment to look for intersection
	 * @return A point representing the intersection
	 */
	public Point2D.Double findIntersectionWithSegment(Segment s){
		
		if(isHorizontal()){
			if(s.isHorizontal()) {
				if(
						(intersectWithPoint(s.getUpperEndpoint()) || intersectWithPoint(s.getLowerEndpoint()))){
							throw new RuntimeException("Parallel segments");	
						}
				return null;
			}
			Point2D.Double rp = new Point2D.Double(s.findIntersectionWithY(getUpperEndpoint()._p.y), getUpperEndpoint()._p.y);
			ComparablePoint p = new ComparablePoint(rp);
			if(mightContainPoint(p) && s.mightContainPoint(p)) return rp;
			return null;
		}else if(s.isHorizontal()){
			return s.findIntersectionWithSegment(this);
		}
		
		if(isVertical()){
			if(s.isVertical()) {
				if(
					(intersectWithPoint(s.getUpperEndpoint()) || intersectWithPoint(s.getLowerEndpoint()))){
						throw new RuntimeException("Parallel segments");	
					}
			
				return null;
			}
			Point2D.Double rp = new Point2D.Double(getUpperEndpoint()._p.x, s.findIntersectionWithX(getUpperEndpoint()._p.x));
			ComparablePoint p = new ComparablePoint(rp);
			if(mightContainPoint(p) && s.mightContainPoint(p)) return rp;
			return null;
		}else if(s.isVertical()){
			return s.findIntersectionWithSegment(this);
		}
			
		double s1m = getSlope();
		double s2m = s.getSlope();
		double s1x = _p1._p.x;
		double s1y = _p1._p.y;
		double s2x = s._p1._p.x;
		double s2y = s._p1._p.y;
		double s1p = s1y - s1m * s1x;
		double s2p = s2y - s2m * s2x;
		
		double xIntersect = (s1p - s2p) / (s2m - s1m);
		double yIntersect = s1m * xIntersect + s1p;
		
		if(Double.isFinite(xIntersect) && Double.isFinite(yIntersect)){
			
			Point2D.Double rp = new Point2D.Double(xIntersect, yIntersect);
			ComparablePoint p = new ComparablePoint(rp);
			if(mightContainPoint(p) && s.mightContainPoint(p)){
				return rp;
			}
			return null;
		}
		
		return null;

		
	}
	
	/**
	 * Return the first point making the segment
	 * @return A Point
	 */
	public ComparablePoint getP1(){
		return _p1;
	}
	
	/**
	 * Return the second point making the segment
	 * @return A Point
	 */
	public ComparablePoint getP2(){
		return _p2;
	}
	
	/**
	 * Return if the point is one of the two points making the segment
	 * @param p The point we want to check
	 * @return
	 */
	public boolean contains(ComparablePoint p){
		 return getP1().compareTo(p) == 0 || getP2().compareTo(p) == 0;
	}
	
	/**
	 * Returns whether or not the point is within the boundaries of the segment
	 * This is used when we check if a segment intersects with another one
	 * Because they are represented with the y = mx + b form
	 * which is an infinite representation
	 * @param p the point we check for
	 * @return true if the point is within the bounds
	 */
	public boolean mightContainPoint(ComparablePoint p){
		
		boolean isBetweenY;
		
		// Segment is horizontal and the point is not on the same y
		if(isHorizontal()){
			if(!Utilities.approxEqual(p._p.y, getUpperEndpoint()._p.y))
				return false;
			isBetweenY = true;
		}else{
			isBetweenY = Utilities.approxGreaterEqual(p._p.y, getLowerEndpoint()._p.y) && Utilities.approxSmallerEqual(p._p.y, getUpperEndpoint()._p.y);
		}
		
		double smallestX = Math.min(getUpperEndpoint()._p.x, getLowerEndpoint()._p.x);
		double biggestX = Math.max(getUpperEndpoint()._p.x, getLowerEndpoint()._p.x);
		boolean isBetweenX = Utilities.approxGreaterEqual(p._p.x, smallestX) && Utilities.approxSmallerEqual(p._p.x, biggestX);
		return isBetweenY && isBetweenX;
	}
	
	@Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return getUpperEndpoint().compareTo(((Segment)o).getUpperEndpoint()) == 0
				&& getLowerEndpoint().compareTo(((Segment)o).getLowerEndpoint()) == 0;

    }
	
	
	@Override
	public int hashCode(){
		return 31 * _p1.hashCode() + _p2.hashCode() + _segGroup;
	}
	
	/**
	 * Whether or not the segment is horizontal
	 * @return A boolean
	 */
	public boolean isHorizontal(){
		return Utilities.approxEqual(getP1()._p.y, getP2()._p.y);
	}

	@Override
	/**
	 * Compares a segment to another
	 * @param o The segment to compare to
	 */
	public int compareTo(Segment o) {
		
			if(equals(o)) return 0;
			
			ComparablePoint p = _status.getEventPoint();
			
			double s1xIntercept;
			double s2xIntercept;
			
			if(isHorizontal()){
				if(o.intersectWithPoint(p)){
					return 1;
				}else if(o.isHorizontal()){
					s2xIntercept = o.getUpperEndpoint()._p.x;
				}else{
					s2xIntercept = o.findIntersectionWithY(_status.getSweepLinePosition());
				}
				s1xIntercept = p._p.x;
			}else if(o.isHorizontal()){
				return -o.compareTo(this);
			}else{
				// Find intersection x value with the sweep line
				s1xIntercept = findIntersectionWithY(_status.getSweepLinePosition()); 
				s2xIntercept = o.findIntersectionWithY(_status.getSweepLinePosition()); 
			}
								
			// If the current segment has a higher X, it is considered bigger.
			if(Utilities.approxGreater(s1xIntercept, s2xIntercept)){
				return 1;
			}
			// If the current segment has a lower X, it is considered smaller.
			else if(Utilities.approxSmaller(s1xIntercept, s2xIntercept)){
				return -1;
			}else{
				// They both cross the sweep line at the same x
				double s1newIntercept = findIntersectionWithY(_status.getSweepLinePosition() - 0.001); 
				double s2newIntercept = o.findIntersectionWithY(_status.getSweepLinePosition() - 0.001);
				
				if(Utilities.approxGreater(s1newIntercept, s2newIntercept)){
					return 1;
				}else{
					return -1;
				}
			}
					
	}
}
