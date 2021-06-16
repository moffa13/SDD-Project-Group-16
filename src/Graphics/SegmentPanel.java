package Graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import javax.swing.JPanel;
import Default.ComparablePoint;
import Default.Map;
import Default.Pair;
import Default.Segment;
import Default.Utilities;

public class SegmentPanel extends JPanel implements MouseListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map _m;
	private final static int scale = 3;
	private final MainWindow _window;
	private boolean _editMode = false;
	
	// Used to create segments in edit mode
	private ComparablePoint p1;
	private ComparablePoint p2;
	
	private double getOffset(){
		return getHeight() / scale;
	}
	
	public SegmentPanel(MainWindow w) {
		super();
		_window = w;
		setBackground(Color.lightGray);
		addMouseListener(this);
	}
	
	public void setMap(Map m){
		_m = m;
		repaint();
	}
	
	public void drawSegment(Segment s, Graphics2D g2d){
		double x1 = s.getP1()._p.x;
		double reversedY1 = getOffset() - s.getP1()._p.y;
		double x2 = s.getP2()._p.x;
		double reversedY2 = getOffset() - s.getP2()._p.y; 
		
		g2d.draw(new Line2D.Double(x1, reversedY1, x2, reversedY2));
	}
	
	public void drawCross(int w, int h, Point2D.Double point, Graphics2D g2d){
		g2d.draw(new Line2D.Double(point.x - w / 2, getOffset() - point.y, point.x + w / 2, getOffset() - point.y));
		g2d.draw(new Line2D.Double(point.x, getOffset() - point.y - h / 2, point.x, getOffset() - point.y + h / 2));
	}
	
	public void drawPoint(ComparablePoint s, Graphics2D g2d){
		Rectangle2D.Double rect = new Rectangle2D.Double(s._p.x - 1, getOffset() - s._p.y - 1, 2, 2);
		g2d.draw(rect);
	}
	

	@Override
	/**
	 * Paints all the needed elements,
	 * intersections, segments, sweep line, C(p), U(p), L(p)
	 */
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		if(_m != null){
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
			AffineTransform at = new AffineTransform();
			at.scale(scale, scale);
			g2d.transform(at);
			
			
			HashSet<Segment> intersectSegments = new HashSet<>();
			
			// Intersections
			g2d.setColor(Color.GREEN);
			for(Pair<ComparablePoint, HashSet<Segment>> s : _m.getIntersections()){
				if(_window.shouldShowOnlyIntersections()){
					intersectSegments.addAll(s.getValue());
				}
			}
			
			// Segments
			for(Segment s : _window.shouldShowOnlyIntersections() ? intersectSegments : _m.getSegments()){
				if(_m.getStatus().tree().search(s) != null) {
					g2d.setColor(Color.WHITE);
				}else {
					g2d.setColor(_window.getColor(s));
				}
				drawSegment(s, g2d);
			}
			
			// Draw Intersections
			g2d.setColor(Color.GREEN);
			for(Pair<ComparablePoint, HashSet<Segment>> s : _m.getIntersections()){
				drawPoint(s.getKey(), g2d);
			}
			
			// Sweep line
			g2d.setColor(Color.RED);
			g2d.draw(new Line2D.Double(0, getOffset() - _m.getStatus().getSweepLinePosition(), getWidth(), getOffset() - _m.getStatus().getSweepLinePosition()));
			
			
			// Draw p1 when edit mode
			if(p1 != null){
				drawCross(5, 5, p1.toPoint(), g2d);
			}
			
		
			
			if(_m.getStatus().getEventPoint() != null){
				
				
				// Event point
				g2d.setColor(Color.PINK);
				drawPoint(_m.getStatus().getEventPoint(), g2d);
				
				// C(p)
				g2d.setColor(Color.ORANGE);
				for(Segment s : _m.getStatus().tree().findC(_m.getStatus().getEventPoint())){
					drawSegment(s, g2d);
				}
				
				
				// U(p)
				g2d.setColor(Color.BLUE);
				for(Segment s : _m.getStatus().getEventPoint().getLowerSegments()){
					drawSegment(s, g2d);
				}
				
				// L(p)
				g2d.setColor(Color.PINK);
				for(Segment s : _m.getStatus().getEventPoint().getUpperSegments()){
					drawSegment(s, g2d);
				}
			}
			
			
		}
		
	}

	public boolean getEditMode() {
		return _editMode;
	}

	public void setEditMode(boolean editMode) {
		p1 = null;
		p2 = null;
		_editMode = editMode;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(_editMode){
			double x = e.getX() / scale;
			double y = (getHeight() - e.getY()) /scale;
			
			if(p1 == null){
				p1 = new ComparablePoint(new Point2D.Double(x, y));
			}else if(p2 == null){
				p2 = new ComparablePoint(new Point2D.Double(x, y));
				
				if(p1.compareTo(p2) != 0){
					_window.addSegment(p1, p2);
				}
				
				p1 = null;
				p2 = null;
			}
			repaint();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
