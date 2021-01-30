package Game;

/*
 * Custom exception when server input invalid game dimensions.
 * 
 * @author Cathal Corbett
 * @version 1.0
 * @since 30/01/2021
 */
public class InvalidServerGameInputException extends Exception
{
	private static final long serialVersionUID = -2027530488914554079L;
	private String exception;
   
	public InvalidServerGameInputException(String exception) 
	{
		this.exception = exception;
	}
   
	public String toString()
	{ 
		return ("InvalidServerInputException Occurred: " + this.exception) ;
	}
}
