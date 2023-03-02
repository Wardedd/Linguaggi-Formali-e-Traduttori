package valutatore;
import java.io.*;
import lexer.Lexer;
import lexer.Token;
import lexer.NumberTok;
import lexer.Tag;

public class Valutatore {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Valutatore(Lexer l, BufferedReader br) {
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
    	int expr_val;
    	
    	switch(look.tag) {
			case Tag.NUM:
			case '(':
				expr_val=expr();
				match(Tag.EOF);
				System.out.println(expr_val);
	 			break;
	 		default:
	 			error(getErrorSyntaxMsg("start","( or Number"));
	 			break;
    	}
    }

    private int expr() {
    	int expr_val=-1;
    	
    	int term_val;
    	int exprp_i;
    	
    	switch(look.tag) {
			case Tag.NUM:
			case '(':
				term_val=term();
				exprp_i=term_val;
	    		expr_val=exprp(exprp_i);
	 			break;
			default:
	 			error(getErrorSyntaxMsg("expr","( or Number"));
	 			break;
    	}
    	
    	return expr_val;
    }

    private int exprp(int exprp_i) {
    	int exprp_val=-1;
    	
    	int term_val;
    	int exprp1_val;
    	int exprp1_i;
    	
		switch (look.tag) {
			case '+':
				match('+');
				term_val=term();
				
				exprp1_i=exprp_i+term_val;
				
				exprp1_val=exprp(exprp1_i);
				
				exprp_val=exprp1_val;
				break;
			case '-':
				match('-');
				term_val=term();
				
				exprp1_i=exprp_i-term_val;
				
				exprp1_val=exprp(exprp1_i);
				
				exprp_val=exprp1_val;
				break;
			case Tag.EOF:
			case ')':
				//E'->ε
				exprp_val=exprp_i;
				break;
			default:
	 			error(getErrorSyntaxMsg("exprp","+ or - or EOF or )"));
	 			break;
		}
		return exprp_val;
    }

    private int term() {
    	int term_val=-1;
    	
    	int termp_val;
    	int termp_i;
    	
    	switch(look.tag) {
			case Tag.NUM:
			case '(':
		   		termp_i=fact();
	    		termp_val=termp(termp_i);
	    		term_val=termp_val;
	 			break;
			default:
	 			error(getErrorSyntaxMsg("term","NUM or ("));
	 			break;
    	}
    	return term_val;
    }

    private int termp(int termp_i) {
    	int termp_val=-1;
    	
    	int termp1_i;
    	int termp1_val;
    	int fact_val;
    	
    	switch(look.tag) {
			case '*':
				match('*');
				fact_val=fact();
				termp1_i=termp_i*fact_val;
				termp1_val=termp(termp1_i);
				termp_val=termp1_val;
				break;
			case '/':
				match('/');
				fact_val=fact();
				termp1_i=termp_i/fact_val;
				termp1_val=termp(termp1_i);
				termp_val=termp1_val;
				break;
		    case '+':
			case '-':
			case Tag.EOF:
			case ')':	
				//E'->ε
				termp_val=termp_i;
				break;
			default:
	 			error(getErrorSyntaxMsg("termp","* or / or + or - or EOF or )"));
	 			break;
    	}
    	return termp_val;
    }

    private int fact() {
    	int fact_val=-1;

    	int expr_val;
    	
    	switch(look.tag) {
    		case Tag.NUM:{
    			fact_val=((NumberTok)look).number; //se faccio dopo Match non ho + il token del numero!
        		match(Tag.NUM);  
    		}
        		break;
    		case '(':
        		match('(');
        		expr_val=expr();
        		match(')');
        		fact_val=expr_val;
    			break;
    		default:
	 			error(getErrorSyntaxMsg("termp","( or Number"));
	 			break;
    	}
    	return fact_val;
	}
	
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "input.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Valutatore parser = new Valutatore(lex, br);
            parser.start();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}