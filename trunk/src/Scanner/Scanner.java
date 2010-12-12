package Scanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.io.FileInputStream;


public class Scanner {
	private TokenType nextTokenType;
	private String	nextTokenValue;
	private int lineNumber, colNumber;
	private PushbackInputStream inputFileReader;
	private FileOutputStream logFileWriter;
	private boolean peeked;	
	
	public Scanner(String inputFile, String logFile) throws ScanException{
		try {
			inputFileReader = new PushbackInputStream(new FileInputStream(inputFile));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			ScanException ex = new ScanException("Input file could not be found!");
			ex.setStackTrace(e1.getStackTrace());
			throw ex;
		} 
		File log = new File(logFile);
		if(!log.exists()) {
			try {
				log.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				ScanException ex = new ScanException("Log file did not exist and could not be created!");
				ex.setStackTrace(e.getStackTrace());
				throw ex;
			}
		}
		try {
			logFileWriter = new FileOutputStream(log, false);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			ScanException ex = new ScanException("Log file did not exist and could not be created!");
			ex.setStackTrace(e1.getStackTrace());
			throw ex;
		}
		peeked = false;
		lineNumber = 0;
		colNumber = 0;
		ExtractToken();		
	}
	
	public void Close() {
		try {
			logFileWriter.close();
			logFileWriter.flush();
			inputFileReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public TokenType PeekToken() throws ScanException {
		if(!peeked) {
			ExtractToken();	
		}	
		return nextTokenType;
	}
	
	public Token GetNextToken() throws ScanException {
		if(!peeked) {
			ExtractToken();		
		}
		peeked = false;
		return new Token(nextTokenType, nextTokenValue, lineNumber);
	}
	
	private enum ScannerState {
		STARTED,
		REGEX,
		ASCII,
		INTNUM,
		KEYWORD,
		ID
	}
	
	private void ExtractToken() throws ScanException {
		try {
			boolean valid = false, keepLooking = true;
			int next = inputFileReader.read();
			TokenType tokenType = TokenType.ID; 
			ScannerState scannerState = ScannerState.STARTED;
			StringBuilder output = new StringBuilder();
			
			char[] matchStr = new char[0]; 
			int matchPos = 0;
			while(next > -1 && keepLooking){
				char nextChar = (char)next;
				switch(scannerState){
					case STARTED:
					if(nextChar == '\n') {
						++lineNumber;
						colNumber = -1;
					}
					else if(nextChar == '\''){
						scannerState = ScannerState.REGEX;
						tokenType = TokenType.REGEX;
					}
					else if(nextChar == '\"') {
						scannerState = ScannerState.ASCII;
						tokenType = TokenType.ASCII;
					}
					else if(IsNumber(next)) {
						if(nextChar == '0'){
							keepLooking = false;
						}
						scannerState = ScannerState.INTNUM;
						tokenType = TokenType.INTNUM;
						output.append(nextChar);
					}
					else if(nextChar == '+'){
						tokenType = TokenType.PLUS;
						valid = true;
						keepLooking = false;
						output.append(nextChar);
					}
					else if(nextChar == '*'){
						tokenType = TokenType.STAR;
						valid = true;
						keepLooking = false;
						output.append(nextChar);
					}
					else if(nextChar == '/'){
						tokenType = TokenType.FSLASH;
						valid = true;
						keepLooking = false;
						output.append(nextChar);
					}
					else if(nextChar == '('){
						tokenType = TokenType.LPAREN;
						valid = true;
						keepLooking = false;
						output.append(nextChar);
					}
					else if(nextChar == ')'){
						tokenType = TokenType.RPAREN;
						valid = true;
						keepLooking = false;
						output.append(nextChar);
					}
					else if(nextChar == ','){
						tokenType = TokenType.COMMA;
						valid = true;
						keepLooking = false;
						output.append(nextChar);
					}
					else if(nextChar == ';'){
						tokenType = TokenType.SEMI;
						valid = true;
						keepLooking = false;
						output.append(nextChar);
					}
					else if(nextChar == ':') {
						int lookAhead = inputFileReader.read();
						if((char)lookAhead == '=') {
							tokenType = TokenType.ASSIGN;
							valid = true;
							keepLooking = false;
							output.append(nextChar);
							output.append((char)lookAhead);
							++colNumber;
						}
						else {
							++colNumber;
							keepLooking = false;
						}
					}
					else if(nextChar == '-')
					{
						int lookAhead = inputFileReader.read();
						if((char)lookAhead == '>') {
							tokenType = TokenType.ARROW;
							valid = true;
							keepLooking = false;
							output.append(nextChar);
							output.append((char)lookAhead);
							++colNumber;
						}
						else if(!IsNumber(lookAhead)) {
							tokenType = TokenType.MINUS;
							valid = true;
							keepLooking = false;
							output.append(nextChar);
							if(lookAhead != -1) {
								inputFileReader.unread(lookAhead);
							}
						}
						else if(((char)lookAhead) == '0') {
							++colNumber;
							keepLooking = false;
						}
						else {
							inputFileReader.unread(lookAhead);
							output.append(nextChar);
							scannerState = ScannerState.INTNUM;
							tokenType = TokenType.INTNUM;
						}			
					}
					else if(nextChar != ' ' && nextChar != '\r') {
						scannerState = ScannerState.KEYWORD;
						output.append(nextChar);
						switch(nextChar) {
						case 'w':
							tokenType = TokenType.WITH;
							matchStr = new char[] {'i', 't', 'h'};
							break;
						case 'u':
							tokenType = TokenType.UNION;
							matchStr = new char[] {'n', 'i', 'o', 'n'};
							break;
						case 'r':
							tokenType = TokenType.REPLACE;
							matchStr = new char[] {'e', 'p', 'l', 'a', 'c', 'e'};
							break;
						case 'b':
							tokenType = TokenType.BEGIN;
							matchStr = new char[] {'e', 'g', 'i', 'n'};
							break;
						case 'e':
							tokenType = TokenType.END;
							matchStr = new char[] {'n', 'd'};
							break;
						case 'p':
							tokenType = TokenType.PRINT;
							matchStr = new char[] {'r', 'i', 'n', 't'};
							break;
						case 'i':
							tokenType = TokenType.IN;
							matchStr = new char[] {'n'};
							break;
						case 'f':
							tokenType = TokenType.FIND;
							matchStr = new char[] {'i', 'n', 'd'};
							break;
						default:
							if(IsLetter(next)){
								scannerState = ScannerState.ID;
								tokenType = TokenType.ID;
							}
							else {
								keepLooking = false;
								output.deleteCharAt(0);
							}
							break;
						}
					}
					break;
					case ASCII:
						if(nextChar == '\"') {
							valid = true;
							keepLooking = false;
						}
						else if (IsPrintable(next)){
							output.append(nextChar);
						}
						else {
							keepLooking = false;
						}
						break;
					case REGEX:
						if(nextChar == '\\') {
							int lookAhead = inputFileReader.read();
							if((char)lookAhead == '\'') {
								output.append(nextChar);
								output.append((char)lookAhead);
								++colNumber;
							}	
							else {
								if(lookAhead != -1) {
									inputFileReader.unread(lookAhead);
								}
								output.append(nextChar);
							}
						}
						else if(nextChar == '\'') {
							valid = true;
							keepLooking = false;
						}
						else if (IsPrintable(next)){
							output.append(nextChar);
						}
						else {
							keepLooking = false;
						}
						break;
					case INTNUM:
						if(IsNumber(next)){
							output.append(nextChar);
						}
						else {
							inputFileReader.unread(next);
							valid = true;
							keepLooking = false;
						}
						break;
					case ID:
						if(IsNumber(next) || IsLetter(next) || nextChar == '_'){
							output.append(nextChar);
						}
						else {
							inputFileReader.unread(next);
							valid = true;
							keepLooking = false;
						}
						break;
					case KEYWORD:
						if(matchPos >= matchStr.length) {
							if(tokenType == TokenType.IN && nextChar == 't') {
								tokenType = TokenType.INTERS;
								matchStr = new char[] {'e', 'r', 's'};
								matchPos = 0;
								output.append(nextChar);
							}
							else {
								inputFileReader.unread(next);
								valid = true;
								keepLooking = false;
							}
						}
						else if(nextChar == matchStr[matchPos]){
							output.append(nextChar);
							++matchPos;
						}
						else if(IsNumber(next) || IsLetter(next) || nextChar == '_') {
							scannerState = ScannerState.ID;
							tokenType = TokenType.ID;
							output.append(nextChar);
						}
						else {
							keepLooking = false;
						}
						break;					
				}
				if(keepLooking) {
					next = inputFileReader.read();
					++colNumber;
				}
			}
			if (valid){
				nextTokenType = tokenType;
				nextTokenValue = output.toString();
				logFileWriter.write((nextTokenType.toString() + ": " + nextTokenValue + " at: " + lineNumber +", " +colNumber +  System.getProperty( "line.separator" )).getBytes()); 
			}
			else if(next == -1){ 
				if(scannerState == ScannerState.STARTED) {			
					nextTokenType = TokenType.EOF;
					nextTokenValue = "$";
					logFileWriter.write("End of file reached!".getBytes());
				}
				else if(tokenType == TokenType.END && matchPos >= matchStr.length){
					nextTokenType = tokenType;
					nextTokenValue = output.toString();
					logFileWriter.write((nextTokenType.toString() + ": " + nextTokenValue + " at: " + lineNumber +", " +colNumber +  System.getProperty( "line.separator" )).getBytes()); 
				}
			}
			else {
				String error = "Unexpected character found: " + (char)next + "(" + next +") at: "+ lineNumber +", " +colNumber;
				logFileWriter.write(( error +  System.getProperty( "line.separator" )).getBytes());
				ScanException ex = new ScanException(error);
				ex.fillInStackTrace();
				throw ex;
			}
		}  catch (IOException e) {
			e.printStackTrace();
			ScanException ex = new ScanException("Error reading file!");
			ex.setStackTrace(e.getStackTrace());
			throw ex;
		}
	}
	
	private boolean IsLetter(int character) {
		return ((character > 64 && character < 91) || (character > 96 && character < 123));
	}
	
	private boolean IsNumber(int character){
		return (character > 47 && character < 58);
	}
	
	private boolean IsPrintable(int character) {
		return (character > 31 && character < 127);
	}
}
