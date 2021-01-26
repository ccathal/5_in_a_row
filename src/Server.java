import Game.Game;

// A Java program for a Server 
import java.net.*;
import java.util.Random;
import java.io.*; 

public class Server 
{ 
//	//initialize socket and input stream 
//	private Socket socket1 = null;
//	private Socket socket2 = null;
//	
//	private ServerSocket server  = null;
//	
//	private DataInputStream in1	 = null;
//	private DataInputStream in2	 = null;
//	
//	//private PrintWriter out1 = null;
//	//private PrintWriter out2 = null;
//	
//	private DataOutputStream out1 = null;
//	private DataOutputStream out2 = null;
	
	
	
	// constructor with port 
	public Server(int port) 
	{ 
		//initialize socket and input stream 
		Socket socket1 = null;
		Socket socket2 = null;
		
		ServerSocket server  = null;
		
		DataInputStream in1	 = null;
		DataInputStream in2	 = null;
		
		//private PrintWriter out1 = null;
		//private PrintWriter out2 = null;
		
		DataOutputStream out1 = null;
		DataOutputStream out2 = null;
		
		// starts server and waits for a connection 
		try
		{ 
			server = new ServerSocket(port); 
			System.out.println("Server started"); 

			System.out.println("Waiting for a client ..."); 

			
			// initial client 1 connection
			
			socket1 = server.accept(); 
			System.out.println("Client 1 accepted"); 

			// takes input from the client socket 
			in1 = new DataInputStream( 
				new BufferedInputStream(socket1.getInputStream()));
			
			String name1 = "";
			
			while(name1.equals(""))
			{
				try
				{ 
					name1 = in1.readUTF();
					System.out.println("Player 1 joined: " + name1);
				} 
				catch(IOException i) 
				{ 
					System.out.println(i); 
				} 
			}
			
			out1 = new DataOutputStream(socket1.getOutputStream());
			out1.writeUTF("Welcome " + name1 + " to 5-in-a-row challenge!\nSit tight and wait on your competitor!\n");
			
			
			
			// initial client 2 connection
			
			socket2 = server.accept(); 
			System.out.println("Client 2 accepted");
			
			// takes input from the client socket 
			in2 = new DataInputStream(
				new BufferedInputStream(socket2.getInputStream()));
			
			String name2 = "";
			
			while(name2.equals(""))
			{
				try
				{ 
					name2 = in2.readUTF();
					System.out.println("Player 1 joined: " + name1);
				} 
				catch(IOException i) 
				{ 
					System.out.println(i);
				} 
			}
			
			out2 = new DataOutputStream(socket2.getOutputStream());
			out2.writeUTF("Welcome " + name2 + " to 5-in-a-row challenge!\nYour competitor is ready!\n");

			int width = 9;
		    int heigth = 6;
			int counter = 5;
			Game game = new Game(width, heigth, counter);
			
			Random random = new Random();
			int coinFlip = random.nextInt(2);
			
			ObjectOutputStream objectOutputStream1 = new ObjectOutputStream(socket1.getOutputStream());
			ObjectOutputStream objectOutputStream2 = new ObjectOutputStream(socket2.getOutputStream());
			
			while(!(coinFlip == -1))
			{
				coinFlip = coinFlip%2;
				
				printBoard(game.getBoard());
				
				
				
				objectOutputStream1.writeObject(game.getBoard());
				objectOutputStream2.writeObject(game.getBoard());
				
				System.out.println("Written board arrays to client.");
				
				if(coinFlip == 0)
				{
					out2.writeUTF("wait your turn");
					commenceGo(out1, in1, width, game, coinFlip+1);
				}
				else
				{
					out1.writeUTF("wait your turn");
					commenceGo(out2, in2, width, game, coinFlip+1);
				} 
			}
			
			String line1 = "";
			String line2 = ""; 

			// reads message from client until "Over" is sent 
			/*
			while (!line1.equals("Over") || !line2.contentEquals("Over")) 
			{
				try
				{ 
					line1 = in1.readUTF();
					line2 = in1.readUTF();
					
					System.out.println(line1);
					System.out.println(line2);

				} 
				catch(IOException i) 
				{ 
					System.out.println(i); 
				} 
			} 
			System.out.println("Closing connection");
			*/

			// close connection 
			socket1.close();
			socket2.close(); 
			in1.close(); 
			in2.close(); 
		} 
		catch(IOException i) 
		{ 
			System.out.println(i); 
		} 
	}
	
	private void commenceGo(DataOutputStream out, DataInputStream in, int width, Game game, int player)
	{
		boolean completedGo = false;
		int position = -1;
		while(!completedGo)
		{
			try {
				out.writeUTF("your go");
			} catch (IOException e) {
				System.out.println(e);
			}
			
			String positionStr = "";
			
			while(positionStr.equals(""))
			{
				try
				{ 
					positionStr = in.readUTF();
					System.out.println(positionStr);
					position = Integer.parseInt(positionStr);
					if(position > 0 && position < width+1) completedGo = true;
					
					int x = game.addToBoard(position, player);
					game.printBoard();
					if(game.checkWin(x, position-1, player))
					{
						System.out.println("Player 1 Winner!!!");
					}
				} 
				catch(Exception i)
				{ 
					System.out.println(i);
				} 
			}
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
		new Server(50); 
	} 
} 
