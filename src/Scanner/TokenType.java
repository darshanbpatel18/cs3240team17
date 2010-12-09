package Scanner;

/**
 * The TokenType enum encapsulates all the possible  tokens the scanner outputs. 
 */
public enum TokenType {
	BEGIN,		//begin
	END,		//end	
	ID,			//ID
	ASSIGN, 	//:=
	SEMI,		//;
	REPLACE,	//replace
	REGEX,		//REGEX
	WITH,		//with
	ASCII,		//ASCII-STR
	IN,			//in
	ARROW, 		//->
	PRINT,		//print
	LPAREN,		//(
	RPAREN,		//)
	COMMA,		//,
	INTNUM,		//INTNUM
	POUND,		//#
	FIND,		//find
	PLUS,		//+
	MINUS,		//-
	STAR,		//*
	FSLASH,		///
	UNIION,		//union
	INTERS,		//inters
	SQUOTE,		//'
	RECHAR,		//RE_CHAR
	UNION,		//UNION
	CLSCHAR, 	//CLS_CHAR
	PERIOD,		//.
	LBRACKET, 	//[
	RBRACKET,	//]
	CARROT		//^
}