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

    public Token lexical_scan(BufferedReader br){
    	
       while (peek == ' ' || peek == '\t' || peek == '\n'  || peek == '\r' || peek=='/') 
        {
        	if(peek=='/') {
        		readch(br);
        		if(peek=='*')
        		{ 
        			/* implemento parte del dfa 1.10 */
        			int state=2;
        			do {
        				readch(br);
                		if (peek == '\n') line++;
                		
        				switch (state) {
	        				case 2:
	        					if(peek=='*')
	        						state=3;
	        					else if(peek==(char)-1)
	        						state=-1;
	        					else 
	        					{
	    							//rimani in 2
	        					}
	        						break;
	        					

	        				case 3:
	        					if (peek=='/')
	        						state=0;
	        					else if(peek==(char)-1)
	        						state=-1;
	        					else if(peek=='*')
	        					{   
	        						//rimani in 3
	        					}
	        					else 
	        						state=2;
	        					break;
        				}
        			}while(state!=0 && state!=-1);
        			
        			if(state==0) //usciti correttamente da sezione di commenti
        				readch(br);
        			else {
        				System.err.println("Sezione di commenti ( /*...*/ ) iniziata ma non terminata");
        				return null;
        			}
        		}
        		else if(peek=='/')
        		{
        			while(peek != '\n' && peek!=(char)-1)
        				readch(br);
        			//se peek = \n, line viene incrementato alla prossima iterazione del while
        			//se peek ==-1, va nello switch e ritorna Tag.EOF
        		}
        		else {
            		return Token.div;
        		}
        	}
        	else {
        		if (peek == '\n') line++;
        		readch(br);
            }
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
                    while(Character.isLetter(peek) || Character.isDigit(peek) || peek=='_'){
                        string+=peek;
                        readch(br);
                    }
                    
                    tmpResult=keywordsToken.get(string);
                    if(tmpResult!=null) 
                    	return tmpResult;
                    else 
                    	return new Word(Tag.ID,string);
                }
                else if(peek=='_')
                {
                	// leggo l'id.
                	// all'uscita dell'if lascio il carattere successivo alla stringa in peek
                	String string = "_";
                    Token tmpResult;
                    
                    readch(br);
                    while(peek=='_') {
                    	string+=peek;
                    	readch(br);
                    }
                    
                    if(Character.isLetter(peek) || Character.isDigit(peek)) {
                    	string+=peek;
                    	readch(br);
                        while(Character.isLetter(peek) || Character.isDigit(peek) || peek=='_'){
                            string+=peek;
                            readch(br);
                        }
                        /* attualmente non ci sono keyword che iniziano con l'underscore, 
                         * ma tengo la seguente verifica nel caso si aggiungono */
                        tmpResult=keywordsToken.get(string);
                        if(tmpResult!=null) 
                        	return tmpResult;
                        else 
                        	return new Word(Tag.ID,string);
                    }
                    
                    //stringa è composta di soli underscore
                    System.err.println("error: Id is composed by only underscores: "+string);
                    return null;
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
}
