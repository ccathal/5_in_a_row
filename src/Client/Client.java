package Client;

import java.io.*;
// A Java program for a Client 
import java.net.*;
import java.util.Scanner; 

public class Client 
{
	private Socket socket		 	= null; 
	private DataInputStream input 	= null; 
	private DataOutputStream out	= null; 
	private ObjectInputStream objectInputStream	= null;

	/*
	 * Client constructor.
	 * @param address String value of the server address.
	 * @param port Integer value of the server port.
	 * @exception IOException Exception when connection falure to server.
	 */
	public Client(String address, int port)
	{
		try
		{ 
			socket = new Socket(address, port);
			System.out.println("\nConnection to the Server has been successful!\n");
		} 
		catch(IOException i) 
		{ 
			System.out.println("Connection problem to server on port " + port + ". Please ensure port number is correct.");
			System.exit(0);
		} 
	}
	
	/*
	 * Method for client for game setup.
	 * @param name String of the player name to be sent to server.
	 * IOException IOException Exception when connection failure between client and server.
	 */
	private void initiateGameStart(String name)
	{
		try
		{ 
			// Client sends username to server.
			out = new DataOutputStream(socket.getOutputStream());
			out.writeUTF(name);
			
			// Client takes in server welcome message and prints.
			input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));	
			String welcome = "";
			while(welcome.equals(""))
			{
				welcome = input.readUTF();
				System.out.println("\n" + welcome + "\n");
			}
		} 
		catch(IOException i) 
		{ 
			System.out.println("Connection Problem. Ending Game.");
			System.exit(0);
		} 
	}
	
	/*
	 * Method for the game.
	 * @param scanner Scanner object for taking in command line arguments from user.
	 * @param name String name of player.
	 * @exception ClassNotFoundException Exception thrown 
	 * 		when client accepts board array over objectinputstream
	 * @exception IOException Exception when client - server connection fails.
	 */
	private void startGame(Scanner scanner, String name) throws ClassNotFoundException
	{
		try
		{
			objectInputStream = new ObjectInputStream (socket.getInputStream());
			
			boolean gameOver = false;
			while(!gameOver)
			{
				// Each game turn the server will send the board array to the client.
				int[][] board = (int[][]) objectInputStream.readObject();
				printBoard(board);
				
				// The server sends the clients game move.
				String move = "";
				while(move.equals(""))
				{
					move = input.readUTF();
				}
				
				if(move.contentEquals("your go")) // clients game turn
				{
					int position = -1;
					boolean correctInput = false;
					while(!correctInput) 
					{
						scanner = new Scanner(System.in);
						System.out.print(name + ", please enter where you would like your position ('end' to end the game): ");
						String positionStr = scanner.next();
						
						// Check if the user wants to quit the game.
						if(positionStr.contentEquals("end"))
						{
							System.out.println("Ending the game. Good bye!");
							out.writeUTF(positionStr);
							scanner.close();
							return;
						}
						else { // check the number input is a valid integer.
							try
							{
								position = Integer.parseInt(positionStr);
								
								// Client only checks for valid integer.
								// Integer out of range errors dealt with as server.
								// This allows the game to be designed as flexible as possible.
								// No valid integer will prompt the client to re-enter a value.
								if(position != -1)
								{
									out.writeUTF(Integer.toString(position));
									correctInput = true;
								}
								else
								{
									System.out.println("Invalid input. Please input an integer within range.");
								}
							}
							catch(NumberFormatException e)
							{
								System.out.println("Invalid input. Please input an integer, not String.");
							}
						}
					}
				}
				else if(move.contentEquals("wait")) // clients turn to wait
				{
					System.out.println(name + ", wait your turn.");
				}
				else if(move.contentEquals("winner")) // client win
				{
					System.out.println("Well done " + name + ", you have won! Good bye.");
					gameOver = true;
				}
				else if(move.contentEquals("loser")) // client loose
				{
					System.out.println(name + ", you have lost. Best of luck next time. Good bye.");
					gameOver = true;
				}
				else // other competitor had input end flag
				{
					System.out.println(name + ", the other competitor has forteited the game.\nYour are the nominated winner! Good bye.");
					gameOver = true;
				}
				System.out.println();
			}
	
			// Close connections.
			socket.close();
			scanner.close();
		}
		catch(IOException i) 
		{ 
			System.out.println("Connection Problem. Ending Game.");
			return;
		}
	}

	/*
	 * Method to print board to command line.
	 * @param board 2D array of game board print method. 
	 */
	private void printBoard(int[][] board)
	{
		System.out.println("Printing Current Board:");
		String xo;
		for (int row = 0; row < board.length; row++) 
		{
			for (int col = 0; col < board[row].length; col++) 
			{ 
				if(board[row][col] == 1)
				{
					xo = "x";
				}
				else if(board[row][col] == 2)
				{
					xo = "o";
				}
				else xo = " ";
				System.out.print("[ " + xo + " ] ");
			}
			System.out.println(); 
		}
		
		for (int col = 1; col < board[0].length+1; col++) 
		{
			System.out.print("| " + col + " | ");
		}
		System.out.println(); 
	}

	/*
	 * Client main method.
	 */
	public static void main(String args[]) throws ClassNotFoundException 
	{
		Client client = new Client("127.0.0.1", 50);
		
		Scanner scanner = new Scanner(System.in);
		System.out.print("Please enter your name for the game: ");  
		String name = scanner.next();
		
		client.initiateGameStart(name);
		client.startGame(scanner, name);
	}
} 
