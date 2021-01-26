// A Java program for a Client 
import java.net.*;
import java.util.Scanner;
import java.io.*; 

public class Client 
{ 
	// initialize socket and input output streams 
	private Socket socket		 	= null; 
	private DataInputStream input 	= null; 
	private DataOutputStream out	= null; 
	//private PrintWriter out	= null; 


	// constructor to put ip address and port 
	public Client(String address, int port) 
	{
		
		Scanner scanner = new Scanner(System.in);
		System.out.print("Please enter your name for the game: ");  
		String name = scanner.next();
		
		// establish a connection 
		try
		{ 
			socket = new Socket(address, port); 
			System.out.println("Connected");

			// sends output to the socket 
			out = new DataOutputStream(socket.getOutputStream());
			//out = new PrintWriter(socket.getOutputStream(), true /* autoFlush */);
			out.writeUTF(name);
			System.out.println("Sent name to server");
			
			// takes input from terminal
			//input = new DataInputStream(System.in);
			input = new DataInputStream(
					new BufferedInputStream(socket.getInputStream()));
			
		}
		catch(UnknownHostException u) 
		{ 
			System.out.println(u);
		} 
		catch(IOException i)
		{ 
			System.out.println(i);
		}
		
		String welcome = "";
		while(welcome.equals(""))
		{
			try
			{ 
				welcome = input.readUTF();
				System.out.println(welcome);
			} 
			catch(IOException i) 
			{ 
				System.out.println(i); 
			} 
		}
		
		ObjectInputStream objectInputStream = null;
		try {
			objectInputStream = new ObjectInputStream (socket.getInputStream());
		} catch (IOException e1) {
			System.out.println(e1);
		}
		
		int x = 1;
		while(!(x == -1))
		{
			int[][] board = new int[6][9];
			try {
				board = (int[][]) objectInputStream.readObject();
			} catch (ClassNotFoundException | IOException e1) {
				System.out.println(e1);
			}
			printBoard(board);
			
			
			String move = "";
			while(move.equals(""))
			{
				try
				{ 
					move = input.readUTF();
				} 
				catch(IOException i) 
				{ 
					System.out.println(i); 
				} 
			}
			
			
			if(move.contentEquals("your go"))
			{
				int position = -1;
				
				boolean correctInput = false;
				while(!correctInput) 
				{
					scanner = new Scanner(System.in);
					System.out.print("Please enter where you would like your position: ");  
					String positionStr = scanner.next();
					
					try
					{
						position = Integer.parseInt(positionStr);
						//System.out.println(position);
					}
					catch(Exception e)
					{
						System.out.println(e);
					}
					
					if(position != -1)
					{
						try {
							out.writeUTF(Integer.toString(position));
						} catch (IOException e) {
							System.out.println(e);
						}
						correctInput = true;
					}
				}
			} 
			else 
			{
				System.out.println("Other players turn.");
			}
		}
		
		

		// string to read message from input 
		String line = ""; 

		// keep reading until "Over" is input 
		while (!line.equals("Over")) 
		{ 
			try
			{ 
				line = input.readUTF(); 
				out.writeUTF(line); 
			} 
			catch(IOException i) 
			{ 
				System.out.println(i); 
			} 
		} 

		// close the connection 
		try
		{ 
			input.close(); 
			out.close(); 
			socket.close(); 
		} 
		catch(IOException i) 
		{ 
			System.out.println(i); 
		} 
	} 

	private void printBoard(int[][] board)
	{
		System.out.println("Printing Current Board:"); 
		for (int row = 0; row < board.length; row++) 
		{
			for (int col = 0; col < board[row].length; col++) 
			{ 
				//board[row][col] = 0;
				System.out.print(board[row][col] + "\t");
			}
			System.out.println(); 
		}
	}

	public static void main(String args[]) 
	{ 
		Client client = new Client("127.0.0.1", 50); 
	}
} 
