package lexer;
import java.util.Map;
import java.util.HashMap;
import java.io.*; 

public class Lexer 
{
    public static int line = 1;
    private char peek = ' ';
    private Map<String,Token> keywordsToken=Map.of(
    		"assign",Word.assign,
    		"to",Word.to,
    		"if",Word.iftok,
    		"else",Word.elsetok,
    		"while",Word.whiletok,
    		"begin",Word.begin,
    		"end",Word.end,
    		"print",Word.print,
    		"read",Word.read
    		);    
    
    private void readch(BufferedReader br)
    {	
    	try
    	{
            peek = (char) br.read();
        } 
    	catch (IOException exc)
    	{
            peek = (char) -1; // ERROR
        }
    }

    public Token lexical_scan(BufferedReader br) 
    {
        while (peek == ' ' || peek == '\t' || peek == '\n'  || peek == '\r') 
        {
            if (peek == '\n') line++;
            readch(br);
        }
        
        switch (peek) 
        {

	// ... gestire i casi di ( ) { } + - * / ; , ... //
	        case '!': peek = ' '; return Token.not;
	        case '(': peek = ' '; return Token.lpt;
            case ')': peek = ' '; return Token.rpt;
            case '{': peek = ' '; return Token.lpg;
            case '}': peek = ' '; return Token.rpg;
            case '+': peek = ' '; return Token.plus;
            case '-': peek = ' '; return Token.minus;
            case '*': peek = ' '; return Token.mult;
            case '/': peek = ' '; return Token.div;
            case ';': peek = ' '; return Token.semicolon;
            case ',': peek = ' '; return Token.comma;

	// ... gestire i casi di || < > <= >= == <> ... //
            case '&':
                readch(br);
                if (peek == '&') 
                {
                    peek = ' ';
                    return Word.and;
                }
                else 
                {
                    System.err.println("Erroneous character"
                            + " after & : "  + peek );
                    return null;
                }
            
             case '|':
                readch(br);
                if (peek == '|')
                {
                    peek = ' ';
                    return Word.or;
                }
                else
                {
                    System.err.println("Erroneous character"
                            + " after | : "  + peek );
                    return null;
                } 

            case '=':
                readch(br);
                if (peek == '=')
                {
                    peek = ' ';
                    return Word.eq;
                }
                else 
                {
                    System.err.println("Erroneous character"
                            + " after = : "  + peek );
                    return null;
                }

            case '<':
                readch(br);
                
                if(peek=='>')
                {
                    peek = ' ';
                    return Word.ne;
                }
                else if(peek=='=')
                {
                    peek = ' ';
                    return Word.le; 
                }
                else
                {
                    //lascio peek per la prox. iterazione
                    return Word.lt;
                }

            case '>':
                readch(br);
                
                if(peek=='=')
                {
                    peek = ' ';
                    return Word.ge;
                }
                else 
                {
                    //lascio peek per la prox. iterazione
                	return Word.gt;
                }

            case (char)-1:
                return new Token(Tag.EOF);
            	
            default:
                if (Character.isLetter(peek))
                {
                	// leggo l'id.
                	// all'uscita dell'if lascio il carattere successivo alla stringa in peek
                	String string = ""+peek;
                    Token tmpResult;
                    
                    readch(br);
                    while(Character.isLetter(peek) || Character.isDigit(peek)){
                        string+=peek;
                        readch(br);
                    }
                    
                    tmpResult=keywordsToken.get(string);
                    if(tmpResult!=null) 
                    	return tmpResult;
                    else 
                    	return new Word(Tag.ID,string);
                }
                else if (Character.isDigit(peek))
                {
                    String string = ""+peek;
                    if(peek=='0')
                    {
                    	readch(br);
                    	if(Character.isDigit(peek)) {
                            System.err.println("error: Number continued after initial 0: " 
                                    + (string+peek));                    		
                    		return null;
                    	}else {
                    		//lascio peek per prossima iterazione	
                    		return new NumberTok(Tag.NUM,0); 
                    	}
                    }
                    else {
	                    readch(br); 
	                    while(Character.isDigit(peek))
	                    {
	                        string+=peek;
	                        readch(br);
	                    }
	                    //lascio peek per prox. iterazione
	                	return new NumberTok(Tag.NUM,Integer.parseInt(string)); 
                    }            
                } 
                else
                {
                        System.err.println("Erroneous character: " + peek );
                        return null;
                }
         }
    }
		
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "input.txt"; // il percorso del file da leggere
        try 
        {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Token tok;
            do 
            {
                tok = lex.lexical_scan(br);
                if(tok!=null)
                	System.out.println("Scan: " + tok);
            } while (tok!=null && tok.tag != Tag.EOF);
        
            br.close();
        } catch (IOException e) {e.printStackTrace();}    
    }

}
