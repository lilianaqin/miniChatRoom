package drawapp;
import java.awt.*;
import java.awt.event.*;

public abstract class ClosedGraph implements Pattern
{
	protected int x1,y1,x2,y2;
	protected Color color;
	protected Stroke stroke;
	protected ClosedGraph(Color c, float s, int x,int y)
	{
		color = c;
		stroke = new BasicStroke(s);
		x1 = x2 = x;
		y1 = y2 = y;
	}
	public void processCursorEvent(MouseEvent e, int type)
	{
		if (Pattern.CURSOR_DRAGGED != type)
			return ;
		x2 = e.getX();
		y2 = e.getY();
	}
}