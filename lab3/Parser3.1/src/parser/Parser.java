package parser;
import java.io.*;
import lexer.Lexer;
import lexer.Token;
import lexer.Tag;

public class Parser {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;
    
    /* look==null:errore rilevato dal lexer - throw NullPointerException*/
    
    public Parser(Lexer l, BufferedReader br) {
        lex = l;
        pbr = br;
        move();
    }

    void move() {
        look = lex.lexical_scan(pbr);
        System.out.println("token = " + look);
    }

    void error(String s) {
    	throw new Error("near line " + Lexer.line + ": " + s);
    }

    void match(int t) {
		if (look.tag == t) {
		    if (look.tag != Tag.EOF) move();
		} else error("syntax error");
    }

    private String getErrorSyntaxMsg(String fnName,String expected) {
    	if(look==null)
    		return "error in function "+fnName;
		return "in function "+fnName+". Expected following tokens  <"+expected+"> , found token: "+ look;
	}
    
    public void start() {
    	switch(look.tag) {
			case Tag.NUM:
			case '(':
				expr();
				match(Tag.EOF);
	 			break;
	 		default:
	 			error(getErrorSyntaxMsg("start","( or Number"));
	 			break;
    	}
    }

    private void expr() {
    	
    	switch(look.tag) {
			case Tag.NUM:
			case '(':
				term();
	    		exprp();
	 			break;
			default:
	 			error(getErrorSyntaxMsg("expr","( or Number"));
	 			break;
    	}
    }

    private void exprp() {
		switch (look.tag) {
			case '+':
				match('+');
				term();
				exprp();
				break;
			case '-':
				match('-');
				term();
				exprp();
				break;
			case Tag.EOF:
			case ')':
				//E'->ε
				break;
			default:
	 			error(getErrorSyntaxMsg("exprp","+ or - or EOF or )"));
	 			break;
		}
    }

    private void term() {
    	switch(look.tag) {
			case Tag.NUM:
			case '(':
		   		fact();
	    		termp();    		
	 			break;
			default:
	 			error(getErrorSyntaxMsg("term","NUM or ("));
	 			break;
    	}
    }

    private void termp() {
    	switch(look.tag) {
			case '*':
				match('*');
				fact();
				termp();
				break;
			case '/':
				match('/');
				fact();
				termp();
				break;
		    case '+':
			case '-':
			case Tag.EOF:
			case ')':	
				//E'->ε
				break;
			default:
	 			error(getErrorSyntaxMsg("termp","* or / or + or - or EOF or )"));
	 			break;
    	}
    }

    private void fact() {
    	switch(look.tag) {
    		case Tag.NUM:
        		match(Tag.NUM);    			
    			break;
    		case '(':
        		match('(');
        		expr();
        		match(')');
    			break;
    		default:
	 			error(getErrorSyntaxMsg("termp","( or Number"));
	 			break;
    	}
	}
	
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "input.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.start();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}