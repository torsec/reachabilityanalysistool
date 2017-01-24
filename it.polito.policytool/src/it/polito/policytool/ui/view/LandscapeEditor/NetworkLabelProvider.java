
package it.polito.policytool.ui.view.LandscapeEditor;

import it.polito.policytool.util.Figure;
import it.polito.policytoollib.landscape.FilteringZone;
import it.polito.policytoollib.landscape.SecurityControl;
import it.polito.policytoollib.landscape.Host;

import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.zest.core.viewers.IEntityStyleProvider;



public class NetworkLabelProvider extends LabelProvider implements IEntityStyleProvider
{
	/*
	 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object element)
	{
		if (element instanceof SecurityControl)
		{
			SecurityControl node = (SecurityControl) element;
			return node.getName();
		}
		if (element instanceof FilteringZone)
		{
			FilteringZone node = (FilteringZone) element;
			return node.getName();
		}
		if (element instanceof Host)
		{
			Host node = (Host) element;
			return node.getName();
		}
		return null;
	}

	@Override
	public Image getImage(Object element)
	{
		if (element instanceof SecurityControl)
		{
			return Figure.POLICY.load();
		}
		if (element instanceof FilteringZone)
		{
			return Figure.NETWORK.load();
		}
		if (element instanceof Host)
		{
			return Figure.HOST.load();
		}
		return null;
	}

	/*
	 * @see org.eclipse.zest.core.viewers.IEntityStyleProvider#getNodeHighlightColor(java.lang.Object)
	 */
	@Override
	public Color getNodeHighlightColor(Object entity)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * @see org.eclipse.zest.core.viewers.IEntityStyleProvider#getBorderColor(java.lang.Object)
	 */
	@Override
	public Color getBorderColor(Object entity)
	{
		return new Color(Display.getDefault(), 0, 0, 0);
	}

	/*
	 * @see org.eclipse.zest.core.viewers.IEntityStyleProvider#getBorderHighlightColor(java.lang.Object)
	 */
	@Override
	public Color getBorderHighlightColor(Object entity)
	{
		return getBorderColor(entity);
	}

	/*
	 * @see org.eclipse.zest.core.viewers.IEntityStyleProvider#getBorderWidth(java.lang.Object)
	 */
	@Override
	public int getBorderWidth(Object entity)
	{
		// TODO Auto-generated method stub
		return 3;
	}

	/*
	 * @see org.eclipse.zest.core.viewers.IEntityStyleProvider#getBackgroundColour(java.lang.Object)
	 */
	@Override
	public Color getBackgroundColour(Object entity)
	{
		return null;
	}

	/*
	 * @see org.eclipse.zest.core.viewers.IEntityStyleProvider#getForegroundColour(java.lang.Object)
	 */
	@Override
	public Color getForegroundColour(Object entity)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * @see org.eclipse.zest.core.viewers.IEntityStyleProvider#getTooltip(java.lang.Object)
	 */
	@Override
	public IFigure getTooltip(Object element)
	{
		return null;
	}

	/*
	 * @see org.eclipse.zest.core.viewers.IEntityStyleProvider#fisheyeNode(java.lang.Object)
	 */
	@Override
	public boolean fisheyeNode(Object entity)
	{
		// TODO Auto-generated method stub
		return false;
	}
}
