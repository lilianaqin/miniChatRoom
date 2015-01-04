package Client;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.*;

import drawapp.DrawApp;
import Client.privatechat.bppListener;
 
public class MiniChatClient extends JFrame {
    
	private static final long serialVersionUID = 8894752138296928658L;
	Socket s = null;
    //DataOutputStream dos = null;
    //DataInputStream dis = null;
    ObjectOutputStream OOS = null;
    ObjectInputStream OIS = null;
    private boolean bConnected = false;
 
    TextField tfTxt = new TextField();
 
    //TextArea taContent = new TextArea();
    JTextPane Content = new JTextPane();
    JScrollPane ScrollText = new JScrollPane(Content);
    JButton bPostPic = new JButton("图片");
    JButton bDraw = new JButton("涂鸦板");
    String[] str_name = { "宋体", "黑体", "Dialog", "Gulim" };
    String[] str_Size = { "12", "14", "18", "22", "30", "40" };
    String[] str_Style = { "常规", "斜体", "粗体", "粗斜体" };
    String[] str_Color = { "黑色", "红色", "蓝色", "黄色", "绿色" };
    String[] str_BackColor = { "无色", "灰色", "淡红", "淡蓝", "淡黄", "淡绿" };
    private JComboBox<String> fontName = null, fontSize = null, fontStyle = null, fontColor = null,
    		   fontBackColor = null; 
    private JLabel lbfontName = new JLabel("字体"), lbfontSize = new JLabel("字号"), lbfontStyle = new JLabel("样式"),
    			lbfontColor = new JLabel("文字颜色"), lbfontBackColor = new JLabel("背景色");
    
    private DefaultListModel<String> dlm = new DefaultListModel<String>();
    private JList<String> jlPersonList = new JList<String>(dlm);
    private JScrollPane spPersonList = new JScrollPane(jlPersonList);
    
    private JLabel zxlb = new JLabel("--在线列表--");
    
    private StyledDocument doc = null; 

    ArrayList<privatechat> privatechaters = new ArrayList<privatechat>();
    
    Thread tRecv = new Thread(new RecvThread()); 
     
    String UserName = null;
    public static Toolkit theKit = null;
	public static GridBagLayout G = new GridBagLayout();
	private GridBagConstraints c = new GridBagConstraints();
	
    public MiniChatClient(String UN) {
    	UserName = UN;
    	launchFrame();
    }
 
    private void launchFrame() {
    	this.setTitle(UserName);
    	fontName = new JComboBox<String>(str_name); // 字体名称
    	fontSize = new JComboBox<String>(str_Size); // 字号
    	fontStyle = new JComboBox<String>(str_Style); // 样式
    	fontColor = new JComboBox<String>(str_Color); // 颜色
    	fontBackColor = new JComboBox<String>(str_BackColor);
    	Content.setEditable(false);
    	doc = Content.getStyledDocument();
    	setLayout(G);
    	c.fill = GridBagConstraints.BOTH;
    	c.weightx = 1;
    	c.weighty = 1;
    	c.gridx = 0;
    	c.gridy = 0;
    	c.gridwidth = 8;
    	c.gridheight = 2;
    	add(ScrollText,c);
    	
    	c.weightx = c.weighty = 0;
    	c.gridx = 8;
    	c.gridwidth = 2;
    	c.gridheight = 1;
    	add(zxlb,c);
    	
    	c.weighty = 1;
    	c.gridy = 1;
    	c.gridwidth = 2;
    	c.gridheight = 5;
    	add(spPersonList,c);
    	
    	c.weighty = 0;
    	c.gridx = 0;
    	c.gridy = 2;
    	c.gridwidth = c.gridheight = 1;
    	add(bPostPic,c);
    	c.gridy = 3;
    	add(bDraw, c);
    	c.gridx = 1;
    	c.gridy = 2;
    	add(lbfontName,c);
    	c.gridx = 2;
    	add(fontName,c);
    	
    	c.gridx = 3;
    	add(lbfontSize,c);
    	c.gridx = 4;
    	add(fontSize,c);
    	c.gridx = 5;
    	add(lbfontColor,c);
    	c.gridx = 6;
    	add(fontColor,c);
    	c.gridx = 1;
    	c.gridy = 3;
    	add(lbfontStyle,c);
    	c.gridx = 2;
    	add(fontStyle,c);
    	c.gridx = 3;
    	add(lbfontBackColor,c);
    	c.gridx = 4;
    	add(fontBackColor,c);

    	c.gridy = 4;
    	c.gridx = 0;
    	c.gridwidth = 8;
    	c.gridheight = 2;
    	c.weightx = 1;
    	add(tfTxt,c);
        this.addWindowListener(new WindowAdapter() {
 
            @Override
            public void windowClosing(WindowEvent arg0) {
                disconnect();
                System.exit(0);
            }
             
        });
        tfTxt.addActionListener(new TFListener());
        bPostPic.addActionListener(new bppListener());
        bDraw.addActionListener(new drawListener());
        jlPersonList.addMouseListener(new plMouseAdaptor());
        theKit = this.getToolkit();
		Dimension wndSize = theKit.getScreenSize();
		setBounds(wndSize.width/4,wndSize.height/4,500,500);
        setVisible(true);
        connect();
         
        tRecv.start();
    }
    
    
    
    private void connect() {
        try {
            s = new Socket("127.0.0.1", 8888);
            OOS = new ObjectOutputStream(s.getOutputStream());
            OIS = new ObjectInputStream(s.getInputStream());
            System.out.println("connected!");
            bConnected = true;
            
            cts info = new cts();
            info.type = "username";
            info.content = UserName;
            OOS.writeObject(info);
            OOS.flush();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
         
    }
     
    private void disconnect() {
        try {
            OOS.close();
            OIS.close();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
         
    }
     
    private class TFListener implements ActionListener {
 
        public void actionPerformed(ActionEvent e) {
            String str = tfTxt.getText().trim();
            tfTxt.setText("");
             
            try {
                //dos.writeUTF(str);
                //dos.flush();
            	FontAttrib txt = getFontAttrib(str);
            	cts info = new cts();
            	info.username = UserName;
            	info.type = "txt";
            	info.txt = txt;
            	OOS.writeObject(info);
            	OOS.flush();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
             
        }
         
    }
    
    private class drawListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			new DrawApp(OOS,UserName);
		}
    	
    }
    
    private class bppListener implements ActionListener {

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
						info.username = UserName;
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
    
    private class plMouseAdaptor implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				privatechat pc = new privatechat(UserName,jlPersonList.getSelectedValue(),OOS);
				privatechaters.add(pc);
			}
		}
		@Override
		public void mouseEntered(MouseEvent arg0) {
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
		}
		@Override
		public void mouseReleased(MouseEvent arg0) {
		}
    }
    
    private void InsertTXT(FontAttrib attrib) {
		try { // 插入文本
			doc.insertString(doc.getLength(), 
					attrib.getText() + "\n", 
					attrib.getAttrSet());
		} catch (BadLocationException e) {
		    e.printStackTrace();
		}
	}
    
    private void InsertImage(byte[] image) {
    	Content.setCaretPosition(doc.getLength()); // 设置插入位置
    	Content.insertIcon(new ImageIcon(image)); // 插入图片
    	InsertTXT(new FontAttrib());
    }
    
    private class RecvThread implements Runnable {
 
        public void run() {
            try {
                while(bConnected) {
                    //String str = dis.readUTF();
                    //taContent.setText(taContent.getText() + str + '\n');
                	stc info = new stc();
                	info = (stc)OIS.readObject();
                	if (info.type.equals("command") == true) {
                		if (info.content.equals("AddUser") == true) {
                			String str = info.UserNameList[0];
                			dlm.addElement(str);
                		} else if (info.content.equals("UserNameList") == true) {
                			for (String str : info.UserNameList) {
                				dlm.addElement(str);
                			}
                		} else if (info.content.equals("DeleteUser") == true) {
                			String str = info.UserNameList[0];
                			dlm.removeElement(str);
                		} else if (info.content.equals("Duplicated") == true) {
                			JOptionPane.showMessageDialog(null, "已存在同名用户");
                			new ClientLogin();
                			dispose();
                		}
                	} else if (info.type.equals("txt") == true) {
                		FontAttrib user = new FontAttrib();
                		user.setSize(15);
                		user.setText(info.username+"：");
                		info.txt.setText("    "+info.txt.getText());
                		if (info.username.equals(UserName) == true) {
                			user.setColor(new Color(255,0,0));
                		} else {
                			user.setColor(new Color(0,0,255));
                		}
                		if (info.tusername == null) {
                			InsertTXT(user);
                			InsertTXT(info.txt);
                		} else {
                			privatechat pc = null;
                			if (info.tusername.equals(UserName) == true) {
                				if ((pc = FindPC(info.username)) == null) {
                					pc = new privatechat(UserName,info.username,OOS);
                					privatechaters.add(pc);
                				}
                			} else {
                				if ((pc = FindPC(info.tusername)) == null) {
                					pc = new privatechat(UserName,info.tusername,OOS);
                					privatechaters.add(pc);
                				}
                			}
                			pc.InsertTXT(user);
        					pc.InsertTXT(info.txt);
                		}
                	} else if (info.type.equals("image") == true) {
                		FontAttrib user = new FontAttrib();
                		user.setSize(15);
                		user.setText(info.username+"：");
                		if (info.username.equals(UserName) == true) {
                			user.setColor(new Color(255,0,0));
                		} else {
                			user.setColor(new Color(0,0,255));
                		}
                		if (info.tusername == null) {
                			InsertTXT(user);
                			InsertImage(info.image);
                		} else {
                			privatechat pc = null;
                			if (info.tusername.equals(UserName) == true) {
                				if ((pc = FindPC(info.username)) == null) {
                					pc = new privatechat(UserName,info.username,OOS);
                					privatechaters.add(pc);
                				}
                			} else {
                				if ((pc = FindPC(info.tusername)) == null) {
                					pc = new privatechat(UserName,info.tusername,OOS);
                					privatechaters.add(pc);
                				}
                			}
                			pc.InsertTXT(user);
                			pc.InsertImage(info.image);
                		}
                	}
                }
            } catch (SocketException e) {
                System.out.println("退出了，bye!");
            } catch (EOFException e) {
                System.out.println("退出了，bye!");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
            	e.printStackTrace();
            }
             
        }
         
    }
    
    private privatechat FindPC(String UN) {
    	Iterator<privatechat> iter = privatechaters.iterator();
    	privatechat pc = null;
    	while (iter.hasNext()) {
    		pc = iter.next();
    		if (pc.tUserName.equals(UN) == true)
    			return pc;
    	}
    	return null;
    }
    
    private FontAttrib getFontAttrib(String str) {
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