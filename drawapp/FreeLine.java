package drawapp;
import java.awt.*;
import java.awt.event.MouseEvent;


public class FreeLine 
	implements Pattern
{
	public static int POINTSETSIZE = 50;
	private Color color;
	private Stroke stroke;
	private PointsSet pointSet;

	public FreeLine(Color c, float s, int x, int y)
	{
		pointSet = new PointsSet(POINTSETSIZE);
		color = c;
		stroke = new BasicStroke(s);
		pointSet.addPoint(x,y);
		
	}
	public void processCursorEvent(MouseEvent e,int t)
	{
		if (t != Pattern.CURSOR_DRAGGED)  
			return ;
		pointSet.addPoint(e.getX(),e.getY());
	}
	public void draw(Graphics2D g2d)
	{
		g2d.setColor(color);
		g2d.setStroke(stroke);
		int[][] points = pointSet.getPoints();
		int len = points[0].length;
		if (1==len)
		{
			int x = points[0][0];
			int y = points[1][0];
			g2d.drawLine(x,y,x,y);
		} else {
			g2d.drawPolyline(points[0],points[1],len);
		}
	}
}