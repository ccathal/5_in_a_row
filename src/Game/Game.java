package Game;

/*
 * Game class for Connect 4 & variations.
 * Variations of the game can be played using different
 * width, heigth and counter values.
 * 
 * @author Cathal Corbett
 * @version 1.0
 * @since 30/01/2021
 */
public class Game {

	// Initialise game board and counter number variables.
	private int[][] board;
	private int counter;
	
	/*
	 * Game constructor.
	 * @param width Integer for the board array width dimension (x-axis).
	 * @param heigth Integer for the board array heigth dimension (y-axis).
	 * @param counter Inteer for the number of consecutave counters on the board to win the game.
	 * @exception InvalidServerGameInputException Exception when counter value is greater than widt or heigth.
	 */
	public Game(int width, int heigth, int counter) throws InvalidServerGameInputException
	{
		if((counter > heigth) || (counter > width))
		{
			throw new InvalidServerGameInputException("Heigth and width values entered must be greater than counter.");
		}
		this.board = new int[heigth][width];
		this.counter = counter; 
	}
	
	/*
	 * Getter method to retrieve game board.
	 * return int[][] Returns the game board 2D array.
	 */
	public int[][] getBoard()
	{
		return this.board;
	}
	
	/*
	 * Getter method to retrieve game counter value.
	 * @return int Counter value.
	 */
	private int getCounter()
	{
		return this.counter;
	}
	
	/*
	 * Method to print game board.
	 */
	public void printBoard()
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
	 * Method for player to add to game board.
	 * @param position Integer column of the board the player adds to. Assumes position in the range of 1 to width.
	 * @param player Integer number of the player adding to the board.
	 * @return int returns the row number of where the the players counter is added to.
	 */
	public int addToBoard(int position, int player)
	{
		if((position < 1) || (position > board[0].length))
		{
			System.out.println("Position entered by client not within board range.");
			return -1;
		}
		
		if(!(player == 1 || player == 2))
		{
			System.out.println("Invalid player number. Only 2 players per games allowed.");
			return -1;
		}
		// Search from the bottom of the array up for 0 value.
		// Then assign the array position to the assiciated player (1 or 2)
		for(int i = board.length-1 ; i >= 0; i--)
		{
			if(board[i][position-1] == 0)
			{
				board[i][position-1] = player;
				return i;
			}
		}
		System.out.println("Position " + position + " full.");
		return -1;
	}
	
	/*
	 * Method to check if a player has won.
	 * Subtract one at the end of each calculation due to counting the x,y twice.
	 * @param x Integer for row value to check win from. Assume x value in range of 0 to heigth-1.
	 * @param y Integer for column value to check win from. Assume y value in range of 0 to width-1.
	 * @return boolean True if win, else False.
	 */
	public boolean checkWin(int x, int y, int player)
	{
		// Check number of consecutive player values in the negativly sloping diagonal.
		int checkNegativeDiagonal = countDownRight(x, y, player) + countUpLeft(x, y, player) - 1;
		if(checkNegativeDiagonal >= getCounter()) {
			return true;
		}
		
		// Check number of consecutive player values in the positivly sloping diagonal.
		int checkPositiveDiagonal = countDownLeft(x, y, player) + countUpRight(x, y, player) - 1;
		if(checkPositiveDiagonal >= getCounter()) {
			return true;
		}
		
		// Check number of consecutive player values in the up-down direction.
		// Do not need to check up here.
		int checkUpDown	= countDown(x, y, player);
		if(checkUpDown >= getCounter()) {
			return true;
		}
		
		// Check number of consecutive player values in the across direction.
		int checkAcross	= countLeft(x, y, player) + countRight(x, y, player) - 1;
		if(checkAcross >= getCounter()) {
			return true;
		}
		
		return false;
	}
	
	
	/*
	 * Individual diagonals check.
	 * Methods to recursivly count diagonal values in the specified directions. 
	 * These methods are kept public for testing purposes.
	 * Methods should be private.
	 * @param x Integer for board array row value.
	 * @param y Integer for board array column value.
	 * @param player Integer for the player value looking to see if they've won.
	 * @return int The number of values of the player in the direction of array specified.
	 */

	public int countDownRight(int x, int y, int player)
	{
		if(x > board.length-1 || y > board[0].length-1 || board[x][y] != player) return 0;
		return 1 + countDownRight(x+1, y+1, player);
	}
	
	public int countUpLeft(int x, int y, int player)
	{
		if( x < 0 || y < 0 || board[x][y] != player) return 0;
		return 1 + countUpLeft(x-1, y-1, player);
	}
	
	public int countDownLeft(int x, int y, int player)
	{
		if( x > board.length-1 || y < 0 || board[x][y] != player) return 0;
		return 1 + countDownLeft(x+1, y-1, player);
	}
	
	public int countUpRight(int x, int y, int player)
	{
		if( x < 0 ||  y > board[0].length-1 || board[x][y] != player) return 0;
		return 1 + countUpRight(x-1, y+1, player);
	}
	
	/*
	 * Individual down, right and left check.
	 * Methods to recursivly count x,y direction values in the specified directions. 
	 * These methods are kept public for testing purposes.
	 * Methods should be private.
	 * @param x Integer for board array row value.
	 * @param y Integer for board array column value.
	 * @param player Integer for the player value looking to see if they've won.
	 * @return int The number of values of the player in the direction of array specified.
	 */
	
	public int countDown(int x, int y, int player)
	{
		if(x > board.length-1 || board[x][y] != player) return 0;
		return 1 + countDown(x+1, y, player);
	}
	
	public int countLeft(int x, int y, int player)
	{
		if( y < 0 || board[x][y] != player) return 0;
		return 1 + countLeft(x, y-1, player);
	}
	
	public int countRight(int x, int y, int player)
	{
		if( y > board[0].length-1 || board[x][y] != player) return 0;
		return 1 + countRight(x, y+1, player);
	}
}
