package Test;
import org.junit.Test;
import Default.ComparablePoint;
import Default.Segment;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import static org.junit.Assert.assertEquals;

public class SegmentTest {
	
	@Test
	public void horizontalTest(){
		ComparablePoint p = new ComparablePoint(new Double(5,10));
		ComparablePoint p2 = new ComparablePoint(new Double(10,10));
		Segment s = new Segment(null, 0, p, p2);
		assertTrue(s.isHorizontal());
	}
	
	@Test
	public void verticalTest(){
		ComparablePoint p = new ComparablePoint(new Double(50,10));
		ComparablePoint p2 = new ComparablePoint(new Double(50,20));
		Segment s = new Segment(null, 0, p, p2);
		assertTrue(s.isVertical());
	}
	
	@Test
	public void notVerticalnotHorizontalTest(){
		ComparablePoint p = new ComparablePoint(new Double(50,10));
		ComparablePoint p2 = new ComparablePoint(new Double(56,20));
		Segment s = new Segment(null, 0, p, p2);
		assertTrue(!(s.isVertical() && s.isHorizontal()));
	}
	
	@Test
	public void intersectNotHoriNotVerti(){
		ComparablePoint p = new ComparablePoint(new Double(20,40));
		ComparablePoint p2 = new ComparablePoint(new Double(40,20));
		
		ComparablePoint p3 = new ComparablePoint(new Double(10,10));
		ComparablePoint p4 = new ComparablePoint(new Double(40,40));
		Segment s = new Segment(null, 0, p, p2);
		Segment s2 = new Segment(null, 0, p3, p4);
		
		Point2D.Double normalIntersection = new Point2D.Double(30,30);
		
		Point2D.Double calculatedIntersection = s.findIntersectionWithSegment(s2);
		
		assertTrue(normalIntersection.equals(calculatedIntersection));
	}
	
	@Test
	public void intersectHori(){
		ComparablePoint p = new ComparablePoint(new Double(0,30));
		ComparablePoint p2 = new ComparablePoint(new Double(100,30));
		
		ComparablePoint p3 = new ComparablePoint(new Double(10,10));
		ComparablePoint p4 = new ComparablePoint(new Double(40,40));
		Segment s = new Segment(null, 0, p, p2);
		Segment s2 = new Segment(null, 0, p3, p4);
		
		Point2D.Double normalIntersection = new Point2D.Double(30,30);
		
		Point2D.Double calculatedIntersection = s.findIntersectionWithSegment(s2);
		
		assertTrue(normalIntersection.equals(calculatedIntersection));
	}
	
	@Test
	public void intersectVerti(){
		ComparablePoint p = new ComparablePoint(new Double(30,0));
		ComparablePoint p2 = new ComparablePoint(new Double(30,100));
		
		ComparablePoint p3 = new ComparablePoint(new Double(10,10));
		ComparablePoint p4 = new ComparablePoint(new Double(40,40));
		Segment s = new Segment(null, 0, p, p2);
		Segment s2 = new Segment(null, 0, p3, p4);
		
		Point2D.Double normalIntersection = new Point2D.Double(30,30);
		
		Point2D.Double calculatedIntersection = s.findIntersectionWithSegment(s2);
		
		assertTrue(normalIntersection.equals(calculatedIntersection));
	}
	
	@Test
	public void notIntersectVerti(){
		ComparablePoint p = new ComparablePoint(new Double(30,50));
		ComparablePoint p2 = new ComparablePoint(new Double(30,100));
		
		ComparablePoint p3 = new ComparablePoint(new Double(10,10));
		ComparablePoint p4 = new ComparablePoint(new Double(40,40));
		Segment s = new Segment(null, 0, p, p2);
		Segment s2 = new Segment(null, 0, p3, p4);
		
		Point2D.Double normalIntersection = new Point2D.Double(30,30);
		
		Point2D.Double calculatedIntersection = s.findIntersectionWithSegment(s2);
		
		assertFalse(normalIntersection.equals(calculatedIntersection));
	}
	
	@Test
	public void notIntersectHori(){
		ComparablePoint p = new ComparablePoint(new Double(50,30));
		ComparablePoint p2 = new ComparablePoint(new Double(100,30));
		
		ComparablePoint p3 = new ComparablePoint(new Double(10,10));
		ComparablePoint p4 = new ComparablePoint(new Double(40,40));
		Segment s = new Segment(null, 0, p, p2);
		Segment s2 = new Segment(null, 0, p3, p4);
		
		Point2D.Double normalIntersection = new Point2D.Double(30,30);
		
		Point2D.Double calculatedIntersection = s.findIntersectionWithSegment(s2);
		
		assertFalse(normalIntersection.equals(calculatedIntersection));
	}
	
	@Test
	public void notIntersectNormal(){
		ComparablePoint p = new ComparablePoint(new Double(100,90));
		ComparablePoint p2 = new ComparablePoint(new Double(87,52));
		
		ComparablePoint p3 = new ComparablePoint(new Double(10,10));
		ComparablePoint p4 = new ComparablePoint(new Double(40,40));
		Segment s = new Segment(null, 0, p, p2);
		Segment s2 = new Segment(null, 0, p3, p4);
		
		Point2D.Double normalIntersection = new Point2D.Double(30,30);
		
		Point2D.Double calculatedIntersection = s.findIntersectionWithSegment(s2);
		
		assertFalse(normalIntersection.equals(calculatedIntersection));
	}
	
	@Test
	public void mightContainPointVertical(){
		ComparablePoint p = new ComparablePoint(new Double(100,70));
		ComparablePoint pI = new ComparablePoint(new Double(100,162));
		Segment s = new Segment(null, 0, p, pI);
		
		
		ComparablePoint p1 = new ComparablePoint(new Double(100, 170));
		ComparablePoint p2 = new ComparablePoint(new Double(100, 150));
		ComparablePoint p3 = new ComparablePoint(new Double(100, 70));
		ComparablePoint p4 = new ComparablePoint(new Double(100, 162));
		ComparablePoint p5 = new ComparablePoint(new Double(100, 100));
		ComparablePoint p6 = new ComparablePoint(new Double(105, 80));
		ComparablePoint p7 = new ComparablePoint(new Double(20, 20));
		
		assertFalse(s.mightContainPoint(p1));
		assertTrue(s.mightContainPoint(p2));
		assertTrue(s.mightContainPoint(p3));
		assertTrue(s.mightContainPoint(p4));
		assertTrue(s.mightContainPoint(p5));
		assertFalse(s.mightContainPoint(p6));
		assertFalse(s.mightContainPoint(p7));
		
	}
	
	@Test
	public void mightContainPointHorizontal(){
		ComparablePoint p = new ComparablePoint(new Double(70, 100));
		ComparablePoint pI = new ComparablePoint(new Double(162, 100));
		Segment s = new Segment(null, 0, p, pI);
		
		
		ComparablePoint p1 = new ComparablePoint(new Double(170, 100));
		ComparablePoint p2 = new ComparablePoint(new Double(150, 100));
		ComparablePoint p3 = new ComparablePoint(new Double(70, 100));
		ComparablePoint p4 = new ComparablePoint(new Double(162, 100));
		ComparablePoint p5 = new ComparablePoint(new Double(100, 100));
		ComparablePoint p6 = new ComparablePoint(new Double(105, 80));
		ComparablePoint p7 = new ComparablePoint(new Double(20, 20));
		
		assertFalse(s.mightContainPoint(p1));
		assertTrue(s.mightContainPoint(p2));
		assertTrue(s.mightContainPoint(p3));
		assertTrue(s.mightContainPoint(p4));
		assertTrue(s.mightContainPoint(p5));
		assertFalse(s.mightContainPoint(p6));
		assertFalse(s.mightContainPoint(p7));
		
	}
	
	@Test
	public void yIntersectNormal(){
		ComparablePoint p = new ComparablePoint(new Double(10, 10));
		ComparablePoint pI = new ComparablePoint(new Double(100, 100));
		Segment s = new Segment(null, 0, p, pI);
		
		assertEquals(50, s.findIntersectionWithY(50), 0.005);
	}
	
	@Test(expected = RuntimeException.class)
	public void yIntersectHorizontal(){
		ComparablePoint p = new ComparablePoint(new Double(70, 100));
		ComparablePoint pI = new ComparablePoint(new Double(162, 100));
		Segment s = new Segment(null, 0, p, pI);
		
		s.findIntersectionWithY(100);
		s.findIntersectionWithY(50);
	}
	
	@Test
	public void yIntersectVertical(){
		ComparablePoint p = new ComparablePoint(new Double(100, 70));
		ComparablePoint pI = new ComparablePoint(new Double(100, 162));
		Segment s = new Segment(null, 0, p, pI);
		
		assertEquals(100, s.findIntersectionWithY(50), 0.005);
	}
}
