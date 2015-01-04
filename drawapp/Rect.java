package drawapp;
import java.awt.*;

public class Rect extends ClosedGraph
{
	public Rect(Color c,float s,int x, int y)
	{
		super(c,s,x,y);
	}
	public void draw(Graphics2D g2d)
	{
		g2d.setColor(color);
		g2d.setStroke(stroke);
		int x,y;
		int weight, height;
		weight = Math.abs(x1-x2);
		height = Math.abs(y1-y2);
		x = Math.min(x1, x2);
		y = Math.min(y1, y2);
		g2d.drawRect(x, y, weight, height);
	}
}