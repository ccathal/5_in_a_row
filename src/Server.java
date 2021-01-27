import Game.Game;

// A Java program for a Server 
import java.net.*;
import java.util.Random;
import java.io.*; 

public class Server 
{
	private ServerSocket server  = null;
	private Socket socket1 = null;
	private Socket socket2 = null;	
	private DataInputStream in1	 = null;
	private DataInputStream in2	 = null;	
	private DataOutputStream out1 = null;
	private DataOutputStream out2 = null;
	
	/*
	 * Server constructor.
	 */
	public Server(int port) 
	{
		try
		{ 
			this.server = new ServerSocket(port); 
			System.out.println("Server started"); 
		} 
		catch(IOException i) 
		{ 
			System.out.println("Problem starting server. Perhaps port " + 
					port + " is already in use. Try a different port number.");
			return;
		}
	}
	
	/*
	 * Method for server to accept 2 clients.
	 * Returns integer for the main method to check if a connection failure occured (-1).
	 */
	private int acceptClients()
	{	
		try
		{ 
			// Client 1 connection
			socket1 = server.accept(); 
			System.out.println("Client 1 accepted"); 

			in1 = new DataInputStream(new BufferedInputStream(socket1.getInputStream()));
			out1 = new DataOutputStream(socket1.getOutputStream());
			
			// Takes input from client 1 containing their name
			String name1 = "";
			while(name1.equals(""))
			{
				name1 = in1.readUTF();
				System.out.println("Player 1 joined: " + name1);
			}
			
			// Send message back to client 1 telling them to wait on other competitor.
			out1.writeUTF("Welcome " + name1 + " to 5-in-a-row challenge!\nSit tight and wait on your competitor!");
			
			// Initial client 2 connection.
			socket2 = server.accept(); 
			System.out.println("Client 2 accepted");
			
			in2 = new DataInputStream(new BufferedInputStream(socket2.getInputStream()));
			out2 = new DataOutputStream(socket2.getOutputStream());
			
			// Takes input from client 2 containing their name
			String name2 = "";
			while(name2.equals(""))
			{
				name2 = in2.readUTF();
				System.out.println("Player 2 joined: " + name2);
			}
			
			// Send message back to client 2 telling them the other competitor is ready.
			out2.writeUTF("Welcome " + name2 + " to 5-in-a-row challenge!\nYour competitor is ready!");
		} 
		catch(IOException i) 
		{ 
			System.out.println("One of the clients has disconnected. Ending Game.");
			return -1; // error message back to main method
		}
		return 0; 
	}
	
	/*
	 * Method to commence the game.
	 */
	private void commenceGame(int width, int heigth, int counter) 
	{
		try {
			// Set up new game object.
			Game game = new Game(width, heigth, counter);
			
			// Randomly choose a person to go second.
			Random random = new Random();
			int coinFlip = random.nextInt(2);
			
			ObjectOutputStream objectOutputStream1 = new ObjectOutputStream(socket1.getOutputStream());
			ObjectOutputStream objectOutputStream2 = new ObjectOutputStream(socket2.getOutputStream());
			
			boolean gameOver = false;
			int win = 0;
			while(!gameOver) // loop until game over (win, lose, ended by client).
			{

				// ObjectOutputStreams need to be reset each time the board object is sent.
				objectOutputStream1.reset();
				objectOutputStream2.reset();
				
				// The coinFlip alernates each round indicating the players turn.
				coinFlip = coinFlip%2==1 ? 0 : 1;				
				if(coinFlip == 0) // player 1
				{
					// Write board object to waiting player (player 2) and issue wait message.
					objectOutputStream2.writeObject(game.getBoard());
					out2.writeUTF("wait");
					// Enter commenceTurn method for other player (player 1)
					win = commenceTurn(objectOutputStream1, out1, in1, width, game, coinFlip+1);
				}
				else // player 2
				{
					objectOutputStream1.writeObject(game.getBoard());
					out1.writeUTF("wait");
					win = commenceTurn(objectOutputStream2, out2, in2, width, game, coinFlip+1);
				}
				
				/*
				 * win = -1	: end flag.
				 * win = 0	: no winner.
				 * win = 1	: winner.
				 */
				if(win != 0)
				{
					objectOutputStream1.reset();
					objectOutputStream2.reset();
				
					if(win == 1) // if a win exists
					{
						objectOutputStream1.writeObject(game.getBoard());
						objectOutputStream2.writeObject(game.getBoard());
						if(coinFlip == 0) // player 1 win
						{
							out1.writeUTF("winner");
							out2.writeUTF("loser");
						}
						else // player 2 win
						{
							out2.writeUTF("winner");
							out1.writeUTF("loser");
						}
					}
					else // if end flag inputted
					{
						if(coinFlip == 0) // player 2 wins by forfeiture
						{
							objectOutputStream2.writeObject(game.getBoard());
							out2.writeUTF("end");
						}
						else // player 1 wins by forfeiture
						{
							objectOutputStream1.writeObject(game.getBoard());
							out1.writeUTF("end");
						}
					}
					gameOver = true;
				}
			}
			
			// Close connections.
			socket1.close();
			socket2.close(); 
			in1.close(); 
			in2.close();
			out1.close(); 
			out2.close();
			server.close();
			objectOutputStream1.close();
			objectOutputStream2.close();
		} 
		catch(IOException i) 
		{
			System.out.println("One of the clients has disconnected. Ending Game.");
			return;
		}
	}
	
	/*
	 * Method for the Server to take care of clients game turn.
	 */
	private int commenceTurn(ObjectOutputStream objectOutputStream, DataOutputStream out, DataInputStream in, int width, Game game, int player)
	{
		boolean completedTurn = false;
		int columnPosition = -1;
		int rowPosition = -1;
		while(!completedTurn)  // loop until client has completed a successful turn.
		{
			try 
			{
				// Write board array to the client and inform them it's their go.
				objectOutputStream.writeObject(game.getBoard());
				out.writeUTF("your go");
			
				String positionStr = "";
				while(positionStr.equals(""))
				{
					positionStr = in.readUTF();
					
					 // Client input end program flag.
					if(positionStr.contentEquals("end")) return -1;
					else 
					{
						try
						{
							columnPosition = Integer.parseInt(positionStr);
						}
						catch(NumberFormatException e)
						{
							System.out.println("Invalid input from client. Integer in the range 1-9 must be provided.");
						}
					
						// Ensure position inputted is within correct range.
						if(columnPosition > 0 && columnPosition < width+1) 
						{
							// Add client turn to the game board.
							// Returns -1 if an unsucessful add occured.
							// Else, returns the row position of the add.
							rowPosition = game.addToBoard(columnPosition, player);
							
							// Check the flag is ok. If not, the server asks the client to repeat the turn.
							if(rowPosition != -1) completedTurn = true;
						}
					}
				}
			}
			catch (IOException e) 
			{
				// If communication error occurs return to commenceTurn method to deal with ending the program.
				// Returning any flag is possible here as commenceTurn will call an IO function and encounter same error.
				return -1;
			}
		}
		return (game.checkWin(rowPosition, columnPosition-1, player)) ? 1 : 0;
	}
	
	/*
	 * Method print board for testing purposes
	 */
	private void printBoard(int[][] board)
	{
		System.out.println("Printing Current Board:"); 
		for (int row = 0; row < board.length; row++) 
		{
			for (int col = 0; col < board[row].length; col++) 
			{
				System.out.print(board[row][col] + "\t");
			}
			System.out.println(); 
		}
	}

	/*
	 * Server main method.
	 */
	public static void main(String args[]) 
	{
		int width = 9;
	    int heigth = 6;
		int counter = 5;
		
		Server server = new Server(50); 
		if(server.acceptClients() == -1) return;
		server.commenceGame(width, heigth, counter);
	} 
} 
