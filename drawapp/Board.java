package drawapp;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.util.ArrayList;

public class Board 
	extends JPanel 
	implements MouseListener,MouseMotionListener
{
	private static final long serialVersionUID = 1163884722716967755L;
	private static Color color;
	private static int Stroke;
	private static int tool;
	private ArrayList<Pattern> patternList;
	private Pattern tempPattern;
	private static final float[] StrokeSel = {1.0f,3.0f,6.0f,10.0f,15.0f,20.0f};
	private static final float[] EraserStrokeSel = {1.0f,6.0f,10.0f,20.0f,40.0f,60.0f};
	
	public Board()
	{
		patternList = new ArrayList<Pattern>();
		tool = DrawApp.tPencil;
		color = new Color(0,0,0);
		Stroke = 0;
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	public static void SetTool(int val)
	{
		tool = val;
	}
	public static void SetColor(Color newColor)
	{
		color = newColor;
	}
	public static void SetStroke(int newStroke)
	{
		Stroke = newStroke;
	}
	public void Back()
	{
		if (patternList.size() > 0)
		{
			patternList.remove(patternList.size()-1);
		}
		repaint();
	}
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		int size = patternList.size();
		Graphics2D g2d = (Graphics2D)g;
		int i;
		for (i=0;i < size; i++)
		{
			((Pattern)patternList.get(i)).draw(g2d);
		}
	}
	public void mousePressed(MouseEvent e)
	{
		if (e.getButton() == MouseEvent.BUTTON1)
		{
			super.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			switch (tool)
			{
				case DrawApp.tPencil:
					tempPattern = new FreeLine(color,StrokeSel[Stroke],e.getX(),e.getY());
					break;
				case DrawApp.tEraser:
					tempPattern = new FreeLine(Color.WHITE,EraserStrokeSel[Stroke],e.getX(),e.getY());
					break;
				case DrawApp.tLine:
					tempPattern = new Line(color,StrokeSel[Stroke],e.getX(),e.getY());
					break;
				case DrawApp.tRect:
					tempPattern = new Rect(color,StrokeSel[Stroke],e.getX(),e.getY());
					break;
				case DrawApp.tOval:
					tempPattern = new Oval(color,StrokeSel[Stroke],e.getX(),e.getY());
					break;
				case DrawApp.tCircle:
					tempPattern = new Circle(color,StrokeSel[Stroke],e.getX(),e.getY());
					break;
			}
			
			patternList.add(tempPattern);
			repaint();
		}
	}
	public void mouseDragged(MouseEvent e)
	{
		if (tempPattern != null) {
            tempPattern.processCursorEvent(e, Pattern.CURSOR_DRAGGED);
            repaint();
        }
	}
	public void mouseReleased(MouseEvent e) 
	{
		super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		tempPattern = null;
	}

	public void mouseClicked(MouseEvent e) {}  
    public void mouseEntered(MouseEvent e) {}  
    public void mouseExited(MouseEvent e) {}  
    public void mouseMoved(MouseEvent e) {}  
}