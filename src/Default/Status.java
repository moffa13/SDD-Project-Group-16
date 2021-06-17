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
	
	public ComparablePoint getEventPoint(){
		return _eventPoint;
	}
	
	public void clear(){
		sweepLineY = Double.POSITIVE_INFINITY;
		_eventPoint = null;
		tree = new StatusTree(this);
	}
	
	public StatusTree tree(){
		return tree;
	}
	
	public double getSweepLinePosition(){
		return sweepLineY;
	}
	public void updateSweepLinePosition(ComparablePoint p){
		sweepLineY = p._p.y;
	}
}
