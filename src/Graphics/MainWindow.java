package Graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.Stack;
import javax.swing.Timer;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import Default.ComparablePoint;
import Default.Map;
import Default.Segment;

public class MainWindow extends JFrame implements ActionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final long colorSeed = 547534L;
	private final Random rand = new Random(colorSeed);
	private JMenuItem _loadFile;
	private Map _map = new Map();
	private SegmentPanel _segPanel = new SegmentPanel(this);
	private MenuPanel _menuPanel = new MenuPanel(this);
	private int segGroup = 0;
	private HashMap<Integer, Color> groupColors = new HashMap<>();
	private Stack<Color> userColors = new Stack<>();
	private boolean showOnlyIntersections = false;
	private Timer _t;
	
	
	public MainWindow(){
		
		super();
		
		setTitle("Projet SDD II Groupe 16");
		
		populateUserColors();
		
		setSize(1380, 800);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loadMenu();
		
		
		
		add(_segPanel, BorderLayout.CENTER);
		_segPanel.setMap(_map);
		
		add(_menuPanel, BorderLayout.LINE_START);
		setVisible(true);
	}
	
	/**
	 * Add some user-defined color instead of random
	 */
	private void populateUserColors(){
		userColors.add(Color.WHITE);
		userColors.add(Color.BLACK);		
	}
	
	/**
	 * Set whether or not we should only show segment that intersect between the groups 
	 * @param v the boolean
	 */
	public void setShowOnlyIntersections(boolean v){
		showOnlyIntersections = v;
		repaint();
	}
	
	/**
	 * Whether or not we should only show segment that intersect between the groups
	 * @return
	 */
	public boolean shouldShowOnlyIntersections(){
		return showOnlyIntersections;
	}
	
	
	/**
	 * Get a random color
	 * Used to color different groups of segments
	 * Uses first a user determined set of colors so that first cases uses pretty ones
	 * Then picks random colors in a deterministic way
	 * @return color
	 */
	private Color getRandomColor(){
		
		if(!userColors.isEmpty())
			return userColors.pop();
		
		float r = rand.nextFloat();
		float g = rand.nextFloat();
		float b = rand.nextFloat();
		return new Color(r, g, b);
	}
	
	/**
	 * Load the menubar
	 */
	private void loadMenu(){
		JMenuBar menuBar = new JMenuBar();
		
		JMenu menu = new JMenu("Fichier");
		_loadFile = new JMenuItem("Charger une carte");
		_loadFile.addActionListener(this);
		
		menu.add(_loadFile);
		
		
		menuBar.add(menu);
		
		setJMenuBar(menuBar);
	}
	
	/**
	 * Set the window's edit mode
	 * @param editMode A boolean
	 */
	public void editMode(boolean editMode){
		_segPanel.setEditMode(editMode);
	}
	
	/**
	 * Check if the window is in edit mode
	 * @return A boolean
	 */
	public boolean isEditMode(){
		return _segPanel.getEditMode();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		// We clicked on load map button
		if(e.getSource() == _loadFile){
			openLoadFileDialog();
		}
		
	}
	
	/**
	 * Find one intersection every time unit
	 * @param interval The interval in ms, if interval is 0, show all intersections at once
	 */
	public void findIntersections(int interval){
		if(_map != null){
			
			ActionListener listener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean notEmpty = _map.handleEventPointManual();
					if(!notEmpty)
						((Timer)e.getSource()).stop();
					MainWindow.this.repaint();
					
				}
			};
			
			_map.findIntersections();
			if(interval == 0){
				_map.handleEventPointAuto();
				repaint();
			}else{
				if(_t != null){
					_t.stop();
				}
				_t = new Timer(interval, listener);
				_t.start();
			}			
		}
	}
	
	/**
	 * Reset the map with colors
	 */
	public void clearAllMaps(){
		_map.clear();
		segGroup = 0;
		userColors.clear();
		groupColors.clear();
		populateUserColors();
		repaint();
	}

	/**
	 * Open a file dialog and lets you select a map
	 * Then loads the map file.
	 * Shows a visual error when the map could not be loaded
	 */
	public void openLoadFileDialog() {
		FileDialog fd = new FileDialog(this, "Choisir une carte à ouvrir", FileDialog.LOAD);
		fd.setDirectory(new JFileChooser().getFileSystemView().getDefaultDirectory().toString());
		fd.setVisible(true);
		String filename = fd.getFile();
		if(filename != null){
			try {
				Color c = getRandomColor();
				_map.loadMap(fd.getDirectory() + filename, segGroup);
				groupColors.put(segGroup, c);
				segGroup++;
				repaint();
			} catch (IOException | RuntimeException e) {
				JOptionPane.showMessageDialog(this, "Impossible de charger la carte.");
			}
		}
	}
	
	/**
	 *  Open a file dialog and lets you select a map file where to write
	 *  Shows a visual error when the map could not be saved
	 */
	public void openSaveFileDialog() {
		
		if(_map.isEmpty()){
			JOptionPane.showMessageDialog(this, "La carte est vide.");
			return;
		}
		
		FileDialog fd = new FileDialog(this, "Choisir une carte à ouvrir", FileDialog.SAVE);
		fd.setDirectory(new JFileChooser().getFileSystemView().getDefaultDirectory().toString());
		fd.setVisible(true);
		String filename = fd.getFile();
		if(filename != null){
			try {
				_map.saveMap(fd.getDirectory() + filename);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, "Impossible d'enregistrer la carte.");
			}
		}
	}
	
	/**
	 * Return the color associated with the group the segment's in
	 * @param s The segment we want to check
	 * @return A color
	 */
	public Color getColor(Segment s){
		return groupColors.get(s.getGroup());
	}

	/**
	 * Manually find intersection one by one
	 */
	public void stepByStep() {
		if(_map == null) return;
		
		if(_map.getStatus().getEventPoint() == null){
			_map.findIntersections();
		}
		
		if(!_map.handleEventPointManual()){
			_map.findIntersections();
		}
		repaint();
		
	}
	
	/**
	 * Add a segment to the window from its points
	 * @param p1 The first point
	 * @param p2 The second point
	 */
	public void addSegment(ComparablePoint p1, ComparablePoint p2) {
		Segment s = new Segment(_map.getStatus(), segGroup, p1, p2);
		_map.addSegment(s);
	}
}




