package Graphics;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Left side menu panel
 * @author moffa
 *
 */
public class MenuPanel extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final MainWindow window;
	final JButton loadButton = new JButton("Charger une carte");
	final JButton saveButton = new JButton("Sauvegarder une carte");
	final JButton findIntersections = new JButton("Trouver les intersections");
	final String showAllIntersectionsText = "Afficher toutes les intersections";
	final String showOnlyIntersectionsBetweenMapsText = "Afficher intersections entre cartes";
	final JButton showIntersections = new JButton(showOnlyIntersectionsBetweenMapsText);
	final JButton stepByStep = new JButton("Pas à pas");
	final JButton clearAllMaps = new JButton("Supprimer toutes les cartes");
	final String editModeText = "Passer en mode édition";
	final String noEditModeText = "Quitter le mode édition";
	final JButton editMode = new JButton(editModeText);
	final JSlider intersectionsInterval = new JSlider();
	final int yBorder = 1;
	final int internalPadding = 10;
	int animationInterval = 0;
	
	public MenuPanel(MainWindow w){
		super();
		window = w;
		setLayout(new GridLayout(0,1));
		addButtons();
		setBorder(new EmptyBorder(0, internalPadding, 0, internalPadding));
		setPreferredSize(new Dimension(300,0));
		
	}
	
	
	/**
	 * Add and set all of the buttons
	 */
	private void addButtons(){
		
		add(Box.createHorizontalGlue());
		
		add(Box.createVerticalGlue());
		
		add(loadButton);
		loadButton.addActionListener(this);
		add(Box.createRigidArea(new Dimension(0, yBorder)));
		
		add(saveButton);
		saveButton.addActionListener(this);
		add(Box.createRigidArea(new Dimension(0, yBorder)));
		
		
		findIntersections.addActionListener(this);
		add(findIntersections);
		add(Box.createRigidArea(new Dimension(0, yBorder)));
		
		intersectionsInterval.setOrientation(SwingConstants.HORIZONTAL);
		intersectionsInterval.setMinimum(0);
		intersectionsInterval.setMaximum(500);
		intersectionsInterval.setMinorTickSpacing(50);
		intersectionsInterval.setValue(animationInterval);
		intersectionsInterval.setPaintTicks(true);
		intersectionsInterval.setPaintLabels(true);
		intersectionsInterval.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				animationInterval = ((JSlider)e.getSource()).getValue();				
			}
		});
		add(intersectionsInterval);
		
		add(Box.createRigidArea(new Dimension(0, yBorder)));
		
		showIntersections.addActionListener(this);
		add(showIntersections);
		add(Box.createRigidArea(new Dimension(0, yBorder)));
		
		stepByStep.addActionListener(this);
		add(stepByStep);
		add(Box.createRigidArea(new Dimension(0, yBorder)));
		
		clearAllMaps.addActionListener(this);
		add(clearAllMaps);
		add(Box.createRigidArea(new Dimension(0, yBorder)));
		
		editMode.addActionListener(this);
		add(editMode);
		
		add(Box.createVerticalGlue());
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == loadButton){
			window.openLoadFileDialog();
		}else if(e.getSource() == findIntersections){
			window.findIntersections(animationInterval);
		}else if(e.getSource() == stepByStep){
			window.stepByStep();
		}else if(e.getSource() == clearAllMaps){
			window.clearAllMaps();
		}else if(e.getSource() == editMode){
			boolean editMode = !window.isEditMode();
			if(editMode){
				this.editMode.setText(noEditModeText);
			}else{
				this.editMode.setText(editModeText);
			}
			window.editMode(editMode);
		}else if(e.getSource() == saveButton){
			window.openSaveFileDialog();
		}else if(e.getSource() == showIntersections){
			boolean shouldShowOnlyIntersections = !window.shouldShowOnlyIntersections();
			if(shouldShowOnlyIntersections){
				this.showIntersections.setText(showAllIntersectionsText);
			}else{
				this.showIntersections.setText(showOnlyIntersectionsBetweenMapsText);
			}
			window.setShowOnlyIntersections(shouldShowOnlyIntersections);
		}
	}
	
}
