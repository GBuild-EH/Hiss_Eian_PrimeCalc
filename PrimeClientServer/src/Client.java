import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;

public class Client {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run() {
				constructGUI();
			}
		}); 		
	}
	private static void constructGUI() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		PClientGui frame = new PClientGui();
		frame.setVisible(true);
	}
}

class PClientGui extends JFrame {
	public JTextField toSend;
	public JTextArea received;
	public JButton connect;
	public DefaultListModel<String> listModel = new DefaultListModel<String>();
	
	public PClientGui () {
		super();
		init();
	}
	
	private void init() { //main window components
		toSend = new JTextField();
		received = new JTextArea();
		connect = new JButton("Connect");
		connect.addActionListener(new ClientListener(this));
		
		//assembling window
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Prime Calculator Client");
		this.setLayout(new GridLayout(3,2));
		this.add(new JLabel("Server request:"));
		this.add(toSend);
		toSend.setText("Enter number to evaluate");
		this.add(new JLabel("Server Response:"));
		received.setEditable(false);
		this.add(received);
		this.add(new JLabel());
		this.add(connect);
		int frameWidth = 1000;
		int frameHeight = 500;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds((int) (screenSize.getWidth()/2) - frameWidth,
				(int) (screenSize.getHeight() / 2) - frameHeight,
				frameWidth, frameHeight);
	}
} 

class ClientListener implements ActionListener {
	PClientGui cFrame;
	
	public ClientListener (PClientGui frame) {
		cFrame = frame;
	}
	
	public void actionPerformed (ActionEvent e) {
		int toSend = Integer.parseInt(cFrame.toSend.getText().trim());
		cFrame.received.append("Sent number: " + toSend + "\n");
		try {
			Socket sock = new Socket("localhost", 1236);
			DataOutputStream out = new DataOutputStream(sock.getOutputStream());
			BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out.writeInt(toSend);			
			String serverString = in.readLine();
			cFrame.received.append(serverString.trim() + "\n");
			sock.close();			
			
		} catch (UnknownHostException uhe) {
			uhe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
