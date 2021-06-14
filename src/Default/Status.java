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
	
	// Insert mode is used to swap the order of 2 segments when reinserting
	private boolean _insertMode = false;
	
	public Status(){
		super();
	}
	
	public void setInsertMode(){
		_insertMode = true;
	}
	
	public void unsetInsertMode(){
		_insertMode = false;
	}
	
	public boolean isInsertMode(){
		return _insertMode;
	}
	
	public void setEventPoint(ComparablePoint eventPoint){
		// The new point should always be more important.
		if(_eventPoint != null){
			if(eventPoint._p.y > _eventPoint._p.y ){
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
		_insertMode = false;
		tree = new StatusTree(this);
	}
	
	public StatusTree tree(){
		return tree;
	}
	
	public double getSweepLinePosition(){
		return sweepLineY;
	}
	public void updateSweepLinePosition(ComparablePoint p){
		
		if(p._p.y > sweepLineY){
			System.out.println("Internal error");
		}
		sweepLineY = p._p.y;
	}
}
