import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.*;

public class Client {

	// For main UI
	JFrame frame = new JFrame();
	JButton button = new JButton("Send");
	JPanel panel = new JPanel();
	JTextField userInput = new JTextField(20);
	JTextArea text = new JTextArea(20, 30);
	JScrollPane scroller = new JScrollPane(text);
	JScrollBar sBar = null;

	// For setting server ip/user ID UI
	JFrame setServer = new JFrame();
	JButton setButton = new JButton("OK");
	JTextField setServerText = new JTextField(20);
	JTextField setUserNameText = new JTextField(20);
	JLabel nameLabel = new JLabel("Name ");
	JLabel serverLabel = new JLabel("    IP    ");
	JPanel setPanel1 = new JPanel();
	JPanel setPanel2 = new JPanel();

	// Var
	String userName;
	String ip;
	String host;
	String userMessage;

	// Socket & Stream
	Socket chat = null;
	InputStreamReader stream = null;
	BufferedReader reader = null;
	PrintWriter writer = null;

	// Theard
	Thread readerTheard = new Thread(new receiver());

	public void goGUI() {
		// Setting UI
		//setServer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JFrame.setDefaultLookAndFeelDecorated(true);
		
		try {
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		 
		setServer.setSize(300, 150);
		setServer.setTitle("Chat Settings");
		setServer.setResizable(false);
		
		setPanel1.add(nameLabel);
		setPanel1.add(setUserNameText);

		setPanel2.add(serverLabel);
		setPanel2.add(setServerText);

		setServer.add(BorderLayout.NORTH, setPanel1);
		setServer.add(BorderLayout.CENTER, setPanel2);
		setServer.add(BorderLayout.SOUTH, setButton);

		setButton.addActionListener(new setListener());

		setServer.setVisible(true);
	}

	public void goService() {
		
		try {
			chat = new Socket(ip, Integer.parseInt(host));
		}  catch (SocketException e){
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			stream = new InputStreamReader(chat.getInputStream(),"utf-8");
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			//PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8")),false);   
			writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(chat.getOutputStream(),"UTF-8")),false);
		} catch (IOException e) {
			e.printStackTrace();
		}

		reader = new BufferedReader(stream);

		readerTheard.start();
	}

	class setListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == setButton && !setServerText.getText().trim().equals("") && !setUserNameText.getText().trim().equals("")) {

				// GUI 澶勭悊
				//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setSize(400, 300);
				frame.setTitle("Chat");
				frame.setResizable(false);
				
				panel.add(userInput);
				panel.add(button);

				text.setLineWrap(true);
				text.setEditable(false);

				frame.getContentPane().add(BorderLayout.SOUTH, panel);
				frame.getContentPane().add(BorderLayout.CENTER, scroller);

				button.addActionListener(new chatListener());
				
				userInput.addKeyListener(
					new KeyAdapter(){
						
						//键盘事件，按下enter的时候发送文本。
						public void keyPressed(KeyEvent e){
							if(e.getKeyCode() == KeyEvent.VK_ENTER){
								try {
									if(!userInput.getText().trim().equals("")){
										writer.println(userName + ":" + userInput.getText());
										writer.flush();
									}
									String[] temp = userInput.getText().split("");
									if(temp[0] == "/changechannel"){
										text.setText("");
									}
								} catch (Exception e1) {
									e1.printStackTrace();
								}
								userInput.setText("");
								userInput.requestFocus();
							}
						}
					}
				);
				
				setServer.setVisible(false);
				frame.setVisible(true);

				// 鍙橀噺澶勭悊
				String work[] = setServerText.getText().split(":");
				ip = work[0];
				host = work[1];
					
				userName = setUserNameText.getText();

				goService();
			}
		}
	}

	class chatListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				if(!userInput.getText().trim().equals("")){
					writer.println(userName + ": " + userInput.getText());
					writer.flush();
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			userInput.setText("");
			userInput.requestFocus();
		}
	}

	class receiver implements Runnable {
		String mes;

		@Override
		public void run() {
			try {
				while ((mes = reader.readLine()) != null) {
					text.append(mes + "\n");
					text.setCaretPosition(text.getText().length());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static void main(String[] args) {
		Client c = new Client();
		c.goGUI();
	}
}
