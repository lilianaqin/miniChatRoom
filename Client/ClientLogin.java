package Client;
import java.awt.*;

import javax.swing.*;

import java.awt.event.*;

public class ClientLogin implements ActionListener
{
	public static JFrame f = new JFrame("登录");
	public static Toolkit theKit = f.getToolkit();
	public static GridBagLayout G = new GridBagLayout();
	private GridBagConstraints c = new GridBagConstraints();
	private JTextField UserNameEnter = null;
	private JButton bLogin = null;
	private JLabel lLogin = null;
	
	public ClientLogin()
	{
		try { // 使用Windows的界面风格
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
		
		UserNameEnter = new JTextField(10);
		bLogin = new JButton("登录");
		lLogin = new JLabel("请输入用户名: ");
		f.setLayout(G);
		c.fill = GridBagConstraints.BOTH;

		c.weightx = c.weighty = 0;
		c.gridx = 2;
		c.gridy = 0;
		f.add(bLogin,c);
		
		c.gridx = 0;
		f.add(lLogin,c);
		
		c.gridx = 1;
		f.add(UserNameEnter,c);
		bLogin.addActionListener(this);
		UserNameEnter.addActionListener(this);
		Dimension wndSize = theKit.getScreenSize();
		f.setBounds(wndSize.width/4,wndSize.height/4,300,80);
		f.setVisible(true);
		
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public void actionPerformed(ActionEvent event)
	{
		String UserName = UserNameEnter.getText();
		f.dispose();
		new MiniChatClient(UserName);
		
	}
	public static void main(String args[])
	{
		new ClientLogin();
	}
}