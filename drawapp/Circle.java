package drawapp;
import java.awt.Color;
import java.awt.Graphics2D;

public class Circle extends ClosedGraph
{
	public Circle(Color c,float s,int x, int y)
	{
		super(c,s,x,y);
	}
	public void draw(Graphics2D g2d)
	{
		g2d.setColor(color);
		g2d.setStroke(stroke);
		int x,y;
		int weight;
		weight = Math.abs(x1-x2);
		x = Math.min(x1, x2);
		y = Math.min(y1, y2);
		g2d.drawOval(x, y, weight,weight);
	}
}