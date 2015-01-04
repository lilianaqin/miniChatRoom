package drawapp;
import java.awt.Color;
import java.awt.Graphics2D;

public class Line extends ClosedGraph
{
	public Line(Color c,float s,int x, int y)
	{
		super(c,s,x,y);
	}
	public void draw(Graphics2D g2d)
	{
		g2d.setColor(color);
		g2d.setStroke(stroke);
		
		g2d.drawLine(x1, y1, x2, y2);
	}
}