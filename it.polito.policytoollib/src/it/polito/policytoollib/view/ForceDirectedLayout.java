package it.polito.policytoollib.view;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.jgraph.JGraph;
import org.jgraph.graph.GraphCell;
import org.jgraph.graph.GraphConstants;

/**
 * 
 * @author Paolo C. Pomi
 */
public class ForceDirectedLayout implements Runnable {

	/**
	 *  *
	 */
	private JGraph vGraph;

	private double springLength = 100;

	private double stiffness = 50;

	private double electricalRepulsion = 5000;

	private double increment = 0.50;

	private boolean initialized = false;

	private Thread runner;

	private ArrayList<GraphCell> fixedVertexList;

	double width = 800;

	double height = 600;

	/**
	 * 
	 * @param vGraph
	 *            the jGraph on which applying layout
	 * @param width
	 *            horizontal space on which the graph can be viewed in px
	 * @param height
	 *            vertical space on which the graph can be viewed in px
	 */
	public ForceDirectedLayout(JGraph vGraph, double width, double height) {
		this.vGraph = vGraph;
		this.fixedVertexList = new ArrayList<GraphCell>(10);
		this.width = width;
		this.height = height;
	}

	/**
	 * Returns the desired spring length for all edges. The default value is 50.
	 * 
	 */
	public double getSpringLength() {
		return this.springLength;
	}

	/**
	 * Returns the stiffness for all edges. The default value is 50.
	 * 
	 */
	public double getStiffness() {
		return this.stiffness;
	}

	/**
	 * Returns the eletrical repulsion between all vertices The default value is
	 * 400.
	 */
	public double getEletricalRepulsion() {
		return this.electricalRepulsion;
	}

	/**
	 * Returns the increment by which the vertices gets closer to the
	 * equilibrium or closer to the force. The default value is 0.50.
	 * 
	 */
	public double getIncrement() {
		return this.increment;
	}

	/**
	 * Sets the desired length of the spring among all edges
	 * 
	 */
	public void setSpringLength(double length) {
		this.springLength = length;
	}

	/**
	 * Sets the value of stiffness among all edges
	 * 
	 */
	public void setStiffness(double stiffness) {
		this.stiffness = stiffness;
	}

	/**
	 * Sets the value of the electrical repulsion between all vertices
	 */
	public void setEletricalRepulsion(double repulsion) {
		this.electricalRepulsion = repulsion;
	}

	/**
	 * Sets the increment by which the vertices gets close to the equilibrium or
	 * gets closer to the direction of the force. This must be a number > 0 and <=
	 * 1. The higher the value, the faster the layout reaches equilibrium.
	 * 
	 */
	public void setIncrement(double increment) {
		this.increment = increment;
	}

	/**
	 * Adds a <tt>VisuaLVertex</tt> that will not be moved from its position
	 * during the layout operation of <tt>ForceDirectedLayout</tt>.
	 */
	public void addFixedVertex(GraphCell vVertex) {
		this.fixedVertexList.add(vVertex);
	}

	/**
	 * Returns <tt>true</tt> if the specified <tt>VisualVertex</tt> has a
	 * fixed position.
	 */
	public boolean isVertexFixed(GraphCell vVertex) {
		return this.fixedVertexList.contains(vVertex);
	}

	/**
	 * Removes a <tt>VisualVertex</tt> from the list of
	 * <tt>VisualVertices</tt> that has a fixed position.
	 */
	public void removeFixedVertex(GraphCell vVertex) {
		this.fixedVertexList.remove(vVertex);
	}

	/**
	 * Determines if the graph has been initially laid out. This method should
	 * be called prior to any painting to be done by the graph layout manager,
	 * as most internal variables are only initialized during layout.
	 * 
	 * @return True if the graph has at least been laid out once.
	 * 
	 */
	public boolean isInitialized() {
		return initialized;
	}

	/*
	 * public void paintEdge(Graphics2D g2d, GraphCell vEdge) {
	 * this.routeEdge(g2d, vEdge); }
	 * 
	 * public void routeEdge(Graphics2D g2d, GraphCell vEdge) { GeneralPath
	 * gPath = vEdge.getGeneralPath();
	 * 
	 * g2d.setColor(vEdge.getOutlinecolor());
	 * 
	 * Point2D.Float fromcenter = new Point2D.Float(new
	 * Double(vEdge.getVisualVertexA().getBounds2D().getCenterX())
	 * .floatValue(), new
	 * Double(vEdge.getVisualVertexA().getBounds2D().getCenterY()).floatValue());
	 * Point2D.Float tocenter = new Point2D.Float(new
	 * Double(vEdge.getVisualVertexB().getBounds2D().getCenterX())
	 * .floatValue(), new
	 * Double(vEdge.getVisualVertexB().getBounds2D().getCenterY()).floatValue());
	 * 
	 * gPath.reset(); gPath.moveTo((float) (fromcenter.x), (float)
	 * (fromcenter.y)); gPath.lineTo((float) (tocenter.x), (float)
	 * (tocenter.y)); }
	 */
	/**
	 * This method is called to layout the vertices in the graph, running a
	 * thread to perform the layout if the thread is not running, or stopping
	 * the thread if the thread is running.
	 */
	public void layout() {
		this.initialized = true;
		if (this.runner != null) {
			this.runner = null;
		} else {
			this.runner = new Thread(this);
			this.runner.start();
		}
	}

	Rectangle2D gRect;

	/**
	 * "Relax" the force on the VisualVertex. "Relax" here means to get closer
	 * to the equilibrium position.
	 * <p>
	 * This method will:
	 * <ul>
	 * <li>Find the spring force between all of its adjacent VisualVertices
	 * </li>
	 * <li>Get the electrical repulsion between all VisualVertices, including
	 * those which are not adjacent.</li>
	 * </ul>
	 */
	HashMap<GraphCell, List<GraphCell>> edgesByVertices = new HashMap<GraphCell, List<GraphCell>>();

	private void relax(GraphCell vVertex, Map<GraphCell, Map<?, ?>> h) {
		// System.out.print("@");
		Map<?, ?> amap;
		if (h.containsKey(vVertex))
			amap = h.get(vVertex);
		else
			amap = vVertex.getAttributes();

		if (GraphConstants.getBounds(amap) == null)
			return;
		// If the VisualVertex is fixed, don't do anything.
		if (this.fixedVertexList.contains(vVertex)) {
			return;
		}

		double xForce = 0;
		double yForce = 0;

		double distance;
		double spring;
		double repulsion;
		double xSpring = 0;
		double ySpring = 0;
		double xRepulsion = 0;
		double yRepulsion = 0;

		// double adjacentDistance = 0;

		double adjX;
		double adjY;

		double thisX = GraphConstants.getBounds(amap).getCenterX();
		double thisY = GraphConstants.getBounds(amap).getCenterY();

		// List adjacentVertices;
		GraphCell adjacentVisualVertex;
		int i, size;
		List<GraphCell> listEdges;

		if (edgesByVertices.containsKey(vVertex)) {
			listEdges = (List<GraphCell>) edgesByVertices.get(vVertex);
		} else {
			listEdges = new ArrayList<GraphCell>();
			int numChildren = vGraph.getModel().getChildCount(vVertex);
			for (int j = 0; j < numChildren; j++) {
				Object port = vGraph.getModel().getChild(vVertex, j);
				if (vGraph.getModel().isEdge(port))
					listEdges.add((GraphCell) port);
				if (vGraph.getModel().isPort(port)) {
					Iterator<?> iter = vGraph.getModel().edges(port);
					while (iter.hasNext())
						listEdges.add((GraphCell) iter.next());

				}
			}
			edgesByVertices.put(vVertex, listEdges);
		}
		// Get the spring force between all of its adjacent vertices.
		Iterator<GraphCell> iterEdges = listEdges.iterator();

		// adjacentVertices =
		// this.vGraph.getAdjacentVertices(vVertex.getVertex());
		// size = adjacentVertices.size();

		while (iterEdges.hasNext()) {
			Object port = iterEdges.next();
			adjacentVisualVertex = (GraphCell) vGraph.getModel().getParent(vGraph.getModel().getTarget(port));
			if (adjacentVisualVertex == null)
				continue;
			if (adjacentVisualVertex.equals(vVertex))
				adjacentVisualVertex = (GraphCell) vGraph.getModel().getParent(vGraph.getModel().getSource(port));

			// }
			// for (i = 0; i < size; i++) {
			// adjacentVisualVertex = this.vGraph.getVisualVertex((Vertex)
			// adjacentVertices.get(i));
			// if (adjacentVisualVertex == vVertex)
			// continue;
			if (adjacentVisualVertex == null)
				continue;
			if (adjacentVisualVertex == vVertex)
				continue;

			Map<?, ?> aVisualAtt;
			if (h.containsKey(adjacentVisualVertex)) {
				aVisualAtt = h.get(adjacentVisualVertex);
			} else {
				aVisualAtt = adjacentVisualVertex.getAttributes();
			}
			adjX = GraphConstants.getBounds(aVisualAtt).getCenterX();
			adjY = GraphConstants.getBounds(aVisualAtt).getCenterY();

			distance = Point2D.distance(adjX, adjY, thisX, thisY);
			if (distance == 0)
				distance = .0001;

			// spring = this.stiffness * ( distance - this.springLength ) *
			// (( thisX - adjX ) / ( distance ));
			spring = this.stiffness * Math.log(distance / this.springLength) * ((thisX - adjX) / (distance));

			xSpring += spring;

			// spring = this.stiffness * ( distance - this.springLength ) *
			// (( thisY - adjY ) / ( distance ));
			spring = this.stiffness * Math.log(distance / this.springLength) * ((thisY - adjY) / (distance));

			ySpring += spring;

		}

		// Get the electrical repulsion between all vertices,
		// including those that are not adjacent.
		/*
		 * List allVertices = this.vGraph.getModel(); VisualVertex
		 * aVisualVertex;
		 */
		size = this.vGraph.getModel().getRootCount();
		for (i = 0; i < size; i++) {
			if (this.vGraph.getModel().getRootAt(i) instanceof GraphCell) {
				try {
					GraphCell aVisualVertex = (GraphCell) this.vGraph.getModel().getRootAt(i);
					/*
					 * if (!this.vGraph.getModel().isLeaf(aVisualVertex))
					 * continue;
					 */
					if (aVisualVertex == null)
						continue;
					if (aVisualVertex == vVertex)
						continue;
					Map<?, ?> aVisualAtt;
					if (h.containsKey(aVisualVertex)) {
						aVisualAtt = h.get(aVisualVertex);
					} else {
						aVisualAtt = aVisualVertex.getAttributes();
					}
					adjX = GraphConstants.getBounds(aVisualAtt).getCenterX();
					adjY = GraphConstants.getBounds(aVisualAtt).getCenterY();

					distance = Point2D.distance(adjX, adjY, thisX, thisY);
					if (distance == 0) {
						distance = .0001;
						// adjX--;
						// adjY--;
					}
					/*
					 * if (distance > 400) continue;
					 */
					repulsion = (this.electricalRepulsion * vGraph.getModel().getChildCount(vVertex) / distance)
							* ((thisX - adjX) / (distance))
					/** vGraph.getModel().getChildCount(vVertex) */
					;

					xRepulsion += repulsion;

					repulsion = (this.electricalRepulsion * vGraph.getModel().getChildCount(vVertex) / distance)
							* ((thisY - adjY) / (distance))
					/** vGraph.getModel().getChildCount(vVertex) */
					;

					yRepulsion += repulsion;
				} catch (NullPointerException e) {
				}
			}
		}

		// Combine the two to produce the total force exerted on the vertex.
		xForce = xSpring - xRepulsion;
		yForce = ySpring - yRepulsion;

		// Move the vertex in the direction of "the force" --- thinking of star
		// wars :-)
		// by a small proportion
		double xadj = (0 - (xForce * this.increment));
		double yadj = (0 - (yForce * this.increment));

		/*
		 * double maxMove = 20; if (Math.abs(xadj)>maxMove) xadj =
		 * xadj/Math.abs(xadj)*maxMove; if (Math.abs(yadj)>maxMove) yadj =
		 * yadj/Math.abs(yadj)*maxMove;
		 */
		if (Math.abs(xadj) < 0.5 && Math.abs(yadj) < 0.5)
			return;

		double newX = GraphConstants.getBounds(amap).getMinX() + xadj;
		double newY = GraphConstants.getBounds(amap).getMinY() + yadj;
		int width = (int) Math.round(GraphConstants.getBounds(amap).getWidth());
		int heigth = (int) Math.round(GraphConstants.getBounds(amap).getHeight());
		// Ensure the vertex's position is never negative.
		// HashMap imap = new HashMap();
		/* if (newX >= 0 && newY >= 0) */{

			GraphConstants.setBounds(amap, new Rectangle(new Point((int) Math.round(newX), (int) Math.round(newY)),
					new Dimension(width, heigth)));

			// vVertex.setLocationDelta(xadj, yadj);
		}
		h.put(vVertex, amap);

	}

	/**
	 * This method is called to actually paint or draw the layout of the graph.
	 * This method should only be called after at least one call to layout().
	 */
	public void drawLayout() {
		this.vGraph.repaint();
	}

	public void addVertex(GraphCell vVertex) {
		// Do nothing here
	}

	public void removeEdge(GraphCell vEdge) {
		// Do nothing here
	}

	public void addEdge(GraphCell vEdge) {
		// Do nothing here
	}

	public void run() {
		// List<GraphCell> visualVertices;
		GraphCell vVertex;
		int i, size;

		Date now = new Date();

		// thisMap<?, ?>ectLayout();
		// this.drawLayout();
		int k = 0;
		// Thread me = Thread.currentThread();
		Map<GraphCell, Map<?, ?>> h = new HashMap<GraphCell, Map<?, ?>>();
		size = this.vGraph.getModel().getRootCount();
		
		//Randomise position to avoid that all the vertices are in hte same position
		for (i = 0; i < size; i++) {
			vVertex = (GraphCell) this.vGraph.getModel().getRootAt(i);
			Rectangle2D r = GraphConstants.getBounds(vVertex.getAttributes());
			// System.out.print ("FROM " + r);
			try{
			GraphConstants.setBounds(vVertex.getAttributes(),new Rectangle2D.Double((double)new Random().nextInt((int)20), (double)new Random().nextInt((int)20),r.getWidth(),r.getHeight()));
			}catch (Exception e) {
				//System.err.println(vVertex);
			}
			

		}
		while (true) {
			drawLayout();
			for (i = 0; i < size; i++) {
				vVertex = (GraphCell) this.vGraph.getModel().getRootAt(i);
				
				this.relax(vVertex, h);

			}

			// System.out.println(k +" " + (new Date().getTime() -
			// now.getTime()) + "s");
			if (k >= 10 && k % 10 == 0 && (new Date().getTime() - now.getTime()) > 1000) {
				k = 1000;

			}
			if (k++ >= 1000 || h.size() <= 0) {

				{
					Enumeration<Map<?, ?>> enumeration = Collections.enumeration(h.values());
					while (enumeration.hasMoreElements()) {
						Map<?, ?> element = enumeration.nextElement();
						Rectangle2D r = GraphConstants.getBounds(element);
						if (gRect == null)
							gRect = r;
						else
							gRect = r.createUnion(gRect);
					}
				}
				// System.out.println(gRect);

				{
					Enumeration<Map<?, ?>> enumeration = Collections.enumeration(h.values());
					while (enumeration.hasMoreElements()) {
						Map<?, ?> element = enumeration.nextElement();
						Rectangle2D r = GraphConstants.getBounds(element);
						// System.out.print ("FROM " + r);
						GraphConstants.setBounds(element, new Rectangle2D.Double(r.getX() - gRect.getX(), r.getY()
								- gRect.getY(), r.getWidth(), r.getHeight()));
						// System.out.println(" TO " +
						// GraphConstants.getBounds(element));

					}
				}
				vGraph.getGraphLayoutCache().edit(h, null, null, null);
				if (gRect != null) {
					double scale = Math.min(width / gRect.getWidth(), height / gRect.getHeight());

					vGraph.setScale(scale);

				}
				this.vGraph.repaint();

				return;
			}

			/*
			 * try { Thread.sleep(1000); } catch (InterruptedException ex) {
			 * break; }
			 */

		}
	}

}