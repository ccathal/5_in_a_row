package Game;

/*
 * Game class for Connect 4.
 * Variations of the game can be played using different
 * width, heigth and counter values.
 */
public class Game {
	
	/*
	 * Initialise game board and counter number.
	 */
	private int[][] board;
	private int counter;
	
	/*
	 * Game constructor.
	 */
	public Game(int width, int heigth, int counter)
	{
		this.board = new int[heigth][width];
		this.counter = counter; 
	}
	
	/*
	 * Getter method to retrieve game board.
	 */
	public int[][] getBoard()
	{
		return this.board;
	}
	
	/*
	 * Getter method to retrieve game counter value.
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
	 */
	public int addToBoard(int position, int player)
	{
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
	 */
	public boolean checkWin(int x, int y, int player)
	{
		// Check number of consecutive player values in the negativly sloping diagonal.
		int checkNegativeDiagonal = checkDownRight(x, y, player) + checkUpLeft(x, y, player) - 1;
		if(checkNegativeDiagonal >= getCounter()) {
			return true;
		}
		
		// Check number of consecutive player values in the positivly sloping diagonal.
		int checkPositiveDiagonal = checkDownLeft(x, y, player) + checkUpRight(x, y, player) - 1;
		if(checkPositiveDiagonal >= getCounter()) {
			return true;
		}
		
		// Check number of consecutive player values in the up-down direction.
		// Do not need to check up here.
		int checkUpDown	= checkDown(x, y, player);
		if(checkUpDown >= getCounter()) {
			return true;
		}
		
		// Check number of consecutive player values in the across direction.
		int checkAcross	= checkLeft(x, y, player) + checkRight(x, y, player) - 1;
		if(checkAcross >= getCounter()) {
			return true;
		}
		
		return false;
	}
	
	/*
	 * Individual diagonals check.
	 */
	
	private int checkDownRight(int x, int y, int player)
	{
		if(x > board.length-1 || y > board[0].length-1 || board[x][y] != player) return 0;
		return 1 + checkDownRight(x+1, y+1, player);
	}
	
	private int checkUpLeft(int x, int y, int player)
	{
		if( x < 0 || y < 0 || board[x][y] != player) return 0;
		return 1 + checkUpLeft(x-1, y-1, player);
	}
	
	private int checkDownLeft(int x, int y, int player)
	{
		if( x > board.length-1 || y < 0 || board[x][y] != player) return 0;
		return 1 + checkDownLeft(x+1, y-1, player);
	}
	
	private int checkUpRight(int x, int y, int player)
	{
		if( x < 0 ||  y > board[0].length-1 || board[x][y] != player) return 0;
		return 1 + checkUpRight(x-1, y+1, player);
	}
	
	/*
	 * Individual x,y direction check.
	 */
	
	private int checkDown(int x, int y, int player)
	{
		if(x > board.length-1 || board[x][y] != player) return 0;
		return 1 + checkDown(x+1, y, player);
	}
	
	// not required for our purpose.
	private int checkUp(int x, int y, int player)
	{
		if(x < 0 || board[x][y] != player) return 0;
		return 1 + checkUp(x-1, y, player);
	}
	
	private int checkLeft(int x, int y, int player)
	{
		if( y < 0 || board[x][y] != player) return 0;
		return 1 + checkLeft(x, y-1, player);
	}
	
	private int checkRight(int x, int y, int player)
	{
		if( y > board[0].length-1 || board[x][y] != player) return 0;
		return 1 + checkRight(x, y+1, player);
	}
	
	/*
	 * Main method example for testing.
	 */
	public static void main(String[] args)
	{
		int width = 9;
	    int heigth = 6;
		int counter = 5;
		Game game = new Game(width, heigth, counter);
		
		int position = 5;
		int player = 2;
		
		int x = game.addToBoard(position, player);
		game.printBoard();
		if(game.checkWin(x, position-1, player)) {
			System.out.println("Winner!!");
		}
		
		int xx = game.addToBoard(position, player);
		if(game.checkWin(xx, position-1, player)) {
			System.out.println("Winner!!");
		}
		
		int xxx = game.addToBoard(position, player);
		if(game.checkWin(xxx, position-1, player)) {
			System.out.println("Winner!!");
		}
		
		int xxxx = game.addToBoard(position, player);
		game.printBoard();
		if(game.checkWin(xxxx, position-1, player)) {
			System.out.println("Winner!!");
		}
		
		int xxxxx = game.addToBoard(position, player);
		if(game.checkWin(xxxxx, position-1, player)) {
			System.out.println("Winner!!");
		}
	}
}
