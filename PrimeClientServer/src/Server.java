import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.util.Date;
import java.io.*;

public class Server {

	static PServerGui frame = null;
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run() {
				constructGUI();
				}			
		}); 
		boolean shutdown = false;
		ServerSocket server = null;	
		try {
			server = new ServerSocket(1236);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		frame.received.append("Server started at " + new Date() + '\n');
		while (!shutdown) {
			try {
				Socket client = server.accept(); //getting client connection
				DataInputStream input = new DataInputStream(client.getInputStream());
				DataOutputStream output = new DataOutputStream(client.getOutputStream());
				
				int clientNumber = input.readInt(); //get number from client
				frame.received.append("Received: " + clientNumber + "\n");
				String response = "The number " + clientNumber + " is" + (isPrime(Math.abs(clientNumber)) ? " " : " NOT ") + "prime.\n";
				frame.received.append("Sending: " + response); //felt cute, went with a ternary operator
				output.writeUTF(response); //send response to client
				client.close();
				
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
		}
	}
	
	private static boolean isPrime(int n) {
		if (n <= 1)
			  return false;
		  if (n == 2 || n == 3)
			  return true;
		  if (n % 2 == 0 || n % 3 == 0)
			  return false;
		  for (int i = 5; i <= Math.sqrt(n); i = i+6)
			  if (n % i == 0 || n % (i+2) == 0)
				  return false;
		  return true;
	}
	
	private static void constructGUI() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		frame = new PServerGui();
		frame.setVisible(true);
	}
}

class PServerGui extends JFrame {
	
	public JTextArea received;	
	public PServerGui() {
		super();
		init();
	}
	
	private void init() {
		received = new JTextArea();		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Prime Calculator Server");
		this.setLayout(new GridLayout(1,1));
		received.setEditable(false);
		this.add(received);
		int frameWidth = 1000;
		int frameHeight = 500;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds((int) (screenSize.getWidth()/2) - frameWidth,
				(int) (screenSize.getHeight() / 2) - frameHeight,
				frameWidth, frameHeight);
	}
}
