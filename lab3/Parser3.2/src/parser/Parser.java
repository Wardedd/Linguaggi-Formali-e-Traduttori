package parser;
import java.io.*;
import lexer.*;

public class Parser {
    private Lexer lex; 
    private BufferedReader pbr;
    private Token look;

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
    	throw new Error("near line " + lex.line + ": " + s);
    }

    void match(int t) {
		if (look.tag == t) {
		    if (look.tag != Tag.EOF) move();
		} else error("syntax error");
    }
    
    private String getErrorSyntaxMsg(String fnName) {
		return "in function "+fnName;
    }

    public void start() {
    	prog();
    }
    
    public void prog() { 
    	
    	switch(look.tag) {
			case Tag.ASSIGN:
			case Tag.PRINT:
			case Tag.READ:
			case Tag.WHILE:
			case Tag.IF:
			case '{':
				statlist();
				match(Tag.EOF);
				break;
	
	 		default:
	 			error(getErrorSyntaxMsg("prog"));
	 			break;
    	}
    }
    
    
    public void statlist(){
    	
    	switch(look.tag) {
			case Tag.ASSIGN:
			case Tag.PRINT:
			case Tag.READ:
			case Tag.WHILE:
			case Tag.IF:
			case '{':
				stat();
				statlistp();
				break;
	
	 		default:
	 			error(getErrorSyntaxMsg("statlist"));
	 			break;
    	}
    }
    
    public void statlistp(){
    	
    	switch(look.tag) {
	    	case ';':
	    		match(';');
	    		stat();
	    		statlistp();
				break;
				
    		case Tag.EOF:
			case '}':
				//epsilon
				break;
			
	 		default:
	 			error(getErrorSyntaxMsg("statlistp"));
	 			break;
    	}
    }
    
    public void stat(){
    	
    	switch(look.tag) {
			case Tag.ASSIGN:
				match(Tag.ASSIGN);
				expr();
				match(Tag.TO);
				idlist();
				break;
				
			case Tag.PRINT:
				match(Tag.PRINT);
				match('(');
				exprlist();
				match(')');
				break;
			
			case Tag.READ:
				match(Tag.READ);
				match('(');
				idlist();
				match(')');
				break;
			
			case Tag.WHILE:
				match(Tag.WHILE);
				match('(');
				bexpr();
				match(')');
				stat();
				break;
			
			case Tag.IF:
				match(Tag.IF);
				match('(');
				bexpr();
				match(')');
				stat();
				ifp();
				break;
				
			case '{':
				match('{');
				statlist();
				match('}');
				break;
				
	 		default:
	 			error(getErrorSyntaxMsg("stat"));
	 			break;
		}	
    }
    
    public void ifp() {
    	
    	switch(look.tag) {
    		case Tag.END:
    			match(Tag.END);
    			break;
    			
    		case Tag.ELSE:
    			match(Tag.ELSE);
    			stat();
    			match(Tag.END);
    			break;
    			
	 		default:
	 			error(getErrorSyntaxMsg("ifp"));
	 			break;
    	}
    }
    
    public void idlist(){
    	
    	switch(look.tag) {
			case Tag.ID:
				match(Tag.ID);
				idlistp();
				break;
				
	 		default:
	 			error(getErrorSyntaxMsg("idlist"));
	 			break;
    	}
    }
    
    public void idlistp(){
    	
    	switch(look.tag) {
			case ',':
				match(',');
				match(Tag.ID);
				idlistp();
				break;
    		case Tag.END:
    		case Tag.ELSE:
    		case Tag.EOF:
    		case '}':
    		case ';':
    		case ')':
    			//epsilon
    			break;
    			
	 		default:
	 			error(getErrorSyntaxMsg("idlistp"));
	 			break;
    	}
    }
    
    public void bexpr (){
    	
    	switch(look.tag) {
			case Tag.RELOP:
				match(Tag.RELOP);
				expr();
				expr();
				break;
				
	 		default:
	 			error(getErrorSyntaxMsg("bexpr"));
	 			break;
    	}
    }
    
    public void expr (){
    	
    	switch(look.tag) {
    		case '+':
				match('+');
				match('(');
				exprlist();
				match(')');
				break;
			
			case '*':
				match('*');
				match('(');
				exprlist();
				match(')');
				break;
			
			case '/':
				match('/');
				expr();
				expr();
				break;
			
			case '-':
				match('-');
				expr();
				expr();
				break;
			
			case Tag.NUM:
				match(Tag.NUM);
				break;
				
			case Tag.ID:
				match(Tag.ID);
				break;
				
	 		default:
	 			error(getErrorSyntaxMsg("expr"));
	 			break;
    	}
    }
       
    public void exprlist(){

    	switch(look.tag) {
			case Tag.NUM:
			case Tag.ID:
			case '+':
			case '-':
			case '*':
			case '/':
				expr();
				exprlistp();
				break;
		
			default:
	 			error(getErrorSyntaxMsg("exprlist"));
	 			break;
    	}
    }
	
    public void exprlistp(){

    	switch(look.tag) {
			case ',':
				match(',');
				expr();
				exprlistp();
				break;
			
			case ')':
				//epsilon
				break;
				
	 		default:
	 			error(getErrorSyntaxMsg("exprlistp"));
	 			break;
    	}
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "max_tre_num.lft"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.start(); //calls parser.prog();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}