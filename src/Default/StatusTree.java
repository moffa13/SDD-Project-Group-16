package Default;

import java.util.HashSet;

/**
 * Specification of a BST to add stuff to work with segments
 * @author moffa
 *
 */
public class StatusTree extends BalancedBinarySearchTree<Segment> {
	
	private Status _status;
	
	public StatusTree(Status status){
		_status = status;
	}
	
	public void insertEmpty(Segment elem) {
		setData(elem);
		setLeft(new StatusTree(_status));
		setRight(new StatusTree(_status));
		height = 1;
	}
	
	
	public StatusTree getLeft(){
		return (StatusTree)super.getLeft();
	}
	public StatusTree getRight(){
		return (StatusTree)super.getRight();
	}
	
	/**
	 * Finds all segments in the status tree crossing a point which is not part of the segments
	 * @param p the point we check for intersection
	 * @return A set of segments which are intersecting with p
	 */
	public HashSet<Segment> findC(ComparablePoint p){
		
		Segment currentSegment = getData();
		
		if(currentSegment == null) return new HashSet<>();
		
		HashSet<Segment> all = new HashSet<>();
		
		// Intersection of currentSegment with sweep line and p
		if(currentSegment.intersectWithPoint(p)){
			
			if(!currentSegment.contains(p)){ // The segment does not have an endpoint at the sweepline, add to C
				all.add(currentSegment);
			}
			
		}
		
		if(getLeft() != null){
			all.addAll(getLeft().findC(p));
		}
		if(getRight() != null){
			all.addAll(getRight().findC(p));
		}
		
		return all;
		
	}
	
	public Segment getNeighbour(Segment p, Direction direction){
		Pair<BalancedBinarySearchTree<Segment>, HashSet<Segment>> p2 = searchTree(p);
		if(p2 == null) {
			return null;
		}
		
		StatusTree tree = (StatusTree)(p2.getKey());
		
		if(direction == Direction.LEFT && tree.getLeft().getData() != null) { // Left neighbour required and has a left child
			return tree.getNeighbour(p, direction, null, true);
		}else if(direction == Direction.RIGHT && tree.getRight().getData() != null) { // Right neighbour required and has a right child
			return tree.getNeighbour(p, direction, null, true);
		}else {
			return getMinMaxDirection(p, p2.getValue(), direction);
		}
		
	}
	
	/**
	 * Returns the direct neighbour of a segment in a set
	 * @param segment
	 * @param set
	 * @param d Whether you look for the left or the right neighbour
	 * @return
	 */
	public Segment getMinMaxDirection(Segment segment, HashSet<Segment> set, Direction d) {
		Segment s = null;
		for(Segment seg : set) {
			if(d == Direction.LEFT && seg.compareTo(segment) < 0 && (s == null || seg.compareTo(s) > 0)
			|| (d == Direction.RIGHT && seg.compareTo(segment) > 0 && (s == null || seg.compareTo(s) < 0))) {
					s = seg;
			}
		}
		return s;
	}
	
	/**
	 * Gets the neighbour of a point in a status tree
	 * @param p The point we need to find the neighbour of
	 * @param direction The specified direction of the neighbour
	 * @param prec The last good candidate for recursion
	 * @return The Segment which is the neighbour
	 */
	public Segment getNeighbour(Segment p, Direction direction, Segment prec, boolean dir){
		
		Segment currentSegment = getData();
		
		if(currentSegment == null) return prec;
		
		// If intersection with p, go to direction
		if(dir){
			if(direction == Direction.LEFT){
				return getLeft().getNeighbour(p, direction, null, false);
			}else{
				return getRight().getNeighbour(p, direction, null, false);
			}
		}
		
		// No intersection with p
		else{
			
			StatusTree t;			
			// Try to find a segment closer
			
			// If the found segment is at the right of p, go to p (to the left)
			if(direction == Direction.RIGHT){
				t = getLeft();	
			}else{
				t = getRight();
			}
				
			return t.getNeighbour(p, direction, currentSegment, false);
			
		}

			
	}
}
