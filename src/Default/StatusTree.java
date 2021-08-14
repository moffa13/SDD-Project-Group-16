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
	
	/**
	 * Override of the insertEmpty method to to add the right node type
	 * @param elem The segment to insert
	 */
	public void insertEmpty(Segment elem) {
		setData(elem);
		setLeft(new StatusTree(_status));
		setRight(new StatusTree(_status));
		height = 1;
	}
	
	
	/**
	 * Override of the getLeft method to get the right type
	 * @return A StatusTree
	 */
	public StatusTree getLeft(){
		return (StatusTree)super.getLeft();
	}
	
	/**
	 * Override of the getRight method to get the right type
	 * @return A StatusTree
	 */
	public StatusTree getRight(){
		return (StatusTree)super.getRight();
	}
	
	/**
	 * Find all segments in the status tree crossing a point which is not part of the segments
	 * @param p the point we check for intersection
	 * @return A set of segments which are intersecting with p
	 */
	public HashSet<Segment> findC(ComparablePoint p){
		
		Segment currentSegment = getData();
		
		if(currentSegment == null) return new HashSet<>();
		
		HashSet<Segment> all = new HashSet<>();
		
		StatusTree leftNeighbourST = getLeft();
		StatusTree rightNeighbourST = getRight();
		
		if(currentSegment.intersectWithPoint(p)){
			if(!currentSegment.contains(p)){ // The segment does not have an endpoint at the sweepline, add to C
				all.add(currentSegment);
			}
		}else{
			
			
			double SLIntersectionX = currentSegment.findIntersectionWithY(p._p.y);
			
			if(Utilities.approxSmaller(SLIntersectionX, p._p.x)){ // intersection with current segment is smaller than the event point, look right neighbour, so remove left one
				leftNeighbourST = null;
			}else{
				rightNeighbourST = null;
			}
			
		}
		
		if(leftNeighbourST != null){
			all.addAll(leftNeighbourST.findC(p));
		}
		
		if(rightNeighbourST != null){
			all.addAll(rightNeighbourST.findC(p));
		}
		
		return all;
		
	}
	
	
	/**
	 * Public version of getNeighbour
	 * @param p The segment for which we are looking a neighbour 
	 * @param direction Whether you look for the left or the right neighbour
	 * @return The best candidate for being the nearest neighbour
	 */
	public Segment getNeighbour(Segment p, Direction direction){
		Pair<BalancedBinarySearchTree<Segment>, HashSet<Segment>> p2 = searchTree(p);
		if(p2 == null) {
			return null;
		}
		
		StatusTree tree = (StatusTree)(p2.getKey());
		
		if(direction == Direction.LEFT && tree.getLeft() != null && tree.getLeft().getData() != null) { // Left neighbour required and has a left child
			return tree.getNeighbour(p, direction, null, true);
		}else if(direction == Direction.RIGHT && tree.getRight() != null && tree.getRight().getData() != null) { // Right neighbour required and has a right child
			return tree.getNeighbour(p, direction, null, true);
		}else {
			return getMinMaxDirection(p, p2.getValue(), direction);
		}
		
	}
	
	/**
	 * Returns the direct neighbour of a segment in a set
	 * @param segment The segment for which we are looking a neighbour
	 * @param set The path taken to get to the segment
	 * @param d Whether you look for the left or the right neighbour
	 * @return The best candidate for being the nearest neighbour
	 */
	private static Segment getMinMaxDirection(Segment segment, HashSet<Segment> set, Direction d) {
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
	private Segment getNeighbour(Segment p, Direction direction, Segment prec, boolean dir){
		
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