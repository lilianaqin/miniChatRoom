package Client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import drawapp.DrawApp;

public class privatechat extends JFrame{

	private static final long serialVersionUID = -3600979635502450129L;

	String fUserName = null,tUserName = null;
	
	ObjectOutputStream OOS = null;
	
	Toolkit theKit = this.getToolkit();
	JTextField tfTxt = new JTextField();
	
	JTextPane Content = new JTextPane();
	JPanel empty = new JPanel();
	JButton bPostPic = new JButton("发图");
	JButton bDraw = new JButton("涂鸦板");
	
	StyledDocument doc = null;
	private JScrollPane ScrollText = null;
	
	public static GridBagLayout G = new GridBagLayout();
	private GridBagConstraints c = new GridBagConstraints();
	
	private JComboBox fontName = null, fontSize = null, fontStyle = null, fontColor = null,
			 fontBackColor = null;
	String[] StrName = {"宋体","黑体","Dialog","Gulim"};
	String[] StrSize = { "12", "14", "18", "22", "30", "40" };
	String[] StrStyle = { "常规", "斜体", "粗体", "粗斜体" };
	String[] StrColor = { "黑色", "红色", "蓝色", "黄色", "绿色" };
	String[] StrBackColor = { "无色", "灰色", "淡红", "淡蓝", "淡黄", "淡绿" };


	
	public privatechat(String u1,String u2,ObjectOutputStream oos) {
		fUserName = u1;
		tUserName = u2;
		launchFrame();
		OOS = oos;
	}
	public void launchFrame() {
    	this.setTitle("Talking to: " + tUserName);
    	
        Dimension wndSize = theKit.getScreenSize();
		setBounds(wndSize.width/4,wndSize.height/4,500,500);
		
		Content.setEditable(false);

		doc = Content.getStyledDocument(); //get document of Content
		ScrollText = new JScrollPane(Content);
		
		fontName = new JComboBox(StrName); // 字体名称
		fontSize = new JComboBox(StrSize); // 字号
		fontStyle = new JComboBox(StrStyle); // 样式
		fontColor = new JComboBox(StrColor); // 颜色
		fontBackColor = new JComboBox(StrBackColor); // 背景颜色
		
		setLayout(G);
		c.fill = GridBagConstraints.BOTH;
		
		c.weightx = c.weighty = 1;
		c.gridwidth = 5;
		c.gridheight = 3;
		add(ScrollText,c);
		
		c.weightx = c.weighty = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 0;
		c.gridy = 4;
		add(bPostPic,c);
		c.gridx = 1;
		add(bDraw,c);
		
		c.weightx = 1;
		c.gridwidth = 3;
		c.gridx = 2;
		add(empty,c);
		
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 5;
		c.gridheight = 2;
		c.gridy = 5;
		c.gridx = 0;
		add(tfTxt,c);
        //pack();
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        tfTxt.addActionListener(new TFListener());
        bPostPic.addActionListener(new bppListener());
        bDraw.addActionListener(new drawListener());
        setVisible(true);
        
    }
	private class TFListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			String str = tfTxt.getText().trim();
	        tfTxt.setText("");
	         
	        try {//向socket中写数据
	            //OS.writeUTF(str);
	            //OS.flush();
	        	cts TxtInfo = new cts();
	        	TxtInfo.type = "txt";
	        	TxtInfo.txt = getFontAttrib(str);
	        	TxtInfo.username = fUserName;
	        	TxtInfo.tusername = tUserName;
	        	OOS.writeObject(TxtInfo);
	        	OOS.flush();
	        } catch (IOException e1) {
	            e1.printStackTrace();
	        }
	         
			
		}
		
	}
	
	public class bppListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				JFileChooser f = new JFileChooser(); // 查找文件
				f.showOpenDialog(null);
				BufferedImage input = ImageIO.read(f.getSelectedFile());
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				
				String filename = f.getName(f.getSelectedFile());
				int indexofp = filename.lastIndexOf('.');
				if (indexofp == -1)
				{
					BadImageFileWarning();
				} else {
					String extension = filename.substring(indexofp+1);
					String[] recog = {"bmp","jpg","gif","jpeg","png","ico"};
					boolean ok = false;
					for (String str : recog)
					{
						if (extension.equals(str))
						{
							ok = true;
							break;
						}
					}
					if (ok == false) 
					{
						BadImageFileWarning();
					} else {
						ImageIO.write(input,extension,out);
						byte[] b = out.toByteArray();
						/*测试
						Content.setCaretPosition(doc.getLength());
						Content.insertIcon(new ImageIcon(b));
						InsertTXT(new FontAttrib());
						*/
						cts info = new cts();
						info.type = "image";
						info.image = b;
						info.username = fUserName;
						info.tusername = tUserName;
						OOS.writeObject(info);
						OOS.flush();
					}
				}
				
			} catch (IOException e) {
				
			}
		}
		private void BadImageFileWarning() {
			FontAttrib warn = new FontAttrib();
    		warn.setText("图片格式有误，请重新选择");
    		warn.setName("宋体");
    		warn.setSize(12);
    		warn.setStyle(FontAttrib.GENERAL);
    		warn.setColor(new Color(150,150,150));
			InsertTXT(warn);
		}
		
	}
	
	private class drawListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			new DrawApp(OOS,fUserName,tUserName);
		}
    	
    }
	
	protected void InsertTXT(FontAttrib attrib) {
		try { // 插入文本
			doc.insertString(doc.getLength(), attrib.getText() + "\n", attrib.getAttrSet());
		} catch (BadLocationException e) {
		    e.printStackTrace();
		}
	}
	
	public void InsertImage(byte[] image) {
    	Content.setCaretPosition(doc.getLength()); // 设置插入位置
    	Content.insertIcon(new ImageIcon(image)); // 插入图片
    	InsertTXT(new FontAttrib());
    }
	
	public FontAttrib getFontAttrib(String str) {
		  FontAttrib att = new FontAttrib();
		  att.setText(str);
		  att.setName((String) fontName.getSelectedItem());
		  att.setSize(Integer.parseInt((String) fontSize.getSelectedItem()));
		  String temp_style = (String) fontStyle.getSelectedItem();
		  if (temp_style.equals("常规")) {
		   att.setStyle(FontAttrib.GENERAL);
		  } else if (temp_style.equals("粗体")) {
		   att.setStyle(FontAttrib.BOLD);
		  } else if (temp_style.equals("斜体")) {
		   att.setStyle(FontAttrib.ITALIC);
		  } else if (temp_style.equals("粗斜体")) {
		   att.setStyle(FontAttrib.BOLD_ITALIC);
		  }
		  String temp_color = (String) fontColor.getSelectedItem();
		  if (temp_color.equals("黑色")) {
		   att.setColor(new Color(0, 0, 0));
		  } else if (temp_color.equals("红色")) {
		   att.setColor(new Color(255, 0, 0));
		  } else if (temp_color.equals("蓝色")) {
		   att.setColor(new Color(0, 0, 255));
		  } else if (temp_color.equals("黄色")) {
		   att.setColor(new Color(255, 255, 0));
		  } else if (temp_color.equals("绿色")) {
		   att.setColor(new Color(0, 255, 0));
		  }
		  String temp_backColor = (String) fontBackColor.getSelectedItem();
		  if (!temp_backColor.equals("无色")) {
		   if (temp_backColor.equals("灰色")) {
		    att.setBackColor(new Color(200, 200, 200));
		   } else if (temp_backColor.equals("淡红")) {
		    att.setBackColor(new Color(255, 200, 200));
		   } else if (temp_backColor.equals("淡蓝")) {
		    att.setBackColor(new Color(200, 200, 255));
		   } else if (temp_backColor.equals("淡黄")) {
		    att.setBackColor(new Color(255, 255, 200));
		   } else if (temp_backColor.equals("淡绿")) {
		    att.setBackColor(new Color(200, 255, 200));
		   }
		  }
		  return att;
		 }
}
