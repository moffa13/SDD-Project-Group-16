package Test;

import org.junit.Test;
import Default.ComparablePoint;
import java.awt.geom.Point2D;
import static org.junit.Assert.assertEquals;

public class ComparablePointTest {
	@Test
	public void upperPointTest(){
		// A point
		ComparablePoint p = new ComparablePoint(new Point2D.Double(10,10));
		// upper to p
		ComparablePoint p2 = new ComparablePoint(new Point2D.Double(10,11));
		// upper to p but < x
		ComparablePoint p3 = new ComparablePoint(new Point2D.Double(5,11));
		// upper to p but > x
		ComparablePoint p4 = new ComparablePoint(new Point2D.Double(15,11));
		
		ComparablePoint p5 = new ComparablePoint(new Point2D.Double(10,9));
		ComparablePoint p6 = new ComparablePoint(new Point2D.Double(5,9));
		ComparablePoint p7 = new ComparablePoint(new Point2D.Double(15,9));
		
		ComparablePoint p8 = new ComparablePoint(new Point2D.Double(10,10));
		ComparablePoint p9 = new ComparablePoint(new Point2D.Double(5,10));
		ComparablePoint p10 = new ComparablePoint(new Point2D.Double(15,10));
		
		assertEquals(1, p2.compareTo(p));
		assertEquals(1, p3.compareTo(p));
		assertEquals(1, p4.compareTo(p));
		
		assertEquals(-1, p5.compareTo(p));
		assertEquals(-1, p6.compareTo(p));
		assertEquals(-1, p7.compareTo(p));
		
		assertEquals(0, p8.compareTo(p));
		assertEquals(1, p9.compareTo(p));
		assertEquals(-1, p10.compareTo(p));
	}
}
