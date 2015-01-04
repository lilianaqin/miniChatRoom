package drawapp;
import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import Client.cts;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;

interface Pattern
{
    public static final int CURSOR_DRAGGED = 1;
	public void draw(java.awt.Graphics2D g);
	public void processCursorEvent(java.awt.event.MouseEvent evt, int type);
}
public class DrawApp extends JFrame implements ActionListener 
{
	private static final long serialVersionUID = 6184449499872788359L;
	//public static JFrame f = new JFrame("Í¿Ñ»°å");
	public static Toolkit theKit = null;
	public static GridBagLayout G = new GridBagLayout();
	private GridBagConstraints c = new GridBagConstraints();
	private JButton bPencil = new JButton("Ç¦±Ê");
	private JButton bEraser = new JButton("ÏðÆ¤");
	private JButton bLine = new JButton("Ö±Ïß");
	private JButton bRect = new JButton("¾ØÐÎ");
	private JButton bCircle = new JButton("Ô²ÐÎ");
	private JButton bOval = new JButton("ÍÖÔ²"); 
	private JButton bColor = new JButton("ÑÕÉ«");
	private JButton bBack = new JButton("³·Ïú");
	private JButton bFinish = new JButton("FINISH!");
	private static String[] strokeText = {"´ÖÏ¸1","2","3","4","5","6"};
	private JComboBox<String> selStroke = new JComboBox<String>(strokeText);
	private Board board = new Board();
	private JPanel empty = new JPanel();

	public static final int tPencil = 1;
	public static final int tEraser = 2;
	public static final int tRect = 3;
	public static final int tCircle = 4;
	public static final int tColor = 5;
	public static final int tOval  = 6;
	public static final int tLine  = 7;
	
	private Color color;
	
	private byte[] bImage = null; 
	
	ObjectOutputStream OOS = null;
	String UserName = null;
	String tUserName = null;

	private void DrawWindow()
	{
		bPencil.setIcon(new ImageIcon("./icons/pencil.jpg"));
		bEraser.setIcon(new ImageIcon("./icons/eraser.jpg"));
		bLine.setIcon(new ImageIcon("./icons/line.jpg"));
		bRect.setIcon(new ImageIcon("./icons/rect.jpg"));
		bCircle.setIcon(new ImageIcon("./icons/circle.jpg"));
		bOval.setIcon(new ImageIcon("./icons/oval.jpg"));
		bColor.setIcon(new ImageIcon("./icons/color.jpg"));
		bBack.setIcon(new ImageIcon("./icons/back.jpg"));
		setLayout(G);
		c.fill = GridBagConstraints.BOTH;

		c.weightx = c.weighty = 0;
		c.gridx = c.gridy = 0;
		add(bPencil,c);
		
		c.weightx = c.weighty = 0;
		c.gridx = 0;
		c.gridy = 1;
		add(bEraser,c);
		
		c.gridy = 2;
		add(bLine,c);
		
		c.gridy = 3;
		add(bRect,c);
		
		c.gridy = 4;
		add(bCircle,c);

		c.gridy = 5;
		add(bOval,c);
		
		c.gridy = 6;
		add(bColor,c);

		c.gridy = 7;
		add(selStroke,c);
		
		c.gridy = 8;
		add(bBack,c);
		
		c.gridy = 9;
		add(bFinish,c);
		
		c.weightx = c.weighty = GridBagConstraints.REMAINDER;
		c.gridy = 10;
		add(empty,c);
		
		board.setBackground(Color.white);
		c.gridx=1;
		c.gridy=0;
		c.weightx = 3;
		c.weighty = 3;
		c.gridwidth = 3;
		c.gridheight = 11;
		add(board,c);
		theKit = this.getToolkit();
		Dimension wndSize = theKit.getScreenSize();
		setBounds(wndSize.width/4,wndSize.height/4,400,350);
		setVisible(true);
	}
	private void AddMouseListenerToButtons()
	{
		bPencil.addActionListener(this);
		bEraser.addActionListener(this);
		bLine.addActionListener(this);
		bRect.addActionListener(this);
		bOval.addActionListener(this);
		bCircle.addActionListener(this);
		bColor.addActionListener(this);
		selStroke.addActionListener(this);
		bBack.addActionListener(this);
		bFinish.addActionListener(this);
		
	}
	public DrawApp() {
	}
	public DrawApp(ObjectOutputStream oos, String UN) {
		super("Í¿Ñ»°å");
		OOS = oos;
		UserName = UN;
		DrawWindow();
		AddMouseListenerToButtons();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	public DrawApp(ObjectOutputStream oos, String fUN, String tUN) {
		OOS = oos;
		UserName = fUN;
		tUserName = tUN;
		DrawWindow();
		AddMouseListenerToButtons();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	public void actionPerformed(ActionEvent event)
	{
		if (bPencil == event.getSource()) Board.SetTool(tPencil);
		else if (bEraser == event.getSource()) Board.SetTool(tEraser);
		else if (bLine   == event.getSource()) Board.SetTool(tLine);
		else if (bRect   == event.getSource()) Board.SetTool(tRect);
		else if (bCircle == event.getSource()) Board.SetTool(tCircle);
		else if (bOval   == event.getSource()) Board.SetTool(tOval);
		else if (bColor  == event.getSource()) {
			color = JColorChooser.showDialog(this,"µ÷É«°å",color);
			Board.SetColor(color);
		} else if (selStroke == event.getSource()) {
			String sel = (String) selStroke.getSelectedItem();
			int Sel= 1;
			if (sel.equals("´ÖÏ¸1")) Sel = 0;
			else if (sel.equals("2")) Sel = 1;
			else if (sel.equals("3")) Sel = 2;
			else if (sel.equals("4")) Sel = 3;
			else if (sel.equals("5")) Sel = 4;
			else if (sel.equals("6")) Sel = 5;
			Board.SetStroke(Sel);
		} else if (bBack == event.getSource()) board.Back();
		else if (bFinish == event.getSource()) {
			Dimension imageSize = board.getSize();
		    BufferedImage image = new BufferedImage(imageSize.width,
		            imageSize.height, BufferedImage.TYPE_INT_ARGB);
		    ByteArrayOutputStream out = new ByteArrayOutputStream();
		    Graphics2D g = image.createGraphics();
		    board.paint(g);
		    g.dispose();
		    try {
				ImageIO.write(image,"png",out);
				bImage = out.toByteArray();
			
				cts info = new cts();
				info.type = "image";
				info.username = UserName;
				info.tusername = tUserName;
				info.image = bImage;
				OOS.writeObject(info);
				OOS.flush();
		    } catch (IOException e) {
				e.printStackTrace();
			}
		    dispose();
		}
	}
}