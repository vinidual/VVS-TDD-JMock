package br.com.Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class LexicalAnalyzerExecutor {
	
	private MockLexicalAnalyzerListener lal;
	private File file;
	
	private FileReader fr;
	
	@SuppressWarnings("unused")
	private BufferedReader br;
	
	private char []cbuffer;
	
	@SuppressWarnings("unused")
	private String fullpath;
	
	@SuppressWarnings("unused")
	private String filename;
	
	String lexema = "";
	int numLines = 1;
	String lexerror = "";
	int inerror = 0;
	
	private static enum Token {
		ID, NUM, RESERVED,
		SUM, SUB, MULT, DIV,
		LOW, GRE, LOWE, GREE,
		EQL, DIF,
		ATR,
		COM, COMPT,
		OPR, CPR, OBR, CBR, OKE, CKE,
		OCM, CCM,
		ERR
	}
	
	Token token;
	
	public void addListener(MockLexicalAnalyzerListener listener){
		this.lal = listener;
	}

	public void fileExist(String fullpath) {
		file = new File(fullpath);
		if( file.exists() ){
			lal.fileExist("file exists!");
			this.fullpath = fullpath;
		}
		else 
			lal.fileExist("file not found!");
	}

	public void validateExtension(String filename) {
		if( filename.endsWith(".cm") ){
			lal.fileValidateExtension("valid file extension!");
			this.filename = filename;
		}
		else
			lal.fileValidateExtension("invalid file extension!");
	}
	
	public void fileReader(){
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			cbuffer = new char[(int)file.length()];
			lal.fileReadable("file read successfully!");
		} catch (Exception ex) {
			lal.fileReadable("file cannot be read!");
			lal.fileError("error: " + ex.getMessage());
		}
	}

	public void bufferAnalyzer() {
		lexema = "";
		numLines = 1;
		lexerror = "";
		inerror = 0;
		for( char c : cbuffer ){
			String lex = Character.toString(c);
			if( inerror == 1 && !lex.matches("\n") && !lex.matches(";") )
				lexerror += lex;
			if( lex.matches("[a-zA-Z]") ){
				letterReaded(lex);
			}
			else if( lex.matches("[0-9]") ){
				numberReaded(lex);
			}
			else if( lex.matches("\\+") ){
				symbolReaded("SUM");
			}
			else if( lex.matches("-") ){
				symbolReaded("SUB");
			}
			else if( lex.matches("\\*") ){
				symbolReaded("MULT");
			}
			else if( lex.matches("/") ){
				symbolReaded("DIV");
			}
			else if( lex.matches("=") ){
				if( token == Token.ATR ){
					symbolReaded("EQL");
					token = null;
				}	
				else if( token == Token.DIF ){
					symbolReaded("DIF");
					token = null;
				}
				else if( token == Token.LOW ){
					symbolReaded("LOWE");
					token = null;
				}
				else if( token == Token.GRE ){
					symbolReaded("GREE");
					token = null;
				}
				else {
					symbolReaded("ATR");
					token = Token.ATR;
				}
			}
			else if( lex.matches("<") ){
				if( token != Token.LOW ){
					token = Token.LOW;
				}
				else {
					symbolReaded("ERR");
					lal.putError("<<");
				}
			}
			else if( lex.matches(">") ){
				if( token != Token.GRE ){
					token = Token.GRE;
				}
				else {
					symbolReaded("ERR");
					lal.putError(">>");
				}
			}
			else if( lex.matches("!") ){
				if( token != Token.DIF ){
					token = Token.DIF;
				}
				else {
					symbolReaded("ERR");
					lal.putError("!!");
				}
			}
			else if( lex.matches("\\(") ){
				symbolReaded("OPR");
			}
			else if( lex.matches("\\)") ){
				symbolReaded("CPR");
			}
			else if( lex.matches("\\[") ){
				symbolReaded("OBR");
			}
			else if( lex.matches("\\]") ){
				symbolReaded("CBR");
			}
			else if( lex.matches("\\{") ){
				symbolReaded("OKE");
			}
			else if( lex.matches("\\}") ){
				symbolReaded("CKE");
			}
			else if( lex.matches(",") ){
				symbolReaded("COM");
			}
			else if( lex.matches(";") ){
				lexerror += lex;
				symbolReaded("COMPT");
			}
			else if( lex.matches("[ |\t|\"|\'|\r|\n]") ){
				if( inerror == 1 && lexerror.compareTo("") != 0 ){
					lal.putError(lexerror);
					lexerror = "";
				}
				inerror = 0;
				if( lex.matches("\n") ){
					numLines += 1;
					inerror = 0;
				}
			}
			else {
				symbolReaded("ERR");
				if( inerror == 0 )
					lexerror += lex;
				inerror = 1;
			}
		}
		
	}
	
	public boolean checkTokenReserved( String str ) {
		if( str.compareTo("if") == 0 ||
			str.compareTo("else") == 0 ||
			str.compareTo("int") == 0 ||
			str.compareTo("void") == 0 ||
			str.compareTo("while") == 0 ||
			str.compareTo("return") == 0 
		)
			return true;
		return false;
	}
		
	public void letterReaded(String lex){
		if( lexema.matches("[0-9]+") ){
			lexema = lex;
			token = Token.NUM;
			lal.addToken("NUM");
		}
		else if( checkTokenReserved(lexema) ){
			lexema = lex;
			token = Token.RESERVED;
			lal.addToken("RESERVED");
		}
		else {
			lexema += lex;
			token = Token.ID;
		}
	}
	
	public void numberReaded(String lex){
		if( lexema.matches("[a-zA-Z]+") ){
			lexema += lex;
			token = Token.ID;
		}
		else if( checkTokenReserved(lexema) ){
			lexema = lex;
			token = Token.RESERVED;
			lal.addToken("RESERVED");
		}
		else {
			lexema += lex;
			token = Token.NUM;
		}
	}
	
	public void symbolReaded(String symbol){
		if( token == Token.NUM )
			lal.addToken("NUM");
		if( token == Token.ID )
			lal.addToken("ID");
		lal.addToken(symbol);
		lexema = "";
	}
	
	public void checkLastLexema(){
		if( lexema.matches("[a-zA-Z]+.*") )
			lal.addToken("ID");
		else
			if( lexema.matches("[0-9]+") )
				lal.addToken("NUM");
	}	

}
