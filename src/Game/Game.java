package Game;

public class Game {
	
	//	private static int width = 9;
	//	private static int heigth = 6;
	//	private static int counter = 5;
	
	private int[][] board;
	private int counter;
	
	public Game(int width, int heigth, int counter)
	{
		this.board = new int[heigth][width];
		//initBoard(this.board);
		this.counter = counter; 
	}
	
	public int[][] getBoard()
	{
		return this.board;
	}
	
	private int getCounter()
	{
		return this.counter;
	}
	
	public void printBoard()
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
	
	public int addToBoard(int position, int player)
	{
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
	
	public boolean checkWin(int x, int y, int player)
	{
		
		int checkNegativeDiagonal = checkDownRight(x, y, player) + checkUpLeft(x, y, player) - 1;
		if(checkNegativeDiagonal >= getCounter()) {
			return true;
		}
		
		int checkPositiveDiagonal = checkDownLeft(x, y, player) + checkUpRight(x, y, player) - 1;
		if(checkPositiveDiagonal >= getCounter()) {
			return true;
		}
		
		int checkUpDown	= checkDown(x, y, player) + checkUp(x, y, player) - 1;
		if(checkUpDown >= getCounter()) {
			return true;
		}
		
		int checkAcross	= checkLeft(x, y, player) + checkRight(x, y, player) - 1;
		if(checkAcross >= getCounter()) {
			return true;
		}
		
		return false;
	}
	
	// Diagonals Check
	
	private int checkDownRight(int x, int y, int player)
	{
		if(x > board.length-1 || y > board[0].length-1 || board[x][y] != player) return 0;
		return 1 + checkDownRight(player, x+1, y+1);
	}
	
	private int checkUpLeft(int x, int y, int player)
	{
		if( x < 0 || y < 0 || board[x][y] != player) return 0;
		return 1 + checkUpLeft(player, x-1, y-1);
	}
	
	private int checkDownLeft(int x, int y, int player)
	{
		if( x > board.length-1 || y < 0 || board[x][y] != player) return 0;
		return 1 + checkDownLeft(player, x+1, y-1);
	}
	
	private int checkUpRight(int x, int y, int player)
	{
		if( x < 0 ||  y > board[0].length-1 || board[x][y] != player) return 0;
		return 1 + checkUpRight(player, x-1, y+1);
	}
	
	
	// X & Y Check
	
	private int checkDown(int x, int y, int player)
	{
		if(x > board.length-1 || board[x][y] != player) {
			return 0;
		}
		
		return 1 + checkDown(x+1, y, player);
	}
	
	private int checkUp(int x, int y, int player)
	{
		if(x < 0 || board[x][y] != player) return 0;
		return 1 + checkUp(player, x-1, y);
	}
	
	private int checkLeft(int x, int y, int player)
	{
		if( y < 0 || board[x][y] != player) return 0;
		return 1 + checkLeft(player, x, y-1);
	}
	
	private int checkRight(int x, int y, int player)
	{
		if( y > board[0].length-1 || board[x][y] != player) return 0;
		return 1 + checkRight(player, x, y+1);
	}
	
	
	

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
