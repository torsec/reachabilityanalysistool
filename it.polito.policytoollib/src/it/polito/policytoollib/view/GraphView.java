package it.polito.policytoollib.view;

import java.awt.Dimension;

import javax.swing.JFrame;

import org.jgraph.JGraph;
import org.jgrapht.DirectedGraph;
import org.jgrapht.ext.JGraphModelAdapter;

@SuppressWarnings("rawtypes")
public class GraphView {
	private DirectedGraph g;
	private JFrame frame;
//    private JGraphModelAdapter m_jgAdapter;
    private double width = 800, heigth = 600;
    ModifedForceDirectedLayout fr;
	
	public GraphView(DirectedGraph g){
		this.g = g;
		this.frame = new JFrame("Network Graph");
	}
	public GraphView(DirectedGraph g, String s){
		this.g = g;
		this.frame = new JFrame(s);
	}
	public GraphView(DirectedGraph g, double width, double heigth){
		this.g = g;
		this.frame = new JFrame("Network Graph");
		this.width = width;
		this.heigth = heigth;
		
	}

	
	@SuppressWarnings("unchecked")
	public void visualizeGraph(String title){
		JGraphModelAdapter m_jgAdapter = new JGraphModelAdapter( this.g );

        JGraph jgraph = new JGraph( m_jgAdapter );
		jgraph.addMouseWheelListener(new MouseWheelManager());
        fr = new ModifedForceDirectedLayout(jgraph, this.width, this.heigth-50);
        fr.run();
        frame.getContentPane(  ).add( jgraph );
        frame.pack();
        frame.setSize(new Dimension((int)this.width,(int)this.heigth));
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle(title);
	}
	
	public ModifedForceDirectedLayout getLayout(){
		return fr;
	}
	
}
