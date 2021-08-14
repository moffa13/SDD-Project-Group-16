package Default;

/**
 * 
 * @author moffa
 *
 */
public class Status {
	double sweepLineY = Double.POSITIVE_INFINITY;
	private StatusTree tree = new StatusTree(this);
	private ComparablePoint _eventPoint;
	
	public Status(){
		super();
	}
	
	/**
	 * Set the event point to a new one and update sweepline
	 * Show an error message to the console for debugging purposes
	 * @param eventPoint
	 */
	public void setEventPoint(ComparablePoint eventPoint){
		// The new point should always be more important.
		if(_eventPoint != null){
			if(Utilities.approxGreater(eventPoint._p.y, _eventPoint._p.y)){
				System.out.println("Internal error, new event point cannot be higher than the old one.");
			}
		}
		
		_eventPoint = eventPoint;
		updateSweepLinePosition(_eventPoint);
	}
	
	/**
	 * Return the event point
	 * @return A point
	 */
	public ComparablePoint getEventPoint(){
		return _eventPoint;
	}
	
	/**
	 * Reset the sweepline position and remove the event point
	 * Clear the status tree
	 */
	public void clear(){
		sweepLineY = Double.POSITIVE_INFINITY;
		_eventPoint = null;
		tree = new StatusTree(this);
	}
	
	/**
	 * Return the tree
	 * @return A Statustree
	 */
	public StatusTree tree(){
		return tree;
	}
	
	/**
	 * Return the sweep line position
	 * @return A double precision number
	 */
	public double getSweepLinePosition(){
		return sweepLineY;
	}
	
	/**
	 * Change the sweep line position using a point
	 * @param p A point representing the new position (y)
	 */
	public void updateSweepLinePosition(ComparablePoint p){
		sweepLineY = p._p.y;
	}
}
