package it.polito.policytoollib.view;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import org.jgraph.JGraph;

public class MouseWheelManager implements MouseWheelListener {

	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.getSource() instanceof JGraph) {
			JGraph graph = (JGraph) e.getSource();
			int notches = -e.getWheelRotation();
			double newScale = Math.pow(1.1, notches) * graph.getScale();
	        if (notches > 0)
	            graph.setScale(Math.min(2,newScale), e
	                    .getPoint());
	        else
	            graph.setScale(Math.max(0.2, newScale));
	        // System.out.println(e.getPoint());
		}
        
	}

}