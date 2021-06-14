package Default;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Map {
	
	private final ArrayList<Segment> segments = new ArrayList<>();
	BalancedBinarySearchTree<ComparablePoint> eventQueue = new BalancedBinarySearchTree<>();
	Status status = new Status();
	HashSet<Pair<ComparablePoint, HashSet<Segment>>> intersections = new HashSet<>();
	private int loadTimes = 0;
	

	public Map(){
	}
	
	/**
	 * Loads all the segments found in the file into the segment list
	 * @param path Absolute path of the map
	 * @param segGroup the group number for which all the segments belong to
	 * @throws IOException thrown when an IOException occurred
	 */
	public void loadMap(String path, int segGroup) throws IOException, RuntimeException{
		reset();
		BufferedReader reader;
		reader = new BufferedReader(new FileReader(path));
		String line;
		while((line = reader.readLine()) != null){
			if(line.isEmpty()) continue;
			try{
				double[] coordinates = Stream.of(line.split(" ")).mapToDouble(e -> Double.parseDouble(e)).toArray();
				segments.add(new Segment(status, segGroup, coordinates[0], coordinates[1], coordinates[2], coordinates[3]));
			}catch(Exception e){
				reader.close();
				throw new RuntimeException();
			}
		}
		reader.close();
		loadTimes++;
	}
	

	/**
	 * Saves the current map to the file
	 * @param path the absolute path of the map
	 * @throws IOException thrown when an IOException occurred
	 */
	public void saveMap(String path) throws IOException {
		PrintWriter writer;
		writer = new PrintWriter(path);
		for(Segment s : segments){
			double[] val = {s.getP1()._p.x, s.getP1()._p.y, s.getP2()._p.x, s.getP2()._p.y};
			String line = Arrays.stream(val)
			.mapToObj(i -> ((Double) i).toString())
			.collect(Collectors.joining(" "));
			writer.println(line);
		}
		writer.close();
	}
	
	public void addSegment(Segment s){
		if(loadTimes == 0) loadTimes = 1;
		segments.add(s);
	}
	
	public Status getStatus(){
		return status;
	}
	
	public void reset(){
		intersections.clear();
		eventQueue = new BalancedBinarySearchTree<>();
		status.clear();
	}
	
	public void clear(){
		reset();
		loadTimes = 0;
		segments.clear();
	}
	
	/**
	 * Get the left/right most segment of a set
	 * @param set the segments
	 * @param direction left/right most
	 * @return a segment
	 */
	private Segment findLeftRightMostSegment(HashSet<Segment> set, Direction direction){
		Segment leftRightMost = null;
		status.setInsertMode();
		for(Segment s : set){
			if(leftRightMost == null) leftRightMost = s;
			else if(direction == Direction.LEFT &&
					s.compareTo(leftRightMost) < 0){
				leftRightMost = s;
			}else if(direction == Direction.RIGHT &&
					s.compareTo(leftRightMost) > 0){
				leftRightMost = s;
			}
		}
		status.unsetInsertMode();
		return leftRightMost;
	}	
	
	public HashSet<Pair<ComparablePoint, HashSet<Segment>>> getIntersections(){
		return intersections;
	}
	
	public ArrayList<Segment> getSegments(){
		return segments;
	}
	
	/**
	 * This adds all the points of each segment to initialize the event queue
	 */
	public void findIntersections(){
		
		reset();
		
		
		// Add all the segments into the event queue
		for(Segment s : segments){
			
			ComparablePoint p1 = s.getP1();
			ComparablePoint p2 = s.getP2();
			
			ComparablePoint eventQueuePoint;
			
			
			if((eventQueuePoint = eventQueue.search(p1)) == null) eventQueue.insert(p1);
			else p1 = eventQueuePoint;
			if((eventQueuePoint = eventQueue.search(p2)) == null) eventQueue.insert(p2);
			else p2 = eventQueuePoint;
			
			
			
			if(s.getUpperEndpoint().compareTo(p1) == 0){
				p1.addLowerSegment(s);
				p2.addUpperSegment(s);
			}else{
				p1.addUpperSegment(s);
				p2.addLowerSegment(s);
			}
			
		}
		
	}
	
	public boolean handleEventPointManual(){
		if(!eventQueue.isEmpty()){
			ComparablePoint nextEventPoint = eventQueue.deleteMax();
			status.setEventPoint(nextEventPoint);
			handleEventPoint();
			return true;
		}else{
			return false;
		}
	}
	
	public void handleEventPointAuto(){
		while(!eventQueue.isEmpty()){
			ComparablePoint nextEventPoint = eventQueue.deleteMax();
			status.setEventPoint(nextEventPoint);
			handleEventPoint();
		}
		status.clear();
	}
	
	private void handleEventPoint(){
		
		ComparablePoint eventPoint = status.getEventPoint();
		
		
		HashSet<Segment> u = eventPoint.getLowerSegments();
		HashSet<Segment> l = eventPoint.getUpperSegments();
		HashSet<Segment> c = status.tree().findC(eventPoint);

		HashSet<Segment> luc = new HashSet<>();
		luc.addAll(l);
		luc.addAll(u);
		luc.addAll(c);
		
		
		HashSet<Segment> uc = new HashSet<>();
		uc.addAll(u);
		uc.addAll(c);
		
		if(luc.size() > 1){
			System.out.println("intersection point x: " + eventPoint._p.x + ", y:" + eventPoint._p.y);
			HashSet<Integer> differentGroups = new HashSet<>();
			for(Segment s : luc){
				differentGroups.add(s.getGroup());
			}
			
			if(loadTimes == 1 || differentGroups.size() > 1){
				intersections.add(new Pair<ComparablePoint, HashSet<Segment>>(eventPoint, luc));
			}
			
			
		}
		
		// Remove l
		for(Segment s : l){
			status.tree().delete(s);
		}
		
		// Remove c
		for(Segment s : c){
			status.tree().delete(s);
		}
		
		// insert c union u
		// Remove and insert c in order to swap the order.
		getStatus().setInsertMode();
		for(Segment s : uc){
			status.tree().insert(s);
		}
		getStatus().unsetInsertMode();
		
		if(uc.size() == 0){ // No segment at the bottom of p
			Segment leftNeighbour = status.tree().getNeighbour(eventPoint, Direction.LEFT);
			Segment rightNeighbour = status.tree().getNeighbour(eventPoint, Direction.RIGHT);
			findNewEvent(leftNeighbour, rightNeighbour, eventPoint);
		}else{ // intersection continues...
			Segment leftMost = findLeftRightMostSegment(uc, Direction.LEFT);
			Segment rightMost = findLeftRightMostSegment(uc, Direction.RIGHT);
			Segment leftNeighbour = status.tree().getNeighbour(eventPoint, Direction.LEFT);
			findNewEvent(leftNeighbour, leftMost, eventPoint);
			Segment rightNeighbour = status.tree().getNeighbour(eventPoint, Direction.RIGHT);
			findNewEvent(rightNeighbour, rightMost, eventPoint);
		}
		
		
	}
	
	private void findNewEvent(Segment left, Segment right, ComparablePoint p){
		if(left == null || right == null) return; // ensure both neighbours exist
		
		
		Point2D.Double intersectPoint = left.findIntersectionWithSegment(right);
		
		if(intersectPoint != null){
			// intersectPoint below sweep line or intersectPoint at the right of p
			if(
					Utilities.approxSmaller(intersectPoint.y, p._p.y) ||
					(Utilities.approxEqual(intersectPoint.y, p._p.y) && Utilities.approxGreater(intersectPoint.x, p._p.x))){
				ComparablePoint pp = new ComparablePoint(intersectPoint);
				if(eventQueue.search(pp) == null){
					System.out.println("Found Intersection, adding to event queue");
					System.out.println("intersection point x: " + intersectPoint.x + ", y:" + intersectPoint.y + "\n ------\n");
					eventQueue.insert(pp);
				}
			}
		}
	}

	public boolean isEmpty() {
		return segments.isEmpty();
	}
		
}
