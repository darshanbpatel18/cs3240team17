package Scanner;

public class Scanner {
	private TokenType nextTokenType;
	private String	nextTokenValue;
	
	public Scanner(String inputFile, String logFile){
		
	}
	
	public TokenType PeekToken() {
		// Do peek logic 
		return nextTokenType;
	}
	
	public Token GetNextToken() {
		// Do logic if peek hasn't been called 
		return new Token(nextTokenType, nextTokenValue);
	}
}
