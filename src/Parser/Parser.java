package Parser;

import minire.scanner.TokenType;

public class Parser {
	
	public void program(){						//<MiniRE-program> → begin <statement-list> end
		TokenType token;
		token = peekToken();
		if (token == TokenType.BEGIN){			
			matchToken(token);
			statementList();
			token = peekToken();
			if (token == TokenType.END){
				matchToken(token);
			} else {
				throwException();
			}
		} else {
			throwException();
		}
		
	}
	public void statementList(){				//<statement-list> → <statement> <statement-list> | ε
		boolean statement = statement();
		if (statement){							
			statementList();
		}
	}
	public boolean statement(){
		TokenType token;
		token = peekToken();
		if (token == TokenType.ID){					//<statement> → ID := <exp> ;
			matchToken(token);
			token = peekToken();
			if (token == TokenType.ASSIGN){
				matchToken(token);
				exp();
				token = peekToken();
				if (token == TokenType.SEMI){
					matchToken(token);
					return true;
				} else {
					throwException();
				}
			}else {
				throwException();
			}
		} else if (token == TokenType.REPLACE){		//<statement> → replace REGEX with ASCII-STR in  <file-names> ;
			matchToken(token);
			token = peekToken();
			if (token == TokenType.REGEX){
				matchToken(token);
				token = peekToken();
				if(token == TokenType.WITH){
					matchToken(token);
					if (token == TokenType.ASCII){
						matchToken(token);
						token = peekToken();
						if (token == TokenType.IN) {
							matchToken(token);
							fileNames();
							token = peekToken();
							if (token == TokenType.SEMI){
								matchToken(token);
								return true;
							}else {
								throwException();
							}
						}else {
							throwException();
						}
					}else {
						throwException();
					}
				}else {
					throwException();
				}
			}else {
				throwException();
			}
		} else if (token == TokenType.PRINT){		//<statement> → print ( <exp-list> ) ;
			matchToken(token);
			token = peekToken();
			if (token == TokenType.LPAREN){
				matchToken(token);
				expList();
				token = peekToken();
				if (token == TokenType.RPAREN){
					matchToken(token);
					token = peekToken();
					if ( token == TokenType.SEMI){
						matchToken(token);
					}else {
						throwException();
					}
				}else {
					throwException();
				}
			}else {
				throwException();
			}
		}
		return false;
	}
	
	public void fileNames(){		//<file-names> →  <source-file>  -> <destination-file>
		sourceFile();
		TokenType token;
		token = peekToken();
		if(token == TokenType.ARROW){
			matchToken(token);
			destinationFile();
		}else {
			throwException();
		}
	}
	
	public void sourceFile(){		//<source-file> → ASCII-STR  
		TokenType token;
		token = peekToken();
		if(token == TokenType.ASCII){
			matchToken(token);
		}else {
			throwException();
		}
	}
	
	public void destinationFile(){		//<destination-file> → ASCII-STR
		TokenType token;
		token = peekToken();
		if(token == TokenType.ASCII){
			matchToken(token);
		}else {
			throwException();
		}
	}
	
	public void expList(){			// <exp-list> → <exp> <exp-list-p>
		exp();
		expListPrime();
	}
	
	public void expListPrime(){		// <exp-list-p> → , <exp> <exp-list-p> | ε
		TokenType token;
		token = peekToken();
		if ( token == TokenType.COMMA){
			matchToken(token);
			exp();
			expListPrime();
		}
	}
	
	public void exp(){
		TokenType token;
		token = peekToken();
		if (token == TokenType.ID){				// <exp> → ID <bin-op>
			matchToken(token);
			binOp();
		} else if (token == TokenType.INTNUM){	// <exp> → INTNUM <bin-op>
			matchToken(token);
			binOp();
		} else if (token == TokenType.LPAREN){	// <exp> → ( <exp> ) <bin-op>
			matchToken(token);
			exp();
			token = peekToken();
			if (token == TokenType.RPAREN){
				matchToken(token);
				binOp();
			}
		} else if (token == TokenType.POUND){	// <exp> → # <exp> <bin-op>
			matchToken(token);
			exp();
			binOp();
		} else if (token == TokenType.FIND){	// <exp> → find REGEX in  <file-name> <bin-op>
			matchToken(token);
			token = peekToken();
			if (token == TokenType.REGEX){
				matchToken(token);
				token = peekToken();
				if (token == TokenType.IN){
					matchToken(token);
					fileName();
					binOp();
				}
			}
		} else {
			throwException();
		}
	}
	
	public void fileName(){		//<file-name> → ASCII-STR
		TokenType token;
		token = peekToken();
		if(token == TokenType.ASCII){
			matchToken(token);
		}else {
			throwException();
		}
	}
	
	public void binOp(){		// <bin-op> → + <exp> | - <exp> | * <exp> | / <exp> | union <exp> | inters <exp> | ε
		TokenType token;
		token = peekToken();
		if (token == TokenType.PLUS){
			matchToken(token);
			exp();
		} else if (token == TokenType.MINUS){
			matchToken(token);
			exp();
		} else if (token == TokenType.STAR){
			matchToken(token);
			exp();
		} else if (token == TokenType.FSLASH){
			matchToken(token);
			exp();
		} else if (token == TokenType.UNIION){
			matchToken(token);
			exp();
		} else if (token == TokenType.INTERS){
			matchToken(token);
			exp();
		} 
	}
	
	
	
	private TokenType peekToken(){
		return null;
	}
	private void matchToken(TokenType token){
	}
	private void throwException(){};
	
}

