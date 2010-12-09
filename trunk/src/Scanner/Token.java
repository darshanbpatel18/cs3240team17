package Scanner;

/**
 * The Token class is message object used to communicate between the Scanner and the Parser. 
 */
public class Token {
	
	private TokenType type;
	private String value;
	
	/**
	 * @param type The type of this token.
	 * @param value The value of the token text.
	 */
	public Token(TokenType type, String value){
		this.type = type;
		this.value = value;
	}

	/**
	 * @return Returns this token's type.
	 */
	public TokenType getType() {
		return type;
	}
	
	/**
	 * @return Returns a string containing the value of the token text.
	 */
	public String getValue() {
		return value;
	}
 
	
}


