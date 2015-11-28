package montecarlo;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.text.NumberFormatter;

/**
 * Diese Klasse ist die Hauptklasse der Monte-Carlo-Simulation, welche die Punkte generiert und auch graphisch darstellt.
 * 
 * @author Lukas Schramm
 * @version 1.0
 * 
 */
public class MonteCarlo {
	
	private JFrame frame1 = new JFrame("Monte-Carlo-Simulation");
	private Canvas canvas = new Canvas() {
		public void paint(Graphics gr) {
			zeichne(gr);
		}
	};
	private JButton buttonGenerieren = new JButton("Zufallswerte");
	private NumberFormat format = NumberFormat.getInstance(); 
	private NumberFormatter formatter = new NumberFormatter(format);
	private JFormattedTextField anzahlPunkte = new JFormattedTextField(formatter);
	private JList<String> punkteListe = new JList<String>();
	private DefaultListModel<String> punkteListeModel = new DefaultListModel<String>();
	private JScrollPane punkteListeScrollPane = new JScrollPane(punkteListe);
	private JLabel labelAusgabe = new JLabel();
	private JCheckBox zeichnenCheck = new JCheckBox("Zeichnen");
	private ArrayList<Punkt> punkte = new ArrayList<Punkt>();
	
	public MonteCarlo() {
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame1.setPreferredSize(new Dimension(530,400));
		frame1.setMinimumSize(new Dimension(530,400));
		frame1.setMaximumSize(new Dimension(1060,800));
		frame1.setResizable(true);
		Container cp = frame1.getContentPane();
		cp.setLayout(new GridBagLayout());
		
		JPanel rechts = new JPanel();
		rechts.setLayout(new BorderLayout());
		JPanel temp1 = new JPanel();
		temp1.setLayout(new BorderLayout());
		temp1.add(anzahlPunkte,BorderLayout.WEST);
		temp1.add(buttonGenerieren,BorderLayout.CENTER);
		JPanel temp2 = new JPanel();
		temp2.setLayout(new BorderLayout());
		temp2.add(temp1,BorderLayout.NORTH);
		temp2.add(zeichnenCheck,BorderLayout.CENTER);
		JPanel temp3 = new JPanel();
		temp3.setLayout(new BorderLayout());
		temp3.add(temp2,BorderLayout.NORTH);
		temp3.add(labelAusgabe,BorderLayout.CENTER);
		rechts.add(temp3,BorderLayout.NORTH);
		rechts.add(punkteListeScrollPane,BorderLayout.CENTER);
	    
	    anzahlPunkte.setText("10000");
	    buttonGenerieren.setMargin(new Insets(2, 2, 2, 2));
	    buttonGenerieren.addActionListener(new ActionListener() { 
	    	public void actionPerformed(ActionEvent evt) { 
	    		berechnen();
	    	}
	    });
		
	    zeichnenCheck.setHorizontalAlignment(SwingConstants.CENTER);
	    labelAusgabe.setHorizontalAlignment(SwingConstants.CENTER);
	    format.setGroupingUsed(false);
	    formatter.setAllowsInvalid(false);
	    frame1.add(canvas,new GridBagFelder(0,0,1,1,0.7,1));
	    frame1.add(rechts,new GridBagFelder(1,0,1,1,0.3,1));
	    punkteListe.setModel(punkteListeModel);
	    canvas.setPreferredSize(new Dimension(0,0));
	    rechts.setPreferredSize(new Dimension(0,0));
		frame1.pack();
	    frame1.setLocationRelativeTo(null);
		frame1.setVisible(true);
	}
	
	/**
	 * Diese Methode generiert eine neue Reihe an Punkten, traegt sie in die Liste ein und loest eine Neuzeichnung des Canvas aus.
	 * Des weiteren berechnet sie anhand der Menge an Punkten die im Viertelkreis gelandet sind einen Naeherungswert fuer Pi.
	 */
	private void berechnen() {
		try {
			punkte.clear();
			punkteListeModel.clear();
			Random wuerfel = new Random();
			if(Integer.parseInt(anzahlPunkte.getText())<0) {
				JOptionPane.showMessageDialog(null, "Du hast falsche Werte eingetragen."+System.getProperty("line.separator")+"Wenn Du dies nicht korrigierst"+System.getProperty("line.separator")+"bekommst Du kein Ergebnis!", "Falscheingabe", JOptionPane.WARNING_MESSAGE);
			} else {
				int anzahl = Integer.parseInt(anzahlPunkte.getText());
			    int innen = 0;
				for(int n=0;n<anzahl;n++) {;
					punkte.add(new Punkt(wuerfel.nextDouble(),wuerfel.nextDouble()));
				}
				for(Punkt p:punkte) {
					punkteListeModel.addElement("P("+Math.round(p.getX()*1000.0)/10.0+"|"+Math.round(p.getY()*1000.0)/10.0+")");
					double s = Math.sqrt(Math.pow(p.getX(),2)+Math.pow(p.getY(),2));/**Abstand*/
					if(s<=1) {
						innen++;
				    }
				}
			    labelAusgabe.setText("≈Pi: "+4.0*innen/anzahl);
			    labelAusgabe.setToolTipText("Annäherung für Pi");
				canvas.repaint();
			}
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Du hast falsche Werte eingetragen."+System.getProperty("line.separator")+"Wenn Du dies nicht korrigierst"+System.getProperty("line.separator")+"bekommst Du kein Ergebnis!", "Falscheingabe", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	/**
	 * Diese Methode berechnet die Koordinaten der Punkte im Canvas und zeichnet sie.
	 * @param gr Nimmt GraphicsElement entgegen.
	 */
	private void zeichne(Graphics gr) {
		int breite = canvas.getWidth();
		int hoehe = canvas.getHeight();
		gr.drawArc(-breite,0,2*breite,2*hoehe,0,90);
		if(zeichnenCheck.isSelected()) {
			for(Punkt p:punkte) {
				double xp = p.getX();
				double yp = -p.getY();
				double x = (Double.valueOf(breite))*xp;
				double y = -(Double.valueOf(hoehe))*yp;
				gr.drawLine((int)x-3,(int)y-3,(int)x+3,(int)y+3);
				gr.drawLine((int)x-3,(int)y+3,(int)x+3,(int)y-3);
			}
		}
	}
	
	public static void main(String[] args) {
		new MonteCarlo();
	}
}