package translator;
import java.io.*;
import java.util.*;

import lexer.*;

public class Translator {
    private Lexer lex; 
    private BufferedReader pbr;
    private Token look;
    
    SymbolTable st = new SymbolTable();
    CodeGenerator code = new CodeGenerator();
    int count=0;

    private Instruction[] idList_assignInstrList = {
    		new Instruction(OpCode.dup),
    		new Instruction(OpCode.istore,-1) /* operand -1 = operand is going to be filled 
    													by lookupAddres(id) in <idlist> */
    };
    
    private Instruction[] idList_readInstrList = {
    		new Instruction(OpCode.invokestatic,0),
    		new Instruction(OpCode.istore,-1)
    };

    private Instruction[] exprList_print = {
    		new Instruction(OpCode.invokestatic,1)
    };

    public Translator(Lexer l, BufferedReader br) {
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

    public OpCode getRelopCmp(Token relopToken) { //checks if reference is the same!
    	if(relopToken==Word.lt)
    		return OpCode.if_icmplt;
    	else if(relopToken==Word.gt)
    		return OpCode.if_icmpgt;
    	else if(relopToken==Word.eq)
    		return OpCode.if_icmpeq;
    	else if(relopToken==Word.le)
    		return OpCode.if_icmple;
    	else if(relopToken==Word.ne)
    		return OpCode.if_icmpne;
    	else if(relopToken==Word.ge)
    		return OpCode.if_icmpge;
    	else return null;
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
			{
		        int lnext_prog = code.newLabel();
		        statlist(lnext_prog);
		        code.emitLabel(lnext_prog);
		        match(Tag.EOF);
		        
		        //Scrivo il codice su file Output.j
		        try {
		        	code.toJasmin();
		        	System.out.println("done");
		        }
		        catch(java.io.IOException e) {
		        	System.out.println("IO error\n");
		        };
		        break;
			}
	
	 		default:
	 			error(getErrorSyntaxMsg("prog"));
	 			break;
    	}	
    }
    
    
    public void statlist(int statlist_next){
    	
    	switch(look.tag) {
			case Tag.ASSIGN:
			case Tag.PRINT:
			case Tag.READ:
			case Tag.WHILE:
			case Tag.IF:
			case '{':
			{
				int stat_next=code.newLabel();
				stat(stat_next);
				code.emitLabel(stat_next);
				statlistp(statlist_next);
				break;
			}
	 		default:
	 			error(getErrorSyntaxMsg("statlist"));
	 			break;
    	}
    }
    
    public void statlistp(int statlistp_next){
    	
    	switch(look.tag) {
	    	case ';':
	    	{
	    		match(';');
	    		int stat_next=code.newLabel();
	    		stat(stat_next);
				code.emitLabel(stat_next);
	    		statlistp(statlistp_next);
	    	}
	    	break;
				
    		case Tag.EOF:
			case '}':
				//epsilon
				code.emit(OpCode.GOto, statlistp_next);
				break;
			
	 		default:
	 			error(getErrorSyntaxMsg("statlistp"));
	 			break;
    	}
    }
    
    public void stat(int stat_next){
    	
    	switch(look.tag) {
			case Tag.ASSIGN:
				match(Tag.ASSIGN);
				expr();
				match(Tag.TO);
				idlist(idList_assignInstrList);
				code.emit(OpCode.pop);
				code.emit(OpCode.GOto,stat_next);
				break;
				
			case Tag.PRINT:
			{
				int exprlist_n;
				match(Tag.PRINT);
				match('(');
				exprlist_n=exprlist(exprList_print);
				match(')');
				
				code.emit(OpCode.GOto,stat_next);
			}
				break;
			
			case Tag.READ:
				match(Tag.READ);
				match('(');
				idlist(idList_readInstrList);
				match(')');
				code.emit(OpCode.GOto,stat_next);
				break;
			
			case Tag.WHILE:
			{
				int bexpr_true;
				int stat1_next;

				match(Tag.WHILE);
				match('(');				
				bexpr_true=code.newLabel();
				stat1_next=code.newLabel();
				code.emitLabel(stat1_next);
				bexpr(bexpr_true,stat_next);
				match(')');
				code.emitLabel(bexpr_true);
				stat(stat1_next);
			
				break;
			}
			
			case Tag.IF:
			{
				int bexpr_true,bexpr_false;

				match(Tag.IF);
				match('(');
				bexpr_true=code.newLabel();
				bexpr_false=code.newLabel();
				bexpr(bexpr_true,bexpr_false);
				match(')');
				code.emitLabel(bexpr_true);
				stat(stat_next);
				ifp(stat_next,bexpr_false);
				
				break;
			}
			case '{':
			{
				match('{');
				statlist(stat_next);
				match('}');
			}
				break;
				
	 		default:
	 			error(getErrorSyntaxMsg("stat"));
	 			break;
		}	
    }
    
    public void ifp(int ifp_next,int ifp_false) {
    	
    	switch(look.tag) {
    		case Tag.END:
    			match(Tag.END);
    			code.emitLabel(ifp_false);
    			code.emit(OpCode.GOto,ifp_next);
    			break;
    			
    		case Tag.ELSE:
    			match(Tag.ELSE);
    			code.emitLabel(ifp_false);
    			stat(ifp_next);
    			match(Tag.END);
    			break;
    			
	 		default:
	 			error(getErrorSyntaxMsg("ifp"));
	 			break;
    	}
    }
    
    public void idlist(Instruction[] idlist_instr){
    	
    	switch(look.tag) {
			case Tag.ID:
			{		
				Token tmp = look; //aggiungo tmp anche se non presente in SDT, perchè la funzione match() consuma il Token
				match(Tag.ID);	
				//inizio azione 
				int id_addr = st.lookupAddress(((Word)tmp).lexeme);
                if (id_addr==-1) {
                    id_addr = count;
                    st.insert(((Word)tmp).lexeme,count++);
                }
				for(int i=0;i<idlist_instr.length;i++) {
					if(idlist_instr[i].operand!=-1)
						code.emit(idlist_instr[i].opCode,idlist_instr[i].operand);
					else
					{
						code.emit(idlist_instr[i].opCode,id_addr);
						break;
					}
				}
				//fine azione 			
				idlistp(idlist_instr);
				break;
			}
	 		default:
	 			error(getErrorSyntaxMsg("idlist"));
	 			break;
    	}
    }
    
    public void idlistp(Instruction[] idlistp_instr){
    	
    	switch(look.tag) {
			case ',':
				match(',');
				Token tmp = look; //aggiungo tmp anche se non presente in SDT, perchè la funzione match() consuma il Token
				match(Tag.ID);
				//inizio azione 
				int id_addr = st.lookupAddress(((Word)tmp).lexeme);
                if (id_addr==-1) {
                    id_addr = count;
                    st.insert(((Word)tmp).lexeme,count++);
                }
				for(int i=0;i<idlistp_instr.length;i++) {
					if(idlistp_instr[i].operand!=-1)
						code.emit(idlistp_instr[i].opCode,idlistp_instr[i].operand);
					else
					{
						code.emit(idlistp_instr[i].opCode,id_addr);
						break;
					}
				}
				//fine azione 	
				idlistp(idlistp_instr);
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
    
    public void bexpr (int b_true,int b_false){
    	
    	switch(look.tag) {
			case Tag.RELOP:
			{
				Token tmp = look;
				match(Tag.RELOP);
				expr();
				expr();
				code.emit(getRelopCmp(tmp),b_true);
				code.emit(OpCode.GOto,b_false);
				break;
			}	
	 		default:
	 			error(getErrorSyntaxMsg("bexpr"));
	 			break;
    	}
    }
    
    public void expr (){
    	
    	switch(look.tag) {
    		case '+':
			{
				match('+');
				match('(');
				int exprlist_n=exprlist(null);
				match(')');
				//inizio azione
				for(int i=0;i<exprlist_n-1;i++) {
					code.emit(OpCode.iadd);
				}
				//fine azione
				break;
			}
			case '*':
				match('*');
				match('(');
				int exprlist_n=exprlist(null);
				match(')');
				//inizio azione
				for(int i=0;i<exprlist_n-1;i++) {
					code.emit(OpCode.imul);
				}
				//fine azione
				break;
			
			case '/':
				match('/');
				expr();
				expr();
				//inizio azione
				code.emit(OpCode.idiv);
				//fine azione
				break;
			
			case '-':
				match('-');
				expr();
				expr();
				//inizio azione
				code.emit(OpCode.isub);
				//fine azione
				break;
			
			case Tag.NUM: //azione posta prima in confronto all'sdt perchè match consuma il token attuale
				//inizio azione
				code.emit(OpCode.ldc,((NumberTok)look).number);
				//fine azione
				match(Tag.NUM); 
				break;
				
			case Tag.ID:{

				int id_addr = st.lookupAddress(((Word)look).lexeme);	
				if(id_addr==-1)
					error(getErrorSyntaxMsg("expr"));
				//inizio azione
				code.emit(OpCode.iload,id_addr);
				//fine azione
				match(Tag.ID);
			}
				break;
				
	 		default:
	 			error(getErrorSyntaxMsg("expr"));
	 			break;
    	}
    }
       
    public int exprlist(Instruction[] exprlist_instr){

    	switch(look.tag) {
			case Tag.NUM:
			case Tag.ID:
			case '+':
			case '-':
			case '*':
			case '/':
				expr();
				if(exprlist_instr!=null)
					for(Instruction i: exprlist_instr)
						code.emit(i.opCode,i.operand);
				return 1+exprlistp(exprlist_instr);
		
			default:
	 			error(getErrorSyntaxMsg("exprlist"));
	 			break;
    	}
    	return -1; //non raggiungibile
    }
	
    public int exprlistp(Instruction[] exprlistp_instr){
    	
    	switch(look.tag) {
			case ',':
				match(',');
				expr();
				if(exprlistp_instr!=null)
					for(Instruction i: exprlistp_instr)
						code.emit(i.opCode,i.operand);
				return 1+exprlistp(exprlistp_instr);
			
			case ')':
				//epsilon
				return 0;
				
	 		default:
	 			error(getErrorSyntaxMsg("exprlistp"));
	 			break;
    	}
    	return -1; //non raggiungibile
    }
    
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "input.lft"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Translator translator = new Translator(lex, br);
            translator.start();
            
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}