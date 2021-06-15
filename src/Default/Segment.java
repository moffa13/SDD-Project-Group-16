package Default;
import java.awt.geom.Point2D;

public class Segment implements Comparable<Segment> {
	private final ComparablePoint p1;
	private final ComparablePoint p2;
	private final Status _status;
	private final int _segGroup;
	public Segment(Status status, int segGroup, double p1x, double p1y, double p2x, double p2y){
		_status = status;
		_segGroup = segGroup;
		p1 = new ComparablePoint(new Point2D.Double(p1x, p1y));
		p2 = new ComparablePoint(new Point2D.Double(p2x, p2y));
	}
	
	public Segment(Status status, int segGroup, ComparablePoint p1, ComparablePoint p2){
		_segGroup = segGroup;
		_status = status;
		this.p1 = p1;
		this.p2 = p2;
	}
	
	public int getGroup(){
		return _segGroup;
	}
	
	public ComparablePoint getUpperEndpoint(){
		if(p1.compareTo(p2) > 0){
			return p1;
		}
		
		return p2;
	}
	
	public ComparablePoint getLowerEndpoint(){
		if(getUpperEndpoint().compareTo(p1) == 0){
			return p2;
		}
		
		return p1;
	}
	
	public double getSlope(){
		return (p1._p.y - p2._p.y) / (p1._p.x - p2._p.x);
	}
	
	public boolean isVertical(){
		return Utilities.approxEqual(getLowerEndpoint()._p.x, getUpperEndpoint()._p.x);
	}
	
	public double findIntersectionWithY(double axisY){
		
		if(isVertical()){
			return getUpperEndpoint()._p.x;
		}else if(isHorizontal()){
			throw new RuntimeException("Parallel lines");
		}
		
		double m = getSlope();
		double x = p1._p.x;
		double y = p1._p.y;
		double p = y - m * x;
		double xIntersect = (axisY - p) / m;

		return xIntersect;
	}
	
	public double findIntersectionWithX(double axisX){
		
		if(isHorizontal()){
			return getUpperEndpoint()._p.y;
		}else if(isVertical()){
			throw new RuntimeException("Parallel lines");
		}
		
		double m = getSlope();
		double x = p1._p.x;
		double y = p1._p.y;
		double p = y - m * x;
		double yIntersect = (m * axisX) + p;

		return yIntersect;
	}
	
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
	
	public boolean isAtLeftRightOfPoint(ComparablePoint p, Direction d){
		if(isHorizontal()){
			if(d == Direction.LEFT){
				// Check if right point is smaller than p.x
				return Utilities.approxSmaller(getLowerEndpoint()._p.x, p._p.x);
			}else{
				// Check if left point is greater than p.x
				return Utilities.approxGreater(getUpperEndpoint()._p.x, p._p.x);
			}
		}
		
		double intersect = findIntersectionWithY(p._p.y);
		
		if(d == Direction.LEFT){
			return Utilities.approxSmaller(intersect, p._p.x);
		}else{
			return Utilities.approxGreater(intersect, p._p.x);
		}
		
	}
	
	/**
	 * Finds an intersection between two segments
	 * @param s the segment to look for intersection
	 * @return A point representing the intersection
	 */
	public Point2D.Double findIntersectionWithSegment(Segment s){
		
		if(isHorizontal()){
			Point2D.Double rp = new Point2D.Double(s.findIntersectionWithY(getUpperEndpoint()._p.y), getUpperEndpoint()._p.y);
			ComparablePoint p = new ComparablePoint(rp);
			if(mightContainPoint(p) && s.mightContainPoint(p)) return rp;
			return null;
		}else if(s.isHorizontal()){
			return s.findIntersectionWithSegment(this);
		}
		
		if(isVertical()){
			Point2D.Double rp = new Point2D.Double(getUpperEndpoint()._p.x, s.findIntersectionWithX(getUpperEndpoint()._p.x));
			ComparablePoint p = new ComparablePoint(rp);
			if(mightContainPoint(p) && s.mightContainPoint(p)) return rp;
			return null;
		}else if(s.isVertical()){
			return s.findIntersectionWithSegment(this);
		}
			
		double s1m = getSlope();
		double s2m = s.getSlope();
		double s1x = p1._p.x;
		double s1y = p1._p.y;
		double s2x = s.p1._p.x;
		double s2y = s.p1._p.y;
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
	
	public ComparablePoint getP1(){
		return p1;
	}
	public ComparablePoint getP2(){
		return p2;
	}
	
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
	
	public boolean isHorizontal(){
		return Utilities.approxEqual(getP1()._p.y, getP2()._p.y);
	}

	@Override
	/**
	 * Compares a segment to another
	 */
	public int compareTo(Segment o) {
		
			if(equals(o)) return 0;
			
			if(isHorizontal()){
				return 1;
			}else if(o.isHorizontal()){
				return -1;
			}
			
			// Find intersection x value with the sweep line
			double s1xIntercept = findIntersectionWithY(_status.getSweepLinePosition()); 
			double s2xIntercept = o.findIntersectionWithY(_status.getSweepLinePosition()); 
					
			// If the current segment has a higher X, it is considered bigger.
			if(Utilities.approxGreater(s1xIntercept, s2xIntercept)){
				return 1;
			}
			// If the current segment has a lower X, it is considered smaller.
			else if(Utilities.approxSmaller(s1xIntercept, s2xIntercept)){
				return -1;
			}else{
				// They both cross the sweep line at the same x
				double s1UpperPointX = getUpperEndpoint()._p.x; 
				double s2UpperPointX = o.getUpperEndpoint()._p.x;
				// If the current segment's upper endpoint has smaller X, 
				// it is considered bigger because after crossing the other segment 
				// it will be at the opposite side.
				if(Utilities.approxGreater(s1UpperPointX, s2UpperPointX)){
					return _status.isInsertMode() ? -1 : 1;
				}else{
					return _status.isInsertMode() ? 1 : -1;
				}
			}
					
	}
}
