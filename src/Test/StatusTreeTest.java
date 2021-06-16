package Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.geom.Point2D.Double;
import java.util.HashSet;

import org.junit.Test;

import Default.ComparablePoint;
import Default.Segment;
import Default.Status;
import Default.StatusTree;

public class StatusTreeTest {
	@Test
	public void findCTest() {
		
		Status status = new Status();
		ComparablePoint iPoint = new ComparablePoint(new Double(50, 50));
		status.setEventPoint(iPoint);
		
		StatusTree t = new StatusTree(status);
		
		ComparablePoint p = new ComparablePoint(new Double(10, 10));
		ComparablePoint p2 = new ComparablePoint(new Double(100, 100));
		Segment s = new Segment(status, 0, p, p2);
		
		assertEquals(50, s.findIntersectionWithX(50), .005); // Point in 50,50
		
		ComparablePoint p3 = new ComparablePoint(new Double(50, 50));
		ComparablePoint p4 = new ComparablePoint(new Double(50, 0));
		Segment s2 = new Segment(status, 0, p3, p4);
		
		ComparablePoint p5 = new ComparablePoint(new Double(50, 50));
		ComparablePoint p6 = new ComparablePoint(new Double(50, 100));
		Segment s3 = new Segment(status, 0, p5, p6);
		
		t.insert(s2);
		t.insert(s3);
		
		
		HashSet<Segment> res;
		
		
		res = t.findC(iPoint);
		assertEquals(0, res.size());
		
		
		t.insert(s);
		
		res = t.findC(iPoint);
		
		assertEquals(1, res.size());
		assertEquals(s, res.iterator().next());
		
	}
	
	
	@Test
	public void insertModeTest() {
		
		Status status = new Status();
		ComparablePoint iPoint = new ComparablePoint(new Double(50, 50));
		status.setEventPoint(iPoint);
		
		StatusTree t = new StatusTree(status);
		
		ComparablePoint p = new ComparablePoint(new Double(0, 0));
		ComparablePoint p2 = new ComparablePoint(new Double(100, 100));
		Segment s = new Segment(status, 0, p, p2);
		
		ComparablePoint p3 = new ComparablePoint(new Double(0, 100));
		ComparablePoint p4 = new ComparablePoint(new Double(100, 0));
		Segment s2 = new Segment(status, 0, p3, p4);
		
		assertEquals(50, s2.findIntersectionWithSegment(s).x, .0005);
		assertEquals(50, s2.findIntersectionWithSegment(s).y, .0005);
		
		ComparablePoint p5 = new ComparablePoint(new Double(0, 0));
		ComparablePoint p6 = new ComparablePoint(new Double(0, 100));
		Segment s3 = new Segment(status, 0, p5, p6);
		
		t.insert(s);
		t.insert(s2);
		t.insert(s3);
		
		assertTrue(s3.compareTo(s2) < 0);
		assertTrue(s3.compareTo(s) < 0);
		assertTrue(s2.compareTo(s) < 0);
		
		status.setInsertMode();
		assertTrue(s3.compareTo(s2) < 0);
		assertTrue(s3.compareTo(s) < 0);
		assertTrue(s2.compareTo(s) > 0);
		
	}
	
	
}
