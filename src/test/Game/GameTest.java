package test.Game;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import Game.Game;
import Game.InvalidServerGameInputException;

public class GameTest {
	
	private Game game;
	private int width = 9;
	private int heigth = 7;
	private int counter = 6;
	
	@Before
	public void setUp() throws InvalidServerGameInputException
	{
		game = new Game(width, heigth, counter);
	}

	@Test(expected = InvalidServerGameInputException.class)
	public void testCounterGreaterThanWidth() throws InvalidServerGameInputException
	{
		new Game(2, 5, 5);
	}
	
	@Test
	public void testAddToBoard()
	{
		int position = 5;
		int player = 1;
		int rowPosition = game.addToBoard(position, player);
		assertEquals(heigth-1, rowPosition);
	}
	
	@Test
	public void testAddToBoard_InvalidPosition()
	{
		int position = 0;
		int player = 1;
		int rowPosition = game.addToBoard(position, player);
		assertEquals(-1, rowPosition);
	}
	
	@Test
	public void testAddToBoard_InvalidPlayer()
	{
		int position = 9;
		int player = 3;
		int rowPosition = game.addToBoard(position, player);
		assertEquals(-1, rowPosition);
	}
	
	@Test
	public void testAddToBoard_FullArray()
	{
		int position = 2;
		int player = 1;
		
		// need to add heigth times to board to generate error.
		for (int i = 0; i < heigth; i++)
		{
			game.addToBoard(position, player);
		}
		int rowPosition = game.addToBoard(position, player);
		assertEquals(-1, rowPosition);
	}
	
	@Test
	public void testCountDown()
	{
		int position = 2;
		int player = 1;
		int rowPosition = 0;
		
		// need to add heigth times to board to generate error.
		for (int i = 0; i < counter; i++)
		{
			rowPosition = game.addToBoard(position, player);
		}
		int countDown = game.countDown(rowPosition, position-1, player);		
		assertEquals(countDown, counter);
	}
	
	@Test
	public void testCountRight()
	{
		int position = 8;
		int player = 1;
		int rowPosition = 0;
		
		// need to add heigth times to board to generate error.
		for (int i = 0; i < counter; i++)
		{
			rowPosition = game.addToBoard(position, player);
			position--;
		}
		int countRight = game.countRight(rowPosition, position, player);		
		assertEquals(countRight, counter);
	}
	
	@Test
	public void testCountLeft()
	{
		int position = 4;
		int player = 1;
		int rowPosition = 0;
		
		// need to add heigth times to board to generate error.
		for (int i = 0; i < counter; i++)
		{
			rowPosition = game.addToBoard(position, player);
			position++;
		}
		int countLeft = game.countLeft(rowPosition, position-2, player);		
		assertEquals(countLeft, counter);
	}
	
	@Test
	public void testCountDownLeftDiagonal()
	{
		int player = 1;
		int rowPosition = 0;
		
		// creating a diagonals of 1's.
		for (int i = 0; i < counter; i++)
		{
			int coinFlip = 1;
			for (int j = 0 + i; j < counter; j++)
			{
				coinFlip = coinFlip%2==1 ? 0 : 1;	
				rowPosition = game.addToBoard(j+1, coinFlip+1);
			}
		}
		int countDownLeft = game.countDownLeft(rowPosition, counter-1, player);		
		assertEquals(countDownLeft, counter);
	}
	
	@Test
	public void testCountDownRightDiagonal()
	{
		int randomShift = 3;
		int player = 1;
		int rowPosition = 0;
		
		
		// creating a diagonals of 1's.
		for (int i = 0; i < counter; i++)
		{
			int coinFlip = 1;
			for (int j = counter - i; j > 0; j--)
			{
				coinFlip = coinFlip%2==1 ? 0 : 1;
				rowPosition = game.addToBoard(j+randomShift, coinFlip+1);
			}
		}
		
		int countDownRight = game.countDownRight(rowPosition, randomShift, player);		
		assertEquals(countDownRight, counter);
	}
	
	@Test
	public void testWin()
	{
		int position = 4;
		int player = 1;
		int rowPosition = 0;
		boolean testWin;
		if(counter <= 5)
		{
			testWin = true;
		} else {
			testWin = false;
		}
		
		for (int i = 0; i < 2; i++)
		{
			game.addToBoard(position-1-i, player);
		}
		
		for (int i = 0; i < 2; i++)
		{
			game.addToBoard(position+1+i, player);
		}

		rowPosition = game.addToBoard(position, player);
		boolean win = game.checkWin(rowPosition, position-1, player);
		assertEquals(win, testWin);
	}
	
	@Test
	public void testNoWinner()
	{
		int radomShift = 2;
		int player = 1;
		int rowPosition = 0;
		boolean testWin;
		
		if(counter <= 3)
		{
			testWin = true;
		} else {
			testWin = false;
		}
		
		// creating a diagonals of 1's.
		for (int i = 0; i < 3; i++)
		{
			int coinFlip = 1;
			for (int j = 3 - i; j > 0; j--)
			{
				coinFlip = coinFlip%2==1 ? 0 : 1;
				rowPosition = game.addToBoard(j+radomShift, coinFlip+1);
			}
		}

		boolean win = game.checkWin(rowPosition, radomShift-1, player);
		assertEquals(win, testWin);
	}
}
